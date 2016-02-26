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
import com.dbsys.rs.client.entity.Penduduk;

public class PendudukService extends AbstractService {

    private static PendudukService instance;

    private PendudukService() {
        super();
        service = String.format("%s/rumkit-patient-service", getHost());
    }

    private PendudukService(String host) {
        super(host);
        service = String.format("%s/rumkit-patient-service", getHost());
    }

    public static PendudukService getInstance() {
        if (instance == null)
            instance = new PendudukService();
        return instance;
    }

    public static PendudukService getInstance(String host) {
        if (instance == null)
            instance = new PendudukService(host);
        if (!instance.getHost().equals(host))
            instance.setHost(host);
        return instance;
    }

    public Penduduk simpan(Penduduk penduduk) throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(penduduk, getHeaders());

        ResponseEntity<EntityRestMessage<Penduduk>> response;
        response = restTemplate.exchange("{service}/penduduk", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Penduduk>>() {}, 
                service);

        EntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public void hapus(Penduduk penduduk) throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/penduduk/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, penduduk.getId());

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public Penduduk getById(Long id) throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Penduduk>> response;
        response = restTemplate.exchange("{service}/penduduk/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Penduduk>>() {}, 
                service, id);

        EntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Penduduk> cari(String keyword) throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Penduduk>> response;
        response = restTemplate.exchange("{service}/penduduk/keyword/{keyword}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Penduduk>>() {}, 
                service, keyword);

        ListEntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Penduduk> getAll() throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Penduduk>> response;
        response = restTemplate.exchange("{service}/penduduk", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Penduduk>>() {}, 
                service);

        ListEntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
