package com.tetransfiroservices.utils;

import java.math.BigDecimal;
import java.util.List;

import com.tetransfiro.services.model.entities.PersonTotal;

public class TestUtils {

	private static final String FULL_NAME = "FULL_NAME";
	private static final String EMAIL = "email@test.com";
	private static final BigDecimal TOTAL = new BigDecimal("10.00");
	
	public static PersonTotal createPersonTotalForTest() {
		return new PersonTotal(FULL_NAME, EMAIL, TOTAL);
	}
	
	public static List<PersonTotal> createPersonTotalListForTest() {
		return List.of(createPersonTotalForTest(), createPersonTotalForTest());
	}
}
