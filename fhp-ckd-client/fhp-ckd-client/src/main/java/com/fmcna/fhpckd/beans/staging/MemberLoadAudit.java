package com.fmcna.fhpckd.beans.staging;

import java.util.Date;

public class MemberLoadAudit {

	private Date sfhcLoadDttm;
	private Long srcMemberId;

	private String sfhcLoadStatus;
	private String sfhcLoadComment;

	private String salesforceIdMatch1;
	private String salesforceIdMatch2;
	private String salesforceIdMatch3;
	private String salesforceIdMatch4;
	private String salesforceIdMatch5;
	private String salesforceIdMatch6;
	private String salesforceIdMatch7;
	private String salesforceIdMatch8;
	private String salesforceIdMatch9;
	private String salesforceIdMatch10;

	private Long tgtMemberId; // fhpIdFromHc; TGT_MEMBER_ID
	private String operation; // hcOperation; HC_OPERATION

	private String memberType; // MEMBER_TYPE
	private String flowState; // FLOW_STATE
	private String comment; // COMMENT
	private String category; // CATEGORY
	private String mrnFromHc; // HC_MRN
	private String mrnFromEmpi; // EMPI_MRN
	private String empiSearchStringRequest; // EMPI_SEARCH_STRING_REQUEST
	private String empiSearchStringResponse; // EMPI_SEARCH_STRING_RESPONSE
	
	private String mrnMatch1; // duplicateMrns; // MRN_MATCH1....MRN_MATCH10
	private String mrnMatch2;
	private String mrnMatch3;
	private String mrnMatch4;
	private String mrnMatch5;
	private String mrnMatch6;
	private String mrnMatch7;
	private String mrnMatch8;
	private String mrnMatch9;
	private String mrnMatch10;

    private String sfIdFromHc;
    private String sfIdFromEmpi;

	private String failureComponent;
	
	public String getFailureComponent() {
		return failureComponent;
	}

	public void setFailureComponent(String failureComponent) {
		this.failureComponent = failureComponent;
	}

	public Date getSfhcLoadDttm() {
		return sfhcLoadDttm;
	}

	public void setSfhcLoadDttm(Date sfhcLoadDttm) {
		this.sfhcLoadDttm = sfhcLoadDttm;
	}

	public Long getSrcMemberId() {
		return srcMemberId;
	}

	public void setSrcMemberId(Long srcMemberId) {
		this.srcMemberId = srcMemberId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getSfhcLoadStatus() {
		return sfhcLoadStatus;
	}

	public void setSfhcLoadStatus(String sfhcLoadStatus) {
		this.sfhcLoadStatus = sfhcLoadStatus;
	}

	public String getSfhcLoadComment() {
		return sfhcLoadComment;
	}

	public void setSfhcLoadComment(String sfhcLoadComment) {
		this.sfhcLoadComment = sfhcLoadComment;
	}

	public Long getTgtMemberId() {
		return tgtMemberId;
	}

	public void setTgtMemberId(Long tgtMemberId) {
		this.tgtMemberId = tgtMemberId;
	}

	public String getSalesforceIdMatch1() {
		return salesforceIdMatch1;
	}

	public void setSalesforceIdMatch1(String salesforceIdMatch1) {
		this.salesforceIdMatch1 = salesforceIdMatch1;
	}

	public String getSalesforceIdMatch2() {
		return salesforceIdMatch2;
	}

	public void setSalesforceIdMatch2(String salesforceIdMatch2) {
		this.salesforceIdMatch2 = salesforceIdMatch2;
	}

	public String getSalesforceIdMatch3() {
		return salesforceIdMatch3;
	}

	public void setSalesforceIdMatch3(String salesforceIdMatch3) {
		this.salesforceIdMatch3 = salesforceIdMatch3;
	}

	public String getSalesforceIdMatch4() {
		return salesforceIdMatch4;
	}

	public void setSalesforceIdMatch4(String salesforceIdMatch4) {
		this.salesforceIdMatch4 = salesforceIdMatch4;
	}

	public String getSalesforceIdMatch5() {
		return salesforceIdMatch5;
	}

	public void setSalesforceIdMatch5(String salesforceIdMatch5) {
		this.salesforceIdMatch5 = salesforceIdMatch5;
	}

	public String getSalesforceIdMatch6() {
		return salesforceIdMatch6;
	}

	public void setSalesforceIdMatch6(String salesforceIdMatch6) {
		this.salesforceIdMatch6 = salesforceIdMatch6;
	}

	public String getSalesforceIdMatch7() {
		return salesforceIdMatch7;
	}

	public void setSalesforceIdMatch7(String salesforceIdMatch7) {
		this.salesforceIdMatch7 = salesforceIdMatch7;
	}

	public String getSalesforceIdMatch8() {
		return salesforceIdMatch8;
	}

	public void setSalesforceIdMatch8(String salesforceIdMatch8) {
		this.salesforceIdMatch8 = salesforceIdMatch8;
	}

	public String getSalesforceIdMatch9() {
		return salesforceIdMatch9;
	}

	public void setSalesforceIdMatch9(String salesforceIdMatch9) {
		this.salesforceIdMatch9 = salesforceIdMatch9;
	}

	public String getSalesforceIdMatch10() {
		return salesforceIdMatch10;
	}

	public void setSalesforceIdMatch10(String salesforceIdMatch10) {
		this.salesforceIdMatch10 = salesforceIdMatch10;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getFlowState() {
		return flowState;
	}

	public void setFlowState(String flowState) {
		this.flowState = flowState;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMrnFromHc() {
		return mrnFromHc;
	}

	public void setMrnFromHc(String mrnFromHc) {
		this.mrnFromHc = mrnFromHc;
	}

	public String getMrnFromEmpi() {
		return mrnFromEmpi;
	}

	public void setMrnFromEmpi(String mrnFromEmpi) {
		this.mrnFromEmpi = mrnFromEmpi;
	}

	public String getEmpiSearchStringRequest() {
		return empiSearchStringRequest;
	}

	public void setEmpiSearchStringRequest(String empiSearchStringRequest) {
		this.empiSearchStringRequest = empiSearchStringRequest;
	}

	public String getEmpiSearchStringResponse() {
		return empiSearchStringResponse;
	}

	public void setEmpiSearchStringResponse(String empiSearchStringResponse) {
		this.empiSearchStringResponse = empiSearchStringResponse;
	}

	public String getMrnMatch1() {
		return mrnMatch1;
	}

	public void setMrnMatch1(String mrnMatch1) {
		this.mrnMatch1 = mrnMatch1;
	}

	public String getMrnMatch2() {
		return mrnMatch2;
	}

	public void setMrnMatch2(String mrnMatch2) {
		this.mrnMatch2 = mrnMatch2;
	}

	public String getMrnMatch3() {
		return mrnMatch3;
	}

	public void setMrnMatch3(String mrnMatch3) {
		this.mrnMatch3 = mrnMatch3;
	}

	public String getMrnMatch4() {
		return mrnMatch4;
	}

	public void setMrnMatch4(String mrnMatch4) {
		this.mrnMatch4 = mrnMatch4;
	}

	public String getMrnMatch5() {
		return mrnMatch5;
	}

	public void setMrnMatch5(String mrnMatch5) {
		this.mrnMatch5 = mrnMatch5;
	}

	public String getMrnMatch6() {
		return mrnMatch6;
	}

	public void setMrnMatch6(String mrnMatch6) {
		this.mrnMatch6 = mrnMatch6;
	}

	public String getMrnMatch7() {
		return mrnMatch7;
	}

	public void setMrnMatch7(String mrnMatch7) {
		this.mrnMatch7 = mrnMatch7;
	}

	public String getMrnMatch8() {
		return mrnMatch8;
	}

	public void setMrnMatch8(String mrnMatch8) {
		this.mrnMatch8 = mrnMatch8;
	}

	public String getMrnMatch9() {
		return mrnMatch9;
	}

	public void setMrnMatch9(String mrnMatch9) {
		this.mrnMatch9 = mrnMatch9;
	}

	public String getMrnMatch10() {
		return mrnMatch10;
	}

	public void setMrnMatch10(String mrnMatch10) {
		this.mrnMatch10 = mrnMatch10;
	}

	public String getSfIdFromHc() {
		return sfIdFromHc;
	}

	public void setSfIdFromHc(String sfIdFromHc) {
		this.sfIdFromHc = sfIdFromHc;
	}

	public String getSfIdFromEmpi() {
		return sfIdFromEmpi;
	}

	public void setSfIdFromEmpi(String sfIdFromEmpi) {
		this.sfIdFromEmpi = sfIdFromEmpi;
	}

}
