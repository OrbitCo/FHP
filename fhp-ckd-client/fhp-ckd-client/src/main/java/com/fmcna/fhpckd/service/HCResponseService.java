package com.fmcna.fhpckd.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmcna.fhpckd.beans.failure.FailedResponseUpdate;
import com.fmcna.fhpckd.beans.failure.FailedSplitMergeResponseUpdate;
import com.fmcna.fhpckd.beans.staging.MemberResponse;
import com.fmcna.fhpckd.beans.staging.MemberSplitMergeResponse;
import com.fmcna.fhpckd.beans.status.JobStatus;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.model.FhpCkdResponse;
import com.fmcna.fhpckd.model.FhpSplitMergeResponse;
import com.fmcna.fhpckd.persistence.FailedResponseUpdatePersistence;
import com.fmcna.fhpckd.persistence.SplitMergeService;
import com.fmcna.fhpckd.persistence.SplitMergeStatusService;
import com.fmcna.fhpckd.persistence.StatusService;
import com.fmcna.fhpckd.staging.StagingDAO;

@Service
public class HCResponseService {

	private static final Logger logger = LoggerFactory.getLogger(HCResponseService.class);

	@Autowired
	private StatusService statusService;

	@Autowired
	private StagingDAO stagingDAO;

	@Autowired
	private FailedResponseUpdatePersistence failureService;
	
	@Autowired
	private ErrorService errorService;
	
	@Autowired
	private SplitMergeStatusService splitMergeStatusService;

	@Autowired
	private SplitMergeService splitMergeService;
	
	public boolean processResponse( FhpCkdResponse response ) {
		logger.debug("Method called to process a response from the queue");

		FabricMessageConvertor messageConvertor = new FabricMessageConvertor();

		MemberResponse memberResponse = messageConvertor.parseFabricResponse(response);
		
		// Push failure in Mongo record
		if( !memberResponse.isSuccessfulHCUpsert()) {
			statusService.addFailedResponse(errorService.getBusinessError(memberResponse));
			logger.info("Response from FABRIC indicates failure during upsert to HealthCloud for member " + memberResponse.getId());
		} else {
			statusService.incrementResponseCount(1);
		}
		
		try {
			stagingDAO.updateMemberResponse(memberResponse);
			stagingDAO.populateMemberLoadAudit(messageConvertor.mapMemberLoadAudit(response));
			checkResponseCompletion();
			
			
		}
		catch( ServiceException e ) {
			logger.error("Error updating response status for member ID { " + memberResponse.getId() + "} to Staging Tables. Saving to Mongo for retry.", e);
			this.addResponseUpdateFailure(response);
			statusService.updateResponseStatus(StateEnum.ERROR);
			return false;
		}
		return true;
	}

	private void checkResponseCompletion() {
		JobStatus lastJobStatus = statusService.getLastJob();

		if( lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE && 
				lastJobStatus.getResponseStatusEnum() != StateEnum.ABORTED &&
				lastJobStatus.getResponseCount() >= lastJobStatus.getRequestCount()) {
			logger.info("Job is completed. All responses have been received.");

			lastJobStatus.setResponseStatusEnum(StateEnum.COMPLETE);
			lastJobStatus.setEndTime(new Date());
			statusService.updateStatus(lastJobStatus);
		}
		else if( lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE 
				&&  lastJobStatus.getResponseStatusEnum() == StateEnum.AWAITING ) {
			lastJobStatus.setResponseStatusEnum(StateEnum.IN_PROGRESS);
			statusService.updateStatus(lastJobStatus);
		}
		statusService.updateStatus(lastJobStatus);
	}


	private void addResponseUpdateFailure(FhpCkdResponse response) {
		FailedResponseUpdate failedUpdate = new FailedResponseUpdate();

		failedUpdate.setResponseTime(new Date());
		failedUpdate.setFhpId(response.getFhpId());

		failedUpdate.setResponse(response);

		this.failureService.addFailedResponseUpdate(failedUpdate);
	}

	public boolean processResponse(FhpSplitMergeResponse response) {
		FabricMessageConvertor messageConvertor = new FabricMessageConvertor();

		MemberSplitMergeResponse memberSplitMergeResponse = messageConvertor.parseFabricResponse(response);
		
		String jobType = memberSplitMergeResponse.getType();

		logger.debug("Method called to process a {} repsonse from the queue", response.getType());
		
		if (memberSplitMergeResponse.isSuccess()) {
			splitMergeStatusService.incrementResponseCount(1, jobType);
		} else {			
			splitMergeStatusService.addFailedResponse(errorService.getFailedResponse(memberSplitMergeResponse), jobType);
			logger.info("Response from FABRIC indicates failure during {} processing in HealthCloud of member {} ", jobType, memberSplitMergeResponse.getFhpId());
		}

		try {		
			splitMergeService.updateSplitMergeResponse(jobType, memberSplitMergeResponse);
			checkResponseCompletion(jobType);

		} catch( ServiceException e) {
			logger.error("Error updating {} response status for member ID {} to Staging Tables. Saving to Mongo for retry.", jobType, response.getFhpId(), e);
		
			this.addResponseUpdateFailure(response);
			splitMergeStatusService.updateResponseStatus(StateEnum.ERROR, jobType);
			return false;
		}
		
		return true;
	}

	private void checkResponseCompletion(String jobType) {
		SplitMergeJobStatus lastJobStatus = splitMergeStatusService.getLastJob(jobType);
		if (lastJobStatus != null) {
			if (lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE
					&& lastJobStatus.getResponseStatusEnum() != StateEnum.ABORTED
					&& lastJobStatus.getResponseCount() >= lastJobStatus.getRequestCount()) {
				logger.info("Job is completed. All responses have been received.");
	
				lastJobStatus.setResponseStatusEnum(StateEnum.COMPLETE);
				lastJobStatus.setEndTime(new Date());
				splitMergeStatusService.updateStatus(lastJobStatus);
	
			} else if (lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE
					&& lastJobStatus.getResponseStatusEnum() == StateEnum.AWAITING) {
				logger.info("Job is in progress. Waiting for responses.");

				lastJobStatus.setResponseStatusEnum(StateEnum.IN_PROGRESS);
				splitMergeStatusService.updateStatus(lastJobStatus);
	
			}
		}

	}
	
	private void addResponseUpdateFailure(FhpSplitMergeResponse response) {
		FailedSplitMergeResponseUpdate failedUpdate = new FailedSplitMergeResponseUpdate();

		failedUpdate.setResponseTime(new Date());
		failedUpdate.setFhpId(response.getFhpId());
		failedUpdate.setResponse(response);

		this.failureService.addFailedResponseUpdate(failedUpdate);
	}

}
