package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.document.AbstractDocumentView;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.RekapStokPdfView;
import com.dbsys.rs.client.document.pdf.RekapUnitPdfView;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.adapter.RekapStokBarangAdapter;
import com.dbsys.rs.connector.adapter.RekapUnitAdapter;
import com.dbsys.rs.connector.service.ReportService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.Unit;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class FrameRekap extends JFrame {

    private final ReportService reportService = ReportService.getInstance();
    
    private final JFrame frame;
    private final Class<?> cls;
    
    private final PdfProcessor pdfProcessor;
    private final Map<String, Object> model;
    private AbstractDocumentView documentView;
    
    /**
     * Creates new form FramePasienKeluar
     * 
     * @param frame
     * @param cls
     * 
     * @throws com.dbsys.rs.connector.ServiceException pasien atau frame null.
     */
    public FrameRekap(JFrame frame, Class<?> cls) throws ServiceException {
        initComponents();
        setSize(330, 220);
        
        if (frame == null)
            throw new ServiceException("Frame null");
        this.frame = frame;
        this.cls = cls;
        
        txtTanggalAwal.setText(DateUtil.getFirstDate().toString());
        txtTanggalAkhir.setText(DateUtil.getLastDate().toString());
        
        this.pdfProcessor = new PdfProcessor();
        this.model = new HashMap<>();
    }

    private void rekapUnit(Date awal, Date akhir) throws ServiceException, DocumentException {
        documentView = new RekapUnitPdfView();
        
        List<RekapUnitAdapter> list = reportService.rekapUnit(awal, akhir);
        model.put("list", list);
        model.put("awal", awal);
        model.put("akhir", akhir);

        pdfProcessor.generate(documentView, model, String.format("E://print//rekap-unit-%s.pdf", DateUtil.getTime().hashCode()));
    }

    private void rekapStok(Date awal, Date akhir) throws ServiceException, DocumentException {
        documentView = new RekapStokPdfView();

        List<RekapStokBarangAdapter> list = reportService.rekapStok(awal, akhir);
        model.put("list", list);
        model.put("awal", awal);
        model.put("akhir", akhir);

        pdfProcessor.generate(documentView, model, String.format("E://print//rekap-unit-%s.pdf", DateUtil.getTime().hashCode()));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtTanggalAwal = new javax.swing.JTextField();
        txtTanggalAkhir = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("REKAP");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel14.setText("Tanggal Akhir");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 100, 25));
        getContentPane().add(txtTanggalAwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 160, 25));
        getContentPane().add(txtTanggalAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 160, 25));

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, -1, 30));

        jLabel4.setText("Tanggal Awal");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, 25));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        String awalStr = txtTanggalAwal.getText();
        if (awalStr == null || awalStr.equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan masukan tanggal awal");
        }
        
        String akhirStr = txtTanggalAkhir.getText();
        if (akhirStr == null || akhirStr.equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan masukan tanggal akhir");
        }
        
        Date awal = DateUtil.getDate(awalStr);
        Date akhir = DateUtil.getDate(akhirStr);

        try {
            
            if (Unit.class.equals(cls)) {
                rekapUnit(awal, akhir);
            } else if (Stok.class.equals(cls)) {
                rekapStok(awal, akhir);
            }
            
            this.dispose();
            frame.setVisible(true);
        } catch (DocumentException |ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnOkActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtTanggalAkhir;
    private javax.swing.JTextField txtTanggalAwal;
    // End of variables declaration//GEN-END:variables
}
