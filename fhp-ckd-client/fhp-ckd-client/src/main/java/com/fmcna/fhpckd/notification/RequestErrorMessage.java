package com.fmcna.fhpckd.notification;


import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.status.JobStatus;

public class RequestErrorMessage extends NotificationMessage {

	public RequestErrorMessage(JobStatus jobStatus) {
		super(jobStatus);
	}

	@Override
	public String getSubject() {
		return "Failure - transfer of FHP Member data to HealthCloud";
	}

	@Override
	public String getBody() {
		StringBuffer msgBody = new StringBuffer();

		msgBody.append("<html><body>There is failure in transfer of updated FHP-CKD members to HealthCloud detected at ")
		.append( this.formatDate(new Date()))
		.append(". There were errors while submitting the data to FABRIC and the timeout for retry has exceeded.<p/>");
		msgBody.append("<b>The job is now aborted and must be resubmitted.</b><p/>");
		msgBody.append(this.getJobStatistics());

		msgBody.append("</body></html>");

		return msgBody.toString();
	}

}
