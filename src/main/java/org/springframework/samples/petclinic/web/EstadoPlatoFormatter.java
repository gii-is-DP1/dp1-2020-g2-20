package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.EstadoPlato;
import org.springframework.samples.petclinic.service.EstadoPlatoService;
import org.springframework.stereotype.Component;

@Component
public class EstadoPlatoFormatter implements Formatter<EstadoPlato>{
	@Autowired
	private EstadoPlatoService estadoPlatoService;
	
	public EstadoPlatoFormatter(EstadoPlatoService estadoPlatoService) {
		super();
		this.estadoPlatoService = estadoPlatoService;
	}

	@Override
	public String print(EstadoPlato estadoPlato, Locale locale) {
		return estadoPlato.getName();
	}

	@Override
	public EstadoPlato parse(String text, Locale locale) throws ParseException {
		Collection<EstadoPlato> encuentraEstadoPlato = this.estadoPlatoService.findAll();
		for (EstadoPlato tipo : encuentraEstadoPlato) {
			if (tipo.getName().equals(text)) {
				return tipo;
			}
		}
		throw new ParseException("tipo no encontrado: " + text, 0);
	}
}
