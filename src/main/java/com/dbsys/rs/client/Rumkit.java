package com.dbsys.rs.client;

import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.ExceptionPdfView;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.frame.FrameUtama;
import com.dbsys.rs.lib.entity.Dokter;
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
    }
    
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    private static void run() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameUtama frm = new FrameUtama();
                frm.setVisible(true);
            }
        });
    }
    
    private static void testPdf() {
        ExceptionPdfView pdfView = new ExceptionPdfView();
        Map<String, Object> model = new HashMap<>();
        model.put("message", "Test Jadi");
        
        PdfProcessor pdfProcessor = new PdfProcessor();
        try {
            pdfProcessor.generate(pdfView, model, "E:\\test.pdf");
        } catch (DocumentException ex) {
            System.out.println(ex);
        }
    }
    
    private static void littleTest() {
        System.out.println(Dokter.class.getSimpleName().equals("Dokter"));
    }
}
