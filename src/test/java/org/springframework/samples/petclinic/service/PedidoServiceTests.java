package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Pedido;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPedidoException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class PedidoServiceTests {

	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private PedidoService pedidoService;
	
	//FindPedidoByProveedorID
	@Test
	public void findPedidoByProveedorId() {
		Iterable<Pedido> test = this.pedidoService.findByProveedorId(1);
		Iterator<Pedido> it_test = test.iterator();
		List<Pedido> aux = new ArrayList<Pedido>();
		while (it_test.hasNext()) {
			Pedido pedido = it_test.next();
			aux.add(pedido);
			assertThat(pedido.getId()).isNotNull();
		}
		
		int a = aux.get(0).getId();
		assertEquals(1, a);
		assertEquals(false, aux.get(0).getHaLlegado());
		assertEquals("Makro", aux.get(0).getProveedor().getName());

		
	}
	
	//SavePedido
	
	@Test
	public void guardarPedido() {
		int found = pedidoService.count();
		
		Pedido pedido = new Pedido();
		pedido.setFechaPedido(LocalDate.now());
		pedido.setHaLlegado(false);
		pedido.setProveedor(proveedorService.findById(3).get());
		
		try {
			pedidoService.save(pedido);
		} catch (DuplicatedPedidoException ex) {
			Logger.getLogger(PedidoServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
      
		int size = pedidoService.count();
		assertEquals(found+1, size);	
	}
	
	
	@Test
	public void falloGuardarPedidoRepetido() {
		int found = pedidoService.count();
		
		Pedido pedido = new Pedido();
		pedido.setFechaPedido(LocalDate.now());
		pedido.setHaLlegado(false);
		pedido.setProveedor(proveedorService.findById(1).get());
		
		try {
			pedidoService.save(pedido);
		} catch (DuplicatedPedidoException ex) {
			Logger.getLogger(PedidoServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
      
		int size = pedidoService.count();
		assertEquals(found, size);
	}
	
	//EncontrarProductoProveedor
	
	@Test
	public void listaDeProductosDeProveedor() {
		Producto producto = productoService.findById(1).get();
		Collection<Producto> aux = productoService.findByProveedor(producto);
		int size = aux.size();
		assertEquals(size, 7);
	}
	
	
	@Test
	public void testprueba() {
		Optional<Pedido> test = this.pedidoService.findById(1);
		assertEquals("1", test.get().getId().toString());
		assertEquals(false, test.get().getHaLlegado());
		assertEquals("Makro", test.get().getProveedor().getName());		
	}
	
	
}
