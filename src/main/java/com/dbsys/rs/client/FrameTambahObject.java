package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.BhpTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.TindakanTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.BarangService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.Perawat;
import com.dbsys.rs.lib.entity.Tindakan;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Acer One 10
 */
public class FrameTambahObject extends JFrame implements  TindakanFrame {

    private final JFrame frame;
    private final Class<?> clsDomain;
    
    private final BarangService bhpService = BarangService.getInstance(EventController.host);
    private final BarangService obatService = BarangService.getInstance(EventController.host);
    private final PemakaianService pemakaianBhpService = PemakaianService.getInstance(EventController.host);
    private final PemakaianService pemakaianObatService = PemakaianService.getInstance(EventController.host);
    private final TindakanService tindakanService = TindakanService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);

    private final Pasien pasien;

    private Pemakaian pemakaianBhp;
    private BahanHabisPakai bhp;

    private Pemakaian pemakaianObat;
    private ObatFarmasi obat;
    private String nomorResep;
    
    private Pelayanan pelayanan;
    private Tindakan tindakan;
    private Pegawai pelaksana;
    
    /**
     * Creates new form FrameTambahObject
     * @param frame
     * @param clsDomain
     * @param pasien
     */
    public FrameTambahObject(JFrame frame, Class<?> clsDomain, Pasien pasien) {
        initComponents();
        this.clsDomain = clsDomain;
        this.frame = frame;
        this.pasien = pasien;

        pnlPemakaian.setVisible(false);
        pnlPelayanan.setVisible(false);
        
        if (clsDomain.equals(BahanHabisPakai.class) || clsDomain.equals(ObatFarmasi.class)) {
            setSize(500, 580);
            txtPemakaianTanggal.setText(DateUtil.getDate().toString());
            pnlPemakaian.setVisible(true);
        } else if (clsDomain.equals(Tindakan.class)) {
            setSize(500, 700);
            txtPelayananTanggal.setText(DateUtil.getDate().toString());
            pnlPelayanan.setVisible(true);
        }
    }

    FrameTambahObject(JFrame frame, Pasien pasien, Pemakaian pemakaianBhp) {
        this(frame, BahanHabisPakai.class, pasien);
        this.pemakaianBhp = pemakaianBhp;
        this.bhp = (BahanHabisPakai)pemakaianBhp.getBarang();
        
        txtPemakaianBarang.setText(bhp.getNama());
        txtPemakaianBiayaTambahan.setText(pemakaianBhp.getBiayaTambahan().toString());
        txtPemakaianJumlah.setText(pemakaianBhp.getJumlah().toString());
        txtPemakaianTanggal.setText(pemakaianBhp.getTanggal().toString());
        txtPemakaianKeterangan.setText(pemakaianBhp.getKeterangan());
    }

    FrameTambahObject(JFrame frame, Pasien pasien, Pelayanan pelayanan) {
        this(frame, Tindakan.class, pasien);
        this.pelayanan = pelayanan;
        this.tindakan = pelayanan.getTindakan();
        this.pelaksana = pelayanan.getPelaksana();

        txtPelayananTindakanNama.setText(tindakan.getNama());
        txtPelayananTindakanKelas.setText(tindakan.getKelas().toString());
        txtPelayananBiayaTambahan.setText(pelayanan.getBiayaTambahan().toString());
        txtPelayananJumlah.setText(pelayanan.getJumlah().toString());
        txtPelayananTanggal.setText(pelayanan.getTanggal().toString());
        txtPelayananKeterangan.setText(pelayanan.getKeterangan());
        
        if (pelaksana != null)
            txtPelayananPelaksana.setText(pelaksana.getNama());
    }

    FrameTambahObject(JFrame frame, Pasien pasien, String nomorResep) {
        this(frame, ObatFarmasi.class, pasien);
        this.nomorResep = nomorResep;
    }
    
    @Override
    public void setPegawaiForPelayanan(Pegawai pegawai) {
        this.pelaksana = pegawai;
        txtPelayananPelaksana.setText(pelaksana.getNama());
    }
    
    private void cariBhp(String keyword) throws ServiceException {
        List<Barang> list = bhpService.cari(keyword, BahanHabisPakai.class);
        List<BahanHabisPakai> listBhp = new ArrayList<>();
        for (Barang barang : list) {
            if (barang instanceof BahanHabisPakai)
                listBhp.add((BahanHabisPakai) barang);
        }
        
        BhpTableModel tableModel = new BhpTableModel(listBhp);
        tblCari.setModel(tableModel);
    }
    
    private BahanHabisPakai getBhp(int index) {
        BhpTableModel tableModel = (BhpTableModel)tblCari.getModel();
        return tableModel.getBhp(index);
    }
    
    private Pemakaian getPemakaianBhp() {
        if (pemakaianBhp == null)
            pemakaianBhp = new Pemakaian();
        
        String biayaTambahan = txtPemakaianBiayaTambahan.getText();
        if (biayaTambahan == null || biayaTambahan.equals(""))
            biayaTambahan = "0";
        
        String jumlah = txtPemakaianJumlah.getText();
        String tanggal = txtPemakaianTanggal.getText();
        
        pemakaianBhp.setBarang(bhp);
        pemakaianBhp.setBiayaTambahan(Long.valueOf(biayaTambahan));
        pemakaianBhp.setJumlah(Integer.valueOf(jumlah));
        pemakaianBhp.setKeterangan(txtPemakaianKeterangan.getText());
        pemakaianBhp.setTanggal(DateUtil.getDate(tanggal));
        pemakaianBhp.setPasien(pasien);
        pemakaianBhp.setUnit(TokenHolder.getToken().getOperator().getUnit());
        
        return pemakaianBhp;
    }
    
    private void cariObat(String keyword) throws ServiceException {
        List<Barang> list = obatService.cari(keyword);
        List<ObatFarmasi> listObat = new ArrayList<>();
        for (Barang barang : list) {
            if (barang instanceof ObatFarmasi)
                listObat.add((ObatFarmasi) barang);
        }
        
        ObatTableModel tableModel = new ObatTableModel(listObat);
        tblCari.setModel(tableModel);
    }
    
    private ObatFarmasi getObat(int index) {
        ObatTableModel tableModel = (ObatTableModel)tblCari.getModel();
        return tableModel.getObat(index);
    }
    
    private Pemakaian getPemakaianObat() {
        if (pemakaianObat == null)
            pemakaianObat = new Pemakaian();
        
        String biayaTambahan = txtPemakaianBiayaTambahan.getText();
        if (biayaTambahan == null || biayaTambahan.equals(""))
            biayaTambahan = "0";
        
        String jumlah = txtPemakaianJumlah.getText();
        String tanggal = txtPemakaianTanggal.getText();
        
        pemakaianObat.setBarang(obat);
        pemakaianObat.setBiayaTambahan(Long.valueOf(biayaTambahan));
        pemakaianObat.setJumlah(Integer.valueOf(jumlah));
        pemakaianObat.setKeterangan(txtPemakaianKeterangan.getText());
        pemakaianObat.setTanggal(DateUtil.getDate(tanggal));
        pemakaianObat.setPasien(pasien);
        pemakaianObat.setUnit(TokenHolder.getToken().getOperator().getUnit());
        pemakaianObat.setNomorResep(nomorResep);
        
        return pemakaianObat;
    }
    
    private void cariTindakan(String keyword) throws ServiceException {
        List<Tindakan> list = tindakanService.cari(keyword);
        
        TindakanTableModel tableModel = new TindakanTableModel(list);
        tblCari.setModel(tableModel);
    }
    
    private Tindakan getTindakan(int index) {
        TindakanTableModel tableModel = (TindakanTableModel)tblCari.getModel();
        return tableModel.getTindakan(index);
    }
    
    private Pelayanan getPelayananTindakan() {
        if (pelayanan == null)
            pelayanan = new Pelayanan();
        
        String biayaTambahan = txtPelayananBiayaTambahan.getText();
        if (biayaTambahan == null || biayaTambahan.equals(""))
            biayaTambahan = "0";
        
        String jumlah = txtPelayananJumlah.getText();
        String tanggal = txtPelayananTanggal.getText();
        
        pelayanan.setTindakan(tindakan);
        pelayanan.setBiayaTambahan(Long.valueOf(biayaTambahan));
        pelayanan.setJumlah(Integer.valueOf(jumlah));
        pelayanan.setKeterangan(txtPemakaianKeterangan.getText());
        pelayanan.setTanggal(DateUtil.getDate(tanggal));
        pelayanan.setPasien(pasien);
        pelayanan.setUnit(TokenHolder.getToken().getOperator().getUnit());
        pelayanan.setPelaksana(pelaksana);
        
        return pelayanan;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCari = new javax.swing.JTable();
        pnlPelayanan = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        txtPelayananTindakanNama = new javax.swing.JTextField();
        txtPelayananTindakanKelas = new javax.swing.JTextField();
        txtPelayananJumlah = new javax.swing.JTextField();
        txtPelayananBiayaTambahan = new javax.swing.JTextField();
        txtPelayananKeterangan = new javax.swing.JTextField();
        txtPelayananTanggal = new javax.swing.JTextField();
        cbPelayananTipePelaksana = new javax.swing.JComboBox();
        txtPelayananPelaksana = new javax.swing.JTextField();
        pnlPemakaian = new javax.swing.JPanel();
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
        btnSimpan = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PENAMBAHAN");
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setLabelFor(txtKeyword);
        jLabel1.setText("Kata Kunci");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 95, 80, 20);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        getContentPane().add(txtKeyword);
        txtKeyword.setBounds(110, 95, 260, 25);

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
        jScrollPane1.setBounds(20, 130, 460, 154);

        pnlPelayanan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Pemakaian"));
        pnlPelayanan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("Nama Tindakan");
        pnlPelayanan.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, -1));

        jLabel13.setText("Jumlah");
        pnlPelayanan.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, -1));

        jLabel14.setText("Biaya Tambahan");
        pnlPelayanan.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 90, -1));

        jLabel15.setText("Keterangan");
        pnlPelayanan.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 90, -1));

        jLabel16.setText("Tanggal");
        pnlPelayanan.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 90, -1));

        jLabel22.setText("Pelaksana");
        pnlPelayanan.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 90, -1));

        jLabel7.setText("Tipe Pelaksana");
        pnlPelayanan.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 90, -1));

        jLabel17.setText("Kelas Tindakan");
        pnlPelayanan.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 90, -1));
        pnlPelayanan.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 460, 10));
        pnlPelayanan.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 460, 10));

        txtPelayananTindakanNama.setEditable(false);
        pnlPelayanan.add(txtPelayananTindakanNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 270, 25));

        txtPelayananTindakanKelas.setEditable(false);
        pnlPelayanan.add(txtPelayananTindakanKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 270, 25));
        pnlPelayanan.add(txtPelayananJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, 270, 25));
        pnlPelayanan.add(txtPelayananBiayaTambahan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 270, 25));
        pnlPelayanan.add(txtPelayananKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 270, 25));
        pnlPelayanan.add(txtPelayananTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 270, 25));

        cbPelayananTipePelaksana.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "DOKTER", "PERAWAT" }));
        pnlPelayanan.add(cbPelayananTipePelaksana, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 270, 270, 25));

        txtPelayananPelaksana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPelayananPelaksanaMouseClicked(evt);
            }
        });
        pnlPelayanan.add(txtPelayananPelaksana, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, 270, 25));

        getContentPane().add(pnlPelayanan);
        pnlPelayanan.setBounds(20, 290, 460, 360);

        pnlPemakaian.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Pemakaian"));
        pnlPemakaian.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Nama Barang");
        pnlPemakaian.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

        jLabel3.setText("Jumlah");
        pnlPemakaian.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, -1));

        jLabel4.setText("Biaya Tambahan");
        pnlPemakaian.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, -1));

        jLabel5.setText("Keterangan");
        pnlPemakaian.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 90, -1));

        jLabel6.setText("Tanggal");
        pnlPemakaian.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 90, -1));

        txtPemakaianBarang.setEditable(false);
        pnlPemakaian.add(txtPemakaianBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 270, 25));
        pnlPemakaian.add(txtPemakaianJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 270, 25));
        pnlPemakaian.add(txtPemakaianBiayaTambahan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, 270, 25));
        pnlPemakaian.add(txtPemakaianKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 270, 25));
        pnlPemakaian.add(txtPemakaianTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 270, 25));

        getContentPane().add(pnlPemakaian);
        pnlPemakaian.setBounds(20, 290, 460, 240);

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(btnSimpan);
        btnSimpan.setBounds(380, 90, 100, 33);

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Pencarian.jpg"))); // NOI18N
        lblBackground.setText("jLabel8");
        getContentPane().add(lblBackground);
        lblBackground.setBounds(0, 0, 500, 430);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCariMouseClicked
        int index = tblCari.getSelectedRow();
        
        if (clsDomain.equals(BahanHabisPakai.class)) {
            bhp = getBhp(index);
            txtPemakaianBarang.setText(bhp.getNama());
        } else if (clsDomain.equals(ObatFarmasi.class)) {
            obat = getObat(index);
            txtPemakaianBarang.setText(obat.getNama());
        } else if (clsDomain.equals(Tindakan.class)) {
            tindakan = getTindakan(index);
            txtPelayananTindakanNama.setText(tindakan.getNama());
            txtPelayananTindakanKelas.setText(tindakan.getKelas().toString());
        }
    }//GEN-LAST:event_tblCariMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        try {
            if (clsDomain.equals(BahanHabisPakai.class)) {
                pemakaianBhp = getPemakaianBhp();
                pemakaianBhpService.simpan(pemakaianBhp);

                ((BhpTableFrame)frame).reloadTableBhp();
            } else if (clsDomain.equals(ObatFarmasi.class)) {
                pemakaianObat = getPemakaianObat();
                pemakaianObatService.simpan(pemakaianObat);
                
                ((ObatTableFrame)frame).reloadTableObat();
            } else if (clsDomain.equals(Tindakan.class)) {
                pelayanan = getPelayananTindakan();
                pelayananService.simpan(pelayanan);
                
                ((TindakanTableFrame)frame).reloadTableTindakan();
            }
            
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void txtPelayananPelaksanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPelayananPelaksanaMouseClicked
        String tipe = (String)cbPelayananTipePelaksana.getSelectedItem();
        
        if (tipe == null || tipe.equals("- Pilih -")) {
            JOptionPane.showMessageDialog(this, "Slihakan pilih tipe pelaksana terlebih dahulu");
            return;
        }
        
        Class<?> pegawai;
        switch (tipe) {
            case "DOKTER":
                pegawai = Dokter.class;
                break;
            case "PERAWAT":
                pegawai = Perawat.class;
                break;
            default: return;
        }
        
        new FrameCari(this, pegawai).setVisible(true);
    }//GEN-LAST:event_txtPelayananPelaksanaMouseClicked

    private void txtKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKeywordFocusLost
        String keyword = txtKeyword.getText();

        if (keyword.equals(""))
            return;

        try {
            if (clsDomain.equals(BahanHabisPakai.class)) {
                cariBhp(keyword);
            } else if (clsDomain.equals(ObatFarmasi.class)) {
                cariObat(keyword);
            } else if (clsDomain.equals(Tindakan.class)) {
                cariTindakan(keyword);
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtKeywordFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox cbPelayananTipePelaksana;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JPanel pnlPelayanan;
    private javax.swing.JPanel pnlPemakaian;
    private javax.swing.JTable tblCari;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtPelayananBiayaTambahan;
    private javax.swing.JTextField txtPelayananJumlah;
    private javax.swing.JTextField txtPelayananKeterangan;
    private javax.swing.JTextField txtPelayananPelaksana;
    private javax.swing.JTextField txtPelayananTanggal;
    private javax.swing.JTextField txtPelayananTindakanKelas;
    private javax.swing.JTextField txtPelayananTindakanNama;
    private javax.swing.JTextField txtPemakaianBarang;
    private javax.swing.JTextField txtPemakaianBiayaTambahan;
    private javax.swing.JTextField txtPemakaianJumlah;
    private javax.swing.JTextField txtPemakaianKeterangan;
    private javax.swing.JTextField txtPemakaianTanggal;
    // End of variables declaration//GEN-END:variables
}
