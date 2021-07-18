package org.springframework.samples.petclinic.validators;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.samples.petclinic.model.Comanda;
import org.springframework.samples.petclinic.model.IngredientePedido;
import org.springframework.samples.petclinic.model.PlatoPedido;

public class NumeroMesaValidator implements ConstraintValidator<NumeroMesaConstraint, Comanda>{
	
	@Override
	public boolean isValid(Comanda comanda, ConstraintValidatorContext context) {
		Boolean res = true;
		if(comanda.equals(null)||comanda.getMesa()<1||comanda.getMesa()>20) 
		    res = false;
			
		return res;
		
		
	}

}
