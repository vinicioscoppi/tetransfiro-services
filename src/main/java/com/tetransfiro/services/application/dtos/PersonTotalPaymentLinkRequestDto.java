package com.tetransfiro.services.application.dtos;

import java.math.BigInteger;

import com.tetransfiro.services.model.entities.PersonTotal;
import com.tetransfiro.services.utils.CurrencyConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonTotalPaymentLinkRequestDto {

	private static final String DEFAULT_DESCRIPTION = "(Te transfiro!) %s, sua cobrança chegou!";

	private CustomerDto customer;
	private String type = "link";
	private String currency = "INR";

	private String description;
	private BigInteger amount;

	@Getter
	@AllArgsConstructor
	public class CustomerDto {
		private String name;
		private String email;
	}

	public PersonTotalPaymentLinkRequestDto(PersonTotal personTotal) {
		this.description = String.format(DEFAULT_DESCRIPTION, personTotal.getPersonFullName());
		this.amount = new CurrencyConverter().determineValueInInr(personTotal.getTotal());
		this.customer = new CustomerDto(personTotal.getPersonFullName(),
		                                                             personTotal.getEmail());
	}
}