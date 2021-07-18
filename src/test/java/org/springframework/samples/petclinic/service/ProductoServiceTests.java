package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.samples.petclinic.model.TipoProducto;
import org.springframework.samples.petclinic.service.exceptions.PedidoPendienteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductoServiceTests {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private TipoProductoService tipoProductoService;
	
	@Test
	void shouldFindAllProducto() {
		Collection<Producto> productos=Lists.newArrayList(productoService.findAll());
		assertThat(productos.size()).isEqualTo(19);
		
		
	}
	
	  @Test
	  void shouldFindProductoByType(){ 
		 Collection<TipoProducto> tp= tipoProductoService.findAll();
		 for(TipoProducto e: tp) {
			 assertThat(e).isNotNull();
	  
	  }
		 assertThat(tp.size()).isEqualTo(6);
		 }
	  
	  @Test
	  @Transactional
	  void ShouldInsertProduct() {
		  Collection<TipoProducto> t= this.tipoProductoService.findAll();
		  List<TipoProducto> y= new ArrayList<>();
		  y.addAll(t);
		  int beforInsert=Lists.newArrayList(this.productoService.findAll()).size();
		  
		  Producto p=new Producto();
		  p.setCantAct(6.0);
		  p.setCantMax(10.0);
		  //p.setCantMin(2);
		  //p.setFechaCaducidad(LocalDate.now().plusMonths(5));
		  
		  Proveedor pv= new Proveedor();
		  pv.setId(2);
		 
		 p.setProveedor(pv);
		 p.setTipoProducto(y.get(0));
		 
		 
		 this.productoService.save(p);
		 assertThat(p.getId().longValue()).isEqualTo(20);
		 
		 int after =Lists.newArrayList(this.productoService.findAll()).size();
			assertThat(beforInsert+1).isEqualTo(after);
		  
	  }
		
		@Test
		void shouldDeleteProducto() throws DataAccessException, PedidoPendienteException {

			int foundBefore = Lists.newArrayList(this.productoService.findAll()).size();

			Producto pr = this.productoService.findById(19).get();
			this.productoService.deleteById(pr.getId());

			int foundAfter = Lists.newArrayList(this.productoService.findAll()).size();
			assertThat(foundBefore).isEqualTo(foundAfter + 1);

		}
		 
	  
	    @Test
		@Transactional
		void shouldUpdateProduct() {
			Optional<Producto> p = this.productoService.findById(1);
			
			String name = "Solomillo";
			double cantidadActual=p.get().getCantAct();
			double cantidadNow =cantidadActual +1;
			
		    p.get().setCantAct(cantidadNow);
		    p.get().setName(name);
		    
		    TipoProducto tb = new TipoProducto();
			tb.setName("pscd");
			p.get().setTipoProducto(tb);
			
			this.productoService.save(p.get());
			
			assertThat(p.get().getCantAct()).isEqualTo(cantidadNow);
			assertThat(p.get().getName()).isEqualTo(name);
			assertTrue(this.productoService.findById(p.get().getId()).isPresent());
		}
}
