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
    
    public static final String defaultHost = "";
    protected String host;

    protected String accountService;
    protected String hrService;
    protected String inventoryService;
    protected String patientService;
    protected String paymentService;
    protected String treatmentService;
    protected String usageService;
    protected String serveService;

    protected AbstractService() {
        super();
        this.restTemplate = new RestTemplate();
        setHost("http://localhost:8080");
    }

    protected AbstractService(String host) {
        super();
        this.restTemplate = new RestTemplate();
        setHost(host);
    }
    
    public String getHost() {
    	return host;
    }
    
    public void setHost(String host) {
        this.host = host;
        this.accountService = String.format("%s/rumkit-account-service", host);
        this.hrService = String.format("%s/rumkit-hr-service", host);
        this.inventoryService = String.format("%s/rumkit-inventory-service", host);
        this.patientService = String.format("%s/rumkit-patient-service", host);
        this.paymentService = String.format("%s/rumkit-payment-service", host);
        this.treatmentService = String.format("%s/rumkit-treatment-service", host);
        this.usageService = String.format("%s/rumkit-usage-service", host);
        this.serveService = String.format("%s/rumkit-serve-service", host);
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
