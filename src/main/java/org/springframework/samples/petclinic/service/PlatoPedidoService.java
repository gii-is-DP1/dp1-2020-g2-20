package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Ingrediente;
import org.springframework.samples.petclinic.model.IngredientePedido;
import org.springframework.samples.petclinic.model.PlatoPedido;
import org.springframework.samples.petclinic.repository.PlatoPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PlatoPedidoService {
	@Autowired
	private PlatoPedidoRepository platoPedidoRepository;
	@Autowired
	private IngredientePedidoService ingredientePedidoService;
	
	public PlatoPedidoService(PlatoPedidoRepository platoPedidoRepository,
			IngredientePedidoService ingredientePedidoService) {
		super();
		this.platoPedidoRepository = platoPedidoRepository;
		this.ingredientePedidoService = ingredientePedidoService;
	}

	@Transactional
	public Iterable<PlatoPedido> findAll() {
		return platoPedidoRepository.findAll();
	}
	
	@Transactional
	public Optional<PlatoPedido> findById(Integer id) {
		return platoPedidoRepository.findById(id);
	}
	
	@Transactional
	public int count() {
		return (int) platoPedidoRepository.count();
	}

	@Transactional
	public PlatoPedido save(PlatoPedido pp) {
		if(pp.getComanda() != null & pp.getEstadoplato().getId() == 1) {
			log.info(String.format("PlateOrder with name %s has been created for table %s", pp.getPlato().getName(), pp.getComanda().getMesa().toString()));
		}else if(pp.getComanda() != null){
			log.info(String.format("PlateOrder with name %s has been updated to %s", pp.getPlato().getName(), pp.getEstadoplato().getName()));
		}
		return platoPedidoRepository.save(pp);
	}
	
	@Transactional
	public Iterable<PlatoPedido> platosPedidosDesponibles() {
		return platoPedidoRepository.platosPedidosDesponibles();
	}

	@Transactional
	public Collection<IngredientePedido> CrearIngredientesPedidos(PlatoPedido pp) {
		Collection<Ingrediente> ingredienteList = pp.getPlato().getIngredientes();
		Collection<IngredientePedido> lista2 = pp.getIngredientesPedidos();
		Collection<Ingrediente> lista3 = new ArrayList<>();
		if (lista2 != null) {
			Iterator<IngredientePedido> iterator2 = lista2.iterator();
			while (iterator2.hasNext()) {
				IngredientePedido ingredienteEnPP = iterator2.next();
				lista3.add(ingredienteEnPP.getIngrediente());
			}
		}
		Collection<IngredientePedido> res = new ArrayList<>();
		Iterator<Ingrediente> iterator = ingredienteList.iterator();
		while (iterator.hasNext()) {
			Ingrediente i = iterator.next();
			if (!(lista3.contains(i))) {
				IngredientePedido ingp = ingredientePedidoService.crearIngredientePedidoPorIngrediente(i);
				ingp.setPp(pp);
				res.add(ingp);
			}
		}
		return res;
	}
}
