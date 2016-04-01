package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.ComponentSelectionException;
import com.dbsys.rs.client.PasienTableFrame;
import com.dbsys.rs.client.TindakanTableFrame;
import com.dbsys.rs.client.UnitFrame;
import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.Kelas;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.KartuPasienPdfView;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.Penduduk;
import com.dbsys.rs.client.entity.Tindakan;
import com.dbsys.rs.client.entity.Unit;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PendudukService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.connector.service.TokenService;

import java.awt.Color;
import java.sql.Date;
import java.util.ArrayList;
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
public class Ugd extends javax.swing.JFrame implements TindakanTableFrame, PasienTableFrame, UnitFrame {

    private final PendudukService pendudukService = PendudukService.getInstance();
    private final PasienService pasienService = PasienService.getInstance();
    private final TokenService tokenService= TokenService.getInstance();
    private final PelayananService pelayananService = PelayananService.getInstance();
    private final TindakanService tindakanService = TindakanService.getInstance();

    private Penduduk penduduk;
    private Pasien pasien;
    private Pasien pasienValue;
    private Unit tujuan;
    
    /**
     * Creates new form Pendaftaran
     */
    public Ugd() {
        initComponents();
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
        
        showPendaftaran();
    }
    
    /**
     * Creates new form Pendaftaran
     * @param image
     */
    public Ugd(String image) {
        this();
        Image.setIcon(new javax.swing.ImageIcon(getClass().getResource(image)));
    }
    
    private void showPendaftaran() {
        pnlPendaftaranDetail.setVisible(true);
        pnlPendaftaranDetailPenduduk.setVisible(true);
        pnlPenduduk.setVisible(true);
        
        pnlTindakan.setVisible(false);
        pnlPasienDetail.setVisible(false);
        pnlKeluar.setVisible(false);
        
        pnlHome.setVisible(false);
        pnlHomeDetail.setVisible(false);
        
        txtPendudukKode.setText(Penduduk.createKode());
        txtPasienNomor.setText(Pasien.createKode());
        
        Calendar now = Calendar.getInstance();
        now.setTime(DateUtil.getDate());
        txtPasienTanggalMasuk.setSelectedDate(now);
    }
    
    @Override
    public void reloadTableTindakan() {
        loadTindakan(pasien);
    }

    private void loadTindakan(Pasien pasien) {
        if (pasien == null)
            return;

        List<Pelayanan> list = null;
        try {
            list = pelayananService.getByPasien(pasien);
        } catch (ServiceException ex) {
            list = new ArrayList<>();
        } finally {
            PelayananTableModel tableModel = new PelayananTableModel(list);
            tblTindakan.setModel(tableModel);
        }
    }
    
    private Pelayanan getPelayanan() throws ComponentSelectionException {
        int index = tblTindakan.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PelayananTableModel tableModel = (PelayananTableModel)tblTindakan.getModel();
        return tableModel.getPelayanan(index);
    }

    @Override
    public void reloadTablePasien() {
        List<Pasien> list = null;
        try {
            list = loadPasienPerawatan();
        } catch (ServiceException ex) {
            // JOptionPane.showMessageDialog(this, ex.getMessage());
            tblPasien.setModel(new PasienTableModel(list));
        } finally {
            loadDetailRuangan(list);
            
            pasien = null;
        }
    }
    
    private List<Pasien> loadPasienPerawatan() throws ServiceException {
        List<Pasien> list = pasienService.getByUnit(TokenHolder.getUnit());
        PasienTableModel tableModel = new PasienTableModel(list);
        tblPasien.setModel(tableModel);
        
        return list;
    }
    
    private void loadDetailRuangan(List<Pasien> list) {
        if (list == null)
            list = new ArrayList<>();
        
        txtJumlahPasien.setText(String.valueOf(list.size()));
        
        Integer jumlahVvip = 0;
        Integer jumlahVip = 0;
        Integer jumlahKelas1 = 0;
        Integer jumlahKelas2 = 0;
        Integer jumlahKelas3 = 0;
        
        for (Pasien p : list) {
            if (Kelas.VVIP.equals(p.getKelas())) {
                jumlahVvip++;
            } else if (Kelas.VIP.equals(p.getKelas())) {
                jumlahVip++;
            } else if (Kelas.I.equals(p.getKelas())) {
                jumlahKelas1++;
            } else if (Kelas.II.equals(p.getKelas())) {
                jumlahKelas2++;
            } else if (Kelas.III.equals(p.getKelas())) {
                jumlahKelas3++;
            }
        }
        
        txtJumlahPasienVvip.setText(jumlahVvip.toString());
        txtJumlahPasienVip.setText(jumlahVip.toString());
        txtJumlahPasienKelas1.setText(jumlahKelas1.toString());
        txtJumlahPasienKelas2.setText(jumlahKelas2.toString());
        txtJumlahPasienKelas3.setText(jumlahKelas3.toString());
    }

    @Override
    public void setUnit(Unit unit) {
        tujuan = unit;
        
        txtPasienTujuan.setText(tujuan.getNama());
    }
    
    private void tambahTagihanKarcis(Pasien pasien) throws ServiceException {
        Tindakan tindakan = tindakanService.get("Karcis Rawat Inap", Kelas.NONE);
        
        Pelayanan pelayanan = new Pelayanan();
        pelayanan.setTindakan(tindakan);
        pelayanan.setBiayaTambahan(-3750L); // Minus agar tetap 15.000
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

        pnlPenduduk = new javax.swing.JPanel();
        scrollPenduduk = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        pnlHome = new javax.swing.JPanel();
        scrollPasien = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        pnlMasuk = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPasienMasuk = new javax.swing.JTextField();
        btnPasienMasuk = new javax.swing.JButton();
        btnPasienKeluar = new javax.swing.JButton();
        pnlTindakan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        btnTindakanSimpan = new javax.swing.JButton();
        btnTindakanUpdate = new javax.swing.JButton();
        btnTindakanHapus = new javax.swing.JButton();
        pnlPendaftaranDetailPenduduk = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtPendudukKode = new javax.swing.JTextField();
        txtPendudukNik = new javax.swing.JTextField();
        txtPendudukNama = new javax.swing.JTextField();
        cbPendudukKelamin = new javax.swing.JComboBox();
        txtPendudukUmur = new javax.swing.JTextField();
        btnPendudukTambah = new javax.swing.JButton();
        btnPendudukUpdate = new javax.swing.JButton();
        pnlPendaftaranDetail = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtPasienNomor = new javax.swing.JTextField();
        txtPasienTujuan = new javax.swing.JTextField();
        cbPasienKelas = new javax.swing.JComboBox();
        cbPasienTanggungan = new javax.swing.JComboBox();
        btnPasienTambah = new javax.swing.JButton();
        txtPasienTanggalMasuk = new datechooser.beans.DateChooserCombo();
        btnCetakKartu = new javax.swing.JButton();
        pnlPasienDetail = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtPasienKode = new javax.swing.JTextField();
        txtPasienNik = new javax.swing.JTextField();
        txtPasienNama = new javax.swing.JTextField();
        txtPasienTanggalLahir = new javax.swing.JTextField();
        txtPasienDarah = new javax.swing.JTextField();
        txtPasienAgama = new javax.swing.JTextField();
        txtPasienTelepon = new javax.swing.JTextField();
        txtPasienKelamin = new javax.swing.JTextField();
        txtPasienStatus = new javax.swing.JTextField();
        txtPasienTanggalMasukDetail = new javax.swing.JTextField();
        txtPasienTanggungan = new javax.swing.JTextField();
        txtPasienKelas = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        pnlHomeDetail = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txtJumlahPasien = new javax.swing.JTextField();
        txtJumlahPasienKelas3 = new javax.swing.JTextField();
        txtJumlahPasienVvip = new javax.swing.JTextField();
        txtJumlahPasienVip = new javax.swing.JTextField();
        txtJumlahPasienKelas1 = new javax.swing.JTextField();
        txtJumlahPasienKelas2 = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        btnUbahPasien = new javax.swing.JButton();
        btnCetakPasien = new javax.swing.JButton();
        btnPendaftaran = new javax.swing.JButton();
        btnPasien = new javax.swing.JButton();
        btnRuangan = new javax.swing.JButton();
        pnlKeluar = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        cbPasienKeadaan = new javax.swing.JComboBox();
        btnPasienKeluar1 = new javax.swing.JButton();
        Image = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Rumah Sakit Liun Kendage Tahuna");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlPenduduk.setBackground(Utama.colorTransparentPanel);
        pnlPenduduk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPenduduk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollPenduduk.setBackground(Utama.colorTransparentPanel);

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
        scrollPenduduk.setViewportView(tblPenduduk);

        pnlPenduduk.add(scrollPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 770, 480));

        jLabel13.setText("NAMA PASIEN");
        pnlPenduduk.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

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
        pnlPenduduk.add(txtKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 660, 25));

        getContentPane().add(pnlPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 810, 560));

        pnlHome.setBackground(Utama.colorTransparentPanel);
        pnlHome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlHome.setLayout(null);

        scrollPasien.setBackground(Utama.colorTransparentPanel);

        tblPasien.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPasienMouseClicked(evt);
            }
        });
        scrollPasien.setViewportView(tblPasien);

        pnlHome.add(scrollPasien);
        scrollPasien.setBounds(20, 90, 770, 450);

        pnlMasuk.setBackground(Utama.colorTransparentPanel);
        pnlMasuk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN MASUK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlMasuk.setLayout(null);

        jLabel2.setText("NOMOR PASIEN");
        pnlMasuk.add(jLabel2);
        jLabel2.setBounds(20, 20, 90, 25);
        pnlMasuk.add(txtPasienMasuk);
        txtPasienMasuk.setBounds(110, 20, 450, 25);

        btnPasienMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_masuk.png"))); // NOI18N
        btnPasienMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienMasukActionPerformed(evt);
            }
        });
        pnlMasuk.add(btnPasienMasuk);
        btnPasienMasuk.setBounds(580, 17, 80, 30);

        btnPasienKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_keluar.png"))); // NOI18N
        btnPasienKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluarActionPerformed(evt);
            }
        });
        pnlMasuk.add(btnPasienKeluar);
        btnPasienKeluar.setBounds(670, 17, 80, 30);

        pnlHome.add(pnlMasuk);
        pnlMasuk.setBounds(20, 20, 770, 60);

        getContentPane().add(pnlHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 810, 560));

        pnlTindakan.setBackground(Utama.colorTransparentPanel);
        pnlTindakan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DAFTAR TINDAKAN YANG SUDAH DIBERIKAN", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlTindakan.setLayout(null);

        jScrollPane1.setBackground(Utama.colorTransparentPanel);

        tblTindakan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTindakanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTindakan);

        pnlTindakan.add(jScrollPane1);
        jScrollPane1.setBounds(20, 30, 670, 510);

        btnTindakanSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_tambah.png"))); // NOI18N
        btnTindakanSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanSimpanActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanSimpan);
        btnTindakanSimpan.setBounds(710, 30, 80, 30);

        btnTindakanUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnTindakanUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUpdateActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanUpdate);
        btnTindakanUpdate.setBounds(710, 70, 80, 30);

        btnTindakanHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanHapus);
        btnTindakanHapus.setBounds(710, 110, 80, 30);

        getContentPane().add(pnlTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 810, 560));

        pnlPendaftaranDetailPenduduk.setBackground(Utama.colorTransparentPanel);
        pnlPendaftaranDetailPenduduk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA REKAM MEDIK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPendaftaranDetailPenduduk.setBackground(new Color(0,0,0,20));
        pnlPendaftaranDetailPenduduk.setLayout(null);

        jLabel4.setText("NOMOR MEDREK");
        pnlPendaftaranDetailPenduduk.add(jLabel4);
        jLabel4.setBounds(20, 30, 100, 25);

        jLabel5.setText("NOMOR JAMINAN");
        pnlPendaftaranDetailPenduduk.add(jLabel5);
        jLabel5.setBounds(20, 60, 100, 25);

        jLabel6.setText("NAMA");
        pnlPendaftaranDetailPenduduk.add(jLabel6);
        jLabel6.setBounds(20, 90, 100, 25);

        jLabel7.setText("KELAMIN");
        pnlPendaftaranDetailPenduduk.add(jLabel7);
        jLabel7.setBounds(20, 120, 100, 25);

        jLabel11.setText("UMUR");
        pnlPendaftaranDetailPenduduk.add(jLabel11);
        jLabel11.setBounds(20, 150, 100, 25);

        txtPendudukKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukKode.setEnabled(false);
        pnlPendaftaranDetailPenduduk.add(txtPendudukKode);
        txtPendudukKode.setBounds(130, 30, 250, 25);

        txtPendudukNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukNik.setEnabled(false);
        pnlPendaftaranDetailPenduduk.add(txtPendudukNik);
        txtPendudukNik.setBounds(130, 60, 250, 25);

        txtPendudukNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukNama.setEnabled(false);
        pnlPendaftaranDetailPenduduk.add(txtPendudukNama);
        txtPendudukNama.setBounds(130, 90, 250, 25);

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        cbPendudukKelamin.setBorder(null);
        cbPendudukKelamin.setEnabled(false);
        pnlPendaftaranDetailPenduduk.add(cbPendudukKelamin);
        cbPendudukKelamin.setBounds(130, 120, 250, 25);

        txtPendudukUmur.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPendudukUmur.setEnabled(false);
        pnlPendaftaranDetailPenduduk.add(txtPendudukUmur);
        txtPendudukUmur.setBounds(130, 150, 250, 25);

        btnPendudukTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_tambah.png"))); // NOI18N
        btnPendudukTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukTambahActionPerformed(evt);
            }
        });
        pnlPendaftaranDetailPenduduk.add(btnPendudukTambah);
        btnPendudukTambah.setBounds(210, 180, 80, 30);

        btnPendudukUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnPendudukUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukUpdateActionPerformed(evt);
            }
        });
        pnlPendaftaranDetailPenduduk.add(btnPendudukUpdate);
        btnPendudukUpdate.setBounds(300, 180, 80, 30);

        getContentPane().add(pnlPendaftaranDetailPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 180, 400, 220));

        pnlPendaftaranDetail.setBackground(Utama.colorTransparentPanel);
        pnlPendaftaranDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPendaftaranDetail.setLayout(null);
        pnlPendaftaranDetail.setBackground(new Color(0,0,0,20));

        jLabel37.setText("TANGGAL MASUK");
        pnlPendaftaranDetail.add(jLabel37);
        jLabel37.setBounds(20, 60, 100, 25);

        jLabel38.setText("UNIT TUJUAN");
        pnlPendaftaranDetail.add(jLabel38);
        jLabel38.setBounds(20, 90, 100, 25);

        jLabel16.setText("NOMOR PASIEN");
        pnlPendaftaranDetail.add(jLabel16);
        jLabel16.setBounds(20, 30, 100, 25);

        jLabel15.setText("KELAS");
        pnlPendaftaranDetail.add(jLabel15);
        jLabel15.setBounds(20, 120, 100, 25);

        jLabel12.setText("TANGGUNGAN");
        pnlPendaftaranDetail.add(jLabel12);
        jLabel12.setBounds(20, 150, 100, 25);

        txtPasienNomor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetail.add(txtPasienNomor);
        txtPasienNomor.setBounds(130, 30, 250, 25);

        txtPasienTujuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasienTujuanMouseClicked(evt);
            }
        });
        pnlPendaftaranDetail.add(txtPasienTujuan);
        txtPasienTujuan.setBounds(130, 90, 250, 25);

        cbPasienKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "VVIP", "VIP", "I", "II", "III", "NONE" }));
        pnlPendaftaranDetail.add(cbPasienKelas);
        cbPasienKelas.setBounds(130, 120, 250, 25);

        cbPasienTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        cbPasienTanggungan.setBorder(null);
        pnlPendaftaranDetail.add(cbPasienTanggungan);
        cbPasienTanggungan.setBounds(130, 150, 250, 25);

        btnPasienTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnPasienTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienTambahActionPerformed(evt);
            }
        });
        pnlPendaftaranDetail.add(btnPasienTambah);
        btnPasienTambah.setBounds(200, 190, 80, 30);
        pnlPendaftaranDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(130, 60, 250, 25);

        btnCetakKartu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_cetak.png"))); // NOI18N
        btnCetakKartu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKartuActionPerformed(evt);
            }
        });
        pnlPendaftaranDetail.add(btnCetakKartu);
        btnCetakKartu.setBounds(300, 190, 80, 30);

        getContentPane().add(pnlPendaftaranDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 410, 400, 240));

        pnlPasienDetail.setBackground(Utama.colorTransparentPanel);
        pnlPasienDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlPasienDetail.setLayout(null);

        jLabel14.setText("NO. PASIEN");
        pnlPasienDetail.add(jLabel14);
        jLabel14.setBounds(20, 30, 100, 25);

        jLabel17.setText("NO. JAMINAN");
        pnlPasienDetail.add(jLabel17);
        jLabel17.setBounds(20, 90, 100, 25);

        jLabel18.setText("NAMA");
        pnlPasienDetail.add(jLabel18);
        jLabel18.setBounds(20, 120, 100, 25);

        jLabel19.setText("KELAMIN");
        pnlPasienDetail.add(jLabel19);
        jLabel19.setBounds(20, 150, 100, 25);

        jLabel20.setText("TANGGAL LAHIR");
        pnlPasienDetail.add(jLabel20);
        jLabel20.setBounds(20, 180, 100, 25);

        jLabel21.setText("GOL. DARAH");
        pnlPasienDetail.add(jLabel21);
        jLabel21.setBounds(20, 210, 100, 25);

        jLabel22.setText("AGAMA");
        pnlPasienDetail.add(jLabel22);
        jLabel22.setBounds(20, 240, 100, 25);

        jLabel23.setText("TELEPON");
        pnlPasienDetail.add(jLabel23);
        jLabel23.setBounds(20, 270, 100, 25);

        jLabel24.setText("TANGGUNGAN");
        pnlPasienDetail.add(jLabel24);
        jLabel24.setBounds(20, 300, 100, 25);

        jLabel25.setText("STATUS RAWAT");
        pnlPasienDetail.add(jLabel25);
        jLabel25.setBounds(20, 330, 100, 25);

        jLabel26.setText("KELAS");
        pnlPasienDetail.add(jLabel26);
        jLabel26.setBounds(20, 390, 100, 25);

        jLabel39.setText("TANGGAL MASUK");
        pnlPasienDetail.add(jLabel39);
        jLabel39.setBounds(20, 360, 100, 25);

        txtPasienKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        txtPasienKode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasienKodeKeyPressed(evt);
            }
        });
        pnlPasienDetail.add(txtPasienKode);
        txtPasienKode.setBounds(130, 30, 250, 25);

        txtPasienNik.setEditable(false);
        txtPasienNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNik);
        txtPasienNik.setBounds(130, 90, 250, 25);

        txtPasienNama.setEditable(false);
        txtPasienNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNama);
        txtPasienNama.setBounds(130, 120, 250, 25);

        txtPasienTanggalLahir.setEditable(false);
        txtPasienTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(130, 180, 250, 25);

        txtPasienDarah.setEditable(false);
        txtPasienDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienDarah);
        txtPasienDarah.setBounds(130, 210, 250, 25);

        txtPasienAgama.setEditable(false);
        txtPasienAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienAgama);
        txtPasienAgama.setBounds(130, 240, 250, 25);

        txtPasienTelepon.setEditable(false);
        txtPasienTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(130, 270, 250, 25);

        txtPasienKelamin.setEditable(false);
        txtPasienKelamin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(130, 150, 250, 25);

        txtPasienStatus.setEditable(false);
        txtPasienStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienStatus);
        txtPasienStatus.setBounds(130, 330, 250, 25);

        txtPasienTanggalMasukDetail.setEditable(false);
        txtPasienTanggalMasukDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalMasukDetail);
        txtPasienTanggalMasukDetail.setBounds(130, 360, 250, 25);

        txtPasienTanggungan.setEditable(false);
        txtPasienTanggungan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(130, 300, 250, 25);

        txtPasienKelas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienKelas);
        txtPasienKelas.setBounds(130, 390, 250, 25);
        pnlPasienDetail.add(jSeparator2);
        jSeparator2.setBounds(0, 62, 400, 10);

        getContentPane().add(pnlPasienDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 180, 400, 430));

        pnlHomeDetail.setBackground(Utama.colorTransparentPanel);
        pnlHomeDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA RUUANGAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlHomeDetail.setLayout(null);

        jLabel29.setText("JUMLAH PASIEN");
        pnlHomeDetail.add(jLabel29);
        jLabel29.setBounds(20, 30, 100, 25);

        jLabel30.setText("VVIP");
        pnlHomeDetail.add(jLabel30);
        jLabel30.setBounds(20, 90, 100, 25);

        jLabel31.setText("VIP");
        pnlHomeDetail.add(jLabel31);
        jLabel31.setBounds(20, 130, 100, 25);

        jLabel32.setText("KELAS I");
        pnlHomeDetail.add(jLabel32);
        jLabel32.setBounds(20, 170, 100, 25);

        jLabel33.setText("KELAS II");
        pnlHomeDetail.add(jLabel33);
        jLabel33.setBounds(20, 210, 100, 25);

        jLabel34.setText("KELAS III");
        pnlHomeDetail.add(jLabel34);
        jLabel34.setBounds(20, 250, 100, 25);
        pnlHomeDetail.add(jSeparator3);
        jSeparator3.setBounds(0, 70, 400, 10);

        txtJumlahPasien.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasien);
        txtJumlahPasien.setBounds(130, 30, 250, 25);

        txtJumlahPasienKelas3.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas3);
        txtJumlahPasienKelas3.setBounds(130, 250, 250, 25);

        txtJumlahPasienVvip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVvip);
        txtJumlahPasienVvip.setBounds(130, 90, 250, 25);

        txtJumlahPasienVip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVip);
        txtJumlahPasienVip.setBounds(130, 130, 250, 25);

        txtJumlahPasienKelas1.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas1);
        txtJumlahPasienKelas1.setBounds(130, 170, 250, 25);

        txtJumlahPasienKelas2.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas2);
        txtJumlahPasienKelas2.setBounds(130, 210, 250, 25);

        getContentPane().add(pnlHomeDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 180, 400, 300));

        jToolBar1.setBackground(java.awt.SystemColor.activeCaptionBorder);
        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel1.setText("ANDA LOGIN SEBEGAI :");
        jToolBar1.add(jLabel1);

        lblOperator.setText("OPERATOR");
        jToolBar1.add(lblOperator);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText(" - ");
        jLabel35.setMaximumSize(new java.awt.Dimension(25, 14));
        jToolBar1.add(jLabel35);

        jLabel36.setText("UNIT : ");
        jToolBar1.add(jLabel36);

        lblUnit.setText("UNIT");
        jToolBar1.add(lblUnit);

        jSeparator1.setMaximumSize(new java.awt.Dimension(20, 32767));
        jToolBar1.add(jSeparator1);

        btnLogout.setText("LOGOUT");
        btnLogout.setFocusable(false);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setMaximumSize(new java.awt.Dimension(80, 21));
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
        btnUbahPasien.setMaximumSize(new java.awt.Dimension(120, 21));
        btnUbahPasien.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUbahPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahPasienActionPerformed(evt);
            }
        });
        jToolBar1.add(btnUbahPasien);

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

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 760, 1280, 40));

        btnPendaftaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_pendaftaran.png"))); // NOI18N
        btnPendaftaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendaftaranActionPerformed(evt);
            }
        });
        getContentPane().add(btnPendaftaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, 130, 30));

        btnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_sal_pasien.png"))); // NOI18N
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        getContentPane().add(btnPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 120, 190, 50));

        btnRuangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_sal_ruangan.png"))); // NOI18N
        btnRuangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRuanganActionPerformed(evt);
            }
        });
        getContentPane().add(btnRuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 120, 190, 50));

        pnlKeluar.setBackground(Utama.colorTransparentPanel);
        pnlKeluar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN KELUAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlKeluar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setText("Keadaan Pasien");
        pnlKeluar.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI" }));
        pnlKeluar.add(cbPasienKeadaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 250, 25));

        btnPasienKeluar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_keluar.png"))); // NOI18N
        btnPasienKeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluar1ActionPerformed(evt);
            }
        });
        pnlKeluar.add(btnPasienKeluar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 80, 30));

        getContentPane().add(pnlKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 620, 400, 90));

        Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_ugd.png"))); // NOI18N
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

        Calendar tanggalLahir = Calendar.getInstance();
        tanggalLahir.setTime(penduduk.getTanggalLahir());
        
        txtPasienNomor.setText(Pasien.createKode());
    }//GEN-LAST:event_tblPendudukMouseClicked

    private void btnPendudukUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukUpdateActionPerformed
        TambahPasien tambahPasien = new TambahPasien(penduduk);
        tambahPasien.setVisible(true);
    }//GEN-LAST:event_btnPendudukUpdateActionPerformed

    private void btnPendudukTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukTambahActionPerformed
        pasien = null;

        TambahPasien tambahPasien = new TambahPasien();
        tambahPasien.setVisible(true);

    }//GEN-LAST:event_btnPendudukTambahActionPerformed

    private void btnPasienTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienTambahActionPerformed
        String kode = txtPasienNomor.getText();
        String tanggungan = cbPasienTanggungan.getSelectedItem().toString();
        String kelas = cbPasienKelas.getSelectedItem().toString();

        Calendar tanggalMasuk = txtPasienTanggalMasuk.getSelectedDate();
        long lTime = tanggalMasuk.getTimeInMillis();
        
        try {
            if (tanggungan == null || tanggungan.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih tanggungan");
            if (kelas == null || kelas.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih kelas");
            if (tujuan == null)
                throw new ServiceException("Silahkan masukan unit tujuan");
            
            pasienValue = pasienService.daftar(penduduk, Penanggung.valueOf(tanggungan), new Date(lTime), kode, Pasien.Pendaftaran.UGD, Kelas.valueOf(kelas), tujuan);
            txtPasienNomor.setText(pasienValue.getKode());
            
            tambahTagihanKarcis(pasienValue);

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
            
            txtPasienNomor.setText(Pasien.createKode());
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtKeywordFocusLost

    private void btnPendaftaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendaftaranActionPerformed
        showPendaftaran();
    }//GEN-LAST:event_btnPendaftaranActionPerformed

    private void btnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienActionPerformed
        pnlPendaftaranDetail.setVisible(false);
        pnlPendaftaranDetailPenduduk.setVisible(false);
        pnlPenduduk.setVisible(false);
        
        pnlTindakan.setVisible(true);
        pnlPasienDetail.setVisible(true);
        pnlKeluar.setVisible(true);
        
        pnlHome.setVisible(false);
        pnlHomeDetail.setVisible(false);
    }//GEN-LAST:event_btnPasienActionPerformed

    private void btnRuanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRuanganActionPerformed
        pnlPendaftaranDetail.setVisible(false);
        pnlPendaftaranDetailPenduduk.setVisible(false);
        pnlPenduduk.setVisible(false);
        
        pnlTindakan.setVisible(false);
        pnlPasienDetail.setVisible(false);
        pnlKeluar.setVisible(false);
        
        pnlHome.setVisible(true);
        pnlHomeDetail.setVisible(true);
        
        reloadTablePasien();
    }//GEN-LAST:event_btnRuanganActionPerformed

    private void btnTindakanHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanHapusActionPerformed
        try {
            Pelayanan pelayanan = getPelayanan();
            
            if (!TokenHolder.getUnit().equals(pelayanan.getUnit())) {
                JOptionPane.showMessageDialog(this, 
                        String.format("Maaf anda tidak bisa menghapus pelayanan %s yang berasal dari unit %s. Anda hanya bisa menghapus pelayanan dari unit %s", 
                                pelayanan.getTindakan().getNama(), pelayanan.getNamaUnit(), TokenHolder.getNamaUnit()));
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(this, String.format("Anda yakin ingin menghapus pelayanan %s pada tanggal %s",
                pelayanan.getTindakan().getNama(), pelayanan.getTanggal()));

            if (JOptionPane.YES_OPTION == pilihan) {
                pelayananService.hapus(pelayanan);
                reloadTableTindakan();
            }
        } catch (ComponentSelectionException | ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanHapusActionPerformed

    private void btnTindakanSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanSimpanActionPerformed
        new TambahTagihan(this, Tindakan.class, pasien).setVisible(true);
    }//GEN-LAST:event_btnTindakanSimpanActionPerformed

    private void btnTindakanUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanUpdateActionPerformed
        try {
            Pelayanan pelayanan = getPelayanan();
            
            if (!TokenHolder.getUnit().equals(pelayanan.getUnit())) {
                JOptionPane.showMessageDialog(this, 
                        String.format("Maaf anda tidak bisa mengubah pelayanan %s yang berasal dari unit %s. Anda hanya bisa mengubah pelayanan dari unit %s", 
                                pelayanan.getTindakan().getNama(), pelayanan.getNamaUnit(), TokenHolder.getNamaUnit()));
                return;
            }

            new TambahTagihan(this, pasien, pelayanan).setVisible(true);
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanUpdateActionPerformed

    private void tblTindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTindakanMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTindakanMouseClicked

    private void txtPasienKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasienKodeFocusLost
        String kode = txtPasienKode.getText();

        if (kode.equals(""))
            return;

        try {
            pasien = pasienService.get(kode);

            txtPasienNik.setText(pasien.getNik());
            txtPasienNama.setText(pasien.getNama());
            txtPasienKelamin.setText(pasien.getKelamin().toString());
            txtPasienTanggalLahir.setText(pasien.getTanggalLahir().toString());
            txtPasienDarah.setText(pasien.getDarah());
            txtPasienAgama.setText(pasien.getAgama());
            txtPasienTelepon.setText(pasien.getTelepon());
            txtPasienTanggungan.setText(pasien.getPenanggung().toString());
            txtPasienStatus.setText(pasien.getStatus().toString());
            txtPasienTanggalMasukDetail.setText(pasien.getTanggalMasuk().toString());
            txtPasienKelas.setText(pasien.getKelas().toString());

            loadTindakan(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    private void tblPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPasienMouseClicked
        Integer index = tblPasien.getSelectedRow();
        PasienTableModel tableModel = (PasienTableModel)tblPasien.getModel();

        pasien = tableModel.getPasien(index);
    }//GEN-LAST:event_tblPasienMouseClicked

    private void btnPasienMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienMasukActionPerformed
        String kode = txtPasienMasuk.getText();
        
        try {
            if (kode.equals(""))
                throw new ComponentSelectionException("Silahkan masukan nomor pasien");

            pasienService.masuk(kode, TokenHolder.getIdUnit());

            JOptionPane.showMessageDialog(this, "Berhasil!");
            reloadTablePasien();
        } catch (ServiceException | ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienMasukActionPerformed

    private void txtPasienTujuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasienTujuanMouseClicked
        Pencarian frameCari = new Pencarian(this, Unit.class);
        frameCari.setVisible(true);
    }//GEN-LAST:event_txtPasienTujuanMouseClicked

    private void btnPasienKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienKeluarActionPerformed
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih pasien dari tabel");
            return;
        }
        
        try {
            pasienService.keluar(pasien.getKode());
            JOptionPane.showMessageDialog(this, "Berhasil");
            reloadTablePasien();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienKeluarActionPerformed

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnPendaftaran.requestFocus();
    }//GEN-LAST:event_txtKeywordKeyPressed

    private void txtPasienKodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasienKodeKeyPressed
        if (evt.getKeyCode() == 10)
            btnPasien.requestFocus();
    }//GEN-LAST:event_txtPasienKodeKeyPressed

    private void btnUbahPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahPasienActionPerformed
        new DetailPasien().setVisible(true);
    }//GEN-LAST:event_btnUbahPasienActionPerformed

    private void btnCetakPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPasienActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Pasien.class);

            frame.setPendaftaran(Pasien.Pendaftaran.UGD);
            if (TokenHolder.getToken().getNamaUnit().equals("NICU"))
                frame.setPendaftaran(Pasien.Pendaftaran.NICU);
            
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakPasienActionPerformed

    private void btnPasienKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienKeluar1ActionPerformed
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Silahkan cari pasien terlebih dulu.");
            return;
        }

        String keadaan = (String) cbPasienKeadaan.getSelectedItem();
        if (keadaan.equals("- Pilih -")) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih keadaan pasien.");
            return;
        }

        try {
            pasien = pasienService.keluar(pasien, Pasien.KeadaanPasien.valueOf(keadaan), Pasien.StatusPasien.KELUAR);
            JOptionPane.showMessageDialog(this, "Berhasil!");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienKeluar1ActionPerformed

    private void btnCetakKartuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKartuActionPerformed
        if (pasienValue == null) {
            JOptionPane.showMessageDialog(this, "Silahkan mengisi data pasien terlebih dahulu");
            return;
        }

        PdfProcessor pdfProcessor = new PdfProcessor();
        KartuPasienPdfView pdfView = new KartuPasienPdfView();

        try {
            Map<String, Object> model = new HashMap<>();
            model.put("pasien", pasienValue);

            pdfProcessor.process(pdfView, model, String.format("pasien-%s.pdf", DateUtil.getTime().hashCode()));
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakKartuActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Image;
    private javax.swing.JButton btnCetakKartu;
    private javax.swing.JButton btnCetakPasien;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JButton btnPasienKeluar1;
    private javax.swing.JButton btnPasienMasuk;
    private javax.swing.JButton btnPasienTambah;
    private javax.swing.JButton btnPendaftaran;
    private javax.swing.JButton btnPendudukTambah;
    private javax.swing.JButton btnPendudukUpdate;
    private javax.swing.JButton btnRuangan;
    private javax.swing.JButton btnTindakanHapus;
    private javax.swing.JButton btnTindakanSimpan;
    private javax.swing.JButton btnTindakanUpdate;
    private javax.swing.JButton btnUbahPasien;
    private javax.swing.JComboBox cbPasienKeadaan;
    private javax.swing.JComboBox cbPasienKelas;
    private javax.swing.JComboBox cbPasienTanggungan;
    private javax.swing.JComboBox cbPendudukKelamin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeDetail;
    private javax.swing.JPanel pnlKeluar;
    private javax.swing.JPanel pnlMasuk;
    private javax.swing.JPanel pnlPasienDetail;
    private javax.swing.JPanel pnlPendaftaranDetail;
    private javax.swing.JPanel pnlPendaftaranDetailPenduduk;
    private javax.swing.JPanel pnlPenduduk;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JScrollPane scrollPasien;
    private javax.swing.JScrollPane scrollPenduduk;
    private javax.swing.JTable tblPasien;
    private javax.swing.JTable tblPenduduk;
    private javax.swing.JTable tblTindakan;
    private javax.swing.JTextField txtJumlahPasien;
    private javax.swing.JTextField txtJumlahPasienKelas1;
    private javax.swing.JTextField txtJumlahPasienKelas2;
    private javax.swing.JTextField txtJumlahPasienKelas3;
    private javax.swing.JTextField txtJumlahPasienVip;
    private javax.swing.JTextField txtJumlahPasienVvip;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKelas;
    private javax.swing.JTextField txtPasienKode;
    private javax.swing.JTextField txtPasienMasuk;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienNomor;
    private javax.swing.JTextField txtPasienStatus;
    private javax.swing.JTextField txtPasienTanggalLahir;
    private datechooser.beans.DateChooserCombo txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTanggalMasukDetail;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPasienTelepon;
    private javax.swing.JTextField txtPasienTujuan;
    private javax.swing.JTextField txtPendudukKode;
    private javax.swing.JTextField txtPendudukNama;
    private javax.swing.JTextField txtPendudukNik;
    private javax.swing.JTextField txtPendudukUmur;
    // End of variables declaration//GEN-END:variables
}
