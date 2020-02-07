package com.tetransfiro.services.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetransfiro.services.model.events.PurchaseTotalDeterminedEvent;

@Component
public class MessageService {

	@Autowired
	private PersonTotalService personTotalService;

	private ObjectMapper objectMapper = new ObjectMapper();

	public void handle(String message) throws JsonProcessingException {
		personTotalService.dispatchPaymentLinks(mapAsPurchaseTotalDeterminedEvent(message).getPersonTotals());
	}

	private PurchaseTotalDeterminedEvent mapAsPurchaseTotalDeterminedEvent(String message)
	        throws JsonProcessingException {

		return objectMapper.readValue(message, PurchaseTotalDeterminedEvent.class);
	}
}
