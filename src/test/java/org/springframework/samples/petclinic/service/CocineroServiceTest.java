package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.petclinic.model.Cocinero;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CocineroServiceTest {
	@Autowired
	private CocineroService cocineroService;
	
	  @Test
	  @Transactional
		public void shouldInsertCocinero() {
		  Iterable<Cocinero> it= this.cocineroService.findAll();
		  List<Cocinero> ls=Lists.newArrayList(it);
		  int before = ls.size();
			
			
		  Cocinero coc= new Cocinero();
	        		
	       coc.setApellido("acfrr");
	       coc.setContrasena("12345");
	       coc.setGmail("addff@gmail.com");
	       coc.setName("adddd");
	       coc.setTelefono("123456789");
	       coc.setUsuario("cocinero1");
	      
	                
			this.cocineroService.save(coc);
			assertThat(coc.getId().longValue()).isNotEqualTo(0);

			int after = Lists.newArrayList(this.cocineroService.findAll()).size();
			assertThat(before+1).isEqualTo(after);
	 

}
	  @Test
	  void shouldDeleteCocinero() {
	  Iterable<Cocinero> it= this.cocineroService.findAll();
	  List<Cocinero> ls=Lists.newArrayList(it);
	  int foundBefore = ls.size();
	  
	  Cocinero cm = this.cocineroService.findById(1).get();
	  this.cocineroService.deleteById(cm.getId());	  
		
	  List<Cocinero> ls2=Lists.newArrayList(this.cocineroService.findAll());
		 
	  int foundAfter = ls2.size();
	  assertThat(foundBefore).isEqualTo(foundAfter+1);
	  
	  }

}
