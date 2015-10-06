package com.dbsys.rs.client.service;

import com.dbsys.rs.lib.Credential;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Token;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Bramwell Kasaedja
 */
public class TokenService extends AbstractService {
    private final RestTemplate restTemplate;
    private final String host = "http://192.168.0.106:8080/rumkit";
    
    public TokenService(){
        super();
        restTemplate = new RestTemplate();
    }
    
    public Token create(Credential credential) throws ServiceException  {
	EntityRestMessage<Token> message;
	message = restTemplate.postForObject("{host}/token", credential, EntityRestMessage.class, host);
                
        if (message.getTipe().equals(Type.ERROR)) {
            throw new ServiceException("Login Gagal");
        } else {
            return createToken((Map<String, Object>) message.getModel());
        }
    }
    
    public void lock(Token token) throws ServiceException {
        HttpEntity<Token> httpEntity = new HttpEntity<>(null);
        
        ResponseEntity<RestMessage> message;
        message = restTemplate.exchange("{host}/token/{kode}", HttpMethod.PUT, httpEntity, RestMessage.class, host);
        
        if (!message.getBody().getTipe().equals(Type.SUCCESS))
            throw new ServiceException(message.getBody().getMessage());
    }
}
