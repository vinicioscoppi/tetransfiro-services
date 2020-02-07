package com.tetransfiro.services.model.events;

import java.util.Collection;

import org.springframework.lang.NonNull;

import com.tetransfiro.services.model.entities.PersonTotal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseTotalDeterminedEvent {

	private String name = "PurchaseTotalDeterminedEvent";

	@NonNull
	private Collection<PersonTotal> personTotals;

	public PurchaseTotalDeterminedEvent(Collection<PersonTotal> personTotals) {
		this.personTotals = personTotals;
	}

}
