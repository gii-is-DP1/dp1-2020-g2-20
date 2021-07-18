package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Proveedor;

public interface ProveedorRepository extends  CrudRepository<Proveedor, Integer>{
	
	@Query("SELECT p.name FROM Proveedor p WHERE p.activo=true")
	List<String> findActivosName() throws DataAccessException; 
	
	@Query("SELECT p.name FROM Proveedor p ORDER BY p.id")
	List<String> findAllNames() throws DataAccessException; 
	
	Iterable<Proveedor> findByActivoTrue() throws DataAccessException; 
	
	Proveedor findByName(String nombre) throws DataAccessException; 
	
}
