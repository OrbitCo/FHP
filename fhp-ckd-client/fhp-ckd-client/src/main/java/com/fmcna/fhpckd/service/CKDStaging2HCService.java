package com.fmcna.fhpckd.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fmcna.fhpckd.beans.staging.MemberMaster;
import com.fmcna.fhpckd.beans.staging.MemberResponse;
import com.fmcna.fhpckd.beans.staging.MemberStagingData;
import com.fmcna.fhpckd.beans.status.JobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.fabric.FabricClient;
import com.fmcna.fhpckd.model.FhpCkdRequest;
import com.fmcna.fhpckd.persistence.AuditService;
import com.fmcna.fhpckd.persistence.StatusService;
import com.fmcna.fhpckd.staging.StagingDAO;
import com.fmcna.fhpckd.util.DateUtil;

/**
 * The service class that initiates and control the batch jobs to transfer data from FHP Staging tables to HealthCloud.
 * 
 * @author manishtiwari
 *
 */
@Component
@RestController
public class  CKDStaging2HCService{

	private static final Logger logger = LoggerFactory.getLogger(CKDStaging2HCService.class);

	private static final int INC_BATCH_SIZE = 100;

	@Autowired
	private StatusService statusService;

	@Autowired
	private FabricClient client;
	
	@Autowired
	private AuditService auditService;

	@Autowired
	StagingDAO stagingDAO;
	
	@Autowired
	ErrorService errorService;

	/**
	 * The main method that will initiate the daily batch to transfer data from FHP Staging tables to HealthCloud.
	 * 
	 * @return
	 */

	@GetMapping ("/member/sendupdated")
	@Scheduled( fixedDelayString= "${fhptohc.schedule.interval}" )
	public boolean sendUpdatedMemberRecords() {

		logger.info("Started sendUpdatedMemberRecords()" );

		// Don't start the Job if the previous had partial error and all responses are not received.
		JobStatus lastJob = statusService.getLastJob();

		if ( lastJob != null && lastJob.isJobInProgress()) {

			logger.error("Previous pending job found " + lastJob.getId()   );
			return false;
		}
		
		Date lastMaxSrcUpdateTime = this.getLastMaxSrcUpdateTime();

		logger.info("Last Completed Job MaxSrcUpdateTime : {}", lastMaxSrcUpdateTime);
		logger.info("Completed sendUpdatedMemberRecords()" );

		return this.sendUpdatedMemberRecords(lastMaxSrcUpdateTime);
	}

	private boolean sendUpdatedMemberRecords( Date lastTimeStamp ) {

		int incCount = 0;
		int failureCount = 0;
		String jobId = null;
		boolean isSuccessful = false;

 		try {
			if( !this.hasNewStagingData())
				return true;
		} catch (ServiceException e) {
			return false;
		}


		JobStatus jobStatus = new JobStatus();

		jobStatus.setStartTime(new Date());
		jobStatus.setRequestStatusEnum(StateEnum.IN_PROGRESS);
		jobStatus.setResponseStatusEnum(StateEnum.AWAITING);
		
		statusService.createStatus(jobStatus);
		
		jobId = jobStatus.getId();

		List<Long> updatedMemberIDList = null;

		try{
			updatedMemberIDList = stagingDAO.getUpdatedMemberIds(lastTimeStamp); 
		}
		catch( ServiceException e) {
			String errorMessage = "Error fetching list of member IDs for update to HealthCloud";
			logger.error(errorMessage, e);
			jobStatus.setRequestStatusEnum(StateEnum.ABORTED);
			jobStatus.setErrorMessage(errorMessage);
			statusService.updateStatus(jobStatus);
			return false;
		} 

		logger.info("Number of Members identified for CKD to HC transfer is " + updatedMemberIDList.size() );

		Date currentMaxSrcTimestamp = this.stagingDAO.getMaxSrcUpdateDate();

		logger.info("Maximum SRC Update TimeStamp for curent for CKD to HC transfer is " + currentMaxSrcTimestamp );

		jobStatus.setTotalMemberCount(updatedMemberIDList.size());
		jobStatus.setMaxStagingUpdateTimestamp(currentMaxSrcTimestamp);

		statusService.updateStatus(jobStatus);

		logger.info("Initiating transfer of Member data from CKD to HC transfer.");

		// get Previously completed Job max time
		Date lastMaxSrcUpdateTime = this.getLastMaxSrcUpdateTime();

		for( Long memberID : updatedMemberIDList ) {
			try {
				if( this.sendMember( jobId, memberID.longValue(), lastMaxSrcUpdateTime)) {
					incCount++;
	
					if( incCount % INC_BATCH_SIZE == 0) {
						statusService.incrementRequestCount(INC_BATCH_SIZE);
						incCount = 0;
					}
				}
			}
			catch( Exception e) {
				statusService.addRequestFailedMemberID(memberID);
				failureCount++;
				logger.error("Error sending record to FABRIC for member ID " + memberID, e);
			}
		}

		statusService.incrementRequestCount(incCount);


		if( failureCount == 0 ) {
			statusService.updateRequestStatus(StateEnum.COMPLETE);

			logger.info("Completed transfer of FHP CKD Member data from Staging database to HC.");
			isSuccessful = true;
		} 
		else {
			statusService.updateRequestStatus(StateEnum.ERROR);
			isSuccessful = false;
		}
		
		this.clearHandshakeFlag();
		
		return isSuccessful;
	}

	/**
	 * This method will read and transfer one member record based on member ID.
	 * 
	 * @param memberId The FHP ID of the member record to be transferred.
	 * 
	 * @throws ServiceException
	 */
	@GetMapping ("/member/{memberId}/send")
	public boolean sendMember(String jobId, @PathVariable long memberId, Date lastMaxSrcUpdateTime) throws ServiceException {

		FabricMessageConvertor requestConvertor = new FabricMessageConvertor();

		try {

			MemberStagingData stagingData  = this.stagingDAO.getMemberStagingData(memberId, lastMaxSrcUpdateTime);

			if( this.validateMember(stagingData)) {
				FhpCkdRequest request = requestConvertor.generateFabricRequest(stagingData);
	
				logger.info("Sending request for FHP Member ID : " + memberId);
	
				client.sendRequest(request);
				
				auditService.auditRequestSent(jobId, request);
				return true;
			}
		} 
		catch( ServiceException e) {
			logger.error("Error sending Member data for member Id: " + memberId, e);
			throw e;
		}

		return false;
	}

	private boolean validateMember(MemberStagingData stagingData) {
		
		MemberMaster memberMaster = stagingData.getMemberMaster();
		
		if(  (memberMaster.getFirstName() == null || memberMaster.getFirstName().isEmpty())
			|| (memberMaster.getLastName() == null || memberMaster.getLastName().isEmpty())
			|| (memberMaster.getGender() == null || memberMaster.getGender().isEmpty())
			|| (memberMaster.getDateOfBirth() == null || memberMaster.getDateOfBirth().isEmpty())) {
			MemberResponse response = new MemberResponse();
			response.setId(memberMaster.getId());
			response.setSuccessfulHCUpsert(false);
			response.setFailureReason("Mandatory attributes for the member are missing. The first name, last name, gender and date of birth are mandatory.");
			
			errorService.processFailedResponse(response);
			
			return false;
		}
			
			
		
		return true;
	}

	private boolean hasNewStagingData() throws ServiceException{
		if( this.stagingDAO.getFHPDataReadiness() > 0 )
			return true;
		else
			return false;
	}
	
	private void clearHandshakeFlag(){
		try {
			this.stagingDAO.clearProcessHandshake();
		} catch (ServiceException e) {
			logger.error("Error while clearing the handshake flag");
		}
	}

	/**
	 * get LastCompleted Job Max Src Update time
	 * @return
	 */
	public Date getLastMaxSrcUpdateTime() {
		Date lastMaxSrcUpdateTime;
		JobStatus lastCompletedJob = statusService.getLastCompletedJob();

		if( lastCompletedJob != null && lastCompletedJob.getMaxStagingUpdateTimestamp() != null) {
			lastMaxSrcUpdateTime = lastCompletedJob.getMaxStagingUpdateTimestamp();
		} else {
			lastMaxSrcUpdateTime = DateUtil.getOldestRefDate();
		}

		return lastMaxSrcUpdateTime;
	}

}
