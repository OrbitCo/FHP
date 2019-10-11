package com.fmcna.fhpckd.notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fmcna.fhpckd.beans.status.FailedResponse;
import com.fmcna.fhpckd.beans.status.FailedResponseDetail;
import com.fmcna.fhpckd.beans.status.JobStatus;

public abstract class NotificationMessage {

	private static DateFormat dateFormat = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");

	protected JobStatus jobStatus;

	public NotificationMessage(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public abstract String getSubject();

	public abstract String getBody();

	public String formatDate(Date date) {
		return dateFormat.format(date);
	}

	protected String getJobStatistics() {
		StringBuffer msgBody = new StringBuffer();
		msgBody.append("The details for the job are as follows:")
				.append("<ul>");

		msgBody.append("<li>Start time for the job : ").append(this.formatDate(this.jobStatus.getStartTime()))
				.append("</li>");
		msgBody.append("<li>Total count of updated members  : ").append(this.jobStatus.getTotalMemberCount())
				.append("</li>");
		msgBody.append("<li>Count of members submitted to FABRIC for update to HealthCloud : ")
				.append(this.jobStatus.getRequestCount()).append("</li>");
		msgBody.append("<li>Count of responses : ").append(this.jobStatus.getResponseCount()).append("</li>");

		if (CollectionUtils.isEmpty(this.jobStatus.getFailedResponseDetails())){
			msgBody.append("<li>Number of member record failures : none </li>");
		} else {
			msgBody.append("<li>Number of member record failures : ").append(this.jobStatus.getFailedResponseDetails().size())
					.append("</li>");
		}

		
		//-- here need to use new collection
		
		
		
		if (!CollectionUtils.isEmpty(this.jobStatus.getFailedResponseDetails())){
			List<FailedResponseDetail> failedResponseDetails = this.jobStatus.getFailedResponseDetails();
			msgBody.append("<p/>");
			msgBody.append("The following Members failed during upload to HealthCloud:");
			msgBody.append("<table border=\"1\"><tr><th>Member ID</th><th>Reason</th></tr>");

			for (FailedResponseDetail failedResponseDetail : failedResponseDetails) {
				msgBody.append("<tr><td>").append(failedResponseDetail.getFhpID()).append("</td><td>");

				if (failedResponseDetail.getFailureReason() != null) {
					msgBody.append(failedResponseDetail.getFailureReason());
				}

				msgBody.append("</td></tr>");
			}

			msgBody.append("</table>");

		}

		return msgBody.toString();
	}

}
