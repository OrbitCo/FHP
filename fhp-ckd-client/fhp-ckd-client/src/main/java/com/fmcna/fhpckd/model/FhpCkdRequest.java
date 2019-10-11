/**
 * 
 */
package com.fmcna.fhpckd.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author bhupeshkantamneni
 */
public class FhpCkdRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String fhpId;
    private List<Nephrologist> nephrologists;
    private PatientDemographics demographics;
    private List<PatientLabData> labs;
    private List<PatientProgramEligibility> programEligibilities;
    private List<PatientEvents> events;

    public FhpCkdRequest() {
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

    /**
     * @return the demographics
     */
    public PatientDemographics getDemographics() {
        return demographics;
    }

    /**
     * @param demographics the demographics to set
     */
    public void setDemographics(PatientDemographics demographics) {
        this.demographics = demographics;
    }

    /**
     * @return the nephrologists
     */
    public List<Nephrologist> getNephrologists() {
        return nephrologists;
    }

    /**
     * @param nephrologists the nephrologists to set
     */
    public void setNephrologists(List<Nephrologist> nephrologists) {
        this.nephrologists = nephrologists;
    }

    /**
     * @return the labs
     */
    public List<PatientLabData> getLabs() {
        return labs;
    }

    /**
     * @param labs the labs to set
     */
    public void setLabs(List<PatientLabData> labs) {
        this.labs = labs;
    }

    /**
     * @return the programEligibilities
     */
    public List<PatientProgramEligibility> getProgramEligibilities() {
        return programEligibilities;
    }

    /**
     * @param programEligibilities the programEligibilities to set
     */
    public void setProgramEligibilities(List<PatientProgramEligibility> programEligibilities) {
        this.programEligibilities = programEligibilities;
    }

    /**
     * @return the events
     */
    public List<PatientEvents> getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    public void setEvents(List<PatientEvents> events) {
        this.events = events;
    }

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("FhpCkdRequest [fhpId=")
		.append(fhpId)
		.append(", nephrologists=")
		.append(nephrologists)
		.append(", demographics=")
		.append(demographics)
		.append( ", labs=" )
		.append(labs)
		.append(", programEligibilities=")
		.append(programEligibilities)
		.append(", events=")
		.append(events)
		.append("]");
		return   stringBuffer.toString();
	}
    
}