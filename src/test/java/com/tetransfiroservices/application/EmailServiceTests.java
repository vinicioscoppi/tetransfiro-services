package com.tetransfiroservices.application;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.tetransfiro.services.TetransfiroServicesApplication;
import com.tetransfiro.services.application.EmailService;
import com.tetransfiro.services.model.entities.PersonTotal;
import com.tetransfiroservices.utils.TestUtils;

@SpringBootTest(classes = TetransfiroServicesApplication.class)
public class EmailServiceTests {

	@Autowired
	private EmailService emailService;

	@MockBean
	private JavaMailSender javaMailSender;

	private static final String DEFAULT_SUBJECT = "(Te transfiro!) Sua cobrança chegou!";
	private static final String DEFAULT_MESSAGE = "%s, você está devendo R$%s.%n%nInfelizmente, não foi possível gerar um link de pagamento :(%nEntre em contato com seus colegas.";

	@Test
	public void shouldSendExpectedMessageWhenAValidPersonTotalIsReceived() {
		var personTotal = TestUtils.createPersonTotalForTest();
		var expectedMessage = createExpectedFormattedMessageForTest(personTotal);

		emailService.sendSupportEmail(personTotal);

		verify(javaMailSender, times(1)).send(expectedMessage);
	}

	private SimpleMailMessage createExpectedFormattedMessageForTest(PersonTotal personTotal) {
		var message = new SimpleMailMessage();
		message.setTo(personTotal.getEmail());
		message.setSubject(DEFAULT_SUBJECT);
		message.setText(String.format(DEFAULT_MESSAGE,
		                              personTotal.getPersonFullName(),
		                              personTotal.getTotal().toString()));
		return message;
	}
}
