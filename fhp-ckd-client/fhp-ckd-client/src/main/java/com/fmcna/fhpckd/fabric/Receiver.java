package com.fmcna.fhpckd.fabric;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmcna.fhpckd.beans.status.SplitMergeEnum;
import com.fmcna.fhpckd.exception.ServiceException;
import com.fmcna.fhpckd.model.FhpCkdResponse;
import com.fmcna.fhpckd.model.FhpSplitMergeResponse;
import com.fmcna.fhpckd.persistence.AuditService;
import com.fmcna.fhpckd.service.HCResponseService;

public class Receiver {
	private static final Logger logger =
			LoggerFactory.getLogger(Receiver.class);


	@Autowired
	HCResponseService service;
	
	@Autowired
	ObjectMapper jsonMapper;

	@Autowired
	AuditService auditService;
	
//	@JmsListener(destination = "FHPCKDInbound")
	public void receive(String message) {
		logger.info("Received test request message='{}'", message);

	}
	

 	@JmsListener(destination = "${outboundQueue}")
	public void receiveResponse(String message) {
		logger.info("received response message='{}'", message);
		
		FhpCkdResponse response;
		try {
			response = jsonMapper.readValue(message, FhpCkdResponse.class);
			auditService.auditResponse(response);
			service.processResponse(response);
		} catch (Exception e) {
			logger.error("Error processing member response message: " + message, e);

		}/* catch (JsonParseException e) {
			logger.error("Error parsing response message: " + message, e);
			throw new ServiceException("Error parsing response message: " + message, e);
		} catch (JsonMappingException e) {
			logger.error("Error parsing response message: " + message, e);
			throw new ServiceException("Error parsing response message: " + message, e);
		} catch (IOException e) {
			logger.error("Error parsing response message: " + message, e);
			throw new ServiceException("Error parsing response message: " + message, e);
		}*/
	}

	@JmsListener(destination = "${outboundMergeSplitQueue}")
	public void receiveMergeSplitResponse(String message) {
		logger.info("Received merge-split response message='{}'", message);

		FhpSplitMergeResponse response;
		try {
			response = jsonMapper.readValue(message, FhpSplitMergeResponse.class);
			auditService.auditResponse(response);
			
			//Fabric sends jobType inconsistently, so we are handling it here to be consistent.
			response.setType(SplitMergeEnum.fromValue(response.getType()).getType()); 
			service.processResponse(response);

		} catch (Exception e) {
			logger.error("Error processing member merge-split response message : {} ", message, e);

		} /*catch (JsonParseException e) {
			logger.error("Error parsing member merge-split response message: " + message, e);
			throw new ServiceException("Error parsing member merge-split response message: " + message, e);
		} catch (JsonMappingException e) {
			logger.error("Error parsing member merge-split response message: " + message, e);
			throw new ServiceException("Error parsing member merge-split response message: " + message, e);
		} catch (IOException e) {
			logger.error("Error parsing member merge-split response message: " + message, e);
			throw new ServiceException("Error parsing member merge-split response message: " + message, e);
		}*/
	}

}
