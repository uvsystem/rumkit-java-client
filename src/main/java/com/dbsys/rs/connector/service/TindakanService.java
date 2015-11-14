package com.dbsys.rs.connector.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Tindakan;

public class TindakanService extends AbstractService {

    private static TindakanService instance;

    private TindakanService() {
        super();
    }

    private TindakanService(String host) {
        super(host);
    }

    public static TindakanService getInstance() {
        if (instance == null)
            instance = new TindakanService();
        return instance;
    }

    public static TindakanService getInstance(String host) {
        if (instance == null)
            instance = new TindakanService(host);
        if (!instance.getHost().equals(host))
            instance.setHost(host);
        return instance;
    }

    public Tindakan simpan(Tindakan tindakan) throws ServiceException {
        HttpEntity<Tindakan> entity = new HttpEntity<>(tindakan, getHeaders());

        ResponseEntity<EntityRestMessage<Tindakan>> response;
        response = restTemplate.exchange("{treatmentService}/tindakan", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Tindakan>>() {}, 
                treatmentService);

        EntityRestMessage<Tindakan> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Tindakan get(String nama, Kelas kelas) throws ServiceException {
        HttpEntity<Tindakan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Tindakan>> resposen;
        resposen = restTemplate.exchange("{treatmentService}/tindakan/nama/{nama}/kelas/{kelas}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Tindakan>>() {}, 
                treatmentService, nama, kelas);

        EntityRestMessage<Tindakan> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Tindakan> getAll() throws ServiceException {
        HttpEntity<Tindakan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Tindakan>> resposen;
        resposen = restTemplate.exchange("{treatmentService}/tindakan", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Tindakan>>() {}, 
                treatmentService);

        ListEntityRestMessage<Tindakan> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Tindakan> cari(String keyword) throws ServiceException {
        HttpEntity<Tindakan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Tindakan>> resposen;
        resposen = restTemplate.exchange("{treatmentService}/tindakan/keyword/{keyword}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Tindakan>>() {}, 
                treatmentService, keyword);

        ListEntityRestMessage<Tindakan> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
