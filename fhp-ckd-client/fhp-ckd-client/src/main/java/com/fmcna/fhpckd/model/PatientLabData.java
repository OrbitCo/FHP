/**
 * 
 */
package com.fmcna.fhpckd.model;

import java.io.Serializable;

/**
 * @author bhupeshkantamneni
 */
public class PatientLabData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long sourceId;
	private boolean deleted;
	private String labType;
	private String labResult;
	private String labLoincCode;
	private String serviceDate;

	public PatientLabData() {
	}

	public long getSourceId() {
		return sourceId;
	}

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getLabType() {
		return labType;
	}

	public void setLabType(String labType) {
		this.labType = labType;
	}

	public String getLabResult() {
		return labResult;
	}

	public void setLabResult(String labResult) {
		this.labResult = labResult;
	}

	public String getLabLoincCode() {
		return labLoincCode;
	}

	public void setLabLoincCode(String labLoincCode) {
		this.labLoincCode = labLoincCode;
	}

	/**
	 * @return the serviceDate
	 */
	public String getServiceDate() {
		return serviceDate;
	}

	/**
	 * @param serviceDate the serviceDate to set
	 */
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
}
