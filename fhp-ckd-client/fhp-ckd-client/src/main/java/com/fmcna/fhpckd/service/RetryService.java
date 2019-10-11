package com.fmcna.fhpckd.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.fmcna.fhpckd.beans.failure.FailedResponseUpdate;
import com.fmcna.fhpckd.beans.failure.FailedSplitMergeResponseUpdate;
import com.fmcna.fhpckd.beans.status.JobStatus;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;
import com.fmcna.fhpckd.persistence.FailedResponseUpdatePersistence;
import com.fmcna.fhpckd.persistence.SplitMergeStatusService;
import com.fmcna.fhpckd.persistence.StatusService;

@Component
public class RetryService {

	private static final Logger logger = LoggerFactory.getLogger(RetryService.class);

	private final static int INC_BATCH_SIZE = 100;

	@Autowired
	StatusService statusService;

	@Autowired
	SplitMergeStatusService splitMergeStatusService;

	@Autowired
	FailedResponseUpdatePersistence failureService;

	@Autowired
	HCResponseService responseService;

	@Autowired
	CKDStaging2HCService requestService;

	@Autowired
	CKDStagingSplitMerge2HCService splitMergeRequestService;

	@GetMapping("/member/retrysenderror")
	@Scheduled(fixedDelayString = "${retry.schedule.interval}")
	public boolean retrySendErrors() {

		logger.info("Started retrySendErrors()");

		JobStatus lastJob = statusService.getLastJob();

		int incCount = 0, failureCount = 0;

		// get Previously completed Job max time
		Date lastMaxSrcUpdateTime = requestService.getLastMaxSrcUpdateTime();
		logger.info("Last Completed Job MaxSrcUpdateTime : {}", lastMaxSrcUpdateTime);

		if (lastJob != null && lastJob.getRequestStatusEnum() == StateEnum.ERROR) {
			List<Long> failedMemberRequests = lastJob.getFailedRequests();

			statusService.clearRequestFailures();
			statusService.updateRequestStatus(StateEnum.IN_PROGRESS);

			for (Long memberID : failedMemberRequests) {
				try {
					requestService.sendMember(lastJob.getId(), memberID.longValue(), lastMaxSrcUpdateTime);
					incCount++;

					if (incCount % INC_BATCH_SIZE == 0) {
						statusService.incrementRequestCount(INC_BATCH_SIZE);
						incCount = 0;
					}
				} catch (Exception e) {
					statusService.addRequestFailedMemberID(memberID);
					statusService.updateRequestStatus(StateEnum.ERROR, e.getMessage());
					failureCount++;
				}
			}

			statusService.incrementRequestCount(incCount);
			if (failureCount == 0) {
				statusService.updateRequestStatus(StateEnum.COMPLETE);

				logger.info("Completed transfer of Member data from CKD to HC transfer.");
				return true;
			} else {
				statusService.updateRequestStatus(StateEnum.ERROR);

				logger.info("Completed transfer of Member data from CKD to HC transfer.");
				return false;
			}

		}

		logger.info("Completed retrySendErrors()");
		return true;

	}

	@Scheduled(fixedDelayString = "${retry.schedule.interval}")
	public void reprocessFailedResponseUpdates() {

		List<FailedResponseUpdate> failedResponses = this.failureService.getFailedResponseUpdates();

		boolean result = false;

		if (failedResponses != null && !failedResponses.isEmpty()) {
			for (FailedResponseUpdate failedUpdate : failedResponses) {
				result = responseService.processResponse(failedUpdate.getResponse());

				if (result) {
					failureService.deleteFailedResponseUpdate(failedUpdate);
				}
			}
		}
	}

	// Split-Merge Retry

	@GetMapping("/member/splitmerge/retrysenderror")
	@Scheduled(fixedDelayString = "${retry.schedule.interval}")
	public boolean retrySplitMergeSendErrors() {

		logger.info("Started retrySplitMergeSendErrors()");

		SplitMergeJobStatus lastJobStatus;
		String jobType;
		int incCount = 0, failureCount = 0;
		for (SplitMergeEnum splitMergeEnum : SplitMergeEnum.values()) {
			jobType = splitMergeEnum.getType();

			if (!splitMergeEnum.equals(SplitMergeEnum.getDefault())) {
				lastJobStatus = splitMergeStatusService.getLastJob(jobType);

				if (lastJobStatus != null && lastJobStatus.getRequestStatusEnum() == StateEnum.ERROR) {
					List<FhpSplitMergeRequest> failedMemberRequests = lastJobStatus.getFailedRequests();
					String lastJobId = lastJobStatus.getId();
					splitMergeStatusService.clearRequestFailures(lastJobId);
					splitMergeStatusService.updateRequestStatus(lastJobId, StateEnum.IN_PROGRESS);

					for (FhpSplitMergeRequest fhpSplitMergeRequest : failedMemberRequests) {
						try {
							splitMergeRequestService.sendMemberSplitMerge(lastJobId, fhpSplitMergeRequest);
							incCount++;

							if (incCount % INC_BATCH_SIZE == 0) {
								splitMergeStatusService.incrementRequestCount(lastJobId, INC_BATCH_SIZE);
								incCount = 0;
							}
						} catch (Exception e) {
							splitMergeStatusService.addFailedRequest(lastJobId, fhpSplitMergeRequest);
							splitMergeStatusService.updateRequestStatus(lastJobId, StateEnum.ERROR, e.getMessage());
							failureCount++;
						}
					}

					splitMergeStatusService.incrementRequestCount(lastJobId, incCount);
					if (failureCount == 0) {
						splitMergeStatusService.updateRequestStatus(lastJobId, StateEnum.COMPLETE);

						logger.info("Completed transfer of {} Member data from CKD to HC.", lastJobStatus.getType());
						return true;
					} else {
						splitMergeStatusService.updateRequestStatus(lastJobId, StateEnum.ERROR);

						logger.info("Error while transferring {} Member data from CKD to HC.", lastJobStatus.getType());
						return false;
					}
				}
			}
		}

		logger.info("Completed retrySplitMergeSendErrors()");
		return true;
	}

	@Scheduled(fixedDelayString = "${retry.schedule.interval}")
	public void reprocessSplitMergeFailedResponseUpdates() {
		logger.info("Started reprocessSplitMergeFailedResponseUpdates()");
		List<FailedSplitMergeResponseUpdate> failedResponses = this.failureService.getSplitMergeFailedResponseUpdates();

		boolean result = false;

		if (failedResponses != null && !failedResponses.isEmpty()) {
			for (FailedSplitMergeResponseUpdate failedUpdate : failedResponses) {
				result = responseService.processResponse(failedUpdate.getResponse());

				if (result) {
					failureService.deleteFailedResponseUpdate(failedUpdate);
				}
			}
		}
		
		logger.info("Completed reprocessSplitMergeFailedResponseUpdates()");
	}

}
