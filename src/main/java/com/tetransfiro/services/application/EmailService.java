package com.tetransfiro.services.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.tetransfiro.services.model.entities.PersonTotal;

@Component
public class EmailService {
	
	private static final String DEFAULT_SUBJECT = "(Te transfiro!) Sua cobrança chegou!";
	private static final String DEFAULT_MESSAGE = "%s, você está devendo R$%s.%n%nInfelizmente, não foi possível gerar um link de pagamento :(%nEntre em contato com seus colegas.";
	
	@Autowired
	private JavaMailSender emailSender;
	
	public void sendSupportEmail(PersonTotal personTotal) {
		var message = new SimpleMailMessage();
		message.setTo(personTotal.getEmail());
		message.setSubject(DEFAULT_SUBJECT);
		message.setText(getFormattedMessageFor(personTotal));
		emailSender.send(message);
	}

	private String getFormattedMessageFor(PersonTotal personTotal) {
		return String.format(DEFAULT_MESSAGE, personTotal.getPersonFullName(), personTotal.getTotal().toString());
	}
}
