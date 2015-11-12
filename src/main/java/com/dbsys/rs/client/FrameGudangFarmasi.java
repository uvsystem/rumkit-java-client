package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.BhpTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.BarangService;
import com.dbsys.rs.connector.service.StokService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class FrameGudangFarmasi extends javax.swing.JFrame {

    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    private final BarangService barangService = BarangService.getInstance(EventController.host);
    private final StokService stokService = StokService.getInstance(EventController.host);
    
    private Barang barang;

    /**
     * Creates new FrameFarmasi
     */
    public FrameGudangFarmasi() {
        initComponents();
        setSize(1280, 800);
        
        paneBarang.setVisible(false);
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
    }

    private void setDetailBhp(BahanHabisPakai bhp) {
        if (bhp == null) {
            bhp = new BahanHabisPakai();

            this.txtEksternalHarga.setText(null);
            this.txtEksternalTanggungan.setText(null);
            this.txtEksternalJumlah.setText(null);
        } else {
            this.txtEksternalHarga.setText(bhp.getHarga().toString());
            this.txtEksternalTanggungan.setText(bhp.getPenanggung().toString());
            this.txtEksternalJumlah.setText(bhp.getJumlah().toString());
        }
        
        this.txtEksternalKode.setText(bhp.getKode());
        this.txtEksternalNama.setText(bhp.getNama());
        this.txtEksternalSatuan.setText(bhp.getSatuan());

        this.txtEksternalStokJumlah.setText("0");
        this.txtEksternalStokTanggal.setText(DateUtil.getDate().toString());
        this.txtEksternalStokJam.setText(DateUtil.getTime().toString());
        
        tblEksternal.removeAll();
        
        this.barang = bhp;
    }

    private void setDetailObat(ObatFarmasi obat) {
        if (obat == null) {
            obat = new ObatFarmasi();

            this.txtInternalHarga.setText(null);
            this.txtInternalTanggungan.setText(null);
            this.txtInternalJumlah.setText(null);
        } else {
            this.txtInternalHarga.setText(obat.getHarga().toString());
            this.txtInternalTanggungan.setText(obat.getPenanggung().toString());
            this.txtInternalJumlah.setText(obat.getJumlah().toString());
        }
        
        this.txtInternalKode.setText(obat.getKode());
        this.txtInternalNama.setText(obat.getNama());
        this.txtInternalSatuan.setText(obat.getSatuan());

        this.txtInternalStokJumlah.setText("0");
        this.txtInternalStokTanggal.setText(DateUtil.getDate().toString());
        this.txtInternalStokJam.setText(DateUtil.getTime().toString());
        
        tblInternal.removeAll();
        
        this.barang = obat;
    }
    
    private void simpanStokMasuk(Long idBarang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
        stokService.simpan(idBarang, jumlah, tanggal, jam);
    }
    
    private void simpanStokKeluar(Long idBarang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
        stokService.simpan(idBarang, jumlah, tanggal, jam);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paneBarang = new javax.swing.JTabbedPane();
        pnlStokEksternal = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtEksternalKeyword = new javax.swing.JTextField();
        pnlEksternalDetail = new javax.swing.JPanel();
        txtEksternalKode = new javax.swing.JTextField();
        txtEksternalNama = new javax.swing.JTextField();
        txtEksternalHarga = new javax.swing.JTextField();
        txtEksternalTanggungan = new javax.swing.JTextField();
        txtEksternalSatuan = new javax.swing.JTextField();
        txtEksternalJumlah = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        pnlEksternalStok = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtEksternalStokTanggal = new javax.swing.JTextField();
        txtEksternalStokJam = new javax.swing.JTextField();
        txtEksternalStokJumlah = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEksternal = new javax.swing.JTable();
        btnEksternalStokMasuk = new javax.swing.JButton();
        btnEksternalStokReset = new javax.swing.JButton();
        btnEksternalStokKeluar = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        pnlStokInternal = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtInternalKeyword = new javax.swing.JTextField();
        pnlInternalDetail = new javax.swing.JPanel();
        txtInternalKode = new javax.swing.JTextField();
        txtInternalNama = new javax.swing.JTextField();
        txtInternalHarga = new javax.swing.JTextField();
        txtInternalTanggungan = new javax.swing.JTextField();
        txtInternalSatuan = new javax.swing.JTextField();
        txtInternalJumlah = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        pnlInternalStok = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtInternalStokTanggal = new javax.swing.JTextField();
        txtInternalStokJam = new javax.swing.JTextField();
        txtInternalStokJumlah = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblInternal = new javax.swing.JTable();
        btnInternalStokReset = new javax.swing.JButton();
        btnInternalStokKeluar = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        pnlStokKembali = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel31 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RUMAH SAKIT LIUN KENDAGE TAHUNA - FARMASI");
        setBounds(new java.awt.Rectangle(0, 0, 1280, 800));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        paneBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneBarangMouseClicked(evt);
            }
        });

        pnlStokEksternal.setLayout(null);

        jLabel6.setText("Kata Kunci");
        pnlStokEksternal.add(jLabel6);
        jLabel6.setBounds(20, 15, 90, 14);

        txtEksternalKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEksternalKeywordFocusLost(evt);
            }
        });
        pnlStokEksternal.add(txtEksternalKeyword);
        txtEksternalKeyword.setBounds(130, 15, 280, 25);

        pnlEksternalDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlEksternalDetail.setLayout(null);

        txtEksternalKode.setEditable(false);
        pnlEksternalDetail.add(txtEksternalKode);
        txtEksternalKode.setBounds(140, 30, 420, 25);

        txtEksternalNama.setEditable(false);
        pnlEksternalDetail.add(txtEksternalNama);
        txtEksternalNama.setBounds(140, 60, 420, 25);

        txtEksternalHarga.setEditable(false);
        txtEksternalHarga.setToolTipText("");
        pnlEksternalDetail.add(txtEksternalHarga);
        txtEksternalHarga.setBounds(140, 90, 420, 25);

        txtEksternalTanggungan.setEditable(false);
        txtEksternalTanggungan.setToolTipText("");
        pnlEksternalDetail.add(txtEksternalTanggungan);
        txtEksternalTanggungan.setBounds(140, 120, 420, 25);

        txtEksternalSatuan.setEditable(false);
        pnlEksternalDetail.add(txtEksternalSatuan);
        txtEksternalSatuan.setBounds(140, 180, 420, 25);

        txtEksternalJumlah.setEditable(false);
        pnlEksternalDetail.add(txtEksternalJumlah);
        txtEksternalJumlah.setBounds(140, 150, 420, 25);

        jLabel14.setText("Kode");
        pnlEksternalDetail.add(jLabel14);
        jLabel14.setBounds(30, 30, 90, 14);

        jLabel15.setText("Nama");
        pnlEksternalDetail.add(jLabel15);
        jLabel15.setBounds(30, 60, 90, 14);

        jLabel16.setText("Harga");
        pnlEksternalDetail.add(jLabel16);
        jLabel16.setBounds(30, 90, 90, 14);

        jLabel84.setText("Tanggungan");
        pnlEksternalDetail.add(jLabel84);
        jLabel84.setBounds(30, 120, 90, 14);

        jLabel17.setText("Jumlah");
        pnlEksternalDetail.add(jLabel17);
        jLabel17.setBounds(30, 150, 90, 14);

        jLabel18.setText("Satuan");
        pnlEksternalDetail.add(jLabel18);
        jLabel18.setBounds(30, 180, 90, 14);

        pnlStokEksternal.add(pnlEksternalDetail);
        pnlEksternalDetail.setBounds(20, 310, 590, 220);

        pnlEksternalStok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlEksternalStok.setLayout(null);

        jLabel19.setText("Tanggal");
        pnlEksternalStok.add(jLabel19);
        jLabel19.setBounds(30, 30, 90, 14);

        jLabel20.setText("Jam");
        pnlEksternalStok.add(jLabel20);
        jLabel20.setBounds(30, 60, 90, 14);

        jLabel21.setText("Jumlah");
        pnlEksternalStok.add(jLabel21);
        jLabel21.setBounds(30, 90, 90, 14);
        pnlEksternalStok.add(txtEksternalStokTanggal);
        txtEksternalStokTanggal.setBounds(140, 30, 420, 25);
        pnlEksternalStok.add(txtEksternalStokJam);
        txtEksternalStokJam.setBounds(140, 60, 420, 25);
        pnlEksternalStok.add(txtEksternalStokJumlah);
        txtEksternalStokJumlah.setBounds(140, 90, 420, 25);

        pnlStokEksternal.add(pnlEksternalStok);
        pnlEksternalStok.setBounds(630, 310, 590, 140);

        tblEksternal.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEksternal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEksternalMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEksternal);

        pnlStokEksternal.add(jScrollPane2);
        jScrollPane2.setBounds(20, 50, 1200, 240);

        btnEksternalStokMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/barangMasuk_Icon.png"))); // NOI18N
        btnEksternalStokMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokMasukActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokMasuk);
        btnEksternalStokMasuk.setBounds(870, 470, 100, 40);

        btnEksternalStokReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/BarangReset_Icon.png"))); // NOI18N
        btnEksternalStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokResetActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokReset);
        btnEksternalStokReset.setBounds(1130, 470, 90, 39);

        btnEksternalStokKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/barangKeluar_Icon.png"))); // NOI18N
        btnEksternalStokKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokKeluarActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokKeluar);
        btnEksternalStokKeluar.setBounds(1000, 470, 100, 39);

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/stockBarang_icon.png"))); // NOI18N
        pnlStokEksternal.add(jLabel35);
        jLabel35.setBounds(1110, 10, 110, 30);

        paneBarang.addTab("KELUAR / MASUK", pnlStokEksternal);

        pnlStokInternal.setLayout(null);

        jLabel22.setText("Kata Kunci");
        pnlStokInternal.add(jLabel22);
        jLabel22.setBounds(10, 10, 60, 14);

        txtInternalKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInternalKeywordFocusLost(evt);
            }
        });
        pnlStokInternal.add(txtInternalKeyword);
        txtInternalKeyword.setBounds(90, 10, 280, 20);

        pnlInternalDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInternalDetail.setLayout(null);

        txtInternalKode.setEditable(false);
        pnlInternalDetail.add(txtInternalKode);
        txtInternalKode.setBounds(160, 30, 170, 20);

        txtInternalNama.setEditable(false);
        pnlInternalDetail.add(txtInternalNama);
        txtInternalNama.setBounds(160, 60, 170, 20);

        txtInternalHarga.setEditable(false);
        txtInternalHarga.setToolTipText("");
        pnlInternalDetail.add(txtInternalHarga);
        txtInternalHarga.setBounds(160, 90, 170, 20);

        txtInternalTanggungan.setEditable(false);
        txtInternalTanggungan.setToolTipText("");
        pnlInternalDetail.add(txtInternalTanggungan);
        txtInternalTanggungan.setBounds(160, 120, 170, 20);

        txtInternalSatuan.setEditable(false);
        pnlInternalDetail.add(txtInternalSatuan);
        txtInternalSatuan.setBounds(160, 180, 170, 20);

        txtInternalJumlah.setEditable(false);
        pnlInternalDetail.add(txtInternalJumlah);
        txtInternalJumlah.setBounds(160, 150, 170, 20);

        jLabel23.setText("Kode");
        pnlInternalDetail.add(jLabel23);
        jLabel23.setBounds(30, 30, 24, 14);

        jLabel24.setText("Nama");
        pnlInternalDetail.add(jLabel24);
        jLabel24.setBounds(30, 60, 27, 14);

        jLabel25.setText("Harga");
        pnlInternalDetail.add(jLabel25);
        jLabel25.setBounds(30, 90, 29, 14);

        jLabel85.setText("Tanggungan");
        pnlInternalDetail.add(jLabel85);
        jLabel85.setBounds(30, 120, 60, 14);

        jLabel26.setText("Jumlah");
        pnlInternalDetail.add(jLabel26);
        jLabel26.setBounds(30, 150, 33, 14);

        jLabel27.setText("Satuan");
        pnlInternalDetail.add(jLabel27);
        jLabel27.setBounds(30, 180, 34, 14);

        pnlStokInternal.add(pnlInternalDetail);
        pnlInternalDetail.setBounds(10, 300, 360, 220);

        pnlInternalStok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInternalStok.setLayout(null);

        jLabel28.setText("Tanggal");
        pnlInternalStok.add(jLabel28);
        jLabel28.setBounds(30, 30, 38, 14);

        jLabel29.setText("Jam");
        pnlInternalStok.add(jLabel29);
        jLabel29.setBounds(30, 60, 19, 14);

        jLabel30.setText("Jumlah");
        pnlInternalStok.add(jLabel30);
        jLabel30.setBounds(30, 90, 33, 14);
        pnlInternalStok.add(txtInternalStokTanggal);
        txtInternalStokTanggal.setBounds(119, 30, 190, 20);
        pnlInternalStok.add(txtInternalStokJam);
        txtInternalStokJam.setBounds(119, 60, 190, 20);
        pnlInternalStok.add(txtInternalStokJumlah);
        txtInternalStokJumlah.setBounds(119, 90, 190, 20);

        pnlStokInternal.add(pnlInternalStok);
        pnlInternalStok.setBounds(380, 300, 350, 140);

        tblInternal.setModel(new javax.swing.table.DefaultTableModel(
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
        tblInternal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInternalMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblInternal);

        pnlStokInternal.add(jScrollPane3);
        jScrollPane3.setBounds(10, 50, 720, 240);

        btnInternalStokReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/BarangReset_Icon.png"))); // NOI18N
        btnInternalStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInternalStokResetActionPerformed(evt);
            }
        });
        pnlStokInternal.add(btnInternalStokReset);
        btnInternalStokReset.setBounds(650, 480, 80, 39);

        btnInternalStokKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/barangKeluar_Icon.png"))); // NOI18N
        btnInternalStokKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInternalStokKeluarActionPerformed(evt);
            }
        });
        pnlStokInternal.add(btnInternalStokKeluar);
        btnInternalStokKeluar.setBounds(520, 480, 90, 39);

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/stockBarang_icon.png"))); // NOI18N
        pnlStokInternal.add(jLabel36);
        jLabel36.setBounds(620, 10, 110, 30);

        paneBarang.addTab("KE UNIT", pnlStokInternal);
        paneBarang.addTab("KEMBALI DARI PASIEN", pnlStokKembali);

        getContentPane().add(paneBarang);
        paneBarang.setBounds(20, 180, 1240, 570);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel31.setText("LOGIN SEBAGAI:");
        jToolBar1.add(jLabel31);

        lblOperator.setText("jLabel32");
        jToolBar1.add(lblOperator);

        jLabel32.setText(" - ");
        jToolBar1.add(jLabel32);

        jLabel33.setText("UNIT: ");
        jToolBar1.add(jLabel33);

        lblUnit.setText("jLabel34");
        jToolBar1.add(lblUnit);
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

        getContentPane().add(jToolBar1);
        jToolBar1.setBounds(0, 770, 1280, 30);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Farmasi_Bg.jpg"))); // NOI18N
        getContentPane().add(background);
        background.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paneBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneBarangMouseClicked
        setDetailBhp(null);
        setDetailObat(null);
        
        JOptionPane.showMessageDialog(this, "Silahkan cari data menggunakan kode/nama barang");
    }//GEN-LAST:event_paneBarangMouseClicked

    private void txtEksternalKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEksternalKeywordFocusLost
        String keyword = txtEksternalKeyword.getText();
        
        if (keyword.equals(""))
            return;
        
        List<Barang> list = null;
        try {
            list = barangService.cari(keyword);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            // TODO set table model (barang)
            setDetailBhp(null);
        }
    }//GEN-LAST:event_txtEksternalKeywordFocusLost

    private void tblEksternalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEksternalMouseClicked
        Integer index = tblEksternal.getSelectedRow();
        
        BhpTableModel tableModel = (BhpTableModel)tblEksternal.getModel();
        BahanHabisPakai bhp = tableModel.getBhp(index);
        
        setDetailBhp(bhp);
    }//GEN-LAST:event_tblEksternalMouseClicked

    private void btnEksternalStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokMasukActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtEksternalStokTanggal.getText();
        String jam = txtEksternalStokJam.getText();
        String jumlah = txtEksternalStokJumlah.getText();
        
        try {
            simpanStokMasuk(barang.getId(), new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailBhp(null);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnEksternalStokMasukActionPerformed

    private void btnEksternalStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokKeluarActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtEksternalStokTanggal.getText();
        String jam = txtEksternalStokJam.getText();
        String jumlah = txtEksternalStokJumlah.getText();
        
        try {
            simpanStokKeluar(barang.getId(), new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailBhp(null);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnEksternalStokKeluarActionPerformed

    private void btnEksternalStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokResetActionPerformed
        setDetailBhp(null);
    }//GEN-LAST:event_btnEksternalStokResetActionPerformed

    private void txtInternalKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInternalKeywordFocusLost
        String keyword = txtInternalKeyword.getText();
        
        if (keyword.equals(""))
            return;
        
        List<Barang> list = null;
        try {
            list = barangService.cari(keyword);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            // TODO set table model
            setDetailObat(null);
        }
    }//GEN-LAST:event_txtInternalKeywordFocusLost

    private void tblInternalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInternalMouseClicked
        Integer index = tblInternal.getSelectedRow();
        
        ObatTableModel tableModel = (ObatTableModel)tblInternal.getModel();
        ObatFarmasi obat = tableModel.getObat(index);
        
        setDetailObat(obat);
    }//GEN-LAST:event_tblInternalMouseClicked

    private void btnInternalStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInternalStokKeluarActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtInternalStokTanggal.getText();
        String jam = txtInternalStokJam.getText();
        String jumlah = txtInternalStokJumlah.getText();
        
        try {
            simpanStokKeluar(barang.getId(), new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailObat(null);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnInternalStokKeluarActionPerformed

    private void btnInternalStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInternalStokResetActionPerformed
        setDetailObat(null);
    }//GEN-LAST:event_btnInternalStokResetActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameLogin().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JButton btnEksternalStokKeluar;
    private javax.swing.JButton btnEksternalStokMasuk;
    private javax.swing.JButton btnEksternalStokReset;
    private javax.swing.JButton btnInternalStokKeluar;
    private javax.swing.JButton btnInternalStokReset;
    private javax.swing.JButton btnLogout;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JTabbedPane paneBarang;
    private javax.swing.JPanel pnlEksternalDetail;
    private javax.swing.JPanel pnlEksternalStok;
    private javax.swing.JPanel pnlInternalDetail;
    private javax.swing.JPanel pnlInternalStok;
    private javax.swing.JPanel pnlStokEksternal;
    private javax.swing.JPanel pnlStokInternal;
    private javax.swing.JPanel pnlStokKembali;
    private javax.swing.JTable tblEksternal;
    private javax.swing.JTable tblInternal;
    private javax.swing.JTextField txtEksternalHarga;
    private javax.swing.JTextField txtEksternalJumlah;
    private javax.swing.JTextField txtEksternalKeyword;
    private javax.swing.JTextField txtEksternalKode;
    private javax.swing.JTextField txtEksternalNama;
    private javax.swing.JTextField txtEksternalSatuan;
    private javax.swing.JTextField txtEksternalStokJam;
    private javax.swing.JTextField txtEksternalStokJumlah;
    private javax.swing.JTextField txtEksternalStokTanggal;
    private javax.swing.JTextField txtEksternalTanggungan;
    private javax.swing.JTextField txtInternalHarga;
    private javax.swing.JTextField txtInternalJumlah;
    private javax.swing.JTextField txtInternalKeyword;
    private javax.swing.JTextField txtInternalKode;
    private javax.swing.JTextField txtInternalNama;
    private javax.swing.JTextField txtInternalSatuan;
    private javax.swing.JTextField txtInternalStokJam;
    private javax.swing.JTextField txtInternalStokJumlah;
    private javax.swing.JTextField txtInternalStokTanggal;
    private javax.swing.JTextField txtInternalTanggungan;
    // End of variables declaration//GEN-END:variables
}
