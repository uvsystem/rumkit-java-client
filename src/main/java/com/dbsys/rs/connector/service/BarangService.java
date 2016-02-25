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
import com.dbsys.rs.client.entity.Barang;

public class BarangService extends AbstractService {

    private static BarangService instance;

    private BarangService() {
        super();
        service = String.format("%s/rumkit-inventory-service", getHost());
    }

    private BarangService(String host) {
        super(host);
        service = String.format("%s/rumkit-inventory-service", getHost());
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

    public Barang simpan(Barang barang) throws ServiceException {
        HttpEntity<Barang> entity = new HttpEntity<>(barang, getHeaders());

        ResponseEntity<EntityRestMessage<Barang>> response;
        response = restTemplate.exchange("{service}/barang", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Barang>>() {}, 
                service);

        EntityRestMessage<Barang> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Barang> getAll() throws ServiceException {
        HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Barang>> response;
        response = restTemplate.exchange("{service}/barang", HttpMethod.GET, entity, 
            new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, service);

        ListEntityRestMessage<Barang> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Barang> getAll(Class cls) throws ServiceException {
        HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Barang>> response;
        response = restTemplate.exchange("{service}/barang/class/{class}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, 
                service, cls.getSimpleName());

        ListEntityRestMessage<Barang> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Barang> cari(String keyword) throws ServiceException {
        HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Barang>> resposen;
        resposen = restTemplate.exchange("{service}/barang/keyword/{keyword}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, 
                service, keyword);

        ListEntityRestMessage<Barang> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Barang> cari(String keyword, Class cls) throws ServiceException {
        HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Barang>> resposen;
        resposen = restTemplate.exchange("{service}/barang/keyword/{keyword}/class/{class}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Barang>>() {}, 
                service, keyword, cls.getSimpleName());

        ListEntityRestMessage<Barang> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public void hapus(Barang barang) throws ServiceException {
        HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> resposen;
        resposen = restTemplate.exchange("{service}/barang/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, barang.getId());

        RestMessage message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

}
