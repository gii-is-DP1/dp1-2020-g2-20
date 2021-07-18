package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.PlatoPedido;

public interface PlatoPedidoRepository extends CrudRepository<PlatoPedido, Integer>{

	@Query("SELECT pp FROM PlatoPedido pp WHERE NOT(pp.comanda IS null)")
	List<PlatoPedido> platosPedidosDesponibles() throws DataAccessException;
	
}
