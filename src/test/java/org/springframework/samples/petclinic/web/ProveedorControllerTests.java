package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Proveedor;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.ProveedorService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test class for {@link PedidoController}
 *
 * @author Victor y tabares
 */

@WebMvcTest(controllers=ProveedorController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class ProveedorControllerTests {
	private static final int TEST_PROVEEDOR_ID = 1;

	@MockBean
	private ProveedorService proveedorService;

	@Autowired
	private MockMvc mockMvc;


	private Proveedor proveedor;

	@BeforeEach
	void setup() {
		proveedor = new Proveedor();
		proveedor.setId(TEST_PROVEEDOR_ID);
		proveedor.setName("jorge");
		proveedor.setGmail("patata@gmail.com");
		proveedor.setTelefono("954333333");

		given(this.proveedorService.findByName("jorge")).willReturn(proveedor);
		given(this.proveedorService.findById(TEST_PROVEEDOR_ID)).willReturn(Optional.of(proveedor));


}
	@WithMockUser(value = "spring")
    @Test
    void testProveedorNew() throws Exception {
	mockMvc.perform(get("/proveedor/new")).andExpect(status().isOk()).andExpect(model().attributeExists("proveedor"))
			.andExpect(view().name("proveedor/editProveedor"));
}

	@WithMockUser(value = "spring")
        @Test
	void testSaveProveedorSuccess() throws Exception {
		mockMvc.perform(post("/proveedor/save").param("name", "pepito")
							.with(csrf())
							.param("gmail", "pepitopalotes@gmail.com")
							.param("telefono", "676661638"))
		.andExpect(view().name("proveedor/listadoDeProveedores"));
	}

	@WithMockUser(value = "spring")
    @Test
void testSaveProveedorFail() throws Exception {
	mockMvc.perform(post("/proveedor/save").param("name", "pepito")
						.with(csrf())
						.param("gmail", "pepitopalotesgmail.com")
						.param("telefono", "62"))
	.andExpect(model().attributeHasErrors("proveedor"))
	.andExpect(model().attributeHasFieldErrors("proveedor", "gmail"))
	.andExpect(model().attributeHasFieldErrors("proveedor", "telefono"))
	.andExpect(view().name("proveedor/editProveedor"));
}

	
    @WithMockUser(value = "spring")
    @Test
    void testEditproveedor() throws Exception {
    	mockMvc.perform(get("/proveedor/edit/{proveedorId}",TEST_PROVEEDOR_ID)).andExpect(status().isOk())
   			.andExpect(model().attributeExists("proveedor"))
   			.andExpect(model().attribute("proveedor", hasProperty("name", is("jorge"))))
   			.andExpect(model().attribute("proveedor", hasProperty("gmail", is("patata@gmail.com"))))
    		.andExpect(model().attribute("proveedor", hasProperty("telefono", is("954333333"))))
    		.andExpect(view().name("proveedor/editarProveedor"));
}

    @WithMockUser(value = "spring")
	@Test
	void testPostProveedorEditadoSuccess() throws Exception {
		mockMvc.perform(post("/proveedor/edit", TEST_PROVEEDOR_ID)
							.with(csrf())
							.param("name", "bones")
							.param("gmail", "patata@gmail.com")
							.param("telefono", "666666666"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/proveedor"));
	}

    @WithMockUser(value = "spring")
 	@Test
 	void testPostProveedorEditadoFail() throws Exception {
 		mockMvc.perform(post("/proveedor/edit", TEST_PROVEEDOR_ID)
 							.with(csrf())
 							.param("name", "bo")
 							.param("gmail", "patata,gm.com")
 							.param("telefono", "666666666"))
 		.andExpect(status().isOk())
 		.andExpect(model().attributeHasErrors("proveedor"))
		.andExpect(model().attributeHasFieldErrors("proveedor", "name"))
		.andExpect(model().attributeHasFieldErrors("proveedor", "gmail"))
 		.andExpect(view().name("proveedor/editarProveedor"));
 	}

    @WithMockUser(value = "spring")
 	@Test
 	void testDeleteProveedor() throws Exception {
    	mockMvc.perform(get("/proveedor/delete/{proveedorid}",TEST_PROVEEDOR_ID)).andExpect(status().isOk())
    	.andExpect(model().attributeExists("proveedor"))
		.andExpect(view().name("proveedor/listadoDeProveedores"));
 	}
}

