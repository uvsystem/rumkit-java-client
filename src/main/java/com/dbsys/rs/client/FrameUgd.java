package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.PendudukService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PelayananTemporal;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Tindakan;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class FrameUgd extends javax.swing.JFrame implements BhpTableFrame, TindakanTableFrame {

    private final PendudukService pendudukService = PendudukService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final TokenService tokenService= TokenService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);
    private final PemakaianService pemakaianBhpService = PemakaianService.getInstance(EventController.host);
    private final TindakanService tindakanService = TindakanService.getInstance(EventController.host);

    private Penduduk penduduk;
    private Pasien pasien;
    
    /**
     * Creates new form Pendaftaran
     */
    public FrameUgd() {
        initComponents();
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
        txtPasienTanggalMasuk.setText(DateUtil.getDate().toString());
        
        showPendaftaran();
    }
    
    private void showPendaftaran() {
        pnlPendaftaranCari.setVisible(true);
        pnlPendaftaranDetail.setVisible(true);
        pnlPendaftaranDetailPenduduk.setVisible(true);
        lblPendaftaran.setVisible(true);
        scrollPenduduk.setVisible(true);
        
        panePasien.setVisible(false);
        pnlPasienDetail.setVisible(false);
        
        pnlHome.setVisible(false);
        pnlHomeDetail.setVisible(false);
    }
    
    @Override
    public void reloadTableTindakan() {
        try {
            loadTindakan(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            tblTindakan.setModel(new PelayananTableModel(null));
        }
    }

    private void loadTindakan(Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<Pelayanan> list = pelayananService.getByPasien(pasien.getId());

        PelayananTableModel tableModel = new PelayananTableModel(list);
        tblTindakan.setModel(tableModel);
    }
    
    private Pelayanan getPelayanan() throws ComponentSelectionException {
        int index = tblTindakan.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PelayananTableModel tableModel = (PelayananTableModel)tblTindakan.getModel();
        return tableModel.getPelayanan(index);
    }
    
    @Override
    public void reloadTableBhp() {
        try {
            loadBhp(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            tblBhp.setModel(new PemakaianTableModel(null));
        }
    }
    
    private void loadBhp(Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<Pemakaian> listPemakaian = pemakaianBhpService.getByPasien(pasien.getId());
        PemakaianTableModel tableModel = new PemakaianTableModel(listPemakaian);
        tblBhp.setModel(tableModel);
    }
    
    private Pemakaian getPemakaianBhp() throws ComponentSelectionException {
        int index = tblBhp.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PemakaianTableModel tableModel = (PemakaianTableModel)tblBhp.getModel();
        return tableModel.getPemakaian(index);
    }

    private void reloadHome() {
        List<Pasien> list = null;
        try {
            list = loadPasienPerawatan();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            tblPasien.setModel(new PasienTableModel(list));
        } finally {
            loadDetailRuangan(list);
        }
    }
    
    private List<Pasien> loadPasienPerawatan() throws ServiceException {
        List<Pasien> list = pasienService.getByUnit(TokenHolder.getIdUnit());
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogKeluar = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtKeterangan = new javax.swing.JTextField();
        txtBiayaTambahan = new javax.swing.JTextField();
        btnPasienKeluar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        pnlHome = new javax.swing.JPanel();
        scrollPasien = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        pnlMasuk = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPasienMasuk = new javax.swing.JTextField();
        cbKelas = new javax.swing.JComboBox();
        btnMasuk = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        panePasien = new javax.swing.JTabbedPane();
        pnlTindakan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        btnTindakanSimpan = new javax.swing.JButton();
        btnTindakanUpdate = new javax.swing.JButton();
        btnTindakanHapus = new javax.swing.JButton();
        pnlBhp = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        btnBhpTambah = new javax.swing.JButton();
        btnBhpUpdate = new javax.swing.JButton();
        btnBhpHapus = new javax.swing.JButton();
        scrollPenduduk = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
        lblPendaftaran = new javax.swing.JLabel();
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
        pnlPendaftaranCari = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        pnlPendaftaranDetailPenduduk = new javax.swing.JPanel();
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
        pnlPendaftaranDetail = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cbPasienTanggungan = new javax.swing.JComboBox();
        txtPasienNomor = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        btnPasienTambah = new javax.swing.JButton();
        btnCetakStatus = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        btnPendaftaran = new javax.swing.JButton();
        btnPasien = new javax.swing.JButton();
        btnRuangan = new javax.swing.JButton();
        Image = new javax.swing.JLabel();

        dialogKeluar.setUndecorated(true);
        dialogKeluar.setResizable(false);
        dialogKeluar.getContentPane().setLayout(null);

        jLabel3.setText("Biaya Tambahan");
        dialogKeluar.getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 20, 80, 14);

        jLabel28.setText("Keterangan");
        dialogKeluar.getContentPane().add(jLabel28);
        jLabel28.setBounds(20, 60, 60, 14);
        dialogKeluar.getContentPane().add(txtKeterangan);
        txtKeterangan.setBounds(120, 60, 140, 20);
        dialogKeluar.getContentPane().add(txtBiayaTambahan);
        txtBiayaTambahan.setBounds(120, 20, 140, 20);

        btnPasienKeluar.setText("Pasien Keluar");
        btnPasienKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluarActionPerformed(evt);
            }
        });
        dialogKeluar.getContentPane().add(btnPasienKeluar);
        btnPasienKeluar.setBounds(163, 90, 97, 30);

        jButton1.setText("Batal");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        dialogKeluar.getContentPane().add(jButton1);
        jButton1.setBounds(70, 90, 57, 30);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Rumah Sakit Liun Kendage Tahuna");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlHome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlHome.setLayout(null);

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
        scrollPasien.setBounds(10, 80, 820, 470);

        pnlMasuk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pasien Masuk"));
        pnlMasuk.setLayout(null);

        jLabel2.setText("No. Pasien");
        pnlMasuk.add(jLabel2);
        jLabel2.setBounds(30, 20, 90, 20);
        pnlMasuk.add(txtPasienMasuk);
        txtPasienMasuk.setBounds(140, 20, 160, 20);

        cbKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "NONE", "VVIP", "VIP", "I", "II", "III", "ICU" }));
        pnlMasuk.add(cbKelas);
        cbKelas.setBounds(540, 20, 160, 20);

        btnMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Tambah(small).png"))); // NOI18N
        btnMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasukActionPerformed(evt);
            }
        });
        pnlMasuk.add(btnMasuk);
        btnMasuk.setBounds(720, 15, 90, 35);

        jLabel27.setText("Kelas");
        pnlMasuk.add(jLabel27);
        jLabel27.setBounds(420, 20, 90, 14);

        pnlHome.add(pnlMasuk);
        pnlMasuk.setBounds(10, 10, 820, 60);

        getContentPane().add(pnlHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 840, 560));

        panePasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panePasienMouseClicked(evt);
            }
        });

        pnlTindakan.setLayout(null);

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
        jScrollPane1.setBounds(10, 11, 720, 510);

        btnTindakanSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTindakanSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanSimpanActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanSimpan);
        btnTindakanSimpan.setBounds(740, 10, 90, 35);

        btnTindakanUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Update(small).png"))); // NOI18N
        btnTindakanUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUpdateActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanUpdate);
        btnTindakanUpdate.setBounds(740, 50, 90, 35);

        btnTindakanHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus(small).png"))); // NOI18N
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanHapus);
        btnTindakanHapus.setBounds(740, 90, 90, 35);

        panePasien.addTab("TINDAKAN", pnlTindakan);

        pnlBhp.setLayout(null);

        tblBhp.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblBhp);

        pnlBhp.add(jScrollPane2);
        jScrollPane2.setBounds(10, 11, 720, 510);

        btnBhpTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnBhpTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpTambahActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpTambah);
        btnBhpTambah.setBounds(740, 10, 90, 35);

        btnBhpUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Update(small).png"))); // NOI18N
        btnBhpUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpUpdateActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpUpdate);
        btnBhpUpdate.setBounds(740, 50, 90, 35);

        btnBhpHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus(small).png"))); // NOI18N
        btnBhpHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpHapusActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpHapus);
        btnBhpHapus.setBounds(740, 90, 90, 35);

        panePasien.addTab("BAHAN HABIS PAKAI", pnlBhp);

        getContentPane().add(panePasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 840, 560));

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

        getContentPane().add(scrollPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 840, 560));

        lblPendaftaran.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPendaftaran.setText("DAFTAR PASIEN / REKAM MEDIK");
        getContentPane().add(lblPendaftaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 170, 180, -1));

        pnlPasienDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Pasien"));
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlPasienDetail.setLayout(null);

        jLabel14.setText("No. Pasien");
        pnlPasienDetail.add(jLabel14);
        jLabel14.setBounds(20, 30, 90, 14);

        jLabel17.setText("NIK");
        pnlPasienDetail.add(jLabel17);
        jLabel17.setBounds(20, 90, 90, 14);

        jLabel18.setText("NAMA");
        pnlPasienDetail.add(jLabel18);
        jLabel18.setBounds(20, 120, 90, 14);

        jLabel19.setText("KELAMIN");
        pnlPasienDetail.add(jLabel19);
        jLabel19.setBounds(20, 150, 90, 14);

        jLabel20.setText("TANGGAL LAHIR");
        pnlPasienDetail.add(jLabel20);
        jLabel20.setBounds(20, 180, 90, 14);

        jLabel21.setText("GOL. DARAH");
        pnlPasienDetail.add(jLabel21);
        jLabel21.setBounds(20, 210, 90, 14);

        jLabel22.setText("AGAMA");
        pnlPasienDetail.add(jLabel22);
        jLabel22.setBounds(20, 240, 90, 14);

        jLabel23.setText("TELEPON");
        pnlPasienDetail.add(jLabel23);
        jLabel23.setBounds(20, 270, 90, 14);

        jLabel24.setText("TANGGUNGAN");
        pnlPasienDetail.add(jLabel24);
        jLabel24.setBounds(20, 300, 90, 14);

        jLabel25.setText("STATUS RAWAT");
        pnlPasienDetail.add(jLabel25);
        jLabel25.setBounds(20, 330, 90, 14);

        jLabel26.setText("TANGGAL MASUK");
        pnlPasienDetail.add(jLabel26);
        jLabel26.setBounds(20, 360, 90, 14);

        txtPasienKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        pnlPasienDetail.add(txtPasienKode);
        txtPasienKode.setBounds(120, 30, 250, 20);

        txtPasienNik.setEditable(false);
        txtPasienNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNik);
        txtPasienNik.setBounds(120, 90, 250, 20);

        txtPasienNama.setEditable(false);
        txtPasienNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNama);
        txtPasienNama.setBounds(120, 120, 250, 20);

        txtPasienTanggalLahir.setEditable(false);
        txtPasienTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(120, 180, 250, 20);

        txtPasienDarah.setEditable(false);
        txtPasienDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienDarah);
        txtPasienDarah.setBounds(120, 210, 250, 20);

        txtPasienAgama.setEditable(false);
        txtPasienAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienAgama);
        txtPasienAgama.setBounds(120, 240, 250, 20);

        txtPasienTelepon.setEditable(false);
        txtPasienTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(120, 270, 250, 20);

        txtPasienKelamin.setEditable(false);
        txtPasienKelamin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(120, 150, 250, 20);

        txtPasienStatus.setEditable(false);
        txtPasienStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienStatus);
        txtPasienStatus.setBounds(120, 330, 250, 18);

        txtPasienTanggalMasukDetail.setEditable(false);
        txtPasienTanggalMasukDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalMasukDetail);
        txtPasienTanggalMasukDetail.setBounds(120, 360, 250, 18);

        txtPasienTanggungan.setEditable(false);
        txtPasienTanggungan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(120, 300, 250, 18);
        pnlPasienDetail.add(jSeparator2);
        jSeparator2.setBounds(0, 62, 400, 10);

        getContentPane().add(pnlPasienDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 190, 400, 400));

        pnlHomeDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Data Ruangan"));
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlHomeDetail.setLayout(null);

        jLabel29.setText("Jumlah Pasien");
        pnlHomeDetail.add(jLabel29);
        jLabel29.setBounds(20, 30, 90, 14);

        jLabel30.setText("VVIP");
        pnlHomeDetail.add(jLabel30);
        jLabel30.setBounds(20, 110, 90, 14);

        jLabel31.setText("VIP");
        pnlHomeDetail.add(jLabel31);
        jLabel31.setBounds(20, 150, 90, 14);

        jLabel32.setText("KELAS I");
        pnlHomeDetail.add(jLabel32);
        jLabel32.setBounds(20, 190, 90, 14);

        jLabel33.setText("KELAS II");
        pnlHomeDetail.add(jLabel33);
        jLabel33.setBounds(20, 230, 90, 14);

        jLabel34.setText("KELAS III");
        pnlHomeDetail.add(jLabel34);
        jLabel34.setBounds(20, 270, 90, 14);
        pnlHomeDetail.add(jSeparator3);
        jSeparator3.setBounds(0, 70, 400, 10);

        txtJumlahPasien.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasien);
        txtJumlahPasien.setBounds(120, 30, 250, 20);

        txtJumlahPasienKelas3.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas3);
        txtJumlahPasienKelas3.setBounds(120, 270, 250, 20);

        txtJumlahPasienVvip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVvip);
        txtJumlahPasienVvip.setBounds(120, 110, 250, 20);

        txtJumlahPasienVip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVip);
        txtJumlahPasienVip.setBounds(120, 150, 250, 20);

        txtJumlahPasienKelas1.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas1);
        txtJumlahPasienKelas1.setBounds(120, 190, 250, 20);

        txtJumlahPasienKelas2.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas2);
        txtJumlahPasienKelas2.setBounds(120, 230, 250, 20);

        getContentPane().add(pnlHomeDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 190, 400, 300));

        pnlPendaftaranCari.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        pnlPendaftaranCari.setLayout(null);
        pnlPendaftaranCari.setBackground(new Color(0, 0, 0, 20));

        jLabel13.setText("Kata Kunci");
        pnlPendaftaranCari.add(jLabel13);
        jLabel13.setBounds(12, 20, 150, 14);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        pnlPendaftaranCari.add(txtKeyword);
        txtKeyword.setBounds(170, 20, 210, 20);

        getContentPane().add(pnlPendaftaranCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 190, 400, 60));

        pnlPendaftaranDetailPenduduk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Rekam Medik"));
        pnlPendaftaranDetailPenduduk.setBackground(new Color(0,0,0,20));
        pnlPendaftaranDetailPenduduk.setLayout(null);

        jLabel4.setText("NOMOR");
        pnlPendaftaranDetailPenduduk.add(jLabel4);
        jLabel4.setBounds(20, 30, 140, 14);

        jLabel5.setText("NIK");
        pnlPendaftaranDetailPenduduk.add(jLabel5);
        jLabel5.setBounds(20, 60, 140, 14);

        jLabel6.setText("NAMA");
        pnlPendaftaranDetailPenduduk.add(jLabel6);
        jLabel6.setBounds(20, 90, 140, 14);

        jLabel7.setText("KELAMIN");
        pnlPendaftaranDetailPenduduk.add(jLabel7);
        jLabel7.setBounds(20, 120, 140, 14);

        jLabel8.setText("TANGGAL LAHIR");
        pnlPendaftaranDetailPenduduk.add(jLabel8);
        jLabel8.setBounds(20, 150, 140, 14);

        jLabel9.setText("GOL. DARAH");
        pnlPendaftaranDetailPenduduk.add(jLabel9);
        jLabel9.setBounds(20, 180, 140, 14);

        jLabel10.setText("AGAMA");
        pnlPendaftaranDetailPenduduk.add(jLabel10);
        jLabel10.setBounds(20, 210, 140, 14);

        jLabel11.setText("TELEPON");
        pnlPendaftaranDetailPenduduk.add(jLabel11);
        jLabel11.setBounds(20, 240, 140, 14);

        txtPendudukKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukKode);
        txtPendudukKode.setBounds(170, 30, 210, 20);

        txtPendudukNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukNik);
        txtPendudukNik.setBounds(169, 60, 210, 20);

        txtPendudukNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukNama);
        txtPendudukNama.setBounds(169, 90, 210, 20);

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        cbPendudukKelamin.setBorder(null);
        pnlPendaftaranDetailPenduduk.add(cbPendudukKelamin);
        cbPendudukKelamin.setBounds(170, 120, 210, 20);

        txtPendudukTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukTanggalLahir);
        txtPendudukTanggalLahir.setBounds(169, 150, 210, 20);

        txtPendudukDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukDarah);
        txtPendudukDarah.setBounds(169, 180, 210, 20);

        txtPendudukAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukAgama);
        txtPendudukAgama.setBounds(169, 210, 210, 20);

        txtPendudukTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukTelepon);
        txtPendudukTelepon.setBounds(169, 240, 210, 20);

        btnPendudukSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPendudukSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukSimpanActionPerformed(evt);
            }
        });
        pnlPendaftaranDetailPenduduk.add(btnPendudukSimpan);
        btnPendudukSimpan.setBounds(170, 270, 80, 30);

        btnPendudukClean.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnPendudukClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukCleanActionPerformed(evt);
            }
        });
        pnlPendaftaranDetailPenduduk.add(btnPendudukClean);
        btnPendudukClean.setBounds(300, 270, 80, 30);

        getContentPane().add(pnlPendaftaranDetailPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 255, 400, 320));

        pnlPendaftaranDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Pendaftaran Pasien"));
        pnlPendaftaranDetail.setLayout(null);
        pnlPendaftaranDetail.setBackground(new Color(0,0,0,20));

        jLabel12.setText("TANGGUNGAN");
        pnlPendaftaranDetail.add(jLabel12);
        jLabel12.setBounds(10, 30, 150, 14);

        jLabel16.setText("NOMOR PASIEN");
        pnlPendaftaranDetail.add(jLabel16);
        jLabel16.setBounds(10, 60, 150, 14);

        jLabel15.setText("TANGGAL MASUK");
        pnlPendaftaranDetail.add(jLabel15);
        jLabel15.setBounds(10, 90, 150, 14);

        cbPasienTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        cbPasienTanggungan.setBorder(null);
        pnlPendaftaranDetail.add(cbPasienTanggungan);
        cbPasienTanggungan.setBounds(170, 30, 210, 18);

        txtPasienNomor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetail.add(txtPasienNomor);
        txtPasienNomor.setBounds(170, 60, 210, 18);

        txtPasienTanggalMasuk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(170, 90, 210, 18);

        btnPasienTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPasienTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienTambahActionPerformed(evt);
            }
        });
        pnlPendaftaranDetail.add(btnPasienTambah);
        btnPasienTambah.setBounds(170, 120, 80, 30);

        btnCetakStatus.setText("Cetak Status");
        btnCetakStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakStatusActionPerformed(evt);
            }
        });
        pnlPendaftaranDetail.add(btnCetakStatus);
        btnCetakStatus.setBounds(280, 120, 95, 30);

        getContentPane().add(pnlPendaftaranDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 580, 400, 170));

        jToolBar1.setBackground(java.awt.SystemColor.activeCaptionBorder);
        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel1.setText("ANDA LOGIN SEBEGAI :");
        jToolBar1.add(jLabel1);

        lblOperator.setText("OPERATOR");
        jToolBar1.add(lblOperator);

        jLabel35.setText(" - ");
        jToolBar1.add(jLabel35);

        jLabel36.setText("UNIT : ");
        jToolBar1.add(jLabel36);

        lblUnit.setText("UNIT");
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

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 760, 1280, 40));

        btnPendaftaran.setText("PENDAFTARAN");
        btnPendaftaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendaftaranActionPerformed(evt);
            }
        });
        getContentPane().add(btnPendaftaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 130, 120, 50));

        btnPasien.setText("PASIEN");
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        getContentPane().add(btnPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 130, 120, 50));

        btnRuangan.setText("RUANGAN");
        btnRuangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRuanganActionPerformed(evt);
            }
        });
        getContentPane().add(btnRuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 130, 120, 50));

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
    }//GEN-LAST:event_tblPendudukMouseClicked

    private void btnPendudukCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukCleanActionPerformed
        penduduk = null;
        
        txtPendudukKode.setText(null);
        txtPendudukNik.setText(null);
        txtPendudukNama.setText(null);
        cbPendudukKelamin.setSelectedIndex(0);
        txtPendudukTanggalLahir.setText(null);
        txtPendudukDarah.setText(null);
        txtPendudukAgama.setText(null);
        txtPendudukTelepon.setText(null);
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
        String tanggungan = cbPasienTanggungan.getSelectedItem().toString();
        String tanggalMasuk = txtPasienTanggalMasuk.getText();
        String kode = txtPasienNomor.getText();
        
        try {
            pasien = pasienService.daftar(penduduk.getId(), Penanggung.valueOf(tanggungan), DateUtil.getDate(tanggalMasuk), kode);
            JOptionPane.showMessageDialog(this, "Berhasil menyimpan data pasien.");
            
            txtPasienNomor.setText(pasien.getKode());
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienTambahActionPerformed

    private void btnCetakStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakStatusActionPerformed
        JOptionPane.showMessageDialog(this, "Cetak Status Pasien belum");
    }//GEN-LAST:event_btnCetakStatusActionPerformed

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

    private void btnPendaftaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendaftaranActionPerformed
        showPendaftaran();
    }//GEN-LAST:event_btnPendaftaranActionPerformed

    private void btnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienActionPerformed
        pnlPendaftaranCari.setVisible(false);
        pnlPendaftaranDetail.setVisible(false);
        pnlPendaftaranDetailPenduduk.setVisible(false);
        lblPendaftaran.setVisible(false);
        scrollPenduduk.setVisible(false);
        
        panePasien.setVisible(true);
        pnlPasienDetail.setVisible(true);
        
        pnlHome.setVisible(false);
        pnlHomeDetail.setVisible(false);
    }//GEN-LAST:event_btnPasienActionPerformed

    private void btnRuanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRuanganActionPerformed
        pnlPendaftaranCari.setVisible(false);
        pnlPendaftaranDetail.setVisible(false);
        pnlPendaftaranDetailPenduduk.setVisible(false);
        lblPendaftaran.setVisible(false);
        scrollPenduduk.setVisible(false);
        
        panePasien.setVisible(false);
        pnlPasienDetail.setVisible(false);
        
        pnlHome.setVisible(true);
        pnlHomeDetail.setVisible(true);
    }//GEN-LAST:event_btnRuanganActionPerformed

    private void btnTindakanHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanHapusActionPerformed
        try {
            Pelayanan pelayanan = getPelayanan();

            int pilihan = JOptionPane.showConfirmDialog(this, String.format("Anda yakin ingin menghapus pelayanan %s pada tanggal %s",
                pelayanan.getTindakan().getNama(), pelayanan.getTanggal()));

        if (JOptionPane.YES_OPTION == pilihan) {
            JOptionPane.showMessageDialog(this, "Belum bisa");
        }
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanHapusActionPerformed

    private void btnTindakanSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanSimpanActionPerformed
        new FrameTambahObject(this, Tindakan.class, pasien).setVisible(true);
    }//GEN-LAST:event_btnTindakanSimpanActionPerformed

    private void btnTindakanUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanUpdateActionPerformed
        try {
            Pelayanan pelayanan = getPelayanan();

            new FrameTambahObject(this, pasien, pelayanan).setVisible(true);
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanUpdateActionPerformed

    private void btnBhpHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpHapusActionPerformed
        try {
            Pemakaian pemakaianBhp = getPemakaianBhp();

            int pilihan = JOptionPane.showConfirmDialog(this, String.format("Anda yakin ingin menghapus pemakaian %s pada tanggal %s",
                pemakaianBhp.getBarang().getNama(), pemakaianBhp.getTanggal()));

            if (JOptionPane.YES_OPTION == pilihan) {
                JOptionPane.showMessageDialog(this, "Belum bisa");
            }
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpHapusActionPerformed

    private void btnBhpTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpTambahActionPerformed
        new FrameTambahObject(this, BahanHabisPakai.class, pasien).setVisible(true);
    }//GEN-LAST:event_btnBhpTambahActionPerformed

    private void btnBhpUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpUpdateActionPerformed
        try {
            Pemakaian pemakaianBhp = getPemakaianBhp();

            new FrameTambahObject(this, pasien, pemakaianBhp).setVisible(true);
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpUpdateActionPerformed

    private void panePasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panePasienMouseClicked
        Integer index = panePasien.getSelectedIndex();

        if (pasien == null)
            index = -1;

        try {
            switch(index) {
                case 0: loadTindakan(pasien);
                    break;
                case 1: loadBhp(pasien);
                    break;
                default: break;
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_panePasienMouseClicked

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

            loadTindakan(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    private void tblPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPasienMouseClicked
        Point point = evt.getLocationOnScreen();
        dialogKeluar.setBounds(point.x, point.y, 280, 140);

        txtBiayaTambahan.setText("0");
        txtKeterangan.setText("-");

        dialogKeluar.setVisible(true);
    }//GEN-LAST:event_tblPasienMouseClicked

    private void btnMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasukActionPerformed
        String kode = txtPasienMasuk.getText();
        String kelas = (String)cbKelas.getSelectedItem();

        try {
            if (kode.equals(""))
                throw new ComponentSelectionException("Silahkan masukan nomor pasien");
            if (kelas.equals("- Pilih -"))
                throw new ComponentSelectionException("Silahkan pilih kelas");

            Pasien p = pasienService.get(kode);
            Tindakan tindakan = tindakanService.get("Rawat Inap", Kelas.valueOf(kelas));

            PelayananTemporal pelayanan = new PelayananTemporal();
            pelayanan.setPasien(p);
            pelayanan.setUnit(TokenHolder.getToken().getOperator().getUnit());
            pelayanan.setTanggalMulai(DateUtil.getDate());
            pelayanan.setJamMasuk(DateUtil.getTime());
            pelayanan.setTindakan(tindakan);
            pelayanan.setBiayaTambahan(new Long(0));
            pelayanan.setJumlah(0);
            pelayanan.setKeterangan("");

            pelayananService.masukSal(pelayanan);

            JOptionPane.showMessageDialog(this, "Berhasil!");
            reloadHome();
        } catch (ServiceException | ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnMasukActionPerformed

    private void btnPasienKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienKeluarActionPerformed
        dialogKeluar.setVisible(false);

        Integer index = tblPasien.getSelectedRow();
        PasienTableModel tableModel = (PasienTableModel)tblPasien.getModel();

        Pasien p = tableModel.getPasien(index);
        String tambahan = txtBiayaTambahan.getText();
        String keterangan = txtKeterangan.getText();

        try {
            pelayananService.keluarSal(p.getId(), DateUtil.getDate(), DateUtil.getTime(), Long.valueOf(tambahan), keterangan);

            JOptionPane.showMessageDialog(this, "Berhasil!");
            reloadHome();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienKeluarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dialogKeluar.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Image;
    private javax.swing.JButton btnBhpHapus;
    private javax.swing.JButton btnBhpTambah;
    private javax.swing.JButton btnBhpUpdate;
    private javax.swing.JButton btnCetakStatus;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMasuk;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JButton btnPasienTambah;
    private javax.swing.JButton btnPendaftaran;
    private javax.swing.JButton btnPendudukClean;
    private javax.swing.JButton btnPendudukSimpan;
    private javax.swing.JButton btnRuangan;
    private javax.swing.JButton btnTindakanHapus;
    private javax.swing.JButton btnTindakanSimpan;
    private javax.swing.JButton btnTindakanUpdate;
    private javax.swing.JComboBox cbKelas;
    private javax.swing.JComboBox cbPasienTanggungan;
    private javax.swing.JComboBox cbPendudukKelamin;
    private javax.swing.JDialog dialogKeluar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblPendaftaran;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JTabbedPane panePasien;
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeDetail;
    private javax.swing.JPanel pnlMasuk;
    private javax.swing.JPanel pnlPasienDetail;
    private javax.swing.JPanel pnlPendaftaranCari;
    private javax.swing.JPanel pnlPendaftaranDetail;
    private javax.swing.JPanel pnlPendaftaranDetailPenduduk;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JScrollPane scrollPasien;
    private javax.swing.JScrollPane scrollPenduduk;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblPasien;
    private javax.swing.JTable tblPenduduk;
    private javax.swing.JTable tblTindakan;
    private javax.swing.JTextField txtBiayaTambahan;
    private javax.swing.JTextField txtJumlahPasien;
    private javax.swing.JTextField txtJumlahPasienKelas1;
    private javax.swing.JTextField txtJumlahPasienKelas2;
    private javax.swing.JTextField txtJumlahPasienKelas3;
    private javax.swing.JTextField txtJumlahPasienVip;
    private javax.swing.JTextField txtJumlahPasienVvip;
    private javax.swing.JTextField txtKeterangan;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKode;
    private javax.swing.JTextField txtPasienMasuk;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienNomor;
    private javax.swing.JTextField txtPasienStatus;
    private javax.swing.JTextField txtPasienTanggalLahir;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTanggalMasukDetail;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPasienTelepon;
    private javax.swing.JTextField txtPendudukAgama;
    private javax.swing.JTextField txtPendudukDarah;
    private javax.swing.JTextField txtPendudukKode;
    private javax.swing.JTextField txtPendudukNama;
    private javax.swing.JTextField txtPendudukNik;
    private javax.swing.JTextField txtPendudukTanggalLahir;
    private javax.swing.JTextField txtPendudukTelepon;
    // End of variables declaration//GEN-END:variables
}
