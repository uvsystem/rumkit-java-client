package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.ComponentSelectionException;
import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.TindakanTableFrame;
import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PelayananTemporal;
import com.dbsys.rs.lib.entity.Tindakan;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 */
public class FrameSal extends javax.swing.JFrame implements TindakanTableFrame {

    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);
    private final TindakanService tindakanService = TindakanService.getInstance(EventController.host);

    private Pasien pasien;
    
    /**
     * Creates new form SAL
     */
    public FrameSal() {
        initComponents();

        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
        
        pnlHome.setVisible(true);
        pnlHomeDetail.setVisible(true);

        pnlTindakan.setVisible(false);
        pnlPasienDetail.setVisible(false);

        try {
            List<Pasien> list = loadPasienPerawatan();
            loadDetailRuangan(list);
        } catch (ServiceException ex) { }
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

    private void loadTindakan(Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<Pelayanan> list = pelayananService.getByPasien(pasien);

        PelayananTableModel tableModel = new PelayananTableModel(list);
        tblTindakan.setModel(tableModel);
    }
    
    @Override
    public void reloadTable() {
        try {
            loadTindakan(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            tblTindakan.setModel(new PelayananTableModel(null));
        }
    }
    
    private Pelayanan getPelayanan() throws ComponentSelectionException {
        int index = tblTindakan.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PelayananTableModel tableModel = (PelayananTableModel)tblTindakan.getModel();
        return tableModel.getPelayanan(index);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogKeluar = new javax.swing.JFrame();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtKeterangan = new javax.swing.JTextField();
        txtBiayaTambahan = new javax.swing.JTextField();
        btnPasienKeluar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnPasien = new javax.swing.JButton();
        pnlHome = new javax.swing.JPanel();
        scrollPasien = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        pnlKeluar = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtPasienKeluar = new javax.swing.JTextField();
        btnKeluar = new javax.swing.JButton();
        pnlMasuk = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPasienMasuk = new javax.swing.JTextField();
        btnMasuk = new javax.swing.JButton();
        pnlKelas = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        cbPasienKelas = new javax.swing.JComboBox();
        btnSimpanKelas = new javax.swing.JButton();
        pnlTindakan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        btnTindakanTambah = new javax.swing.JButton();
        btnTindakanUpdate = new javax.swing.JButton();
        btnTindakanHapus = new javax.swing.JButton();
        pnlPasienDetail = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtPasienKode = new javax.swing.JTextField();
        txtPasienNik = new javax.swing.JTextField();
        txtPasienNama = new javax.swing.JTextField();
        txtPasienTanggalLahir = new javax.swing.JTextField();
        txtPasienDarah = new javax.swing.JTextField();
        txtPasienAgama = new javax.swing.JTextField();
        txtPasienTelepon = new javax.swing.JTextField();
        txtPasienKelamin = new javax.swing.JTextField();
        txtPasienStatus = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienTanggungan = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        txtPasienKelas = new javax.swing.JTextField();
        pnlHomeDetail = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txtJumlahPasien = new javax.swing.JTextField();
        txtJumlahPasienKelas3 = new javax.swing.JTextField();
        txtJumlahPasienVvip = new javax.swing.JTextField();
        txtJumlahPasienVip = new javax.swing.JTextField();
        txtJumlahPasienKelas1 = new javax.swing.JTextField();
        txtJumlahPasienKelas2 = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel17 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();

        dialogKeluar.setUndecorated(false);
        dialogKeluar.setResizable(false);
        dialogKeluar.getContentPane().setLayout(null);

        jLabel3.setText("Biaya Tambahan");
        dialogKeluar.getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 20, 80, 14);

        jLabel14.setText("Keterangan");
        dialogKeluar.getContentPane().add(jLabel14);
        jLabel14.setBounds(20, 60, 60, 14);
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
        setTitle("RUMAH SAKIT LIUN KENDAGE");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setLayout(null);

        pnlMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlMain.setLayout(null);

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/SalHome_icon.png"))); // NOI18N
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        pnlMain.add(btnHome);
        btnHome.setBounds(30, 10, 150, 44);

        btnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/SalPasien_icon.png"))); // NOI18N
        btnPasien.setPreferredSize(new java.awt.Dimension(105, 23));
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        pnlMain.add(btnPasien);
        btnPasien.setBounds(220, 10, 150, 44);

        getContentPane().add(pnlMain);
        pnlMain.setBounds(860, 180, 400, 70);

        pnlHome.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DAFTAR PASIEN YANG DIRAWAT", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
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
        scrollPasien.setBounds(10, 100, 790, 390);

        pnlKeluar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN KELUAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlKeluar.setLayout(null);

        jLabel13.setText("No. Pasien");
        pnlKeluar.add(jLabel13);
        jLabel13.setBounds(10, 20, 90, 25);
        pnlKeluar.add(txtPasienKeluar);
        txtPasienKeluar.setBounds(100, 20, 200, 25);

        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        pnlKeluar.add(btnKeluar);
        btnKeluar.setBounds(310, 20, 73, 25);

        pnlHome.add(pnlKeluar);
        pnlKeluar.setBounds(410, 30, 390, 60);

        pnlMasuk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN MASUK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlMasuk.setLayout(null);

        jLabel2.setText("No. Pasien");
        pnlMasuk.add(jLabel2);
        jLabel2.setBounds(10, 20, 90, 25);
        pnlMasuk.add(txtPasienMasuk);
        txtPasienMasuk.setBounds(100, 20, 200, 25);

        btnMasuk.setText("MASUK");
        btnMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasukActionPerformed(evt);
            }
        });
        pnlMasuk.add(btnMasuk);
        btnMasuk.setBounds(310, 20, 73, 25);

        pnlHome.add(pnlMasuk);
        pnlMasuk.setBounds(10, 30, 390, 60);

        pnlKelas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "KELAS PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlKelas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setText("KELAS");
        pnlKelas.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 90, 25));

        cbPasienKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "VVIP", "VIP", "I", "II", "III" }));
        pnlKelas.add(cbPasienKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 25, 200, 25));

        btnSimpanKelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnSimpanKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanKelasActionPerformed(evt);
            }
        });
        pnlKelas.add(btnSimpanKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(305, 23, 80, 30));

        pnlHome.add(pnlKelas);
        pnlKelas.setBounds(10, 500, 400, 70);

        getContentPane().add(pnlHome);
        pnlHome.setBounds(20, 180, 810, 580);

        pnlTindakan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DAFTAR TINDAKAN YANG SUDAH DIBERIKAN", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
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
        jScrollPane1.setViewportView(tblTindakan);

        pnlTindakan.add(jScrollPane1);
        jScrollPane1.setBounds(10, 40, 690, 530);

        btnTindakanTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Tambah(small).png"))); // NOI18N
        btnTindakanTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanTambahActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanTambah);
        btnTindakanTambah.setBounds(720, 40, 80, 30);

        btnTindakanUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Update(small).png"))); // NOI18N
        btnTindakanUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUpdateActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanUpdate);
        btnTindakanUpdate.setBounds(720, 80, 80, 30);

        btnTindakanHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus(small).png"))); // NOI18N
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanHapus);
        btnTindakanHapus.setBounds(720, 120, 80, 30);

        getContentPane().add(pnlTindakan);
        pnlTindakan.setBounds(20, 180, 810, 580);

        pnlPasienDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlPasienDetail.setLayout(null);

        jLabel4.setText("No. Pasien");
        pnlPasienDetail.add(jLabel4);
        jLabel4.setBounds(20, 30, 100, 14);

        jLabel5.setText("NIK");
        pnlPasienDetail.add(jLabel5);
        jLabel5.setBounds(20, 70, 100, 25);

        jLabel6.setText("NAMA");
        pnlPasienDetail.add(jLabel6);
        jLabel6.setBounds(20, 100, 100, 25);

        jLabel7.setText("KELAMIN");
        pnlPasienDetail.add(jLabel7);
        jLabel7.setBounds(20, 130, 100, 25);

        jLabel8.setText("TANGGAL LAHIR");
        pnlPasienDetail.add(jLabel8);
        jLabel8.setBounds(20, 160, 100, 25);

        jLabel9.setText("GOL. DARAH");
        pnlPasienDetail.add(jLabel9);
        jLabel9.setBounds(20, 190, 100, 25);

        jLabel10.setText("AGAMA");
        pnlPasienDetail.add(jLabel10);
        jLabel10.setBounds(20, 220, 100, 25);

        jLabel11.setText("TELEPON");
        pnlPasienDetail.add(jLabel11);
        jLabel11.setBounds(20, 250, 100, 25);

        jLabel12.setText("TANGGUNGAN");
        pnlPasienDetail.add(jLabel12);
        jLabel12.setBounds(20, 280, 100, 25);

        jLabel16.setText("STATUS RAWAT");
        pnlPasienDetail.add(jLabel16);
        jLabel16.setBounds(20, 310, 100, 25);

        jLabel15.setText("TANGGAL MASUK");
        pnlPasienDetail.add(jLabel15);
        jLabel15.setBounds(20, 340, 100, 25);

        txtPasienKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        pnlPasienDetail.add(txtPasienKode);
        txtPasienKode.setBounds(140, 30, 240, 25);

        txtPasienNik.setEditable(false);
        txtPasienNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNik);
        txtPasienNik.setBounds(140, 70, 240, 25);

        txtPasienNama.setEditable(false);
        txtPasienNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNama);
        txtPasienNama.setBounds(140, 100, 240, 25);

        txtPasienTanggalLahir.setEditable(false);
        txtPasienTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(140, 160, 240, 25);

        txtPasienDarah.setEditable(false);
        txtPasienDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienDarah);
        txtPasienDarah.setBounds(140, 190, 240, 25);

        txtPasienAgama.setEditable(false);
        txtPasienAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienAgama);
        txtPasienAgama.setBounds(140, 220, 240, 25);

        txtPasienTelepon.setEditable(false);
        txtPasienTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(140, 250, 240, 25);

        txtPasienKelamin.setEditable(false);
        txtPasienKelamin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(140, 130, 240, 25);

        txtPasienStatus.setEditable(false);
        txtPasienStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienStatus);
        txtPasienStatus.setBounds(140, 310, 240, 25);

        txtPasienTanggalMasuk.setEditable(false);
        txtPasienTanggalMasuk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(140, 340, 240, 25);

        txtPasienTanggungan.setEditable(false);
        txtPasienTanggungan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(140, 280, 240, 25);
        pnlPasienDetail.add(jSeparator1);
        jSeparator1.setBounds(0, 62, 400, 10);

        jLabel21.setText("KELAS");
        pnlPasienDetail.add(jLabel21);
        jLabel21.setBounds(20, 370, 100, 25);

        txtPasienKelas.setEditable(false);
        pnlPasienDetail.add(txtPasienKelas);
        txtPasienKelas.setBounds(140, 370, 240, 25);

        getContentPane().add(pnlPasienDetail);
        pnlPasienDetail.setBounds(860, 260, 400, 410);

        pnlHomeDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Data Ruangan"));
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlHomeDetail.setLayout(null);

        jLabel26.setText("Jumlah Pasien");
        pnlHomeDetail.add(jLabel26);
        jLabel26.setBounds(20, 30, 90, 25);

        jLabel27.setText("VVIP");
        pnlHomeDetail.add(jLabel27);
        jLabel27.setBounds(20, 90, 90, 25);

        jLabel28.setText("VIP");
        pnlHomeDetail.add(jLabel28);
        jLabel28.setBounds(20, 130, 90, 25);

        jLabel29.setText("KELAS I");
        pnlHomeDetail.add(jLabel29);
        jLabel29.setBounds(20, 170, 90, 25);

        jLabel30.setText("KELAS II");
        pnlHomeDetail.add(jLabel30);
        jLabel30.setBounds(20, 210, 90, 25);

        jLabel31.setText("KELAS III");
        pnlHomeDetail.add(jLabel31);
        jLabel31.setBounds(20, 250, 90, 25);
        pnlHomeDetail.add(jSeparator3);
        jSeparator3.setBounds(0, 70, 400, 10);

        txtJumlahPasien.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasien);
        txtJumlahPasien.setBounds(120, 30, 260, 25);

        txtJumlahPasienKelas3.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas3);
        txtJumlahPasienKelas3.setBounds(120, 250, 260, 25);

        txtJumlahPasienVvip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVvip);
        txtJumlahPasienVvip.setBounds(120, 90, 260, 25);

        txtJumlahPasienVip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVip);
        txtJumlahPasienVip.setBounds(120, 130, 260, 25);

        txtJumlahPasienKelas1.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas1);
        txtJumlahPasienKelas1.setBounds(120, 170, 260, 25);

        txtJumlahPasienKelas2.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas2);
        txtJumlahPasienKelas2.setBounds(120, 210, 260, 25);

        getContentPane().add(pnlHomeDetail);
        pnlHomeDetail.setBounds(860, 260, 400, 290);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel17.setText("LOGIN SEBAGAI : ");
        jToolBar1.add(jLabel17);

        lblOperator.setText("jLabel31");
        jToolBar1.add(lblOperator);

        jLabel1.setText(" - ");
        jToolBar1.add(jLabel1);

        jLabel18.setText("UNIT :");
        jToolBar1.add(jLabel18);

        lblUnit.setText("jLabel1");
        jToolBar1.add(lblUnit);
        jToolBar1.add(jSeparator2);

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

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/SAL_Bg.jpg"))); // NOI18N
        getContentPane().add(jLabel19);
        jLabel19.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        pnlHome.setVisible(true);
        pnlHomeDetail.setVisible(true);

        pnlTindakan.setVisible(false);
        pnlPasienDetail.setVisible(false);

        reloadHome();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasukActionPerformed
        String kode = txtPasienMasuk.getText();
        
        try {
            if (kode.equals(""))
                throw new ComponentSelectionException("Silahkan masukan nomor pasien");
            
            Pasien p = pasienService.get(kode);
            Tindakan tindakan = tindakanService.get("Rawat Inap", p.getKelas());

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

    private void tblPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPasienMouseClicked
        Integer index = tblPasien.getSelectedRow();
        PasienTableModel tableModel = (PasienTableModel)tblPasien.getModel();

        pasien = tableModel.getPasien(index);
        
        txtPasienKeluar.setText(pasien.getKode());
        cbPasienKelas.setSelectedItem(pasien.getKelas());
    }//GEN-LAST:event_tblPasienMouseClicked

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

    private void btnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienActionPerformed
        pnlHome.setVisible(false);
        pnlHomeDetail.setVisible(false);

        pnlTindakan.setVisible(true);
        pnlPasienDetail.setVisible(true);
    }//GEN-LAST:event_btnPasienActionPerformed

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
            txtPasienTanggalMasuk.setText(pasien.getTanggalMasuk().toString());
            txtPasienKelas.setText(pasien.getKelas().toString());
            
            loadTindakan(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    private void btnTindakanTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanTambahActionPerformed
        new FrameTambahObject(this, Tindakan.class, pasien).setVisible(true);
    }//GEN-LAST:event_btnTindakanTambahActionPerformed

    private void btnTindakanUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanUpdateActionPerformed
        try {
            Pelayanan pelayanan = getPelayanan();
            
            new FrameTambahObject(this, pasien, pelayanan).setVisible(true);
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanUpdateActionPerformed

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

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameLogin().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dialogKeluar.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dialogKeluar.setSize(280, 160);

        txtBiayaTambahan.setText("0");
        txtKeterangan.setText("-");

        dialogKeluar.setVisible(true);
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnSimpanKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanKelasActionPerformed
        String kelas = (String) cbPasienKelas.getSelectedItem();
        if (kelas.equals("- Pilih -"))
            return;

        try {
            pasienService.ubahKelas(pasien, Kelas.valueOf(kelas));
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanKelasActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMasuk;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JButton btnSimpanKelas;
    private javax.swing.JButton btnTindakanHapus;
    private javax.swing.JButton btnTindakanTambah;
    private javax.swing.JButton btnTindakanUpdate;
    private javax.swing.JComboBox cbPasienKelas;
    private javax.swing.JFrame dialogKeluar;
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
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeDetail;
    private javax.swing.JPanel pnlKelas;
    private javax.swing.JPanel pnlKeluar;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMasuk;
    private javax.swing.JPanel pnlPasienDetail;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JScrollPane scrollPasien;
    private javax.swing.JTable tblPasien;
    private javax.swing.JTable tblTindakan;
    private javax.swing.JTextField txtBiayaTambahan;
    private javax.swing.JTextField txtJumlahPasien;
    private javax.swing.JTextField txtJumlahPasienKelas1;
    private javax.swing.JTextField txtJumlahPasienKelas2;
    private javax.swing.JTextField txtJumlahPasienKelas3;
    private javax.swing.JTextField txtJumlahPasienVip;
    private javax.swing.JTextField txtJumlahPasienVvip;
    private javax.swing.JTextField txtKeterangan;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKelas;
    private javax.swing.JTextField txtPasienKeluar;
    private javax.swing.JTextField txtPasienKode;
    private javax.swing.JTextField txtPasienMasuk;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienStatus;
    private javax.swing.JTextField txtPasienTanggalLahir;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPasienTelepon;
    // End of variables declaration//GEN-END:variables
}
