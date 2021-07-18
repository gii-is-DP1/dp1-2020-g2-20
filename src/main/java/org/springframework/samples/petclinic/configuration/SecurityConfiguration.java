package org.springframework.samples.petclinic.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	public static final String ROLE_PROPIETARIO = "propietario";
	public static final String ROLE_CAMARERO = "camarero";
	public static final String ROLE_COCINERO = "cocinero";
	public static final String ROLE_MANAGER = "manager";
	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**","/webjars/**","/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/","/oups").permitAll()
				.antMatchers("/users/new").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/platopedido").hasAnyAuthority(ROLE_CAMARERO,ROLE_COCINERO)
				.antMatchers("/platopedido/**").hasAnyAuthority(ROLE_CAMARERO,ROLE_COCINERO)
				.antMatchers("/camareros").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/camareros/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/cocinero").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/cocinero/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/platos").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/platos/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/ingrediente").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/ingrediente/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/ingredientepedido").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/ingredientepedido/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/managers").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/managers/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/propietarios/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/admin/**").permitAll()
				.antMatchers("/owners/**").permitAll()
				.antMatchers("/proveedor").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/proveedor/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/pedidos").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/pedidos/**").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/lineaPedido").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/lineaPedido/**").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/vets/**").authenticated()
				.antMatchers("/producto").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/producto/**").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/empleados").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/empleados/**").hasAnyAuthority(ROLE_PROPIETARIO)
				.antMatchers("/comanda/listaComandaTotal").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/comanda/listaComandaTotal/**").hasAnyAuthority(ROLE_PROPIETARIO,ROLE_MANAGER)
				.antMatchers("/comanda/listaComandaActual").hasAnyAuthority(ROLE_CAMARERO)
				.antMatchers("/comanda/listaComandaActual/**").hasAnyAuthority(ROLE_CAMARERO)
				.anyRequest().denyAll()
				.and()
				 	.formLogin()
				 	/*.loginPage("/login")*/
				 	.failureUrl("/login-error")
				.and()
					.logout()
						.logoutSuccessUrl("/"); 
                // Configuración para que funcione la consola de administración 
                // de la BD H2 (deshabilitar las cabeceras de protección contra
                // ataques de tipo csrf y habilitar los framesets si su contenido
                // se sirve desde esta misma página.
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
	}
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
	      .dataSource(dataSource)
	      .usersByUsernameQuery(
	       "select username,password,enabled "
	        + "from users "
	        + "where username = ?")
	      .authoritiesByUsernameQuery(
	       "select username, authority "
	        + "from authorities "
	        + "where username = ?")	      	      
	      .passwordEncoder(passwordEncoder());	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {	    
		PasswordEncoder encoder =  NoOpPasswordEncoder.getInstance();
	    return encoder;
	}
	
}


