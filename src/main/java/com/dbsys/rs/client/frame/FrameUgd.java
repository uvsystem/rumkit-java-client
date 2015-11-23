package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.ComponentSelectionException;
import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.TindakanTableFrame;
import com.dbsys.rs.client.UnitFrame;
import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
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
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PelayananTemporal;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Tindakan;
import com.dbsys.rs.lib.entity.Unit;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class FrameUgd extends javax.swing.JFrame implements TindakanTableFrame, UnitFrame {

    private final PendudukService pendudukService = PendudukService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final TokenService tokenService= TokenService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);
    private final PemakaianService pemakaianBhpService = PemakaianService.getInstance(EventController.host);
    private final TindakanService tindakanService = TindakanService.getInstance(EventController.host);

    private Penduduk penduduk;
    private Pasien pasien;
    private Unit tujuan;
    
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
        scrollPenduduk.setVisible(true);
        
        pnlTindakan.setVisible(false);
        pnlPasienDetail.setVisible(false);
        
        pnlHome.setVisible(false);
        pnlHomeDetail.setVisible(false);
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

    private void loadTindakan(Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<Pelayanan> list = pelayananService.getByPasien(pasien);

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

    @Override
    public void setUnit(Unit unit) {
        tujuan = unit;
        
        txtPasienTujuan.setText(tujuan.getNama());
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
        btnKeluar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        pnlTindakan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        btnTindakanSimpan = new javax.swing.JButton();
        btnTindakanUpdate = new javax.swing.JButton();
        btnTindakanHapus = new javax.swing.JButton();
        pnlHome = new javax.swing.JPanel();
        scrollPasien = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        pnlKeluar = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        txtPasienKeluar = new javax.swing.JTextField();
        btnPasienKeluar = new javax.swing.JButton();
        pnlMasuk = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPasienMasuk = new javax.swing.JTextField();
        btnPasienMasuk = new javax.swing.JButton();
        scrollPenduduk = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
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
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtPasienNomor = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienTujuan = new javax.swing.JTextField();
        cbPasienKelas = new javax.swing.JComboBox();
        cbPasienTanggungan = new javax.swing.JComboBox();
        btnPasienTambah = new javax.swing.JButton();
        btnCetakStatus = new javax.swing.JButton();
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

        btnKeluar.setText("Pasien Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        dialogKeluar.getContentPane().add(btnKeluar);
        btnKeluar.setBounds(163, 90, 97, 30);

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
        tblTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTindakanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTindakan);

        pnlTindakan.add(jScrollPane1);
        jScrollPane1.setBounds(10, 40, 690, 510);

        btnTindakanSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTindakanSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanSimpanActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanSimpan);
        btnTindakanSimpan.setBounds(710, 40, 90, 35);

        btnTindakanUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Update(small).png"))); // NOI18N
        btnTindakanUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUpdateActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanUpdate);
        btnTindakanUpdate.setBounds(710, 80, 90, 35);

        btnTindakanHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus(small).png"))); // NOI18N
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanHapus);
        btnTindakanHapus.setBounds(710, 120, 90, 35);

        getContentPane().add(pnlTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 810, 560));

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
        scrollPasien.setBounds(10, 80, 790, 470);

        pnlKeluar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN KELUAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlKeluar.setLayout(null);

        jLabel27.setText("No. Pasien");
        pnlKeluar.add(jLabel27);
        jLabel27.setBounds(20, 20, 90, 25);
        pnlKeluar.add(txtPasienKeluar);
        txtPasienKeluar.setBounds(110, 20, 160, 25);

        btnPasienKeluar.setText("KELUAR");
        btnPasienKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluarActionPerformed(evt);
            }
        });
        pnlKeluar.add(btnPasienKeluar);
        btnPasienKeluar.setBounds(280, 18, 90, 30);

        pnlHome.add(pnlKeluar);
        pnlKeluar.setBounds(410, 10, 390, 60);

        pnlMasuk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN MASUK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlMasuk.setLayout(null);

        jLabel2.setText("No. Pasien");
        pnlMasuk.add(jLabel2);
        jLabel2.setBounds(20, 20, 90, 25);
        pnlMasuk.add(txtPasienMasuk);
        txtPasienMasuk.setBounds(110, 20, 160, 25);

        btnPasienMasuk.setText("MASUK");
        btnPasienMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienMasukActionPerformed(evt);
            }
        });
        pnlMasuk.add(btnPasienMasuk);
        btnPasienMasuk.setBounds(280, 18, 90, 30);

        pnlHome.add(pnlMasuk);
        pnlMasuk.setBounds(10, 10, 390, 60);

        getContentPane().add(pnlHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 810, 560));

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

        getContentPane().add(scrollPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 810, 560));

        pnlPasienDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlPasienDetail.setLayout(null);

        jLabel14.setText("No. Pasien");
        pnlPasienDetail.add(jLabel14);
        jLabel14.setBounds(20, 30, 90, 25);

        jLabel17.setText("NIK");
        pnlPasienDetail.add(jLabel17);
        jLabel17.setBounds(20, 90, 90, 25);

        jLabel18.setText("NAMA");
        pnlPasienDetail.add(jLabel18);
        jLabel18.setBounds(20, 120, 90, 25);

        jLabel19.setText("KELAMIN");
        pnlPasienDetail.add(jLabel19);
        jLabel19.setBounds(20, 150, 90, 25);

        jLabel20.setText("TANGGAL LAHIR");
        pnlPasienDetail.add(jLabel20);
        jLabel20.setBounds(20, 180, 90, 25);

        jLabel21.setText("GOL. DARAH");
        pnlPasienDetail.add(jLabel21);
        jLabel21.setBounds(20, 210, 90, 25);

        jLabel22.setText("AGAMA");
        pnlPasienDetail.add(jLabel22);
        jLabel22.setBounds(20, 240, 90, 25);

        jLabel23.setText("TELEPON");
        pnlPasienDetail.add(jLabel23);
        jLabel23.setBounds(20, 270, 90, 25);

        jLabel24.setText("TANGGUNGAN");
        pnlPasienDetail.add(jLabel24);
        jLabel24.setBounds(20, 300, 90, 25);

        jLabel25.setText("STATUS RAWAT");
        pnlPasienDetail.add(jLabel25);
        jLabel25.setBounds(20, 330, 90, 25);

        jLabel26.setText("KELAS");
        pnlPasienDetail.add(jLabel26);
        jLabel26.setBounds(20, 390, 90, 25);

        jLabel39.setText("TANGGAL MASUK");
        pnlPasienDetail.add(jLabel39);
        jLabel39.setBounds(20, 360, 90, 25);

        txtPasienKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        pnlPasienDetail.add(txtPasienKode);
        txtPasienKode.setBounds(120, 30, 250, 25);

        txtPasienNik.setEditable(false);
        txtPasienNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNik);
        txtPasienNik.setBounds(120, 90, 250, 25);

        txtPasienNama.setEditable(false);
        txtPasienNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNama);
        txtPasienNama.setBounds(120, 120, 250, 25);

        txtPasienTanggalLahir.setEditable(false);
        txtPasienTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(120, 180, 250, 25);

        txtPasienDarah.setEditable(false);
        txtPasienDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienDarah);
        txtPasienDarah.setBounds(120, 210, 250, 25);

        txtPasienAgama.setEditable(false);
        txtPasienAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienAgama);
        txtPasienAgama.setBounds(120, 240, 250, 25);

        txtPasienTelepon.setEditable(false);
        txtPasienTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(120, 270, 250, 25);

        txtPasienKelamin.setEditable(false);
        txtPasienKelamin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(120, 150, 250, 25);

        txtPasienStatus.setEditable(false);
        txtPasienStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienStatus);
        txtPasienStatus.setBounds(120, 330, 250, 25);

        txtPasienTanggalMasukDetail.setEditable(false);
        txtPasienTanggalMasukDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalMasukDetail);
        txtPasienTanggalMasukDetail.setBounds(120, 360, 250, 25);

        txtPasienTanggungan.setEditable(false);
        txtPasienTanggungan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(120, 300, 250, 25);

        txtPasienKelas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienKelas);
        txtPasienKelas.setBounds(120, 390, 250, 25);
        pnlPasienDetail.add(jSeparator2);
        jSeparator2.setBounds(0, 62, 400, 10);

        getContentPane().add(pnlPasienDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 110, 400, 430));

        pnlPendaftaranCari.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PENCARIAN REKAM MEDIK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPendaftaranCari.setLayout(null);
        pnlPendaftaranCari.setBackground(new Color(0, 0, 0, 20));

        jLabel13.setText("Kata Kunci");
        pnlPendaftaranCari.add(jLabel13);
        jLabel13.setBounds(20, 25, 140, 25);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        pnlPendaftaranCari.add(txtKeyword);
        txtKeyword.setBounds(170, 25, 210, 25);

        getContentPane().add(pnlPendaftaranCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 110, 400, 60));

        pnlPendaftaranDetailPenduduk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA REKAM MEDIK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPendaftaranDetailPenduduk.setBackground(new Color(0,0,0,20));
        pnlPendaftaranDetailPenduduk.setLayout(null);

        jLabel4.setText("NOMOR");
        pnlPendaftaranDetailPenduduk.add(jLabel4);
        jLabel4.setBounds(20, 30, 140, 25);

        jLabel5.setText("NIK");
        pnlPendaftaranDetailPenduduk.add(jLabel5);
        jLabel5.setBounds(20, 60, 140, 25);

        jLabel6.setText("NAMA");
        pnlPendaftaranDetailPenduduk.add(jLabel6);
        jLabel6.setBounds(20, 90, 140, 25);

        jLabel7.setText("KELAMIN");
        pnlPendaftaranDetailPenduduk.add(jLabel7);
        jLabel7.setBounds(20, 120, 140, 25);

        jLabel8.setText("TANGGAL LAHIR");
        pnlPendaftaranDetailPenduduk.add(jLabel8);
        jLabel8.setBounds(20, 150, 140, 25);

        jLabel9.setText("GOL. DARAH");
        pnlPendaftaranDetailPenduduk.add(jLabel9);
        jLabel9.setBounds(20, 180, 140, 25);

        jLabel10.setText("AGAMA");
        pnlPendaftaranDetailPenduduk.add(jLabel10);
        jLabel10.setBounds(20, 210, 140, 25);

        jLabel11.setText("TELEPON");
        pnlPendaftaranDetailPenduduk.add(jLabel11);
        jLabel11.setBounds(20, 240, 140, 25);

        txtPendudukKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukKode);
        txtPendudukKode.setBounds(170, 30, 210, 25);

        txtPendudukNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukNik);
        txtPendudukNik.setBounds(169, 60, 210, 25);

        txtPendudukNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukNama);
        txtPendudukNama.setBounds(169, 90, 210, 25);

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        cbPendudukKelamin.setBorder(null);
        pnlPendaftaranDetailPenduduk.add(cbPendudukKelamin);
        cbPendudukKelamin.setBounds(170, 120, 210, 25);

        txtPendudukTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukTanggalLahir);
        txtPendudukTanggalLahir.setBounds(169, 150, 210, 25);

        txtPendudukDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukDarah);
        txtPendudukDarah.setBounds(169, 180, 210, 25);

        txtPendudukAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukAgama);
        txtPendudukAgama.setBounds(169, 210, 210, 25);

        txtPendudukTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetailPenduduk.add(txtPendudukTelepon);
        txtPendudukTelepon.setBounds(169, 240, 210, 25);

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

        getContentPane().add(pnlPendaftaranDetailPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 170, 400, 320));

        pnlPendaftaranDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPendaftaranDetail.setLayout(null);
        pnlPendaftaranDetail.setBackground(new Color(0,0,0,20));

        jLabel37.setText("TANGGAL MASUK");
        pnlPendaftaranDetail.add(jLabel37);
        jLabel37.setBounds(20, 60, 90, 25);

        jLabel38.setText("UNIT TUJUAN");
        pnlPendaftaranDetail.add(jLabel38);
        jLabel38.setBounds(20, 90, 90, 25);

        jLabel16.setText("NOMOR PASIEN");
        pnlPendaftaranDetail.add(jLabel16);
        jLabel16.setBounds(20, 30, 140, 25);

        jLabel15.setText("KELAS");
        pnlPendaftaranDetail.add(jLabel15);
        jLabel15.setBounds(20, 120, 90, 25);

        jLabel12.setText("TANGGUNGAN");
        pnlPendaftaranDetail.add(jLabel12);
        jLabel12.setBounds(20, 150, 90, 25);

        txtPasienNomor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetail.add(txtPasienNomor);
        txtPasienNomor.setBounds(170, 30, 210, 25);

        txtPasienTanggalMasuk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPendaftaranDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(120, 60, 250, 25);

        txtPasienTujuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPasienTujuanMouseClicked(evt);
            }
        });
        pnlPendaftaranDetail.add(txtPasienTujuan);
        txtPasienTujuan.setBounds(120, 90, 250, 25);

        cbPasienKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "VVIP", "VIP", "I", "II", "III" }));
        pnlPendaftaranDetail.add(cbPasienKelas);
        cbPasienKelas.setBounds(120, 120, 250, 25);

        cbPasienTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        cbPasienTanggungan.setBorder(null);
        pnlPendaftaranDetail.add(cbPasienTanggungan);
        cbPasienTanggungan.setBounds(120, 150, 250, 25);

        btnPasienTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPasienTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienTambahActionPerformed(evt);
            }
        });
        pnlPendaftaranDetail.add(btnPasienTambah);
        btnPasienTambah.setBounds(170, 200, 80, 30);

        btnCetakStatus.setText("Cetak Status");
        btnCetakStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakStatusActionPerformed(evt);
            }
        });
        pnlPendaftaranDetail.add(btnCetakStatus);
        btnCetakStatus.setBounds(280, 200, 95, 30);

        getContentPane().add(pnlPendaftaranDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 500, 400, 240));

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

        getContentPane().add(pnlHomeDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 110, 400, 300));

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
        getContentPane().add(btnPendaftaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, 120, 50));

        btnPasien.setText("PASIEN");
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        getContentPane().add(btnPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 120, 50));

        btnRuangan.setText("RUANGAN");
        btnRuangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRuanganActionPerformed(evt);
            }
        });
        getContentPane().add(btnRuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 120, 50));

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
        tujuan = null;
        
        txtPendudukKode.setText(null);
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
            
            Pasien pasienValue = pasienService.daftar(penduduk, Penanggung.valueOf(tanggungan), DateUtil.getDate(tanggalMasuk), kode, Pasien.Pendaftaran.UGD, Kelas.valueOf(kelas), tujuan);
            JOptionPane.showMessageDialog(this, "Berhasil menyimpan data pasien.");
            
            txtPasienNomor.setText(pasienValue.getKode());
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
        scrollPenduduk.setVisible(false);
        
        pnlTindakan.setVisible(true);
        pnlPasienDetail.setVisible(true);
        
        pnlHome.setVisible(false);
        pnlHomeDetail.setVisible(false);
    }//GEN-LAST:event_btnPasienActionPerformed

    private void btnRuanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRuanganActionPerformed
        pnlPendaftaranCari.setVisible(false);
        pnlPendaftaranDetail.setVisible(false);
        pnlPendaftaranDetailPenduduk.setVisible(false);
        scrollPenduduk.setVisible(false);
        
        pnlTindakan.setVisible(false);
        pnlPasienDetail.setVisible(false);
        
        pnlHome.setVisible(true);
        pnlHomeDetail.setVisible(true);
        
        reloadHome();
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
                reloadTable();
            }
        } catch (ComponentSelectionException | ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanHapusActionPerformed

    private void btnTindakanSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanSimpanActionPerformed
        new FrameTambahObject(this, Tindakan.class, pasien).setVisible(true);
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

            new FrameTambahObject(this, pasien, pelayanan).setVisible(true);
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

        Pasien p = tableModel.getPasien(index);
        txtPasienKeluar.setText(p.getKode());
    }//GEN-LAST:event_tblPasienMouseClicked

    private void btnPasienMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienMasukActionPerformed
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
    }//GEN-LAST:event_btnPasienMasukActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
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
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dialogKeluar.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPasienTujuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPasienTujuanMouseClicked
        FrameCari frameCari = new FrameCari(this, Unit.class);
        frameCari.setVisible(true);
    }//GEN-LAST:event_txtPasienTujuanMouseClicked

    private void btnPasienKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienKeluarActionPerformed
        dialogKeluar.setSize(280, 160);

        txtBiayaTambahan.setText("0");
        txtKeterangan.setText("-");

        dialogKeluar.setVisible(true);
    }//GEN-LAST:event_btnPasienKeluarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Image;
    private javax.swing.JButton btnCetakStatus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JButton btnPasienMasuk;
    private javax.swing.JButton btnPasienTambah;
    private javax.swing.JButton btnPendaftaran;
    private javax.swing.JButton btnPendudukClean;
    private javax.swing.JButton btnPendudukSimpan;
    private javax.swing.JButton btnRuangan;
    private javax.swing.JButton btnTindakanHapus;
    private javax.swing.JButton btnTindakanSimpan;
    private javax.swing.JButton btnTindakanUpdate;
    private javax.swing.JComboBox cbPasienKelas;
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
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel pnlPendaftaranCari;
    private javax.swing.JPanel pnlPendaftaranDetail;
    private javax.swing.JPanel pnlPendaftaranDetailPenduduk;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JScrollPane scrollPasien;
    private javax.swing.JScrollPane scrollPenduduk;
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
    private javax.swing.JTextField txtPasienKelas;
    private javax.swing.JTextField txtPasienKeluar;
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
