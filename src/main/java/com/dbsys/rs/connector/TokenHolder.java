package com.dbsys.rs.connector;

import com.dbsys.rs.client.entity.Operator;
import com.dbsys.rs.client.entity.Token;
import com.dbsys.rs.client.entity.Unit;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class TokenHolder {
    
    public static Token token;
        
    public static Token getToken() {
        return token;
    }
    
    public static String getKode() {
        return token.getKode();
    }
    
    public static Operator getOperator() {
    	return token.getOperator();
    }
    
    public static String getUsername() {
        return getOperator().getUsername();
    }

    public static String getNamaOperator() {
        return getOperator().getNama();
    }

    public static Unit getUnit() {
    	return getOperator().getUnit();
    }
    
    public static String getNamaUnit() {
        return getUnit().getNama();
    }
    
    public static Long getIdUnit() {
    	return getUnit().getId();
    }
}
