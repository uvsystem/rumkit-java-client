package com.dbsys.rs.client;

import com.dbsys.rs.connector.ServiceException;

/**
 *
 * @author Deddy Christoper Kakunsi
 * @param <T>
 */
public interface EventController<T> {
    
    String host = "http://192.168.43.223:8080";
    
    T getModel();
    
    void setModel(T t);
    
    void onSave() throws ServiceException;
    
    void onTableClick() throws ServiceException;
    
    void onCleanForm();
    
    void onLoad() throws ServiceException;
    
    void onDelete() throws ServiceException;
    
}
