/**
 * 
 */
package com.fmcna.fhpckd.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author bhupeshkantamneni
 */
public class FhpCkdResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Source Member Id
    private String fhpId;
    private String sfIdFromHc;
    private String sfIdFromEmpi;
    
    // Source MRN
    private String mrn;
    private String euId;
    private String empiSystem; //TODO: no need
    private String empiLocalId;//TODO: no need
    
    // either CKD or FKC – CKD member can be FKC, if MRN found in HC
    private String memberType; 
    
    // current state of the work-flow; RECEIVED, PROCESSING, COMPLETE, FAILED at EMPI, SF, MONGO, etc,...
    private String flowState;    
    private Boolean success;
    private String failureCode;
    private String failureReason;
    private String comment;
    private List<String> duplicateSalesForceIDs;
    private List<String> duplicateMrns;
    private String category;

    // If different from Source
    private String fhpIdFromHc; 

    // If different from Source
    private String mrnFromHc;
    private String mrnFromEmpi;
    private String hcOperation;
    private String empiSearchByDemoRequest;
    private String empiSearchByDemoResponse;

    public FhpCkdResponse() {
    }

    /**
     * @return the fhpId
     */
    public String getFhpId() {
        return fhpId;
    }

    /**
     * @param fhpId the fhpId to set
     */
    public void setFhpId(String fhpId) {
        this.fhpId = fhpId;
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

	/**
     * @return the mrn
     */
    public String getMrn() {
        return mrn;
    }

    /**
     * @param mrn the mrn to set
     */
    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    /**
     * @return the eUId
     */
    public String getEuId() {
        return euId;
    }

    /**
     * @param eUId the eUId to set
     */
    public void setEuId(String euId) {
        this.euId = euId;
    }

    /**
     * @return the empiSystem
     */
    public String getEmpiSystem() {
        return empiSystem;
    }

    /**
     * @param empiSystem the empiSystem to set
     */
    public void setEmpiSystem(String empiSystem) {
        this.empiSystem = empiSystem;
    }

    /**
     * @return the empiLocalId
     */
    public String getEmpiLocalId() {
        return empiLocalId;
    }

    /**
     * @param empiLocalId the empiLocalId to set
     */
    public void setEmpiLocalId(String empiLocalId) {
        this.empiLocalId = empiLocalId;
    }

    /**
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	/**
     * @return the failureReason
     */
    public String getFailureReason() {
        return failureReason;
    }

    /**
     * @param failureReason the failureReason to set
     */
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

	public List<String> getDuplicateSalesForceIDs() {
		return duplicateSalesForceIDs;
	}

	public void setDuplicateSalesForceIDs(List<String> duplicateSalesForceIDs) {
		this.duplicateSalesForceIDs = duplicateSalesForceIDs;
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

	public List<String> getDuplicateMrns() {
		return duplicateMrns;
	}

	public void setDuplicateMrns(List<String> duplicateMrns) {
		this.duplicateMrns = duplicateMrns;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFhpIdFromHc() {
		return fhpIdFromHc;
	}

	public void setFhpIdFromHc(String fhpIdFromHc) {
		this.fhpIdFromHc = fhpIdFromHc;
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

	public String getHcOperation() {
		return hcOperation;
	}

	public void setHcOperation(String hcOperation) {
		this.hcOperation = hcOperation;
	}

	public String getEmpiSearchByDemoRequest() {
		return empiSearchByDemoRequest;
	}

	public void setEmpiSearchByDemoRequest(String empiSearchByDemoRequest) {
		this.empiSearchByDemoRequest = empiSearchByDemoRequest;
	}

	public String getEmpiSearchByDemoResponse() {
		return empiSearchByDemoResponse;
	}

	public void setEmpiSearchByDemoResponse(String empiSearchByDemoResponse) {
		this.empiSearchByDemoResponse = empiSearchByDemoResponse;
	}

	@Override
	public String toString() {
		return "FhpCkdResponse [fhpId=" + fhpId + ", sfIdFromHc=" + sfIdFromHc + ", sfIdFromEmpi=" + sfIdFromEmpi + ", mrn=" + mrn + ", euId=" + euId + ", empiSystem="
				+ empiSystem + ", empiLocalId=" + empiLocalId + ", memberType=" + memberType + ", flowState="
				+ flowState + ", success=" + success + ", failureCode=" + failureCode + ", failureReason="
				+ failureReason + ", comment=" + comment + ", duplicateSalesForceIDs=" + duplicateSalesForceIDs
				+ ", duplicateMrns=" + duplicateMrns + ", category=" + category + ", fhpIdFromHc=" + fhpIdFromHc
				+ ", mrnFromHc=" + mrnFromHc + ", mrnFromEmpi=" + mrnFromEmpi + ", hcOperation=" + hcOperation + ", empiSearchByDemoRequest="
				+ empiSearchByDemoRequest + ", empiSearchByDemoResponse=" + empiSearchByDemoResponse + "]";
	}
    
}