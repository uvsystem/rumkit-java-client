package com.dbsys.rs.client.service;

import com.dbsys.rs.client.TokenHolder;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.Operator;
import com.dbsys.rs.lib.entity.Operator.Role;
import com.dbsys.rs.lib.entity.Token;
import com.dbsys.rs.lib.entity.Token.StatusToken;
import com.dbsys.rs.lib.entity.Unit;
import com.sun.xml.internal.messaging.saaj.util.Base64;
import java.util.Map;
import org.springframework.http.HttpHeaders;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public abstract class AbstractService {
    
    protected HttpHeaders getHeaders() {
        String username = TokenHolder.getUsername();
        String password = TokenHolder.getKode();
        byte[] base64 = Base64.encode(String.format("%s:%s", username, password).getBytes());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", String.format("Basic %s", new String(base64)));
        
        return headers;
    }

    protected Token createToken(Map<String, Object> maps) {
        Token token = new Token();
        token.setKode((String)maps.get("kode"));
        token.setStatus(StatusToken.valueOf((String)maps.get("status")));
        token.setTanggalBuat(DateUtil.getDate((String)maps.get("tanggalBuat")));
        token.setTanggalExpire(DateUtil.getDate((String)maps.get("tanggalExpire")));
        token.setOperator(createOperator((Map<String, Object>)maps.get("operator")));

        return token;
    }

    protected Operator createOperator(Map<String, Object>maps){
        Operator operator = new Operator();
        operator.setId(Long.valueOf((Integer)maps.get("id")));
        operator.setNama((String)maps.get("nama"));
        operator.setPassword((String)maps.get("password"));
        operator.setRole(Role.valueOf((String)maps.get("role")));
        operator.setUsername((String)maps.get("username"));
        operator.setUnit(createUnit((Map<String, Object>)maps.get("unit")));
        return operator;
    }

    protected Unit createUnit(Map<String, Object> maps) {
        Double b = (Double)maps.get("bobot");

        Unit unit = new Unit();
        unit.setBobot(Float.valueOf((b.toString())));
        unit.setId(Long.valueOf((Integer)maps.get("id")));
        unit.setNama((String)maps.get("nama"));
        return unit;
    }
}
