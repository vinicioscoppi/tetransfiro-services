package com.tetransfiro.services;

import java.util.Properties;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class TetransfiroServicesApplication {

	@Value("${queue.purchase.name}")
	private String purchaseQueue;
	
	@Value("${spring.mail.username}")
	private String emailUsername;
	
	@Value("${spring.mail.password}")
	private String emaiPassword;

	@Bean
	public Queue queue() {
		return new Queue(purchaseQueue, true);
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    var mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername(emailUsername);
	    mailSender.setPassword(emaiPassword);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TetransfiroServicesApplication.class, args);
	}

}
