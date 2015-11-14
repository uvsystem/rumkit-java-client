package com.dbsys.rs.connector.service;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Operator;

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
    }
	
    private OperatorService(String host) {
        super(host);
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
        response = restTemplate.exchange("{accountService}/operator", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Operator>>() {}, 
                accountService);

        EntityRestMessage<Operator> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }
    
    public void hapus(Long id) throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{accountService}/operator/{id}", HttpMethod.DELETE, entity, RestMessage.class, accountService, id);

        RestMessage message = response.getBody();
        if (!message.getTipe().equals(Type.SUCCESS))
            throw new ServiceException(message.getMessage());
    }
    
    public List<Operator> getAll() throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<ListEntityRestMessage<Operator>> response;
        response = restTemplate.exchange("{accountService}/operator", HttpMethod.GET, entity, 
        		new ParameterizedTypeReference<ListEntityRestMessage<Operator>>() {}, 
        		accountService);
        
        ListEntityRestMessage<Operator> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
