/**
 * 
 */
package com.fmcna.fhpckd.model;

import java.io.Serializable;

/**
 * @author bhupeshkantamneni
 */
public class PatientProgramEligibility implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long sourceId;
    private boolean deleted;
    private String startDate;
    private String termDate;
    private String termReason;
    private String status;
    private String lobVendor;
    private String lobType;
    private String hclob;
    private String hcGroup;
    private String hcProgram;

    public PatientProgramEligibility() {
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

	/**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the termDate
     */
    public String getTermDate() {
        return termDate;
    }

    /**
     * @param termDate the termDate to set
     */
    public void setTermDate(String termDate) {
        this.termDate = termDate;
    }

    /**
     * @return the termReason
     */
    public String getTermReason() {
        return termReason;
    }

    /**
     * @param termReason the termReason to set
     */
    public void setTermReason(String termReason) {
        this.termReason = termReason;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the lobVendor
     */
    public String getLobVendor() {
        return lobVendor;
    }

    /**
     * @param lobVendor the lobVendor to set
     */
    public void setLobVendor(String lobVendor) {
        this.lobVendor = lobVendor;
    }

    /**
     * @return the lobType
     */
    public String getLobType() {
        return lobType;
    }

    /**
     * @param lobType the lobType to set
     */
    public void setLobType(String lobType) {
        this.lobType = lobType;
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
