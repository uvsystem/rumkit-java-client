package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.ComponentSelectionException;
import com.dbsys.rs.client.TindakanTableFrame;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.Tindakan;
import com.dbsys.rs.client.entity.Unit;

import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 */
public class PelayananMedik extends javax.swing.JFrame implements TindakanTableFrame {

    private final TokenService tokenService = TokenService.getInstance();
    private final PasienService pasienService = PasienService.getInstance();
    private final PelayananService pelayananService = PelayananService.getInstance();

    private Pasien pasien;
    
    /**
     * Creates new form Poliklinik
     * @param unit
     */
    public PelayananMedik(Unit unit) {
        super();
        initComponents();
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
        
        if (Unit.TipeUnit.PENUNJANG_MEDIK.equals(unit.getTipe())) {
            jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_penunjang.png")));
        }
    }
    
    private void setDetailPasien(final Pasien pasien) {
        if (pasien != null) {
            txtPasienKodePenduduk.setText(pasien.getKodePenduduk());
            txtPasienNik.setText(pasien.getNik());
            txtPasienNama.setText(pasien.getNama());
            txtPasienKelamin.setText(pasien.getKelamin().toString());
            txtPasienTanggalLahir.setText(pasien.getTanggalLahir().toString());
            txtPasienDarah.setText(pasien.getDarah());
            txtPasienAgama.setText(pasien.getAgama());
            txtPasienTelepon.setText(pasien.getTelepon());
            txtPasienTanggungan.setText(pasien.getPenanggung().toString());
            txtPasienTanggalMasuk.setText(pasien.getTanggalMasuk().toString());
            txtPasienTipe.setText(pasien.getTipePerawatan().toString());
            txtPasienKelas.setText(pasien.getKelas().toString());
        } else {
            txtPasienKodePenduduk.setText(null);
            txtPasienNik.setText(null);
            txtPasienNama.setText(null);
            txtPasienKelamin.setText(null);
            txtPasienTanggalLahir.setText(null);
            txtPasienDarah.setText(null);
            txtPasienAgama.setText(null);
            txtPasienTelepon.setText(null);
            txtPasienTanggungan.setText(null);
            txtPasienTanggalMasuk.setText(null);
            txtPasienTipe.setText(null);
            txtPasienKelas.setText(null);
        }
        
        this.pasien = pasien;
    }
    
    private void loadTabelTindakan(final Pasien pasien) throws ServiceException {
        if (pasien == null)
            return;

        List<Pelayanan> listPelayanan = pelayananService.getByPasien(pasien);
        PelayananTableModel tableModel = new PelayananTableModel(listPelayanan);
        tblTindakan.setModel(tableModel);
    }
    
    @Override
    public void reloadTableTindakan() {
        try {
            loadTabelTindakan(pasien);
        } catch (ServiceException ex) {
            PelayananTableModel tableModel = new PelayananTableModel(null);
            tblTindakan.setModel(tableModel);
        }
    }
    
    private Pelayanan getPelayanan() throws ComponentSelectionException {
        int index = tblTindakan.getSelectedRow();
        
        if (index < 0)
            throw new ComponentSelectionException("Silahkan memilih data pada tabel terlebih dahulu");
        
        PelayananTableModel tableModel = (PelayananTableModel)tblTindakan.getModel();
        return tableModel.getPelayanan(index);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTindakan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        btnTindakanTambah = new javax.swing.JButton();
        btnTindakanUpdate = new javax.swing.JButton();
        btnTindakanHapus = new javax.swing.JButton();
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
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtPasienKodePenduduk = new javax.swing.JTextField();
        txtPasienNik = new javax.swing.JTextField();
        txtPasienNama = new javax.swing.JTextField();
        txtPasienTanggalLahir = new javax.swing.JTextField();
        txtPasienDarah = new javax.swing.JTextField();
        txtPasienAgama = new javax.swing.JTextField();
        txtPasienTelepon = new javax.swing.JTextField();
        txtPasienKelamin = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienTanggungan = new javax.swing.JTextField();
        txtPasienTipe = new javax.swing.JTextField();
        txtPasienKelas = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel13 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RUMAH SAKIT LIUN KENDAGE");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setLayout(null);

        pnlTindakan.setBackground(Utama.colorTransparentPanel);
        pnlTindakan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DAFTAR PELAYANAN TINDAKAN", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlTindakan.setLayout(null);

        jScrollPane1.setBackground(Utama.colorTransparentPanel);

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
        jScrollPane1.setBounds(20, 30, 670, 530);

        btnTindakanTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_tambah.png"))); // NOI18N
        btnTindakanTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanTambahActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanTambah);
        btnTindakanTambah.setBounds(710, 30, 80, 30);

        btnTindakanUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnTindakanUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUpdateActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanUpdate);
        btnTindakanUpdate.setBounds(710, 70, 80, 30);

        btnTindakanHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnTindakanHapus);
        btnTindakanHapus.setBounds(710, 110, 80, 30);

        getContentPane().add(pnlTindakan);
        pnlTindakan.setBounds(20, 180, 810, 580);

        pnlCari.setBackground(Utama.colorTransparentPanel);
        pnlCari.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlCari.setLayout(null);

        jLabel1.setText("NO. PASIEN");
        pnlCari.add(jLabel1);
        jLabel1.setBounds(20, 10, 110, 25);

        txtPasienKode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKodeFocusLost(evt);
            }
        });
        txtPasienKode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasienKodeKeyPressed(evt);
            }
        });
        pnlCari.add(txtPasienKode);
        txtPasienKode.setBounds(140, 10, 240, 25);

        getContentPane().add(pnlCari);
        pnlCari.setBounds(850, 180, 400, 45);

        pnlDetail.setBackground(Utama.colorTransparentPanel);
        pnlDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlDetail.setBackground(new Color(0,0,0,20));
        pnlDetail.setLayout(null);

        jLabel4.setText("NO. MEDREK");
        pnlDetail.add(jLabel4);
        jLabel4.setBounds(20, 30, 110, 25);

        jLabel5.setText("NO. JAMINAN");
        pnlDetail.add(jLabel5);
        jLabel5.setBounds(20, 60, 110, 25);

        jLabel6.setText("NAMA");
        pnlDetail.add(jLabel6);
        jLabel6.setBounds(20, 90, 110, 25);

        jLabel7.setText("KELAMIN");
        pnlDetail.add(jLabel7);
        jLabel7.setBounds(20, 120, 110, 25);

        jLabel8.setText("TANGGAL LAHIR");
        pnlDetail.add(jLabel8);
        jLabel8.setBounds(20, 150, 110, 25);

        jLabel9.setText("GOL. DARAH");
        pnlDetail.add(jLabel9);
        jLabel9.setBounds(20, 180, 110, 25);

        jLabel10.setText("AGAMA");
        pnlDetail.add(jLabel10);
        jLabel10.setBounds(20, 210, 110, 25);

        jLabel11.setText("TELEPON");
        pnlDetail.add(jLabel11);
        jLabel11.setBounds(20, 240, 110, 25);

        jLabel12.setText("TANGGUNGAN");
        pnlDetail.add(jLabel12);
        jLabel12.setBounds(20, 270, 110, 25);

        jLabel15.setText("TANGGAL MASUK");
        pnlDetail.add(jLabel15);
        jLabel15.setBounds(20, 300, 110, 25);

        jLabel14.setText("KELAS");
        pnlDetail.add(jLabel14);
        jLabel14.setBounds(20, 360, 110, 25);

        jLabel17.setText("PERAWATAN");
        pnlDetail.add(jLabel17);
        jLabel17.setBounds(20, 330, 110, 25);

        txtPasienKodePenduduk.setEditable(false);
        txtPasienKodePenduduk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienKodePenduduk);
        txtPasienKodePenduduk.setBounds(140, 30, 240, 25);

        txtPasienNik.setEditable(false);
        txtPasienNik.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienNik);
        txtPasienNik.setBounds(140, 60, 240, 25);

        txtPasienNama.setEditable(false);
        txtPasienNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienNama);
        txtPasienNama.setBounds(140, 90, 240, 25);

        txtPasienTanggalLahir.setEditable(false);
        txtPasienTanggalLahir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTanggalLahir);
        txtPasienTanggalLahir.setBounds(140, 150, 240, 25);

        txtPasienDarah.setEditable(false);
        txtPasienDarah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienDarah);
        txtPasienDarah.setBounds(140, 180, 240, 25);

        txtPasienAgama.setEditable(false);
        txtPasienAgama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienAgama);
        txtPasienAgama.setBounds(140, 210, 240, 25);

        txtPasienTelepon.setEditable(false);
        txtPasienTelepon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(140, 240, 240, 25);

        txtPasienKelamin.setEditable(false);
        txtPasienKelamin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(140, 120, 240, 25);

        txtPasienTanggalMasuk.setEditable(false);
        txtPasienTanggalMasuk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(140, 300, 240, 25);

        txtPasienTanggungan.setEditable(false);
        txtPasienTanggungan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetail.add(txtPasienTanggungan);
        txtPasienTanggungan.setBounds(140, 270, 240, 25);

        txtPasienTipe.setEditable(false);
        pnlDetail.add(txtPasienTipe);
        txtPasienTipe.setBounds(140, 330, 240, 25);

        txtPasienKelas.setEditable(false);
        pnlDetail.add(txtPasienKelas);
        txtPasienKelas.setBounds(140, 360, 240, 25);

        getContentPane().add(pnlDetail);
        pnlDetail.setBounds(850, 230, 400, 410);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel13.setText("ANDA LOGIN SEBAGAI : ");
        jToolBar1.add(jLabel13);

        lblOperator.setText("jLabel1");
        jToolBar1.add(lblOperator);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText(" - ");
        jLabel18.setMaximumSize(new java.awt.Dimension(25, 14));
        jToolBar1.add(jLabel18);

        jLabel2.setText("UNIT : ");
        jToolBar1.add(jLabel2);

        lblUnit.setText("jLabel3");
        jToolBar1.add(lblUnit);

        jSeparator1.setMaximumSize(new java.awt.Dimension(20, 32767));
        jToolBar1.add(jSeparator1);

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_logout.png"))); // NOI18N
        btnLogout.setFocusable(false);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setMaximumSize(new java.awt.Dimension(80, 20));
        btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLogout);

        getContentPane().add(jToolBar1);
        jToolBar1.setBounds(0, 770, 1270, 30);

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset);
        btnReset.setBounds(1170, 650, 80, 30);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_poliklinik.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new Utama().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnTindakanTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanTambahActionPerformed
        new TambahTagihan(this, Tindakan.class, pasien).setVisible(true);
    }//GEN-LAST:event_btnTindakanTambahActionPerformed

    private void btnTindakanUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanUpdateActionPerformed
        try {
            Pelayanan pelayanan = getPelayanan();
            
            if (!TokenHolder.getUnit().equals(pelayanan.getUnit())) {
                JOptionPane.showMessageDialog(this, 
                        String.format("Maaf anda tidak bisa mengubah pelayanan %s yang berasal dari unit %s. Anda hanya bisa mengubah pelayanan dari unit %s", 
                                pelayanan.getTindakan().getNama(), pelayanan.getNamaUnit(), TokenHolder.getNamaUnit()));
                return;
            }
            
            new TambahTagihan(this, pasien, pelayanan).setVisible(true);
        } catch (ComponentSelectionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanUpdateActionPerformed

    private void btnTindakanHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanHapusActionPerformed
        try {
            Pelayanan pelayanan = getPelayanan();
            
            if (!TokenHolder.getUnit().equals(pelayanan.getUnit())) {
                JOptionPane.showMessageDialog(this, 
                        String.format("Maaf anda tidak bisa menghapus pelayanan %s yang berasal dari unit %s. Anda hanya bisa menghapus pelayanan dari unit %s", 
                                pelayanan.getTindakan().getNama(), pelayanan.getNamaUnit(), TokenHolder.getNamaUnit()));
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(this, String.format("Anda yakin ingin menghapus pelayanan %s pada tanggal %s?", 
                    pelayanan.getTindakan().getNama(), pelayanan.getTanggal()));

            if (JOptionPane.YES_OPTION == pilihan) {
                pelayananService.hapus(pelayanan);
                reloadTableTindakan();
            }
        } catch (ComponentSelectionException | ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanHapusActionPerformed

    private void txtPasienKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasienKodeFocusLost
        String keyword = txtPasienKode.getText();
        
        if (keyword.equals("")) {
            setDetailPasien(null); // reset form
            return;
        }
        
        try {
            pasien = pasienService.get(keyword);
            setDetailPasien(pasien);

            try {
                loadTabelTindakan(pasien);
            } catch (ServiceException e) {
                PelayananTableModel tableModel = new PelayananTableModel(null);
                tblTindakan.setModel(tableModel);
            }
            
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    private void txtPasienKodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasienKodeKeyPressed
        if (evt.getKeyCode() == 10)
            btnTindakanTambah.requestFocus();
    }//GEN-LAST:event_txtPasienKodeKeyPressed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        setDetailPasien(null);
    }//GEN-LAST:event_btnResetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnReset;
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
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JPanel pnlCari;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JTable tblTindakan;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKelas;
    private javax.swing.JTextField txtPasienKode;
    private javax.swing.JTextField txtPasienKodePenduduk;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienTanggalLahir;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTanggungan;
    private javax.swing.JTextField txtPasienTelepon;
    private javax.swing.JTextField txtPasienTipe;
    // End of variables declaration//GEN-END:variables
}
