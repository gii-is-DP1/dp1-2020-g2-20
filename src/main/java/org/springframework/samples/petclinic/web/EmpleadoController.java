package org.springframework.samples.petclinic.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/empleados")
public class EmpleadoController {
	
	  @GetMapping()
	  public String empleadoVista(ModelMap modelMap) {	   
		  String vista= "empleados/empleados";
		  return vista;
	  }
}
