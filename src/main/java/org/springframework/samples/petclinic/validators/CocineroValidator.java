package org.springframework.samples.petclinic.validators;

import org.springframework.samples.petclinic.model.Cocinero;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CocineroValidator implements Validator{

	private AuthoritiesService authoritiesService;

	public CocineroValidator(AuthoritiesService authoritiesService) {
		super();
		this.authoritiesService = authoritiesService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Cocinero.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		Cocinero cocinero = (Cocinero) target;
		
			if (authoritiesService.findAllUsernames().contains(cocinero.getUsuario()) ){
				errors.rejectValue("usuario", "este usuario ya está en uso", "este usuario ya está en uso");
			
		}
	}
}
