package com.tetransfiro.services.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CurrencyConversor {

	public BigInteger determineValueInIrl(BigDecimal valueInBrl) {
		return valueInBrl.multiply(BigDecimal.valueOf(1670)).toBigInteger();
	}
}
