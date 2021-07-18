package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Plato;

public interface PlatoRepository extends CrudRepository<Plato, Integer>{
	
	List<Plato> findByDisponibleTrue() throws DataAccessException;
	
}
