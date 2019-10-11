package com.fmcna.fhpckd.beans.staging;

import java.math.BigDecimal;

/**
 * 
 * @author manishtiwari
 *
 * This is a Java bean class that represents the data read from the Staging table for Member Labs
 */
public class MemberLabs {

	private Long id;
	private Long memberId;
    private String labType;
    private BigDecimal labResult;
    private String labLoincCode;
	private String serviceDate;
	private String rowDeleted;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getLabType() {
		return labType;
	}
	public void setLabType(String labType) {
		this.labType = labType;
	}	
	public BigDecimal getLabResult() {
		return labResult;
	}
	public void setLabResult(BigDecimal labResult) {
		this.labResult = labResult;
	}
	public String getLabLoincCode() {
		return labLoincCode;
	}
	public void setLabLoincCode(String labLoincCode) {
		this.labLoincCode = labLoincCode;
	}
	public String getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
	public String getRowDeleted() {
		return rowDeleted;
	}
	public void setRowDeleted(String rowDeleted) {
		this.rowDeleted = rowDeleted;
	}
	
	
}
