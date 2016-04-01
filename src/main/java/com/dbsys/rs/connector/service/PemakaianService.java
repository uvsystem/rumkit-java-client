package com.dbsys.rs.connector.service;

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
import com.dbsys.rs.client.entity.Pemakaian;

public class PemakaianService extends AbstractService {

    private static PemakaianService instance;

    private PemakaianService() {
        super();
        service = String.format("%s/rumkit-serve-service", getHost());
    }

    private PemakaianService(String host) {
        super(host);
        service = String.format("%s/rumkit-serve-service", getHost());
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
        response = restTemplate.exchange("{service}/pemakaian", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pemakaian>>() {}, service);

        EntityRestMessage<Pemakaian> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }
    
    public void hapus(Pemakaian pemakaian) throws ServiceException {
        HttpEntity<Pemakaian> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/pemakaian/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, pemakaian.getId());

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public Pemakaian getById(Long id) throws ServiceException {
        HttpEntity<Pemakaian> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pemakaian>> response;
        response = restTemplate.exchange("{service}/pemakaian/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pemakaian>>() {}, service, id);

        EntityRestMessage<Pemakaian> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Pemakaian> getByPasien(Pasien pasien) throws ServiceException {
        HttpEntity<Pemakaian> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pemakaian>> response;
        response = restTemplate.exchange("{service}/pemakaian/pasien/{idPasien}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pemakaian>>() {}, service, pasien.getId());

        ListEntityRestMessage<Pemakaian> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pemakaian> getByNomor(String nomor) throws ServiceException {
        HttpEntity<Pemakaian> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pemakaian>> response;
        response = restTemplate.exchange("{service}/pemakaian/nomor/{nomor}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pemakaian>>() {}, service, nomor);

        ListEntityRestMessage<Pemakaian> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
