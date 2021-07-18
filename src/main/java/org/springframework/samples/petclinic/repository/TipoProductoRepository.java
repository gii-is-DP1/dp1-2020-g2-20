package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.TipoProducto;

public interface TipoProductoRepository extends CrudRepository<TipoProducto, Integer>{
	
	Collection<TipoProducto> findAll();

}
