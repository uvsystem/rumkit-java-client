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
import com.dbsys.rs.client.EntityRestMessage;
import com.dbsys.rs.client.ListEntityRestMessage;
import com.dbsys.rs.client.RestMessage.Type;
import com.dbsys.rs.client.entity.Barang;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.StokKembali;
import com.dbsys.rs.client.entity.Unit;

public class StokService extends AbstractService {
	
    private static StokService instance;

    private StokService() {
        super();
        service = String.format("%s/rumkit-inventory-service", getHost());
    }

    private StokService(String host) {
        super(host);
        service = String.format("%s/rumkit-inventory-service", getHost());
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

    public StokKembali simpan(StokKembali StokKembali) throws ServiceException {
        
        HttpEntity<StokKembali> entity = new HttpEntity<>(StokKembali, getHeaders());

        ResponseEntity<EntityRestMessage<StokKembali>> response;
        response = restTemplate.exchange("{service}/StokKembali", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<StokKembali>>() {}, service);

        EntityRestMessage<StokKembali> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }
    
    public void masuk(Barang barang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
        StokEksternal StokKembali = new StokEksternal();
        StokKembali.setBarang(barang);
        StokKembali.setTanggal(tanggal);
        StokKembali.setJam(jam);
        StokKembali.setJumlah(jumlah);
        StokKembali.setJenis(StokKembali.JenisStok.MASUK);
        
        simpan(StokKembali);
    }
    
    public void kembali(Barang barang, Long jumlah, Date tanggal, Time jam, Pasien pasien, String nomor) throws ServiceException {
        StokKembali StokKembali = new StokKembali();
        StokKembali.setBarang(barang);
        StokKembali.setTanggal(tanggal);
        StokKembali.setJam(jam);
        StokKembali.setJumlah(jumlah);
        StokKembali.setPasien(pasien);
        StokKembali.setNomor(nomor);

        simpan(StokKembali);
    }

    public List<StokKembali> stokMasuk(Date awal, Date akhir) throws ServiceException {
        HttpEntity<StokKembali> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokKembali>> resposen;
        resposen = restTemplate.exchange("{service}/StokKembali/masuk/{awal}/to/{akhir}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokKembali>>() {}, service, awal, akhir);

        ListEntityRestMessage<StokKembali> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<StokKembali> stokKembali(Pasien pasien) throws ServiceException {
        HttpEntity<StokKembali> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokKembali>> resposen;
        resposen = restTemplate.exchange("{service}/StokKembali/pasien/{pasien}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokKembali>>() {}, service, pasien.getId());

        ListEntityRestMessage<StokKembali> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<StokKembali> stokKembali(String nomor) throws ServiceException {
        HttpEntity<StokKembali> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokKembali>> resposen;
        resposen = restTemplate.exchange("{service}/StokKembali/nomor/{nomor}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokKembali>>() {}, service, nomor);

        ListEntityRestMessage<StokKembali> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<StokKembali> stokKembali(Date awal, Date akhir) throws ServiceException {
        HttpEntity<StokKembali> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokKembali>> resposen;
        resposen = restTemplate.exchange("{service}/StokKembali/{awal}/to/{akhir}/pasien", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokKembali>>() {}, service, awal, akhir);

        ListEntityRestMessage<StokKembali> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<StokKembali> stokUnit(Date awal, Date akhir, Unit unit) throws ServiceException {
        HttpEntity<StokKembali> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokKembali>> resposen;
        resposen = restTemplate.exchange("{service}/StokKembali/{awal}/to/{akhir}/unit/{unit}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokKembali>>() {}, service, awal, akhir, unit.getId());

        ListEntityRestMessage<StokKembali> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<StokKembali> stokUnit(Date awal, Date akhir) throws ServiceException {
        HttpEntity<StokKembali> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokKembali>> resposen;
        resposen = restTemplate.exchange("{service}/StokKembali/{awal}/to/{akhir}/unit", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokKembali>>() {}, service, awal, akhir);

        ListEntityRestMessage<StokKembali> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
                throw new ServiceException(message.getMessage());
        return message.getList();
    }

}
