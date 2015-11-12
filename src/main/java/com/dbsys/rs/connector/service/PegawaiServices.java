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
import com.dbsys.rs.lib.entity.Pegawai;

public class PegawaiServices extends AbstractService {

	private static PegawaiServices instance;
	
	private PegawaiServices() {
		super();
	}
	
	private PegawaiServices(String host) {
		super(host);
	}
	
	public static PegawaiServices getInstance() {
		if (instance == null)
			instance = new PegawaiServices();
		return instance;
	}
	
	public static PegawaiServices getInstance(String host) {
		if (instance == null)
			instance = new PegawaiServices(host);
		if (!instance.getHost().equals(host))
			instance.setHost(host);
		return instance;
	}
	
	public Pegawai simpan(Pegawai apoteker) throws ServiceException {
		HttpEntity<Pegawai> entity = new HttpEntity<>(apoteker, getHeaders());
		
		ResponseEntity<EntityRestMessage<Pegawai>> response;
		response = restTemplate.exchange("{hrService}", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pegawai>>() {}, 
				hrService);
		
		EntityRestMessage<Pegawai> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public List<Pegawai> getAll() throws ServiceException {
		HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

		ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
		resposen = restTemplate.exchange("{hrService}", HttpMethod.GET, entity,
				new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, 
				hrService);
		
		ListEntityRestMessage<Pegawai> message = resposen.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Pegawai> getAll(Class cls) throws ServiceException {
		HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

		ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
		resposen = restTemplate.exchange("{hrService}/class/{class}", HttpMethod.GET, entity,
				new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, 
				hrService, cls.getSimpleName());
		
		ListEntityRestMessage<Pegawai> message = resposen.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Pegawai> cari(String keyword) throws ServiceException {
		HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

		ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
		resposen = restTemplate.exchange("{hrService}/keyword/{keyword}", HttpMethod.GET, entity,
				new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, 
				hrService, keyword);
		
		ListEntityRestMessage<Pegawai> message = resposen.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Pegawai> cari(String keyword, Class cls) throws ServiceException {
		HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

		ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
		resposen = restTemplate.exchange("{hrService}/keyword/{keyword}/class/{class}", HttpMethod.GET, entity,
				new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, 
				hrService, keyword, cls.getSimpleName());
		
		ListEntityRestMessage<Pegawai> message = resposen.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
}
