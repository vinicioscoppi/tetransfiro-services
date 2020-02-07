package com.tetransfiro.services.application;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tetransfiro.services.model.entities.PersonTotal;

@Component
public class PersonTotalService {

	private Logger logger = LoggerFactory.getLogger(PersonTotalService.class);
	
	@Autowired
	private PaymentLinkService paymentLinkService;

	public void dispatchPaymentLinks(Collection<PersonTotal> personTotals) {
		try (var client = HttpClients.createDefault()) {
			personTotals.forEach(total -> {
				try {
					paymentLinkService.generatePaymentLink(total, client);
				} catch (AuthenticationException | IOException e) {
					logger.error(e.getMessage());
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
