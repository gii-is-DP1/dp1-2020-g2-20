package org.springframework.samples.petclinic.validators;

import org.springframework.samples.petclinic.model.Camarero;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CamareroValidator implements Validator{
	
	private AuthoritiesService authoritiesService;

	public CamareroValidator(AuthoritiesService authoritiesService) {
		super();
		this.authoritiesService = authoritiesService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Camarero.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		Camarero camarero = (Camarero) target;
		
			if (authoritiesService.findAllUsernames().contains(camarero.getUsuario())){
				errors.rejectValue("usuario", "este usuario ya está en uso", "este usuario ya está en uso");
			
		}
	}

}
