package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.springframework.samples.petclinic.validators.NumeroMesaConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comanda")
@NumeroMesaConstraint
public class Comanda extends BaseEntity{
	
	@Column(name = "mesa")
	private Integer mesa;
	
	@Column(name = "precio_total")
	private Double precioTotal;
	
	@Column(name = "fecha_creado")
	private LocalDateTime fechaCreado;
	
	@Column(name = "fecha_finalizado")
	private LocalDateTime fechaFinalizado;
	
	@ManyToOne
	@JoinColumn(name = "camarero_id")
	private Camarero camarero;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "comanda")
	private Set<PlatoPedido> platosPedidos;
	
}
