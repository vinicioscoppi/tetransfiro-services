package com.tetransfiro.services.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CurrencyConverter {

	public BigInteger determineValueInInr(BigDecimal valueInBrl) {
		return valueInBrl.multiply(BigDecimal.valueOf(1670)).toBigInteger();
	}
}
