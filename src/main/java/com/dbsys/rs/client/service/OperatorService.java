package com.dbsys.rs.client.service;

import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Operator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class OperatorService extends AbstractService {

    public OperatorService() {
        super();
    }
    
    public Operator simpan(Operator operator) throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(operator, getHeaders());
        
        ResponseEntity<EntityRestMessage> response;
        response = restTemplate.exchange("{host}/operator", HttpMethod.POST, entity, EntityRestMessage.class, host);

        EntityRestMessage<Operator> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR)) {
            throw new ServiceException(message.getMessage());
        } else {
            return createOperator((Map<String, Object>) message.getModel());
        }
    }
    
    public void hapus(Long id) throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<RestMessage> response;
        response = restTemplate.exchange("{host}/operator/{id}", HttpMethod.DELETE, entity, RestMessage.class, host, id);

        RestMessage message = response.getBody();
        if (!message.getTipe().equals(Type.SUCCESS))
            throw new ServiceException(message.getMessage());
    }
    
    public List<Operator> getAll() throws ServiceException {
        HttpEntity<Operator> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<ListEntityRestMessage> response;
        response = restTemplate.exchange("{host}/operator", HttpMethod.GET, entity, ListEntityRestMessage.class, host);
        
        ListEntityRestMessage<Operator> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR)) {
            throw new ServiceException(message.getMessage());
        } else {
            List<Operator> list = new ArrayList<>();

            for (Object o : message.getList()) {
                Operator unit = createOperator((Map<String, Object>)o);
                list.add(unit);
            }
            
            return list;
        }
    }
}
