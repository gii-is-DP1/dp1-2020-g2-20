package org.springframework.samples.petclinic.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Camarero;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.CamareroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CamareroService {
	
	private UserService userService;
	private CamareroRepository camareroRepository;
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public CamareroService(UserService userService, CamareroRepository camareroRepository,
			AuthoritiesService authoritiesService) {
		super();
		this.userService = userService;
		this.camareroRepository = camareroRepository;
		this.authoritiesService = authoritiesService;
	}

	public Iterable<Camarero> findAll() {
		return camareroRepository.findAll();
	}
	
	public Optional<Camarero> findById(int camareroId) throws DataAccessException {
		return this.camareroRepository.findById(camareroId);
	}

	@Transactional
	public void save(Camarero camarero) {
		User user = authoritiesService.crearUsuario(camarero.getUsuario(), camarero.getContrasena());
		if(camarero.getId()!=null) {	
			String antiguo = this.camareroRepository.findById(camarero.getId()).get().getUsuario();
			if(!user.getUsername().equals(antiguo)) {
				userService.deleteUser(userService.findUser(antiguo).get());
			}
		}
		userService.saveUser(user);
		authoritiesService.saveAuthorities(camarero.getUsuario(), "camarero");
		camareroRepository.save(camarero);
		log.info(String.format("Waiter with username %s has been saved", camarero.getUsuario(),
				camarero.getId()));
	}

	@Transactional
	public void deleteById(Integer id) {
		Camarero camarero = camareroRepository.findById(id).get();
		this.userService.deleteUser(this.userService.findUser(camarero.getUsuario()).get());
		camareroRepository.deleteById(id);
		log.info(String.format("Waiter with username %s has been deleted", camarero.getUsuario()));
	}

	// Se usa para asignar un camarero a una comanda dado su usario
	@Transactional(readOnly = true)
	public Camarero findByUser(String user) {
		Camarero camarero = new Camarero();
		Iterable<Camarero> aux = camareroRepository.findAll();
		Iterator<Camarero> it = aux.iterator();
		while (it.hasNext()) {
			camarero = it.next();
			if (camarero.getUsuario().equals(user))
				return camarero;
		}
		return camarero;
	}
	
	@Transactional(readOnly = true)
	public Boolean CamareroConMismoUsuario(Camarero camarero) throws DataAccessException {
		Boolean res=false;
		Integer camareroId= camarero.getId();
		Camarero comp=this.camareroRepository.findById(camareroId).get();
		if(comp.getUsuario().equals(camarero.getUsuario())) {
			res=true;
		}
		return res;
	}
	
	@Transactional(readOnly = true)
	public BindingResult ErroresSinMismoUser(Camarero camarero,BindingResult result) throws DataAccessException {
		List<FieldError> errorsToKeep = result.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("usuario"))
                .collect(Collectors.toList());
		
		result = new BeanPropertyBindingResult(camarero, "camarero");
		for (FieldError fieldError : errorsToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
}