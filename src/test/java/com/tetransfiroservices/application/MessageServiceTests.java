package com.tetransfiroservices.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetransfiro.services.TetransfiroServicesApplication;
import com.tetransfiro.services.application.MessageService;
import com.tetransfiro.services.application.PersonTotalService;
import com.tetransfiro.services.model.entities.PersonTotal;
import com.tetransfiro.services.model.events.PurchaseTotalDeterminedEvent;
import com.tetransfiro.services.model.exception.TTInvalidMessageFormatException;
import com.tetransfiroservices.utils.TestUtils;

@SpringBootTest(classes = TetransfiroServicesApplication.class)
public class MessageServiceTests {

	private static final String INVALID_MESSAGE = "{invalid_message}";

	@MockBean
	private PersonTotalService personTotalService;

	@Autowired
	private MessageService messageService;

	@Test
	public void shouldNotDispatchPaymentLinksWhenReceivedMessageIsNotFromAnExpectedType() throws Exception {
		var receivedMessage = INVALID_MESSAGE;

		messageService.handle(receivedMessage);

		verify(personTotalService, never()).dispatchPaymentLinks(any());
	}

	@Test
	public void shouldNotDispatchPaymentLinksWhenReceivedMessageHasNullValues() throws Exception {
		var receivedMessage = getReceivedMessageForTest(List.of());

		assertThrows(TTInvalidMessageFormatException.class, () -> messageService.handle(receivedMessage));
		verify(personTotalService, never()).dispatchPaymentLinks(any());
	}

	@Test
	public void shouldDispatchPaymentLinksWhenReceivedMessageIsValid() throws Exception {
		var personTotals = TestUtils.createPersonTotalListForTest();
		var receivedMessage = getReceivedMessageForTest(personTotals);

		messageService.handle(receivedMessage);

		verify(personTotalService).dispatchPaymentLinks(personTotals);
	}

	private String getReceivedMessageForTest(Collection<PersonTotal> personTotals) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(new PurchaseTotalDeterminedEvent(personTotals));
	}
}
