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
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.EntityRestMessage;
import com.dbsys.rs.client.Kelas;
import com.dbsys.rs.client.ListEntityRestMessage;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.RestMessage;
import com.dbsys.rs.client.RestMessage.Type;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.client.entity.Pasien.Pendaftaran;
import com.dbsys.rs.client.entity.Penduduk;
import com.dbsys.rs.client.entity.Unit;

public class PasienService extends AbstractService {

    private static PasienService instance;

    private PasienService() {
        super();
        service = String.format("%s/rumkit-patient-service", getHost());
    }

    private PasienService(String host) {
        super(host);
        service = String.format("%s/rumkit-patient-service", getHost());
    }

    public static PasienService getInstance() {
        if (instance == null)
            instance = new PasienService();
        return instance;
    }

    public static PasienService getInstance(String host) {
        if (instance == null)
            instance = new PasienService(host);
        if (!instance.getHost().equals(host))
            instance.setHost(host);
        return instance;
    }

    public Pasien daftar(Penduduk penduduk, Penanggung penanggung, Date tanggal, String kode, Pendaftaran pendaftaran, Kelas kelas, Unit tujuan) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/penduduk/{idPenduduk}/penanggung/{penanggung}/tanggal/{tanggal}/kode/{kode}/pendaftaran/{pendaftaran}/kelas/{kelas}/tujuan/{tujuan}", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
                service, penduduk.getId(), penanggung, tanggal, verifyString(kode), pendaftaran, kelas, tujuan.getId());

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pasien keluar(Pasien pasien, KeadaanPasien keadaan, Pasien.StatusPasien status) throws ServiceException {
        Date tanggal = DateUtil.getDate();
        Time jam = DateUtil.getTime();

        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/{id}/tanggal/{tanggal}/jam/{jam}/keadaan/{keadaan}/status/{status}", 
                HttpMethod.PUT, entity, new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, service, pasien.getId(), tanggal, jam, keadaan, status);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pasien bayar(Pasien pasien, Long jumlah) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/{id}/jumlah/{jumlah}", 
                HttpMethod.PUT, entity, new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, service, pasien.getId(), jumlah);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pasien get(Long id) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
                service, id);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pasien get(String kode) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/kode/{kode}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
                service, kode);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Pasien> getByPenduduk(Penduduk penduduk) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/penduduk/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                service, penduduk.getId());

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pasien> getByUnit(Unit unit) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/unit/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                service, unit.getId());

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<Pasien> get(Date awal, Date akhir) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                service, awal, akhir);

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<Pasien> get(Date awal, Date akhir, Pasien.Pendaftaran pendaftaran) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/{awal}/to/{akhir}/pendaftaran/{pendaftaran}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                service, awal, akhir, pendaftaran);

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<Pasien> getByMedrek(String nomorMedrek) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/medrek/{medrek}/tunggakan", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                service, nomorMedrek);

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pasien> cari(String keyword) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/keyword/{keyword}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                service, keyword);

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public void ubahKelas(Pasien pasien, Kelas kelas) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/pasien/{id}/kelas/{kelas}", HttpMethod.PUT, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, pasien.getId(), kelas);

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public void ubahPenanggung(Pasien pasien, Penanggung penanggung) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/pasien/{id}/penanggung/{penanggung}", HttpMethod.PUT, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, pasien.getId(), penanggung);

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public void update(Pasien pasien, Pasien.KeadaanPasien keadaan, Pasien.StatusPasien status) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/pasien/{id}/status/{status}/keadaan/{keadaan}", HttpMethod.PUT, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, pasien.getId(), status, keadaan);

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public void hapus(Pasien pasien) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/pasien/{id}", HttpMethod.DELETE, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, pasien.getId());

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public List<Pasien> cariGuest(String keyword) throws ServiceException {
        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{service}/pasien/keyword/{keyword}/guest", HttpMethod.GET, null, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, service, keyword);

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public void masuk(String kode, Long idUnit) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/pasien/{kode}/unit/{unit}", HttpMethod.PUT, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, kode, idUnit);

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }

    public void keluar(String kode) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/pasien/{kode}/unit", HttpMethod.PUT, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, service, kode);

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }
}
