package org.springframework.samples.petclinic.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingrediente_pedido")
public class IngredientePedido extends BaseEntity{
	
	
	@Column(name = "cant_pedida")
	private Double cantidadPedida;
	
	@ManyToOne(optional=false)
	@JoinColumn(name = "ingrediente_id")
	private Ingrediente ingrediente;
	
	@ManyToOne
	@JoinColumn(name = "pp_id")
	private PlatoPedido pp;
}
