/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.BhpTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.BhpService;
import com.dbsys.rs.connector.service.PemakaianBhpService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Acer One 10
 */
public class FrameTambahObject extends JFrame {

    private final JFrame frame;
    private final Class<?> cls;
    
    private final BhpService bhpService = BhpService.getInstance(EventController.host);
    private final PemakaianBhpService pemakaianBhpService = PemakaianBhpService.getInstance(EventController.host);

    private final Pasien pasien;
    private PemakaianBhp pemakaianBhp;
    private BahanHabisPakai bhp;
    
    /**
     * Creates new form FrameTambahObject
     * @param frame
     * @param cls
     * @param pasien
     */
    public FrameTambahObject(JFrame frame, Class<?> cls, Pasien pasien) {
        initComponents();
        this.frame = frame;
        this.cls = cls;
        this.pasien = pasien;
        
        txtPemakaianTanggal.setText(DateUtil.getDate().toString());
    }

    FrameTambahObject(JFrame frame, Class<?> cls, Pasien pasien, PemakaianBhp pemakaianBhp) {
        this(frame, cls, pasien);
        this.pemakaianBhp = pemakaianBhp;

        this.bhp = pemakaianBhp.getBahanHabisPakai();
        txtPemakaianBarang.setText(bhp.getNama());
        txtPemakaianBiayaTambahan.setText(pemakaianBhp.getBiayaTambahan().toString());
        txtPemakaianJumlah.setText(pemakaianBhp.getJumlah().toString());
        txtPemakaianTanggal.setText(pemakaianBhp.getTanggal().toString());
        txtPemakaianKeterangan.setText(pemakaianBhp.getKeterangan());
    }
    
    private void cariBhp(String keyword) throws ServiceException {
        List<BahanHabisPakai> list = bhpService.cari(keyword);
        
        BhpTableModel tableModel = new BhpTableModel(list);
        tblCari.setModel(tableModel);
    }
    
    private BahanHabisPakai getBhp(int index) {
        BhpTableModel tableModel = (BhpTableModel)tblCari.getModel();
        return tableModel.getBhp(index);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCari = new javax.swing.JTable();
        pnlPemakaianBhp = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPemakaianBarang = new javax.swing.JTextField();
        txtPemakaianJumlah = new javax.swing.JTextField();
        txtPemakaianBiayaTambahan = new javax.swing.JTextField();
        txtPemakaianKeterangan = new javax.swing.JTextField();
        txtPemakaianTanggal = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnSimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PENAMBAHAN");
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setText("Kata Kunci");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 59, 50, 14);
        getContentPane().add(txtKeyword);
        txtKeyword.setBounds(80, 56, 209, 20);

        btnCari.setText("OK");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        getContentPane().add(btnCari);
        btnCari.setBounds(295, 55, 47, 23);

        tblCari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCariMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCari);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 89, 452, 154);

        pnlPemakaianBhp.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Pemakaian"));
        pnlPemakaianBhp.setLayout(null);

        jLabel2.setText("Nama Barang");
        pnlPemakaianBhp.add(jLabel2);
        jLabel2.setBounds(20, 40, 70, 14);

        jLabel3.setText("Jumlah");
        pnlPemakaianBhp.add(jLabel3);
        jLabel3.setBounds(20, 80, 34, 14);

        jLabel4.setText("Biaya Tambahan");
        pnlPemakaianBhp.add(jLabel4);
        jLabel4.setBounds(20, 120, 90, 14);

        jLabel5.setText("Keterangan");
        pnlPemakaianBhp.add(jLabel5);
        jLabel5.setBounds(20, 160, 70, 14);

        jLabel6.setText("Tanggal");
        pnlPemakaianBhp.add(jLabel6);
        jLabel6.setBounds(20, 200, 60, 14);
        pnlPemakaianBhp.add(txtPemakaianBarang);
        txtPemakaianBarang.setBounds(160, 30, 270, 20);
        pnlPemakaianBhp.add(txtPemakaianJumlah);
        txtPemakaianJumlah.setBounds(160, 70, 270, 20);
        pnlPemakaianBhp.add(txtPemakaianBiayaTambahan);
        txtPemakaianBiayaTambahan.setBounds(160, 110, 270, 20);
        pnlPemakaianBhp.add(txtPemakaianKeterangan);
        txtPemakaianKeterangan.setBounds(160, 150, 270, 20);
        pnlPemakaianBhp.add(txtPemakaianTanggal);
        txtPemakaianTanggal.setBounds(160, 190, 270, 20);
        pnlPemakaianBhp.add(jSeparator1);
        jSeparator1.setBounds(-10, 250, 0, 2);

        getContentPane().add(pnlPemakaianBhp);
        pnlPemakaianBhp.setBounds(20, 260, 450, 240);

        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(btnSimpan);
        btnSimpan.setBounds(390, 510, 71, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String keyword = txtKeyword.getText();
        if (keyword.equals(""))
            return;

        try {
            if (cls.equals(BahanHabisPakai.class)) {
                cariBhp(keyword);
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void tblCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCariMouseClicked
        int index = tblCari.getSelectedRow();
        
        if (cls.equals(BahanHabisPakai.class)) {
            bhp = getBhp(index);
            txtPemakaianBarang.setText(bhp.getNama());
        }
    }//GEN-LAST:event_tblCariMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (pemakaianBhp == null)
            pemakaianBhp = new PemakaianBhp();
        
        String biayaTambahan = txtPemakaianBiayaTambahan.getText();
        String jumlah = txtPemakaianJumlah.getText();
        String tanggal = txtPemakaianTanggal.getText();
        
        pemakaianBhp.setBahanHabisPakai(bhp);
        pemakaianBhp.setBiayaTambahan(Long.valueOf(biayaTambahan));
        pemakaianBhp.setJumlah(Integer.valueOf(jumlah));
        pemakaianBhp.setKeterangan(txtPemakaianKeterangan.getText());
        pemakaianBhp.setTanggal(DateUtil.getDate(tanggal));
        pemakaianBhp.setPasien(pasien);
        pemakaianBhp.setUnit(TokenHolder.getToken().getOperator().getUnit());
        
        try {
            pemakaianBhpService.simpan(pemakaianBhp);
            this.dispose();
            
            if (cls.equals(BahanHabisPakai.class)) {
                ((FramePoliklinik)frame).reloadTableBhp();
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel pnlPemakaianBhp;
    private javax.swing.JTable tblCari;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtPemakaianBarang;
    private javax.swing.JTextField txtPemakaianBiayaTambahan;
    private javax.swing.JTextField txtPemakaianJumlah;
    private javax.swing.JTextField txtPemakaianKeterangan;
    private javax.swing.JTextField txtPemakaianTanggal;
    // End of variables declaration//GEN-END:variables
}
