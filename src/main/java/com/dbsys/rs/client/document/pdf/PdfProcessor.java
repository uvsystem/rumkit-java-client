package com.dbsys.rs.client.document.pdf;

import com.dbsys.rs.client.document.AbstractDocumentProcessor;
import com.dbsys.rs.client.document.AbstractDocumentView;
import com.dbsys.rs.client.document.DocumentException;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class PdfProcessor extends AbstractDocumentProcessor {

    @Override
    public void generate(AbstractDocumentView view, Map<String, Object> model, String url) throws DocumentException {
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
    public void print(AbstractDocumentView view, Map<String, Object> model) throws DocumentException {
        if (!(view instanceof AbstractPdfView))
            throw new DocumentException("Tipe yang dimasukan salah");

        AbstractPdfView pdfView = (AbstractPdfView) view;
        Document document = pdfView.newDocument();
        
        try {
            PdfWriter.getInstance(document, new PrintStream(new ByteArrayOutputStream()));
            process(pdfView, model, document);
        } catch (com.lowagie.text.DocumentException e) {
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
