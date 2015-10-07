package com.dbsys.rs.client.service;

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
 * @author Bramwell Kasaedja
 */
public class TokenService extends AbstractService {
    
    public TokenService(){
        super();
    }
    
    public Token create(Credential credential) throws ServiceException  {
        HttpEntity<Credential> entity = new HttpEntity<>(credential);
        
        ResponseEntity<EntityRestMessage<Token>> response;
        response = restTemplate.exchange("{host}/token", HttpMethod.POST, entity, 
                new ParameterizedTypeReference<EntityRestMessage<Token>>(){}, 
                host);

        EntityRestMessage<Token> message = response.getBody();
        if (message.getTipe().equals(Type.ERROR))
            throw new ServiceException("Login Gagal");
        return message.getModel();
    }
    
    public void lock(Token token) throws ServiceException {
        HttpEntity<Token> httpEntity = new HttpEntity<>(null);
        
        ResponseEntity<RestMessage> message;
        message = restTemplate.exchange("{host}/token/{kode}", HttpMethod.PUT, httpEntity, RestMessage.class, host);
        
        if (!message.getBody().getTipe().equals(Type.SUCCESS))
            throw new ServiceException(message.getBody().getMessage());
    }
}
