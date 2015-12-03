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
import com.dbsys.rs.connector.adapter.StokAdapter;
import com.dbsys.rs.connector.adapter.StokEksternalAdapter;
import com.dbsys.rs.connector.adapter.StokInternalAdapter;
import com.dbsys.rs.connector.adapter.StokKembaliAdapter;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokEksternal;
import com.dbsys.rs.lib.entity.StokInternal;
import com.dbsys.rs.lib.entity.StokKembali;
import com.dbsys.rs.lib.entity.Unit;
import java.util.ArrayList;

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

    public Stok simpan(Stok stok) throws ServiceException {
        StokAdapter stokAdapter;
        if (stok instanceof StokEksternal) {
            stokAdapter = new StokEksternalAdapter((StokEksternal) stok);
        } else if (stok instanceof StokInternal) {
            stokAdapter = new StokInternalAdapter((StokInternal) stok);
        } else if (stok instanceof StokKembali) {
            stokAdapter = new StokKembaliAdapter((StokKembali) stok);
        } else {
            stokAdapter = new StokAdapter(stok);
        }
        
        HttpEntity<StokAdapter> entity = new HttpEntity<>(stokAdapter, getHeaders());

        ResponseEntity<EntityRestMessage<StokAdapter>> response;
        response = restTemplate.exchange("{service}/stok", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<StokAdapter>>() {}, service);

        EntityRestMessage<StokAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel().getStok();
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
    
    public void kembali(Barang barang, Long jumlah, Date tanggal, Time jam, Pasien pasien, String nomor) throws ServiceException {
        StokKembali stok = new StokKembali();
        stok.setBarang(barang);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setJumlah(jumlah);
        stok.setPasien(pasien);
        stok.setNomor(nomor);

        simpan(stok);
    }

    public List<Stok> stokMasuk(Date awal, Date akhir) throws ServiceException {
        HttpEntity<StokAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/stok/masuk/{awal}/to/{akhir}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<StokAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Stok> stokKeluar(Date awal, Date akhir) throws ServiceException {
        HttpEntity<StokAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/stok/keluar/{awal}/to/{akhir}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<StokAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Stok> stokKembali(Pasien pasien) throws ServiceException {
        HttpEntity<StokAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/stok/pasien/{pasien}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokAdapter>>() {}, service, pasien.getId());

        ListEntityRestMessage<StokAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Stok> stokKembali(String nomor) throws ServiceException {
        HttpEntity<StokAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/stok/nomor/{nomor}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokAdapter>>() {}, service, nomor);

        ListEntityRestMessage<StokAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }
    
    public List<Stok> stokKembali(Date awal, Date akhir) throws ServiceException {
        HttpEntity<StokAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/stok/{awal}/to/{akhir}/pasien", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<StokAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Stok> stokUnit(Date awal, Date akhir, Unit unit) throws ServiceException {
        HttpEntity<StokAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/stok/{awal}/to/{akhir}/unit/{unit}", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokAdapter>>() {}, service, awal, akhir, unit.getId());

        ListEntityRestMessage<StokAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }

    public List<Stok> stokUnit(Date awal, Date akhir) throws ServiceException {
        HttpEntity<StokAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<StokAdapter>> resposen;
        resposen = restTemplate.exchange("{service}/stok/{awal}/to/{akhir}/unit", HttpMethod.GET, entity,
                new ParameterizedTypeReference<ListEntityRestMessage<StokAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<StokAdapter> message = resposen.getBody();
        if (message.getTipe().equals(Type.ERROR))
                throw new ServiceException(message.getMessage());
        return getList(message.getList());
    }
    
    private List<Stok> getList(List<StokAdapter> listAdapter) {
        List<Stok> list = new ArrayList<>();
        for (StokAdapter stokAdapter : listAdapter)
            list.add(stokAdapter.getStok());
        return list;
    }
}
