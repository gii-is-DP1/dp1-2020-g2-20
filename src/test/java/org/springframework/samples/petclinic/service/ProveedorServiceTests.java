package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.stereotype.Service;

//tambien testea pedido y linea pedido

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProveedorServiceTests {
	@Autowired
	private ProveedorService proveedorService;
	
	
	//FindAllNames
	@Test
	public void listaDeNombresDeProveedores() {
		Collection<String> nombres = proveedorService.findAllNames();
		assertEquals(4, nombres.size());
	}
	
	//FindProveedorByName
	@Test
	public void esBuscarProveedor() {
		Proveedor test = proveedorService.findByName("Makro");
		assertEquals("Makro", test.getName());
	}
	
	//EsIgual
	@Test
	public void esIgualProveedorTest() {		
		assertEquals(proveedorService.esIgual("Makro"),true);
		assertEquals(proveedorService.esIgual("Codi"),false);
	}
	
	@Test
	public void findAllNames() {		
		assertEquals(proveedorService.findAllNames().size(),4);
	}
	
	@Test
	public void findActivosNameTest() {	
		assertEquals(proveedorService.findActivosName().size(), 4);
		
	}
	
	@Test
	public void findActivosTest() {
		String name=proveedorService.findActivos()
				.iterator().next().getName();
		assertEquals(name, "Makro");
		
	}
	
	@Test
	public void findProveedorByNameTest() {	
		int id=proveedorService.findByName("Makro").getId();
		assertEquals(id, 1);
		
	}

}
