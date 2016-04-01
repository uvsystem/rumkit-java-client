package com.dbsys.rs.connector.service;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.adapter.RekapPegawaiAdapter;
import com.dbsys.rs.connector.adapter.RekapStokBarangAdapter;
import com.dbsys.rs.connector.adapter.RekapTagihanAdapter;
import com.dbsys.rs.connector.adapter.RekapUnitAdapter;
import com.dbsys.rs.client.ListEntityRestMessage;
import com.dbsys.rs.client.RestMessage.Type;
import com.dbsys.rs.client.entity.Pasien;

import java.sql.Date;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class ReportService extends AbstractService {
    
    private static ReportService instance;
    
    public ReportService() {
        super();
        service = String.format("%s/rumkit-report-service", getHost());
    }
    
    public ReportService(String host) {
        super(host);
        service = String.format("%s/rumkit-report-service", getHost());
    }

    public static ReportService getInstance() {
        if (instance == null)
            instance = new ReportService();
        return instance;
    }

    public static ReportService getInstance(String host) {
        if (instance == null)
            instance = new ReportService(host);
        if (!instance.getHost().equals(host))
            instance.setHost(host);
        return instance;
    }
    
    public List<RekapUnitAdapter> rekapUnit(Date awal, Date akhir) throws ServiceException {
        HttpEntity<RekapUnitAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<RekapUnitAdapter>> response;
        response = restTemplate.exchange("{service}/report/unit/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<RekapUnitAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<RekapUnitAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<RekapStokBarangAdapter> rekapStok(Date awal, Date akhir) throws ServiceException {
        HttpEntity<RekapStokBarangAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<RekapStokBarangAdapter>> response;
        response = restTemplate.exchange("{service}/report/stok/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<RekapStokBarangAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<RekapStokBarangAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<RekapTagihanAdapter> rekapTagihan(Pasien pasien) throws ServiceException {
        HttpEntity<RekapTagihanAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<RekapTagihanAdapter>> response;
        response = restTemplate.exchange("{service}/tagihan/pasien/{pasien}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<RekapTagihanAdapter>>() {}, service, pasien.getId());

        ListEntityRestMessage<RekapTagihanAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
    
    public List<RekapTagihanAdapter> rekapPemakaian(Date awal, Date akhir) throws ServiceException {
        HttpEntity<RekapTagihanAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<RekapTagihanAdapter>> response;
        response = restTemplate.exchange("{service}/tagihan/pemakaian/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<RekapTagihanAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<RekapTagihanAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<RekapPegawaiAdapter> rekapDokter(Date awal, Date akhir) throws ServiceException {
        HttpEntity<RekapPegawaiAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<RekapPegawaiAdapter>> response;
        response = restTemplate.exchange("{service}/pegawai/dokter/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<RekapPegawaiAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<RekapPegawaiAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

    public List<RekapPegawaiAdapter> rekapPerawat(Date awal, Date akhir) throws ServiceException {
        HttpEntity<RekapPegawaiAdapter> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<RekapPegawaiAdapter>> response;
        response = restTemplate.exchange("{service}/pegawai/perawat/{awal}/to/{akhir}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<RekapPegawaiAdapter>>() {}, service, awal, akhir);

        ListEntityRestMessage<RekapPegawaiAdapter> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
