package com.fmcna.fhpckd.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.status.JobStatus;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.notification.NotificationService;
import com.fmcna.fhpckd.persistence.SplitMergeStatusService;
import com.fmcna.fhpckd.persistence.StatusService;

@Component
public class MonitoringService {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);


	@Autowired
	private StatusService statusService;

	@Autowired
	private SplitMergeStatusService splitMergeStatusService;

	@Autowired
	private NotificationService notificationService;
	
	//Request error time out in minutes
	@Value("${request.error.timeout}")
	private long requestErrorTimeout;
	
	//Response time out in minutes
	@Value("${response.timeout}")
	private long responseTimeout;
	
	//Response time out in minutes
	@Value("${response.error.timeout}")
	private long responseErrorTimeout;

	@Scheduled(fixedRateString="${monitoring.service.schedule.interval}")
	public void monitorFailures() {
		
		logger.info("Member Monitoring Started.");
		
		long startTime;
		
		JobStatus lastJobStatus = statusService.getLastJob();
		
		
		if( lastJobStatus == null )
			return;
		
		// First check for successful completion
		if( lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE && 
				lastJobStatus.getResponseStatusEnum() == StateEnum.COMPLETE) {
			startTime = lastJobStatus.getStartTime().getTime();
			
			if( ((System.currentTimeMillis() - startTime) > (responseTimeout * 60000))
					&& (lastJobStatus.isNotificationSent() == false ) ) {
	
				logger.info("All responses for all members has been received. Sending notification");

				notificationService.sendJobCompletionNotification(lastJobStatus);
				lastJobStatus.setNotificationSent(true);
				statusService.updateStatus(lastJobStatus);
				
			}
			
		}
		
		// Check for Request errors
		else if( lastJobStatus.getRequestStatusEnum() == StateEnum.ERROR) {
			startTime = lastJobStatus.getStartTime().getTime();
			
			if( (System.currentTimeMillis() - startTime) > (requestErrorTimeout * 60000)) {
				logger.info("Timeout for retry of request errors has exceeded. Aborting the job");
				
				lastJobStatus.setRequestStatusEnum(StateEnum.ABORTED);
				lastJobStatus.setResponseStatusEnum(StateEnum.ABORTED);
				lastJobStatus.setNotificationSent(true);
				statusService.updateStatus(lastJobStatus);
				
				notificationService.sendRequestErrorNotification(lastJobStatus);
			}
			
		}
		// Check for delays in response from FABRIC
		else if( lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE && 
				lastJobStatus.getResponseStatusEnum() != StateEnum.COMPLETE &&
				lastJobStatus.getResponseStatusEnum() != StateEnum.ABORTED) {
			startTime = lastJobStatus.getStartTime().getTime();
			
			if (lastJobStatus.getRequestCount() == 0) {
				lastJobStatus.setResponseStatusEnum(StateEnum.COMPLETE);
				lastJobStatus.setEndTime(new Date());
				statusService.updateStatus(lastJobStatus);
			}
			else if( (System.currentTimeMillis() - startTime) > (responseTimeout * 60000)) {
	
				logger.info("Timeout for response for all members has exceeded. Aborting the job");

				lastJobStatus.setResponseStatusEnum(StateEnum.ABORTED);
				lastJobStatus.setNotificationSent(true);
				statusService.updateStatus(lastJobStatus);
				
				notificationService.sendResponseTimeoutNotification(lastJobStatus);
			}
			
		}
		// Response errors
		else if( lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE && 
				lastJobStatus.getResponseStatusEnum() == StateEnum.ERROR) {
			startTime = lastJobStatus.getStartTime().getTime();
			
			if( (System.currentTimeMillis() - startTime) > (responseErrorTimeout * 60000)) {
	
				logger.info("Timeout for response for all members has exceeded. Aborting the job");
				lastJobStatus.setResponseStatusEnum(StateEnum.ABORTED);
				lastJobStatus.setNotificationSent(true);
				statusService.updateStatus(lastJobStatus);

				notificationService.sendErrorResponseTimeoutNotification(lastJobStatus);
			}
			
		}

		// If job is struck with status, make the necessary changes to the job so the next process can kick-off.
		else if (lastJobStatus.getRequestStatusEnum() == StateEnum.IN_PROGRESS
				|| lastJobStatus.getResponseStatusEnum() == StateEnum.AWAITING) {
			startTime = lastJobStatus.getStartTime().getTime();

			if ((System.currentTimeMillis() - startTime) > (responseErrorTimeout * 60000)) {
				logger.info("Job Request status {} and Response status {} for more than the the 2 hrs window ", StateEnum.IN_PROGRESS, StateEnum.AWAITING);

				if (lastJobStatus.getRequestCount() != lastJobStatus.getResponseCount()) {
					logger.info("Timeout for response for all members has exceeded and the status is not properly updated. Aborting the job");
					lastJobStatus.setResponseStatusEnum(StateEnum.ABORTED);
					lastJobStatus.setRequestStatusEnum(StateEnum.COMPLETE);
					lastJobStatus.setNotificationSent(true);
					statusService.updateStatus(lastJobStatus);
					notificationService.sendErrorResponseTimeoutNotification(lastJobStatus);

				} else if (lastJobStatus.getRequestCount() == lastJobStatus.getResponseCount()) {
					logger.info("Job status is not updated properly, marking it as completed job.");
					lastJobStatus.setResponseStatusEnum(StateEnum.COMPLETE);
					lastJobStatus.setRequestStatusEnum(StateEnum.COMPLETE);
					lastJobStatus.setNotificationSent(true);
					statusService.updateStatus(lastJobStatus);
					notificationService.sendJobCompletionNotification(lastJobStatus);
				}

			}
		}

		logger.info("Completed monitorFailures.");
	}

	@Scheduled(fixedRateString="${monitoring.service.schedule.interval}")
	public void splitMergeMonitorFailures() {
		logger.info("Started splitMergeMonitorFailures.");

		long startTime;
		String jobType;
		SplitMergeJobStatus lastJobStatus;

		for (SplitMergeEnum splitMergeEnum : SplitMergeEnum.values()) {
			jobType = splitMergeEnum.getType();

			if (!splitMergeEnum.equals(SplitMergeEnum.getDefault())) {
				logger.info("Monitoring Started for JobType : {}", jobType);

				lastJobStatus = splitMergeStatusService.getLastJob(jobType);

				if (lastJobStatus != null) {

					// First check for successful completion
					if (lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE
							&& lastJobStatus.getResponseStatusEnum() == StateEnum.COMPLETE) {
						startTime = lastJobStatus.getStartTime().getTime();

						if (((System.currentTimeMillis() - startTime) > (responseTimeout * 60000))
								&& (lastJobStatus.isNotificationSent() == false)) {
							logger.info("All responses for all members {} requests has been received. Sending notification.", jobType);

							notificationService.sendJobCompletionNotification(lastJobStatus);

							lastJobStatus.setNotificationSent(true);
							splitMergeStatusService.updateStatus(lastJobStatus);
						}

					}

					// Check for Request errors
					else if (lastJobStatus.getRequestStatusEnum() == StateEnum.ERROR) {
						startTime = lastJobStatus.getStartTime().getTime();

						if ((System.currentTimeMillis() - startTime) > (requestErrorTimeout * 60000)) {
							logger.info("Timeout for retry of {} request errors has exceeded. Aborting the job.", jobType);

							notificationService.sendRequestErrorNotification(lastJobStatus);

							lastJobStatus.setRequestStatusEnum(StateEnum.ABORTED);
							lastJobStatus.setNotificationSent(true);
							splitMergeStatusService.updateStatus(lastJobStatus);
						}

					}

					// Check for delays in response from FABRIC
					else if (lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE
							&& lastJobStatus.getResponseStatusEnum() != StateEnum.COMPLETE
							&& lastJobStatus.getResponseStatusEnum() != StateEnum.ABORTED) {
						startTime = lastJobStatus.getStartTime().getTime();

						if (lastJobStatus.getRequestCount() == 0) {
							lastJobStatus.setResponseStatusEnum(StateEnum.COMPLETE);
							lastJobStatus.setEndTime(new Date());
							splitMergeStatusService.updateStatus(lastJobStatus);
						}
						else if ((System.currentTimeMillis() - startTime) > (responseTimeout * 60000)) {
							logger.info("Timeout for response for all members {} requests has exceeded. Aborting the job.", jobType);

							notificationService.sendResponseTimeoutNotification(lastJobStatus);

							lastJobStatus.setResponseStatusEnum(StateEnum.ABORTED);
							lastJobStatus.setNotificationSent(true);
							splitMergeStatusService.updateStatus(lastJobStatus);
						}

					}

					// Response errors
					else if (lastJobStatus.getRequestStatusEnum() == StateEnum.COMPLETE
							&& lastJobStatus.getResponseStatusEnum() == StateEnum.ERROR) {
						startTime = lastJobStatus.getStartTime().getTime();

						if ((System.currentTimeMillis() - startTime) > (responseErrorTimeout * 60000)) {
							logger.info("Timeout for response of all members {} requests has exceeded. Aborting the job.", jobType);

							notificationService.sendErrorResponseTimeoutNotification(lastJobStatus);

							lastJobStatus.setResponseStatusEnum(StateEnum.ABORTED);
							lastJobStatus.setNotificationSent(true);
							splitMergeStatusService.updateStatus(lastJobStatus);
						}
					}

				}
			}
		}

		logger.info("Completed splitMergeMonitorFailures.");
	}

}