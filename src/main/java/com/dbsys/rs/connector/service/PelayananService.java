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
import com.dbsys.rs.connector.adapter.PelayananAdapter;
import com.dbsys.rs.connector.adapter.PelayananTemporalAdapter;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PelayananTemporal;
import java.util.ArrayList;

public class PelayananService extends AbstractService {

    private static PelayananService instance;

    private PelayananService() {
        super();
    }

    private PelayananService(String host) {
        super(host);
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
        PelayananAdapter pelayananAdapter;
        if (pelayanan instanceof PelayananTemporal) {
            pelayananAdapter = new PelayananTemporalAdapter((PelayananTemporal) pelayanan);
        } else if (pelayanan instanceof Pelayanan) {
            pelayananAdapter = new PelayananAdapter(pelayanan);
        } else {
            throw new ServiceException("Class tidak terdaftar");
        }
        
        HttpEntity<PelayananAdapter> entity = new HttpEntity<>(pelayananAdapter, getHeaders());

        ResponseEntity<EntityRestMessage<PelayananAdapter>> response;
        response = restTemplate.exchange("{serveService}/pelayanan", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<PelayananAdapter>>() {}, 
                serveService);

        EntityRestMessage<PelayananAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel().getPelayanan();
    }

    public void masukSal(PelayananTemporal pelayanan) throws ServiceException {
        HttpEntity<PelayananTemporalAdapter> entity = new HttpEntity<>(new PelayananTemporalAdapter(pelayanan), getHeaders());

        ResponseEntity<EntityRestMessage<PelayananTemporalAdapter>> response;
        response = restTemplate.exchange("{serveService}/pelayanan/temporal", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<PelayananTemporalAdapter>>() {}, 
                serveService);

        EntityRestMessage<PelayananTemporalAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public void keluarSal(Pasien pasien, Date tanggal, Time jam, Long tambahan, String keterangan) throws ServiceException {
        HttpEntity<PelayananTemporalAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<PelayananTemporalAdapter>> response;
        response = restTemplate.exchange("{serveService}/pelayanan/temporal/{idPasien}/tanggal/{tanggal}/jam/{jam}/tambahan/{tambahan}/keterangan/{keterangan}", HttpMethod.PUT, entity, 
                new ParameterizedTypeReference<EntityRestMessage<PelayananTemporalAdapter>>() {}, 
                serveService, pasien.getId(), tanggal, jam, tambahan, keterangan);

        EntityRestMessage<PelayananTemporalAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public Pelayanan getById(Long id) throws ServiceException {
        HttpEntity<PelayananAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<PelayananAdapter>> response;
        response = restTemplate.exchange("{serveService}/pelayanan/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<PelayananAdapter>>() {}, 
                serveService, id);

        EntityRestMessage<PelayananAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel().getPelayanan();
    }

    public void hapus(Pelayanan pelayanan) throws ServiceException {
        HttpEntity<Pelayanan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{serveService}/pelayanan/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, serveService, pelayanan.getId());

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public List<Pelayanan> getByPasien(Pasien pasien) throws ServiceException {
        HttpEntity<PelayananAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<PelayananAdapter>> response;
        response = restTemplate.exchange("{serveService}/pelayanan/pasien/{idPasien}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<PelayananAdapter>>() {}, 
                serveService, pasien.getId());

        ListEntityRestMessage<PelayananAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }
    
    private List<Pelayanan> getList(List<PelayananAdapter> listAdapter) {
        List<Pelayanan> list = new ArrayList<>();
        for (PelayananAdapter pelayananAdapter : listAdapter)
            list.add(pelayananAdapter.getPelayanan());
        return list;
    }
}
