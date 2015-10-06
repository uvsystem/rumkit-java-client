package com.dbsys.rs.client.test;

import com.dbsys.rs.client.service.ServiceException;
import com.dbsys.rs.client.service.TokenService;
import com.dbsys.rs.lib.Credential;
import com.dbsys.rs.lib.entity.Token;
import org.junit.Test;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class TokenServiceTest {

    private TokenService tokenService;

    public TokenServiceTest() {
        super();
        tokenService = new TokenService();
    }
    
    @Test
    public void testCreate() throws ServiceException {
        Credential credential = new Credential();
        credential.setUsername("admin");
        credential.setPassword("admin");
        
        Token token = tokenService.create(credential);
        
        System.out.println(token);
    }
}
