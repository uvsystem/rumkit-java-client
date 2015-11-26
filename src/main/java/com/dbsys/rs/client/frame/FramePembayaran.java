package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.PembayaranPdfView;
import com.dbsys.rs.client.document.pdf.TagihanPdfView;
import com.dbsys.rs.client.tableModel.StokTableModel;
import com.dbsys.rs.client.tableModel.TagihanTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.PembayaranService;
import com.dbsys.rs.connector.service.StokService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.Pembayaran;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.Tagihan;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class FramePembayaran extends javax.swing.JFrame {

    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);
    private final PemakaianService pemakaianService = PemakaianService.getInstance(EventController.host);
    private final PembayaranService pembayaranService = PembayaranService.getInstance(EventController.host);
    private final StokService stokService = StokService.getInstance(EventController.host);
    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    
    private Pasien pasien;
    private Pembayaran pembayaran;
    private boolean isPrintPembayaranAvailable;
    private Long total;

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
    
    private Tagihan getTagihan(JTable table) {
        Integer index = table.getSelectedRow();
        TagihanTableModel tableModel = getTagihanTableModel(table);

        return tableModel.getTagihan(index);
    }
    
    private TagihanTableModel getTagihanTableModel(JTable table) {
        return (TagihanTableModel) table.getModel();
    }
    
    private void setDetailPasien(Pasien pasien) {
        if (pasien == null) {
            pasien = new Pasien();

            txtPendudukKelamin.setText(null);
            txtPendudukTanggalLahir.setText(null);
            txtPasienTanggungan.setText(null);
            cbPasienKeadaan.setSelectedIndex(0);
        } else {
            txtPendudukKelamin.setText(pasien.getKelamin().toString());
            txtPendudukTanggalLahir.setText(pasien.getTanggalLahir().toString());
            txtPasienTanggungan.setText(pasien.getPenanggung().toString());
            
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
    
    private void loadData() {
        String keyword = txtKeyword.getText();
        
        if (keyword.equals(""))
            return;
        
        try {
            pasien = pasienService.get(keyword);
            setDetailPasien(pasien);
            
            List<Pelayanan> listPelayanan = null;
            try {
                listPelayanan = pelayananService.getByPasien(pasien);
            } catch (ServiceException ex) {}
            
            List<Pemakaian> listPemakaian = null;
            try {
                listPemakaian = pemakaianService.getByPasien(pasien);
            } catch (ServiceException ex) {}
            
            try {
                listStokKembali = stokService.stokKembali(pasien);
            } catch (ServiceException ex) {
                listStokKembali = null;
            } finally {
                StokTableModel tableModel = new StokTableModel(listStokKembali);
                tblStokKembali.setModel(tableModel);
            }

            TagihanTableModel tableModel = new TagihanTableModel(null);
            tableModel.addListPelayanan(listPelayanan);
            tableModel.addListPemakaian(listPemakaian);
            
            tblSemua.setModel(tableModel);
            tblMenunggak.setModel(tableModel.filter(Tagihan.StatusTagihan.MENUNGGAK));
            tblBayar.setModel(new TagihanTableModel(null));
            
            listTagihan = tableModel.getList();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            total = 0L;
            String totalString = NumberFormat.getNumberInstance(Locale.US).format(total);
            lblTagihan.setText(String.format("Rp %s", totalString));
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
        pnlMenunggak = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMenunggak = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtTagihanBayar = new javax.swing.JTextField();
        btnBayarTagihan = new javax.swing.JButton();
        pnlBayar = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBayar = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtTagihanBatal = new javax.swing.JTextField();
        btnBatalTagihan = new javax.swing.JButton();
        pnlSemua = new javax.swing.JPanel();
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
        jLabel13 = new javax.swing.JLabel();
        txtPasienTanggungan = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        pnlKeluar = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cbPasienKeadaan = new javax.swing.JComboBox();
        btnPasienKeluar = new javax.swing.JButton();
        pnlPembayaran = new javax.swing.JPanel();
        lblTagihan = new javax.swing.JLabel();
        btnBayar = new javax.swing.JButton();
        btnCetakTagihan = new javax.swing.JButton();
        btnCetakPembayaran = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("RUMAH SAKIT UMUM LIUN KENDAGE TAHUNA");
        setBounds(new java.awt.Rectangle(0, 0, 1280, 800));
        setUndecorated(true);
        getContentPane().setLayout(null);

        pnlPencarian.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "CARI PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPencarian.setLayout(null);

        jLabel1.setText("Nomor Pasien");
        pnlPencarian.add(jLabel1);
        jLabel1.setBounds(20, 20, 90, 25);

        txtKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKeywordFocusLost(evt);
            }
        });
        pnlPencarian.add(txtKeyword);
        txtKeyword.setBounds(130, 20, 250, 25);

        getContentPane().add(pnlPencarian);
        pnlPencarian.setBounds(860, 110, 400, 60);

        tabData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabDataMouseClicked(evt);
            }
        });

        pnlMenunggak.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblMenunggak.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMenunggak.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMenunggakMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblMenunggak);

        pnlMenunggak.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 815, 450));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PEMBAYARAN TAGIHAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setText("TAGIHAN");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));
        jPanel1.add(txtTagihanBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 560, 25));

        btnBayarTagihan.setText("BAYAR");
        btnBayarTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarTagihanActionPerformed(evt);
            }
        });
        jPanel1.add(btnBayarTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, 90, 25));

        pnlMenunggak.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 810, 60));

        tabData.addTab("TAGIHAN MENUNGGAK", pnlMenunggak);

        pnlBayar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblBayar.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBayarMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblBayar);

        pnlBayar.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 815, 450));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PEMBATALAN MEMBAYAR TAGIHAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setText("TAGIHAN");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));
        jPanel2.add(txtTagihanBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 560, 25));

        btnBatalTagihan.setText("BATAL");
        btnBatalTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalTagihanActionPerformed(evt);
            }
        });
        jPanel2.add(btnBatalTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, 90, 25));

        pnlBayar.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 810, 60));

        tabData.addTab("BAYAR SEKARANG", pnlBayar);

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
        tblSemua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSemuaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSemua);

        javax.swing.GroupLayout pnlSemuaLayout = new javax.swing.GroupLayout(pnlSemua);
        pnlSemua.setLayout(pnlSemuaLayout);
        pnlSemuaLayout.setHorizontalGroup(
            pnlSemuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSemuaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlSemuaLayout.setVerticalGroup(
            pnlSemuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSemuaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabData.addTab("SEMUA TAGIHAN", pnlSemua);

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
        tblStokKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStokKembaliMouseClicked(evt);
            }
        });
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

        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
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
        txtPendudukKode.setBounds(130, 30, 250, 25);

        txtPendudukNama.setEditable(false);
        pnlDetail.add(txtPendudukNama);
        txtPendudukNama.setBounds(130, 60, 250, 25);

        txtPendudukNik.setEditable(false);
        pnlDetail.add(txtPendudukNik);
        txtPendudukNik.setBounds(130, 90, 250, 25);

        txtPendudukTanggalLahir.setEditable(false);
        pnlDetail.add(txtPendudukTanggalLahir);
        txtPendudukTanggalLahir.setBounds(130, 120, 250, 25);

        txtPendudukDarah.setEditable(false);
        pnlDetail.add(txtPendudukDarah);
        txtPendudukDarah.setBounds(130, 150, 250, 25);

        txtPendudukAgama.setEditable(false);
        pnlDetail.add(txtPendudukAgama);
        txtPendudukAgama.setBounds(130, 180, 250, 25);

        txtPendudukKelamin.setEditable(false);
        pnlDetail.add(txtPendudukKelamin);
        txtPendudukKelamin.setBounds(130, 210, 250, 25);

        txtPendudukTelepon.setEditable(false);
        pnlDetail.add(txtPendudukTelepon);
        txtPendudukTelepon.setBounds(130, 240, 250, 25);

        jLabel13.setText("Tanggungan");
        pnlDetail.add(jLabel13);
        jLabel13.setBounds(20, 270, 90, 25);

        txtPasienTanggungan.setEditable(false);
        pnlDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(130, 270, 250, 25);

        getContentPane().add(pnlDetail);
        pnlDetail.setBounds(860, 180, 400, 310);

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

        pnlKeluar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN KELUAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlKeluar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setText("Keadaan Pasien");
        pnlKeluar.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI" }));
        pnlKeluar.add(cbPasienKeadaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 250, 25));

        btnPasienKeluar.setText("PASIEN KELUAR");
        btnPasienKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluarActionPerformed(evt);
            }
        });
        pnlKeluar.add(btnPasienKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 110, 30));

        getContentPane().add(pnlKeluar);
        pnlKeluar.setBounds(860, 610, 400, 90);

        pnlPembayaran.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "TOTAL PEMBAYARAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPembayaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTagihan.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTagihan.setText("Rp 00.000.000.000");
        pnlPembayaran.add(lblTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 360, -1));

        btnBayar.setText("BAYAR");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        pnlPembayaran.add(btnBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 65, -1, -1));

        getContentPane().add(pnlPembayaran);
        pnlPembayaran.setBounds(860, 500, 400, 100);

        btnCetakTagihan.setText("CETAK TAGIHAN");
        btnCetakTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakTagihanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCetakTagihan);
        btnCetakTagihan.setBounds(1130, 710, 130, 50);

        btnCetakPembayaran.setText("CETAK STRUK");
        btnCetakPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPembayaranActionPerformed(evt);
            }
        });
        getContentPane().add(btnCetakPembayaran);
        btnCetakPembayaran.setBounds(995, 710, 120, 50);

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Admin_Bg.jpg"))); // NOI18N
        getContentPane().add(lblBackground);
        lblBackground.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKeywordFocusLost
        loadData();
        btnCetakTagihan.requestFocus();
        isPrintPembayaranAvailable = false;
    }//GEN-LAST:event_txtKeywordFocusLost

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Silahkan mencari pasien terlebih dahulu.");
            return;
        }
        
        if (total.equals(0L)) {
            JOptionPane.showMessageDialog(this, "Silahkan masukan jumlah pembayaran.");
            return;
        }

        pembayaran = new Pembayaran();
        pembayaran.setPasien(pasien);
        pembayaran.setTanggal(DateUtil.getDate());
        pembayaran.setJam(DateUtil.getTime());
        pembayaran.setJumlah(total);

        TagihanTableModel tableModel = (TagihanTableModel) tblBayar.getModel();
        for (Tagihan tagihan : tableModel.getList()) {
            if (tagihan instanceof Pelayanan) {
                pembayaran.addPelayanan((Pelayanan) tagihan);
            } else {
                pembayaran.addPemakaian((Pemakaian) tagihan);
            }
        }
        
        try {
            String kodePembayaran = pembayaran.generateKode();
            pembayaran.setKode(kodePembayaran);
            pembayaranService.bayar(pembayaran);
            loadData();

            isPrintPembayaranAvailable = true;
            JOptionPane.showMessageDialog(this, String.format("Pembayaran pasien berhasil dengan kode pembayaran = '%s'.\nSilahkan cetak struk pembayaran.", pembayaran.getKode()));
         } catch (ServiceException ex) {
            isPrintPembayaranAvailable = false;
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBayarActionPerformed

    private void btnCetakPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakPembayaranActionPerformed
        if (!isPrintPembayaranAvailable || pembayaran == null) {
            JOptionPane.showMessageDialog(this, "Silahkan melakukan pembayaran dahulu");
            return;
        }
            
        PdfProcessor pdfProcessor = new PdfProcessor();
        
        PembayaranPdfView pdfView = new PembayaranPdfView();
        Map<String, Object> model = new HashMap<>();
        model.put("pembayaran", pembayaran);
        
        try {
            pdfProcessor.generate(pdfView, model, String.format("E://print//%s.pdf", pdfView.getName()));
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakPembayaranActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameUtama().setVisible(true);
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

    private void btnBatalTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalTagihanActionPerformed
        Tagihan tagihan = getTagihan(tblBayar);

        total -= tagihan.getTagihanCounted();
        String totalString = NumberFormat.getNumberInstance(Locale.US).format(total);
        lblTagihan.setText(String.format("Rp %s", totalString));
        
        TagihanTableModel tableModelMenunggak = getTagihanTableModel(tblMenunggak);
        tableModelMenunggak.addTagihan(tagihan);
        tableModelMenunggak.fireTableDataChanged();
        
        TagihanTableModel tableModelBayar = getTagihanTableModel(tblBayar);
        tableModelBayar.removeTagihan(tagihan);
        tableModelBayar.fireTableDataChanged();
        
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_btnBatalTagihanActionPerformed

    private void btnBayarTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarTagihanActionPerformed
        Tagihan tagihan = getTagihan(tblMenunggak);
        
        total += tagihan.getTagihanCounted();
        String totalString = NumberFormat.getNumberInstance(Locale.US).format(total);
        lblTagihan.setText(String.format("Rp %s", totalString));
        
        TagihanTableModel tableModelMenunggak = getTagihanTableModel(tblMenunggak);
        tableModelMenunggak.removeTagihan(tagihan);
        tableModelMenunggak.fireTableDataChanged();
        
        TagihanTableModel tableModelBayar = getTagihanTableModel(tblBayar);
        tableModelBayar.addTagihan(tagihan);
        tableModelBayar.fireTableDataChanged();
        
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_btnBayarTagihanActionPerformed

    private void tblMenunggakMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenunggakMouseClicked
        Tagihan tagihan = getTagihan(tblMenunggak);
        txtTagihanBayar.setText(tagihan.getNama());
        
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_tblMenunggakMouseClicked

    private void tblBayarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBayarMouseClicked
        Tagihan tagihan = getTagihan(tblBayar);
        txtTagihanBatal.setText(tagihan.getNama());
        
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_tblBayarMouseClicked

    private void btnCetakTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakTagihanActionPerformed
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Silahkan cari data pasien dahulu");
            return;
        }
            
        PdfProcessor pdfProcessor = new PdfProcessor();
        
        TagihanPdfView pdfView = new TagihanPdfView();
        Map<String, Object> model = new HashMap<>();
        model.put("pasien", pasien);
        model.put("listTagihan", listTagihan);
        
        try {
            pdfProcessor.generate(pdfView, model, String.format("E://print//%s.pdf", pdfView.getName()));
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakTagihanActionPerformed

    private void tabDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabDataMouseClicked
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_tabDataMouseClicked

    private void tblSemuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSemuaMouseClicked
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_tblSemuaMouseClicked

    private void tblStokKembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStokKembaliMouseClicked
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_tblStokKembaliMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalTagihan;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnBayarTagihan;
    private javax.swing.JButton btnCetakPembayaran;
    private javax.swing.JButton btnCetakTagihan;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JComboBox cbPasienKeadaan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblTagihan;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel pnlBayar;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlKeluar;
    private javax.swing.JPanel pnlMenunggak;
    private javax.swing.JPanel pnlPembayaran;
    private javax.swing.JPanel pnlPencarian;
    private javax.swing.JPanel pnlSemua;
    private javax.swing.JPanel pnlStok;
    private javax.swing.JScrollPane scrollObat;
    private javax.swing.JTabbedPane tabData;
    private javax.swing.JTable tblBayar;
    private javax.swing.JTable tblMenunggak;
    private javax.swing.JTable tblSemua;
    private javax.swing.JTable tblStokKembali;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPendudukAgama;
    private javax.swing.JTextField txtPendudukDarah;
    private javax.swing.JTextField txtPendudukKelamin;
    private javax.swing.JTextField txtPendudukKode;
    private javax.swing.JTextField txtPendudukNama;
    private javax.swing.JTextField txtPendudukNik;
    private javax.swing.JTextField txtPendudukTanggalLahir;
    private javax.swing.JTextField txtPendudukTelepon;
    private javax.swing.JTextField txtTagihanBatal;
    private javax.swing.JTextField txtTagihanBayar;
    // End of variables declaration//GEN-END:variables
}
