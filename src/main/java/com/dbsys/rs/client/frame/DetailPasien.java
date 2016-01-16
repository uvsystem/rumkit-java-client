package com.dbsys.rs.client.frame;

import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.Pasien;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class DetailPasien extends javax.swing.JFrame {

    private final PasienService pasienService = PasienService.getInstance();
    private Pasien pasien;
    
    /**
     * Creates new form DetailPasien
     */
    public DetailPasien() {
        initComponents();
    }
    
    /**
     * Creates new form DetailPasien
     * @param pasien
     */
    public DetailPasien(Pasien pasien) {
        initComponents();
        
        setPasien(pasien);
    }

    public Pasien getPasien() {
        return pasien;
    }

    public void setPasien(Pasien pasien) {
        this.pasien = pasien;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel19 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        txtPasienKodePenduduk = new javax.swing.JTextField();
        txtPasienNik = new javax.swing.JTextField();
        txtPasienNama = new javax.swing.JTextField();
        txtPasienKelamin = new javax.swing.JTextField();
        txtPasienLahir = new javax.swing.JTextField();
        txtPasienDarah = new javax.swing.JTextField();
        txtPasienAgama = new javax.swing.JTextField();
        txtPasienTelepon = new javax.swing.JTextField();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienTipe = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        txtPasienKode = new javax.swing.JTextField();
        btnKeluar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel104 = new javax.swing.JLabel();
        cbPasienKelas = new javax.swing.JComboBox();
        btnSimpanKelas = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel103 = new javax.swing.JLabel();
        cbPasienTanggungan = new javax.swing.JComboBox();
        btnSimpanTanggungan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DATA PASIEN");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DETAIL PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel46.setText("No. Rekam Medik");
        jPanel19.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        jLabel44.setText("No. Jaminan");
        jPanel19.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, 25));

        jLabel45.setText("Nama");
        jPanel19.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 100, 25));

        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel91.setText("Kelamin");
        jPanel19.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, 25));

        jLabel92.setText("Tanggal Lahir");
        jPanel19.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 100, 25));

        jLabel93.setText("Golongan Darah");
        jPanel19.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 100, 25));

        jLabel94.setText("Agama");
        jPanel19.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 100, 25));

        jLabel95.setText("No. Telepon");
        jPanel19.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, 25));

        jLabel97.setText("Tanggal Masuk");
        jPanel19.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 100, 25));

        jLabel102.setText("Tipe Perawatan");
        jPanel19.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 100, 25));

        txtPasienKodePenduduk.setEditable(false);
        jPanel19.add(txtPasienKodePenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 510, 25));

        txtPasienNik.setEditable(false);
        jPanel19.add(txtPasienNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 510, 25));

        txtPasienNama.setEditable(false);
        jPanel19.add(txtPasienNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 510, 25));

        txtPasienKelamin.setEditable(false);
        jPanel19.add(txtPasienKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 510, 25));

        txtPasienLahir.setEditable(false);
        jPanel19.add(txtPasienLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 510, 25));

        txtPasienDarah.setEditable(false);
        jPanel19.add(txtPasienDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 510, 25));

        txtPasienAgama.setEditable(false);
        jPanel19.add(txtPasienAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 510, 25));

        txtPasienTelepon.setEditable(false);
        jPanel19.add(txtPasienTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 510, 25));

        txtPasienTanggalMasuk.setEditable(false);
        jPanel19.add(txtPasienTanggalMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 510, 25));

        txtPasienTipe.setEditable(false);
        jPanel19.add(txtPasienTipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 510, 25));

        getContentPane().add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 660, 340));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "CARI DATA PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("Nomor Pasien");
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, 25));

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
        jPanel1.add(txtPasienKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 510, 25));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 660, 70));

        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        getContentPane().add(btnKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 600, 100, 25));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "UBAH KELAS PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel104.setText("Kelas");
        jPanel2.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        cbPasienKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "VVIP", "VIP", "I", "II", "III" }));
        jPanel2.add(cbPasienKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 410, 25));

        btnSimpanKelas.setText("SIMPAN");
        btnSimpanKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanKelasActionPerformed(evt);
            }
        });
        jPanel2.add(btnSimpanKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 100, 25));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 660, 60));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "UBAH TANGGUNGAN PASIEN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel103.setText("Tanggungan");
        jPanel3.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        cbPasienTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel3.add(cbPasienTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 410, 25));

        btnSimpanTanggungan.setText("SIMPAN");
        btnSimpanTanggungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanTanggunganActionPerformed(evt);
            }
        });
        jPanel3.add(btnSimpanTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 100, 25));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 660, 60));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanKelasActionPerformed
        try {
            if (cbPasienKelas.getSelectedIndex() == 0)
                throw new ServiceException("Silahkan memilih kelas pasien!");
            
            Kelas kelas = Kelas.valueOf(cbPasienKelas.getSelectedItem().toString());
            pasienService.ubahKelas(pasien, kelas);
            
            JOptionPane.showMessageDialog(this, "Berhasil");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanKelasActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void txtPasienKodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasienKodeFocusLost
        String keyword = txtPasienKode.getText();
        if (keyword.equals(""))
            return;
        
        try {
            pasien = pasienService.get(keyword);
            
            txtPasienAgama.setText(pasien.getAgama());
            txtPasienDarah.setText(pasien.getDarah());
            txtPasienKelamin.setText(pasien.getKelamin().toString());
            txtPasienKodePenduduk.setText(pasien.getKodePenduduk());
            txtPasienLahir.setText(pasien.getTanggalLahir().toString());
            txtPasienNama.setText(pasien.getNama());
            txtPasienNik.setText(pasien.getNik());
            txtPasienTanggalMasuk.setText(pasien.getTanggalMasuk().toString());
            txtPasienTelepon.setText(pasien.getTelepon());
            txtPasienTipe.setText(pasien.getTipePerawatan().toString());
            
            cbPasienKelas.setSelectedItem(pasien.getKelas().toString());
            cbPasienTanggungan.setSelectedItem(pasien.getPenanggung().toString());
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPasienKodeFocusLost

    private void txtPasienKodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasienKodeKeyPressed
        if (evt.getKeyCode() == 10)
            btnKeluar.requestFocus();
    }//GEN-LAST:event_txtPasienKodeKeyPressed

    private void btnSimpanTanggunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanTanggunganActionPerformed
        try {
            if (cbPasienTanggungan.getSelectedIndex() == 0)
                throw new ServiceException("Silahkan memilih tanggungan pasien!");
            
            Penanggung penanggung = Penanggung.valueOf(cbPasienTanggungan.getSelectedItem().toString());
            pasienService.ubahPenanggung(pasien, penanggung);
            
            JOptionPane.showMessageDialog(this, "Berhasil");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanTanggunganActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpanKelas;
    private javax.swing.JButton btnSimpanTanggungan;
    private javax.swing.JComboBox cbPasienKelas;
    private javax.swing.JComboBox cbPasienTanggungan;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKode;
    private javax.swing.JTextField txtPasienKodePenduduk;
    private javax.swing.JTextField txtPasienLahir;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTelepon;
    private javax.swing.JTextField txtPasienTipe;
    // End of variables declaration//GEN-END:variables
}
