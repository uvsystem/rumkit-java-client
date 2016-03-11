package com.dbsys.rs.connector;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public abstract class AbstractService {
    
    protected final RestTemplate restTemplate;
    
    private String host;
    protected String service;

    protected AbstractService() {
        super();
        this.restTemplate = new RestTemplate();
        this.host = "http://222.124.150.12:8080";
    }

    protected AbstractService(String host) {
        super();
        this.restTemplate = new RestTemplate();
        this.host = host;
    }
    
    public String getHost() {
    	return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    protected HttpHeaders getHeaders() {
        String username = TokenHolder.getUsername();
        String password = TokenHolder.getKode();
        String auth = String.format("%s:%s", username, password);
        byte[] base64 = Base64.getEncoder().encode(auth.getBytes());

        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Basic %s", new String(base64)));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes);
        
        return headers;
    }
    
    protected String verifyString(String str) {
    	if (str == null || str.equals(""))
    		return "null";
    	return str;
    }
}
