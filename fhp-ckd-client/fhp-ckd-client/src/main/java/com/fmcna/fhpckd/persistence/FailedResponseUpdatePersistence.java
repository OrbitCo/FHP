package com.fmcna.fhpckd.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fmcna.fhpckd.beans.failure.FailedResponseUpdate;
import com.fmcna.fhpckd.beans.failure.FailedSplitMergeResponseUpdate;
import com.fmcna.fhpckd.mongo.MongoService;
import com.fmcna.fhpckd.mongo.SplitMergeMongoService;

@Component
public class FailedResponseUpdatePersistence {

	@Autowired
	MongoService mongoService;

	@Autowired
	SplitMergeMongoService splitMergeMongoService;

	public List<FailedResponseUpdate> getFailedResponseUpdates() {
		return mongoService.getFailedResponseUpdates();
	}

	public void addFailedResponseUpdate(FailedResponseUpdate failedResponseUpdate) {
		mongoService.addFailedResponseUpdate(failedResponseUpdate);
	}

	public void deleteFailedResponseUpdate(FailedResponseUpdate failedResponseUpdate) {
		mongoService.deleteFailedResponseUpdate(failedResponseUpdate);
	}

	public List<FailedSplitMergeResponseUpdate> getSplitMergeFailedResponseUpdates() {
		return splitMergeMongoService.getFailedResponseUpdates();
	}

	public void addFailedResponseUpdate(FailedSplitMergeResponseUpdate failedResponseUpdate) {
		splitMergeMongoService.addFailedResponseUpdate(failedResponseUpdate);
	}

	public void deleteFailedResponseUpdate(FailedSplitMergeResponseUpdate failedResponseUpdate) {
		splitMergeMongoService.deleteFailedResponseUpdate(failedResponseUpdate);
	}
}
