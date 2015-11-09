package com.dbsys.rs.client.document;

import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public abstract class AbstractDocumentProcessor {

    public abstract void generate(AbstractDocumentView view, Map<String, Object> model, String url) throws DocumentException;
    
    public abstract void print(AbstractDocumentView view, Map<String, Object> model) throws DocumentException;
    
}
