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
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokEksternal;
import com.dbsys.rs.lib.entity.StokInternal;
import com.dbsys.rs.lib.entity.StokKembali;
import com.dbsys.rs.lib.entity.Unit;

public class StokService extends AbstractService {
	
    private static StokService instance;

    private StokService() {
        super();
    }

    private StokService(String host) {
        super(host);
    }

    public static StokService getInstance() {
        if (instance == null)
            instance = new StokService();
        return instance;
    }

    public static StokService getInstance(String host) {
        if (instance == null)
            instance = new StokService(host);
        if (!instance.getHost().equals(host))
            instance.setHost(host);
        return instance;
    }

    public void simpan(Stok stok) throws ServiceException {
        HttpEntity<Stok> entity = new HttpEntity<>(stok, getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{inventoryService}/stok", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, 
                inventoryService);

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }
    
    public void masuk(Barang barang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
        StokEksternal stok = new StokEksternal();
        stok.setBarang(barang);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setJumlah(jumlah);
        stok.setJenis(Stok.JenisStok.MASUK);
        
        simpan(stok);
    }
    
    public void keluar(Barang barang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
        StokEksternal stok = new StokEksternal();
        stok.setBarang(barang);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setJumlah(jumlah);
        stok.setJenis(Stok.JenisStok.KELUAR);
        
        simpan(stok);
    }
    
    public void supply(Barang barang, Long jumlah, Date tanggal, Time jam, Unit unit) throws ServiceException {
        StokInternal stok = new StokInternal();
        stok.setBarang(barang);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setJumlah(jumlah);
        stok.setUnit(unit);

        simpan(stok);
    }
    
    public void kembali(Barang barang, Long jumlah, Date tanggal, Time jam, Pasien pasien) throws ServiceException {
        StokKembali stok = new StokKembali();
        stok.setBarang(barang);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setJumlah(jumlah);
        stok.setPasien(pasien);

        simpan(stok);
    }

    public List<Stok> stokMasuk(Date awal, Date akhir) throws ServiceException {
        HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Stok>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/stok/masuk/{awal}/to/{akhir}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<Stok>>() {}, 
                inventoryService, awal, akhir);

        ListEntityRestMessage<Stok> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Stok> stokKeluar(Date awal, Date akhir) throws ServiceException {
        HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Stok>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/stok/keluar/{awal}/to/{akhir}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<Stok>>() {}, 
                inventoryService, awal, akhir);

        ListEntityRestMessage<Stok> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Stok> stokKembali(Pasien pasien) throws ServiceException {
        HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Stok>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/stok/pasien/{pasien}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<Stok>>() {}, 
                inventoryService, pasien.getId());

        ListEntityRestMessage<Stok> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<Stok> stokKembali(Date awal, Date akhir) throws ServiceException {
        HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Stok>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/stok/{awal}/to/{akhir}/pasien", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<Stok>>() {}, 
                inventoryService, awal, akhir);

        ListEntityRestMessage<Stok> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Stok> stokUnit(Date awal, Date akhir, Unit unit) throws ServiceException {
        HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Stok>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/stok/{awal}/to/{akhir}/unit/{unit}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<Stok>>() {}, 
                inventoryService, awal, akhir, unit.getId());

        ListEntityRestMessage<Stok> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Stok> stokUnit(Date awal, Date akhir) throws ServiceException {
        HttpEntity<Stok> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Stok>> resposen;
        resposen = restTemplate.exchange("{inventoryService}/stok/{awal}/to/{akhir}/unit", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<Stok>>() {}, 
                inventoryService, awal, akhir);

        ListEntityRestMessage<Stok> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
                throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
