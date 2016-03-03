package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.PembayaranPdfView;
import com.dbsys.rs.client.document.pdf.RekapPasienPdfView;
import com.dbsys.rs.client.document.pdf.TagihanPdfView;
import com.dbsys.rs.client.tableModel.StokKembaliTableModel;
import com.dbsys.rs.client.tableModel.TagihanTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.adapter.RekapTagihanAdapter;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.PembayaranService;
import com.dbsys.rs.connector.service.ReportService;
import com.dbsys.rs.connector.service.StokService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.Pemakaian;
import com.dbsys.rs.client.entity.Pembayaran;
import com.dbsys.rs.client.entity.StokKembali;
import com.dbsys.rs.client.entity.Tagihan;
import com.dbsys.rs.client.entity.Unit;

import java.text.NumberFormat;
import java.util.ArrayList;
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
public class LoketPembayaran extends javax.swing.JFrame {

    private final PasienService pasienService = PasienService.getInstance();
    private final PelayananService pelayananService = PelayananService.getInstance();
    private final PemakaianService pemakaianService = PemakaianService.getInstance();
    private final PembayaranService pembayaranService = PembayaranService.getInstance();
    private final StokService stokService = StokService.getInstance();
    private final TokenService tokenService = TokenService.getInstance();
    private final ReportService reportService = ReportService.getInstance();
    
    private Pasien pasien;
    private Pembayaran pembayaran;
    private boolean isPrintPembayaranAvailable;
    private Long total;

    private List<StokKembali> listStokKembali;
    private List<Tagihan> listTagihan;

    /**
     * Creates new form FramePembayaran
     */
    public LoketPembayaran() {
        initComponents();
        setSize(1280, 800);
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
        
        btnRekapPelayanan.setVisible(false);
        btnRekapPemakaian.setVisible(false);
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
                listStokKembali = new ArrayList<>();
            } finally {
                StokKembaliTableModel tableModel = new StokKembaliTableModel(listStokKembali);
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
            setTotalLabel(total);
        }
    }

    private void setTotalLabel(Long total) {
        String totalString = NumberFormat.getNumberInstance(Locale.US).format(total);
        lblTagihan.setText(String.format("Rp %s", totalString));
    }
    
    private void addTotal(Long totalAdd) {
        total += totalAdd;
        setTotalLabel(total);
    }
    
    private void substractTotal(Long totalSubstract) {
        total -= totalSubstract;
        setTotalLabel(total);
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
        btnBayarSemuaTagihan = new javax.swing.JButton();
        pnlBayar = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBayar = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtTagihanBatal = new javax.swing.JTextField();
        btnBatalTagihan = new javax.swing.JButton();
        btnBatalSemuaTagihan = new javax.swing.JButton();
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
        btnRekapTagihanUmum = new javax.swing.JButton();
        btnRekapTagihanBpjs = new javax.swing.JButton();
        btnRekapPembayaran = new javax.swing.JButton();
        btnUbahPasien = new javax.swing.JButton();
        btnCetakTunggakanPasien = new javax.swing.JButton();
        btnRekapPemakaian = new javax.swing.JButton();
        btnRekapPelayanan = new javax.swing.JButton();
        pnlKeluar = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cbPasienKeadaan = new javax.swing.JComboBox();
        btnPasienKeluar = new javax.swing.JButton();
        pnlPembayaran = new javax.swing.JPanel();
        lblTagihan = new javax.swing.JLabel();
        btnBayar = new javax.swing.JButton();
        btnCetakPembayaran = new javax.swing.JButton();
        btnCetakTagihan = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("RUMAH SAKIT UMUM LIUN KENDAGE TAHUNA");
        setBounds(new java.awt.Rectangle(0, 0, 1280, 800));
        setUndecorated(true);
        getContentPane().setLayout(null);

        pnlPencarian.setBackground(Utama.colorTransparentPanel);
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
        txtKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeywordKeyPressed(evt);
            }
        });
        pnlPencarian.add(txtKeyword);
        txtKeyword.setBounds(130, 20, 250, 25);

        getContentPane().add(pnlPencarian);
        pnlPencarian.setBounds(860, 110, 400, 60);

        tabData.setBackground(Utama.colorTransparentPanel);
        tabData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabDataMouseClicked(evt);
            }
        });

        pnlMenunggak.setBackground(Utama.colorTransparentPanel);
        pnlMenunggak.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBackground(Utama.colorTransparentPanel);

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

        pnlMenunggak.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 21, 800, 450));

        jPanel1.setBackground(Utama.colorTransparentPanel);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PEMBAYARAN TAGIHAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setText("TAGIHAN");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        txtTagihanBayar.setEditable(false);
        jPanel1.add(txtTagihanBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 470, 25));

        btnBayarTagihan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_bayar.png"))); // NOI18N
        btnBayarTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarTagihanActionPerformed(evt);
            }
        });
        jPanel1.add(btnBayarTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 17, 80, 30));

        btnBayarSemuaTagihan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_semua.png"))); // NOI18N
        btnBayarSemuaTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarSemuaTagihanActionPerformed(evt);
            }
        });
        jPanel1.add(btnBayarSemuaTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 17, 80, 30));

        pnlMenunggak.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 800, 60));

        tabData.addTab("TAGIHAN MENUNGGAK", pnlMenunggak);

        pnlBayar.setBackground(Utama.colorTransparentPanel);
        pnlBayar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setBackground(Utama.colorTransparentPanel);

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

        jPanel2.setBackground(Utama.colorTransparentPanel);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PEMBATALAN MEMBAYAR TAGIHAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setText("TAGIHAN");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        txtTagihanBatal.setEditable(false);
        jPanel2.add(txtTagihanBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 470, 25));

        btnBatalTagihan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_batal.png"))); // NOI18N
        btnBatalTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalTagihanActionPerformed(evt);
            }
        });
        jPanel2.add(btnBatalTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 17, 80, 30));

        btnBatalSemuaTagihan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_semua.png"))); // NOI18N
        btnBatalSemuaTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalSemuaTagihanActionPerformed(evt);
            }
        });
        jPanel2.add(btnBatalSemuaTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 17, 80, 30));

        pnlBayar.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 810, 60));

        tabData.addTab("BAYAR SEKARANG", pnlBayar);

        pnlSemua.setBackground(Utama.colorTransparentPanel);

        jScrollPane1.setBackground(Utama.colorTransparentPanel);

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

        pnlStok.setBackground(Utama.colorTransparentPanel);

        scrollObat.setBackground(Utama.colorTransparentPanel);

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

        pnlDetail.setBackground(Utama.colorTransparentPanel);
        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlDetail.setLayout(null);

        jLabel5.setText("No. Rekam Medik");
        pnlDetail.add(jLabel5);
        jLabel5.setBounds(20, 30, 90, 25);

        jLabel6.setText("Nama Pasien");
        pnlDetail.add(jLabel6);
        jLabel6.setBounds(20, 60, 90, 25);

        jLabel7.setText("No. Jaminan");
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
        jLabel3.setMaximumSize(new java.awt.Dimension(25, 14));
        jToolBar1.add(jLabel3);

        jLabel4.setText("UNIT : ");
        jToolBar1.add(jLabel4);

        lblUnit.setText("jLabel5");
        jToolBar1.add(lblUnit);

        jSeparator1.setMaximumSize(new java.awt.Dimension(20, 32767));
        jToolBar1.add(jSeparator1);

        btnLogout.setText("LOGOUT");
        btnLogout.setFocusable(false);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setMaximumSize(new java.awt.Dimension(80, 21));
        btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLogout);

        btnRekapTagihanUmum.setText("REKAP TAGIHAN UMUM");
        btnRekapTagihanUmum.setFocusable(false);
        btnRekapTagihanUmum.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRekapTagihanUmum.setMaximumSize(new java.awt.Dimension(120, 21));
        btnRekapTagihanUmum.setMinimumSize(new java.awt.Dimension(120, 21));
        btnRekapTagihanUmum.setPreferredSize(new java.awt.Dimension(100, 21));
        btnRekapTagihanUmum.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRekapTagihanUmum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapTagihanUmumActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRekapTagihanUmum);

        btnRekapTagihanBpjs.setText("REKAP TAGIHAN BPJS");
        btnRekapTagihanBpjs.setFocusable(false);
        btnRekapTagihanBpjs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRekapTagihanBpjs.setMaximumSize(new java.awt.Dimension(120, 21));
        btnRekapTagihanBpjs.setMinimumSize(new java.awt.Dimension(120, 21));
        btnRekapTagihanBpjs.setPreferredSize(new java.awt.Dimension(100, 21));
        btnRekapTagihanBpjs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRekapTagihanBpjs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapTagihanBpjsActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRekapTagihanBpjs);

        btnRekapPembayaran.setText("REKAP PEMBAYARAN");
        btnRekapPembayaran.setFocusable(false);
        btnRekapPembayaran.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRekapPembayaran.setMaximumSize(new java.awt.Dimension(110, 21));
        btnRekapPembayaran.setMinimumSize(new java.awt.Dimension(100, 21));
        btnRekapPembayaran.setPreferredSize(new java.awt.Dimension(100, 21));
        btnRekapPembayaran.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRekapPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapPembayaranActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRekapPembayaran);

        btnUbahPasien.setText("UBAH DATA PASIEN");
        btnUbahPasien.setFocusable(false);
        btnUbahPasien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUbahPasien.setMaximumSize(new java.awt.Dimension(120, 20));
        btnUbahPasien.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUbahPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahPasienActionPerformed(evt);
            }
        });
        jToolBar1.add(btnUbahPasien);

        btnCetakTunggakanPasien.setText("TUNGGAKAN PASIEN");
        btnCetakTunggakanPasien.setFocusable(false);
        btnCetakTunggakanPasien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCetakTunggakanPasien.setMaximumSize(new java.awt.Dimension(120, 20));
        btnCetakTunggakanPasien.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCetakTunggakanPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakTunggakanPasienActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCetakTunggakanPasien);

        btnRekapPemakaian.setText("REKAP PEMAKAIAN");
        btnRekapPemakaian.setFocusable(false);
        btnRekapPemakaian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRekapPemakaian.setMaximumSize(new java.awt.Dimension(100, 21));
        btnRekapPemakaian.setMinimumSize(new java.awt.Dimension(100, 21));
        btnRekapPemakaian.setPreferredSize(new java.awt.Dimension(100, 21));
        btnRekapPemakaian.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRekapPemakaian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapPemakaianActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRekapPemakaian);

        btnRekapPelayanan.setText("REKAP PELAYANAN");
        btnRekapPelayanan.setFocusable(false);
        btnRekapPelayanan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRekapPelayanan.setMaximumSize(new java.awt.Dimension(100, 21));
        btnRekapPelayanan.setMinimumSize(new java.awt.Dimension(100, 21));
        btnRekapPelayanan.setPreferredSize(new java.awt.Dimension(100, 21));
        btnRekapPelayanan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRekapPelayanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapPelayananActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRekapPelayanan);

        getContentPane().add(jToolBar1);
        jToolBar1.setBounds(0, 770, 1280, 30);

        pnlKeluar.setBackground(Utama.colorTransparentPanel);
        pnlKeluar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PASIEN KELUAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlKeluar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setText("Keadaan Pasien");
        pnlKeluar.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI" }));
        pnlKeluar.add(cbPasienKeadaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 250, 25));

        btnPasienKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_keluar.png"))); // NOI18N
        btnPasienKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienKeluarActionPerformed(evt);
            }
        });
        pnlKeluar.add(btnPasienKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 80, 30));

        getContentPane().add(pnlKeluar);
        pnlKeluar.setBounds(860, 630, 400, 90);

        pnlPembayaran.setBackground(Utama.colorTransparentPanel);
        pnlPembayaran.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PEMBAYARAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlPembayaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTagihan.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTagihan.setText("Rp 00.000.000.000");
        pnlPembayaran.add(lblTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 360, -1));

        btnBayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_bayar.png"))); // NOI18N
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        pnlPembayaran.add(btnBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 80, 30));

        btnCetakPembayaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_cetak_pembayaran.png"))); // NOI18N
        btnCetakPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakPembayaranActionPerformed(evt);
            }
        });
        pnlPembayaran.add(btnCetakPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 80, 30));

        getContentPane().add(pnlPembayaran);
        pnlPembayaran.setBounds(860, 510, 400, 110);

        btnCetakTagihan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_cetak_tagihan.png"))); // NOI18N
        btnCetakTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakTagihanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCetakTagihan);
        btnCetakTagihan.setBounds(1160, 730, 80, 30);

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_pembayaran.png"))); // NOI18N
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
        
        if (total.equals(0L) && Penanggung.UMUM.equals(pasien.getPenanggung())) {
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
            pdfProcessor.process(pdfView, model, String.format("pembayaran-%s.pdf", DateUtil.getTime().hashCode()));
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnCetakPembayaranActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new Utama().setVisible(true);
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
            JOptionPane.showMessageDialog(this, "Berhasil!");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienKeluarActionPerformed

    private void btnBatalTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalTagihanActionPerformed
        Tagihan tagihan = getTagihan(tblBayar);
        substractTotal(tagihan.getTagihanCounted());
        
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
        addTotal(tagihan.getTagihanCounted());
        
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
        
        try {
            List<RekapTagihanAdapter> list = reportService.rekapTagihan(pasien);
            Map<String, Object> model = new HashMap<>();
            model.put("pasien", pasien);
            model.put("listTagihan", list);

            pdfProcessor.process(pdfView, model, String.format("tagihan-%s.pdf", DateUtil.getTime().hashCode()));
        } catch (DocumentException | ServiceException ex) {
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

    private void btnBayarSemuaTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarSemuaTagihanActionPerformed
        TagihanTableModel menunggakTableModel = (TagihanTableModel) tblMenunggak.getModel();
        TagihanTableModel bayarTableModel = (TagihanTableModel) tblBayar.getModel();

        for (int i = (menunggakTableModel.getList().size() - 1); i >= 0; i--) {
            Tagihan tagihan = menunggakTableModel.getTagihan(i);
            bayarTableModel.addTagihan(tagihan);
            menunggakTableModel.removeTagihan(tagihan);

            total += tagihan.getTagihanCounted();
        }

        bayarTableModel.fireTableDataChanged();
        menunggakTableModel.fireTableDataChanged();
        setTotalLabel(total);
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_btnBayarSemuaTagihanActionPerformed

    private void btnBatalSemuaTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalSemuaTagihanActionPerformed
        TagihanTableModel bayarTableModel = (TagihanTableModel) tblBayar.getModel();
        TagihanTableModel menunggakTableModel = (TagihanTableModel) tblMenunggak.getModel();

        for (int i = (bayarTableModel.getList().size() - 1); i >= 0; i--) {
            Tagihan tagihan = bayarTableModel.getTagihan(i);
            menunggakTableModel.addTagihan(tagihan);
            bayarTableModel.removeTagihan(tagihan);

            total -= tagihan.getTagihanCounted();
        }

        bayarTableModel.fireTableDataChanged();
        menunggakTableModel.fireTableDataChanged();
        setTotalLabel(total);
        btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_btnBatalSemuaTagihanActionPerformed

    private void btnRekapPemakaianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapPemakaianActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Pemakaian.class);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapPemakaianActionPerformed

    private void btnRekapPelayananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapPelayananActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Unit.class);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapPelayananActionPerformed

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnCetakTagihan.requestFocus();
    }//GEN-LAST:event_txtKeywordKeyPressed

    private void btnUbahPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahPasienActionPerformed
        new DetailPasien().setVisible(true);
    }//GEN-LAST:event_btnUbahPasienActionPerformed

    private void btnCetakTunggakanPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakTunggakanPasienActionPerformed
        String nomorMedrek = JOptionPane.showInputDialog(this, "Masukan Nomor Rekam Medik");
        if (nomorMedrek == null || nomorMedrek.equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan memasukan nomor rekam medik pasien");
            return;
        }
        
        try {
            List<Pasien> list = pasienService.getByMedrek(nomorMedrek);

            PdfProcessor pdfProcessor = new PdfProcessor();
            RekapPasienPdfView pdfView = new RekapPasienPdfView();

            Map<String, Object> model = new HashMap<>();
            model.put("nomor", nomorMedrek);
            model.put("list", list);

            pdfProcessor.process(pdfView, model, String.format("rekap-pasien-%s.pdf", DateUtil.getTime().hashCode()));
        } catch (ServiceException | DocumentException  ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

    }//GEN-LAST:event_btnCetakTunggakanPasienActionPerformed

    private void btnRekapTagihanUmumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapTagihanUmumActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Tagihan.class, Penanggung.UMUM);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapTagihanUmumActionPerformed

    private void btnRekapTagihanBpjsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapTagihanBpjsActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Tagihan.class, Penanggung.BPJS);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapTagihanBpjsActionPerformed

    private void btnRekapPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapPembayaranActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Pembayaran.class);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapPembayaranActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalSemuaTagihan;
    private javax.swing.JButton btnBatalTagihan;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnBayarSemuaTagihan;
    private javax.swing.JButton btnBayarTagihan;
    private javax.swing.JButton btnCetakPembayaran;
    private javax.swing.JButton btnCetakTagihan;
    private javax.swing.JButton btnCetakTunggakanPasien;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPasienKeluar;
    private javax.swing.JButton btnRekapPelayanan;
    private javax.swing.JButton btnRekapPemakaian;
    private javax.swing.JButton btnRekapPembayaran;
    private javax.swing.JButton btnRekapTagihanBpjs;
    private javax.swing.JButton btnRekapTagihanUmum;
    private javax.swing.JButton btnUbahPasien;
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
