package com.fmcna.fhpckd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmcna.fhpckd.beans.staging.MemberMerge;
import com.fmcna.fhpckd.beans.staging.MemberSplit;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.fabric.FabricClient;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;
import com.fmcna.fhpckd.persistence.AuditService;
import com.fmcna.fhpckd.persistence.SplitMergeService;
import com.fmcna.fhpckd.persistence.SplitMergeStatusService;
import com.fmcna.fhpckd.util.DateUtil;

/**
 * The service class that initiates and control the batch jobs to process
 * Memeber Split and Merges data from FHP Staging tables to HealthCloud.
 * 
 * @author vidhishanandhikonda
 *
 */
@Component
@RestController
public class CKDStagingSplitMerge2HCService {

	private static final Logger logger = LoggerFactory.getLogger(CKDStagingSplitMerge2HCService.class);

	private static final int INC_BATCH_SIZE = 100;

	@Autowired
	private AuditService auditService;

	@Autowired
	private SplitMergeStatusService splitMergeStatusService;

	@Autowired
	private SplitMergeService splitMergeService;

	@Autowired
	private FabricClient client;

	@GetMapping("/member/splitmerge/send")
	@Scheduled(fixedDelayString = "${fhptohc.splitmerge.schedule.interval}")
	public boolean sendSplitOrMergeMemberRecords() {
		logger.info("Started sendSplitMergeMemberRecords()");

		boolean splitStatus = this.processSplitMergeByType(SplitMergeEnum.SPLIT.getType());
		boolean mergeStatus = this.processSplitMergeByType(SplitMergeEnum.MERGE.getType()); 
		
		logger.info("Completed sendSplitMergeMemberRecords()");
		return splitStatus && mergeStatus;
	}

	private boolean processSplitMergeByType(String jobType) {
		Date lastMaxSrcUpdateTime;
		// Don't start the Job if the previous had partial error and all responses are
		// not received.
		SplitMergeJobStatus lastJob = splitMergeStatusService.getLastJob(jobType);

		if (lastJob != null && lastJob.isJobInProgress()) {
			logger.error("Previous pending job found with Job Id {} for type : {} ", lastJob.getId(), jobType);
			return false;
		}

		SplitMergeJobStatus lastCompletedJob = splitMergeStatusService.getLastCompletedJob(jobType);

		if (lastCompletedJob != null && lastCompletedJob.getMaxStagingUpdateTimestamp() != null) {
			lastMaxSrcUpdateTime = lastCompletedJob.getMaxStagingUpdateTimestamp();
		} else {
			lastMaxSrcUpdateTime = DateUtil.getOldestRefDate();
		}

		return this.sendMemberSplitMergeRecords(lastMaxSrcUpdateTime, jobType);
	}

	private boolean sendMemberSplitMergeRecords(Date lastTimeStamp, String jobType) {
		logger.info("Processing Started for jobType : {}", jobType);
		int incCount = 0;
		int failureCount = 0;
		String jobId = null;
		boolean isSuccessful = false;

		try {
			if (!splitMergeService.hasNewStagingData(jobType)) {
				logger.info("No new staging data flagged in handshake table for jobType : {}", jobType);
				return true;
			}
		} catch (ServiceException e) {
			return false;
		}

		SplitMergeJobStatus jobStatus = new SplitMergeJobStatus();

		jobStatus.setStartTime(new Date());
		jobStatus.setRequestStatusEnum(StateEnum.IN_PROGRESS);
		jobStatus.setResponseStatusEnum(StateEnum.AWAITING);
		jobStatus.setType(jobType);
		splitMergeStatusService.createStatus(jobStatus);

		jobId = jobStatus.getId();

		List<FhpSplitMergeRequest> fhpSplitOrMergeRequestList = null;

		try {
			fhpSplitOrMergeRequestList = this.getMemberSplitMergeRequests(lastTimeStamp, jobType);

		} catch (ServiceException e) {
			String errorMessage = "Error fetching list of " + jobType + " member Records for update to HealthCloud";
			logger.error(errorMessage, e);
			jobStatus.setRequestStatusEnum(StateEnum.ABORTED);
			jobStatus.setErrorMessage(errorMessage);
			splitMergeStatusService.updateStatus(jobStatus);

			return false;
		}

		logger.info("Number of Members identified for {} is {}", jobType, fhpSplitOrMergeRequestList.size());

		Date currentMaxSrcTimestamp = this.splitMergeService.getMaxSrcUpdateDate();

		logger.info("Maximum SRC Update TimeStamp for curent {} member is {} ", jobType, currentMaxSrcTimestamp);

		jobStatus.setMaxStagingUpdateTimestamp(currentMaxSrcTimestamp);

		long totalRequestsCount = fhpSplitOrMergeRequestList.size();
		jobStatus.setTotalRequestCount(totalRequestsCount);

		splitMergeStatusService.updateStatus(jobStatus);

		logger.info("Initiating processing of Member {} records from Staging database to HC.", jobType);

		for (FhpSplitMergeRequest fhpSplitMergeRequest : fhpSplitOrMergeRequestList) {

			try {
				if (this.sendMemberSplitMerge(jobId, fhpSplitMergeRequest)) {
					incCount++;

					if (incCount % INC_BATCH_SIZE == 0) {
						splitMergeStatusService.incrementRequestCount(jobId, INC_BATCH_SIZE);
						incCount = 0;
					}
				}

			} catch (Exception e) {
				splitMergeStatusService.addFailedRequest(jobId, fhpSplitMergeRequest);
				failureCount++;
				logger.error("Error sending {} record to FABRIC with request {} is {} ", jobType, fhpSplitMergeRequest, e);
			}
		}

		splitMergeStatusService.incrementRequestCount(jobId, incCount);

		if (failureCount == 0) {
			splitMergeStatusService.updateRequestStatus(jobId, StateEnum.COMPLETE);

			logger.info("Completed sending {} Member data from Staging database to HC.", jobType);
			isSuccessful = true;
		} else {
			splitMergeStatusService.updateRequestStatus(jobId, StateEnum.ERROR);
			isSuccessful = false;
		}

		splitMergeService.clearHandshakeFlag(jobType);

		logger.info("Processing Completed for jobType : {}", jobType);

		return isSuccessful;
	}

	private List<FhpSplitMergeRequest> getMemberSplitMergeRequests(Date lastTimeStamp, String jobType)
			throws ServiceException {

		if (SplitMergeEnum.SPLIT.getType().equals(jobType)) {
			return this.getMemberSplitRequests(lastTimeStamp);
		} else {
			return this.getMemberMergeRequests(lastTimeStamp);
		}

	}

	private List<FhpSplitMergeRequest> getMemberSplitRequests(Date lastTimeStamp) throws ServiceException {
		FabricMessageConvertor fabricRequestConvertor = new FabricMessageConvertor();

		List<FhpSplitMergeRequest> fhpSplitRequestsList = new ArrayList<>();
		List<MemberSplit> memberSplitList = this.splitMergeService.getMemberSplitRequests(lastTimeStamp);

		for (MemberSplit memberSplit : memberSplitList) {
			fhpSplitRequestsList.add(fabricRequestConvertor.generateFabricSplitRequest(memberSplit));
		}

		return fhpSplitRequestsList;
	}

	private List<FhpSplitMergeRequest> getMemberMergeRequests(Date lastTimeStamp) throws ServiceException {
		FabricMessageConvertor fabricRequestConvertor = new FabricMessageConvertor();

		List<FhpSplitMergeRequest> fhpMergeRequestsList = new ArrayList<>();
		List<MemberMerge> memberMergeList = this.splitMergeService.getMemberMergeRequests(lastTimeStamp);

		for (MemberMerge memberMerge : memberMergeList) {
			fhpMergeRequestsList.add(fabricRequestConvertor.generateFabricMergeRequest(memberMerge));
		}

		return fhpMergeRequestsList;

	}

	// process individual request
	public boolean sendMemberSplitMerge(String jobId, FhpSplitMergeRequest fhpSplitMergeRequest) throws ServiceException {
		String jobType = fhpSplitMergeRequest.getType();
		logger.info("Sending {} request to fabric.", jobType);

		try {
			client.sendSplitMergeRequest(fhpSplitMergeRequest);

			auditService.auditRequestSent(jobId, fhpSplitMergeRequest);	
			return true;

		} catch (ServiceException e) {
			logger.error("Error sending {} data to Fabric : {}", jobType, e);
			throw e;
		}
	}

}
