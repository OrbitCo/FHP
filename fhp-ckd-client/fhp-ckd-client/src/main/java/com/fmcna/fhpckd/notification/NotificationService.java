package com.fmcna.fhpckd.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmcna.fhpckd.beans.status.JobStatus;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;

@Service
public class NotificationService {

	@Autowired
	EmailService emailService;

	public void sendJobCompletionNotification(JobStatus jobStatus) {

		NotificationMessage message = new SuccessfulJobMessage(jobStatus);
		emailService.sendEmail(message.getSubject(), message.getBody());
	}

	public void sendRequestErrorNotification(JobStatus jobStatus) {

		NotificationMessage message = new RequestErrorMessage(jobStatus);
		emailService.sendErrorEmail(message.getSubject(), message.getBody());
	}

	public void sendResponseTimeoutNotification(JobStatus jobStatus) {

		NotificationMessage message = new ResponseTimeoutMessage(jobStatus);
		emailService.sendErrorEmail(message.getSubject(), message.getBody());
	}

	public void sendErrorResponseTimeoutNotification(JobStatus lastJobStatus) {

		NotificationMessage message = new ResponseErrorTimeoutMessage(lastJobStatus);
		emailService.sendErrorEmail(message.getSubject(), message.getBody());
	}

	public void sendJobCompletionNotification(SplitMergeJobStatus splitMergeJobStatus) {

		SplitMergeNotificationMessage message = new SplitMergeSuccessfulJobMessage(splitMergeJobStatus);
		emailService.sendEmail(message.getSubject(), message.getBody());
	}

	public void sendRequestErrorNotification(SplitMergeJobStatus splitMergeJobStatus) {

		SplitMergeNotificationMessage message = new SplitMergeRequestErrorMessage(splitMergeJobStatus);
		emailService.sendErrorEmail(message.getSubject(), message.getBody());
	}

	public void sendResponseTimeoutNotification(SplitMergeJobStatus splitMergeJobStatus) {

		SplitMergeNotificationMessage message = new SplitMergeResponseTimeoutMessage(splitMergeJobStatus);
		emailService.sendErrorEmail(message.getSubject(), message.getBody());
	}

	public void sendErrorResponseTimeoutNotification(SplitMergeJobStatus lastJobStatus) {

		SplitMergeNotificationMessage message = new SplitMergeResponseErrorTimeoutMessage(lastJobStatus);
		emailService.sendErrorEmail(message.getSubject(), message.getBody());
	}

}
