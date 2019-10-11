package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author vidhishanandhikonda
 *
 *         This is a Java bean class that represents the data read from the
 *         Staging table for Member Merge request
 */
public class MemberMergeStagingData {

	private Long rowIdPrimary;
	private String sfIdPrimary;
	private String mrnPrimary;

    private Long rowIdMerge;
	private String sfIdMerge;
	private String mrnMerge;

	public Long getRowIdPrimary() {
		return rowIdPrimary;
	}

	public void setRowIdPrimary(Long rowIdPrimary) {
		this.rowIdPrimary = rowIdPrimary;
	}

	public String getSfIdPrimary() {
		return sfIdPrimary;
	}

	public void setSfIdPrimary(String sfIdPrimary) {
		this.sfIdPrimary = sfIdPrimary;
	}

	public Long getRowIdMerge() {
		return rowIdMerge;
	}

	public void setRowIdMerge(Long rowIdMerge) {
		this.rowIdMerge = rowIdMerge;
	}

	public String getSfIdMerge() {
		return sfIdMerge;
	}

	public void setSfIdMerge(String sfIdMerge) {
		this.sfIdMerge = sfIdMerge;
	}

	public String getMrnPrimary() {
		return mrnPrimary;
	}

	public void setMrnPrimary(String mrnPrimary) {
		this.mrnPrimary = mrnPrimary;
	}

	public String getMrnMerge() {
		return mrnMerge;
	}

	public void setMrnMerge(String mrnMerge) {
		this.mrnMerge = mrnMerge;
	}

}
