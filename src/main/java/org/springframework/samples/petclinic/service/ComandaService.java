package org.springframework.samples.petclinic.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Comanda;
import org.springframework.samples.petclinic.model.PlatoPedido;
import org.springframework.samples.petclinic.repository.ComandaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ComandaService {
	@Autowired
	private ComandaRepository comandaRepository;
	@Autowired
	private PlatoPedidoService platoPedidoService;
	@Autowired
	private CamareroService camareroService;
	
	public ComandaService(ComandaRepository comandaRepository, PlatoPedidoService platoPedidoService,
			CamareroService camareroService) {
		super();
		this.comandaRepository = comandaRepository;
		this.platoPedidoService = platoPedidoService;
		this.camareroService = camareroService;
	}

	@Transactional
	public int comandaCount() {
		return (int) comandaRepository.count();	

	}

	@Transactional
	public Iterable<Comanda> findAll() {
		Iterable<Comanda> res = comandaRepository.findAll();
		return res;
	}
	
	@Transactional
	public Optional<Comanda> findById(Integer id) {
		return comandaRepository.findById(id);
	}
	
	@Transactional
	public int count() {
		return (int) comandaRepository.count();	
	}

	@Transactional
	public Comanda save(Comanda comanda) {
		log.info(String.format("Order to table  %d has been saved", comanda.getMesa()));
		return comandaRepository.save(comanda);
	}

	@Transactional
	public Collection<Comanda> encontrarComandaDia(String dia) throws DataAccessException {
		LocalDate actualDate =LocalDate.parse(dia);
		Collection<Comanda> res = new ArrayList<>();
		Iterable<Comanda> aux = comandaRepository.findAll();
		Iterator<Comanda> it_aux = aux.iterator();
		while (it_aux.hasNext()) {
			Comanda comanda = it_aux.next();
			if (comanda.getFechaCreado().toLocalDate().equals(actualDate)) {
				res.add(comanda);
			}	
		}
		return res; 
	}

	@Transactional
	public Collection<Comanda> encontrarComandaActual() throws DataAccessException {
		Collection<Comanda> res = new ArrayList<>();
		Iterable<Comanda> aux = comandaRepository.findAll();
		Iterator<Comanda> it_aux = aux.iterator();
		while (it_aux.hasNext()) {
			Comanda comAux = it_aux.next();
			if (comAux.getFechaFinalizado()==null) {
				res.add(comAux);
			}	
		}
		return res; 
	}

	@Transactional
	public Integer findLastId() throws DataAccessException{
		return comandaRepository.findLastId();
	}

	@Transactional
	public Comanda crearComanda(Integer mesa, Principal user) {
		Comanda comanda = new Comanda();
		comanda.setMesa(mesa);
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setPrecioTotal(0.0);
		comanda.setCamarero(camareroService.findByUser(user.getName()));
		this.save(comanda);
		return comanda;
	}

	@Transactional
	public Collection<PlatoPedido> getPlatosPedidoDeComanda(int comandaID){
		Iterable<PlatoPedido> allPP = platoPedidoService.findAll();
		Iterator<PlatoPedido> it = allPP.iterator();
		Collection<PlatoPedido> platosEC = new ArrayList<>();
		while(it.hasNext()) {
			PlatoPedido ppAux = it.next();
			if(ppAux.getComanda()!=null) {
				if(ppAux.getComanda().getId()==comandaID)
					platosEC.add(ppAux);
			}
		}
		return platosEC;
	}

	@Transactional
	public void anadirComandaAPlato(PlatoPedido plato,Integer comandaId){
		Comanda comanda = this.findById(comandaId).get();
		plato.setComanda(comanda);
		comanda.setPrecioTotal(comanda.getPrecioTotal()+plato.getPlato().getPrecio());
		platoPedidoService.save(plato);
	}

	@Transactional
	public Boolean estaFinalizado(Comanda comanda){
		Boolean res= false;
		Iterator<PlatoPedido> listaPlatosPedidos = comanda.getPlatosPedidos().iterator();
		while(listaPlatosPedidos.hasNext()) {
			PlatoPedido platoPedido = listaPlatosPedidos.next();
			if(!(platoPedido.getEstadoplato().getId()==3)) {
				res= true;
			}
		}
		return res;
	}
}
