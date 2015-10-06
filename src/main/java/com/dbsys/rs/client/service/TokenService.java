/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbsys.rs.client.service;

import com.dbsys.rs.lib.Credential;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.RestMessage;
import com.dbsys.rs.lib.RestMessage.Type;
import com.dbsys.rs.lib.entity.Operator;
import com.dbsys.rs.lib.entity.Operator.Role;
import com.dbsys.rs.lib.entity.Token;
import com.dbsys.rs.lib.entity.Token.StatusToken;
import com.dbsys.rs.lib.entity.Unit;
import java.sql.Date;
import java.util.Map;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ronald
 */
public class TokenService {
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectCreator objectCreator ;
    private final String host = "http://192.168.0.106:8080/rumkit";
    
    public TokenService(){
        super();
        objectCreator = ObjectCreator.getInstance();
    }
    
    public Token create(Credential credential) throws ServiceException  {
		EntityRestMessage<Token> message;

		message = restTemplate.postForObject("{host}/token", credential, EntityRestMessage.class, host);
                
                if (message.getTipe().equals(Type.ERROR)) {
                    throw new ServiceException("Login Gagal");
                } else {
                    return  objectCreator.createToken((Map<String, Object>) message.getModel());
                }
	}
}

class ObjectCreator {

		private static ObjectCreator instance;
		
		private ObjectCreator() {
			super();
		}
		
		public static ObjectCreator getInstance() {
			if (instance== null)
				instance = new ObjectCreator();
			return instance;
		}
		
		@SuppressWarnings("unchecked")
		public  Token createToken(Map<String, Object> maps) {
			Token token = new Token();
			token.setKode((String)maps.get("kode"));
			token.setStatus(StatusToken.valueOf((String)maps.get("status")));
			//token.setTanggalBuat(new Date((Long)maps.get("tanggalBuat")));
                        System.out.println(maps.get("tanggalBuat"));
			//token.setTanggalExpire(new Date((Long)maps.get("tanggalExpire")));
                        System.out.println(maps.get("tanggalExpire"));
			token.setOperator(createOperator((Map<String, Object>)maps.get("operator")));

			return token;
		}
                
                public Operator createOperator(Map<String, Object>maps){
                    Operator operator = new Operator();
                    operator.setId(Long.valueOf((Integer)maps.get("id")));
                    operator.setNama((String)maps.get("nama"));
                    operator.setPassword((String)maps.get("password"));
                    operator.setRole(Role.valueOf((String)maps.get("role")));
                    operator.setUsername((String)maps.get("username"));
                    operator.setUnit(createUnit((Map<String, Object>)maps.get("unit")));
                    return operator;
                }

                public Unit createUnit(Map<String, Object> maps) {
                    Double b = (Double)maps.get("bobot");
                    
                    Unit unit = new Unit();
                    unit.setBobot(Float.valueOf((b.toString())));
                    unit.setId(Long.valueOf((Integer)maps.get("id")));
                    unit.setNama((String)maps.get("nama"));
                    return unit;
                }
}


