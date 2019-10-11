package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author manishtiwari
 *
 * This is a Java bean class that represents the data read from the Staging table for Member Eligibility
 */

public class MemberEligibility {

	private Long id;
	private Long srcMemberRowId;
	private String startDate;
	private String termDate;
	private String termReason;
	private String status;
	private String lobVendor;
	private String lobType;
	private String rowDeleted;
	private String hclob;
	private String hcGroup;
	private String hcProgram;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getSrcMemberRowId() {
		return srcMemberRowId;
	}
	public void setSrcMemberRowId(Long srcMemberRowId) {
		this.srcMemberRowId = srcMemberRowId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTermDate() {
		return termDate;
	}
	public void setTermDate(String termDate) {
		this.termDate = termDate;
	}
	public String getTermReason() {
		return termReason;
	}
	public void setTermReason(String termReason) {
		this.termReason = termReason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLobVendor() {
		return lobVendor;
	}
	public void setLobVendor(String lobVendor) {
		this.lobVendor = lobVendor;
	}
	public String getLobType() {
		return lobType;
	}
	public void setLobType(String lobType) {
		this.lobType = lobType;
	}
	public String getRowDeleted() {
		return rowDeleted;
	}
	public void setRowDeleted(String rowDeleted) {
		this.rowDeleted = rowDeleted;
	}
	public String getHclob() {
		return hclob;
	}
	public void setHclob(String hclob) {
		this.hclob = hclob;
	}
	public String getHcGroup() {
		return hcGroup;
	}
	public void setHcGroup(String hcGroup) {
		this.hcGroup = hcGroup;
	}
	public String getHcProgram() {
		return hcProgram;
	}
	public void setHcProgram(String hcProgram) {
		this.hcProgram = hcProgram;
	}
	
}
