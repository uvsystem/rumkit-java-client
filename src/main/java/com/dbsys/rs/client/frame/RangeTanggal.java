package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.document.DocumentView;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.RekapPasienPdfView;
import com.dbsys.rs.client.document.pdf.RekapPegawaiPdfView;
import com.dbsys.rs.client.document.pdf.RekapPemakaianPdfView;
import com.dbsys.rs.client.document.pdf.RekapStokPdfView;
import com.dbsys.rs.client.document.pdf.RekapUnitPdfView;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.adapter.RekapPegawaiAdapter;
import com.dbsys.rs.connector.adapter.RekapStokBarangAdapter;
import com.dbsys.rs.connector.adapter.RekapTagihanAdapter;
import com.dbsys.rs.connector.adapter.RekapUnitAdapter;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.ReportService;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.entity.Dokter;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pemakaian;
import com.dbsys.rs.client.entity.Perawat;
import com.dbsys.rs.client.entity.Stok;
import com.dbsys.rs.client.entity.Unit;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class RangeTanggal extends JFrame {

    private final ReportService reportService = ReportService.getInstance();
    
    private final JFrame frame;
    private final Class<?> cls;
    
    private final PdfProcessor pdfProcessor;
    private final Map<String, Object> model;
    private DocumentView documentView;
    
    /**
     * Creates new form FramePasienKeluar
     * 
     * @param frame
     * @param cls
     * 
     * @throws com.dbsys.rs.connector.ServiceException pasien atau frame null.
     */
    public RangeTanggal(JFrame frame, Class<?> cls) throws ServiceException {
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

        pdfProcessor.process(documentView, model, String.format("rekap-unit-%s.pdf", DateUtil.getTime().hashCode()));
    }

    private void rekapStok(Date awal, Date akhir) throws ServiceException, DocumentException {
        documentView = new RekapStokPdfView();

        List<RekapStokBarangAdapter> list = reportService.rekapStok(awal, akhir);
        model.put("list", list);
        model.put("awal", awal);
        model.put("akhir", akhir);

        pdfProcessor.process(documentView, model, String.format("rekap-unit-%s.pdf", DateUtil.getTime().hashCode()));
    }

    private void rekapPemakaian(Date awal, Date akhir) throws ServiceException, DocumentException {
        documentView = new RekapPemakaianPdfView();
        
        List<RekapTagihanAdapter> list = reportService.rekapPemakaian(awal, akhir);
        model.put("awal", awal);
        model.put("akhir", akhir);
        model.put("list", list);

        pdfProcessor.process(documentView, model, String.format("rekap-pemakaian-%s.pdf", DateUtil.getTime().hashCode()));
    }

    private void rekapDokter(Date awal, Date akhir) throws ServiceException, DocumentException {
        documentView = new RekapPegawaiPdfView();
        
        List<RekapPegawaiAdapter> list = reportService.rekapDokter(awal, akhir);
        model.put("awal", awal);
        model.put("akhir", akhir);
        model.put("list", list);

        pdfProcessor.process(documentView, model, String.format("rekap-dokter-%s.pdf", DateUtil.getTime().hashCode()));
    }

    private void rekapPerawat(Date awal, Date akhir) throws ServiceException, DocumentException {
        documentView = new RekapPegawaiPdfView();
        
        List<RekapPegawaiAdapter> list = reportService.rekapPerawat(awal, akhir);
        model.put("awal", awal);
        model.put("akhir", akhir);
        model.put("list", list);

        pdfProcessor.process(documentView, model, String.format("rekap-perawat-%s.pdf", DateUtil.getTime().hashCode()));
    }
    
    private void rekapPasien(Date awal, Date akhir) throws ServiceException, DocumentException {
        PasienService service = PasienService.getInstance();

        documentView = new RekapPasienPdfView();
        
        List<Pasien> list = service.get(awal, akhir);
        model.put("awal", awal);
        model.put("akhir", akhir);
        model.put("list", list);

        pdfProcessor.process(documentView, model, String.format("rekap-pasien-%s.pdf", DateUtil.getTime().hashCode()));
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
        btnOk = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtTanggalAwal = new datechooser.beans.DateChooserCombo();
        txtTanggalAkhir = new datechooser.beans.DateChooserCombo();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("REKAP");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel14.setText("Tanggal Akhir");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 100, 25));

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_cetak.png"))); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 80, 30));

        jLabel4.setText("Tanggal Awal");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, 25));
        getContentPane().add(txtTanggalAwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 160, 25));
        getContentPane().add(txtTanggalAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 160, 25));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        Calendar awalCalendar = txtTanggalAwal.getSelectedDate();
        if (awalCalendar == null) {
            JOptionPane.showMessageDialog(this, "Silahkan masukan tanggal awal");
            return;
        }
        
        Calendar akhirCalendar = txtTanggalAkhir.getSelectedDate();
        if (akhirCalendar == null) {
            JOptionPane.showMessageDialog(this, "Silahkan masukan tanggal akhir");
            return;
        }
        
        Date awal = new Date(awalCalendar.getTimeInMillis());
        Date akhir = new Date(akhirCalendar.getTimeInMillis());

        try {
            
            if (Unit.class.equals(cls)) {
                rekapUnit(awal, akhir);
            } else if (Stok.class.equals(cls)) {
                rekapStok(awal, akhir);
            } else if (Pemakaian.class.equals(cls)) {
                rekapPemakaian(awal, akhir);
            } else if (Dokter.class.equals(cls)) {
                rekapDokter(awal, akhir);
            } else if (Perawat.class.equals(cls)) {
                rekapPerawat(awal, akhir);
            } else if (Pasien.class.equals(cls)) {
                rekapPasien(awal, akhir);
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
    private datechooser.beans.DateChooserCombo txtTanggalAkhir;
    private datechooser.beans.DateChooserCombo txtTanggalAwal;
    // End of variables declaration//GEN-END:variables
}
