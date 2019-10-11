package com.fmcna.fhpckd.notification;


import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.status.JobStatus;

public class ResponseErrorTimeoutMessage extends NotificationMessage {

	public ResponseErrorTimeoutMessage(JobStatus jobStatus) {
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
		.append(". There are errors updating the response message to Staging tables. The timeout for automatic retry has exceeded.")
		.append(" Please check the process. ") ;
		msgBody.append(" <b>No new job will be submitted till the issue is resolved.</b><p/>");

		
		msgBody.append(this.getJobStatistics());

		msgBody.append("</body></html>");
		
		return msgBody.toString();
	}

}
