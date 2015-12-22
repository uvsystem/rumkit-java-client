package com.dbsys.rs.client.document;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public interface DocumentView {

    Document create(Map<String, Object> model, Document doc) throws DocumentException;

    String getLocation();
    
}
