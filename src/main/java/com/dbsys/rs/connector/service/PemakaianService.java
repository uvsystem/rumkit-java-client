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
import com.dbsys.rs.lib.entity.Pemakaian;

public class PemakaianService extends AbstractService {

	private static PemakaianService instance;
	
	private PemakaianService() {
		super();
	}
	
	private PemakaianService(String host) {
		super(host);
	}
	
	public static PemakaianService getInstance() {
		if (instance == null)
			instance = new PemakaianService();
		return instance;
	}
	
	public static PemakaianService getInstance(String host) {
		if (instance == null)
			instance = new PemakaianService(host);
		if (!instance.getHost().equals(host))
			instance.setHost(host);
		return instance;
	}
	
	public Pemakaian simpan(Pemakaian pemakaian) throws ServiceException {
		HttpEntity<Pemakaian> entity = new HttpEntity<>(pemakaian, getHeaders());
		
		ResponseEntity<EntityRestMessage<Pemakaian>> response;
		response = restTemplate.exchange("{usageService}/pemakaian", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pemakaian>>() {}, 
				usageService);
		
		EntityRestMessage<Pemakaian> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public Pemakaian getById(Long id) throws ServiceException {
		HttpEntity<Pemakaian> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<Pemakaian>> response;
		response = restTemplate.exchange("{usageService}/pemakaian/{id}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pemakaian>>() {}, 
				usageService, id);
		
		EntityRestMessage<Pemakaian> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public List<Pemakaian> getByPasien(Long idPasien) throws ServiceException {
		HttpEntity<Pemakaian> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Pemakaian>> response;
		response = restTemplate.exchange("{usageService}/pemakaian/pasien/{idPasien}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Pemakaian>>() {}, 
				usageService, idPasien);
		
		ListEntityRestMessage<Pemakaian> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
}
