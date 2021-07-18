package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.time.LocalDate;

import java.util.HashSet;

import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.LineaPedido;
import org.springframework.samples.petclinic.model.Pedido;

import org.springframework.samples.petclinic.model.Producto;

import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.samples.petclinic.model.TipoProducto;
import org.springframework.samples.petclinic.repository.LineaPedidoRepository;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.samples.petclinic.service.ProveedorService;
import org.springframework.samples.petclinic.service.exceptions.PedidoPendienteException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ProductoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ProductoControllerTests {

	private static final int TEST_PRODUCTO_ID = 20;
	private static final int TEST_LINEAPEDIDO_ID = 3;
	private static final int TEST_PEDIDO_ID = 1;
	private static final int TEST_PROVEEDOR_ID = 1;
	private static final int TEST_TIPO_PRODUCTO_ID = 1;
	
	@MockBean
	private LineaPedidoRepository lineaPedidoRepository;
	@MockBean
	private ProductoService productoService;
	@MockBean
	private ProveedorService proveedorService;
	@MockBean
	private ProductoConverter productconverter;

	@MockBean
	private TipoProductoFormatter tipoproductoformatter;

	@MockBean
	private ProveedorFormatter proveedorformatter;

	@Autowired
	private MockMvc mockMvc;

	private Producto producto = new Producto();
	private TipoProducto tipoproducto = new TipoProducto();
	private Proveedor proveedor = new Proveedor();
	private LineaPedido lineapedido = new LineaPedido();
	private Pedido pedido = new Pedido();

	@BeforeEach

	void setup() throws DataAccessException, ParseException, PedidoPendienteException, NullPointerException {

		// Data Pedido
		pedido.setFechaEntrega(LocalDate.now());
		pedido.setHaLlegado(true);
		pedido.setId(TEST_PEDIDO_ID);
		// Data Linea pedido
		lineapedido.setCantidad(2);
		lineapedido.setProducto(producto);
		lineapedido.setId(TEST_LINEAPEDIDO_ID);
		lineapedido.setPedido(pedido);
		Set<LineaPedido> lineas = new HashSet<LineaPedido>();
		lineas.add(lineapedido);
		// data proveedor
		proveedor.setActivo(true);
		proveedor.setName("proveedor_1");
		proveedor.setTelefono("123456789");
		proveedor.setId(TEST_PROVEEDOR_ID);
		// data Tipo PRoducto
		tipoproducto.setId(TEST_TIPO_PRODUCTO_ID);
		tipoproducto.setName("Otros");

		// data PRoducto
		producto.setCantAct(10.0);
		producto.setCantMax(11.0);
		producto.setCantMin(1.0);
		producto.setName("producto_1");
		producto.setLineasPedidas(lineas);
		producto.setId(TEST_PRODUCTO_ID);
		producto.setProveedor(proveedor);
		producto.setTipoProducto(tipoproducto);

		BDDMockito.given(this.productoService.findById(TEST_PRODUCTO_ID)).willReturn(Optional.of(producto));
		BDDMockito.given(this.proveedorService.findByName("proveedor_1")).willReturn(this.proveedor);
		BDDMockito.given(this.lineaPedidoRepository.findByProductoId(TEST_PRODUCTO_ID))
				.willReturn(this.producto.getLineasPedidas());
	}

	@WithMockUser(value = "spring")
	@Test
	@DisplayName("Vista creacion producto")
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("producto/editProducto"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("producto"));
	}

	@WithMockUser(value = "spring")
	@Test
	@DisplayName("Vista eliminar producto")
	void initDeleteProducto() throws Exception {
		BDDMockito.given(this.productoService.findById(TEST_PRODUCTO_ID)).willReturn(Optional.of(producto));
		mockMvc.perform(get("/producto/delete/{productoId}", TEST_PRODUCTO_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("producto"))
				.andExpect(MockMvcResultMatchers.view().name("producto/listaProducto"));

	}

	@WithMockUser(value = "spring")
	@Test
	@DisplayName("Vista listar productos")
	void testListProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("producto"))
				.andExpect(MockMvcResultMatchers.view().name("producto/listaProducto"));
	}

}
