package com.dbsys.rs.connector.service;

import java.sql.Date;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pembayaran;

public class PembayaranService extends AbstractService {

    private static PembayaranService instance;

    private PembayaranService() {
        super();
    }

    private PembayaranService(String host) {
        super(host);
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
        response = restTemplate.exchange("{paymentService}/pembayaran", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pembayaran>>() {}, paymentService);

        EntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pembayaran get(String kode) throws ServiceException {
        HttpEntity<Pembayaran> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pembayaran>> response;
        response = restTemplate.exchange("{paymentService}/pembayaran/{kode}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pembayaran>>() {}, paymentService, kode);

        EntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Pembayaran> get(Pasien pasien) throws ServiceException {
        HttpEntity<Pembayaran> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pembayaran>> response;
        response = restTemplate.exchange("{paymentService}/pembayaran/pasien{kode}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pembayaran>>() {}, paymentService, pasien.getId());

        ListEntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pembayaran> get(Date awal, Date akhir) throws ServiceException {
        HttpEntity<Pembayaran> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pembayaran>> response;
        response = restTemplate.exchange("{paymentService}/pembayaran/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pembayaran>>() {}, paymentService, awal, akhir);

        ListEntityRestMessage<Pembayaran> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
