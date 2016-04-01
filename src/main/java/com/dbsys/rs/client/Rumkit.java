package com.dbsys.rs.client;

import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.PemakaianPdfView;
import com.dbsys.rs.client.document.pdf.PembayaranPdfView;
import com.dbsys.rs.client.frame.Utama;
import com.dbsys.rs.client.entity.Dokter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;

/**
 *
 * @author Bramwell Kasaedja
 */
public class Rumkit {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        setLookAndFeel();
        run();
        // testPdf();
        
        // Rumkit rumkit = new Rumkit();
        // rumkit.testCall();
    }
    
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    private static void run() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Utama frm = new Utama();
                frm.setVisible(true);
            }
        });
    }
    
    private static void testPdf() {
        PemakaianPdfView pdfView = new PemakaianPdfView();
        Map<String, Object> model = new HashMap<>();
        model.put("message", "Test Jadi");
        
        PdfProcessor pdfProcessor = new PdfProcessor();
        try {
            pdfProcessor.generate(pdfView, model, "E:\\print\\test.pdf");
        } catch (DocumentException ex) {
            System.out.println(ex);
        }
    }
    
    private static void littleTest() {
        System.out.println(Dokter.class.getSimpleName().equals("Dokter"));
    }
    
    public void testCall() {
        PembayaranPdfView pdfView = new PembayaranPdfView();
        System.out.println(pdfView.getLocation());
    }
}
