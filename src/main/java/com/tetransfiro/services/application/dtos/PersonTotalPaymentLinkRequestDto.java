package com.tetransfiro.services.application.dtos;

import java.math.BigInteger;

import com.tetransfiro.services.model.entities.PersonTotal;
import com.tetransfiro.services.utils.CurrencyConversor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonTotalPaymentLinkRequestDto {

	private static final String DEFAULT_DESCRIPTION = "(Te transfiro!) Esse é o valor do seu lanche.";

	private PersonTotalCustomerPaymentLinkRequestDto customer;
	private String type = "link";
	private String currency = "INR";

	private String description;
	private BigInteger amount;

	@Getter
	@AllArgsConstructor
	public class PersonTotalCustomerPaymentLinkRequestDto {
		private String name;
		private String email;
	}

	public PersonTotalPaymentLinkRequestDto(PersonTotal personTotal) {
		this.description = DEFAULT_DESCRIPTION + personTotal.getPersonFullName();
		this.amount = new CurrencyConversor().determineValueInIrl(personTotal.getTotal());
		this.customer = new PersonTotalCustomerPaymentLinkRequestDto(personTotal.getPersonFullName(),
		                                                             personTotal.getEmail());
	}
}