package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.UnitFrame;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.KartuPasienPdfView;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PendudukService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.Kelas;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pasien.Pendaftaran;
import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.Penduduk;
import com.dbsys.rs.client.entity.Tindakan;
import com.dbsys.rs.client.entity.Unit;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.TindakanService;

import java.awt.Color;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public final class LoketPendaftaran extends javax.swing.JFrame implements UnitFrame {

    private final PendudukService pendudukService = PendudukService.getInstance();
    private final PasienService pasienService = PasienService.getInstance();
    private final TokenService tokenService= TokenService.getInstance();

    private Penduduk penduduk;
    private Unit tujuan;
    private Pasien pasien;
    
    /**
     * Creates new form Pendaftaran
     */
    public LoketPendaftaran() {
        initComponents();
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        
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
        txtPendudukUmur.setText(null);
        
        txtPasienNomor.setText(Pasien.createKode());
        txtPasienTujuan.setText(null);
        cbPasienKelas.setSelectedIndex(0);
        cbPasienTanggungan.setSelectedIndex(0);

        Calendar now = Calendar.getInstance();
        now.setTime(DateUtil.getDate());
        txtPasienTanggalMasuk.setSelectedDate(now);
    }
    
    private void tambahTagihanKarcis(Pasien pasien) throws ServiceException {
        final PelayananService pelayananService = PelayananService.getInstance();
        final TindakanService tindakanService = TindakanService.getInstance();
        
        Tindakan tindakan = tindakanService.get("Karcis Rawat Jalan", Kelas.NONE);
        
        Pelayanan pelayanan = new Pelayanan();
        pelayanan.setTindakan(tindakan);
        pelayanan.setBiayaTambahan(0L);
        pelayanan.setJumlah(1);
        pelayanan.setKeterangan(null);
        pelayanan.setPasien(pasien);
        pelayanan.setUnit(TokenHolder.getUnit());
        pelayanan.setTanggal(DateUtil.getDate());
        
        pelayananService.simpan(pelayanan);
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
        jLabel9 = new javax.swing.JLabel();
        txtPendudukKode = new javax.swing.JTextField();
        txtPendudukNik = new javax.swing.JTextField();
        txtPendudukNama = new javax.swing.JTextField();
        cbPendudukKelamin = new javax.swing.JComboBox();
        txtPendudukUmur = new javax.swing.JTextField();
        btnPendudukUpdate = new javax.swing.JButton();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        btnPendudukTambah = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
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
        txtPasienTujuan = new javax.swing.JTextField();
        cbPasienKelas = new javax.swing.JComboBox();
        cbPasienTanggungan = new javax.swing.JComboBox();
        btnPasienTambah = new javax.swing.JButton();
        txtPasienTanggalMasuk = new datechooser.beans.DateChooserCombo();
        btnCetakKartu = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        btnUbahPasien = new javax.swing.JButton();
        btnCetakKartuTagihan = new javax.swing.JButton();
        btnCetakPasien = new javax.swing.JButton();
        Image = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Rumah Sakit Liun Kendage Tahuna");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlDetail.setBackground(Utama.colorTransparentPanel);
        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA REKAM MEDIK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlDetail.setBackground(new Color(0,0,0,20));
        pnlDetail.setLayout(null);

        jLabel4.setText("NO. MEDREK");
        pnlDetail.add(jLabel4);
        jLabel4.setBounds(20, 30, 100, 25);

        jLabel5.setText("NO. JAMINAN");
        pnlDetail.add(jLabel5);
        jLabel5.setBounds(20, 60, 100, 25);

        jLabel6.setText("NAMA");
        pnlDetail.add(jLabel6);
        jLabel6.setBounds(20, 90, 100, 25);

        jLabel7.setText("KELAMIN");
        pnlDetail.add(jLabel7);
        jLabel7.setBounds(20, 120, 100, 25);

        jLabel9.setText("UMUR");
        pnlDetail.add(jLabel9);
        jLabel9.setBounds(20, 150, 100, 25);

        txtPendudukKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukKode.setEnabled(false);
        pnlDetail.add(txtPendudukKode);
        txtPendudukKode.setBounds(130, 30, 250, 25);

        txtPendudukNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukNik.setEnabled(false);
        pnlDetail.add(txtPendudukNik);
        txtPendudukNik.setBounds(130, 60, 250, 25);

        txtPendudukNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukNama.setEnabled(false);
        pnlDetail.add(txtPendudukNama);
        txtPendudukNama.setBounds(130, 90, 250, 25);

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        cbPendudukKelamin.setBorder(null);
        cbPendudukKelamin.setEnabled(false);
        pnlDetail.add(cbPendudukKelamin);
        cbPendudukKelamin.setBounds(130, 120, 250, 25);

        txtPendudukUmur.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukUmur.setEnabled(false);
        pnlDetail.add(txtPendudukUmur);
        txtPendudukUmur.setBounds(130, 150, 250, 25);

        btnPendudukUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnPendudukUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukUpdateActionPerformed(evt);
            }
        });
        pnlDetail.add(btnPendudukUpdate);
        btnPendudukUpdate.setBounds(300, 180, 80, 30);
        pnlDetail.add(dateChooserCombo1);
        dateChooserCombo1.setBounds(70, 380, 155, 20);

        btnPendudukTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_tambah.png"))); // NOI18N
        btnPendudukTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukTambahActionPerformed(evt);
            }
        });
        pnlDetail.add(btnPendudukTambah);
        btnPendudukTambah.setBounds(210, 180, 80, 30);

        getContentPane().add(pnlDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 160, 400, 220));

        jScrollPane1.setBackground(Utama.colorTransparentPanel);

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 830, 520));

        pnlCari.setBackground(Utama.colorTransparentPanel);
        pnlCari.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PENCARIAN REKAM MEDIK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlCari.setLayout(null);
        pnlCari.setBackground(new Color(0, 0, 0, 20));

        jLabel13.setText("NAMA PASIEN");
        pnlCari.add(jLabel13);
        jLabel13.setBounds(20, 20, 110, 25);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        txtKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeywordKeyPressed(evt);
            }
        });
        pnlCari.add(txtKeyword);
        txtKeyword.setBounds(140, 20, 670, 25);

        getContentPane().add(pnlCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 830, 60));

        pnlPendaftaran.setBackground(Utama.colorTransparentPanel);
        pnlPendaftaran.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPendaftaran.setLayout(null);
        pnlPendaftaran.setBackground(new Color(0,0,0,20));

        jLabel12.setText("UNIT TUJUAN");
        pnlPendaftaran.add(jLabel12);
        jLabel12.setBounds(20, 90, 100, 25);

        jLabel16.setText("NO. PASIEN");
        pnlPendaftaran.add(jLabel16);
        jLabel16.setBounds(20, 30, 100, 25);

        jLabel15.setText("TANGGAL MASUK");
        pnlPendaftaran.add(jLabel15);
        jLabel15.setBounds(20, 60, 100, 25);

        jLabel14.setText("TANGGUNGAN");
        pnlPendaftaran.add(jLabel14);
        jLabel14.setBounds(20, 150, 100, 25);

        jLabel17.setText("KELAS");
        pnlPendaftaran.add(jLabel17);
        jLabel17.setBounds(20, 120, 100, 25);

        txtPasienNomor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaran.add(txtPasienNomor);
        txtPasienNomor.setBounds(130, 30, 250, 25);

        txtPasienTujuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasienTujuanMouseClicked(evt);
            }
        });
        pnlPendaftaran.add(txtPasienTujuan);
        txtPasienTujuan.setBounds(130, 90, 250, 25);

        cbPasienKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NONE", "VVIP", "VIP", "I", "II", "III" }));
        pnlPendaftaran.add(cbPasienKelas);
        cbPasienKelas.setBounds(130, 120, 250, 25);

        cbPasienTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        cbPasienTanggungan.setBorder(null);
        pnlPendaftaran.add(cbPasienTanggungan);
        cbPasienTanggungan.setBounds(130, 150, 250, 25);

        btnPasienTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnPasienTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienTambahActionPerformed(evt);
            }
        });
        pnlPendaftaran.add(btnPasienTambah);
        btnPasienTambah.setBounds(210, 180, 80, 30);
        pnlPendaftaran.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(130, 60, 250, 25);

        btnCetakKartu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_cetak.png"))); // NOI18N
        btnCetakKartu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKartuActionPerformed(evt);
            }
        });
        pnlPendaftaran.add(btnCetakKartu);
        btnCetakKartu.setBounds(300, 180, 80, 30);

        getContentPane().add(pnlPendaftaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 390, 400, 220));

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 720, 80, 30));

        jToolBar1.setBackground(java.awt.SystemColor.activeCaptionBorder);
        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel1.setText("ANDA LOGIN SEBEGAI :");
        jToolBar1.add(jLabel1);

        lblOperator.setText("jLabel1");
        jToolBar1.add(lblOperator);

        jSeparator1.setMaximumSize(new java.awt.Dimension(20, 32767));
        jToolBar1.add(jSeparator1);

        btnLogout.setText("LOGOUT");
        btnLogout.setFocusable(false);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setMaximumSize(new java.awt.Dimension(80, 20));
        btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLogout);

        btnUbahPasien.setText("UBAH DATA PASIEN");
        btnUbahPasien.setFocusable(false);
        btnUbahPasien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUbahPasien.setMaximumSize(new java.awt.Dimension(120, 20));
        btnUbahPasien.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUbahPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahPasienActionPerformed(evt);
            }
        });
        jToolBar1.add(btnUbahPasien);

        btnCetakKartuTagihan.setText("CETAK KARTU TAGIHAN");
        btnCetakKartuTagihan.setFocusable(false);
        btnCetakKartuTagihan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCetakKartuTagihan.setMaximumSize(new java.awt.Dimension(123, 20));
        btnCetakKartuTagihan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCetakKartuTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKartuTagihanActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCetakKartuTagihan);

        btnCetakPasien.setText("DAFTAR PASIEN");
        btnCetakPasien.setFocusable(false);
        btnCetakPasien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCetakPasien.setMaximumSize(new java.awt.Dimension(120, 20));
        btnCetakPasien.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCetakPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPasienActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCetakPasien);

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 770, 1280, 30));

        Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_pendaftaran.png"))); // NOI18N
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

        Date hariIni = DateUtil.getDate();
        Date lahir = penduduk.getTanggalLahir();
        int umur = DateUtil.calculate(lahir, hariIni) / 365;
        txtPendudukUmur.setText(Integer.toString(umur));
        
        // Data Pasien
        txtPasienNomor.setText(Pasien.createKode());

        Calendar now = Calendar.getInstance();
        now.setTime(DateUtil.getDate());
        txtPasienTanggalMasuk.setSelectedDate(now);
    }//GEN-LAST:event_tblPendudukMouseClicked

    private void btnPendudukTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukTambahActionPerformed
        pasien = null;

        TambahPasien tambahPasien = new TambahPasien();
        tambahPasien.setVisible(true);
        
    }//GEN-LAST:event_btnPendudukTambahActionPerformed

    private void btnPasienTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienTambahActionPerformed
        String kode = txtPasienNomor.getText();
        String tanggungan = cbPasienTanggungan.getSelectedItem().toString();
        String kelas = cbPasienKelas.getSelectedItem().toString();

        Calendar calendar = txtPasienTanggalMasuk.getSelectedDate();
        long lTime = calendar.getTimeInMillis();

        try {
            if (tanggungan == null || tanggungan.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih tanggungan");
            if (kelas == null || kelas.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih kelas");
            if (tujuan == null)
                throw new ServiceException("Silahkan masukan unit tujuan");
            
            pasien = pasienService.daftar(penduduk, Penanggung.valueOf(tanggungan), new Date(lTime), kode, Pendaftaran.LOKET, Kelas.valueOf(kelas), tujuan);
            txtPasienNomor.setText(pasien.getKode());

            // Otomatis tambah tagihan karcis pasien rawat jalan
            tambahTagihanKarcis(pasien);

            JOptionPane.showMessageDialog(this, "Berhasil menyimpan data pasien.");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienTambahActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new Utama().setVisible(true);
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
        Pencarian frameCari = new Pencarian(this, Unit.class);
        frameCari.setVisible(true);
    }//GEN-LAST:event_txtPasienTujuanMouseClicked

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnPendudukTambah.requestFocus();
    }//GEN-LAST:event_txtKeywordKeyPressed

    private void btnUbahPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahPasienActionPerformed
        new DetailPasien().setVisible(true);
    }//GEN-LAST:event_btnUbahPasienActionPerformed

    private void btnCetakKartuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKartuActionPerformed
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Silahkan mengisi data pasien terlebih dahulu");
            return;
        }
        
        PdfProcessor pdfProcessor = new PdfProcessor();
        KartuPasienPdfView pdfView = new KartuPasienPdfView();
        
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("pasien", pasien);

            pdfProcessor.process(pdfView, model, String.format("pasien-%s.pdf", DateUtil.getTime().hashCode()));
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakKartuActionPerformed

    private void btnCetakKartuTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKartuTagihanActionPerformed
        String kodePasien = JOptionPane.showInputDialog(this, "Masukan Nomor Pasien");
        
        try {
            Pasien lPasien = pasienService.get(kodePasien);
            
            PdfProcessor pdfProcessor = new PdfProcessor();
            KartuPasienPdfView pdfView = new KartuPasienPdfView();

            Map<String, Object> model = new HashMap<>();
            model.put("pasien", lPasien);

            pdfProcessor.process(pdfView, model, String.format("pasien-%s.pdf", DateUtil.getTime().hashCode()));
        } catch (ServiceException | DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakKartuTagihanActionPerformed

    private void btnCetakPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPasienActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Pasien.class);
            frame.setPendaftaran(Pasien.Pendaftaran.LOKET);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakPasienActionPerformed

    private void btnPendudukUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukUpdateActionPerformed
        TambahPasien tambahPasien = new TambahPasien(penduduk);
        tambahPasien.setVisible(true);
    }//GEN-LAST:event_btnPendudukUpdateActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetForm();
    }//GEN-LAST:event_btnResetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Image;
    private javax.swing.JButton btnCetakKartu;
    private javax.swing.JButton btnCetakKartuTagihan;
    private javax.swing.JButton btnCetakPasien;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPasienTambah;
    private javax.swing.JButton btnPendudukTambah;
    private javax.swing.JButton btnPendudukUpdate;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUbahPasien;
    private javax.swing.JComboBox cbPasienKelas;
    private javax.swing.JComboBox cbPasienTanggungan;
    private javax.swing.JComboBox cbPendudukKelamin;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private datechooser.beans.DateChooserCombo txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTujuan;
    private javax.swing.JTextField txtPendudukKode;
    private javax.swing.JTextField txtPendudukNama;
    private javax.swing.JTextField txtPendudukNik;
    private javax.swing.JTextField txtPendudukUmur;
    // End of variables declaration//GEN-END:variables
}
