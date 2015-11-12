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
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Stok;

public class StokService extends AbstractService {
	
	private static StokService instance;
	
	private StokService() {
		super();
	}
	
	private StokService(String host) {
		super(host);
	}
	
	public static StokService getInstance() {
		if (instance == null)
			instance = new StokService();
		return instance;
	}
	
	public static StokService getInstance(String host) {
		if (instance == null)
			instance = new StokService(host);
		if (!instance.getHost().equals(host))
			instance.setHost(host);
		return instance;
	}

	public void simpan(Long idBarang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
		HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<RestMessage> response;
		response = restTemplate.exchange("{inventoryService}/stok/keluar/barang/{idBarang}/jumlah/{jumlah}/tanggal/{tanggal}/jam/{jam}", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<RestMessage>() {}, 
				inventoryService, idBarang, jumlah, tanggal, jam);
		
		RestMessage message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
	}
	
	public List<Stok> cari(Date awal, Date akhir) throws ServiceException {
		HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());

		ResponseEntity<ListEntityRestMessage<Stok>> resposen;
		resposen = restTemplate.exchange("{inventoryService}/stok/keluar/{awal}/to/{akhir}", HttpMethod.GET, entity,
				new ParameterizedTypeReference<ListEntityRestMessage<Stok>>() {}, 
				inventoryService, awal, akhir);
		
		ListEntityRestMessage<Stok> message = resposen.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
}
