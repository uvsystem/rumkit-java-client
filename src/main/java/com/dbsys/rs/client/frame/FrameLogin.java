package com.dbsys.rs.client.frame;

import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.Credential;
import com.dbsys.rs.lib.entity.Operator.Role;
import com.dbsys.rs.lib.entity.Token;
import com.dbsys.rs.lib.entity.Unit;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 */
public class FrameLogin extends javax.swing.JFrame {

    private FrameUtama frameUtama;
    
    /**
     * Creates new form login
     * @param frameUtama
     */
    public FrameLogin(FrameUtama frameUtama) {
        initComponents();
        this.setSize(250, 160);
        this.setLocationRelativeTo(null);
        this.frameUtama = frameUtama;
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
        jLabel2 = new javax.swing.JLabel();
        txt_login_uname = new javax.swing.JTextField();
        txt_login_pass = new javax.swing.JPasswordField();
        btnCancel = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("USER LOGIN");
        setAlwaysOnTop(true);
        setUndecorated(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("USERNAME");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 110, -1));

        jLabel2.setText("PASSWORD");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 55, 110, -1));

        txt_login_uname.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txt_login_uname.setPreferredSize(new java.awt.Dimension(100, 25));
        getContentPane().add(txt_login_uname, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 25, 110, 25));

        txt_login_pass.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        getContentPane().add(txt_login_pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 110, 25));

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Cancel Login.png"))); // NOI18N
        btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCancel.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 80, 40));

        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Button Login.png"))); // NOI18N
        btnLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLogin.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 80, 40));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Login Form.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
        if (frameUtama == null)
            frameUtama = new FrameUtama();
        frameUtama.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        TokenService tokenservice = TokenService.getInstance();
        
        String username = txt_login_uname.getText();
        String pass = String.valueOf(txt_login_pass.getPassword());

        Credential credential = new Credential();
        credential.setUsername(username);
        credential.setPassword(pass);

        try {
            Token token = tokenservice.create(credential);
            TokenHolder.token = token;

            if(token.getRole().equals(Role.ADMIN)) {
                new FrameAdmin().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.LOKET_PENDAFTARAN)) {
                new FramePendaftaran().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.LOKET_PEMBAYARAN)) {
                new FramePembayaran().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.APOTEK_FARMASI)) {
                new FrameApotek().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.GUDANG_FARMASI)) {
                new FrameGudangFarmasi().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.RUANG_PERAWATAN)) {
                new FrameSal().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.ICU)) {
                new FrameSal().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.UGD)) {
                new FrameUgd().setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.POLIKLINIK)) {
                new FramePoliklinik(token.getOperator().getUnit()).setVisible(true);
            } else if (token.getTipe().equals(Unit.TipeUnit.PENUNJANG_MEDIK)) {
                new FramePoliklinik(token.getOperator().getUnit()).setVisible(true);
            } else {
                throw new ServiceException("Anda tidak terdaftar pada unit apapun");
            }
            
            this.dispose();
            frameUtama.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField txt_login_pass;
    private javax.swing.JTextField txt_login_uname;
    // End of variables declaration//GEN-END:variables
}
