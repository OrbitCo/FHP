package com.fmcna.fhpckd.notification;

import com.fmcna.fhpckd.beans.status.JobStatus;

public class SuccessfulJobMessage extends NotificationMessage {

	public SuccessfulJobMessage(JobStatus jobStatus) {
		super(jobStatus);
	}

	@Override
	public String getSubject() {
		return "Completed - Transfer of FHP Member data to HealthCloud";
	}

	@Override
	public String getBody() {
		StringBuffer msgBody = new StringBuffer();

		msgBody.append("<html><body>The transfer of updated FHP-CKD members to HealthCloud completed at ")
				.append(this.formatDate(this.jobStatus.getEndTime())).append("<p/>");

		msgBody.append(this.getJobStatistics());

		msgBody.append("</body></html>");

		return msgBody.toString();
	}

}
