package com.fmcna.fhpckd.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.status.FailedResponse;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;
import com.fmcna.fhpckd.mongo.SplitMergeMongoService;

@Component
public class SplitMergeStatusService {

	@Autowired
	private SplitMergeMongoService splitMergeMongoService;

	public void createStatus(SplitMergeJobStatus status) {
		splitMergeMongoService.createStatus(status);
	}

	public SplitMergeJobStatus getLastJob(String jobType) {
		SplitMergeJobStatus jobStatus = null;
		jobStatus = splitMergeMongoService.getLastJob(jobType);
		return jobStatus;
	}

	public SplitMergeJobStatus getLastCompletedJob(String jobType) {
		SplitMergeJobStatus jobStatus = null;
		jobStatus = splitMergeMongoService.getLastCompletedJob(jobType);
		return jobStatus;
	}

	public void updateStatus(SplitMergeJobStatus status) {
		splitMergeMongoService.updateStatus(status);
	}

	public void incrementRequestCount(String jobId, int additionalCount) {
		splitMergeMongoService.incrementRequestCount(jobId, additionalCount);
	}

	public void addFailedRequest(String jobId, FhpSplitMergeRequest fhpSplitMergeRequest) {
		splitMergeMongoService.addFailedRequest(jobId, fhpSplitMergeRequest);
	}

	public void updateRequestStatus(String jobId, StateEnum status) {
		this.updateRequestStatus(jobId, status, null);
	}

	public void updateRequestStatus(String id, StateEnum status, String message) {
		splitMergeMongoService.updateRequestStatus(id, status, message);
	}

	public void addFailedResponse(FailedResponse response, String jobType) {
		this.addFailedResponse(response, true, jobType);
	}

	public void addFailedResponse(FailedResponse response, boolean incrementResponseCount, String jobType) {
		SplitMergeJobStatus lastJob = this.getLastJob(jobType);

		int incCount = incrementResponseCount ? 1 : 0;

		splitMergeMongoService.addFailedResponse(lastJob.getId(), response, incCount);
	}

	public void incrementResponseCount(int additionalCount, String jobType) {
		SplitMergeJobStatus lastJob = this.getLastJob(jobType);
		if (lastJob != null) {
			splitMergeMongoService.incrementResponseCount(lastJob.getId(), additionalCount);
		}
	}

	public void updateResponseStatus(StateEnum status, String jobType) {
		SplitMergeJobStatus lastJob = this.getLastJob(jobType);
		this.updateResponseStatus(lastJob.getId(), status);
	}

	public void updateResponseStatus(String jobId, StateEnum status) {
		splitMergeMongoService.updateResponseStatus(jobId, status);
	}

	public void clearRequestFailures(String jobId) {
		splitMergeMongoService.clearFailedRequests(jobId);
	}

}
