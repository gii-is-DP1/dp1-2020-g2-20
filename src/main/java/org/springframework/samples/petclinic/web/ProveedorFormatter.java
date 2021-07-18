package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.samples.petclinic.service.ProveedorService;
import org.springframework.stereotype.Component;

@Component
public class ProveedorFormatter implements Formatter<Proveedor>{
	@Autowired
	private ProveedorService proveedorService;
	
	public ProveedorFormatter(ProveedorService proveedorService) {
		super();
		this.proveedorService = proveedorService;
	}

	@Override
	public String print(Proveedor proveedor, Locale locale) {
		return proveedor.getName();
	}

	@Override
	public Proveedor parse(String text, Locale locale) throws ParseException {
		Collection<Proveedor> encuentraProveedor = (Collection<Proveedor>) this.proveedorService.findAll();
		for (Proveedor prov : encuentraProveedor) {
			if (prov.getName().equals(text)) {
				return prov;
			}
		}
		throw new ParseException("tipo no encontrado: " + text, 0);
	}
}
