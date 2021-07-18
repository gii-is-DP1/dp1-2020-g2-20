package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pedido;
import org.springframework.samples.petclinic.repository.PedidoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public PedidoService(PedidoRepository pedidoRepository) {
		super();
		this.pedidoRepository = pedidoRepository;
	}

	@Transactional
	public Iterable<Pedido> findAll(){
		return pedidoRepository.findAll();
	}
	
	@Transactional
	public Optional<Pedido> findById(Integer id) {
		return pedidoRepository.findById(id);
	} 
	
	@Transactional
	public Iterable<Pedido> findByProveedorId(int proveedorID) {
		return pedidoRepository.findByProveedorId(proveedorID);
	}
	
	@Transactional
	public int count(){
		return (int) pedidoRepository.count();
	}
	
	@Transactional(rollbackFor = DuplicatedPedidoException.class)
	public void save(Pedido pedido) throws DataAccessException, DuplicatedPedidoException {
		Iterable<Pedido> lista = pedidoRepository.findAll();
		Iterator<Pedido> it = lista.iterator();
       	Boolean hayRepetido = false;
       	while(it.hasNext()) {
       		Pedido p = it.next();
			if (p.getProveedor()==pedido.getProveedor()&& p.getHaLlegado()==false) {
				hayRepetido = true;
		    }		
       	}
       	if (hayRepetido)  {    
       		throw new DuplicatedPedidoException();
       	}else {
       		pedidoRepository.save(pedido);
       		log.info(String.format("Order with ID %d has been created", pedido.getId()));
       	}
	}
	
	//Esto es para encontrar los pedidos por un dia de la semana 
	@Transactional
	public Collection<Pedido> encontrarPedidoDia(String dia) throws DataAccessException {
		LocalDate actualDate = LocalDate.parse(dia);
		Collection<Pedido> res = new ArrayList<>();
		Iterable<Pedido> aux = pedidoRepository.findAll();
		Iterator<Pedido> it_aux = aux.iterator();
		while (it_aux.hasNext()) {
			Pedido pedido = it_aux.next();
			if (pedido.getFechaPedido().equals(actualDate)) {
				res.add(pedido);
			}	
		}
		return res; 
	}
}
