package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.EstadoPlato;
import org.springframework.samples.petclinic.repository.EstadoPlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstadoPlatoService {
	@Autowired
	private EstadoPlatoRepository estadoPlatoRepository;
	
	public EstadoPlatoService(EstadoPlatoRepository estadoPlatoRepository) {
		super();
		this.estadoPlatoRepository = estadoPlatoRepository;
	}

	@Transactional
	public Collection<EstadoPlato> findAll(){
		return estadoPlatoRepository.findAll();
	}
}
