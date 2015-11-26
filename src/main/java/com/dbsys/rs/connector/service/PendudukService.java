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
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Penduduk;

public class PendudukService extends AbstractService {

    private static PendudukService instance;

    private PendudukService() {
        super();
    }

    private PendudukService(String host) {
        super(host);
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
        response = restTemplate.exchange("{patientService}/penduduk", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Penduduk>>() {}, 
                patientService);

        EntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public void hapus(Penduduk penduduk) throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{patientService}/penduduk/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, patientService, penduduk.getId());

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public Penduduk getById(Long id) throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Penduduk>> response;
        response = restTemplate.exchange("{patientService}/penduduk/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Penduduk>>() {}, 
                patientService, id);

        EntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Penduduk> cari(String keyword) throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Penduduk>> response;
        response = restTemplate.exchange("{patientService}/penduduk/keyword/{keyword}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Penduduk>>() {}, 
                patientService, keyword);

        ListEntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Penduduk> getAll() throws ServiceException {
        HttpEntity<Penduduk> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Penduduk>> response;
        response = restTemplate.exchange("{patientService}/penduduk", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Penduduk>>() {}, 
                patientService);

        ListEntityRestMessage<Penduduk> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
