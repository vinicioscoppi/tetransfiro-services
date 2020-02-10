package com.tetransfiro.services.model.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonTotal {

	@NonNull
	private String personFullName;

	@NonNull
	private String email;

	@NonNull
	private BigDecimal total;
}
