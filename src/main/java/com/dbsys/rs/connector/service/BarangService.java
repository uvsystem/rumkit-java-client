package com.dbsys.rs.connector.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.adapter.BahanHabisPakaiAdapter;
import com.dbsys.rs.connector.adapter.BarangAdapter;
import com.dbsys.rs.connector.adapter.ObatAdapter;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import java.util.ArrayList;

public class BarangService extends AbstractService {

    private static BarangService instance;

    private BarangService() {
        super();
    }

    private BarangService(String host) {
        super(host);
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
        BarangAdapter barangAdapter;
        
        if (barang instanceof BahanHabisPakai) {
            barangAdapter = new BahanHabisPakaiAdapter((BahanHabisPakai) barang);
        } else if (barang instanceof ObatFarmasi) {
            barangAdapter = new ObatAdapter((ObatFarmasi) barang);
        } else {
            barangAdapter = new BarangAdapter(barang);
        }
        HttpEntity<BarangAdapter> entity = new HttpEntity<>(barangAdapter, getHeaders());

        ResponseEntity<EntityRestMessage<BarangAdapter>> response;
        response = restTemplate.exchange("{inventoryService}/barang", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<BarangAdapter>>() {}, 
                inventoryService);

        EntityRestMessage<BarangAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel().getBarang();
    }

    public List<Barang> getAll() throws ServiceException {
        HttpEntity<BarangAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<BarangAdapter>> response;
        response = restTemplate.exchange("{inventoryService}/barang", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<BarangAdapter>>() {}, 
                inventoryService);

        ListEntityRestMessage<BarangAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Barang> getAll(Class cls) throws ServiceException {
        HttpEntity<BarangAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<BarangAdapter>> response;
        response = restTemplate.exchange("{inventoryService}/barang/class/{class}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<BarangAdapter>>() {}, 
                inventoryService, cls.getSimpleName());

        ListEntityRestMessage<BarangAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Barang> cari(String keyword) throws ServiceException {
        HttpEntity<BarangAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<BarangAdapter>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/barang/keyword/{keyword}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<BarangAdapter>>() {}, 
                inventoryService, keyword);

        ListEntityRestMessage<BarangAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Barang> cari(String keyword, Class cls) throws ServiceException {
        HttpEntity<BarangAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<BarangAdapter>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/barang/keyword/{keyword}/class/{class}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<BarangAdapter>>() {}, 
                inventoryService, keyword, cls.getSimpleName());

        ListEntityRestMessage<BarangAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public void hapus(Barang barang) throws ServiceException {
        HttpEntity<Barang> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> resposen;
        resposen = restTemplate.exchange("{inventoryService}/barang/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, inventoryService, barang.getId());

        RestMessage message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }
    
    private List<Barang> getList(List<BarangAdapter> listAdapter) {
        List<Barang> list = new ArrayList<>();
        for (BarangAdapter barangAdapter : listAdapter)
            list.add(barangAdapter.getBarang());
        return list;
    }
}
