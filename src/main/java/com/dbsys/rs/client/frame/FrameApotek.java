package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.BarangTableFrame;
import com.dbsys.rs.client.ComponentSelectionException;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.PemakaianPdfView;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pemakaian;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class FrameApotek extends javax.swing.JFrame implements BarangTableFrame {

    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    private final PemakaianService pemakaianService = PemakaianService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);

    private Pasien pasien;
    
    /**
     * Digunakan untuk cetak
     */
    private List<Pemakaian> listPemakaian;

    /**
     * Creates new FrameFarmasi
     */
    public FrameApotek() {
        initComponents();
        setSize(1280, 800);
        
        pnlResep.setVisible(true);
        
        setDetailPasien(pasien);
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
    }

    private void setDetailPasien(Pasien pasien) {
        if (pasien == null) {
            pasien = new Pasien();

            txtPasienKelamin.setText(null);
            txtPasienTanggalLahir.setText(null);
            txtPasienTanggungan.setText(null);
            txtPasienStatusRawat.setText(null);
            txtPasienTanggalMasuk.setText(null);
        } else {
            txtPasienKelamin.setText(pasien.getKelamin().toString());
            txtPasienTanggalLahir.setText(pasien.getTanggalLahir().toString());
            txtPasienTanggungan.setText(pasien.getPenanggung().toString());
            txtPasienStatusRawat.setText(pasien.getStatus().toString());
            txtPasienTanggalMasuk.setText(pasien.getTanggalMasuk().toString());
        }
        
        txtPasienNik.setText(pasien.getNik());
        txtPasienNama.setText(pasien.getNama());
        txtPasienGolonganDarah.setText(pasien.getDarah());
        txtPasienAgama.setText(pasien.getAgama());
        txtPasienTelepon.setText(pasien.getTelepon());
        
        this.pasien = pasien;
    }

    private List<Pemakaian> loadTableResep(Pasien pasien) throws ServiceException {
        List<Pemakaian> list = pemakaianService.getByPasien(pasien);
        PemakaianTableModel tableModel = new PemakaianTableModel(list);

        tblResep.removeAll();
        tblResep.setModel(tableModel);
        
        return list;
    }
    
    private List<Pemakaian> loadTableResep(String nomorPasien) throws ServiceException {
        List<Pemakaian> list = pemakaianService.getByNomor(nomorPasien);
        PemakaianTableModel tableModel = new PemakaianTableModel(list);

        tblResep.removeAll();
        tblResep.setModel(tableModel);

        return list;
    }
    
    @Override
    public void reloadTable() {
        try {
            loadTableResep(pasien);
        } catch (ServiceException ex) {}
    }
    
    private Pemakaian getPemakaian() throws ComponentSelectionException {
        int index = tblResep.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PemakaianTableModel tableModel = (PemakaianTableModel) tblResep.getModel();
        return tableModel.getPemakaian(index);
    }

    private void printPemakaian() {
        if (listPemakaian == null) {
            JOptionPane.showMessageDialog(this, "Silahakn cari pemakaian menggunakan nomor resep/kode pasien");
            return;
        }
        
        PdfProcessor pdfProcessor = new PdfProcessor();
        
        PemakaianPdfView pdfView = new PemakaianPdfView();
        Map<String, Object> model = new HashMap<>();
        model.put("list", listPemakaian);
        
        try {
            pdfProcessor.generate(pdfView, model, "E://print//pemakaian2.pdf");
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
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

        pnlCari = new javax.swing.JPanel();
        txtPasienKode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        pnlPasien = new javax.swing.JPanel();
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
        pnlResep = new javax.swing.JPanel();
        txtResepNomor = new javax.swing.JTextField();
        btnObatTambah = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResep = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnHapus = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel31 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        btnPasien = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RUMAH SAKIT LIUN KENDAGE TAHUNA - FARMASI");
        setBounds(new java.awt.Rectangle(0, 0, 1280, 800));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        pnlCari.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PENCARIAN PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlCari.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        pnlCari.add(txtPasienKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 250, 25));

        jLabel2.setText("No. Pasien");
        pnlCari.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        getContentPane().add(pnlCari);
        pnlCari.setBounds(860, 140, 400, 60);

        pnlPasien.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPasien.setLayout(null);

        jLabel3.setText("NIK");
        pnlPasien.add(jLabel3);
        jLabel3.setBounds(20, 30, 90, 25);

        jLabel4.setText("Nama");
        pnlPasien.add(jLabel4);
        jLabel4.setBounds(20, 60, 90, 25);

        jLabel5.setText("Kelamin");
        pnlPasien.add(jLabel5);
        jLabel5.setBounds(20, 90, 90, 25);

        jLabel7.setText("Agama");
        pnlPasien.add(jLabel7);
        jLabel7.setBounds(20, 180, 90, 25);

        jLabel8.setText("Telepon");
        pnlPasien.add(jLabel8);
        jLabel8.setBounds(20, 210, 90, 25);

        jLabel9.setText("Gol. Darah");
        pnlPasien.add(jLabel9);
        jLabel9.setBounds(20, 150, 90, 25);

        jLabel10.setText("Tanggal Masuk");
        pnlPasien.add(jLabel10);
        jLabel10.setBounds(20, 300, 90, 25);

        jLabel11.setText("Tanggal Lahir");
        pnlPasien.add(jLabel11);
        jLabel11.setBounds(20, 120, 90, 25);

        jLabel12.setText("Tanggungan");
        pnlPasien.add(jLabel12);
        jLabel12.setBounds(20, 240, 90, 25);

        jLabel13.setText("Status Rawat");
        pnlPasien.add(jLabel13);
        jLabel13.setBounds(20, 270, 90, 25);

        txtPasienNik.setEditable(false);
        pnlPasien.add(txtPasienNik);
        txtPasienNik.setBounds(130, 30, 250, 25);

        txtPasienNama.setEditable(false);
        pnlPasien.add(txtPasienNama);
        txtPasienNama.setBounds(130, 60, 250, 25);

        txtPasienKelamin.setEditable(false);
        pnlPasien.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(130, 90, 250, 25);

        txtPasienTanggalLahir.setEditable(false);
        pnlPasien.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(130, 120, 250, 25);

        txtPasienGolonganDarah.setEditable(false);
        pnlPasien.add(txtPasienGolonganDarah);
        txtPasienGolonganDarah.setBounds(130, 150, 250, 25);

        txtPasienAgama.setEditable(false);
        pnlPasien.add(txtPasienAgama);
        txtPasienAgama.setBounds(130, 180, 250, 25);

        txtPasienTelepon.setEditable(false);
        pnlPasien.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(130, 210, 250, 25);

        txtPasienTanggungan.setEditable(false);
        pnlPasien.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(130, 240, 250, 25);

        txtPasienStatusRawat.setEditable(false);
        pnlPasien.add(txtPasienStatusRawat);
        txtPasienStatusRawat.setBounds(130, 270, 250, 25);

        txtPasienTanggalMasuk.setEditable(false);
        pnlPasien.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(130, 300, 250, 25);

        getContentPane().add(pnlPasien);
        pnlPasien.setBounds(860, 210, 400, 340);

        pnlResep.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DAFTAR OBAT PASIEN", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlResep.setLayout(null);

        txtResepNomor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtResepNomorFocusLost(evt);
            }
        });
        pnlResep.add(txtResepNomor);
        txtResepNomor.setBounds(160, 40, 530, 25);

        btnObatTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Tambah(small).png"))); // NOI18N
        btnObatTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatTambahActionPerformed(evt);
            }
        });
        pnlResep.add(btnObatTambah);
        btnObatTambah.setBounds(710, 40, 100, 30);

        tblResep.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblResep);

        pnlResep.add(jScrollPane1);
        jScrollPane1.setBounds(20, 90, 790, 470);

        jLabel1.setText("Nomor Resep");
        pnlResep.add(jLabel1);
        jLabel1.setBounds(20, 40, 130, 25);

        btnHapus.setText("HAPUS");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        pnlResep.add(btnHapus);
        btnHapus.setBounds(730, 570, 80, 30);

        getContentPane().add(pnlResep);
        pnlResep.setBounds(20, 140, 830, 620);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel31.setText("LOGIN SEBAGAI:");
        jToolBar1.add(jLabel31);

        lblOperator.setText("jLabel32");
        jToolBar1.add(lblOperator);

        jLabel32.setText(" - ");
        jToolBar1.add(jLabel32);

        jLabel33.setText("UNIT: ");
        jToolBar1.add(jLabel33);

        lblUnit.setText("jLabel34");
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

        btnPasien.setText("CETAK PEMAKAIAN");
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        getContentPane().add(btnPasien);
        btnPasien.setBounds(1133, 560, 130, 40);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Farmasi_Bg.jpg"))); // NOI18N
        getContentPane().add(background);
        background.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPasienKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasienKodeFocusLost
        String keyword = txtPasienKode.getText();
        txtResepNomor.setText(Pemakaian.createKode());
        
        if (keyword.equals(""))
            return;

        try {
            pasien = pasienService.get(keyword);
            setDetailPasien(pasien);
            listPemakaian = loadTableResep(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    private void btnObatTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatTambahActionPerformed
        String nomorResep = txtResepNomor.getText();
        
        if (nomorResep == null || nomorResep.equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan masukan nomor resep");
            return;
        }
        
        new FrameTambahObject(this, pasien, nomorResep).setVisible(true);
    }//GEN-LAST:event_btnObatTambahActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameUtama().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void txtResepNomorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtResepNomorFocusLost
        String nomor = txtResepNomor.getText();
        if (nomor == null || nomor.equals(""))
            return;

        try {
            listPemakaian = loadTableResep(nomor);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtResepNomorFocusLost

    private void btnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienActionPerformed
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Silahkan mencari data pasien terlebih dahulu");
            return;
        }
        
        printPemakaian();
    }//GEN-LAST:event_btnPasienActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        try {
            Pemakaian pemakaian = getPemakaian();

            int pilihan = JOptionPane.showConfirmDialog(this, String.format("Anda yakin akan menghapus pemakaian obat/bhp '%s' ?", 
                    pemakaian.getBarang().getNama()));

            if (JOptionPane.YES_OPTION == pilihan) {
                pemakaianService.hapus(pemakaian);
                loadTableResep(pasien);
            }
        } catch (ComponentSelectionException | ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnObatTambah;
    private javax.swing.JButton btnPasien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel pnlCari;
    private javax.swing.JPanel pnlPasien;
    private javax.swing.JPanel pnlResep;
    private javax.swing.JTable tblResep;
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
