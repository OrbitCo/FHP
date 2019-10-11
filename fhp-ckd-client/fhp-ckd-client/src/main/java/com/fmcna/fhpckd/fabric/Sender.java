package com.fmcna.fhpckd.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

public class Sender {

	private static final Logger logger = LoggerFactory.getLogger(Sender.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${inboundQueue}")
	private String inboundQueueName;

	@Value("${inboundMergeSplitQueue}")
	private String inboundMergeSplitQueueName;

	public void send(String message) {
		logger.info("sending message='{}'", message);

		jmsTemplate.convertAndSend(inboundQueueName, message);

		logger.info("message queued - " + message);
	}

	public void sendSplitMerge(String message) {
		//LOGGER.info("sending message='{}'", message);

		jmsTemplate.convertAndSend(inboundMergeSplitQueueName, message);

		logger.info("message queued - " + message);
	}

	public void sendTestResponse(String message) {
		logger.info("sending test response message='{}'", message);
		jmsTemplate.convertAndSend("FHPCKDOutbound", message);
	}
}
