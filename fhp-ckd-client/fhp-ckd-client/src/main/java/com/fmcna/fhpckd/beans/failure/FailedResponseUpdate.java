package com.fmcna.fhpckd.beans.failure;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fmcna.fhpckd.model.FhpCkdResponse;

@Document
public class FailedResponseUpdate {
	
	@Id
	private String id;
	
	private String fhpId;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date responseTime;

	private FhpCkdResponse response;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFhpId() {
		return fhpId;
	}

	public void setFhpId(String fhpId) {
		this.fhpId = fhpId;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public FhpCkdResponse getResponse() {
		return response;
	}

	public void setResponse(FhpCkdResponse response) {
		this.response = response;
		
		if( this.fhpId == null) {
			this.fhpId = response.getFhpId();
		}
	}
	
}
