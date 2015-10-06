/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbsys.rs.client;

import com.dbsys.rs.lib.entity.Token;

/**
 *
 * @author Ronald
 */
public class TokenHolder {
    
    public static Token token;
        
    public static Token getToken() {
        return token;
    }
    
    public static String getKode() {
        return token.getKode();
    }
    
    public static String getUsername() {
        return token.getOperator().getUsername();
    }

    static String getNamaOperator() {
        return token.getOperator().getNama();
    }
}
