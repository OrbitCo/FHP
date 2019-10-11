package com.fmcna.fhpckd.notification;

import java.util.Date;

import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;

public class SplitMergeRequestErrorMessage extends SplitMergeNotificationMessage {

	public SplitMergeRequestErrorMessage(SplitMergeJobStatus splitMergeJobStatus) {
		super(splitMergeJobStatus);
	}

	@Override
	public String getSubject() {
		return "Failure - Processing of FHP Member " + this.splitMergeJobStatus.getType() + " data to HealthCloud";
	}

	@Override
	public String getBody() {
		StringBuffer msgBody = new StringBuffer();

		msgBody.append("<html><body>There is failure in transfer of ")
				.append(this.splitMergeJobStatus.getType())
				.append(" FHP-CKD members to HealthCloud detected at ")
				.append(this.formatDate(new Date()))
				.append(". There were errors while submitting the data to FABRIC and the timeout for retry has exceeded.<p/>")
				.append("<b>The job is now aborted and must be resubmitted.</b><p/>")
				.append(this.getJobStatistics())
				.append("</body></html>");

		return msgBody.toString();
	}

}
