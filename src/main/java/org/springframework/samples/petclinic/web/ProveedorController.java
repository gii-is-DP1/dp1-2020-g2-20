package org.springframework.samples.petclinic.web;

import java.util.Iterator;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.samples.petclinic.service.ProveedorService;
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
@RequestMapping("/proveedor")
public class ProveedorController {
	@Autowired
	private ProveedorService proveedorService;
	
	public ProveedorController(ProveedorService proveedorService) {
		super();
		this.proveedorService = proveedorService;
	}

	@GetMapping()
	public String listadoDeProveedores(ModelMap modelMap) {
		String vista = "proveedor/listadoDeProveedores";
		Iterable<Proveedor> proveedor = proveedorService.findActivos();
		Iterator<Proveedor> it_proveedor = proveedor.iterator();

		if (!(it_proveedor.hasNext())) {
			modelMap.addAttribute("message",
					"No hay proveedores, necesitas alguno para obtener productos, añade uno nuevo");
		}
		modelMap.addAttribute("proveedor", proveedor);
		return vista;
	}

	@GetMapping(path = "/new")
	public String crearProveedor(ModelMap modelMap) {
		String view = "proveedor/editProveedor";
		modelMap.addAttribute("proveedor", new Proveedor());
		return view;
	}

	@PostMapping(path = "/save")
	public String guardarProveedor(@Valid Proveedor proveedor, BindingResult result, ModelMap modelMap) {
		String view = "proveedor/listadoDeProveedores";
		if (result.hasErrors()) {
			log.info(String.format("Provider with name %s wasn't able to be created", proveedor.getName()));
			modelMap.addAttribute("proveedor", proveedor);
			return "proveedor/editProveedor";
		}else {
			if (proveedorService.esIgual(proveedor.getName())) {
				modelMap.addAttribute("message", "El proveedor ya existe");
				modelMap.addAttribute("proveedor", proveedor);
				return "proveedor/editProveedor";
			} else {
				Proveedor proveedorfinal = proveedorService.findByName(proveedor.getName());
				if (proveedorfinal != null) {
					proveedorfinal.setActivo(true);
					proveedorfinal.setGmail(proveedor.getGmail());
					proveedorfinal.setTelefono(proveedor.getTelefono());
					proveedorService.save(proveedorfinal);
				} else {
					proveedor.setActivo(true);
					proveedorService.save(proveedor);
				}
				modelMap.addAttribute("message", "El proveedor se guardo exitosamente");
				view = listadoDeProveedores(modelMap);
			}
		}
		return view;
	}

	@GetMapping(path = "/delete/{proveedorid}")
	public String borrarProveedor(@PathVariable("proveedorid") int proveedorid, ModelMap modelMap) {
		String view = "proveedor/listadoDeProveedores";
		Optional<Proveedor> proveedor = proveedorService.findById(proveedorid);
		if (proveedor.isPresent()) {
			Proveedor proveedorfinal = proveedor.get();
			proveedorfinal.setActivo(false);
			proveedorService.save(proveedorfinal);
			modelMap.addAttribute("message", "proveedor successfuly deleted");
			view = listadoDeProveedores(modelMap);
		} else {
			modelMap.addAttribute("message", "proveedor not found");
			view = listadoDeProveedores(modelMap);
		}
		return view;
	}

	@GetMapping(value = "/edit/{proveedorId}")
	public String initUpdateProveedorForm(@PathVariable("proveedorId") int proveedorId, ModelMap model) {
		String vista = "proveedor/editarProveedor";
		Proveedor proveedor = proveedorService.findById(proveedorId).get();
		model.addAttribute("proveedor", proveedor);
		return vista;
	}
	@PostMapping(value = "/edit")
	public String processUpdateProveedorForm(@Valid Proveedor proveedor, BindingResult result, ModelMap modelMap) {
		String vista = "proveedor/editarProveedor";

		if (result.hasErrors()) {
			modelMap.addAttribute("proveedor", proveedor);
			return vista;
		}else {
			if (proveedorService.findAllNames().contains(proveedor.getName())
					&& !proveedorService.findById(proveedor.getId()).get().getName().equals(proveedor.getName())) {
				modelMap.addAttribute("message", "El proveedor ya existe");
				return initUpdateProveedorForm(proveedor.getId(),modelMap);
			}
			else {
			proveedor.setActivo(true);
			this.proveedorService.save(proveedor);
			return "redirect:/proveedor";
			}
		}
	}	
}
