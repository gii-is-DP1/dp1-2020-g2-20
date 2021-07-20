package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.LineaPedido;
import org.springframework.samples.petclinic.model.Pedido;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.repository.LineaPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LineaPedidoService {
	
	private LineaPedidoRepository lineaPedidoRepository;
	
	@Autowired
	public LineaPedidoService(LineaPedidoRepository lineaPedidoRepository) {
		super();
		this.lineaPedidoRepository = lineaPedidoRepository;
	}

	public Iterable<LineaPedido> findAll(){
		return lineaPedidoRepository.findAll();
	}
	
	public Optional<LineaPedido> findById(Integer id) {
		return lineaPedidoRepository.findById(id);
	}
	
	public Iterable<LineaPedido> findByProductoId(int productoID) {
		return lineaPedidoRepository.findByProductoId(productoID);
	}
	
	public Iterable<LineaPedido> findByPedidoId(int pedidoID) {
		return lineaPedidoRepository.findByPedidoId(pedidoID);
	}
	
	public int count() {
		return (int) lineaPedidoRepository.count();	
	}
	
	@Transactional
	public void save(LineaPedido lineaPedido) throws DataAccessException {
		lineaPedidoRepository.save(lineaPedido);
		log.info(String.format("Order with product %s has been created", lineaPedido.getProducto().getName()));
	}
	
	@Transactional
	public void deleteById(Integer id) {
		lineaPedidoRepository.deleteById(id);
	}

	@Transactional
	public LineaPedido anadirLineaPedido(Producto producto, Pedido pedido) throws DataAccessException {
		LineaPedido res = new LineaPedido();
		Integer cantidad = (int) (producto.getCantMax()-producto.getCantAct());
		res.setProducto(producto);
		res.setCantidad(cantidad);
		res.setPedido(pedido);
		return res;
	}
}
