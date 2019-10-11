package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author vidhishanandhikonda
 *
 */

public class MemberSplitMergeResponse {
	private String type;
	private Long fhpId;
	private boolean success;
	private String failureCode;

	private String failureReason;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getFhpId() {
		return fhpId;
	}

	public void setFhpId(Long fhpId) {
		this.fhpId = fhpId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

}
