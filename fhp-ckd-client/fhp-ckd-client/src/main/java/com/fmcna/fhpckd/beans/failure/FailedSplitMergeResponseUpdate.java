package com.fmcna.fhpckd.beans.failure;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fmcna.fhpckd.model.FhpSplitMergeResponse;

@Document
public class FailedSplitMergeResponseUpdate {

	@Id
	private String id;

	private String fhpId;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date responseTime;

	private FhpSplitMergeResponse response;

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

	public FhpSplitMergeResponse getResponse() {
		return response;
	}

	public void setResponse(FhpSplitMergeResponse response) {
		this.response = response;

		if (this.fhpId == null) {
			this.fhpId = response.getFhpId();
		}
	}

}
