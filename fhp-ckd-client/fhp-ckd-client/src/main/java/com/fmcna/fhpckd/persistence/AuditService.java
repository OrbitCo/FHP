package com.fmcna.fhpckd.persistence;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmcna.fhpckd.beans.audit.AuditRecord;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.model.FhpCkdRequest;
import com.fmcna.fhpckd.model.FhpCkdResponse;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;
import com.fmcna.fhpckd.model.FhpSplitMergeResponse;
import com.fmcna.fhpckd.mongo.MongoService;

@Service
public class AuditService {

	private final String MESSAGE_TYPE_FHP_MEMBER_REQUEST = "FHP Member Update FABRIC Request";
	private final String MESSAGE_TYPE_FHP_MEMBER_RESPONSE = "FHP Member Update FABRIC Response";
	private final String MESSAGE_TYPE_REQUEST_SENT = "SENT";
	private final String MESSAGE_TYPE_RESPONSE_RECEIVED = "RECEIVED";

	private final String MESSAGE_TYPE_SPLIT_MEMBER_REQUEST = "FHP Member Split FABRIC Request";
	private final String MESSAGE_TYPE_SPLIT_MEMBER_RESPONSE = "FHP Member Split FABRIC Response";

	private final String MESSAGE_TYPE_MERGE_MEMBER_REQUEST = "FHP Member Merge FABRIC Request";
	private final String MESSAGE_TYPE_MERGE_MEMBER_RESPONSE = "FHP Member Merge FABRIC Response";

	@Autowired
	private MongoService mongoService;

	public void auditRequestSent(String jobID, FhpCkdRequest request) {

		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setJobId(jobID);
		auditRecord.setMessageType(MESSAGE_TYPE_FHP_MEMBER_REQUEST);
		auditRecord.setAuditTime(new Date());
		auditRecord.setStatus(MESSAGE_TYPE_REQUEST_SENT);
		auditRecord.setSourceId(request.getFhpId());
		auditRecord.setMessage((Object) request);

		mongoService.addAuditRecord(auditRecord);
	}

	public void auditResponse(FhpCkdResponse response) {
		this.auditResponse(null, response);
	}

	public void auditResponse(String jobID, FhpCkdResponse response) {

		AuditRecord auditRecord = new AuditRecord();

		auditRecord.setJobId(jobID);
		auditRecord.setMessageType(MESSAGE_TYPE_FHP_MEMBER_RESPONSE);
		auditRecord.setAuditTime(new Date());
		auditRecord.setStatus(MESSAGE_TYPE_RESPONSE_RECEIVED);
		auditRecord.setSourceId(response.getFhpId());
		auditRecord.setMessage((Object) response);

		mongoService.addAuditRecord(auditRecord);
	}

	public void auditRequestSent(String jobID, FhpSplitMergeRequest request) {

		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setJobId(jobID);
		auditRecord.setStatus(MESSAGE_TYPE_REQUEST_SENT);
		auditRecord.setAuditTime(new Date());

		if (SplitMergeEnum.SPLIT.getType().equals(request.getType())) {
			auditRecord.setMessageType(this.MESSAGE_TYPE_SPLIT_MEMBER_REQUEST);
			auditRecord.setSourceId(request.getRowIdOriginal());
		} else {
			auditRecord.setMessageType(this.MESSAGE_TYPE_MERGE_MEMBER_REQUEST);
			auditRecord.setSourceId(request.getRowIdPrimary());
		}
		auditRecord.setMessage((Object) request);

		mongoService.addAuditRecord(auditRecord);
	}

	public void auditResponse(FhpSplitMergeResponse response) {

		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setAuditTime(new Date());
		auditRecord.setStatus(MESSAGE_TYPE_RESPONSE_RECEIVED);
		auditRecord.setSourceId(response.getFhpId());
		auditRecord.setMessage((Object) response);

		if (SplitMergeEnum.SPLIT.getType().equals(response.getType())) {
			auditRecord.setMessageType(this.MESSAGE_TYPE_SPLIT_MEMBER_RESPONSE);
		} else {
			auditRecord.setMessageType(this.MESSAGE_TYPE_MERGE_MEMBER_RESPONSE);
		}

		mongoService.addAuditRecord(auditRecord);
	}

}