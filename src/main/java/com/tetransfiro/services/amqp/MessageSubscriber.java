package com.tetransfiro.services.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tetransfiro.services.application.MessageService;

@Component
public class MessageSubscriber {

	@Autowired
	private MessageService messageService;

	private Logger logger = LoggerFactory.getLogger(MessageSubscriber.class);

	@RabbitListener(queues = { "${queue.purchase.name}" })
	public void receive(@Payload String fileBody) {
		
		logger.debug("Received <{}>", fileBody);

		try {
			messageService.handle(fileBody);
			
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
	}
}
