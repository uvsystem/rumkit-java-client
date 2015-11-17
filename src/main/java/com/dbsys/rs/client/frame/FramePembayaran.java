package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.TagihanPdfView;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.client.tableModel.StokTableModel;
import com.dbsys.rs.client.tableModel.TagihanTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.StokService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokKembali;
import com.dbsys.rs.lib.entity.Tagihan;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class FramePembayaran extends javax.swing.JFrame {

    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);
    private final PemakaianService pemakaianService = PemakaianService.getInstance(EventController.host);
    private final StokService stokService = StokService.getInstance(EventController.host);
    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    
    private Pasien pasien;
    private Long total = 0L;
    private List<Pelayanan> listPelayanan;
    private List<Pemakaian> listPemakaian;
    private List<Stok> listStokKembali;
    
    private List<Tagihan> listTagihan;
    
    /**
     * Creates new form FramePembayaran
     */
    public FramePembayaran() {
        initComponents();
        setSize(1280, 800);
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
    }
    
    private void setDetailPasien(Pasien pasien) {
        if (pasien == null) {
            pasien = new Pasien();

            txtPendudukKelamin.setText(null);
            txtPendudukTanggalLahir.setText(null);
            txtPasienTanggungan.setText(null);
            txtPasienTanggalMasuk.setText(null);
            cbPasienKeadaan.setSelectedIndex(0);
        } else {
            txtPendudukKelamin.setText(pasien.getKelamin().toString());
            txtPendudukTanggalLahir.setText(pasien.getTanggalLahir().toString());
            txtPasienTanggungan.setText(pasien.getPenanggung().toString());
            txtPasienTanggalMasuk.setText(pasien.getTanggalMasuk().toString());
            
            Pasien.KeadaanPasien keadaan = pasien.getKeadaan();
            if (keadaan != null)
                cbPasienKeadaan.setSelectedItem(pasien.getKeadaan().toString());
        }
        
        txtPendudukKode.setText(pasien.getKodePenduduk());
        txtPendudukNik.setText(pasien.getNik());
        txtPendudukNama.setText(pasien.getNama());
        txtPendudukDarah.setText(pasien.getDarah());
        txtPendudukAgama.setText(pasien.getAgama());
        txtPendudukTelepon.setText(pasien.getTelepon());
    }
    
    private List<Stok> loadStokKembali(final Pasien pasien) throws ServiceException {
        if (pasien == null)
            return null;

        List<Stok> list = stokService.stokKembali(pasien);
        StokTableModel tableModel = new StokTableModel(list);
        tblStokKembali.setModel(tableModel);
        
        return list;
    }
    
    private void loadData() {
        total = 0L;
        String keyword = txtKeyword.getText();
        
        if (keyword.equals(""))
            return;
        
        try {
            pasien = pasienService.get(keyword);
            setDetailPasien(pasien);
            
            try {
                listPelayanan = pelayananService.getByPasien(pasien);
                for (Pelayanan pelayanan : listPelayanan)
                    total += pelayanan.hitungTagihan();
            } catch (ServiceException ex) {}
            
            try {
                listPemakaian = pemakaianService.getByPasien(pasien.getId());
                for (Pemakaian pemakaian : listPemakaian)
                    total += pemakaian.hitungTagihan();
            } catch (ServiceException ex) {}
            
            try {
                listStokKembali = loadStokKembali(pasien);
                for (Stok stok : listStokKembali)
                    total -= ((StokKembali)stok).hitungPengembalian();
            } catch (ServiceException ex) {}

            TagihanTableModel tableModel = new TagihanTableModel(null);
            tableModel.addListPelayanan(listPelayanan);
            tableModel.addListPemakaian(listPemakaian);
            
            listTagihan = tableModel.getList();
            tblSemua.setModel(tableModel);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            Long cicilan = 0L;
            if (pasien.getCicilan() != null)
                cicilan = pasien.getCicilan();
            
            Long sisa = total - cicilan;
            String totalString = NumberFormat.getNumberInstance(Locale.US).format(sisa);

            lblTagihan.setText(String.format("Rp %s", totalString));
            txtPasienCicilan.setText(sisa.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPencarian = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        tabData = new javax.swing.JTabbedPane();
        pnlTagihan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSemua = new javax.swing.JTable();
        pnlStok = new javax.swing.JPanel();
        scrollObat = new javax.swing.JScrollPane();
        tblStokKembali = new javax.swing.JTable();
        pnlDetail = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtPendudukKode = new javax.swing.JTextField();
        txtPendudukNama = new javax.swing.JTextField();
        txtPendudukNik = new javax.swing.JTextField();
        txtPendudukTanggalLahir = new javax.swing.JTextField();
        txtPendudukDarah = new javax.swing.JTextField();
        txtPendudukAgama = new javax.swing.JTextField();
        txtPendudukKelamin = new javax.swing.JTextField();
        txtPendudukTelepon = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtPasienTanggungan = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        cbPasienKeadaan = new javax.swing.JComboBox();
        btnPasienKeluar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        lblTagihan = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtPasienCicilan = new javax.swing.JTextField();
        btnCetak = new javax.swing.JButton();
        btnBayar = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("RUMAH SAKIT UMUM LIUN KENDAGE TAHUNA");
        setBounds(new java.awt.Rectangle(0, 0, 1280, 800));
        setUndecorated(true);
        getContentPane().setLayout(null);

        pnlPencarian.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cari Pasien"));
        pnlPencarian.setLayout(null);

        jLabel1.setText("Nomor Pasien");
        pnlPencarian.add(jLabel1);
        jLabel1.setBounds(20, 30, 90, 25);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        pnlPencarian.add(txtKeyword);
        txtKeyword.setBounds(130, 30, 240, 25);

        getContentPane().add(pnlPencarian);
        pnlPencarian.setBounds(860, 100, 400, 70);

        tblSemua.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblSemua);

        javax.swing.GroupLayout pnlTagihanLayout = new javax.swing.GroupLayout(pnlTagihan);
        pnlTagihan.setLayout(pnlTagihanLayout);
        pnlTagihanLayout.setHorizontalGroup(
            pnlTagihanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTagihanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTagihanLayout.setVerticalGroup(
            pnlTagihanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTagihanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabData.addTab("TAGIHAN", pnlTagihan);

        tblStokKembali.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollObat.setViewportView(tblStokKembali);

        javax.swing.GroupLayout pnlStokLayout = new javax.swing.GroupLayout(pnlStok);
        pnlStok.setLayout(pnlStokLayout);
        pnlStokLayout.setHorizontalGroup(
            pnlStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStokLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollObat, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlStokLayout.setVerticalGroup(
            pnlStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStokLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollObat, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabData.addTab("STOK KEMBALI", pnlStok);

        getContentPane().add(tabData);
        tabData.setBounds(10, 180, 840, 580);

        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Data Pasien"));
        pnlDetail.setLayout(null);

        jLabel5.setText("No. Rekam Medik");
        pnlDetail.add(jLabel5);
        jLabel5.setBounds(20, 30, 90, 25);

        jLabel6.setText("Nama Pasien");
        pnlDetail.add(jLabel6);
        jLabel6.setBounds(20, 60, 90, 25);

        jLabel7.setText("NIK");
        pnlDetail.add(jLabel7);
        jLabel7.setBounds(20, 90, 90, 25);

        jLabel8.setText("Tanggal Lahir");
        pnlDetail.add(jLabel8);
        jLabel8.setBounds(20, 120, 90, 25);

        jLabel9.setText("Golongan Darah");
        pnlDetail.add(jLabel9);
        jLabel9.setBounds(20, 150, 90, 25);

        jLabel10.setText("Agama");
        pnlDetail.add(jLabel10);
        jLabel10.setBounds(20, 180, 90, 25);

        jLabel11.setText("Jenis Kelamin");
        pnlDetail.add(jLabel11);
        jLabel11.setBounds(20, 210, 90, 25);

        jLabel12.setText("Telepon");
        pnlDetail.add(jLabel12);
        jLabel12.setBounds(20, 240, 90, 25);

        txtPendudukKode.setEditable(false);
        pnlDetail.add(txtPendudukKode);
        txtPendudukKode.setBounds(130, 30, 240, 25);

        txtPendudukNama.setEditable(false);
        pnlDetail.add(txtPendudukNama);
        txtPendudukNama.setBounds(130, 60, 240, 25);

        txtPendudukNik.setEditable(false);
        pnlDetail.add(txtPendudukNik);
        txtPendudukNik.setBounds(130, 90, 240, 25);

        txtPendudukTanggalLahir.setEditable(false);
        pnlDetail.add(txtPendudukTanggalLahir);
        txtPendudukTanggalLahir.setBounds(130, 120, 240, 25);

        txtPendudukDarah.setEditable(false);
        pnlDetail.add(txtPendudukDarah);
        txtPendudukDarah.setBounds(130, 150, 240, 25);

        txtPendudukAgama.setEditable(false);
        pnlDetail.add(txtPendudukAgama);
        txtPendudukAgama.setBounds(130, 180, 240, 25);

        txtPendudukKelamin.setEditable(false);
        pnlDetail.add(txtPendudukKelamin);
        txtPendudukKelamin.setBounds(130, 210, 240, 25);

        txtPendudukTelepon.setEditable(false);
        pnlDetail.add(txtPendudukTelepon);
        txtPendudukTelepon.setBounds(130, 240, 240, 25);
        pnlDetail.add(jSeparator2);
        jSeparator2.setBounds(0, 270, 400, 10);

        jLabel13.setText("Tanggungan");
        pnlDetail.add(jLabel13);
        jLabel13.setBounds(20, 290, 90, 25);

        jLabel14.setText("Tanggal Masuk");
        pnlDetail.add(jLabel14);
        jLabel14.setBounds(20, 320, 90, 25);

        jLabel18.setText("Keadaan Pasien");
        pnlDetail.add(jLabel18);
        jLabel18.setBounds(20, 350, 90, 25);

        txtPasienTanggungan.setEditable(false);
        pnlDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(130, 290, 240, 25);

        txtPasienTanggalMasuk.setEditable(false);
        pnlDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(130, 320, 240, 25);

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI" }));
        pnlDetail.add(cbPasienKeadaan);
        cbPasienKeadaan.setBounds(130, 350, 240, 25);

        btnPasienKeluar.setText("PASIEN KELUAR");
        btnPasienKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluarActionPerformed(evt);
            }
        });
        pnlDetail.add(btnPasienKeluar);
        btnPasienKeluar.setBounds(260, 380, 110, 30);
        pnlDetail.add(jSeparator3);
        jSeparator3.setBounds(0, 420, 400, 10);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("TOTAL");
        pnlDetail.add(jLabel15);
        jLabel15.setBounds(20, 430, 90, 15);

        lblTagihan.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTagihan.setText("Rp 00.000.000.000");
        pnlDetail.add(lblTagihan);
        lblTagihan.setBounds(20, 450, 350, 40);

        jLabel16.setText("Pembayaran");
        pnlDetail.add(jLabel16);
        jLabel16.setBounds(20, 500, 90, 25);
        pnlDetail.add(txtPasienCicilan);
        txtPasienCicilan.setBounds(130, 500, 240, 25);

        btnCetak.setText("CETAK");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        pnlDetail.add(btnCetak);
        btnCetak.setBounds(130, 530, 110, 40);

        btnBayar.setText("BAYAR");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        pnlDetail.add(btnBayar);
        btnBayar.setBounds(260, 530, 110, 40);

        getContentPane().add(pnlDetail);
        pnlDetail.setBounds(860, 180, 400, 580);

        jToolBar1.setRollover(true);

        jLabel2.setText("LOGIN SEBAGAI : ");
        jToolBar1.add(jLabel2);

        lblOperator.setText("jLabel3");
        jToolBar1.add(lblOperator);

        jLabel3.setText("  -  ");
        jToolBar1.add(jLabel3);

        jLabel4.setText("UNIT : ");
        jToolBar1.add(jLabel4);

        lblUnit.setText("jLabel5");
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

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Admin_Bg.jpg"))); // NOI18N
        getContentPane().add(lblBackground);
        lblBackground.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKeywordFocusLost
        loadData();
    }//GEN-LAST:event_txtKeywordFocusLost

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Silahkan mencari pasien terlebih dahulu.");
            return;
        }
        
        String jumlah = txtPasienCicilan.getText();
        if (jumlah.equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan masukan jumlah pembayaran.");
            return;
        }
        
        try {
            pasienService.bayar(pasien, Long.valueOf(jumlah));
            JOptionPane.showMessageDialog(this, "Pembayaran pasien berhasil. Silahkan cetak struk pembayaran.");
            loadData();
         } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBayarActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        PdfProcessor pdfProcessor = new PdfProcessor();
        
        TagihanPdfView pdfView = new TagihanPdfView();
        Map<String, Object> model = new HashMap<>();
        model.put("list", listTagihan);
        model.put("pasien", pasien);
        
        try {
            pdfProcessor.generate(pdfView, model, "E://test.pdf");
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameLogin().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnPasienKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienKeluarActionPerformed
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
            JOptionPane.showMessageDialog(this, "Berhasil! Silahkan mengisi pembayaran.");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienKeluarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JComboBox cbPasienKeadaan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblTagihan;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlPencarian;
    private javax.swing.JPanel pnlStok;
    private javax.swing.JPanel pnlTagihan;
    private javax.swing.JScrollPane scrollObat;
    private javax.swing.JTabbedPane tabData;
    private javax.swing.JTable tblSemua;
    private javax.swing.JTable tblStokKembali;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtPasienCicilan;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPendudukAgama;
    private javax.swing.JTextField txtPendudukDarah;
    private javax.swing.JTextField txtPendudukKelamin;
    private javax.swing.JTextField txtPendudukKode;
    private javax.swing.JTextField txtPendudukNama;
    private javax.swing.JTextField txtPendudukNik;
    private javax.swing.JTextField txtPendudukTanggalLahir;
    private javax.swing.JTextField txtPendudukTelepon;
    // End of variables declaration//GEN-END:variables
}
