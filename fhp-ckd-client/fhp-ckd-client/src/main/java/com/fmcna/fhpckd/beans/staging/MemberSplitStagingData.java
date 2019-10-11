package com.fmcna.fhpckd.beans.staging;

/**
 * 
 * @author vidhishanandhikonda
 *
 *         This is a Java bean class that represents the data read from the
 *         Staging table for Member Split request
 */
public class MemberSplitStagingData {

	private Long rowIdOriginal;
	private String sfIdOriginal;
	private String mrnOriginal;
	private Long rowIdNew;
	private String sfIdNew;
	private String mrnNew;

	public Long getRowIdOriginal() {
		return rowIdOriginal;
	}

	public void setRowIdOriginal(Long rowIdOriginal) {
		this.rowIdOriginal = rowIdOriginal;
	}

	public String getSfIdOriginal() {
		return sfIdOriginal;
	}

	public void setSfIdOriginal(String sfIdOriginal) {
		this.sfIdOriginal = sfIdOriginal;
	}

	public Long getRowIdNew() {
		return rowIdNew;
	}

	public void setRowIdNew(Long rowIdNew) {
		this.rowIdNew = rowIdNew;
	}

	public String getSfIdNew() {
		return sfIdNew;
	}

	public void setSfIdNew(String sfIdNew) {
		this.sfIdNew = sfIdNew;
	}

	public String getMrnOriginal() {
		return mrnOriginal;
	}

	public void setMrnOriginal(String mrnOriginal) {
		this.mrnOriginal = mrnOriginal;
	}

	public String getMrnNew() {
		return mrnNew;
	}

	public void setMrnNew(String mrnNew) {
		this.mrnNew = mrnNew;
	}

}
