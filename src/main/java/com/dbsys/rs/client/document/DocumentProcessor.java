package com.dbsys.rs.client.document;

import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public interface DocumentProcessor {

    void process(DocumentView view, Map<String, Object> model, String url) throws DocumentException;

    void generate(DocumentView view, Map<String, Object> model, String url) throws DocumentException;
    
    void print(String url) throws DocumentException;
    
}
