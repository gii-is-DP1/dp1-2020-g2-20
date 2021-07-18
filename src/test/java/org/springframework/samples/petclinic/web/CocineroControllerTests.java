package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cocinero;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CocineroService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = CocineroController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CocineroControllerTests {

	private static final int TEST_COCINERO_ID = 1;

	@MockBean
	private CocineroService cocineroService;
	
	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		Cocinero cocinero = new Cocinero();
		cocinero.setApellido("ffffff");
		cocinero.setContrasena("12345");
		cocinero.setGmail("xxxxx@gmail.com");
		cocinero.setName("aaaass");
		cocinero.setTelefono("123456789");
		cocinero.setUsuario("jose");
		//cocinero.getUsuario().se
		given(this.cocineroService.findById(TEST_COCINERO_ID).get()).willReturn(cocinero);

	}

	@WithMockUser(value = "spring")
	@Test
	void showCocinerosList() throws Exception {
		mockMvc.perform(get("/cocinero")).andExpect(status().isOk()).andExpect(view().name("cocinero/listaCocinero"))
				.andExpect(model().attributeExists("cocinero"));
	}

	@WithMockUser(value = "spring")

	@Test
	void initCreationForm() throws Exception {
		mockMvc.perform(get("/cocinero/new")).andExpect(status().isOk())
				.andExpect(view().name("cocinero/editCocinero")).andExpect(model().attributeExists("cocinero"));
	}

	@WithMockUser(value = "spring")

	@Test
	void processCreationCocineroSuccess() throws Exception {
		mockMvc.perform(post("/cocinero/save").with(csrf()).param("name", "Pepe")
				.param("apellidos", "escob")
				.param("telefono", "543972343").param("gmail", "p2000@gmail.com")
				.param("usuario", "jose").param("contrasena", "12345"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		  .andExpect(MockMvcResultMatchers.view().name("cocinero/listaCocinero"));
		}
	
	@WithMockUser(value = "spring")
    @Test
	void processCreationCocineroHasErrors() throws Exception {
		mockMvc.perform(post("/cocinero/save").with(csrf()).param("name", "")
				.param("apellidos", "")
				.param("telefono", "543972343").param("gmail", "p2000")
				.param("usuario", "jose").param("contrasena", "12345"))
	
		.andExpect(model().attributeHasErrors("cocinero"))
		.andExpect(status().isOk())
		.andExpect(view().name("cocinero/editCocinero"));

	}
	//@WithMockUser(username = "prince", password="12345", authorities="propietario")
	@WithMockUser(value = "spring")
    @Test
	void initDeleteCocinero() throws Exception {
		mockMvc.perform(get("/cocinero/delete/"+TEST_COCINERO_ID)).andExpect(status().isOk()).andExpect(view().name("cocinero/listaCocinero"))
		.andExpect(model().attributeExists("cocinero"));
	}
}
