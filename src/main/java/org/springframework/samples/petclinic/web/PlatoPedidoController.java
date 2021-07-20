package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.EstadoPlato;
import org.springframework.samples.petclinic.model.IngredientePedido;
import org.springframework.samples.petclinic.model.PlatoPedido;
import org.springframework.samples.petclinic.model.PlatoPedidoDTO;
import org.springframework.samples.petclinic.service.EstadoPlatoService;
import org.springframework.samples.petclinic.service.IngredientePedidoService;
import org.springframework.samples.petclinic.service.IngredienteService;
import org.springframework.samples.petclinic.service.PlatoPedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/platopedido")
public class PlatoPedidoController {
	
	private PlatoPedidoService platoPedidoService;
	private IngredientePedidoService ingredientePedidoService;
	private IngredienteService ingredienteService;
	private EstadoPlatoService estadoPlatoService;
	private PlatoPedidoConverter ppConverter;
	private EstadoPlatoFormatter estadoPlatoFormatter;
	private PlatoFormatter platoFormatter;
	
	@Autowired
	public PlatoPedidoController(PlatoPedidoService platoPedidoService,
			IngredientePedidoService ingredientePedidoService, IngredienteService ingredienteService,
			EstadoPlatoService estadoPlatoService, PlatoPedidoConverter ppConverter,
			EstadoPlatoFormatter estadoPlatoFormatter, PlatoFormatter platoFormatter) {
		super();
		this.platoPedidoService = platoPedidoService;
		this.ingredientePedidoService = ingredientePedidoService;
		this.ingredienteService = ingredienteService;
		this.estadoPlatoService = estadoPlatoService;
		this.ppConverter = ppConverter;
		this.estadoPlatoFormatter = estadoPlatoFormatter;
		this.platoFormatter = platoFormatter;
	}

	@ModelAttribute("estadoplatopedido") // Esto pertenece a EstadoPlato
	public Collection<EstadoPlato> poblarEstadosPlato() {
		return estadoPlatoService.findAll();
		
	}

	@GetMapping()
	public String listadoPlatosPedido(ModelMap modelMap) {
		String vista = "platosPedido/listaPlatosPedido";
		Iterable<PlatoPedido> pp = platoPedidoService.platosPedidosDesponibles();
		modelMap.addAttribute("platopedido", pp);
		return vista;
	}

	@PostMapping(path = "{comandaId}/save")
	public String guardarPP(@PathVariable("comandaId") int comandaId, @Valid PlatoPedidoDTO ppDTO,
			BindingResult result, ModelMap modelMap) throws ParseException {	
		if(ppDTO.getPlatodto() == null) {
			return "redirect:/comanda/listaComandaActual/"+comandaId;
		}
		String vista = "platosPedido/listaPlatosPedido";
		final PlatoPedido ppFinal = ppConverter.convertPPDTOToEntity(ppDTO);
		ppFinal.setEstadoplato(estadoPlatoFormatter.parse(ppDTO.getEstadoplatodto(), Locale.ENGLISH));
		ppFinal.setPlato(platoFormatter.parse(ppDTO.getPlatodto(), Locale.ENGLISH));
		if (result.hasErrors()) {
			modelMap.addAttribute("message", "ha habido un error al guardar" + result.getAllErrors().toString());
			vista = initUpdatePPForm(comandaId, ppFinal.getId(), modelMap);
		} else {
			platoPedidoService.save(ppFinal);
			modelMap.addAttribute("message", "successfuly saved");
			modelMap.addAttribute("comandaId", comandaId);
			vista = initUpdatePPForm(comandaId, ppFinal.getId(), modelMap);
		}
		return vista;
	}

	@GetMapping(value = "/comanda/{comandaId}/{ppId}")
	public String initUpdatePPForm(@PathVariable("comandaId") int comandaId, @PathVariable("ppId") int ppId,
			ModelMap model) {
		String vista = "platosPedido/modificarIngredientes";
		Collection<EstadoPlato> collectionEstadosPlato = estadoPlatoService.findAll();

		Collection<String> listaPlatos = new ArrayList<String>();

		PlatoPedido pp = platoPedidoService.findById(ppId).get();
		listaPlatos.add(pp.getPlato().getName());

		// Asignación de ingredientespedidos a plato pedido
		Collection<IngredientePedido> ingredientes = new ArrayList<>();
		ingredientes = platoPedidoService.CrearIngredientesPedidos(pp);

		model.addAttribute("comandaId", comandaId);
		model.addAttribute("estadosPlato", collectionEstadosPlato);
		model.addAttribute("listaPlatos", listaPlatos);
		model.addAttribute("platopedidoId", ppId);
		model.addAttribute("ingredientespedido", ingredientes);
		return vista;
	}

	@PostMapping(value = "{comandaId}/guardarIngrediente/{ppId}/{ingId}")
	public String guardarIngrediente(@PathVariable("comandaId") int comandaId, @PathVariable("ppId") int ppId,
			@PathVariable("ingId") int ingId, @Valid IngredientePedido ingredientePedido, BindingResult result,
			ModelMap modelMap) throws ParseException {
		
			ingredientePedido.setIngrediente(ingredienteService.findById(ingId).get());
			ingredientePedido.setPp(platoPedidoService.findById(ppId).get());
			ingredientePedidoService.save(ingredientePedido);
			log.info(String.format("IngredientOrder with ingredient %s and amount %f has been saved", ingredientePedido.getIngrediente().getProducto().getName(), ingredientePedido.getCantidadPedida()));
			modelMap.addAttribute("message", "Añadido");
			return initUpdatePPForm(comandaId, ppId, modelMap);
		
	}

	// parte correspondiente a ingrediente pedido
	@GetMapping("/{ppId}")
	public String showIngredientePedido(@PathVariable("ppId") int ppId, ModelMap model) {
		PlatoPedido pp = platoPedidoService.findById(ppId).get();
		model.addAttribute("platopedido", pp);
		List<IngredientePedido> ls = ingredientePedidoService.findByPlatoPedidoId(ppId);
		model.addAttribute("ingredientespedido", ls);
		return "platosPedido/ingredientesDePlatoPedido";

	}

	// Modifica el estado del plato al siguiente
	@GetMapping(path = "/modificarEstado/{platopedidoID}/{cambiarA}")
	public String Stock(@PathVariable("platopedidoID") Integer ppId, @PathVariable("cambiarA") String estado,
			ModelMap model) throws ParseException {

		Optional<PlatoPedido> pp = platoPedidoService.findById(ppId);
		if (pp.isPresent()) {
			PlatoPedido p = pp.get();
			p.setEstadoplato(estadoPlatoFormatter.parse(estado, Locale.ENGLISH));

			this.platoPedidoService.save(p);
			model.addAttribute("message", "Se ha cambiado el plato con exito");
			String vista = listadoPlatosPedido(model);
			return vista;
		} else {
			model.addAttribute("message", "NO Se ha cambiado el plato con exito");
			String vista = listadoPlatosPedido(model);
			return vista;
		}
	}
}