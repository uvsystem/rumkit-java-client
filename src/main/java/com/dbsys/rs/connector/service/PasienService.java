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
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pasien.KeadaanPasien;
import com.dbsys.rs.lib.entity.Pasien.Pendaftaran;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Unit;

public class PasienService extends AbstractService {

    private static PasienService instance;

    private PasienService() {
        super();
    }

    private PasienService(String host) {
        super(host);
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
        response = restTemplate.exchange("{patientService}/pasien/penduduk/{idPenduduk}/penanggung/{penanggung}/tanggal/{tanggal}/kode/{kode}/pendaftaran/{pendaftaran}/kelas/{kelas}/tujuan/{tujuan}", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
                patientService, penduduk.getId(), penanggung, tanggal, verifyString(kode), pendaftaran, kelas, tujuan.getId());

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
        response = restTemplate.exchange("{patientService}/pasien/{id}/tanggal/{tanggal}/jam/{jam}/keadaan/{keadaan}/status/{status}", 
                HttpMethod.PUT, entity, new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, patientService, pasien.getId(), tanggal, jam, keadaan, status);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pasien bayar(Pasien pasien, Long jumlah) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{patientService}/pasien/{id}/jumlah/{jumlah}", 
                HttpMethod.PUT, entity, new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, patientService, pasien.getId(), jumlah);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pasien get(Long id) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{patientService}/pasien/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
                patientService, id);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public Pasien get(String kode) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<EntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{patientService}/pasien/kode/{kode}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Pasien>>() {}, 
                patientService, kode);

        EntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }

    public List<Pasien> getByPenduduk(Penduduk penduduk) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{patientService}/pasien/penduduk/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                patientService, penduduk.getId());

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pasien> getByUnit(Unit unit) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{patientService}/pasien/unit/{id}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                patientService, unit.getId());

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<Pasien> cari(String keyword) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Pasien>> response;
        response = restTemplate.exchange("{patientService}/pasien/keyword/{keyword}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Pasien>>() {}, 
                patientService, keyword);

        ListEntityRestMessage<Pasien> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public void ubahKelas(Pasien pasien, Kelas kelas) throws ServiceException {
        HttpEntity<Pasien> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{patientService}/pasien/{id}/kelas/{kelas}", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<RestMessage>() {}, patientService, pasien.getId(), kelas);

        RestMessage message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
    }
}
