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
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.lib.entity.Pasien.StatusPasien;

public class PasienService extends AbstractService {

	private static PasienService instance;
	
	private PasienService() {
		super();
	}
	
	private PasienService(String host) {
		super(host);
	}
	
	public static PasienService getInstance() {
		if (instance == null)
			instance = new PasienService();
		return instance;
	}
	
	public static PasienService getInstance(String host) {
		if (instance == null)
			instance = new PasienService(host);
		if (!instance.getHost().equals(host))
			instance.setHost(host);
		return instance;
	}
	
	public Pasien daftar(Long idPenduduk, Penanggung penanggung, Date tanggal, String kode) throws ServiceException {
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/penduduk/{idPenduduk}/penangung/{penanggung}/tanggal/{tanggal}/kode/{kode}", HttpMethod.POST, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
				patientService, idPenduduk, penanggung, tanggal, verifyString(kode));
		
		EntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public Pasien keluar(Long id, KeadaanPasien keadaan) throws ServiceException {
		Date tanggal = DateUtil.getDate();
		Time jam = DateUtil.getTime();
		
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/{id}/tanggal/{tanggal}/jam/{jam}/keadaan/{keadaan}", 
				HttpMethod.PUT, entity, new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, patientService, id, tanggal, jam, keadaan);
		
		EntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public Pasien bayar(Long id, Long jumlah) throws ServiceException {
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/{id}/jumlah/{jumlah}", 
				HttpMethod.PUT, entity, new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, patientService, id, jumlah);
		
		EntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public Pasien get(Long id) throws ServiceException {
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/{id}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
				patientService, id);
		
		EntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public Pasien get(String kode) throws ServiceException {
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<EntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/kode/{kode}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
				patientService, kode);
		
		EntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getModel();
	}
	
	public List<Pasien> getByPenduduk(Long id) throws ServiceException {
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/penduduk/{id}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
				patientService, id);
		
		ListEntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Pasien> getByUnit(Long id) throws ServiceException {
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/unit/{id}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
				patientService, id);
		
		ListEntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
	
	public List<Pasien> cari(String keyword) throws ServiceException {
		HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());
		
		ResponseEntity<ListEntityRestMessage<Pasien>> response;
		response = restTemplate.exchange("{patientService}/pasien/keyword/{keyword}", HttpMethod.GET, entity, 
				new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
				patientService, keyword);
		
		ListEntityRestMessage<Pasien> message = response.getBody();
		if (message.getTipe().equals(Type.ERROR))
			throw new ServiceException(message.getMessage());
		return message.getList();
	}
}
