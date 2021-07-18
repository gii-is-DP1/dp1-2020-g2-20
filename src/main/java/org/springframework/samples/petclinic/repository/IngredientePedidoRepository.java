package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Ingrediente;
import org.springframework.samples.petclinic.model.IngredientePedido;

public interface IngredientePedidoRepository extends CrudRepository<IngredientePedido, Integer>{
	
	Collection<IngredientePedido> findAll();
	
	@Query(value = "SELECT i.* FROM INGREDIENTE i "
			+ "INNER JOIN INGREDIENTE_PEDIDO ip ON i.id = ip.ingrediente_id "
			+ "WHERE ip.id = :ingrediente_pedido_id%", nativeQuery = true)
	Ingrediente ingredienteAsociado(@Param("ingrediente_pedido_id") Integer ingrediente_pedido_id) throws DataAccessException;
}
