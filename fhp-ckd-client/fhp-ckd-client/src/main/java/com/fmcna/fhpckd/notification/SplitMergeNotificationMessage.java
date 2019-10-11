package com.fmcna.fhpckd.notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fmcna.fhpckd.beans.status.FailedResponse;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;

public abstract class SplitMergeNotificationMessage {
	private static DateFormat dateFormat = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");

	
	protected SplitMergeJobStatus splitMergeJobStatus;

	public SplitMergeNotificationMessage(SplitMergeJobStatus jobStatus) {
		this.splitMergeJobStatus = jobStatus;
	}
	public String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	public abstract String getSubject();

	public abstract String getBody();

	protected String getJobStatistics() {
		String jobType = this.splitMergeJobStatus.getType();

		StringBuffer msgBody = new StringBuffer();

		msgBody.append("The details for the job are as follows:").append("<ul>");
		msgBody.append("<li>Job Type : ").append(jobType).append("</li>");
		msgBody.append("<li>Start time for the job : ")
				.append(this.formatDate(this.splitMergeJobStatus.getStartTime())).append("</li>");
		msgBody.append("<li>Total count of updated ").append(jobType).append(" members  : ")
				.append(this.splitMergeJobStatus.getRequestCount()).append("</li>");
		msgBody.append("<li>Count of members submitted to FABRIC for update to HealthCloud : ")
				.append(this.splitMergeJobStatus.getRequestCount()).append("</li>");

		if (this.splitMergeJobStatus.getFailedRequests() == null
				|| this.splitMergeJobStatus.getFailedRequests().isEmpty()) {
			msgBody.append("<li>Number of member record failed requests : none </li>");
		} else {
			msgBody.append("<li>Number of member record failed requests : ")
					.append(this.splitMergeJobStatus.getFailedResponses().size()).append("</li>");
		}
		msgBody.append("<li>Count of responses : ").append(this.splitMergeJobStatus.getResponseCount()).append("</li>");
		
		if (this.splitMergeJobStatus.getFailedResponses() == null
				|| this.splitMergeJobStatus.getFailedResponses().isEmpty()) {
			msgBody.append("<li>Number of member record failures : none </li>");
		} else {
			msgBody.append("<li>Number of member record failures : ")
					.append(this.splitMergeJobStatus.getFailedResponses().size()).append("</li>");
		}
		
		if (this.splitMergeJobStatus.getFailedResponses() != null && !this.splitMergeJobStatus.getFailedResponses().isEmpty()) {
			List<FailedResponse> failedResponses = this.splitMergeJobStatus.getFailedResponses();
			msgBody.append("<p/>");
			msgBody.append("The following Members failed during ").append(jobType)
					.append(" processing in HealthCloud:");
			msgBody.append("<table border=\"1\"><tr><th>FHP ID</th><th>Reason</th></tr>");

			for (FailedResponse failedResponse : failedResponses) {
				msgBody.append("<tr><td>").append(failedResponse.getFhpID()).append("</td><td>");

				if (failedResponse.getFailureReason() != null) {
					msgBody.append(failedResponse.getFailureReason());
				}

				msgBody.append("</td></tr>");
			}

			msgBody.append("</table>");

		}

		return msgBody.toString();
	}

}
