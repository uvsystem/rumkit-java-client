package com.dbsys.rs.connector.service;

import java.sql.Date;
import java.sql.Time;
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
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PelayananTemporal;

public class PelayananService extends AbstractService {

	private static PelayananService instance;
	
	private PelayananService() {
		super();
	}
	
	private PelayananService(String host) {
		super(host);
	}
	
	public static PelayananService getInstance() {
		if (instance == null)
			instance = new PelayananService();
		return instance;
	}
	
	public static PelayananService getInstance(String host) {
		if (instance == null)
			instance = new PelayananService(host);
		if (!instance.getHost().equals(host))
			instance.setHost(host);
		return instance;
	}
	
	public Pelayanan simpan(Pelayanan pelayanan) throws ServiceException {
		HttpEntity<Pelayanan> entity = new HttpEntity<>(pelayanan, getHeaders());
		
		ResponseEntity<EntityRestMessage<Pelayanan>> response;
		response = restTemplate.exchange("{serveService}/pelayanan", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pelayanan>>() {}, 
				serveService);
		
		EntityRestMessage<Pelayanan> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public void masukSal(PelayananTemporal pelayanan) throws ServiceException {
		HttpEntity<PelayananTemporal> entity = new HttpEntity<>(pelayanan, getHeaders());
		
		ResponseEntity<EntityRestMessage<PelayananTemporal>> response;
		response = restTemplate.exchange("{serveService}/pelayanan/temporal", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<EntityRestMessage<PelayananTemporal>>() {}, 
				serveService);
		
		EntityRestMessage<PelayananTemporal> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
	}
	
	public void keluarSal(Long idPasien, Date tanggal, Time jam, Long tambahan, String keterangan) throws ServiceException {
		HttpEntity<PelayananTemporal> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<PelayananTemporal>> response;
		response = restTemplate.exchange("{serveService}/pelayanan/temporal/{idPasien}/tanggal/{tanggal}/jam/{jam}/tambahan/{tambahan}/keterangan/{keterangan}", HttpMethod.PUT, entity, 
				new ParameterizedTypeReference<EntityRestMessage<PelayananTemporal>>() {}, 
				serveService, idPasien, tanggal, jam, tambahan, keterangan);
		
		EntityRestMessage<PelayananTemporal> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
	}
	
	public Pelayanan getById(Long id) throws ServiceException {
		HttpEntity<Pelayanan> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<Pelayanan>> response;
		response = restTemplate.exchange("{serveService}/pelayanan/{id}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pelayanan>>() {}, 
				serveService, id);
		
		EntityRestMessage<Pelayanan> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public List<Pelayanan> getByPasien(Long idPasien) throws ServiceException {
		HttpEntity<Pelayanan> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Pelayanan>> response;
		response = restTemplate.exchange("{serveService}/pelayanan/pasien/{idPasien}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Pelayanan>>() {}, 
				serveService, idPasien);
		
		ListEntityRestMessage<Pelayanan> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
}
