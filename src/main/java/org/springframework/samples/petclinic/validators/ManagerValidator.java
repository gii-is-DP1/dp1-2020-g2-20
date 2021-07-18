package org.springframework.samples.petclinic.validators;

import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ManagerValidator implements Validator{
	private AuthoritiesService authoritiesService;

	public ManagerValidator(AuthoritiesService authoritiesService) {
		super();
		this.authoritiesService = authoritiesService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Manager.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Object nombreOwner = SecurityContextHolder.getContext().getAuthentication().getName();
		Manager manager = (Manager) target;
		
			if (authoritiesService.findAllUsernames().contains(manager.getUsuario())){
				errors.rejectValue("usuario", "este usuario ya está en uso", "este usuario ya está en uso");
		}
	}
}
