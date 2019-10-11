package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author manishtiwari
 *
 * This is a Java bean class that represents the data read from the Staging table for Member Master
 */

public class MemberResponse {

	private Long id;
	private Long MRN;
	private Long EUID;
	private String salesForceID;
	private boolean successfulHCUpsert;
	private String failureReason;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMRN() {
		return MRN;
	}
	public void setMRN(Long mRN) {
		MRN = mRN;
	}
	public Long getEUID() {
		return EUID;
	}
	public void setEUID(Long eUID) {
		EUID = eUID;
	}
	public String getSalesForceID() {
		return salesForceID;
	}
	public void setSalesForceID(String salesForceID) {
		this.salesForceID = salesForceID;
	}
	public boolean isSuccessfulHCUpsert() {
		return successfulHCUpsert;
	}
	public void setSuccessfulHCUpsert(boolean successfulHCUpsert) {
		this.successfulHCUpsert = successfulHCUpsert;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}


}
