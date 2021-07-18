package org.springframework.samples.petclinic.web;

import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Ingrediente;
import org.springframework.samples.petclinic.model.Plato;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.service.IngredienteService;
import org.springframework.samples.petclinic.service.PlatoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(controllers=PlatoController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class PlatoControllerTests {
private static final int TEST_PLATO_ID = 1;
private static final int TEST_ING_ID = 1;

@MockBean
private PlatoService platoService;

@MockBean
private IngredienteService ingredienteService;


@Autowired
private MockMvc mockMvc;



private Plato plato;
private Ingrediente ing;
private Producto prd;

@BeforeEach
void setup() {
	prd= new Producto();
	prd.setId(1);
	prd.setName("huevo");
	
	ing= new Ingrediente();
	ing.setId(TEST_ING_ID);
	ing.setPlato(plato);
	ing.setCantidadUsualPP(3.);
	ing.setProducto(prd);
	
	List<Ingrediente> l= new ArrayList<Ingrediente>();
	l.add(ing);

	plato = new Plato();
	plato.setId(TEST_PLATO_ID);
	plato.setName("espinacas");
	plato.setPrecio(4.0);
	plato.setDisponible(true);
	plato.setIngredientes(null);
	
	given(this.platoService.findById(TEST_PLATO_ID)).willReturn(Optional.of(plato));
	
	given(this.ingredienteService.findByPlatoId(TEST_PLATO_ID)).willReturn(l);
	
	given(this.ingredienteService.save(ing)).willReturn(ing);
	
	given(this.platoService.ingredienteEstaRepetido(any(String.class), any(Integer.class))).willReturn(false);
	
	given(this.ingredienteService.findById(TEST_ING_ID)).willReturn(Optional.of(ing));
	
	
}


@WithMockUser(value = "spring")
@Test
void testCrearPlato() throws Exception {
mockMvc.perform(get("/platos/new")).andExpect(status().isOk()).andExpect(model().attributeExists("platos"))
	.andExpect(view().name("platos/editPlatos"));
}


@WithMockUser(value = "spring")
    @Test
void testGuardarPlato() throws Exception {
	mockMvc.perform(post("/platos/save").param("name", "espinacas")
						.with(csrf())
						.param("precio", "4")
						.param("disponible", "true"))
	.andExpect(view().name("platos/listaPlatos"));
}


@WithMockUser(value = "spring")
@Test
void testInitUpdatePlatoForm() throws Exception {
	mockMvc.perform(get("/platos/{platoId}/edit",TEST_PLATO_ID)).andExpect(status().isOk())
		.andExpect(model().attributeExists("plato"))
		.andExpect(model().attribute("plato", hasProperty("name", is("espinacas"))))
		.andExpect(model().attribute("plato", hasProperty("precio", is(4.0))))
		.andExpect(model().attribute("plato", hasProperty("disponible", is(true))))
		.andExpect(view().name("platos/editarPlatos"));
}

@WithMockUser(value = "spring")
@Test
void processUpdatePlatoForm() throws Exception {
	mockMvc.perform(post("/platos/edit/{platoId}", TEST_PLATO_ID)
						.with(csrf())
						.param("name", "espinac")
						.param("precio", "5.")
						.param("disponible", "true"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/platos"));
}

@WithMockUser(value = "spring")
@Test
void processUpdatePlatoFailedNameForm() throws Exception {
	mockMvc.perform(post("/platos/edit/{platoId}", TEST_PLATO_ID)
						.with(csrf())
						.param("name", "")
						.param("precio", "5.")
						.param("disponible", "true"))
	.andExpect(status().isOk())
	.andExpect(model().attributeHasFieldErrors("plato", "name"))
	.andExpect(view().name("platos/editarPlatos"));
}

@WithMockUser(value = "spring")
	@Test
	void testDeletePlato() throws Exception {
	mockMvc.perform(get("/platos/delete/{platoId}",TEST_PLATO_ID)).andExpect(status().isOk())
	.andExpect(model().attributeExists("platos"))
	.andExpect(view().name("platos/listaPlatos"));
	}

@WithMockUser(value = "spring")
	@Test
	void testShowPlato() throws Exception {
	mockMvc.perform(get("/platos/{platoId}",TEST_PLATO_ID)).andExpect(status().isOk())
	.andExpect(model().attributeExists("plato"))
	.andExpect(model().attributeExists("ingredientes"))
	.andExpect(view().name("platos/platosDetails"));
	}
	//comprobar en tutoria


@WithMockUser(value = "spring")
	@Test
	void testCrearIngrediente() throws Exception {
	
	mockMvc.perform(get("/platos/{platoId}/ingrediente/new",TEST_PLATO_ID)).andExpect(status().isOk())
	.andExpect(model().attributeExists("plato"))
	.andExpect(model().attributeExists("ingrediente"))
	.andExpect(model().attributeExists("listaProductos"))
	.andExpect(view().name("platos/newIngredientes"));
	}

@WithMockUser(value = "spring")
@Test
void testPostProcessCreationIngForm() throws Exception {
	mockMvc.perform(post("/platos/ingSave/{platoId}",TEST_PLATO_ID)
						.with(csrf())
						.param("cantidadUsualPP", "5")
						.param("producto.name", "huevo"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/platos/"+TEST_PLATO_ID));
}

@WithMockUser(value = "spring")
@Test
void testPostProcessCreationIngFormIfRepetido() throws Exception {
	given(this.platoService.ingredienteEstaRepetido(any(String.class), any(Integer.class))).willReturn(true);
	
	mockMvc.perform(post("/platos/ingSave/{platoId}",TEST_PLATO_ID)
						.with(csrf())
						.param("cantidadUsualPP", "5")
						.param("producto.name", "huevo"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/platos/"+TEST_PLATO_ID));
}


}