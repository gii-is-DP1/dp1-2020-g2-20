package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Camarero;
import org.springframework.samples.petclinic.model.Comanda;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ComandaServiceTests {                
	@Autowired
	protected ComandaService comandaService;
	@Autowired
	protected CamareroService camareroService;
	
	@Test
	void shouldFindComandasByDia() {
		Collection<Comanda> comanda = comandaService.encontrarComandaDia("2021-02-09");
		assertThat(comanda.size()).isEqualTo(1);

		comanda = comandaService.encontrarComandaDia("2020-02-09");
		assertThat(comanda.isEmpty()).isTrue();
	}
	
	@Test
	@Transactional
	public void shouldInsertComanda() {
		Collection<Comanda> comandas = comandaService.encontrarComandaDia(LocalDate.now().toString());
		int found = comandas.size();

		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(LocalDateTime.now().plusHours(1));
		comanda.setMesa(250);
		comanda.setPrecioTotal(45.00);
		Camarero camarero = new Camarero();
				camarero.setName("Pedro");
				camarero.setApellido("Varo");
				camarero.setGmail("pedvar@gmail.com");
				camarero.setTelefono("654654659");
				camarero.setUsuario("TestComanda1");
				camarero.setContrasena("12345");
				camareroService.save(camarero);
		comanda.setCamarero(camarero);
              
		comandaService.save(comanda);
		assertThat(comanda.getId().longValue()).isNotEqualTo(0);

		comandas = comandaService.encontrarComandaDia(LocalDate.now().toString());
		assertThat(comandas.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	void shouldFindComandasActuales() {
		Collection<Comanda> comandas = comandaService.encontrarComandaActual();
		int found = comandas.size();

		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(null);
		comanda.setMesa(251);
		comanda.setPrecioTotal(45.00);
		Camarero camarero = new Camarero();
				camarero.setName("Fernando");
				camarero.setApellido("Hidalgo");
				camarero.setGmail("ferhid@gmail.com");
				camarero.setTelefono("683683683");
				camarero.setUsuario("TestComanda2");
				camarero.setContrasena("12345");
				camareroService.save(camarero);
		comanda.setCamarero(camarero);
              
		comandaService.save(comanda);
		assertThat(comanda.getId().longValue()).isNotEqualTo(0);

		comandas = comandaService.encontrarComandaDia(LocalDate.now().toString());
		assertThat(comandas.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	void shouldFindLastId() {
		Integer lastId = comandaService.findLastId();
		
		Comanda comanda = new Comanda();
		comanda.setFechaCreado(LocalDateTime.now());
		comanda.setFechaFinalizado(LocalDateTime.now().plusHours(1));
		comanda.setMesa(252);
		comanda.setPrecioTotal(45.00);
		Camarero camarero = new Camarero();
				camarero.setName("Maria");
				camarero.setApellido("Zamudio");
				camarero.setGmail("marzam@gmail.com");
				camarero.setTelefono("632632637");
				camarero.setUsuario("TestComanda3");
				camarero.setContrasena("12345");
				camareroService.save(camarero);
		comanda.setCamarero(camarero);
              
		comandaService.save(comanda);
		assertThat(comanda.getId().longValue()).isNotEqualTo(0);
		assertThat(comandaService.findLastId()).isEqualTo(lastId + 1);
	}

	@Test
	void shouldUpdateComanda() {
		Collection<Comanda> comandas = comandaService.encontrarComandaActual();
		int found = comandas.size();
		
		//Ahora cerramos una comanda ya abierta en la BD
		Comanda comanda = comandaService.findById(2).get();
		comanda.setFechaFinalizado(LocalDateTime.now());

		assertThat(comandaService.encontrarComandaActual().size()).isEqualTo(found-1);
	}
}
