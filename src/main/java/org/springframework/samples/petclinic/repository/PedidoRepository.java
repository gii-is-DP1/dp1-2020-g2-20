package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Pedido;

public interface PedidoRepository extends CrudRepository<Pedido, Integer>{

	Iterable<Pedido> findByProveedorId(Integer proveedorId);
	
}
