package com.dbsys.rs.client.service;

import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.entity.Unit;
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
public class UnitService extends  AbstractService {

    public UnitService() {
        super();
    }
    
    public Unit simpan(Unit unit) throws ServiceException {
        HttpEntity<Unit> entity = new HttpEntity<>(unit, getHeaders());
        
        ResponseEntity<EntityRestMessage> response;
        response = restTemplate.exchange("{host}/unit", HttpMethod.POST, entity, EntityRestMessage.class, host);
        
        EntityRestMessage<Unit> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR)) {
            throw new ServiceException(message.getMessage());
        } else {
            return createUnit((Map<String, Object>) message.getModel());
        }
    }
    
    public Unit getById(Long id) throws ServiceException {
        HttpEntity<Unit> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<EntityRestMessage> response;
        response = restTemplate.exchange("{host}/unit/{id}", HttpMethod.GET, entity, EntityRestMessage.class, host, id);
        
        EntityRestMessage<Unit> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR)) {
            throw new ServiceException(message.getMessage());
        } else {
            return createUnit((Map<String, Object>) message.getModel());
        }
    }
    
    public List<Unit> getAll() throws ServiceException {
        HttpEntity<Unit> entity = new HttpEntity<>(getHeaders());
        
        ResponseEntity<ListEntityRestMessage> response;
        response = restTemplate.exchange("{host}/unit", HttpMethod.GET, entity, ListEntityRestMessage.class, host);
        
        ListEntityRestMessage<Unit> message = response.getBody();
        if (message.getTipe().equals(RestMessage.Type.ERROR)) {
            throw new ServiceException(message.getMessage());
        } else {
            List<Unit> list = new ArrayList<>();

            for (Object o : message.getList()) {
                Unit unit = createUnit((Map<String, Object>)o);
                list.add(unit);
            }
            
            return list;
        }
    }
}
