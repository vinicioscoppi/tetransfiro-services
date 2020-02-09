package com.tetransfiro.services.application;

import java.util.Collection;

import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tetransfiro.services.model.entities.PersonTotal;
import com.tetransfiro.services.model.exception.TTPaymentLinkRequestException;

@Component
public class PersonTotalService {

	private Logger logger = LoggerFactory.getLogger(PersonTotalService.class);

	@Autowired
	private PaymentLinkService paymentLinkService;

	@Autowired
	private EmailService emailService;

	public void dispatchPaymentLinks(Collection<PersonTotal> personTotals) {
		try (var client = HttpClients.createDefault()) {
			personTotals.forEach(total -> {
				try {
					paymentLinkService.generatePaymentLink(total, client);
				} catch (TTPaymentLinkRequestException e) {
					logger.error("Error in request for {}", total.getPersonFullName());
					logger.debug("Sending support e-mail to {}", total.getEmail());
					emailService.sendSupportEmail(total);
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
