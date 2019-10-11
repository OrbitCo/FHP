package com.fmcna.fhpckd.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmcna.fhpckd.beans.staging.MemberLoadAudit;
import com.fmcna.fhpckd.beans.staging.MemberMaster;
import com.fmcna.fhpckd.beans.staging.MemberResponse;
import com.fmcna.fhpckd.beans.staging.MemberSplitMergeResponse;
import com.fmcna.fhpckd.beans.status.FailedResponse;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.persistence.StatusService;
import com.fmcna.fhpckd.staging.StagingDAO;

@Service
public class ErrorService {

	private static final Logger logger = LoggerFactory.getLogger(ErrorService.class);

	
	@Autowired
	private StatusService statusService;

	@Autowired
	private StagingDAO stagingDAO;

	public void processFailedResponse( MemberResponse response ) {
		statusService.addFailedResponse(this.getBusinessError(response), false);

		try {
			stagingDAO.updateMemberResponse(response);

			MemberLoadAudit memberLoadAudit = new MemberLoadAudit();
			memberLoadAudit.setSfhcLoadStatus("N");
			memberLoadAudit.setSfhcLoadDttm(new Date());
			memberLoadAudit.setSrcMemberId(response.getId());
			memberLoadAudit.setSfhcLoadComment(response.getFailureReason());

			stagingDAO.populateMemberLoadAudit(memberLoadAudit);

		} catch (ServiceException e) {
			logger.error("Error while updating the response in Staging Database");
		}
	}

	public FailedResponse getBusinessError( MemberResponse response) {
		FailedResponse responseLog = new FailedResponse();

		responseLog.setFhpID(response.getId().toString());

		String memberID = "Unknown";

		try {
			MemberMaster memberInfo = this.stagingDAO.getMemberMaster(response.getId().longValue());
			memberID = memberInfo.getLobVendor() + "/" + memberInfo.getHicn();
		}
		catch(ServiceException e) {
			logger.error("Error geting member information from staging tables for MemberID-" + response.getId());
			memberID = "Member Staging DB ID is " + response.getId();
		}

		responseLog.setMemberID(memberID);
		responseLog.setFailureReason(response.getFailureReason());

		return responseLog;
	}
	
	public FailedResponse getFailedResponse( MemberSplitMergeResponse response) {
		FailedResponse failedResponse = new FailedResponse();

		failedResponse.setFhpID(response.getFhpId().toString());
		failedResponse.setFailureReason(response.getFailureReason());

		return failedResponse;
	}

}
