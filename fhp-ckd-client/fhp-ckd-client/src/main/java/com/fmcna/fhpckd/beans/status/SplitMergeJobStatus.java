package com.fmcna.fhpckd.beans.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fmcna.fhpckd.model.FhpSplitMergeRequest;

/**
 * This is a Java bean class that represents the status of a SplitMergeMember
 * batch job to process Member Split Or merges data from FHP staging tables and
 * send to HealthCloud.
 * 
 * @author vidhishanandhikonda
 *
 */
@Document
public class SplitMergeJobStatus {

	@Id
	private String id;

	private String type;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date startTime;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date endTime;

	private String requestStatus;

	private String responseStatus;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date maxStagingUpdateTimestamp;

	private long totalRequestCount;

	private long requestCount;

	private long responseCount;

	private List<FhpSplitMergeRequest> failedRequests;

	private String errorMessage;

	private List<FailedResponse> failedResponses;

	private boolean isNotificationSent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public StateEnum getRequestStatusEnum() {
		return StateEnum.fromValue(requestStatus);
	}

	public void setRequestStatusEnum(StateEnum requestStatusEnum) {
		this.requestStatus = requestStatusEnum.getState();
	}

	public StateEnum getResponseStatusEnum() {
		return StateEnum.fromValue(responseStatus);
	}

	public void setResponseStatusEnum(StateEnum responseStatusEnum) {
		this.responseStatus = responseStatusEnum.getState();
	}

	public Date getMaxStagingUpdateTimestamp() {
		return maxStagingUpdateTimestamp;
	}

	public void setMaxStagingUpdateTimestamp(Date maxStagingUpdateTimestamp) {
		this.maxStagingUpdateTimestamp = maxStagingUpdateTimestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}

	public long getResponseCount() {
		return responseCount;
	}

	public void setResponseCount(long responseCount) {
		this.responseCount = responseCount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<FhpSplitMergeRequest> getFailedRequests() {
		return failedRequests;
	}

	public void setFailedRequests(List<FhpSplitMergeRequest> failedRequests) {
		this.failedRequests = failedRequests;
	}

	public void addFailedRequests(FhpSplitMergeRequest fhpSplitMergeRequest) {
		if (this.failedRequests == null)
			this.failedRequests = new ArrayList<FhpSplitMergeRequest>();

		this.failedRequests.add(fhpSplitMergeRequest);
	}

	public List<FailedResponse> getFailedResponses() {
		return failedResponses;
	}

	public void setFailedResponses(List<FailedResponse> failedResponses) {
		this.failedResponses = failedResponses;
	}

	public boolean isNotificationSent() {
		return isNotificationSent;
	}

	public void setNotificationSent(boolean isNotificationSent) {
		this.isNotificationSent = isNotificationSent;
	}

	public long getTotalRequestCount() {
		return totalRequestCount;
	}

	public void setTotalRequestCount(long totalRequestCount) {
		this.totalRequestCount = totalRequestCount;
	}

	public boolean isJobInProgress() {

		boolean inProgress = true;

		if (this.getRequestStatusEnum() == StateEnum.COMPLETE && this.getResponseStatusEnum() == StateEnum.COMPLETE) {
			inProgress = false;
		} else if (this.getRequestStatusEnum() == StateEnum.ABORTED
				|| this.getResponseStatusEnum() == StateEnum.ABORTED) {
			inProgress = false;
		} else if (this.getRequestStatusEnum() == StateEnum.COMPLETE && this.getRequestCount() == 0) {
			inProgress = false;
		}

		return inProgress;
	}

}
