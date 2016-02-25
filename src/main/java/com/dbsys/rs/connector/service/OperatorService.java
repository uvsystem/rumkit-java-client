package com.dbsys.rs.connector.service;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.client.EntityRestMessage;
import com.dbsys.rs.client.ListEntityRestMessage;
import com.dbsys.rs.client.RestMessage;
import com.dbsys.rs.client.RestMessage.Type;
import com.dbsys.rs.client.entity.Operator;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class OperatorService extends AbstractService {

    private static OperatorService instance;
	
    private OperatorService() {
        super();
        service = String.format("%s/rumkit-account-service", getHost());
    }
	
    private OperatorService(String host) {
        super(host);
        service = String.format("%s/rumkit-account-service", getHost());
    }
    
    public static OperatorService getInstance() {
    	if (instance == null)
            instance = new OperatorService();

        return instance;
    }
    
    public static OperatorService getInstance(String host) {
    	if (instance == null)
            instance = new OperatorService();

        if (!instance.getHost().equals(host))
            instance.setHost(host);

        return instance;
    }
    
    public Operator simpan(Operator operator) throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(operator, getHeaders());
        
        ResponseEntity<EntityRestMessage<Operator>> response;
        response = restTemplate.exchange("{service}/operator", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Operator>>() {}, 
                service);

        EntityRestMessage<Operator> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }
    
    public void hapus(Operator operator) throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{service}/operator/{id}", HttpMethod.DELETE, entity, RestMessage.class, service, operator.getId());

        RestMessage message = response.getBody();
        if (!message.getTipe().equals(Type.SUCCESS))
            throw new ServiceException(message.getMessage());
    }
    
    public List<Operator> getAll() throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<ListEntityRestMessage<Operator>> response;
        response = restTemplate.exchange("{service}/operator", HttpMethod.GET, entity, 
        		new ParameterizedTypeReference<ListEntityRestMessage<Operator>>() {}, 
        		service);
        
        ListEntityRestMessage<Operator> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
