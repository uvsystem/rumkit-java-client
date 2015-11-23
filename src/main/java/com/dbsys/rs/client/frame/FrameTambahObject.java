package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.BarangTableFrame;
import com.dbsys.rs.client.TindakanFrame;
import com.dbsys.rs.client.TindakanTableFrame;
import com.dbsys.rs.client.UnitFrame;
import com.dbsys.rs.client.tableModel.BarangTableModel;
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
import com.dbsys.rs.lib.entity.Unit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Acer One 10
 */
public class FrameTambahObject extends JFrame implements  TindakanFrame, UnitFrame {

    private final JFrame frame;
    private final Class<?> clsDomain;
    
    private final BarangService barangService = BarangService.getInstance(EventController.host);
    private final PemakaianService pemakaianService = PemakaianService.getInstance(EventController.host);
    private final TindakanService tindakanService = TindakanService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);

    private final Pasien pasien;

    private Pemakaian pemakaian;
    private Barang barang;
    private String nomorResep;
    
    private Pelayanan pelayanan;
    private Tindakan tindakan;
    private Pegawai pelaksana;
    private Unit unit;
    
    /**
     * Creates new form FrameTambahObject
     * 
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
        
        if (clsDomain.equals(BahanHabisPakai.class) || clsDomain.equals(ObatFarmasi.class) || clsDomain.equals(Barang.class)) {
            setSize(500, 580);
            pnlPemakaian.setVisible(true);
            txtPemakaianTanggal.setText(DateUtil.getDate().toString());
        } else if (clsDomain.equals(Tindakan.class)) {
            setSize(500, 700);
            this.unit = TokenHolder.getUnit();

            pnlPelayanan.setVisible(true);
            txtPelayananUnit.setText(unit.getNama());
            txtPelayananTanggal.setText(DateUtil.getDate().toString());
        }
    }

    /**
     * Constructor untuk update pemakaian.
     * 
     * @param frame
     * @param pasien
     * @param pemakaian 
     */
    FrameTambahObject(JFrame frame, Pasien pasien, Pemakaian pemakaian) {
        this(frame, Barang.class, pasien);
        this.pemakaian = pemakaian;
        this.barang = pemakaian.getBarang();
        
        txtPemakaianBarang.setText(barang.getNama());
        txtPemakaianBiayaTambahan.setText(pemakaian.getBiayaTambahan().toString());
        txtPemakaianJumlah.setText(pemakaian.getJumlah().toString());
        txtPemakaianTanggal.setText(pemakaian.getTanggal().toString());
        txtPemakaianKeterangan.setText(pemakaian.getKeterangan());
        txtPemakaianSatuan.setText(pemakaian.getBarang().getSatuan());
    }

    /**
     * Constructor untuk update pelayanan.
     * 
     * @param frame
     * @param pasien
     * @param pelayanan 
     */
    FrameTambahObject(JFrame frame, Pasien pasien, Pelayanan pelayanan) {
        this(frame, Tindakan.class, pasien);
        this.pelayanan = pelayanan;
        this.tindakan = pelayanan.getTindakan();
        this.pelaksana = pelayanan.getPelaksana();
        this.unit = pelayanan.getUnit();

        txtPelayananTindakanNama.setText(tindakan.getNama());
        txtPelayananTindakanKelas.setText(tindakan.getKelas().toString());
        txtPelayananBiayaTambahan.setText(pelayanan.getBiayaTambahan().toString());
        txtPelayananJumlah.setText(pelayanan.getJumlah().toString());
        txtPelayananTanggal.setText(pelayanan.getTanggal().toString());
        txtPelayananKeterangan.setText(pelayanan.getKeterangan());
        txtPelayananSatuan.setText(tindakan.getSatuan().toString());
        
        if (pelaksana != null) {
            txtPelayananPelaksana.setText(pelaksana.getNama());
            cbPelayananTipePelaksana.setSelectedItem(pelaksana.getTipe());
        }
        
        if (unit != null)
            txtPelayananUnit.setText(unit.getNama());
    }

    /**
     * Constructor untuk penambahan barang.
     * 
     * @param frame
     * @param pasien
     * @param nomorResep 
     */
    FrameTambahObject(JFrame frame, Pasien pasien, String nomorResep) {
        this(frame, Barang.class, pasien);
        this.nomorResep = nomorResep;
    }

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
        
        txtPelayananUnit.setText(unit.getNama());
    }
    
    @Override
    public void setPegawaiForPelayanan(Pegawai pegawai) {
        this.pelaksana = pegawai;
        txtPelayananPelaksana.setText(pelaksana.getNama());
    }
    
    private void cariBarang(String keyword) throws ServiceException {
        List<Barang> list = barangService.cari(keyword);
        
        BarangTableModel tableModel = new BarangTableModel(list);
        tblCari.setModel(tableModel);
    }
    
    private Barang getBarang(int index) {
        BarangTableModel tableModel = (BarangTableModel)tblCari.getModel();
        return tableModel.getBarang(index);
    }
    
    private Pemakaian getPemakaian() {
        if (pemakaian == null)
            pemakaian = new Pemakaian();
        
        String biayaTambahan = txtPemakaianBiayaTambahan.getText();
        if (biayaTambahan == null || biayaTambahan.equals(""))
            biayaTambahan = "0";
        
        String jumlah = txtPemakaianJumlah.getText();
        String tanggal = txtPemakaianTanggal.getText();
        
        pemakaian.setBarang(barang);
        pemakaian.setBiayaTambahan(Long.valueOf(biayaTambahan));
        pemakaian.setJumlah(Integer.valueOf(jumlah));
        pemakaian.setKeterangan(txtPemakaianKeterangan.getText());
        pemakaian.setTanggal(DateUtil.getDate(tanggal));
        pemakaian.setPasien(pasien);
        pemakaian.setUnit(TokenHolder.getToken().getOperator().getUnit());
        pemakaian.setNomorResep(nomorResep);
        
        return pemakaian;
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
    
    private Pelayanan getPelayanan() {
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
        pelayanan.setUnit(this.unit);
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
        jLabel18 = new javax.swing.JLabel();
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
        txtPelayananSatuan = new javax.swing.JTextField();
        txtPelayananUnit = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        pnlPemakaian = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPemakaianBarang = new javax.swing.JTextField();
        txtPemakaianJumlah = new javax.swing.JTextField();
        txtPemakaianBiayaTambahan = new javax.swing.JTextField();
        txtPemakaianKeterangan = new javax.swing.JTextField();
        txtPemakaianTanggal = new javax.swing.JTextField();
        txtPemakaianSatuan = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PENAMBAHAN");
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel1.setLabelFor(txtKeyword);
        jLabel1.setText("KATA KUNCI");
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

        pnlPelayanan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PELAYANAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPelayanan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("Nama Tindakan");
        pnlPelayanan.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, 25));

        jLabel13.setText("Satuan");
        pnlPelayanan.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel18.setText("Jumlah");
        pnlPelayanan.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        jLabel14.setText("Biaya Tambahan");
        pnlPelayanan.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 90, 25));

        jLabel15.setText("Keterangan");
        pnlPelayanan.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 90, 25));

        jLabel16.setText("Unit");
        pnlPelayanan.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 90, 25));

        jLabel22.setText("Pelaksana");
        pnlPelayanan.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 90, 25));

        jLabel7.setText("Tipe Pelaksana");
        pnlPelayanan.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 90, 25));

        jLabel17.setText("Kelas Tindakan");
        pnlPelayanan.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 90, 25));
        pnlPelayanan.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 460, 10));
        pnlPelayanan.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 460, 10));

        txtPelayananTindakanNama.setEditable(false);
        pnlPelayanan.add(txtPelayananTindakanNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 270, 25));

        txtPelayananTindakanKelas.setEditable(false);
        pnlPelayanan.add(txtPelayananTindakanKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 270, 25));
        pnlPelayanan.add(txtPelayananJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 270, 25));
        pnlPelayanan.add(txtPelayananBiayaTambahan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, 270, 25));
        pnlPelayanan.add(txtPelayananKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 270, 25));
        pnlPelayanan.add(txtPelayananTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 270, 25));

        cbPelayananTipePelaksana.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "DOKTER", "PERAWAT" }));
        pnlPelayanan.add(cbPelayananTipePelaksana, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 270, 25));

        txtPelayananPelaksana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPelayananPelaksanaMouseClicked(evt);
            }
        });
        pnlPelayanan.add(txtPelayananPelaksana, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 270, 25));

        txtPelayananSatuan.setEditable(false);
        pnlPelayanan.add(txtPelayananSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 270, 25));

        txtPelayananUnit.setEditable(false);
        txtPelayananUnit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPelayananUnitMouseClicked(evt);
            }
        });
        pnlPelayanan.add(txtPelayananUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 270, 25));

        jLabel19.setText("Tanggal");
        pnlPelayanan.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 90, 25));

        getContentPane().add(pnlPelayanan);
        pnlPelayanan.setBounds(20, 290, 460, 390);

        pnlPemakaian.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PEMAKAIAN OBAT/BHP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPemakaian.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Satuan");
        pnlPemakaian.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, 25));

        jLabel3.setText("Jumlah");
        pnlPemakaian.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, 25));

        jLabel4.setText("Biaya Tambahan");
        pnlPemakaian.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 90, 25));

        jLabel5.setText("Keterangan");
        pnlPemakaian.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 90, 25));

        jLabel6.setText("Tanggal");
        pnlPemakaian.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 90, 25));

        jLabel8.setText("Nama Barang");
        pnlPemakaian.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, 25));

        txtPemakaianBarang.setEditable(false);
        pnlPemakaian.add(txtPemakaianBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 270, 25));
        pnlPemakaian.add(txtPemakaianJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 270, 25));
        pnlPemakaian.add(txtPemakaianBiayaTambahan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 270, 25));
        pnlPemakaian.add(txtPemakaianKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 270, 25));
        pnlPemakaian.add(txtPemakaianTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 270, 25));

        txtPemakaianSatuan.setEditable(false);
        pnlPemakaian.add(txtPemakaianSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 270, 25));

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
        
        if (clsDomain.equals(BahanHabisPakai.class) || clsDomain.equals(ObatFarmasi.class) || clsDomain.equals(Barang.class)) {
            barang = getBarang(index);
            txtPemakaianBarang.setText(barang.getNama());
            txtPemakaianSatuan.setText(barang.getSatuan());
        } else if (clsDomain.equals(Tindakan.class)) {
            tindakan = getTindakan(index);
            txtPelayananTindakanNama.setText(tindakan.getNama());
            txtPelayananTindakanKelas.setText(tindakan.getKelas().toString());
            txtPelayananSatuan.setText(tindakan.getSatuan().toString());
        }
    }//GEN-LAST:event_tblCariMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        try {
            if (clsDomain.equals(BahanHabisPakai.class) || clsDomain.equals(ObatFarmasi.class) || clsDomain.equals(Barang.class)) {
                pemakaian = getPemakaian();
                pemakaianService.simpan(pemakaian);

                ((BarangTableFrame)frame).reloadTable();
            } else if (clsDomain.equals(Tindakan.class)) {
                pelayanan = getPelayanan();
                pelayananService.simpan(pelayanan);
                
                ((TindakanTableFrame)frame).reloadTable();
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
            if (clsDomain.equals(BahanHabisPakai.class) || clsDomain.equals(ObatFarmasi.class) || clsDomain.equals(Barang.class)) {
                cariBarang(keyword);
            } else if (clsDomain.equals(Tindakan.class)) {
                cariTindakan(keyword);
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtKeywordFocusLost

    private void txtPelayananUnitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPelayananUnitMouseClicked
        FrameCari cari = new FrameCari(this,Unit.class);
        cari.setVisible(true);
    }//GEN-LAST:event_txtPelayananUnitMouseClicked

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
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JTextField txtPelayananSatuan;
    private javax.swing.JTextField txtPelayananTanggal;
    private javax.swing.JTextField txtPelayananTindakanKelas;
    private javax.swing.JTextField txtPelayananTindakanNama;
    private javax.swing.JTextField txtPelayananUnit;
    private javax.swing.JTextField txtPemakaianBarang;
    private javax.swing.JTextField txtPemakaianBiayaTambahan;
    private javax.swing.JTextField txtPemakaianJumlah;
    private javax.swing.JTextField txtPemakaianKeterangan;
    private javax.swing.JTextField txtPemakaianSatuan;
    private javax.swing.JTextField txtPemakaianTanggal;
    // End of variables declaration//GEN-END:variables
}
