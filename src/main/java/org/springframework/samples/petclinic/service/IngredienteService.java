package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Ingrediente;
import org.springframework.samples.petclinic.repository.IngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class IngredienteService {
	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	public IngredienteService(IngredienteRepository ingredienteRepository) {
		super();
		this.ingredienteRepository = ingredienteRepository;
	}

	@Transactional
	public Iterable<Ingrediente> findAll() {
		return ingredienteRepository.findAll();
	}
	
	@Transactional
	public Optional<Ingrediente> findById(Integer id) {
		return ingredienteRepository.findById(id);
	}
	
	@Transactional
	public List<Ingrediente> findByPlatoId(Integer id){
		Collection<Ingrediente> ls= ingredienteRepository.findAll();
		List<Ingrediente> res= new ArrayList<Ingrediente>();
		
 		for(Ingrediente l: ls) {
			if(l.getPlato().getId()==id) {
				res.add(l);
			}
		}
 		return res;
	}
	
	@Transactional
	public Ingrediente save(Ingrediente ing) {
		log.info(String.format("Ingredient with name %s and amount %f has been saved", ing.getProducto().getName(), ing.getCantidadUsualPP()));
		return ingredienteRepository.save(ing);
	}
	
	@Transactional
	public void deleteById(Integer id) {
		Ingrediente ing = ingredienteRepository.findById(id).get();
		ingredienteRepository.deleteById(id);
		log.info(String.format("Ingredient with name %s and amount %f has been deleted", ing.getProducto().getName(), ing.getCantidadUsualPP()));
	}
	
	@Transactional
	public void deleteByPlatoId(Integer id){
		Collection<Ingrediente> ls= ingredienteRepository.findAll();	
 		for(Ingrediente l: ls) {
			if(l.getPlato().getId()==id) {
				deleteById(l.getId());
			}
		}
	}
}
