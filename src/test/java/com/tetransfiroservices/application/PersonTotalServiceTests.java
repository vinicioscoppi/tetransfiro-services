package com.tetransfiroservices.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tetransfiro.services.TetransfiroServicesApplication;
import com.tetransfiro.services.application.EmailService;
import com.tetransfiro.services.application.PaymentLinkService;
import com.tetransfiro.services.application.PersonTotalService;
import com.tetransfiro.services.model.entities.PersonTotal;
import com.tetransfiro.services.model.exception.TTPaymentLinkRequestException;
import com.tetransfiroservices.utils.TestUtils;

@SpringBootTest(classes = TetransfiroServicesApplication.class)
public class PersonTotalServiceTests {

	@Autowired
	private PersonTotalService personTotalService;

	@MockBean
	private PaymentLinkService paymentLinkService;

	@MockBean
	private EmailService emailService;

	@Test
	public void shouldSendSupportEmailWhenPaymentLinkServiceThrowsAnException() {
		var personTotalList = TestUtils.createPersonTotalListForTest();
		doThrow(TTPaymentLinkRequestException.class).when(paymentLinkService)
		                                            .generatePaymentLink(any(PersonTotal.class),
		                                                                 any(CloseableHttpClient.class));

		personTotalService.dispatchPaymentLinks(personTotalList);

		verify(emailService, times(personTotalList.size())).sendSupportEmail(any(PersonTotal.class));

	}

	@Test
	public void shouldRequestAPaymentLinkForEachPersonTotal() {
		var personTotalList = TestUtils.createPersonTotalListForTest();

		personTotalService.dispatchPaymentLinks(personTotalList);

		verify(paymentLinkService, times(personTotalList.size())).generatePaymentLink(any(PersonTotal.class),
		                                                                              any(CloseableHttpClient.class));
	}
}
