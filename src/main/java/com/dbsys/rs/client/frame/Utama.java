package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.tableModel.PasienCariTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.client.entity.Pasien;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class Utama extends javax.swing.JFrame {
    
    public static Color colorTransparentPanel = new Color(255, 255, 255, 75);

    private final PasienService pasienService = PasienService.getInstance();
    
    /**
     * Creates new form FrameUtama
     */
    public Utama() {
        initComponents();
        setSize(1280, 800);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlUtama = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        btnKeluar = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("APLIKASI PEMBAYARAN TAGIHAN | RSUD LIUN KENDAGE");
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlUtama.setBackground(colorTransparentPanel);
        pnlUtama.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));
        pnlUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel1.setText("NAMA PASIEN");
        pnlUtama.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

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
        pnlUtama.add(txtKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 900, 25));

        tblPasien.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblPasien);

        pnlUtama.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 1010, 510));

        getContentPane().add(pnlUtama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1050, 600));

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_keluar.png"))); // NOI18N
        btnKeluar.setToolTipText("");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        getContentPane().add(btnKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 220, 130, 30));

        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_login.png"))); // NOI18N
        btnLogin.setToolTipText("");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 180, 130, 30));

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_main.png"))); // NOI18N
        getContentPane().add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKeywordFocusLost
        String keyword = txtKeyword.getText();
        
        if (keyword == null || keyword.equals(""))
            return;

        List<Pasien> list = null;
        
        try {
            list = pasienService.cariGuest(keyword);
        } catch (ServiceException ex) {
            list = new ArrayList<>();
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            PasienCariTableModel tableModel = new PasienCariTableModel(list);
            tblPasien.setModel(tableModel);
        }
    }//GEN-LAST:event_txtKeywordFocusLost

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        Login frameLogin = new Login(this);
        frameLogin.setVisible(true);
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnLogin.requestFocus();
    }//GEN-LAST:event_txtKeywordKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JPanel pnlUtama;
    private javax.swing.JTable tblPasien;
    private javax.swing.JTextField txtKeyword;
    // End of variables declaration//GEN-END:variables
}
