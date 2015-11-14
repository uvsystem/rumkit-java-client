package com.dbsys.rs.client;

import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.TagihanPdfView;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.client.tableModel.TagihanTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Pemakaian;
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
    private final PemakaianService pemakaianBhpService = PemakaianService.getInstance(EventController.host);
    private final PemakaianService pemakaianObatService = PemakaianService.getInstance(EventController.host);
    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    
    private Pasien pasien;
    private Long total = 0L;
    private List<Pelayanan> listPelayanan;
    private List<Pemakaian> listPemakaianBhp;
    private List<Pemakaian> listPemakaianObat;
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
    
    private List<Pelayanan> loadTabelTindakan(final Pasien pasien) throws ServiceException {
        if (pasien == null)
            return null;

        List<Pelayanan> list = pelayananService.getByPasien(pasien);
        PelayananTableModel tableModel = new PelayananTableModel(list);
        tblTindakan.setModel(tableModel);
        
        return list;
    }
    
    private List<Pemakaian> loadTabelBhp(final Pasien pasien) throws ServiceException {
        if (pasien == null)
            return null;

        List<Pemakaian> list = pemakaianBhpService.getByPasien(pasien.getId());

        PemakaianTableModel tableModel = new PemakaianTableModel(list);
        tblBhp.setModel(tableModel);
        
        return list;
    }
    
    private List<Pemakaian> loadTabelObat(final Pasien pasien) throws ServiceException {
        if (pasien == null)
            return null;

        List<Pemakaian> list = pemakaianObatService.getByPasien(pasien.getId());

        PemakaianTableModel tableModel = new PemakaianTableModel(list);
        tblObat.setModel(tableModel);
        
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
                listPelayanan = loadTabelTindakan(pasien);
                for (Pelayanan pelayanan : listPelayanan)
                    total += pelayanan.hitungTagihan();
            } catch (ServiceException ex) {}
            
            try {
                listPemakaianBhp = loadTabelBhp(pasien);
                for (Pemakaian pemakaian : listPemakaianBhp)
                    total += pemakaian.hitungTagihan();
            } catch (ServiceException ex) {}
            
            try {
                listPemakaianObat = loadTabelObat(pasien);
                for (Pemakaian pemakaian : listPemakaianObat)
                    total += pemakaian.hitungTagihan();
            } catch (ServiceException ex) {}

            TagihanTableModel tableModel = new TagihanTableModel(null);
            tableModel.addListPelayanan(listPelayanan);
            tableModel.addListPemakaian(listPemakaianBhp);
            tableModel.addListPemakaian(listPemakaianObat);
            
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
        pnlSummary = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSemua = new javax.swing.JTable();
        pnlTindakan = new javax.swing.JPanel();
        scrollTindakan = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        pnlBhp = new javax.swing.JPanel();
        scrollBhp = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        pnlObat = new javax.swing.JPanel();
        scrollObat = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();
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
        jLabel1.setBounds(30, 30, 90, 14);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        pnlPencarian.add(txtKeyword);
        txtKeyword.setBounds(140, 30, 240, 20);

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

        javax.swing.GroupLayout pnlSummaryLayout = new javax.swing.GroupLayout(pnlSummary);
        pnlSummary.setLayout(pnlSummaryLayout);
        pnlSummaryLayout.setHorizontalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlSummaryLayout.setVerticalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabData.addTab("SEMUA", pnlSummary);

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
        scrollTindakan.setViewportView(tblTindakan);

        javax.swing.GroupLayout pnlTindakanLayout = new javax.swing.GroupLayout(pnlTindakan);
        pnlTindakan.setLayout(pnlTindakanLayout);
        pnlTindakanLayout.setHorizontalGroup(
            pnlTindakanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTindakanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTindakan, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTindakanLayout.setVerticalGroup(
            pnlTindakanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTindakanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTindakan, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabData.addTab("TINDAKAN", pnlTindakan);

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
        scrollBhp.setViewportView(tblBhp);

        javax.swing.GroupLayout pnlBhpLayout = new javax.swing.GroupLayout(pnlBhp);
        pnlBhp.setLayout(pnlBhpLayout);
        pnlBhpLayout.setHorizontalGroup(
            pnlBhpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBhpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollBhp, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlBhpLayout.setVerticalGroup(
            pnlBhpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBhpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollBhp, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabData.addTab("BAHAN HABIS PAKAI", pnlBhp);

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
        scrollObat.setViewportView(tblObat);

        javax.swing.GroupLayout pnlObatLayout = new javax.swing.GroupLayout(pnlObat);
        pnlObat.setLayout(pnlObatLayout);
        pnlObatLayout.setHorizontalGroup(
            pnlObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlObatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollObat, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlObatLayout.setVerticalGroup(
            pnlObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlObatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollObat, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabData.addTab("OBAT", pnlObat);

        getContentPane().add(tabData);
        tabData.setBounds(10, 180, 840, 580);

        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Data Pasien"));
        pnlDetail.setLayout(null);

        jLabel5.setText("No. Rekam Medik");
        pnlDetail.add(jLabel5);
        jLabel5.setBounds(30, 30, 90, 14);

        jLabel6.setText("Nama Pasien");
        pnlDetail.add(jLabel6);
        jLabel6.setBounds(30, 60, 90, 14);

        jLabel7.setText("NIK");
        pnlDetail.add(jLabel7);
        jLabel7.setBounds(30, 90, 90, 14);

        jLabel8.setText("Tanggal Lahir");
        pnlDetail.add(jLabel8);
        jLabel8.setBounds(30, 120, 90, 14);

        jLabel9.setText("Golongan Darah");
        pnlDetail.add(jLabel9);
        jLabel9.setBounds(30, 150, 90, 14);

        jLabel10.setText("Agama");
        pnlDetail.add(jLabel10);
        jLabel10.setBounds(30, 180, 90, 14);

        jLabel11.setText("Jenis Kelamin");
        pnlDetail.add(jLabel11);
        jLabel11.setBounds(30, 210, 90, 14);

        jLabel12.setText("Telepon");
        pnlDetail.add(jLabel12);
        jLabel12.setBounds(30, 240, 90, 14);

        txtPendudukKode.setEditable(false);
        pnlDetail.add(txtPendudukKode);
        txtPendudukKode.setBounds(140, 30, 240, 20);

        txtPendudukNama.setEditable(false);
        pnlDetail.add(txtPendudukNama);
        txtPendudukNama.setBounds(140, 60, 240, 20);

        txtPendudukNik.setEditable(false);
        pnlDetail.add(txtPendudukNik);
        txtPendudukNik.setBounds(140, 90, 240, 20);

        txtPendudukTanggalLahir.setEditable(false);
        pnlDetail.add(txtPendudukTanggalLahir);
        txtPendudukTanggalLahir.setBounds(140, 120, 240, 20);

        txtPendudukDarah.setEditable(false);
        pnlDetail.add(txtPendudukDarah);
        txtPendudukDarah.setBounds(140, 150, 240, 20);

        txtPendudukAgama.setEditable(false);
        pnlDetail.add(txtPendudukAgama);
        txtPendudukAgama.setBounds(140, 180, 240, 20);

        txtPendudukKelamin.setEditable(false);
        pnlDetail.add(txtPendudukKelamin);
        txtPendudukKelamin.setBounds(140, 210, 240, 20);

        txtPendudukTelepon.setEditable(false);
        pnlDetail.add(txtPendudukTelepon);
        txtPendudukTelepon.setBounds(140, 240, 240, 20);
        pnlDetail.add(jSeparator2);
        jSeparator2.setBounds(0, 270, 400, 10);

        jLabel13.setText("Tanggungan");
        pnlDetail.add(jLabel13);
        jLabel13.setBounds(30, 290, 90, 14);

        jLabel14.setText("Tanggal Masuk");
        pnlDetail.add(jLabel14);
        jLabel14.setBounds(30, 320, 90, 14);

        jLabel18.setText("Keadaan Pasien");
        pnlDetail.add(jLabel18);
        jLabel18.setBounds(30, 350, 90, 14);

        txtPasienTanggungan.setEditable(false);
        pnlDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(140, 290, 240, 20);

        txtPasienTanggalMasuk.setEditable(false);
        pnlDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(140, 320, 240, 20);

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI" }));
        pnlDetail.add(cbPasienKeadaan);
        cbPasienKeadaan.setBounds(140, 350, 240, 20);

        btnPasienKeluar.setText("PASIEN KELUAR");
        btnPasienKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluarActionPerformed(evt);
            }
        });
        pnlDetail.add(btnPasienKeluar);
        btnPasienKeluar.setBounds(270, 380, 110, 30);
        pnlDetail.add(jSeparator3);
        jSeparator3.setBounds(0, 420, 400, 10);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("TOTAL");
        pnlDetail.add(jLabel15);
        jLabel15.setBounds(30, 430, 90, 15);

        lblTagihan.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTagihan.setText("Rp 00.000.000.000");
        pnlDetail.add(lblTagihan);
        lblTagihan.setBounds(10, 450, 350, 40);

        jLabel16.setText("Pembayaran");
        pnlDetail.add(jLabel16);
        jLabel16.setBounds(30, 500, 90, 14);
        pnlDetail.add(txtPasienCicilan);
        txtPasienCicilan.setBounds(140, 500, 240, 20);

        btnCetak.setText("CETAK");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });
        pnlDetail.add(btnCetak);
        btnCetak.setBounds(140, 530, 110, 40);

        btnBayar.setText("BAYAR");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        pnlDetail.add(btnBayar);
        btnBayar.setBounds(270, 530, 110, 40);

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
            pasien = pasienService.keluar(pasien, Pasien.KeadaanPasien.valueOf(keadaan), Pasien.StatusPasien.LUNAS);
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
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlObat;
    private javax.swing.JPanel pnlPencarian;
    private javax.swing.JPanel pnlSummary;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JScrollPane scrollBhp;
    private javax.swing.JScrollPane scrollObat;
    private javax.swing.JScrollPane scrollTindakan;
    private javax.swing.JTabbedPane tabData;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblObat;
    private javax.swing.JTable tblSemua;
    private javax.swing.JTable tblTindakan;
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
