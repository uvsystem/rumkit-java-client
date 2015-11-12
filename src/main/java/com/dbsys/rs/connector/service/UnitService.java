package com.dbsys.rs.connector.service;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.entity.Unit;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class UnitService extends  AbstractService {

	private static UnitService instance;
	
    private UnitService() {
        super();
    }
	
    private UnitService(String host) {
        super(host);
    }
    
    public static UnitService getInstance() {
    	if (instance == null)
    		instance = new UnitService();
    	return instance;
    }
    
    public static UnitService getInstance(String host) {
    	if (instance == null)
    		instance = new UnitService(host);
    	if (!instance.getHost().equals(host))
    		instance.setHost(host);
    	return instance;
    }
    
    public Unit simpan(Unit unit) throws ServiceException {
        HttpEntity<Unit> entity = new HttpEntity<>(unit, getHeaders());
        
        ResponseEntity<EntityRestMessage<Unit>> response;
        response = restTemplate.exchange("{accountService}/unit", HttpMethod.POST, entity, 
        		new ParameterizedTypeReference<EntityRestMessage<Unit>>() {}, 
        		accountService);
        
        EntityRestMessage<Unit> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }
    
    public Unit getById(Long id) throws ServiceException {
        HttpEntity<Unit> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<EntityRestMessage<Unit>> response;
        response = restTemplate.exchange("{accountService}/unit/{id}", HttpMethod.GET, entity, 
        		new ParameterizedTypeReference<EntityRestMessage<Unit>>() {}, 
        		accountService, id);
        
        EntityRestMessage<Unit> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }
    
    public List<Unit> getAll() throws ServiceException {
        HttpEntity<Unit> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<ListEntityRestMessage<Unit>> response;
        response = restTemplate.exchange("{accountService}/unit", HttpMethod.GET, entity, 
        		new ParameterizedTypeReference<ListEntityRestMessage<Unit>>() {}, 
        		accountService);
        
        ListEntityRestMessage<Unit> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getList();
    }
}
