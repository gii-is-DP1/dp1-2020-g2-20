package org.springframework.samples.petclinic.web;

import java.util.Iterator;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cocinero;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CocineroService;
import org.springframework.samples.petclinic.validators.CocineroValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping(value = "/cocinero")
public class CocineroController {
	@Autowired
	private CocineroService cocineroService;
	@Autowired
	private AuthoritiesService authoritiesService;
	
	public CocineroController(CocineroService cocineroService, AuthoritiesService authoritiesService) {
		super();
		this.cocineroService = cocineroService;
		this.authoritiesService = authoritiesService;
	}
	
	@InitBinder("cocinero")
	public void initCocineroBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new CocineroValidator(this.authoritiesService));
	}
	
	@GetMapping()
	public String listadoCocinero(ModelMap modelMap) {
		String vista = "cocinero/listaCocinero";
		Iterable<Cocinero> cocinero = cocineroService.findAll();
		Iterator<Cocinero> it_cocinero = cocinero.iterator();

		if (!(it_cocinero.hasNext())) {
			modelMap.addAttribute("message", "No hay cocineros, contrata a alguien y crea su Ficha de Empleado");
		}
		modelMap.addAttribute("cocinero", cocinero);

		return vista;

	}

	@GetMapping(path = "/new")
	public String crearCocinero(ModelMap modelMap) {
		String vista = "cocinero/editCocinero";
		modelMap.addAttribute("cocinero", new Cocinero());
		return vista;
	}

	@PostMapping(path = "/save")
	public String save(@Valid Cocinero cocinero, BindingResult result, ModelMap modelMap) {
		String vista = "cocinero/listaCocinero";
		if (result.hasErrors()) {
			log.info(String.format("Chef with name %s wasn't able to be created", cocinero.getName()));
			modelMap.addAttribute("cocinero", cocinero);
			return "cocinero/editCocinero";
		} else {
			cocineroService.save(cocinero);
			modelMap.addAttribute("message", "Guardado Correctamente");
			vista = listadoCocinero(modelMap);
		}
		return vista;

	}

	@GetMapping(path = "/delete/{cocineroId}")
	public String deleteById(@PathVariable("cocineroId") int cocineroId, ModelMap modelMap) {
		String vista = "cocinero/listaCocinero";
		Optional<Cocinero> cam = cocineroService.findById(cocineroId);
		if (cam.isPresent()) {
			cocineroService.deleteById(cocineroId);
			modelMap.addAttribute("message", "Borrado Correctamente");
			vista = listadoCocinero(modelMap);
		} else {
			modelMap.addAttribute("message", "Cocinero no encontrado");
			vista = listadoCocinero(modelMap);
		}
		return vista;
	}

	@GetMapping(value = "/edit/{cocineroId}")
	public String initUpdateCocineroForm(@PathVariable("cocineroId") int cocineroId, ModelMap model) {
		String vista = "cocinero/editarCocinero";

		Cocinero cocinero = cocineroService.findById(cocineroId).get();
		model.addAttribute(cocinero);
		return vista;
	}

	@PostMapping(value = "/edit")
	public String processUpdateCocineroForm(@Valid Cocinero cocinero, BindingResult result, ModelMap modelMap) {
		if(this.cocineroService.cocineroConMismoUsuario(cocinero)) {
			result = this.cocineroService.erroresSinMismoUser(cocinero, result);
		}
		if (result.hasErrors()) {
			log.info(String.format("Chef with name %s and ID %d wasn't able to be updated", cocinero.getName(), cocinero.getId()));
			modelMap.addAttribute("cocinero", cocinero);
			return "cocinero/editarCocinero";
		}else {
			this.cocineroService.save(cocinero);
			return "redirect:/cocinero";
		}
	}
}
