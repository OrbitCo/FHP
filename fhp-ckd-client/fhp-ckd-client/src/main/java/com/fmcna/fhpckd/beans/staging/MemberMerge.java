package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author vidhishanandhikonda
 *
 * This is a Java bean class that represents the data for Member Merge request
 * 
 */
public class MemberMerge {

	private SplitMergeMember primary;
	private SplitMergeMember merge;

	public SplitMergeMember getPrimary() {
		return primary;
	}

	public void setPrimary(SplitMergeMember primary) {
		this.primary = primary;
	}

	public SplitMergeMember getMerge() {
		return merge;
	}

	public void setMerge(SplitMergeMember merge) {
		this.merge = merge;
	}

}
