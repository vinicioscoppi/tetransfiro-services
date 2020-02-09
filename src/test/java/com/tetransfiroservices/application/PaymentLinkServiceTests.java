package com.tetransfiroservices.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tetransfiro.services.TetransfiroServicesApplication;
import com.tetransfiro.services.application.HttpRequestService;
import com.tetransfiro.services.application.PaymentLinkService;
import com.tetransfiro.services.model.exception.TTPaymentLinkRequestException;
import com.tetransfiroservices.utils.TestUtils;

@SpringBootTest(classes = TetransfiroServicesApplication.class)
public class PaymentLinkServiceTests {

	private static final String ERROR = "error";

	@MockBean
	private HttpRequestService httpRequestService;

	@MockBean
	private CloseableHttpClient closeableHttpClient;

	@Autowired
	private PaymentLinkService paymentLinkService;

	@Test
	public void shouldThrowAnExceptionWhenResponseIsAnError() throws Exception {
		var personTotal = TestUtils.createPersonTotalForTest();
		when(httpRequestService.sendAutheticatedPostRequestTo(any(), any(), any(), any(), any())).thenReturn(ERROR);

		assertThrows(TTPaymentLinkRequestException.class,
		             () -> paymentLinkService.generatePaymentLink(personTotal, closeableHttpClient));
	}

	@Test
	public void shouldThrowAnExceptionWhenHttpRequestServiceThrowsIOException() throws Exception {
		var personTotal = TestUtils.createPersonTotalForTest();
		when(httpRequestService.sendAutheticatedPostRequestTo(any(),
		                                                      any(),
		                                                      any(),
		                                                      any(),
		                                                      any())).thenThrow(IOException.class);

		assertThrows(TTPaymentLinkRequestException.class,
		             () -> paymentLinkService.generatePaymentLink(personTotal, closeableHttpClient));
	}

	@Test
	public void shouldThrowAnExceptionWhenHttpRequestServiceThrowsAuthenticationException() throws Exception {
		var personTotal = TestUtils.createPersonTotalForTest();
		when(httpRequestService.sendAutheticatedPostRequestTo(any(),
		                                                      any(),
		                                                      any(),
		                                                      any(),
		                                                      any())).thenThrow(AuthenticationException.class);

		assertThrows(TTPaymentLinkRequestException.class,
		             () -> paymentLinkService.generatePaymentLink(personTotal, closeableHttpClient));
	}
}
