package com.tetransfiro.services.application;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetransfiro.services.application.dtos.PersonTotalPaymentLinkRequestDto;
import com.tetransfiro.services.model.entities.PersonTotal;
import com.tetransfiro.services.model.exception.TTPaymentLinkRequestException;

@Component
public class PaymentLinkService {

	private static final String API_URL = "https://api.razorpay.com/v1/invoices";
	
	private static final String ERROR = "error";
	
	private Logger logger = LoggerFactory.getLogger(PaymentLinkService.class);

	@Autowired
	private HttpRequestService httpRequestService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${payment.link.service.username}")
	private String username;

	@Value("${payment.link.service.password}")
	private String password;

	public void generatePaymentLink(PersonTotal personTotal, CloseableHttpClient client) {
		try {
			var responseText = requestLink(personTotal, client);
			if (responseText.contains(ERROR)) {
				logger.error("Request responded with {}", responseText);
				throw new TTPaymentLinkRequestException();
			}
		} catch (IOException | AuthenticationException e) {
			throw new TTPaymentLinkRequestException();
		}
	}

	private String requestLink(PersonTotal personTotal, CloseableHttpClient client)
	        throws AuthenticationException, IOException {
		return httpRequestService.sendAutheticatedPostRequestTo(API_URL,
		                                                        writeAsJsonDto(personTotal),
		                                                        username,
		                                                        password,
		                                                        client);
	}

	private String writeAsJsonDto(PersonTotal personTotal) throws JsonProcessingException {
		return objectMapper.writeValueAsString(new PersonTotalPaymentLinkRequestDto(personTotal));
	}

}
