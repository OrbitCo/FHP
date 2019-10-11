package com.fmcna.fhpckd.beans.status;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;


public class FailedResponseDetail {
	
		
	private String jobID;
	
	private String fhpID;
	
	private String memberID;
	
	private String failureReason;
	
	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	

	public String getFhpID() {
		return fhpID;
	}

	public void setFhpID(String fhpID) {
		this.fhpID = fhpID;
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	
}
