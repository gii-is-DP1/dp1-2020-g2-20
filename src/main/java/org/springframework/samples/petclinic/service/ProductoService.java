package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.LineaPedido;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.samples.petclinic.repository.LineaPedidoRepository;
import org.springframework.samples.petclinic.repository.ProductoRepository;
import org.springframework.samples.petclinic.service.exceptions.PedidoPendienteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ProductoService {
	@Autowired
	private ProductoRepository productoRepository;
	@Autowired
	private LineaPedidoRepository lineaPedidoRepository;
	
	public ProductoService(ProductoRepository productoRepository, LineaPedidoRepository lineaPedidoRepository) {
		super();
		this.productoRepository = productoRepository;
		this.lineaPedidoRepository = lineaPedidoRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Producto> findAll() throws DataAccessException {
		return productoRepository.findAll();
	}	
	
	@Transactional
	public Optional<Producto> findById(Integer id) {
		return productoRepository.findById(id);
	}
	
	//Esto se usa al realizar un pedido pues se necesita la lista de productos con el mismo proveedor
	@Transactional
	public Collection<Producto> findByProveedor(Producto producto) throws DataAccessException {
		Proveedor proveedor = producto.getProveedor();
		return productoRepository.findByProveedor(proveedor);
	}

	@Transactional
	public Producto save(Producto producto) {
		log.info(String.format("Product with name %s has been saved", producto.getName()));
		return productoRepository.save(producto);
	}
	
	@Transactional(rollbackFor = PedidoPendienteException.class)
	public void deleteById(Integer id) throws DataAccessException, PedidoPendienteException {
		Iterable<LineaPedido> LineasPedido = lineaPedidoRepository.findByProductoId(id);
		Iterator<LineaPedido> it = LineasPedido.iterator();
		Boolean HaypedidoPendiente = false;
		while(it.hasNext()) {
			LineaPedido p = it.next();
		    if (p.getPedido().getHaLlegado()==false) {
		    	HaypedidoPendiente = true;
	    	}		
		}if (HaypedidoPendiente)  {    
			throw new PedidoPendienteException();
		}else {
		Producto producto = productoRepository.findById(id).get();
		productoRepository.deleteById(id);
		log.info(String.format("Product with name %s has been deleted", producto.getName()));
		}
	}
}
