package com.fmcna.fhpckd.notification;


import java.util.Date;

import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;

public class SplitMergeResponseErrorTimeoutMessage extends SplitMergeNotificationMessage {

	public SplitMergeResponseErrorTimeoutMessage(SplitMergeJobStatus splitMergeJobStatus) {
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
			   .append( this.formatDate(new Date()))
			   .append(". There are errors updating the response message to Staging tables. The timeout for automatic retry has exceeded.")
			   .append(" Please check the process.")
			   .append(" <b>No new job will be submitted till the issue is resolved.</b><p/>")
			   .append(this.getJobStatistics())
			   .append("</body></html>");
		
		return msgBody.toString();
	}

}
