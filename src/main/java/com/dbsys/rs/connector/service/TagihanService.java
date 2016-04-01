package com.dbsys.rs.connector.service;

import java.sql.Date;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.client.ListEntityRestMessage;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.RestMessage.Type;
import com.dbsys.rs.client.entity.Tagihan;

public class TagihanService extends AbstractService {

    private static TagihanService instance;

    private TagihanService() {
        super();
        service = String.format("%s/rumkit-serve-service", getHost());
    }

    private TagihanService(String host) {
        super(host);
        service = String.format("%s/rumkit-serve-service", getHost());
    }

    public static TagihanService getInstance() {
        if (instance == null)
            instance = new TagihanService();
        return instance;
    }

    public static TagihanService getInstance(String host) {
        if (instance == null)
            instance = new TagihanService(host);
        if (!instance.getHost().equals(host))
            instance.setHost(host);
        return instance;
    }

    public List<Tagihan> get(Date awal, Date akhir, Penanggung penanggung) throws ServiceException {
        HttpEntity<Tagihan> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ListEntityRestMessage<Tagihan>> response;
        response = restTemplate.exchange("{service}/tagihan/awal/{awal}/akhir/{akhir}/penanggung/{penanggung}", HttpMethod.GET, entity, 
                new ParameterizedTypeReference<ListEntityRestMessage<Tagihan>>() {}, 
                service, awal, akhir, penanggung);

        ListEntityRestMessage<Tagihan> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }

}
