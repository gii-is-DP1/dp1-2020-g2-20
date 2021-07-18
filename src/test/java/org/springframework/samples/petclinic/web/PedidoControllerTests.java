package org.springframework.samples.petclinic.web;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Pedido;
import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.PedidoService;
import org.springframework.samples.petclinic.service.ProveedorService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test class for {@link PedidoController}
 *
 * @author Victor y tabares
 */

@WebMvcTest(controllers=PedidoController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class PedidoControllerTests {
	
	private static final int TEST_PEDIDO_ID = 1;
	private static final int TEST_PEDIDO_ID2 = 2;


	@MockBean
	private ProveedorService proveedorService;
	@MockBean
	private PedidoService pedidoService;

	@Autowired
	private MockMvc mockMvc;
	
	private Pedido pedido;
	private Pedido pedido2;
	private Proveedor proveedor;
	
	private Collection<Pedido> lPorDia;

	@BeforeEach
	void test() {
		proveedor = new Proveedor();
		proveedor.setId(7);
		proveedor.setName("jorge");
		proveedor.setGmail("jorge@gmail.com");
		proveedor.setTelefono("678678678");
		
		//Pedidos
		
		pedido = new Pedido();
		pedido.setId(TEST_PEDIDO_ID);
		pedido.setProveedor(proveedor);
		pedido.setHaLlegado(false);
		pedido.setFechaEntrega(null);
		pedido.setFechaPedido(LocalDate.now());
		
		pedido2 = new Pedido();
		pedido2.setId(TEST_PEDIDO_ID2);
		pedido2.setProveedor(proveedor);
		pedido2.setHaLlegado(true);
		pedido2.setFechaEntrega(LocalDate.now());
		pedido2.setFechaPedido(LocalDate.of(2021, 01, 30));
		
		//Lista pedidos
	
		lPorDia = new ArrayList<Pedido>();
		lPorDia.add(pedido2);
		
		given(this.pedidoService.findById(TEST_PEDIDO_ID)).willReturn(Optional.of(pedido));
		given(this.pedidoService.findById(TEST_PEDIDO_ID2)).willReturn(Optional.of(pedido2));
		given(this.pedidoService.encontrarPedidoDia("2021-01-30")).willReturn(lPorDia);
//		given(this.proveedorService.findPedidoByProveedorId(7).iterator().next()).willReturn(pedido);
//		given(this.proveedorService.findProveedorbyName("jorge")).willReturn(proveedor);
//		given(this.proveedorService.findPedidoByProveedorId(7).iterator().next()).willReturn(prueba);

	}
	
	//Test Crear Pedido (NEW)
	
	@WithMockUser(value = "spring")
    @Test
    void testPedidoNew() throws Exception {
		mockMvc.perform(get("/pedidos/new")).andExpect(status().isOk())
				.andExpect(model().attributeExists("pedido"))
				.andExpect(view().name("pedidos/editPedido"));
	}	
	
	
	// Test Guardar pedido (SAVE)
	
	@WithMockUser(value = "spring")
    @Test
    void testSavePedidoSuccess() throws Exception {
		mockMvc.perform(post("/pedidos/save")
				.with(csrf()))
				.andExpect(view().name("pedidos/listaPedidos"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSavePedidoFail() throws Exception {
		mockMvc.perform(post("/pedidos/save")
				.with(csrf())
				.param("fechaEntrega", "13 del 10 de 2020")
				.param("haLlegado", "Si"))
				.andExpect(model().attributeHasErrors("pedido"))
				.andExpect(model().attributeHasFieldErrors("pedido", "haLlegado"))
				.andExpect(model().attributeHasFieldErrors("pedido", "fechaEntrega"))
				.andExpect(view().name("pedidos/editPedido"));
	}
	
	
	// H13+E1 - Llegada de pedido
	
	@WithMockUser(value = "spring")
    @Test
    void testRecargarStockSuccess() throws Exception {
		mockMvc.perform(get("/pedidos/terminarPedido/{pedidoID}", TEST_PEDIDO_ID))
				.andExpect(model().attributeExists("pedidoFinalizado"))
				.andExpect(model().attribute("pedidoFinalizado", hasProperty("haLlegado", is(true))))
				.andExpect(model().attribute("pedidoFinalizado", hasProperty("fechaEntrega", is(LocalDate.now()))))
				.andExpect(view().name("pedidos/listaPedidos"));
	}	
	
	
	// H13-E1 - Llegada de pedido
	
	@WithMockUser(value = "spring")
    @Test
    void testRecargarStockFail() throws Exception {
		mockMvc.perform(get("/pedidos/terminarPedido/{pedidoID}", TEST_PEDIDO_ID2))
				.andExpect(model().attribute("message", is("El pedido ya se ha finalizado")))
				.andExpect(view().name("pedidos/listaPedidos"));
	}	
	
	

	//H12+E1 - Listado de pedidos
	
	@WithMockUser(value = "spring")
	@Test
	void testListadoPedidoPorDia() throws Exception {
		mockMvc.perform(get("/pedidos/listaPedidoTotal/dia")).andExpect(status().isOk())
				.andExpect(model().attributeExists("pedido"))
				.andExpect(view().name("pedidos/listaPedidos"));
	}	
	

}
