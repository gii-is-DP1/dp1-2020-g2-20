package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Propietario;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.PropietarioService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test class for {@link PedidoController}
 *
 * @author Victor y tabares
 */

@WebMvcTest(controllers=PropietarioController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class PropietarioControllerTests {
	private static final int TEST_PROPIETARIO_ID = 1;

	@MockBean
	private PropietarioService propietarioService;
	
	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;


	private Propietario propietario;

	@BeforeEach
	void setup() {
		propietario = new Propietario();
		propietario.setId(TEST_PROPIETARIO_ID);
		propietario.setName("pedro");
		propietario.setApellido("manteca");
		propietario.setGmail("manteca@gmail.com");
		propietario.setTelefono("954339970");

		List<Propietario> listaProp= new ArrayList<Propietario>();
		listaProp.add(propietario);
		Iterable<Propietario> l= listaProp;
		
		
		given(this.propietarioService.findById(TEST_PROPIETARIO_ID)).willReturn(Optional.of(propietario));
		
		given(this.propietarioService.save(propietario))
		.willReturn(propietario);
	
		given(this.propietarioService.findAll()).willReturn(l);
		
		given(this.propietarioService.count()).willReturn(1);
	}
	@WithMockUser(value = "spring")
    @Test
    void testListadoPropietariosVacia() throws Exception {
		given(this.propietarioService.count()).willReturn(0);
		
		
	mockMvc.perform(get("/propietarios")).andExpect(status().isOk())
	.andExpect(model().attributeExists("message"))
	.andExpect(view().name("propietarios/listaPropietarios"));
}
	
	@WithMockUser(value = "spring")
    @Test
    void testListadoPropietariosConAlgunPropietario() throws Exception {
		
	mockMvc.perform(get("/propietarios")).andExpect(status().isOk())
	.andExpect(model().attributeExists("propietarios"))
	.andExpect(view().name("propietarios/listaPropietarios"));
}


	@WithMockUser(value = "spring")
    @Test
    void testPropietarioNew() throws Exception {
	mockMvc.perform(get("/propietarios/new")).andExpect(status().isOk()).andExpect(model().attributeExists("propietario"))
			.andExpect(view().name("propietarios/editPropietario"));
}

	@WithMockUser(value = "spring")
        @Test
	void testSaveProveedorSuccess() throws Exception {
		mockMvc.perform(post("/propietarios/save").param("name", "paco")
				.param("apellidos", "propi")
				.with(csrf())
				.param("gmail", "paquitopalotes@gmail.com")
				.param("telefono", "676661638"))
		.andExpect(view().name("propietarios/listaPropietarios"));
	}

	

    @WithMockUser(value = "spring")
    @Test
    void testEditproveedor() throws Exception {
    	mockMvc.perform(get("/propietarios/edit/{propietarioId}",TEST_PROPIETARIO_ID)).andExpect(status().isOk())
   			.andExpect(model().attributeExists("propietario"))
    		.andExpect(view().name("propietarios/editarPropietario"));
}

    @WithMockUser(value = "spring")
	@Test
	void testPostProveedorEditadoSuccess() throws Exception {
		mockMvc.perform(post("/propietarios/edit", TEST_PROPIETARIO_ID)
							.with(csrf())
							.param("name", "pedro")
							.param("apellidos", "mantecao")
							.param("gmail", "pedritomanteca@gmail.com")
							.param("telefono", "543456666"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/propietarios"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testPostProveedorEditadoNombreVacioFail() throws Exception {
		mockMvc.perform(post("/propietarios/edit", TEST_PROPIETARIO_ID)
							.with(csrf())
							.param("name", "")
							.param("apellidos", "mantecao")
							.param("gmail", "pedritomanteca@gmail.com")
							.param("telefono", "543456666"))
				.andExpect(model().attributeHasErrors("propietario"))
				.andExpect(model().attributeHasFieldErrors("propietario", "name"))
				.andExpect(view().name("propietarios/editarPropietario"));
	}

    @WithMockUser(value = "spring")
 	@Test
 	void testDeleteProveedor() throws Exception {
    	mockMvc.perform(get("/propietarios/delete/{propietarioId}",TEST_PROPIETARIO_ID))
    	.andExpect(model().attributeDoesNotExist("propietario"))
    	.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/propietarios"));
 	}
}


