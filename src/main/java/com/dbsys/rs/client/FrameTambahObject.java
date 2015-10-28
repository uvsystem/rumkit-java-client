package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.BhpTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.TindakanTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.BhpService;
import com.dbsys.rs.connector.service.ObatService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianBhpService;
import com.dbsys.rs.connector.service.PemakaianObatService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.PemakaianObat;
import com.dbsys.rs.lib.entity.Perawat;
import com.dbsys.rs.lib.entity.Tindakan;
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
    
    private final BhpService bhpService = BhpService.getInstance(EventController.host);
    private final ObatService obatService = ObatService.getInstance(EventController.host);
    private final PemakaianBhpService pemakaianBhpService = PemakaianBhpService.getInstance(EventController.host);
    private final PemakaianObatService pemakaianObatService = PemakaianObatService.getInstance(EventController.host);
    private final TindakanService tindakanService = TindakanService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);

    private final Pasien pasien;

    private PemakaianBhp pemakaianBhp;
    private BahanHabisPakai bhp;

    private PemakaianObat pemakaianObat;
    private ObatFarmasi obat;
    
    private Pelayanan pelayanan;
    private Tindakan tindakan;
    private Pegawai pelaksana;
    
    /**
     * Creates new form FrameTambahObject
     * @param frame
     * @param clsFrame
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
            setSize(493, 530);
            txtPemakaianTanggal.setText(DateUtil.getDate().toString());
            pnlPemakaian.setVisible(true);
        } else if (clsDomain.equals(Tindakan.class)) {
            setSize(493, 650);
            txtPelayananTanggal.setText(DateUtil.getDate().toString());
            pnlPelayanan.setVisible(true);
        }
    }

    FrameTambahObject(JFrame frame, Pasien pasien, PemakaianBhp pemakaianBhp) {
        this(frame, BahanHabisPakai.class, pasien);
        this.pemakaianBhp = pemakaianBhp;
        this.bhp = pemakaianBhp.getBahanHabisPakai();
        
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
    
    @Override
    public void setPegawaiForPelayanan(Pegawai pegawai) {
        this.pelaksana = pegawai;
        txtPelayananPelaksana.setText(pelaksana.getNama());
    }
    
    private void cariBhp(String keyword) throws ServiceException {
        List<BahanHabisPakai> list = bhpService.cari(keyword);
        
        BhpTableModel tableModel = new BhpTableModel(list);
        tblCari.setModel(tableModel);
    }
    
    private BahanHabisPakai getBhp(int index) {
        BhpTableModel tableModel = (BhpTableModel)tblCari.getModel();
        return tableModel.getBhp(index);
    }
    
    private PemakaianBhp getPemakaianBhp() {
        if (pemakaianBhp == null)
            pemakaianBhp = new PemakaianBhp();
        
        String biayaTambahan = txtPemakaianBiayaTambahan.getText();
        String jumlah = txtPemakaianJumlah.getText();
        String tanggal = txtPemakaianTanggal.getText();
        
        pemakaianBhp.setBahanHabisPakai(bhp);
        pemakaianBhp.setBiayaTambahan(Long.valueOf(biayaTambahan));
        pemakaianBhp.setJumlah(Integer.valueOf(jumlah));
        pemakaianBhp.setKeterangan(txtPemakaianKeterangan.getText());
        pemakaianBhp.setTanggal(DateUtil.getDate(tanggal));
        pemakaianBhp.setPasien(pasien);
        pemakaianBhp.setUnit(TokenHolder.getToken().getOperator().getUnit());
        
        return pemakaianBhp;
    }
    
    private void cariObat(String keyword) throws ServiceException {
        List<ObatFarmasi> list = obatService.cari(keyword);
        
        ObatTableModel tableModel = new ObatTableModel(list);
        tblCari.setModel(tableModel);
    }
    
    private ObatFarmasi getObat(int index) {
        ObatTableModel tableModel = (ObatTableModel)tblCari.getModel();
        return tableModel.getObat(index);
    }
    
    private PemakaianObat getPemakaianObat() {
        if (pemakaianObat == null)
            pemakaianObat = new PemakaianObat();
        
        String biayaTambahan = txtPemakaianBiayaTambahan.getText();
        String jumlah = txtPemakaianJumlah.getText();
        String tanggal = txtPemakaianTanggal.getText();
        
        pemakaianObat.setObat(obat);
        pemakaianObat.setBiayaTambahan(Long.valueOf(biayaTambahan));
        pemakaianObat.setJumlah(Integer.valueOf(jumlah));
        pemakaianObat.setKeterangan(txtPemakaianKeterangan.getText());
        pemakaianObat.setTanggal(DateUtil.getDate(tanggal));
        pemakaianObat.setPasien(pasien);
        pemakaianObat.setUnit(TokenHolder.getToken().getOperator().getUnit());
        
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
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCari = new javax.swing.JTable();
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
        pnlPelayanan = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtPelayananTindakanNama = new javax.swing.JTextField();
        txtPelayananJumlah = new javax.swing.JTextField();
        txtPelayananBiayaTambahan = new javax.swing.JTextField();
        txtPelayananKeterangan = new javax.swing.JTextField();
        txtPelayananTanggal = new javax.swing.JTextField();
        txtPelayananPelaksana = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        cbPelayananTipePelaksana = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        txtPelayananTindakanKelas = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        btnSimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PENAMBAHAN");
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setText("Kata Kunci");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 59, 50, 14);
        getContentPane().add(txtKeyword);
        txtKeyword.setBounds(80, 56, 209, 20);

        btnCari.setText("OK");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        getContentPane().add(btnCari);
        btnCari.setBounds(295, 55, 47, 23);

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
        jScrollPane1.setBounds(20, 89, 452, 154);

        pnlPemakaian.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Pemakaian"));
        pnlPemakaian.setLayout(null);

        jLabel2.setText("Nama Barang");
        pnlPemakaian.add(jLabel2);
        jLabel2.setBounds(20, 40, 70, 14);

        jLabel3.setText("Jumlah");
        pnlPemakaian.add(jLabel3);
        jLabel3.setBounds(20, 80, 34, 14);

        jLabel4.setText("Biaya Tambahan");
        pnlPemakaian.add(jLabel4);
        jLabel4.setBounds(20, 120, 90, 14);

        jLabel5.setText("Keterangan");
        pnlPemakaian.add(jLabel5);
        jLabel5.setBounds(20, 160, 70, 14);

        jLabel6.setText("Tanggal");
        pnlPemakaian.add(jLabel6);
        jLabel6.setBounds(20, 200, 60, 14);

        txtPemakaianBarang.setEditable(false);
        pnlPemakaian.add(txtPemakaianBarang);
        txtPemakaianBarang.setBounds(160, 30, 270, 20);
        pnlPemakaian.add(txtPemakaianJumlah);
        txtPemakaianJumlah.setBounds(160, 70, 270, 20);
        pnlPemakaian.add(txtPemakaianBiayaTambahan);
        txtPemakaianBiayaTambahan.setBounds(160, 110, 270, 20);
        pnlPemakaian.add(txtPemakaianKeterangan);
        txtPemakaianKeterangan.setBounds(160, 150, 270, 20);
        pnlPemakaian.add(txtPemakaianTanggal);
        txtPemakaianTanggal.setBounds(160, 190, 270, 20);

        getContentPane().add(pnlPemakaian);
        pnlPemakaian.setBounds(20, 250, 450, 240);

        pnlPelayanan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detail Pemakaian"));
        pnlPelayanan.setLayout(null);

        jLabel12.setText("Nama Tindakan");
        pnlPelayanan.add(jLabel12);
        jLabel12.setBounds(20, 40, 80, 14);

        jLabel13.setText("Jumlah");
        pnlPelayanan.add(jLabel13);
        jLabel13.setBounds(20, 130, 34, 14);

        jLabel14.setText("Biaya Tambahan");
        pnlPelayanan.add(jLabel14);
        jLabel14.setBounds(20, 160, 90, 14);

        jLabel15.setText("Keterangan");
        pnlPelayanan.add(jLabel15);
        jLabel15.setBounds(20, 200, 70, 14);

        jLabel16.setText("Tanggal");
        pnlPelayanan.add(jLabel16);
        jLabel16.setBounds(20, 240, 60, 14);

        jLabel22.setText("Pelaksana");
        pnlPelayanan.add(jLabel22);
        jLabel22.setBounds(20, 320, 60, 14);

        txtPelayananTindakanNama.setEditable(false);
        pnlPelayanan.add(txtPelayananTindakanNama);
        txtPelayananTindakanNama.setBounds(160, 30, 270, 20);
        pnlPelayanan.add(txtPelayananJumlah);
        txtPelayananJumlah.setBounds(160, 120, 270, 20);
        pnlPelayanan.add(txtPelayananBiayaTambahan);
        txtPelayananBiayaTambahan.setBounds(160, 150, 270, 20);
        pnlPelayanan.add(txtPelayananKeterangan);
        txtPelayananKeterangan.setBounds(160, 190, 270, 20);
        pnlPelayanan.add(txtPelayananTanggal);
        txtPelayananTanggal.setBounds(160, 230, 270, 20);

        txtPelayananPelaksana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPelayananPelaksanaMouseClicked(evt);
            }
        });
        pnlPelayanan.add(txtPelayananPelaksana);
        txtPelayananPelaksana.setBounds(160, 320, 270, 20);
        pnlPelayanan.add(jSeparator5);
        jSeparator5.setBounds(0, 268, 450, 10);

        jLabel7.setText("Tipe Pelaksana");
        pnlPelayanan.add(jLabel7);
        jLabel7.setBounds(20, 290, 80, 14);

        cbPelayananTipePelaksana.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "DOKTER", "PERAWAT" }));
        pnlPelayanan.add(cbPelayananTipePelaksana);
        cbPelayananTipePelaksana.setBounds(160, 290, 270, 20);

        jLabel17.setText("Kelas Tindakan");
        pnlPelayanan.add(jLabel17);
        jLabel17.setBounds(20, 70, 80, 14);

        txtPelayananTindakanKelas.setEditable(false);
        pnlPelayanan.add(txtPelayananTindakanKelas);
        txtPelayananTindakanKelas.setBounds(160, 60, 270, 20);
        pnlPelayanan.add(jSeparator6);
        jSeparator6.setBounds(0, 98, 450, 10);

        getContentPane().add(pnlPelayanan);
        pnlPelayanan.setBounds(20, 250, 450, 360);

        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(btnSimpan);
        btnSimpan.setBounds(400, 50, 71, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
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
    }//GEN-LAST:event_btnCariActionPerformed

    private void tblCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCariMouseClicked
        int index = tblCari.getSelectedRow();
        
        if (clsDomain.equals(BahanHabisPakai.class)) {
            bhp = getBhp(index);
            txtPemakaianBarang.setText(bhp.getNama());
        } else if (clsDomain.equals(ObatFarmasi.class)) {
            obat = getObat(index);
            txtPemakaianBarang.setText(bhp.getNama());
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
                pelayananService.simpan(pelayanan);
                
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
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
