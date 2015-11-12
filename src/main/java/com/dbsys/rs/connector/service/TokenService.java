package com.dbsys.rs.connector.service;

import com.dbsys.rs.connector.AbstractService;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.lib.Credential;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Token;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class TokenService extends AbstractService {

	private static TokenService instance;
	
    private TokenService(){
        super();
    }
	
    private TokenService(String host){
        super(host);
    }
    
    public static TokenService getInstance() {
    	if (instance == null)
    		instance = new TokenService();
    	return instance;
    }
    
    public static TokenService getInstance(String host) {
    	if (instance == null)
    		instance = new TokenService(host);
    	if (!instance.getHost().equals(host))
    		instance.setHost(host);
    	return instance;
    }
    
    public Token create(Credential credential) throws ServiceException  {
        HttpEntity<Credential> entity = new HttpEntity<>(credential);
        
        ResponseEntity<EntityRestMessage<Token>> response;
        response = restTemplate.exchange("{accountService}/token", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Token>>(){}, 
                accountService);

        EntityRestMessage<Token> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException(message.getMessage());
        return message.getModel();
    }
    
    public void lock(String kode) throws ServiceException {
        ResponseEntity<RestMessage> message;
        message = restTemplate.exchange("{accountService}/token/{kode}", HttpMethod.PUT, HttpEntity.EMPTY, RestMessage.class, accountService, kode);
        
        if (!message.getBody().getTipe().equals(Type.SUCCESS))
            throw new ServiceException(message.getBody().getMessage());
    }
}
