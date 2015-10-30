package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianBhpService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PelayananTemporal;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.Tindakan;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 */
public class FrameSal extends javax.swing.JFrame implements BhpTableFrame, TindakanTableFrame {

    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);
    private final PemakaianBhpService pemakaianBhpService = PemakaianBhpService.getInstance(EventController.host);
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

        panePasien.setVisible(false);
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

    private void loadTindakan(Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<Pelayanan> list = pelayananService.getByPasien(pasien.getId());

        PelayananTableModel tableModel = new PelayananTableModel(list);
        tblTindakan.setModel(tableModel);
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
    
    private Pelayanan getPelayanan() throws ComponentSelectionException {
        int index = tblTindakan.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PelayananTableModel tableModel = (PelayananTableModel)tblTindakan.getModel();
        return tableModel.getPelayanan(index);
    }
    
    private void loadBhp(Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<PemakaianBhp> listPemakaianBhp = pemakaianBhpService.getByPasien(pasien.getId());

        List<Pemakaian> listPemakaian = new ArrayList<>();
        for (Pemakaian pemakaian : listPemakaianBhp)
            listPemakaian.add(pemakaian);

        PemakaianTableModel tableModel = new PemakaianTableModel(listPemakaian);
        tblBhp.setModel(tableModel);
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
    
    private PemakaianBhp getPemakaianBhp() throws ComponentSelectionException {
        int index = tblBhp.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PemakaianTableModel tableModel = (PemakaianTableModel)tblBhp.getModel();
        return (PemakaianBhp)tableModel.getPemakaian(index);
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
        jLabel14 = new javax.swing.JLabel();
        txtKeterangan = new javax.swing.JTextField();
        txtBiayaTambahan = new javax.swing.JTextField();
        btnPasienKeluar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnPasien = new javax.swing.JButton();
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
        pnlHome = new javax.swing.JPanel();
        scrollPasien = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        pnlMasuk = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPasienMasuk = new javax.swing.JTextField();
        cbKelas = new javax.swing.JComboBox();
        btnMasuk = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        panePasien = new javax.swing.JTabbedPane();
        pnlTindakan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnTindakanHapus = new javax.swing.JButton();
        btnTindakanTambah = new javax.swing.JButton();
        btnTindakanUpdate = new javax.swing.JButton();
        pnlBhp = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnBhpHapus = new javax.swing.JButton();
        btnBhpTambah = new javax.swing.JButton();
        btnBhpUpdate = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel17 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();

        dialogKeluar.setUndecorated(true);
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

        btnHome.setText("HOME");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        pnlMain.add(btnHome);
        btnHome.setBounds(10, 10, 150, 44);

        btnPasien.setText("PASIEN");
        btnPasien.setPreferredSize(new java.awt.Dimension(105, 23));
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        pnlMain.add(btnPasien);
        btnPasien.setBounds(170, 10, 150, 44);

        getContentPane().add(pnlMain);
        pnlMain.setBounds(10, 130, 330, 70);

        pnlHomeDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Data Ruangan"));
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlHomeDetail.setLayout(null);

        jLabel26.setText("Jumlah Pasien");
        pnlHomeDetail.add(jLabel26);
        jLabel26.setBounds(20, 30, 70, 14);

        jLabel27.setText("VVIP");
        pnlHomeDetail.add(jLabel27);
        jLabel27.setBounds(20, 110, 40, 14);

        jLabel28.setText("VIP");
        pnlHomeDetail.add(jLabel28);
        jLabel28.setBounds(20, 150, 40, 14);

        jLabel29.setText("KELAS I");
        pnlHomeDetail.add(jLabel29);
        jLabel29.setBounds(20, 190, 40, 14);

        jLabel30.setText("KELAS II");
        pnlHomeDetail.add(jLabel30);
        jLabel30.setBounds(20, 230, 50, 14);

        jLabel31.setText("KELAS III");
        pnlHomeDetail.add(jLabel31);
        jLabel31.setBounds(20, 270, 50, 14);
        pnlHomeDetail.add(jSeparator3);
        jSeparator3.setBounds(0, 70, 330, 10);

        txtJumlahPasien.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasien);
        txtJumlahPasien.setBounds(110, 30, 200, 20);

        txtJumlahPasienKelas3.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas3);
        txtJumlahPasienKelas3.setBounds(110, 270, 200, 20);

        txtJumlahPasienVvip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVvip);
        txtJumlahPasienVvip.setBounds(110, 110, 200, 20);

        txtJumlahPasienVip.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienVip);
        txtJumlahPasienVip.setBounds(110, 150, 200, 20);

        txtJumlahPasienKelas1.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas1);
        txtJumlahPasienKelas1.setBounds(110, 190, 200, 20);

        txtJumlahPasienKelas2.setEditable(false);
        pnlHomeDetail.add(txtJumlahPasienKelas2);
        txtJumlahPasienKelas2.setBounds(110, 230, 200, 20);

        getContentPane().add(pnlHomeDetail);
        pnlHomeDetail.setBounds(10, 220, 330, 410);

        pnlPasienDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Detail Pasien"));
        pnlPasienDetail.setBackground(new Color(0,0,0,20));
        pnlPasienDetail.setLayout(null);

        jLabel4.setText("No. Register Pasien");
        pnlPasienDetail.add(jLabel4);
        jLabel4.setBounds(20, 30, 100, 14);

        jLabel5.setText("NIK");
        pnlPasienDetail.add(jLabel5);
        jLabel5.setBounds(20, 90, 40, 14);

        jLabel6.setText("NAMA");
        pnlPasienDetail.add(jLabel6);
        jLabel6.setBounds(20, 120, 50, 14);

        jLabel7.setText("KELAMIN");
        pnlPasienDetail.add(jLabel7);
        jLabel7.setBounds(20, 150, 60, 14);

        jLabel8.setText("TANGGAL LAHIR");
        pnlPasienDetail.add(jLabel8);
        jLabel8.setBounds(20, 180, 90, 14);

        jLabel9.setText("GOL. DARAH");
        pnlPasienDetail.add(jLabel9);
        jLabel9.setBounds(20, 210, 80, 14);

        jLabel10.setText("AGAMA");
        pnlPasienDetail.add(jLabel10);
        jLabel10.setBounds(20, 240, 50, 14);

        jLabel11.setText("TELEPON");
        pnlPasienDetail.add(jLabel11);
        jLabel11.setBounds(20, 270, 70, 14);

        jLabel12.setText("TANGGUNGAN");
        pnlPasienDetail.add(jLabel12);
        jLabel12.setBounds(20, 300, 90, 14);

        jLabel16.setText("STATUS RAWAT");
        pnlPasienDetail.add(jLabel16);
        jLabel16.setBounds(20, 330, 90, 14);

        jLabel15.setText("TANGGAL MASUK");
        pnlPasienDetail.add(jLabel15);
        jLabel15.setBounds(20, 360, 90, 14);

        txtPasienKode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        pnlPasienDetail.add(txtPasienKode);
        txtPasienKode.setBounds(120, 30, 180, 20);

        txtPasienNik.setEditable(false);
        txtPasienNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNik);
        txtPasienNik.setBounds(120, 90, 179, 20);

        txtPasienNama.setEditable(false);
        txtPasienNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienNama);
        txtPasienNama.setBounds(120, 120, 179, 20);

        txtPasienTanggalLahir.setEditable(false);
        txtPasienTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(120, 180, 179, 20);

        txtPasienDarah.setEditable(false);
        txtPasienDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienDarah);
        txtPasienDarah.setBounds(120, 210, 179, 20);

        txtPasienAgama.setEditable(false);
        txtPasienAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienAgama);
        txtPasienAgama.setBounds(120, 240, 179, 20);

        txtPasienTelepon.setEditable(false);
        txtPasienTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(120, 270, 179, 20);

        txtPasienKelamin.setEditable(false);
        txtPasienKelamin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(120, 150, 179, 20);

        txtPasienStatus.setEditable(false);
        txtPasienStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienStatus);
        txtPasienStatus.setBounds(120, 330, 180, 18);

        txtPasienTanggalMasuk.setEditable(false);
        txtPasienTanggalMasuk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(120, 360, 180, 18);

        txtPasienTanggungan.setEditable(false);
        txtPasienTanggungan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlPasienDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(120, 300, 180, 18);
        pnlPasienDetail.add(jSeparator1);
        jSeparator1.setBounds(0, 62, 330, 10);

        getContentPane().add(pnlPasienDetail);
        pnlPasienDetail.setBounds(10, 220, 330, 410);

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
        scrollPasien.setBounds(10, 100, 600, 390);

        pnlMasuk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pasien Masuk"));
        pnlMasuk.setLayout(null);

        jLabel2.setText("No. Pasien");
        pnlMasuk.add(jLabel2);
        jLabel2.setBounds(10, 20, 60, 20);
        pnlMasuk.add(txtPasienMasuk);
        txtPasienMasuk.setBounds(90, 20, 160, 20);

        cbKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "NONE", "VVIP", "VIP", "I", "II", "III", "ICU" }));
        pnlMasuk.add(cbKelas);
        cbKelas.setBounds(350, 20, 160, 20);

        btnMasuk.setText("MASUK");
        btnMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasukActionPerformed(evt);
            }
        });
        pnlMasuk.add(btnMasuk);
        btnMasuk.setBounds(520, 20, 73, 23);

        jLabel13.setText("Kelas");
        pnlMasuk.add(jLabel13);
        jLabel13.setBounds(300, 20, 25, 14);

        pnlHome.add(pnlMasuk);
        pnlMasuk.setBounds(10, 10, 600, 60);

        getContentPane().add(pnlHome);
        pnlHome.setBounds(360, 130, 620, 500);

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
        jScrollPane1.setViewportView(tblTindakan);

        pnlTindakan.add(jScrollPane1);
        jScrollPane1.setBounds(10, 11, 590, 390);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        btnTindakanHapus.setText("HAPUS");
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        jPanel3.add(btnTindakanHapus);
        btnTindakanHapus.setBounds(190, 10, 65, 23);

        btnTindakanTambah.setText("TAMBAH");
        btnTindakanTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanTambahActionPerformed(evt);
            }
        });
        jPanel3.add(btnTindakanTambah);
        btnTindakanTambah.setBounds(10, 10, 73, 23);

        btnTindakanUpdate.setText("UPDATE");
        btnTindakanUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUpdateActionPerformed(evt);
            }
        });
        jPanel3.add(btnTindakanUpdate);
        btnTindakanUpdate.setBounds(100, 10, 71, 23);

        pnlTindakan.add(jPanel3);
        jPanel3.setBounds(10, 410, 590, 50);

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
        jScrollPane2.setBounds(10, 11, 590, 410);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        btnBhpHapus.setText("HAPUS");
        btnBhpHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpHapusActionPerformed(evt);
            }
        });
        jPanel4.add(btnBhpHapus);
        btnBhpHapus.setBounds(190, 10, 65, 23);

        btnBhpTambah.setText("TAMBAH");
        btnBhpTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpTambahActionPerformed(evt);
            }
        });
        jPanel4.add(btnBhpTambah);
        btnBhpTambah.setBounds(10, 10, 73, 23);

        btnBhpUpdate.setText("UPDATE");
        btnBhpUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpUpdateActionPerformed(evt);
            }
        });
        jPanel4.add(btnBhpUpdate);
        btnBhpUpdate.setBounds(100, 10, 71, 23);

        pnlBhp.add(jPanel4);
        jPanel4.setBounds(10, 430, 590, 50);

        panePasien.addTab("BAHAN HABIS PAKAI", pnlBhp);

        getContentPane().add(panePasien);
        panePasien.setBounds(360, 130, 620, 500);

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
        jToolBar1.setBounds(0, 653, 990, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        pnlHome.setVisible(true);
        pnlHomeDetail.setVisible(true);

        panePasien.setVisible(false);
        pnlPasienDetail.setVisible(false);

        reloadHome();
    }//GEN-LAST:event_btnHomeActionPerformed

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

    private void tblPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPasienMouseClicked
        Point point = evt.getLocationOnScreen();
        dialogKeluar.setBounds(point.x, point.y, 280, 140);

        txtBiayaTambahan.setText("0");
        txtKeterangan.setText("-");

        dialogKeluar.setVisible(true);
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

        panePasien.setVisible(true);
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
            txtPasienTanggungan.setText(pasien.getTanggungan().toString());
            txtPasienStatus.setText(pasien.getStatus().toString());
            txtPasienTanggalMasuk.setText(pasien.getTanggalMasuk().toString());
            
            loadTindakan(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

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

    private void btnBhpTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpTambahActionPerformed
        new FrameTambahObject(this, BahanHabisPakai.class, pasien).setVisible(true);
    }//GEN-LAST:event_btnBhpTambahActionPerformed

    private void btnBhpUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpUpdateActionPerformed
        try {
            PemakaianBhp pemakaianBhp = getPemakaianBhp();
            
            new FrameTambahObject(this, pasien, pemakaianBhp).setVisible(true);
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpUpdateActionPerformed

    private void btnBhpHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpHapusActionPerformed
        try {
            PemakaianBhp pemakaianBhp = getPemakaianBhp();

            int pilihan = JOptionPane.showConfirmDialog(this, String.format("Anda yakin ingin menghapus pemakaian %s pada tanggal %s", 
                    pemakaianBhp.getBarang().getNama(), pemakaianBhp.getTanggal()));

            if (JOptionPane.YES_OPTION == pilihan) {
                JOptionPane.showMessageDialog(this, "Belum bisa");
            }
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpHapusActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBhpHapus;
    private javax.swing.JButton btnBhpTambah;
    private javax.swing.JButton btnBhpUpdate;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMasuk;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JButton btnTindakanHapus;
    private javax.swing.JButton btnTindakanTambah;
    private javax.swing.JButton btnTindakanUpdate;
    private javax.swing.JComboBox cbKelas;
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
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JTabbedPane panePasien;
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeDetail;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMasuk;
    private javax.swing.JPanel pnlPasienDetail;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JScrollPane scrollPasien;
    private javax.swing.JTable tblBhp;
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
