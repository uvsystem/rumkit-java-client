package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.BhpTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.service.BhpService;
import com.dbsys.rs.connector.service.ObatService;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PemakaianObatService;
import com.dbsys.rs.connector.service.StokKeluarService;
import com.dbsys.rs.connector.service.StokMasukService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianObat;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class FrameFarmasi extends javax.swing.JFrame implements ObatTableFrame {

    private final BhpService bhpService = BhpService.getInstance(EventController.host);
    private final ObatService obatService = ObatService.getInstance(EventController.host);
    private final StokMasukService stokMasukService = StokMasukService.getInstance(EventController.host);
    private final StokKeluarService stokKeluarService = StokKeluarService.getInstance(EventController.host);
    private final PemakaianObatService pemakaianObatService = PemakaianObatService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    
    private Barang barang;
    private Pasien pasien;

    /**
     * Creates new FrameFarmasi
     */
    public FrameFarmasi() {
        initComponents();
        
        paneBarang.setVisible(false);
        pnlResep.setVisible(true);
        
        setDetailPasien(pasien);
        
        JOptionPane.showMessageDialog(null, "Silahkan masukan Nomor Pasien");
    }

    private void setDetailBhp(BahanHabisPakai bhp) {
        if (bhp == null)
            bhp = new BahanHabisPakai();
        
        this.txtBhpKode.setText(bhp.getKode());
        this.txtBhpNama.setText(bhp.getNama());
        this.txtBhpHarga.setText(bhp.getHarga().toString());
        this.txtBhpTanggungan.setText(bhp.getTanggungan().toString());
        this.txtBhpJumlah.setText(bhp.getJumlah().toString());
        this.txtBhpSatuan.setText(bhp.getSatuan());

        this.txtBhpStokJumlah.setText("0");
        this.txtBhpStokTanggal.setText(DateUtil.getDate().toString());
        this.txtBhpStokJam.setText(DateUtil.getTime().toString());
        
        tblBhp.removeAll();
        
        this.barang = bhp;
    }

    private void setDetailObat(ObatFarmasi obat) {
        if (obat == null)
            obat = new ObatFarmasi();
        
        this.txtObatKode.setText(obat.getKode());
        this.txtObatNama.setText(obat.getNama());
        this.txtObatHarga.setText(obat.getHarga().toString());
        this.txtObatTanggungan.setText(obat.getTanggungan().toString());
        this.txtObatJumlah.setText(obat.getJumlah().toString());
        this.txtObatSatuan.setText(obat.getSatuan());

        this.txtObatStokJumlah.setText("0");
        this.txtObatStokTanggal.setText(DateUtil.getDate().toString());
        this.txtObatStokJam.setText(DateUtil.getTime().toString());
        
        tblObat.removeAll();
        
        this.barang = obat;
    }

    private void setDetailPasien(Pasien pasien) {
        if (pasien == null)
            pasien = new Pasien();
        
        txtPasienNik.setText(pasien.getNik());
        txtPasienNama.setText(pasien.getNama());
        txtPasienKelamin.setText(pasien.getKelamin().toString());
        txtPasienTanggalLahir.setText(pasien.getTanggalLahir().toString());
        txtPasienGolonganDarah.setText(pasien.getDarah());
        txtPasienAgama.setText(pasien.getAgama());
        txtPasienTelepon.setText(pasien.getTelepon());
        txtPasienTanggungan.setText(pasien.getTanggungan().toString());
        txtPasienStatusRawat.setText(pasien.getStatus().toString());
        txtPasienTanggalMasuk.setText(pasien.getTanggalMasuk().toString());
        
        tblResepObat.removeAll();
        
        this.pasien = pasien;
    }
    
    private void simpanStokMasuk(Long idBarang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
        stokMasukService.simpan(idBarang, jumlah, tanggal, jam);
    }
    
    private void simpanStokKeluar(Long idBarang, Long jumlah, Date tanggal, Time jam) throws ServiceException {
        stokKeluarService.simpan(idBarang, jumlah, tanggal, jam);
    }

    private void loadTableObat(Pasien pasien) throws ServiceException {
        List<PemakaianObat> listObat = pemakaianObatService.getByPasien(pasien.getId());
        List<Pemakaian> list = new ArrayList<>();
        for (PemakaianObat po : listObat)
            list.add(po);

        PemakaianTableModel tableModel = new PemakaianTableModel(list);
        tblResepObat.setModel(tableModel);
    }
    
    @Override
    public void reloadTableObat() {
        try {
            loadTableObat(pasien);
        } catch (ServiceException ex) {}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        btnResep = new javax.swing.JButton();
        btnStok = new javax.swing.JButton();
        pnlResep = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtPasienNik = new javax.swing.JTextField();
        txtPasienNama = new javax.swing.JTextField();
        txtPasienKelamin = new javax.swing.JTextField();
        txtPasienTanggalLahir = new javax.swing.JTextField();
        txtPasienGolonganDarah = new javax.swing.JTextField();
        txtPasienAgama = new javax.swing.JTextField();
        txtPasienTelepon = new javax.swing.JTextField();
        txtPasienTanggungan = new javax.swing.JTextField();
        txtPasienStatusRawat = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienKode = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResepObat = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtResepNomor = new javax.swing.JTextField();
        btnObatTambah = new javax.swing.JButton();
        paneBarang = new javax.swing.JTabbedPane();
        pnlBhp = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtBhpKeyword = new javax.swing.JTextField();
        pnlBhpDetail = new javax.swing.JPanel();
        txtBhpKode = new javax.swing.JTextField();
        txtBhpNama = new javax.swing.JTextField();
        txtBhpHarga = new javax.swing.JTextField();
        txtBhpTanggungan = new javax.swing.JTextField();
        txtBhpSatuan = new javax.swing.JTextField();
        txtBhpJumlah = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        pnlBhpStok = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtBhpStokTanggal = new javax.swing.JTextField();
        txtBhpStokJam = new javax.swing.JTextField();
        txtBhpStokJumlah = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        btnBhpStokMasuk = new javax.swing.JButton();
        btnBhpStokReset = new javax.swing.JButton();
        btnBhpStokKeluar = new javax.swing.JButton();
        pnlObat = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtObatKeyword = new javax.swing.JTextField();
        pnlObatDetail = new javax.swing.JPanel();
        txtObatKode = new javax.swing.JTextField();
        txtObatNama = new javax.swing.JTextField();
        txtObatHarga = new javax.swing.JTextField();
        txtObatTanggungan = new javax.swing.JTextField();
        txtObatSatuan = new javax.swing.JTextField();
        txtObatJumlah = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        pnlObatStok = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtObatStokTanggal = new javax.swing.JTextField();
        txtObatStokJam = new javax.swing.JTextField();
        txtObatStokJumlah = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();
        btnObatStokMasuk = new javax.swing.JButton();
        btnObatStokReset = new javax.swing.JButton();
        btnObatStokKeluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RUMAH SAKIT LIUN KENDAGE TAHUNA - FARMASI");
        getContentPane().setLayout(null);

        pnlMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlMain.setLayout(null);

        btnResep.setText("RESEP");
        btnResep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResepActionPerformed(evt);
            }
        });
        pnlMain.add(btnResep);
        btnResep.setBounds(10, 10, 110, 40);

        btnStok.setText("STOK BARANG");
        btnStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStokActionPerformed(evt);
            }
        });
        pnlMain.add(btnStok);
        btnStok.setBounds(10, 60, 110, 40);

        getContentPane().add(pnlMain);
        pnlMain.setBounds(10, 90, 130, 110);

        pnlResep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlResep.setLayout(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jLabel3.setText("NIK");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 30, 17, 14);

        jLabel4.setText("Nama");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 60, 27, 14);

        jLabel5.setText("Kelamin");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(20, 90, 36, 14);

        jLabel7.setText("Agama");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(370, 30, 33, 14);

        jLabel8.setText("Telepon");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(370, 60, 38, 14);

        jLabel9.setText("Gol. Darah");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(20, 150, 70, 14);

        jLabel10.setText("Tanggal Masuk");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(370, 150, 80, 14);

        jLabel11.setText("Tanggal Lahir");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(20, 120, 70, 14);

        jLabel12.setText("Tanggungan");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(370, 90, 60, 14);

        jLabel13.setText("Status Rawat");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(370, 120, 70, 14);

        txtPasienNik.setEditable(false);
        jPanel1.add(txtPasienNik);
        txtPasienNik.setBounds(110, 30, 210, 20);

        txtPasienNama.setEditable(false);
        jPanel1.add(txtPasienNama);
        txtPasienNama.setBounds(110, 60, 210, 20);

        txtPasienKelamin.setEditable(false);
        jPanel1.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(110, 90, 210, 20);

        txtPasienTanggalLahir.setEditable(false);
        jPanel1.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(110, 120, 210, 20);

        txtPasienGolonganDarah.setEditable(false);
        jPanel1.add(txtPasienGolonganDarah);
        txtPasienGolonganDarah.setBounds(110, 150, 210, 20);

        txtPasienAgama.setEditable(false);
        jPanel1.add(txtPasienAgama);
        txtPasienAgama.setBounds(480, 30, 230, 20);

        txtPasienTelepon.setEditable(false);
        jPanel1.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(480, 60, 230, 20);

        txtPasienTanggungan.setEditable(false);
        jPanel1.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(480, 90, 230, 20);

        txtPasienStatusRawat.setEditable(false);
        jPanel1.add(txtPasienStatusRawat);
        txtPasienStatusRawat.setBounds(480, 120, 230, 20);

        txtPasienTanggalMasuk.setEditable(false);
        jPanel1.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(480, 150, 230, 20);

        pnlResep.add(jPanel1);
        jPanel1.setBounds(10, 80, 730, 190);

        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        pnlResep.add(txtPasienKode);
        txtPasienKode.setBounds(120, 10, 210, 20);

        tblResepObat.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblResepObat);

        pnlResep.add(jScrollPane1);
        jScrollPane1.setBounds(10, 280, 730, 270);

        jLabel2.setText("No. Register Pasien");
        pnlResep.add(jLabel2);
        jLabel2.setBounds(10, 10, 94, 14);

        jLabel1.setText("Nomor Resep");
        pnlResep.add(jLabel1);
        jLabel1.setBounds(10, 40, 90, 14);
        pnlResep.add(txtResepNomor);
        txtResepNomor.setBounds(120, 40, 210, 20);

        btnObatTambah.setText("Tambah Obat");
        btnObatTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatTambahActionPerformed(evt);
            }
        });
        pnlResep.add(btnObatTambah);
        btnObatTambah.setBounds(380, 40, 100, 23);

        getContentPane().add(pnlResep);
        pnlResep.setBounds(150, 90, 750, 560);

        paneBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneBarangMouseClicked(evt);
            }
        });

        pnlBhp.setLayout(null);

        jLabel6.setText("Kata Kunci");
        pnlBhp.add(jLabel6);
        jLabel6.setBounds(10, 10, 60, 14);

        txtBhpKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBhpKeywordFocusLost(evt);
            }
        });
        pnlBhp.add(txtBhpKeyword);
        txtBhpKeyword.setBounds(90, 10, 280, 20);

        pnlBhpDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlBhpDetail.setLayout(null);

        txtBhpKode.setEditable(false);
        pnlBhpDetail.add(txtBhpKode);
        txtBhpKode.setBounds(160, 30, 170, 20);

        txtBhpNama.setEditable(false);
        pnlBhpDetail.add(txtBhpNama);
        txtBhpNama.setBounds(160, 60, 170, 20);

        txtBhpHarga.setEditable(false);
        txtBhpHarga.setToolTipText("");
        pnlBhpDetail.add(txtBhpHarga);
        txtBhpHarga.setBounds(160, 90, 170, 20);

        txtBhpTanggungan.setEditable(false);
        txtBhpTanggungan.setToolTipText("");
        pnlBhpDetail.add(txtBhpTanggungan);
        txtBhpTanggungan.setBounds(160, 120, 170, 20);

        txtBhpSatuan.setEditable(false);
        pnlBhpDetail.add(txtBhpSatuan);
        txtBhpSatuan.setBounds(160, 180, 170, 20);

        txtBhpJumlah.setEditable(false);
        pnlBhpDetail.add(txtBhpJumlah);
        txtBhpJumlah.setBounds(160, 150, 170, 20);

        jLabel14.setText("Kode");
        pnlBhpDetail.add(jLabel14);
        jLabel14.setBounds(30, 30, 24, 14);

        jLabel15.setText("Nama");
        pnlBhpDetail.add(jLabel15);
        jLabel15.setBounds(30, 60, 27, 14);

        jLabel16.setText("Harga");
        pnlBhpDetail.add(jLabel16);
        jLabel16.setBounds(30, 90, 29, 14);

        jLabel84.setText("Tanggungan");
        pnlBhpDetail.add(jLabel84);
        jLabel84.setBounds(30, 120, 60, 14);

        jLabel17.setText("Jumlah");
        pnlBhpDetail.add(jLabel17);
        jLabel17.setBounds(30, 150, 33, 14);

        jLabel18.setText("Satuan");
        pnlBhpDetail.add(jLabel18);
        jLabel18.setBounds(30, 180, 34, 14);

        pnlBhp.add(pnlBhpDetail);
        pnlBhpDetail.setBounds(10, 300, 360, 220);

        pnlBhpStok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlBhpStok.setLayout(null);

        jLabel19.setText("Tanggal");
        pnlBhpStok.add(jLabel19);
        jLabel19.setBounds(30, 30, 38, 14);

        jLabel20.setText("Jam");
        pnlBhpStok.add(jLabel20);
        jLabel20.setBounds(30, 60, 19, 14);

        jLabel21.setText("Jumlah");
        pnlBhpStok.add(jLabel21);
        jLabel21.setBounds(30, 90, 33, 14);
        pnlBhpStok.add(txtBhpStokTanggal);
        txtBhpStokTanggal.setBounds(119, 30, 190, 20);
        pnlBhpStok.add(txtBhpStokJam);
        txtBhpStokJam.setBounds(119, 60, 190, 20);
        pnlBhpStok.add(txtBhpStokJumlah);
        txtBhpStokJumlah.setBounds(119, 90, 190, 20);

        pnlBhp.add(pnlBhpStok);
        pnlBhpStok.setBounds(380, 300, 350, 140);

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
        tblBhp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBhpMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBhp);

        pnlBhp.add(jScrollPane2);
        jScrollPane2.setBounds(10, 50, 720, 240);

        btnBhpStokMasuk.setText("MASUK");
        btnBhpStokMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpStokMasukActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpStokMasuk);
        btnBhpStokMasuk.setBounds(490, 490, 71, 23);

        btnBhpStokReset.setText("RESET");
        btnBhpStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpStokResetActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpStokReset);
        btnBhpStokReset.setBounds(650, 490, 63, 23);

        btnBhpStokKeluar.setText("KELUAR");
        btnBhpStokKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpStokKeluarActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpStokKeluar);
        btnBhpStokKeluar.setBounds(570, 490, 71, 23);

        paneBarang.addTab("BAHAN HABIS PAKAI", pnlBhp);

        pnlObat.setLayout(null);

        jLabel22.setText("Kata Kunci");
        pnlObat.add(jLabel22);
        jLabel22.setBounds(10, 10, 60, 14);

        txtObatKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtObatKeywordFocusLost(evt);
            }
        });
        pnlObat.add(txtObatKeyword);
        txtObatKeyword.setBounds(90, 10, 280, 20);

        pnlObatDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlObatDetail.setLayout(null);

        txtObatKode.setEditable(false);
        pnlObatDetail.add(txtObatKode);
        txtObatKode.setBounds(160, 30, 170, 20);

        txtObatNama.setEditable(false);
        pnlObatDetail.add(txtObatNama);
        txtObatNama.setBounds(160, 60, 170, 20);

        txtObatHarga.setEditable(false);
        txtObatHarga.setToolTipText("");
        pnlObatDetail.add(txtObatHarga);
        txtObatHarga.setBounds(160, 90, 170, 20);

        txtObatTanggungan.setEditable(false);
        txtObatTanggungan.setToolTipText("");
        pnlObatDetail.add(txtObatTanggungan);
        txtObatTanggungan.setBounds(160, 120, 170, 20);

        txtObatSatuan.setEditable(false);
        pnlObatDetail.add(txtObatSatuan);
        txtObatSatuan.setBounds(160, 180, 170, 20);

        txtObatJumlah.setEditable(false);
        pnlObatDetail.add(txtObatJumlah);
        txtObatJumlah.setBounds(160, 150, 170, 20);

        jLabel23.setText("Kode");
        pnlObatDetail.add(jLabel23);
        jLabel23.setBounds(30, 30, 24, 14);

        jLabel24.setText("Nama");
        pnlObatDetail.add(jLabel24);
        jLabel24.setBounds(30, 60, 27, 14);

        jLabel25.setText("Harga");
        pnlObatDetail.add(jLabel25);
        jLabel25.setBounds(30, 90, 29, 14);

        jLabel85.setText("Tanggungan");
        pnlObatDetail.add(jLabel85);
        jLabel85.setBounds(30, 120, 60, 14);

        jLabel26.setText("Jumlah");
        pnlObatDetail.add(jLabel26);
        jLabel26.setBounds(30, 150, 33, 14);

        jLabel27.setText("Satuan");
        pnlObatDetail.add(jLabel27);
        jLabel27.setBounds(30, 180, 34, 14);

        pnlObat.add(pnlObatDetail);
        pnlObatDetail.setBounds(10, 300, 360, 220);

        pnlObatStok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlObatStok.setLayout(null);

        jLabel28.setText("Tanggal");
        pnlObatStok.add(jLabel28);
        jLabel28.setBounds(30, 30, 38, 14);

        jLabel29.setText("Jam");
        pnlObatStok.add(jLabel29);
        jLabel29.setBounds(30, 60, 19, 14);

        jLabel30.setText("Jumlah");
        pnlObatStok.add(jLabel30);
        jLabel30.setBounds(30, 90, 33, 14);
        pnlObatStok.add(txtObatStokTanggal);
        txtObatStokTanggal.setBounds(119, 30, 190, 20);
        pnlObatStok.add(txtObatStokJam);
        txtObatStokJam.setBounds(119, 60, 190, 20);
        pnlObatStok.add(txtObatStokJumlah);
        txtObatStokJumlah.setBounds(119, 90, 190, 20);

        pnlObat.add(pnlObatStok);
        pnlObatStok.setBounds(380, 300, 350, 140);

        tblObat.setModel(new javax.swing.table.DefaultTableModel(
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
        tblObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblObatMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblObat);

        pnlObat.add(jScrollPane3);
        jScrollPane3.setBounds(10, 40, 720, 240);

        btnObatStokMasuk.setText("MASUK");
        btnObatStokMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatStokMasukActionPerformed(evt);
            }
        });
        pnlObat.add(btnObatStokMasuk);
        btnObatStokMasuk.setBounds(490, 480, 67, 23);

        btnObatStokReset.setText("RESET");
        btnObatStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatStokResetActionPerformed(evt);
            }
        });
        pnlObat.add(btnObatStokReset);
        btnObatStokReset.setBounds(650, 480, 63, 23);

        btnObatStokKeluar.setText("KELUAR");
        btnObatStokKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatStokKeluarActionPerformed(evt);
            }
        });
        pnlObat.add(btnObatStokKeluar);
        btnObatStokKeluar.setBounds(570, 480, 71, 23);

        paneBarang.addTab("OBAT", pnlObat);

        getContentPane().add(paneBarang);
        paneBarang.setBounds(150, 90, 750, 560);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStokActionPerformed
        paneBarang.setVisible(true);
        pnlResep.setVisible(false);

        setDetailBhp(null);
        setDetailObat(null);

        JOptionPane.showMessageDialog(this, "Silahkan cari data menggunakan kode/nama barang");
    }//GEN-LAST:event_btnStokActionPerformed

    private void paneBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneBarangMouseClicked
        setDetailBhp(null);
        setDetailObat(null);
        
        JOptionPane.showMessageDialog(this, "Silahkan cari data menggunakan kode/nama barang");
    }//GEN-LAST:event_paneBarangMouseClicked

    private void txtBhpKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBhpKeywordFocusLost
        String keyword = txtBhpKeyword.getText();
        
        List<BahanHabisPakai> list = null;
        try {
            list = bhpService.cari(keyword);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            BhpTableModel tableModel = new BhpTableModel(list);
            tblBhp.setModel(tableModel);
            
            setDetailBhp(null);
        }
    }//GEN-LAST:event_txtBhpKeywordFocusLost

    private void tblBhpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBhpMouseClicked
        Integer index = tblBhp.getSelectedRow();
        
        BhpTableModel tableModel = (BhpTableModel)tblBhp.getModel();
        BahanHabisPakai bhp = tableModel.getBhp(index);
        
        setDetailBhp(bhp);
    }//GEN-LAST:event_tblBhpMouseClicked

    private void btnBhpStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpStokMasukActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtBhpStokTanggal.getText();
        String jam = txtBhpStokJam.getText();
        String jumlah = txtBhpStokJumlah.getText();
        
        try {
            simpanStokMasuk(barang.getId(), new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailBhp(null);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpStokMasukActionPerformed

    private void btnBhpStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpStokKeluarActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtBhpStokTanggal.getText();
        String jam = txtBhpStokJam.getText();
        String jumlah = txtBhpStokJumlah.getText();
        
        try {
            simpanStokKeluar(barang.getId(), new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailBhp(null);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpStokKeluarActionPerformed

    private void btnBhpStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpStokResetActionPerformed
        setDetailBhp(null);
    }//GEN-LAST:event_btnBhpStokResetActionPerformed

    private void txtObatKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtObatKeywordFocusLost
        String keyword = txtObatKeyword.getText();
        
        List<ObatFarmasi> list = null;
        try {
            list = obatService.cari(keyword);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            ObatTableModel tableModel = new ObatTableModel(list);
            tblObat.setModel(tableModel);
            
            setDetailObat(null);
        }
    }//GEN-LAST:event_txtObatKeywordFocusLost

    private void tblObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblObatMouseClicked
        Integer index = tblObat.getSelectedRow();
        
        ObatTableModel tableModel = (ObatTableModel)tblObat.getModel();
        ObatFarmasi obat = tableModel.getObat(index);
        
        setDetailObat(obat);
    }//GEN-LAST:event_tblObatMouseClicked

    private void btnObatStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatStokMasukActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtObatStokTanggal.getText();
        String jam = txtObatStokJam.getText();
        String jumlah = txtObatStokJumlah.getText();
        
        try {
            simpanStokMasuk(barang.getId(), new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailObat(null);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnObatStokMasukActionPerformed

    private void btnObatStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatStokKeluarActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtObatStokTanggal.getText();
        String jam = txtObatStokJam.getText();
        String jumlah = txtObatStokJumlah.getText();
        
        try {
            simpanStokKeluar(barang.getId(), new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailObat(null);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnObatStokKeluarActionPerformed

    private void btnObatStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatStokResetActionPerformed
        setDetailObat(null);
    }//GEN-LAST:event_btnObatStokResetActionPerformed

    private void btnResepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResepActionPerformed
        paneBarang.setVisible(false);
        pnlResep.setVisible(true);
        
        setDetailPasien(pasien);
        
        JOptionPane.showMessageDialog(this, "Silahkan masukan Nomor Pasien");
    }//GEN-LAST:event_btnResepActionPerformed

    private void txtPasienKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasienKodeFocusLost
        String keyword = txtResepNomor.getText();

        try {
            pasien = pasienService.get(keyword);
            
            setDetailPasien(pasien);

            loadTableObat(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    private void btnObatTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatTambahActionPerformed
        new FrameTambahObject(this, ObatFarmasi.class, pasien).setVisible(true);
    }//GEN-LAST:event_btnObatTambahActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBhpStokKeluar;
    private javax.swing.JButton btnBhpStokMasuk;
    private javax.swing.JButton btnBhpStokReset;
    private javax.swing.JButton btnObatStokKeluar;
    private javax.swing.JButton btnObatStokMasuk;
    private javax.swing.JButton btnObatStokReset;
    private javax.swing.JButton btnObatTambah;
    private javax.swing.JButton btnResep;
    private javax.swing.JButton btnStok;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane paneBarang;
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlBhpDetail;
    private javax.swing.JPanel pnlBhpStok;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlObat;
    private javax.swing.JPanel pnlObatDetail;
    private javax.swing.JPanel pnlObatStok;
    private javax.swing.JPanel pnlResep;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblObat;
    private javax.swing.JTable tblResepObat;
    private javax.swing.JTextField txtBhpHarga;
    private javax.swing.JTextField txtBhpJumlah;
    private javax.swing.JTextField txtBhpKeyword;
    private javax.swing.JTextField txtBhpKode;
    private javax.swing.JTextField txtBhpNama;
    private javax.swing.JTextField txtBhpSatuan;
    private javax.swing.JTextField txtBhpStokJam;
    private javax.swing.JTextField txtBhpStokJumlah;
    private javax.swing.JTextField txtBhpStokTanggal;
    private javax.swing.JTextField txtBhpTanggungan;
    private javax.swing.JTextField txtObatHarga;
    private javax.swing.JTextField txtObatJumlah;
    private javax.swing.JTextField txtObatKeyword;
    private javax.swing.JTextField txtObatKode;
    private javax.swing.JTextField txtObatNama;
    private javax.swing.JTextField txtObatSatuan;
    private javax.swing.JTextField txtObatStokJam;
    private javax.swing.JTextField txtObatStokJumlah;
    private javax.swing.JTextField txtObatStokTanggal;
    private javax.swing.JTextField txtObatTanggungan;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienGolonganDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKode;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienStatusRawat;
    private javax.swing.JTextField txtPasienTanggalLahir;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPasienTelepon;
    private javax.swing.JTextField txtResepNomor;
    // End of variables declaration//GEN-END:variables
}
