package com.fmcna.fhpckd.fabric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.model.FhpCkdRequest;
import com.fmcna.fhpckd.model.FhpSplitMergeRequest;

@Component
public class FabricClient {

	@Autowired
	ObjectMapper jsonMapper;

	@Autowired
	Sender sender;

	public void sendRequest(FhpCkdRequest request) throws ServiceException {

		try {
			String jsonRequest = jsonMapper.writeValueAsString(request);

			sender.send(jsonRequest);
		} catch (Exception e) {
			throw new ServiceException("Error sending request to FABRIC queue.", e);
		}

	}

	public void sendSplitMergeRequest(FhpSplitMergeRequest request) throws ServiceException {

		try {
			String jsonRequest = jsonMapper.writeValueAsString(request);

			sender.sendSplitMerge(jsonRequest);
		} catch (Exception e) {
			throw new ServiceException("Error sending Split/Merge request to FABRIC queue.", e);
		}

	}

}
