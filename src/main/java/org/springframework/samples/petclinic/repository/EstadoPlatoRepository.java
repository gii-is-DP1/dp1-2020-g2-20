package org.springframework.samples.petclinic.repository;


import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.EstadoPlato;

public interface EstadoPlatoRepository extends CrudRepository<EstadoPlato, Integer>{
	
	Collection<EstadoPlato> findAll();

}
