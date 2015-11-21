package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.UnitFrame;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PendudukService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pasien.Pendaftaran;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Unit;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public final class FramePendaftaran extends javax.swing.JFrame implements UnitFrame {

    private final PendudukService pendudukService = PendudukService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final TokenService tokenService= TokenService.getInstance(EventController.host);

    private Penduduk penduduk;
    private Unit tujuan;
    
    /**
     * Creates new form Pendaftaran
     */
    public FramePendaftaran() {
        initComponents();
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        txtPasienTanggalMasuk.setText(DateUtil.getDate().toString());
        
        resetForm();
    }
    
    @Override
    public void setUnit(Unit unit) {
        tujuan = unit;
        
        txtPasienTujuan.setText(tujuan.getNama());
    }
    
    private void resetForm() {
        penduduk = null;
        tujuan = null;
        
        txtPendudukKode.setText(Penduduk.createKode());
        txtPendudukNik.setText(null);
        txtPendudukNama.setText(null);
        cbPendudukKelamin.setSelectedIndex(0);
        txtPendudukTanggalLahir.setText(null);
        txtPendudukDarah.setText(null);
        txtPendudukAgama.setText(null);
        txtPendudukTelepon.setText(null);
        
        txtPasienNomor.setText(null);
        txtPasienTanggalMasuk.setText(null);
        txtPasienTujuan.setText(null);
        cbPasienKelas.setSelectedIndex(0);
        cbPasienTanggungan.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDetail = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtPendudukKode = new javax.swing.JTextField();
        txtPendudukNik = new javax.swing.JTextField();
        txtPendudukNama = new javax.swing.JTextField();
        cbPendudukKelamin = new javax.swing.JComboBox();
        txtPendudukTanggalLahir = new javax.swing.JTextField();
        txtPendudukDarah = new javax.swing.JTextField();
        txtPendudukAgama = new javax.swing.JTextField();
        txtPendudukTelepon = new javax.swing.JTextField();
        btnPendudukSimpan = new javax.swing.JButton();
        btnPendudukClean = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        pnlCari = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        pnlPendaftaran = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtPasienNomor = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienTujuan = new javax.swing.JTextField();
        cbPasienKelas = new javax.swing.JComboBox();
        cbPasienTanggungan = new javax.swing.JComboBox();
        btnPasienTambah = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        Image = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Rumah Sakit Liun Kendage Tahuna");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA REKAM MEDIK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlDetail.setBackground(new Color(0,0,0,20));
        pnlDetail.setLayout(null);

        jLabel4.setText("NOMOR");
        pnlDetail.add(jLabel4);
        jLabel4.setBounds(20, 30, 140, 14);

        jLabel5.setText("NIK");
        pnlDetail.add(jLabel5);
        jLabel5.setBounds(20, 60, 140, 14);

        jLabel6.setText("NAMA");
        pnlDetail.add(jLabel6);
        jLabel6.setBounds(20, 90, 140, 14);

        jLabel7.setText("KELAMIN");
        pnlDetail.add(jLabel7);
        jLabel7.setBounds(20, 120, 140, 14);

        jLabel8.setText("TANGGAL LAHIR");
        pnlDetail.add(jLabel8);
        jLabel8.setBounds(20, 150, 140, 14);

        jLabel9.setText("GOL. DARAH");
        pnlDetail.add(jLabel9);
        jLabel9.setBounds(20, 180, 140, 14);

        jLabel10.setText("AGAMA");
        pnlDetail.add(jLabel10);
        jLabel10.setBounds(20, 210, 140, 14);

        jLabel11.setText("TELEPON");
        pnlDetail.add(jLabel11);
        jLabel11.setBounds(20, 240, 140, 14);

        txtPendudukKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPendudukKode);
        txtPendudukKode.setBounds(170, 30, 210, 25);

        txtPendudukNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPendudukNik);
        txtPendudukNik.setBounds(169, 60, 210, 25);

        txtPendudukNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPendudukNama);
        txtPendudukNama.setBounds(169, 90, 210, 25);

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        cbPendudukKelamin.setBorder(null);
        pnlDetail.add(cbPendudukKelamin);
        cbPendudukKelamin.setBounds(170, 120, 210, 25);

        txtPendudukTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPendudukTanggalLahir);
        txtPendudukTanggalLahir.setBounds(169, 150, 210, 25);

        txtPendudukDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPendudukDarah);
        txtPendudukDarah.setBounds(169, 180, 210, 25);

        txtPendudukAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPendudukAgama);
        txtPendudukAgama.setBounds(169, 210, 210, 25);

        txtPendudukTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPendudukTelepon);
        txtPendudukTelepon.setBounds(169, 240, 210, 25);

        btnPendudukSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPendudukSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukSimpanActionPerformed(evt);
            }
        });
        pnlDetail.add(btnPendudukSimpan);
        btnPendudukSimpan.setBounds(170, 270, 80, 30);

        btnPendudukClean.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnPendudukClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukCleanActionPerformed(evt);
            }
        });
        pnlDetail.add(btnPendudukClean);
        btnPendudukClean.setBounds(300, 270, 80, 30);

        getContentPane().add(pnlDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 210, 400, 310));

        tblPenduduk.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPenduduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPendudukMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPenduduk);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 830, 590));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("DAFTAR PASIEN / REKAM MEDIK");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 190, -1));

        pnlCari.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PENCARIAN REKAM MEDIK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlCari.setLayout(null);
        pnlCari.setBackground(new Color(0, 0, 0, 20));

        jLabel13.setText("Kata Kunci");
        pnlCari.add(jLabel13);
        jLabel13.setBounds(20, 25, 140, 25);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        pnlCari.add(txtKeyword);
        txtKeyword.setBounds(170, 25, 210, 25);

        getContentPane().add(pnlCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 140, 400, 60));

        pnlPendaftaran.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPendaftaran.setLayout(null);
        pnlPendaftaran.setBackground(new Color(0,0,0,20));

        jLabel12.setText("UNIT TUJUAN");
        pnlPendaftaran.add(jLabel12);
        jLabel12.setBounds(10, 90, 150, 25);

        jLabel16.setText("NOMOR PASIEN");
        pnlPendaftaran.add(jLabel16);
        jLabel16.setBounds(10, 30, 150, 25);

        jLabel15.setText("TANGGAL MASUK");
        pnlPendaftaran.add(jLabel15);
        jLabel15.setBounds(10, 60, 150, 25);

        jLabel14.setText("TANGGUNGAN");
        pnlPendaftaran.add(jLabel14);
        jLabel14.setBounds(10, 150, 150, 25);

        jLabel17.setText("KELAS");
        pnlPendaftaran.add(jLabel17);
        jLabel17.setBounds(10, 120, 150, 25);

        txtPasienNomor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaran.add(txtPasienNomor);
        txtPasienNomor.setBounds(170, 30, 210, 25);

        txtPasienTanggalMasuk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaran.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(170, 60, 210, 25);

        txtPasienTujuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasienTujuanMouseClicked(evt);
            }
        });
        pnlPendaftaran.add(txtPasienTujuan);
        txtPasienTujuan.setBounds(170, 90, 210, 25);

        cbPasienKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "VVIP", "VIP", "I", "II", "III" }));
        pnlPendaftaran.add(cbPasienKelas);
        cbPasienKelas.setBounds(170, 120, 210, 25);

        cbPasienTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        cbPasienTanggungan.setBorder(null);
        pnlPendaftaran.add(cbPasienTanggungan);
        cbPasienTanggungan.setBounds(170, 150, 210, 25);

        btnPasienTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPasienTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienTambahActionPerformed(evt);
            }
        });
        pnlPendaftaran.add(btnPasienTambah);
        btnPasienTambah.setBounds(300, 180, 80, 30);

        getContentPane().add(pnlPendaftaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 530, 400, 220));

        jToolBar1.setBackground(java.awt.SystemColor.activeCaptionBorder);
        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel1.setText("ANDA LOGIN SEBEGAI :");
        jToolBar1.add(jLabel1);

        lblOperator.setText("jLabel1");
        jToolBar1.add(lblOperator);
        jToolBar1.add(jSeparator1);

        btnLogout.setText("LOGOUT");
        btnLogout.setFocusable(false);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLogout);

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 760, 1280, 40));

        Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Pendaftaran_Bg.jpg"))); // NOI18N
        getContentPane().add(Image, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblPendudukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPendudukMouseClicked
        int index = tblPenduduk.getSelectedRow();
        
        PendudukTableModel tableModel = (PendudukTableModel) tblPenduduk.getModel();
        penduduk = tableModel.getPenduduk(index);
        
        txtPendudukKode.setText(penduduk.getKode());
        txtPendudukNik.setText(penduduk.getNik());
        txtPendudukNama.setText(penduduk.getNama());
        cbPendudukKelamin.setSelectedItem(penduduk.getKelamin().toString());
        txtPendudukTanggalLahir.setText(penduduk.getTanggalLahir().toString());
        txtPendudukDarah.setText(penduduk.getDarah());
        txtPendudukAgama.setText(penduduk.getAgama());
        txtPendudukTelepon.setText(penduduk.getTelepon());
        
        txtPasienNomor.setText(Pasien.createKode());
        txtPasienTanggalMasuk.setText(DateUtil.getDate().toString());
    }//GEN-LAST:event_tblPendudukMouseClicked

    private void btnPendudukCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukCleanActionPerformed
        resetForm();
    }//GEN-LAST:event_btnPendudukCleanActionPerformed

    private void btnPendudukSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukSimpanActionPerformed
        if (penduduk == null)
            penduduk = new Penduduk();

        String kelamin = cbPendudukKelamin.getSelectedItem().toString();
        String tanggalLahir = txtPendudukTanggalLahir.getText();
        
        penduduk.setKode(txtPendudukKode.getText());
        penduduk.setNik(txtPendudukNik.getText());
        penduduk.setNama(txtPendudukNama.getText());
        penduduk.setKelamin(Penduduk.Kelamin.valueOf(kelamin));
        penduduk.setTanggalLahir(DateUtil.getDate(tanggalLahir));
        penduduk.setDarah(txtPendudukDarah.getText());
        penduduk.setAgama(txtPendudukAgama.getText());
        penduduk.setTelepon(txtPendudukTelepon.getText());
        
        try {
            penduduk = pendudukService.simpan(penduduk);
            JOptionPane.showMessageDialog(this, "Berhasil menyimpan rekam medik. Silahkan lanjutkan proses pendaftaran pasien!");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPendudukSimpanActionPerformed

    private void btnPasienTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienTambahActionPerformed
        String kode = txtPasienNomor.getText();
        String tanggalMasuk = txtPasienTanggalMasuk.getText();
        String tanggungan = cbPasienTanggungan.getSelectedItem().toString();
        String kelas = cbPasienKelas.getSelectedItem().toString();
        
        try {
            if (tanggungan == null || tanggungan.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih tanggungan");
            if (kelas == null || kelas.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih kelas");
            if (tujuan == null)
                throw new ServiceException("Silahkan masukan unit tujuan");
            
            Pasien pasien = pasienService.daftar(penduduk, Penanggung.valueOf(tanggungan), DateUtil.getDate(tanggalMasuk), kode, Pendaftaran.LOKET, Kelas.valueOf(kelas), tujuan);
            JOptionPane.showMessageDialog(this, "Berhasil menyimpan data pasien.");
            
            txtPasienNomor.setText(pasien.getKode());
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienTambahActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameLogin().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void txtKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKeywordFocusLost
        String keyword = txtKeyword.getText();
        
        if (keyword.equals(""))
            return;
        
        try {
            List<Penduduk> list = pendudukService.cari(keyword);
            
            PendudukTableModel tableModel = new PendudukTableModel(list);
            tblPenduduk.setModel(tableModel);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtKeywordFocusLost

    private void txtPasienTujuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasienTujuanMouseClicked
        FrameCari frameCari = new FrameCari(this, Unit.class);
        frameCari.setVisible(true);
    }//GEN-LAST:event_txtPasienTujuanMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Image;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPasienTambah;
    private javax.swing.JButton btnPendudukClean;
    private javax.swing.JButton btnPendudukSimpan;
    private javax.swing.JComboBox cbPasienKelas;
    private javax.swing.JComboBox cbPasienTanggungan;
    private javax.swing.JComboBox cbPendudukKelamin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JPanel pnlCari;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlPendaftaran;
    private javax.swing.JTable tblPenduduk;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtPasienNomor;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTujuan;
    private javax.swing.JTextField txtPendudukAgama;
    private javax.swing.JTextField txtPendudukDarah;
    private javax.swing.JTextField txtPendudukKode;
    private javax.swing.JTextField txtPendudukNama;
    private javax.swing.JTextField txtPendudukNik;
    private javax.swing.JTextField txtPendudukTanggalLahir;
    private javax.swing.JTextField txtPendudukTelepon;
    // End of variables declaration//GEN-END:variables
}
