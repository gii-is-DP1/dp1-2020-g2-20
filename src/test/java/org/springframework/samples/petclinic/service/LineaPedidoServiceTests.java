package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.LineaPedido;
import org.springframework.samples.petclinic.model.Pedido;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class LineaPedidoServiceTests {
	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private LineaPedidoService lineaPedidoService;
	
	//Contar cuantas lineas de pedido hay.
	@Test
	public void testCountWithInitialData() {
		int count = lineaPedidoService.count();
		assertEquals(count,2);
	}
	
	//FindLineaPEdidoByProductoID
	@Test
	public void esBuscarLineaPedidoconProductoId() {
		Iterable<LineaPedido> test = lineaPedidoService.findByProductoId(1);
		Iterator<LineaPedido> it_test = test.iterator();
		int i = 0;
		while(it_test.hasNext()) {
			LineaPedido lineaPedido = it_test.next();
			int productoID = lineaPedido.getProducto().getId();
			assertEquals(1,productoID);
			assertThat(lineaPedido.getPedido()).isNotNull();
			assertThat(lineaPedido.getProducto()).isNotNull();
			i++;
		}
		assertEquals(i,1);
	}
	
	//AñadirLineaPedido
	@Test
	public void añadirLineaPedidoAPedido() {

		Producto producto = productoService.findById(5).get();
		Pedido pedido1 = pedidoService.findById(1).get();
		
		LineaPedido lineapedido = lineaPedidoService.anadirLineaPedido(producto, pedido1);
		lineaPedidoService.save(lineapedido);
		assertThat(lineapedido.getId()).isNotNull();
		
		double cantidad = producto.getCantMax()-producto.getCantAct();
		assertThat(lineapedido.getCantidad()).isEqualTo(cantidad);	
	}
}
