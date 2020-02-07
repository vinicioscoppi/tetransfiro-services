package com.tetransfiro.services.application;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tetransfiro.services.application.dtos.PersonTotalPaymentLinkRequestDto;
import com.tetransfiro.services.model.entities.PersonTotal;

@Component
public class PaymentLinkService {

	private static final String API_URL = "https://api.razorpay.com/v1/invoices";
	private static final String SHORT_URL_JSON_KEY = "short_url";

	@Autowired
	private HttpRequestService httpRequestService;

	@Value("${payment.link.service.username}")
	private String username;

	@Value("${payment.link.service.password}")
	private String password;

	public String generatePaymentLink(PersonTotal personTotal, CloseableHttpClient client) throws IOException, AuthenticationException {
		return new JSONObject(requestLink(personTotal, client)).getString(SHORT_URL_JSON_KEY);
	}

	private String requestLink(PersonTotal personTotal, CloseableHttpClient client) throws AuthenticationException, IOException {
		return httpRequestService.sendAutheticatedPostRequestTo(API_URL,
		                                                        new JSONObject(new PersonTotalPaymentLinkRequestDto(personTotal)).toString(),
		                                                        username,
		                                                        password, client);
	}

}
