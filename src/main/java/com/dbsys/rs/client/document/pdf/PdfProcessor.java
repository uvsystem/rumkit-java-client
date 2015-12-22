package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.client.document.DocumentProcessor;
import com.dbsys.rs.client.document.DocumentView;
import com.dbsys.rs.client.document.DocumentException;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class PdfProcessor implements DocumentProcessor {

    @Override
    public void process(DocumentView view, Map<String, Object> model, String url) throws DocumentException {
        String newUrl = String.format("%s//%s", view.getLocation(), url);

        generate(view, model, newUrl);
        print(newUrl);
    }

    @Override
    public void generate(DocumentView view, Map<String, Object> model, String url) throws DocumentException {
        if (!(view instanceof AbstractPdfView))
            throw new DocumentException("Tipe yang dimasukan salah");

        AbstractPdfView pdfView = (AbstractPdfView) view;
        Document document = pdfView.newDocument();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(url));
            process(pdfView, model, document);
        } catch (com.lowagie.text.DocumentException | FileNotFoundException e) {
            throw new DocumentException(e.getMessage());
        }
    }

    @Override
    public void print(String url) throws DocumentException {
        try {
            File pdfFile = new File(url);

            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                  throw new DocumentException("AWT tidak didukung");
                }
            } else {
                throw new DocumentException("File tidak ditemukan");
            }
	  } catch (IOException | DocumentException e) {
              throw new DocumentException(e.getMessage());
	  }
    }
    
    private void process(AbstractPdfView pdfView, Map<String, Object> model, Document document) throws DocumentException {
        try {
            document.open();
            pdfView.create(model, document);
            document.close();
        } catch (com.lowagie.text.DocumentException e) {
            throw new DocumentException(e.getMessage());
        }
    }
}
