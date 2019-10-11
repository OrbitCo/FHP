package com.fmcna.fhpckd.notification;

import java.util.Date;

import com.fmcna.fhpckd.beans.status.JobStatus;

public class ResponseTimeoutMessage extends NotificationMessage {

	public ResponseTimeoutMessage(JobStatus jobStatus) {
		super(jobStatus);
	}

	@Override
	public String getSubject() {
		return "Failure - transfer of FHP Member data to HealthCloud";
	}

	@Override
	public String getBody() {
		StringBuffer msgBody = new StringBuffer();

		msgBody.append(
				"<html><body>There is failure in transfer of updated FHP-CKD members to HealthCloud detected at ")
				.append(this.formatDate(new Date()))
				.append(". The response for all member updates were not received and the timeout has exceeded.<p/>");
		msgBody.append("<b>The job is now aborted and must be resubmitted.</b><p/>");

		msgBody.append(this.getJobStatistics());

		msgBody.append("</body></html>");

		return msgBody.toString();
	}

}
