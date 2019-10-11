package com.fmcna.fhpckd.model;

import java.io.Serializable;

/**
 * This is a Java bean class that represents the Split/Merge member request to Fabric Client
 *         
 * @author vidhishanandhikonda
 *
 */
public class FhpSplitMergeRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7797742542326760113L;
	
	private String type;
	
	//Merge
    private String rowIdPrimary;
	private String rowIdMerge;
	private String sfIdPrimary;
	private String sfIdMerge;
	private String mrnPrimary;
	private String mrnMerge;

    //Split 
    private String rowIdOriginal;
    private String sfIdOriginal;
    private String mrnOriginal;
    private String rowIdNew1;
    private String rowIdNew2;
    private String rowIdNew3;
    private String rowIdNew4;
    private String rowIdNew5;

    public FhpSplitMergeRequest() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRowIdPrimary() {
		return rowIdPrimary;
	}

	public void setRowIdPrimary(String rowIdPrimary) {
		this.rowIdPrimary = rowIdPrimary;
	}

	public String getRowIdMerge() {
		return rowIdMerge;
	}

	public void setRowIdMerge(String rowIdMerge) {
		this.rowIdMerge = rowIdMerge;
	}

	public String getSfIdPrimary() {
		return sfIdPrimary;
	}

	public void setSfIdPrimary(String sfIdPrimary) {
		this.sfIdPrimary = sfIdPrimary;
	}

	public String getSfIdMerge() {
		return sfIdMerge;
	}

	public void setSfIdMerge(String sfIdMerge) {
		this.sfIdMerge = sfIdMerge;
	}

	public String getRowIdOriginal() {
		return rowIdOriginal;
	}

	public void setRowIdOriginal(String rowIdOriginal) {
		this.rowIdOriginal = rowIdOriginal;
	}

	public String getSfIdOriginal() {
		return sfIdOriginal;
	}

	public void setSfIdOriginal(String sfIdOriginal) {
		this.sfIdOriginal = sfIdOriginal;
	}

	public String getRowIdNew1() {
		return rowIdNew1;
	}

	public void setRowIdNew1(String rowIdNew1) {
		this.rowIdNew1 = rowIdNew1;
	}

	public String getRowIdNew2() {
		return rowIdNew2;
	}

	public void setRowIdNew2(String rowIdNew2) {
		this.rowIdNew2 = rowIdNew2;
	}

	public String getRowIdNew3() {
		return rowIdNew3;
	}

	public void setRowIdNew3(String rowIdNew3) {
		this.rowIdNew3 = rowIdNew3;
	}

	public String getRowIdNew4() {
		return rowIdNew4;
	}

	public void setRowIdNew4(String rowIdNew4) {
		this.rowIdNew4 = rowIdNew4;
	}

	public String getRowIdNew5() {
		return rowIdNew5;
	}

	public void setRowIdNew5(String rowIdNew5) {
		this.rowIdNew5 = rowIdNew5;
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

	public String getMrnOriginal() {
		return mrnOriginal;
	}

	public void setMrnOriginal(String mrnOriginal) {
		this.mrnOriginal = mrnOriginal;
	}
    
}
