package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.petclinic.model.Ingrediente;
import org.springframework.samples.petclinic.model.Plato;
import org.springframework.samples.petclinic.repository.IngredienteRepository;
import org.springframework.samples.petclinic.repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PlatoService {
	@Autowired
	private PlatoRepository platoRepository;
	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	public PlatoService(PlatoRepository platoRepository, IngredienteRepository ingredienteRepository) {
		super();
		this.platoRepository = platoRepository;
		this.ingredienteRepository = ingredienteRepository;
	}

	@Transactional
	public Collection<Plato> findAll() {
		return  (Collection<Plato>) platoRepository.findAll();	
	}
	
	@Transactional
	public Optional<Plato> findById(Integer id) {
		return platoRepository.findById(id);	
	}

	@Transactional
	public Plato save(Plato plato) {
		log.info(String.format("Plate with name %s has been saved", plato.getName()));
		return platoRepository.save(plato);	
	}
	
	@Transactional
	public void deleteById(Integer id) {
		Plato plato = platoRepository.findById(id).get();
		platoRepository.deleteById(id);	
		log.info(String.format("Plate with name %s has been saved", plato.getName()));
	}
	
	@Transactional
	public boolean ingredienteEstaRepetido(String nombreIng, int platoId) {
		Collection<Ingrediente> ls= ingredienteRepository.findAll();	
		boolean res = false;
		
 		for(Ingrediente l: ls) {
 			if(l.getProducto().getName().equals(nombreIng) && l.getPlato().getId().equals(platoId)) {
 				res= true;
 			}
 		}
 		return res;
	}
	
	//Mostrar platos disponibles para ofrecerlos (elimina platos de los que faltan ingredientes)
	@Transactional
	public List<Plato> findAllAvailable() {
		Boolean falta = false;
		List<Plato> listaPlatos = platoRepository.findByDisponibleTrue();
		List<Plato> res = new ArrayList<>();
		res.addAll(listaPlatos);
		Iterator<Plato> iterator = listaPlatos.iterator();
		while (iterator.hasNext()) {
			falta=false;
			Plato plato = iterator.next();
			Collection<Ingrediente> listaIngredientes = plato.getIngredientes();
			Iterator<Ingrediente> iterator2 = listaIngredientes.iterator();
			while (iterator2.hasNext()) {
				Ingrediente ingrediente = iterator2.next();
				if(ingrediente.getCantidadUsualPP() > ingrediente.getProducto().getCantAct()) {
					falta = true;
				}
			}
			if(falta) {
				res.remove(plato);
			}
		}
		return res;
	}
}
