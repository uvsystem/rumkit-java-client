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
import com.dbsys.rs.client.EntityRestMessage;
import com.dbsys.rs.client.ListEntityRestMessage;
import com.dbsys.rs.client.RestMessage;
import com.dbsys.rs.client.RestMessage.Type;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.PelayananTemporal;

public class PelayananService extends AbstractService {

    private static PelayananService instance;

    private PelayananService() {
        super();
        service = String.format("%s/rumkit-serve-service", getHost());
    }

    private PelayananService(String host) {
        super(host);
        service = String.format("%s/rumkit-serve-service", getHost());
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
        response = restTemplate.exchange("{service}/pelayanan", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pelayanan>>() {}, 
                service);

        EntityRestMessage<Pelayanan> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public void masukSal(PelayananTemporal pelayanan) throws ServiceException {
        HttpEntity<PelayananTemporal> entity = new HttpEntity<>(pelayanan, getHeaders());

        ResponseEntity<EntityRestMessage<PelayananTemporal>> response;
        response = restTemplate.exchange("{service}/pelayanan/temporal", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<PelayananTemporal>>() {}, 
                service);

        EntityRestMessage<PelayananTemporal> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public void keluarSal(Pasien pasien, Date tanggal, Time jam, Long tambahan, String keterangan) throws ServiceException {
        HttpEntity<PelayananTemporal> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<PelayananTemporal>> response;
        response = restTemplate.exchange("{service}/pelayanan/temporal/{idPasien}/tanggal/{tanggal}/jam/{jam}/tambahan/{tambahan}/keterangan/{keterangan}", HttpMethod.PUT, entity, 
                new ParameterizedTypeReference<EntityRestMessage<PelayananTemporal>>() {}, 
                service, pasien.getId(), tanggal, jam, tambahan, keterangan);

        EntityRestMessage<PelayananTemporal> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public Pelayanan getById(Long id) throws ServiceException {
        HttpEntity<Pelayanan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pelayanan>> response;
        response = restTemplate.exchange("{service}/pelayanan/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pelayanan>>() {}, 
                service, id);

        EntityRestMessage<Pelayanan> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public void hapus(Pelayanan pelayanan) throws ServiceException {
        HttpEntity<Pelayanan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage> response;
        response = restTemplate.exchange("{service}/pelayanan/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<EntityRestMessage>() {}, service, pelayanan.getId());

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public List<Pelayanan> getByPasien(Pasien pasien) throws ServiceException {
        HttpEntity<Pelayanan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pelayanan>> response;
        response = restTemplate.exchange("{service}/pelayanan/pasien/{idPasien}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pelayanan>>() {}, 
                service, pasien.getId());

        ListEntityRestMessage<Pelayanan> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

}
