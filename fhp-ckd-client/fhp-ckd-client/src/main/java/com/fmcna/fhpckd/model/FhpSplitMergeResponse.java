package com.fmcna.fhpckd.model;

import java.io.Serializable;

/**
 * This is a Java bean class that represents the Split/Merge member request to
 * Fabric Client
 * 
 * @author vidhishanandhikonda
 *
 */
public class FhpSplitMergeResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7797742542326760113L;

	private String type;
	private String fhpId; // Merge - rowIdPrimary, Split - rowIdOriginal
	private Boolean success;
	private String failureCode;
	private String failureReason;

	public FhpSplitMergeResponse() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFhpId() {
		return fhpId;
	}

	public void setFhpId(String fhpId) {
		this.fhpId = fhpId;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
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

	@Override
	public String toString() {
		return "FhpSplitMergeResponse [type=" + type + ", fhpId=" + fhpId + ", success=" + success + ", failureCode="
				+ failureCode + ", failureReason=" + failureReason + "]";
	}

}
