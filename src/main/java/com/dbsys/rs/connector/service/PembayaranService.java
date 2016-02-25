package com.dbsys.rs.connector.service;

import java.sql.Date;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.client.EntityRestMessage;
import com.dbsys.rs.client.ListEntityRestMessage;
import com.dbsys.rs.client.RestMessage.Type;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pembayaran;

public class PembayaranService extends AbstractService {

    private static PembayaranService instance;

    private PembayaranService() {
        super();
        service = String.format("%s/rumkit-serve-service", getHost());
    }

    private PembayaranService(String host) {
        super(host);
        service = String.format("%s/rumkit-serve-service", getHost());
    }

    public static PembayaranService getInstance() {
        if (instance == null)
            instance = new PembayaranService();
        return instance;
    }

    public static PembayaranService getInstance(String host) {
        if (instance == null)
            instance = new PembayaranService(host);
        if (!instance.getHost().equals(host))
            instance.setHost(host);
        return instance;
    }

    public Pembayaran bayar(Pembayaran pembayaran) throws ServiceException {
        HttpEntity<Pembayaran> entity = new HttpEntity<>(pembayaran, getHeaders());

        ResponseEntity<EntityRestMessage<Pembayaran>> response;
        response = restTemplate.exchange("{service}/pembayaran", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pembayaran>>() {}, service);

        EntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pembayaran get(String kode) throws ServiceException {
        HttpEntity<Pembayaran> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pembayaran>> response;
        response = restTemplate.exchange("{service}/pembayaran/{kode}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pembayaran>>() {}, service, kode);

        EntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Pembayaran> get(Pasien pasien) throws ServiceException {
        HttpEntity<Pembayaran> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pembayaran>> response;
        response = restTemplate.exchange("{service}/pembayaran/pasien{kode}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pembayaran>>() {}, service, pasien.getId());

        ListEntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pembayaran> get(Date awal, Date akhir) throws ServiceException {
        HttpEntity<Pembayaran> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pembayaran>> response;
        response = restTemplate.exchange("{service}/pembayaran/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pembayaran>>() {}, service, awal, akhir);

        ListEntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
