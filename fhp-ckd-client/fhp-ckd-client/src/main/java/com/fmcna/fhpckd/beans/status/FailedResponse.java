package com.fmcna.fhpckd.beans.status;

public class FailedResponse {
	private String fhpID;
	
	private String memberID;
	
	private String failureReason;

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
