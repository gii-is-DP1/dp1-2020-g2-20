package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Proveedor;

public interface ProductoRepository extends CrudRepository<Producto, Integer>{
	
	Collection<Producto> findAll();
	
	Collection<Producto> findByProveedor(Proveedor proveedor) throws DataAccessException;
	
}
