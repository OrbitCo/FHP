package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author vidhishanandhikonda
 *
 */
public class SplitMergeMember {
	private Long memberRowId;
	private String memberSalesforceId;
	private String memberMrn;

	public Long getMemberRowId() {
		return memberRowId;
	}

	public void setMemberRowId(Long memberRowId) {
		this.memberRowId = memberRowId;
	}

	public String getMemberSalesforceId() {
		return memberSalesforceId;
	}

	public void setMemberSalesforceId(String memberSalesforceId) {
		this.memberSalesforceId = memberSalesforceId;
	}

	public String getMemberMrn() {
		return memberMrn;
	}

	public void setMemberMrn(String memberMrn) {
		this.memberMrn = memberMrn;
	}

	public SplitMergeMember(Long memberRowId, String memberSalesforceId, String memberMrn) {
		super();
		this.memberRowId = memberRowId;
		this.memberSalesforceId = memberSalesforceId;
		this.memberMrn = memberMrn;
	}

}
