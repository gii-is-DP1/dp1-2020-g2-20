package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Ingrediente;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.service.IngredienteService;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(value = "/ingrediente")
public class IngredienteController {
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private ProductoFormatter productoFormatter;
	@Autowired
	private ProductoService productoService;
	
	public IngredienteController(IngredienteService ingredienteService, ProductoFormatter productoFormatter,
			ProductoService productoService) {
		super();
		this.ingredienteService = ingredienteService;
		this.productoFormatter = productoFormatter;
		this.productoService = productoService;
	}

	@GetMapping()
	public String listadoIngrediente(ModelMap modelMap) {
		String vista= "ingrediente/listaIngredientes";
		Iterable<Ingrediente> inglist = ingredienteService.findAll();
		modelMap.addAttribute("ingrediente",inglist);
		return vista;	
	}
	
	@GetMapping(path="/new")
	public String crearIngrediente(ModelMap modelMap) {
		String vista= "ingrediente/newIngrediente";
		Collection<Producto> listaProd= (Collection<Producto>) productoService.findAll();
		modelMap.addAttribute("ingrediente",new Ingrediente());
		modelMap.addAttribute("listaProductos", listaProd);
		return vista;
	}
	
	@PostMapping(path="/save")
	public String guardarIngrediente(@Valid Ingrediente ing,BindingResult result,ModelMap modelMap) throws ParseException {
		String vista= "ingrediente/listaIngredientes";
		final Ingrediente ingFinal = ing;
		ingFinal.setProducto(productoFormatter.parse(ing.getProducto().getName(), Locale.ENGLISH));
		if(result.hasErrors()) {
			log.info(String.format("Ingredient with name %s wasn't able to be created", ing.getProducto().getName()));
			return "platosPedido/newPlatosPedido";
		}else {
			ingredienteService.save(ingFinal);
			modelMap.addAttribute("message", "Guardado Correctamente");
			vista=listadoIngrediente(modelMap);
		}
		return vista; 
	}

	@GetMapping(path="/delete/{ingId}")
	public String borrarIngrediente(@PathVariable("ingId") int ingId, ModelMap modelMap) {
		String vista= "ingrediente/listaIngredientes";
		Optional<Ingrediente> pp= ingredienteService.findById(ingId);
		if(pp.isPresent()) {
			ingredienteService.deleteById(ingId);
			modelMap.addAttribute("message", "Borrado Correctamente");
			vista=listadoIngrediente(modelMap);
		}else {
			modelMap.addAttribute("message", "Ingrediente no encontrado");
			vista=listadoIngrediente(modelMap);
		}
		return vista;
	}
}
