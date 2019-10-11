package com.fmcna.fhpckd.beans.staging;

import java.util.List;

/**
 * 
 * @author manishtiwari
 *
 * This is a wrapper class that encapsulates all the beans for a member data received from FHP.
 */

public class MemberStagingData {
	private MemberMaster memberMaster;
	private List<MemberEligibility> memberEligibilityList;
	private List<MemberLabs> memberLabsList;
	
	public MemberMaster getMemberMaster() {
		return memberMaster;
	}
	public void setMemberMaster(MemberMaster memberMaster) {
		this.memberMaster = memberMaster;
	}
	public List<MemberEligibility> getMemberEligibilityList() {
		return memberEligibilityList;
	}
	public void setMemberEligibilityList(List<MemberEligibility> memberEligibilityList) {
		this.memberEligibilityList = memberEligibilityList;
	}
	public List<MemberLabs> getMemberLabsList() {
		return memberLabsList;
	}
	public void setMemberLabsList(List<MemberLabs> memberLabsList) {
		this.memberLabsList = memberLabsList;
	}
	
	
}
