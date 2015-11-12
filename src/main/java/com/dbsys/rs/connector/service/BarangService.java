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
import com.dbsys.rs.lib.entity.Barang;

public class BarangService extends AbstractService {

	private static BarangService instance;
	
	private BarangService() {
		super();
	}
	
	private BarangService(String host) {
		super(host);
	}
	
	public static BarangService getInstance() {
		if (instance == null)
			instance = new BarangService();
		return instance;
	}
	
	public static BarangService getInstance(String host) {
		if (instance == null)
			instance = new BarangService(host);
		if (!instance.getHost().equals(host))
			instance.setHost(host);
		return instance;
	}
	
	public Barang simpan(Barang bhp) throws ServiceException {
		HttpEntity<Barang> entity = new HttpEntity<>(bhp, getHeaders());
		
		ResponseEntity<EntityRestMessage<Barang>> response;
		response = restTemplate.exchange("{inventoryService}", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Barang>>() {}, 
				inventoryService);
		
		EntityRestMessage<Barang> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public List<Barang> getAll() throws ServiceException {
		HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Barang>> response;
		response = restTemplate.exchange("{inventoryService}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, 
				inventoryService);
		
		ListEntityRestMessage<Barang> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Barang> getAll(Class cls) throws ServiceException {
		HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Barang>> response;
		response = restTemplate.exchange("{inventoryService}/class/{class}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, 
				inventoryService, cls.getSimpleName());
		
		ListEntityRestMessage<Barang> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Barang> cari(String keyword) throws ServiceException {
		HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Barang>> resposen;
		resposen = restTemplate.exchange("{inventoryService}/keyword/{keyword}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, 
				inventoryService, keyword);
		
		ListEntityRestMessage<Barang> message = resposen.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Barang> cari(String keyword, Class cls) throws ServiceException {
		HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Barang>> resposen;
		resposen = restTemplate.exchange("{inventoryService}/keyword/{keyword}/class/{class}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, 
				inventoryService, keyword, cls.getSimpleName());
		
		ListEntityRestMessage<Barang> message = resposen.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
}
