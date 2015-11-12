package com.dbsys.rs.connector.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.KategoriTindakan;

public class KategoriService extends AbstractService {

	private static KategoriService instance;
	
	private KategoriService() {
		super();
	}
	
	private KategoriService(String host) {
		super(host);
	}
	
	public static KategoriService getInstance() {
		if (instance == null)
			instance = new KategoriService();
		return instance;
	}
	
	public static KategoriService getInstance(String host) {
		if (instance == null)
			instance = new KategoriService(host);
		if (!instance.getHost().equals(host))
			instance.setHost(host);
		return instance;
	}

	public KategoriTindakan simpan(KategoriTindakan kategori) throws ServiceException {
		HttpEntity<KategoriTindakan> entity = new HttpEntity<KategoriTindakan>(kategori, getHeaders());
		
		ResponseEntity<EntityRestMessage<KategoriTindakan>> response;
		response = restTemplate.exchange("{treatmentService}/kategori", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<EntityRestMessage<KategoriTindakan>>() {},
				treatmentService);
		
		EntityRestMessage<KategoriTindakan> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}

	public KategoriTindakan getById(Long id) throws ServiceException {
		HttpEntity<KategoriTindakan> entity = new HttpEntity<KategoriTindakan>(getHeaders());
		
		ResponseEntity<EntityRestMessage<KategoriTindakan>> response;
		response = restTemplate.exchange("{treatmentService}/kategori/{id}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<EntityRestMessage<KategoriTindakan>>() {},
				treatmentService, id);
		
		EntityRestMessage<KategoriTindakan> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public List<KategoriTindakan> getAll() throws ServiceException {
		HttpEntity<KategoriTindakan> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<KategoriTindakan>> response;
		response = restTemplate.exchange("{treatmentService}/kategori", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<KategoriTindakan>>() {},
				treatmentService);
		
		ListEntityRestMessage<KategoriTindakan> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
}
