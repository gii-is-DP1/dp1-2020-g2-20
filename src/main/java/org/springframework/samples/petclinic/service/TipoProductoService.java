package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.TipoProducto;
import org.springframework.samples.petclinic.repository.TipoProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoProductoService {
	
	private TipoProductoRepository tipoProductoRepository;
	
	@Autowired
	public TipoProductoService(TipoProductoRepository tipoProductoRepository) {
		super();
		this.tipoProductoRepository = tipoProductoRepository;
	}

	public Collection<TipoProducto> findAll() throws DataAccessException {
		return tipoProductoRepository.findAll();
	}
}
