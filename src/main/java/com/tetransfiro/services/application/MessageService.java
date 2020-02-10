package com.tetransfiro.services.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetransfiro.services.model.events.PurchaseTotalDeterminedEvent;
import com.tetransfiro.services.model.exception.TTInvalidMessageFormatException;

@Component
public class MessageService {

	@Autowired
	private PersonTotalService personTotalService;

	@Autowired
	private ObjectMapper objectMapper;

	private Logger logger = LoggerFactory.getLogger(MessageService.class);

	public void handle(String message) throws JsonProcessingException {
		if (message.contains("PurchaseTotalDeterminedEvent"))
			personTotalService.dispatchPaymentLinks(mapAsPurchaseTotalDeterminedEvent(message).getPersonTotals());
	}

	private PurchaseTotalDeterminedEvent mapAsPurchaseTotalDeterminedEvent(String message)
	        throws JsonProcessingException {

		var createdEvent = objectMapper.readValue(message, PurchaseTotalDeterminedEvent.class);

		if (createdEvent == null || createdEvent.getPersonTotals().isEmpty()) {
			logger.error("Received message has null fields and will not be processed.");
			throw new TTInvalidMessageFormatException();
		}
		return createdEvent;
	}
}
