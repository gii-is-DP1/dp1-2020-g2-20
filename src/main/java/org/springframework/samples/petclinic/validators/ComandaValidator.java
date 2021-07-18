package org.springframework.samples.petclinic.validators;

import org.springframework.samples.petclinic.model.Comanda;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ComandaValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Comanda.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	}

}