package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.TipoProducto;
import org.springframework.samples.petclinic.service.TipoProductoService;
import org.springframework.stereotype.Component;

@Component
public class TipoProductoFormatter implements Formatter<TipoProducto>{
	@Autowired
	private TipoProductoService tipoProductoService;
	
	public TipoProductoFormatter(TipoProductoService tipoProductoService) {
		super();
		this.tipoProductoService = tipoProductoService;
	}

	@Override
	public String print(TipoProducto tipoProducto, Locale locale) {
		return tipoProducto.getName();
	}

	@Override
	public TipoProducto parse(String text, Locale locale) throws ParseException {
		Collection<TipoProducto> encuentraTipoProducto = this.tipoProductoService.findAll();
		for (TipoProducto tipo : encuentraTipoProducto) {
			if (tipo.getName().equals(text)) {
				return tipo;
			}
		}
		throw new ParseException("tipo no encontrado: " + text, 0);
	}
}
