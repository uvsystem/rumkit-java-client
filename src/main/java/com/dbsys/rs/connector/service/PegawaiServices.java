package com.dbsys.rs.connector.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.adapter.ApotekerAdapter;
import com.dbsys.rs.connector.adapter.DokterAdapter;
import com.dbsys.rs.connector.adapter.PegawaiAdapter;
import com.dbsys.rs.connector.adapter.PekerjaAdapter;
import com.dbsys.rs.connector.adapter.PerawatAdapter;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Apoteker;
import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Pekerja;
import com.dbsys.rs.lib.entity.Perawat;
import java.util.ArrayList;

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
        PegawaiAdapter pegawaiAdapter;

        if (pegawai instanceof Dokter) {
            pegawaiAdapter = new DokterAdapter((Dokter) pegawai);
        } else if (pegawai instanceof Perawat) {
            pegawaiAdapter = new PerawatAdapter((Perawat) pegawai);
        } else if (pegawai instanceof Apoteker) {
            pegawaiAdapter = new ApotekerAdapter((Apoteker) pegawai);
        } else if (pegawai instanceof Pekerja) {
            pegawaiAdapter = new PekerjaAdapter((Pekerja) pegawai);
        } else {
            pegawaiAdapter = new PegawaiAdapter(pegawai);
        }

        HttpEntity<PegawaiAdapter> entity = new HttpEntity<>(pegawaiAdapter, getHeaders());

        ResponseEntity<EntityRestMessage<PegawaiAdapter>> response;
        response = restTemplate.exchange("{service}/pegawai", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<PegawaiAdapter>>() {}, 
                service);

        EntityRestMessage<PegawaiAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel().getPegawai();
    }

    public List<Pegawai> getAll() throws ServiceException {
        HttpEntity<PegawaiAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<PegawaiAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<PegawaiAdapter>>() {}, 
                service);

        ListEntityRestMessage<PegawaiAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Pegawai> getAll(Class cls) throws ServiceException {
        HttpEntity<PegawaiAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<PegawaiAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai/class/{class}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<PegawaiAdapter>>() {}, 
                service, cls.getSimpleName());

        ListEntityRestMessage<PegawaiAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Pegawai> cari(String keyword) throws ServiceException {
        HttpEntity<PegawaiAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<PegawaiAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai/keyword/{keyword}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<PegawaiAdapter>>() {}, 
                service, keyword);

        ListEntityRestMessage<PegawaiAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Pegawai> cari(String keyword, Class cls) throws ServiceException {
        HttpEntity<PegawaiAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<PegawaiAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/pegawai/keyword/{keyword}/class/{class}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<PegawaiAdapter>>() {}, 
                service, keyword, cls.getSimpleName());

        ListEntityRestMessage<PegawaiAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
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
    
    private List<Pegawai> getList(List<PegawaiAdapter> listAdapter) {
        List<Pegawai> list = new ArrayList<>();
        for (PegawaiAdapter pegawaiAdapter : listAdapter)
            list.add(pegawaiAdapter.getPegawai());
        return list;
    }
}
