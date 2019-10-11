package com.fmcna.fhpckd.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.status.FailedResponse;
import com.fmcna.fhpckd.beans.status.JobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.mongo.MongoService;

@Component
public class StatusService {

	@Autowired
	private MongoService mongoService;
	
	private static Logger logger = LoggerFactory.getLogger(StatusService.class);

	public void createStatus(JobStatus status) {
		mongoService.createStatus(status);
	}
	
	public void updateStatus(JobStatus status) {
		mongoService.updateStatus(status);
	}
	
	public void updateRequestStatus(StateEnum status) {
		JobStatus lastJob = this.getLastJob();
		this.updateRequestStatus(lastJob.getId(), status, null);
	}

	public void updateRequestStatus(StateEnum status, String message) {
		JobStatus lastJob = this.getLastJob();	
		this.updateRequestStatus(lastJob.getId(), status, message);
	}

	public void updateRequestStatus(String jobID, StateEnum status) {
		this.updateRequestStatus(jobID, status, null);
	}

	public void updateRequestStatus(String id, StateEnum status, String message) {
		mongoService.updateRequestStatus(id, status, message);
	}

	public void updateResponseStatus(StateEnum status) {
		JobStatus lastJob = this.getLastJob();
		this.updateResponseStatus(lastJob.getId(), status);;
	}

	public void updateResponseStatus(String id, StateEnum status) {
		mongoService.updateResponseStatus(id, status);;
	}

	public void addRequestFailedMemberID( long memberId ) {
			JobStatus lastJob = this.getLastJob();
			mongoService.addFailedRequest(lastJob.getId(), memberId);
	}
	
	public void clearRequestFailures() {
		JobStatus lastJob = this.getLastJob();
		mongoService.clearFailedRequests(lastJob.getId());
	}

	public void addFailedResponse( FailedResponse response, boolean incrementResponseCount ) {
			JobStatus lastJob = this.getLastJob();
			
			int incCount = incrementResponseCount == true ? 1: 0;
			
			mongoService.addFailedResponse(lastJob.getId(), response, incCount);
	}
	
	public void addFailedResponse( FailedResponse response ) {
		this.addFailedResponse(response, true);
	}

	public void incrementRequestCount(int additionalCount) {
		JobStatus lastJob = this.getLastJob();
		mongoService.incrementRequestCount(lastJob.getId(), additionalCount);	
	}
	
	public void incrementResponseCount(int additionalCount) {
		JobStatus lastJob = this.getLastJob();
		mongoService.incrementResponseCount(lastJob.getId(), additionalCount);	
	} 
	
	public JobStatus getLastCompletedJob() {
		JobStatus jobStatus = null ;
		jobStatus = mongoService.getLastCompletedJob();
		return jobStatus;
	}
	
	public JobStatus getLastJob() {
		JobStatus jobStatus = null ;
		jobStatus = mongoService.getLastJob();	
		return jobStatus;
	}
	
}
