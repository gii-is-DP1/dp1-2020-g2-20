package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Integer> {
	
	Collection<Ingrediente> findAll();
	
}

