package com.dbsys.rs.connector;

import com.dbsys.rs.client.frame.Progress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class Interceptor {

    @Around("execution(public * com.dbsys.rs.connector.service.*.*(..) throws com.dbsys.rs.connector.ServiceException)")
    public void process(ProceedingJoinPoint jointPoint) {
        System.out.println("Hell");
        
        Progress progress = new Progress(null, false);
        progress.setVisible(true);
        
        try {
            jointPoint.proceed();
        } catch (Throwable ex) {
            Logger.getLogger(Interceptor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            progress.setVisible(false);
        }
    }
}
