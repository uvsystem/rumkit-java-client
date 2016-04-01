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
import com.dbsys.rs.client.entity.Pegawai;

public class PegawaiServices extends AbstractService {

    private static PegawaiServices instance;

    private PegawaiServices() {
        super();
        service = String.format("%s/rumkit-hr-service", getHost());
    }

    private PegawaiServices(String host) {
        super(host);
        service = String.format("%s/rumkit-hr-service", getHost());
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

    public Pegawai simpan(Pegawai pegawai) throws ServiceException {
        HttpEntity<Pegawai> entity = new HttpEntity<>(pegawai, getHeaders());

        ResponseEntity<EntityRestMessage<Pegawai>> response;
        response = restTemplate.exchange("{service}/pegawai", HttpMethod.POST, entity, 
            new ParameterizedTypeReference<EntityRestMessage<Pegawai>>() {}, service);

        EntityRestMessage<Pegawai> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Pegawai> getAll() throws ServiceException {
        HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai", HttpMethod.GET, entity,
            new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, service);

        ListEntityRestMessage<Pegawai> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pegawai> getAll(Class cls) throws ServiceException {
        HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai/class/{class}", HttpMethod.GET, entity,
            new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, service, cls.getSimpleName());

        ListEntityRestMessage<Pegawai> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pegawai> cari(String keyword) throws ServiceException {
        HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai/keyword/{keyword}", HttpMethod.GET, entity,
            new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, service, keyword);

        ListEntityRestMessage<Pegawai> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pegawai> cari(String keyword, Class cls) throws ServiceException {
        HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pegawai>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai/keyword/{keyword}/class/{class}", HttpMethod.GET, entity,
            new ParameterizedTypeReference<ListEntityRestMessage<Pegawai>>() {}, service, keyword, cls.getSimpleName());

        ListEntityRestMessage<Pegawai> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public void hapus(Pegawai pegawai) throws ServiceException {
        HttpEntity<Pegawai> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> resposen;
        resposen = restTemplate.exchange("{service}/pegawai/{id}", HttpMethod.DELETE, entity,
            new ParameterizedTypeReference<RestMessage>() {}, service, pegawai.getId());

        RestMessage message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

}
