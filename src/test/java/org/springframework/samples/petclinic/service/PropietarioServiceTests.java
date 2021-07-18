package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Propietario;
import org.springframework.samples.petclinic.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PropietarioServiceTests {
	
	@Autowired
	private PropietarioService propietarioService;
	
	@Autowired
	protected PropietarioRepository propietarioRepository;
			
	@Test
	public void testCountWithInitialData() {
		int count= propietarioService.count();
		assertEquals(count,1);
	}
	
	@Test
	@Transactional
	void shouldFindPropietario() {
		java.util.Optional<Propietario> propietario = this.propietarioService.findById(1);
		Propietario p= propietario.get();
		assertThat(p.getName().equals("Abdel"));
		assertThat(p.getApellido().equals("Ch"));
		assertThat(p.getGmail().equals("Abdch@gmail.com"));
		assertThat(p.getTelefono().equals("602354622"));
		assertThat(p.getContrasena().equals("12345"));

	}
	
	@Test
	@Transactional
	void shouldInsertPropietario() {
		 
		Propietario p = new Propietario();
		p.setName("abdel");
		p.setApellido("Chell");
        p.setGmail("abdel@gmail.com");
		p.setContrasena("1234567");
		p.setTelefono("602343454");
		
		this.propietarioRepository.save(p);
		assertThat(p.getId()).isNotNull();
		
	}
	
	@Test
	@Transactional
	void shouldNotInsertPropietarioithWrongEmail() {

		Propietario p = new Propietario();
		p.setName("abdel");
		p.setApellido("Ch");
        p.setGmail("abdel");
		p.setContrasena("1234567");
		p.setTelefono("602343454");
		assertThrows(ConstraintViolationException.class, () -> {
			this.propietarioRepository.save(p);
		});
		
		
	}
	@Test
	@Transactional
	void shouldNotInsertPropietarioWithWrongcontraseña() {

		Propietario p = new Propietario();
		p.setName("abdel");
		p.setApellido("Ch");
        p.setGmail("abdel");
		p.setContrasena("1");
		p.setTelefono("602343454");
		assertThrows(ConstraintViolationException.class, () -> {
			this.propietarioRepository.save(p);
		});
		
		
	}
	@Test
	@Transactional
	void shouldUpdateNombre() {
		Propietario p = new Propietario();
		String nombre = p.getName();
		String n =nombre +"abi";
		p.setName(n);
		this.propietarioRepository.save(p);
		assertThat(p.getName()).isEqualTo(n);
	}
}
