package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianBhpService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.Tindakan;
import com.dbsys.rs.lib.entity.Unit;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 */
public class FramePoliklinik extends javax.swing.JFrame implements BhpTableFrame, TindakanTableFrame {

    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    private final PemakaianBhpService pemakaianBhpService = PemakaianBhpService.getInstance(EventController.host);
    private final PelayananService pelayananService = PelayananService.getInstance(EventController.host);

    private Pasien pasien;
    
    /**
     * Creates new form Poliklinik
     * @param unit
     */
    public FramePoliklinik(Unit unit) {
        super();
        initComponents();
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
        
        if (Unit.Type.POLIKLINIK.equals(unit.getTipe())) {
            // TODO
        } else if (Unit.Type.LABORATORIUM.equals(unit.getTipe())) {
            // TODO
        } else if (Unit.Type.TRANSFUSI_DARAH.equals(unit.getTipe())) {
            // TODO
        } else if (Unit.Type.RADIOLOGI.equals(unit.getTipe())) {
            // TODO
        }
    }
    
    private void setDetailPasien(final Pasien pasien) {
        txtPasienKodePenduduk.setText(pasien.getKodePenduduk());
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
        txtPasienTipe.setText(pasien.getTipe().toString());
    }
    
    private void loadTabelTindakan(final Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<Pelayanan> listPelayanan = pelayananService.getByPasien(pasien.getId());
        PelayananTableModel tableModel = new PelayananTableModel(listPelayanan);
        tblTindakan.setModel(tableModel);
    }
    
    @Override
    public void reloadTableTindakan() {
        try {
            loadTabelTindakan(pasien);
        } catch (ServiceException ex) {}
    }
    
    private Pelayanan getPelayanan() throws ComponentSelectionException {
        int index = tblTindakan.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PelayananTableModel tableModel = (PelayananTableModel)tblTindakan.getModel();
        return tableModel.getPelayanan(index);
    }
    
    private void loadTabelBhp(final Pasien pasien) throws ServiceException {
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
            loadTabelBhp(pasien);
        } catch (ServiceException ex) {}
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

        jToolBar1 = new javax.swing.JToolBar();
        jLabel13 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        tabPane = new javax.swing.JTabbedPane();
        pnlTindakan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        btnTindakanTambah = new javax.swing.JButton();
        btnTindakanUpdate = new javax.swing.JButton();
        btnTindakanHapus = new javax.swing.JButton();
        pnlBhp = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        btnBhpTambah = new javax.swing.JButton();
        btnBhpUpdate = new javax.swing.JButton();
        btnBhpHapus = new javax.swing.JButton();
        pnlCari = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPasienKode = new javax.swing.JTextField();
        pnlDetail = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtPasienKodePenduduk = new javax.swing.JTextField();
        txtPasienNik = new javax.swing.JTextField();
        txtPasienNama = new javax.swing.JTextField();
        txtPasienTanggalLahir = new javax.swing.JTextField();
        txtPasienDarah = new javax.swing.JTextField();
        txtPasienAgama = new javax.swing.JTextField();
        txtPasienTelepon = new javax.swing.JTextField();
        txtPasienKelamin = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtPasienStatus = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienTanggungan = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPasienTipe = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RUMAH SAKIT LIUN KENDAGE");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setLayout(null);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel13.setText("ANDA LOGIN SEBAGAI : ");
        jToolBar1.add(jLabel13);

        lblOperator.setText("jLabel1");
        jToolBar1.add(lblOperator);

        jLabel2.setText(" -  UNIT : ");
        jToolBar1.add(jLabel2);

        lblUnit.setText("jLabel3");
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
        jToolBar1.setBounds(0, 770, 1270, 30);

        tabPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPaneMouseClicked(evt);
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
        jScrollPane1.setBounds(10, 11, 690, 530);

        btnTindakanTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Tambah(small).png"))); // NOI18N
        btnTindakanTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanTambahActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanTambah);
        btnTindakanTambah.setBounds(710, 10, 90, 30);

        btnTindakanUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Update(small).png"))); // NOI18N
        btnTindakanUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUpdateActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanUpdate);
        btnTindakanUpdate.setBounds(710, 50, 90, 30);

        btnTindakanHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus(small).png"))); // NOI18N
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanHapus);
        btnTindakanHapus.setBounds(710, 90, 90, 30);

        tabPane.addTab("TINDAKAN", pnlTindakan);

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
        jScrollPane2.setBounds(10, 11, 700, 530);

        btnBhpTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Tambah(small).png"))); // NOI18N
        btnBhpTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpTambahActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpTambah);
        btnBhpTambah.setBounds(720, 10, 80, 30);

        btnBhpUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Update(small).png"))); // NOI18N
        btnBhpUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpUpdateActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpUpdate);
        btnBhpUpdate.setBounds(720, 50, 80, 30);

        btnBhpHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus(small).png"))); // NOI18N
        btnBhpHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpHapusActionPerformed(evt);
            }
        });
        pnlBhp.add(btnBhpHapus);
        btnBhpHapus.setBounds(720, 90, 80, 30);

        tabPane.addTab("BAHAN HABIS PAKAI", pnlBhp);

        getContentPane().add(tabPane);
        tabPane.setBounds(20, 180, 810, 580);

        pnlCari.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlCari.setLayout(null);

        jLabel1.setText("No. Pasien");
        pnlCari.add(jLabel1);
        jLabel1.setBounds(20, 10, 110, 14);

        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        pnlCari.add(txtPasienKode);
        txtPasienKode.setBounds(140, 10, 240, 20);

        getContentPane().add(pnlCari);
        pnlCari.setBounds(840, 130, 400, 40);

        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Detail Pasien"));
        pnlDetail.setBackground(new Color(0,0,0,20));
        pnlDetail.setLayout(null);

        jLabel4.setText("NO. MEDREK");
        pnlDetail.add(jLabel4);
        jLabel4.setBounds(20, 30, 110, 14);

        jLabel5.setText("NIK");
        pnlDetail.add(jLabel5);
        jLabel5.setBounds(20, 60, 110, 14);

        jLabel6.setText("NAMA");
        pnlDetail.add(jLabel6);
        jLabel6.setBounds(20, 90, 110, 14);

        jLabel7.setText("KELAMIN");
        pnlDetail.add(jLabel7);
        jLabel7.setBounds(20, 120, 110, 14);

        jLabel8.setText("TANGGAL LAHIR");
        pnlDetail.add(jLabel8);
        jLabel8.setBounds(20, 150, 110, 14);

        jLabel9.setText("GOL. DARAH");
        pnlDetail.add(jLabel9);
        jLabel9.setBounds(20, 180, 110, 14);

        jLabel10.setText("AGAMA");
        pnlDetail.add(jLabel10);
        jLabel10.setBounds(20, 210, 110, 14);

        jLabel11.setText("TELEPON");
        pnlDetail.add(jLabel11);
        jLabel11.setBounds(20, 240, 110, 14);

        txtPasienKodePenduduk.setEditable(false);
        txtPasienKodePenduduk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienKodePenduduk);
        txtPasienKodePenduduk.setBounds(140, 30, 240, 20);

        txtPasienNik.setEditable(false);
        txtPasienNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienNik);
        txtPasienNik.setBounds(140, 60, 240, 20);

        txtPasienNama.setEditable(false);
        txtPasienNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienNama);
        txtPasienNama.setBounds(140, 90, 240, 20);

        txtPasienTanggalLahir.setEditable(false);
        txtPasienTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(140, 150, 240, 20);

        txtPasienDarah.setEditable(false);
        txtPasienDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienDarah);
        txtPasienDarah.setBounds(140, 180, 240, 20);

        txtPasienAgama.setEditable(false);
        txtPasienAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienAgama);
        txtPasienAgama.setBounds(140, 210, 240, 20);

        txtPasienTelepon.setEditable(false);
        txtPasienTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(140, 240, 240, 20);

        txtPasienKelamin.setEditable(false);
        txtPasienKelamin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(140, 120, 240, 20);

        jLabel12.setText("TANGGUNGAN");
        pnlDetail.add(jLabel12);
        jLabel12.setBounds(20, 270, 110, 14);

        jLabel16.setText("STATUS");
        pnlDetail.add(jLabel16);
        jLabel16.setBounds(20, 300, 110, 14);

        jLabel15.setText("TANGGAL MASUK");
        pnlDetail.add(jLabel15);
        jLabel15.setBounds(20, 330, 110, 14);

        txtPasienStatus.setEditable(false);
        txtPasienStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienStatus);
        txtPasienStatus.setBounds(140, 300, 240, 18);

        txtPasienTanggalMasuk.setEditable(false);
        txtPasienTanggalMasuk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(140, 330, 240, 18);

        txtPasienTanggungan.setEditable(false);
        txtPasienTanggungan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(140, 270, 240, 18);

        jLabel14.setText("PERAWATAN");
        pnlDetail.add(jLabel14);
        jLabel14.setBounds(20, 360, 110, 14);

        txtPasienTipe.setEditable(false);
        pnlDetail.add(txtPasienTipe);
        txtPasienTipe.setBounds(140, 360, 240, 20);

        getContentPane().add(pnlDetail);
        pnlDetail.setBounds(840, 180, 400, 400);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/poliklinik.jpg"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPaneMouseClicked
        int index = tabPane.getSelectedIndex();

        try {
            switch(index) {
                case 0: loadTabelTindakan(pasien);
                    break;
                case 1: loadTabelBhp(pasien);
                    break;
                default: break;
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tabPaneMouseClicked

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

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameLogin().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

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

    private void txtPasienKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasienKodeFocusLost
        String keyword = txtPasienKode.getText();
        
        if (keyword.equals(""))
            return;
        
        try {
            pasien = pasienService.get(keyword);
            setDetailPasien(pasien);

            loadTabelTindakan(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBhpHapus;
    private javax.swing.JButton btnBhpTambah;
    private javax.swing.JButton btnBhpUpdate;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnTindakanHapus;
    private javax.swing.JButton btnTindakanTambah;
    private javax.swing.JButton btnTindakanUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlCari;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblTindakan;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKode;
    private javax.swing.JTextField txtPasienKodePenduduk;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienStatus;
    private javax.swing.JTextField txtPasienTanggalLahir;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPasienTelepon;
    private javax.swing.JTextField txtPasienTipe;
    // End of variables declaration//GEN-END:variables
}
