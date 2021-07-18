package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.TipoProducto;
import org.springframework.samples.petclinic.repository.TipoProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class TipoProductoService {
	@Autowired
	private TipoProductoRepository tipoProductoRepository;
	
	public TipoProductoService(TipoProductoRepository tipoProductoRepository) {
		super();
		this.tipoProductoRepository = tipoProductoRepository;
	}

	@Transactional(readOnly = true)
	public Collection<TipoProducto> findAll() throws DataAccessException {
		return tipoProductoRepository.findAll();
	}
}
