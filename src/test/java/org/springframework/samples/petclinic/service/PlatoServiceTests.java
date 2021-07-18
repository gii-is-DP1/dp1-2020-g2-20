package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlatoServiceTests {

	@Autowired
	private PlatoService platoService;
	@Autowired
	private IngredienteService ingredienteService;
	

	@Test
	public void ingredientePorPlatoTest() {	
		assertEquals(ingredienteService.findByPlatoId(2).size(), 2); 
	}
	
	//EsIgual
		@Test
		public void estaIngRepetidoTest() {		
			assertEquals(platoService.ingredienteEstaRepetido("Lechuga",2),false);
			assertEquals(platoService.ingredienteEstaRepetido("Mayonesa",2),false);
		}
		
	
}
