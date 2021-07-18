package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Ingrediente;
import org.springframework.samples.petclinic.model.IngredientePedido;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.repository.IngredientePedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class IngredientePedidoService {
	@Autowired
	private IngredientePedidoRepository ingredientePedidoRepository;
	@Autowired
	private ProductoService productoService;
	
	public IngredientePedidoService(IngredientePedidoRepository ingredientePedidoRepository,
			ProductoService productoService) {
		super();
		this.ingredientePedidoRepository = ingredientePedidoRepository;
		this.productoService = productoService;
	}

	@Transactional
	public List<IngredientePedido> findByPlatoPedidoId(Integer id) {
		Collection<IngredientePedido> ls = ingredientePedidoRepository.findAll();
		List<IngredientePedido> res = new ArrayList<IngredientePedido>();
		for (IngredientePedido l : ls) {
			if (l.getPp() != null) {
				if (l.getPp().getId() == id) {
					res.add(l);
				}
			}
		}
		return res;
	}

	@Transactional
	public IngredientePedido save(IngredientePedido ing) {
		Double cantidad = ing.getCantidadPedida();
		Producto prod = ing.getIngrediente().getProducto();
		prod.setCantAct(prod.getCantAct()-cantidad);
		productoService.save(prod);
		ingredientePedidoRepository.save(ing);
		return ing;
	}
	
	@Transactional
	public void deleteById(Integer id) {
		IngredientePedido ip = ingredientePedidoRepository.findById(id).get();
		ingredientePedidoRepository.deleteById(id);
		log.info(String.format("IngredientOrder with ingredient %s and amount %f has been delete", ip.getIngrediente().getProducto().getName(), ip.getCantidadPedida()));	
	}
	
	@Transactional
	public IngredientePedido crearIngredientePedidoPorIngrediente(Ingrediente i) {
		IngredientePedido ip = new IngredientePedido();
		ip.setCantidadPedida(i.getCantidadUsualPP());
		ip.setIngrediente(i);
		return ip;
	}
	
	@Transactional(readOnly = true)
	public Ingrediente ingredienteAsociado(Integer ingrediente_pedido_id) throws DataAccessException{
		return ingredientePedidoRepository.ingredienteAsociado(ingrediente_pedido_id);
	}
}
