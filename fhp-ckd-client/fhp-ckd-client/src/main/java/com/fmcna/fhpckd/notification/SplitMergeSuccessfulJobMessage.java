package com.fmcna.fhpckd.notification;

import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;

public class SplitMergeSuccessfulJobMessage extends SplitMergeNotificationMessage {

	public SplitMergeSuccessfulJobMessage(SplitMergeJobStatus splitMergeJobStatus) {
		super(splitMergeJobStatus);
	}

	@Override
	public String getSubject() {
		return "Completed - Processing of FHP Member " + this.splitMergeJobStatus.getType() + " data to HealthCloud";
	}

	@Override
	public String getBody() {
		StringBuffer msgBody = new StringBuffer();

		msgBody.append("<html><body>The processing of FHP-CKD ")
			   .append(this.splitMergeJobStatus.getType())
			   .append(" members in HealthCloud completed at ")
			   .append(this.formatDate(this.splitMergeJobStatus.getEndTime()))
			   .append("<p/>")
			   .append(this.getJobStatistics())
			   .append("</body></html>");

		return msgBody.toString();
	}

}
