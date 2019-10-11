package com.fmcna.fhpckd.mongo;

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

import com.fmcna.fhpckd.beans.failure.FailedSplitMergeResponseUpdate;
import com.fmcna.fhpckd.beans.status.FailedResponse;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.beans.status.SplitMergeJobStatus;
import com.fmcna.fhpckd.beans.status.StateEnum;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;

/**
 * Mongo service to save SplitMergeMember JobStatus and Audit data
 * 
 * @author vidhishanandhikonda
 *
 */
@Component
public class SplitMergeMongoService {

	private static Logger logger = LoggerFactory.getLogger(SplitMergeMongoService.class);

	@Autowired
	public MongoTemplate mongoTemplate;

	@Value("${mongo.SplitMerge.BatchStatus.collection}")
	private String splitMergeJobStatusCollectionName;

	@Value("${mongo.SplitMerge.FailedResponseUpdates.collection}")
	private String splitMergeFailedResponseCollectionName;

	public void createStatus(SplitMergeJobStatus status) {

		try {
			mongoTemplate.insert(status, splitMergeJobStatusCollectionName);

			logger.info("Record Created in Mongo for JobStatus for Id  : {} ", status.getId());

		} catch (Exception e) {
			logger.error("Exception while creating SplitOrMerge JobStatus record in mongo for id : {} ",
					status.getId());

		}
	}

	public SplitMergeJobStatus getLastJob(String jobType) {
		SplitMergeJobStatus jobStatus = null;
		String jobTypeVal = SplitMergeEnum.fromValue(jobType).getType();

		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("type").is(jobTypeVal));
			query.with(new Sort(Sort.Direction.DESC, JobStatusConstants.ATTR_START_TIME));

			jobStatus = mongoTemplate.findOne(query, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while getting last {} JobStatus from mongo : {}", jobType, e.getMessage());

		}

		return jobStatus;
	}

	public SplitMergeJobStatus getLastCompletedJob(String jobType) {
		SplitMergeJobStatus jobStatus = null;

		String jobTypeVal = SplitMergeEnum.fromValue(jobType).getType();
		try {

			Query query = new Query();

			query.addCriteria(Criteria.where(JobStatusConstants.ATTR_REQUEST_STATUS).is(StateEnum.COMPLETE.getState())
					.and(JobStatusConstants.ATTR_RESPONSE_STATUS).is(StateEnum.COMPLETE.getState()).and("type")
					.is(jobTypeVal));
			query.with(new Sort(Sort.Direction.DESC, JobStatusConstants.ATTR_START_TIME));

			jobStatus = mongoTemplate.findOne(query, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while getting last completed {} JobStatus from mongo : {}", jobType,
					e.getMessage());

		}

		return jobStatus;
	}

	public void updateStatus(SplitMergeJobStatus status) {
		try {
			mongoTemplate.save(status, splitMergeJobStatusCollectionName);

			logger.info("Record updated in Mongo for SplitMergeJobStatus for Id  : {}", status.getId());

		} catch (Exception e) {
			logger.error("Exception while creating SplitMergeJobStatus record in mongo for id : {}", status.getId());

		}
	}

	public void incrementRequestCount(String jobId, int additionalCount) {
		try {
			Query query = new Query();
			Update update = new Update();

			query.addCriteria(Criteria.where("_id").is(jobId));

			update.inc(JobStatusConstants.ATTR_REQUEST_COUNT, new Integer(additionalCount));

			mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while incrementing requestCount for last SplitMergeJobStatus in mongo : {}",
					e.getMessage());
		}

	}

	public void addFailedRequest(String jobId, FhpSplitMergeRequest fhpSplitMergeRequest) {
		try {
			Query query = new Query();
			Update update = new Update();

			query.addCriteria(Criteria.where("_id").is(jobId));

			update.addToSet(JobStatusConstants.ATTR_FAILED_REQUESTS, fhpSplitMergeRequest);

			this.mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for last SplitMergeJobStatus in mongo : {}",
					e.getMessage());
		}

	}

	public void updateRequestStatus(String jobId, StateEnum status) {
		try {
			this.updateRequestStatus(jobId, status, null);

		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for last SplitMergeJobStatus in mongo : {} ",
					e.getMessage());

		}
	}

	public void updateRequestStatus(String id, StateEnum status, String message) {
		try {
			Query query = new Query();
			Update update = new Update();

			query.addCriteria(Criteria.where("_id").is(id));

			update.set(JobStatusConstants.ATTR_REQUEST_STATUS, status.getState());
			if (message != null) {
				update.set(JobStatusConstants.ATTR_ERROR_MSG, message);
			}

			mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for SplitMergeJobStatus in mongo : {}",
					e.getMessage());

		}
	}

	public void addFailedResponse(String jobId, FailedResponse response, int incCount) {
		try {

			Query query = new Query();
			Update update = new Update();

			query.addCriteria(Criteria.where("_id").is(jobId));

			SplitMergeJobStatus jobStatus = mongoTemplate.findOne(query, SplitMergeJobStatus.class,
					splitMergeJobStatusCollectionName);

			List<FailedResponse> currentList = null;

			if (jobStatus.getFailedResponses() != null) {
				currentList = jobStatus.getFailedResponses().stream()
						.filter(failedResponse -> failedResponse.getFhpID().equals(response.getFhpID()))
						.collect(Collectors.toList());
			}

			if (currentList != null && !currentList.isEmpty()) {
				update.pull(JobStatusConstants.ATTR_FAILED_RESPONSES, currentList.get(0));

				this.mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class,
						splitMergeJobStatusCollectionName);

				update = new Update();
				update.addToSet(JobStatusConstants.ATTR_FAILED_RESPONSES, response);
			} else {
				update.addToSet(JobStatusConstants.ATTR_FAILED_RESPONSES, response);
				update.inc(JobStatusConstants.ATTR_RESPONSE_COUNT, new Integer(incCount));
			}

			this.mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while updating responseStatus for last SplitMerge JobStatus in mongo : "
					+ e.getMessage());
		}

	}

	public void incrementResponseCount(String jobId, int additionalCount) {
		try {

			Query query = new Query();
			Update update = new Update();

			query.addCriteria(Criteria.where("_id").is(jobId));

			update.inc(JobStatusConstants.ATTR_RESPONSE_COUNT, new Integer(additionalCount));

			mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);
		} catch (Exception e) {
			logger.error("Error Occured while incrementing responseCount for last SplitMerge JobStatus in mongo : "
					+ e.getMessage());
		}

	}

	public List<FailedSplitMergeResponseUpdate> getFailedResponseUpdates() {

		List<FailedSplitMergeResponseUpdate> failedResponseUpdates = null;

		try {
			failedResponseUpdates = mongoTemplate.findAll(FailedSplitMergeResponseUpdate.class,
					this.splitMergeFailedResponseCollectionName);
		} catch (Exception e) {
			logger.error("Error Occured while getting last Split - Merge FailedResponseUpdates from mongo : {}",
					e.getMessage());
		}

		return failedResponseUpdates;
	}

	public void addFailedResponseUpdate(FailedSplitMergeResponseUpdate failedResponseUpdate) {

		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("fhpId").is(failedResponseUpdate.getFhpId()));

			if (!mongoTemplate.exists(query, FailedSplitMergeResponseUpdate.class,
					this.splitMergeFailedResponseCollectionName)) {
				mongoTemplate.insert(failedResponseUpdate, this.splitMergeFailedResponseCollectionName);
			}
		} catch (Exception e) {
			logger.error("Error Occured while inserting to Split - Merge FailedResponseUpdate to mongo : {}",
					e.getMessage());
		}
	}

	public void deleteFailedResponseUpdate(FailedSplitMergeResponseUpdate failedResponseUpdate) {

		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(failedResponseUpdate.getId()));

			mongoTemplate.findAllAndRemove(query, this.splitMergeFailedResponseCollectionName);
		} catch (Exception e) {
			logger.error("Error Occured while deleting from Split - Merge FailedResponseUpdate in mongo : {}",
					e.getMessage());
		}
	}

	public void updateResponseStatus(String jobId, StateEnum status) {
		try {

			Query query = new Query();
			Update update = new Update();

			query.addCriteria(Criteria.where("_id").is(jobId));

			update.set(JobStatusConstants.ATTR_RESPONSE_STATUS, status.getState());

			mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);
		} catch (Exception e) {
			logger.error("Error Occured while updating responseStatus for Split-Merge JobStatus in mongo : {}",
					e.getMessage());
		}
	}

	public void clearFailedRequests(String jobId) {
		try {
			Query query = new Query();
			Update update = new Update();

			query.addCriteria(Criteria.where("_id").is(jobId));

			update.unset(JobStatusConstants.ATTR_FAILED_REQUESTS);

			this.mongoTemplate.updateFirst(query, update, SplitMergeJobStatus.class, splitMergeJobStatusCollectionName);

		} catch (Exception e) {
			logger.error("Error Occured while updating requestStatus for last JobStatus in mongo : " + e.getMessage());
		}

	}
}
