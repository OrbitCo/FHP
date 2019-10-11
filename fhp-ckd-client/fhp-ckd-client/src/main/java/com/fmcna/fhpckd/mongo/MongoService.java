package com.fmcna.fhpckd.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.audit.AuditRecord;
import com.fmcna.fhpckd.beans.failure.FailedResponseUpdate;
import com.fmcna.fhpckd.beans.status.FailedResponse;
import com.fmcna.fhpckd.beans.status.FailedResponseDetail;
import com.fmcna.fhpckd.beans.status.JobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;

@Component
public class MongoService {

	@Autowired
	public MongoTemplate mongoTemplate;
	
	@Value("${mongo.BatchStatus.collection}")
	private String jobStatusCollectionName;
	
	@Value("${mongo.FailedResponseUpdates.collection}")
	private String failedResponseCollectionName;
	
	@Value("${mongo.FailedResponseDetails.collection}")
	private String failedResponseDetailsCollectionName;

	@Value("${mongo.Audit.collection}")
	private String auditCollectionName;
	
	private static Logger logger = LoggerFactory.getLogger(MongoService.class);

	public void createStatus(JobStatus status) {
		
		try {
			mongoTemplate.insert(status, jobStatusCollectionName);

			logger.info("Record Created in Mongo for JobStatus for Id  : " + status.getId());
		} 
		catch (Exception e) {
			logger.error( "Exception while creating JobStatus record in mongo for id " + status.getId());
		}
	}
	
	public void updateStatus(JobStatus status) {
		try {
			mongoTemplate.save(status, jobStatusCollectionName);
			
			logger.info("Record updated in Mongo for JobStatus for Id  : " + status.getId());
		} 
		catch (Exception e) {
			logger.error( "Exception while creating JobStatus record in mongo for id " + status.getId());
		}
	}
	
	public void addFailedRequest( String jobId, long memberId ) {
		try {
			Query query = new Query();
			Update update = new Update();
			
			query.addCriteria(Criteria.where("_id").is(jobId));
			
			update.addToSet(JobStatusConstants.ATTR_FAILED_REQUESTS, new Long(memberId));
			
			this.mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);
			
		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for last JobStatus in mongo : " + e.getMessage());
		}
		
	}
	
	public  List<FailedResponseDetail> getFailedResponseDetails(String jobId){
		Query query=new Query();
		query.addCriteria(Criteria.where("jobID").is(jobId));
		List<FailedResponseDetail> failedResponseDetails = mongoTemplate.find(query, FailedResponseDetail.class, failedResponseDetailsCollectionName);
        return failedResponseDetails;
	}
	
	// This is a test
	
	public void clearFailedRequests( String jobId ) {
		try {
			Query query = new Query();
			Update update = new Update();
			
			query.addCriteria(Criteria.where("_id").is(jobId));
			
			update.unset(JobStatusConstants.ATTR_FAILED_REQUESTS);
			
			this.mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);
			
		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for last JobStatus in mongo : " + e.getMessage());
		}
		
	}
	
	
	public void addFailedResponse( String jobId, FailedResponse response, int incCount ) {
		try {
			
			logger.info("SAVE RESPONSE ****** " );
			
			Query query = new Query();
			Update update = new Update();
			
			
			
			
			query.addCriteria(Criteria.where("_id").is(jobId));
			
		
			JobStatus jobStatus = mongoTemplate.findOne(query, JobStatus.class, jobStatusCollectionName);
			
			List<FailedResponse> currentList = null;
			
			
			
			FailedResponseDetail fr=new FailedResponseDetail();
			
			fr.setJobID(jobStatus.getId());
			
			fr.setFhpID(response.getFhpID());
			
			fr.setFailureReason(response.getFailureReason());
			
			fr.setMemberID(response.getMemberID());
			
			
			jobStatus.getFailedResponseDetails().add(fr);
			
			
			//response.setFailureReason("");
			//response.setMemberID("");
			
			this.mongoTemplate.save(fr,failedResponseDetailsCollectionName);
			
			List<FailedResponseDetail> ld = new  ArrayList<FailedResponseDetail>();
			
			ld = mongoTemplate.find(query, FailedResponseDetail.class, jobStatusCollectionName);
			
			
			
			jobStatus.setFailedResponseDetails(ld);
	
			
			if( jobStatus.getFailedResponses() != null ) {
				currentList = jobStatus.getFailedResponses().stream()
							.filter(failedResponse -> failedResponse.getFhpID().equals(response.getFhpID()))
							.collect(Collectors.toList());
				
			}
				
			if( currentList != null && !currentList.isEmpty() ) {
			
				update.pull(JobStatusConstants.ATTR_FAILED_RESPONSES, currentList.get(0));

				this.mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);
				
				update = new Update();
				update.addToSet(JobStatusConstants.ATTR_FAILED_RESPONSES, response );
			}
			else {
			
				update.addToSet(JobStatusConstants.ATTR_FAILED_RESPONSES, response );
				update.inc(JobStatusConstants.ATTR_RESPONSE_COUNT, new Integer (incCount));
			}
			
			
			// --query.fields().exclude("failedResponses.failureReason");
			
			
			this.mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);
			
					
			
			
		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for last JobStatus in mongo : " + e.getMessage());
		}
		
	}
	
	public void updateRequestStatus(String jobId, StateEnum status) {
		try {
			
			this.updateRequestStatus(jobId, status, null);

		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for last JobStatus in mongo : " + e.getMessage());
		}
	}

	public void updateRequestStatus(String id, StateEnum status, String message) {
		try {
			
			Query query = new Query();
			Update update = new Update();
			
			query.addCriteria(Criteria.where("_id").is(id));
			
			update.set(JobStatusConstants.ATTR_REQUEST_STATUS, status.getState());
			if( message != null)
				update.set(JobStatusConstants.ATTR_ERROR_MSG, message );
			
			mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);
		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for JobStatus in mongo : " + e.getMessage());
		}
	}

	public void updateResponseStatus(String jobId, StateEnum status) {
		try {
			
			Query query = new Query();
			Update update = new Update();
			
			query.addCriteria(Criteria.where("_id").is(jobId));
			
			update.set(JobStatusConstants.ATTR_RESPONSE_STATUS, status.getState());
			
			mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);
		} catch (Exception e) {
			logger.error("Error Occured while updating responseStatus for JobStatus in mongo : " + e.getMessage());
		}
	}

	public void incrementRequestCount(String jobId, int additionalCount) {
		try {
			
			Query query = new Query();
			Update update = new Update();
			
			query.addCriteria(Criteria.where("_id").is(jobId));
			
			update.inc(JobStatusConstants.ATTR_REQUEST_COUNT, new Integer (additionalCount));
			
			mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while incrementing requestCount for last JobStatus in mongo : " + e.getMessage());
		}

	}
	
	public void incrementResponseCount(String jobId, int additionalCount) {
		try {
			
			Query query = new Query();
			Update update = new Update();
			
			query.addCriteria(Criteria.where("_id").is(jobId));
			
			update.inc(JobStatusConstants.ATTR_RESPONSE_COUNT, new Integer (additionalCount));
			
			mongoTemplate.updateFirst(query, update, JobStatus.class, jobStatusCollectionName);
		} catch (Exception e) {
			logger.error("Error Occured while incrementing responseCount for last JobStatus in mongo : " + e.getMessage());
		}

	}
	
	public JobStatus getLastCompletedJob() {
		JobStatus jobStatus = null ;
		try {
			Query query = new Query();
		
			query.addCriteria(
					Criteria.where(JobStatusConstants.ATTR_REQUEST_STATUS).is(StateEnum.COMPLETE.getState()).
					and(JobStatusConstants.ATTR_RESPONSE_STATUS).is(StateEnum.COMPLETE.getState()));
			query.with(new Sort( Sort.Direction.DESC , JobStatusConstants.ATTR_START_TIME));
	
			jobStatus = mongoTemplate.findOne(query, JobStatus.class, jobStatusCollectionName);
			if(jobStatus != null) {
			jobStatus.setFailedResponseDetails(getFailedResponseDetails(jobStatus.getId()));
			}
		} catch (Exception e) {
			logger.error("Error Occured while getting last completed JobStatus from mongo : " + e.getMessage());
		}
		return jobStatus;
	}
	
	public JobStatus getLastJob() {
		JobStatus jobStatus = null ;
		try {
			Query query = new Query();
		
			query.with(new Sort( Sort.Direction.DESC , JobStatusConstants.ATTR_START_TIME));
	
			jobStatus = mongoTemplate.findOne(query, JobStatus.class, jobStatusCollectionName);
			
			if(jobStatus != null) {
			jobStatus.setFailedResponseDetails(getFailedResponseDetails(jobStatus.getId()));
			}
		} catch (Exception e) {
			logger.error("Error Occured while getting last JobStatus from mongo : " + e.getMessage());
		}
		return jobStatus;
	}
	
	public List<FailedResponseUpdate> getFailedResponseUpdates(){
		
		List<FailedResponseUpdate> failedResponseUpdates = null;
		
		try {
			failedResponseUpdates = mongoTemplate.findAll(FailedResponseUpdate.class, this.failedResponseCollectionName);
	    } 
		catch (Exception e) {
			logger.error("Error Occured while getting last FailedResponseUpdates from mongo : " + e.getMessage());
		}
		
		return failedResponseUpdates;
	}

	public void addFailedResponseUpdate(FailedResponseUpdate failedResponseUpdate){
		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("fhpId").is(failedResponseUpdate.getFhpId()));
			
			if( !mongoTemplate.exists(query, FailedResponseUpdate.class, this.failedResponseCollectionName) ) {
				mongoTemplate.insert(failedResponseUpdate, this.failedResponseCollectionName);
			}
	    } 
		catch (Exception e) {
			logger.error("Error Occured while iserting to FailedResponseUpdate to mongo : " + e.getMessage());
		}
	}

	public void deleteFailedResponseUpdate(FailedResponseUpdate failedResponseUpdate){
		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(failedResponseUpdate.getId()));

			mongoTemplate.findAllAndRemove(query, this.failedResponseCollectionName);
		    } 
		catch (Exception e) {
			logger.error("Error Occured while deleting from FailedResponseUpdate in mongo : " + e.getMessage());
		}
	}

	public void addAuditRecord(AuditRecord auditRecord) {
		try {
			mongoTemplate.insert(auditRecord, auditCollectionName);

			logger.info("Record Created in Mongo for AuditRecord for Id : {} : {}", auditRecord.getId(), auditRecord.getMessage());
		} 
		catch (Exception e) {
			logger.error( "Exception while creating AuditRecord record in mongo for id : {} : {} ", auditRecord.getId(), auditRecord.getMessage());
		}
			
	}

	
}
