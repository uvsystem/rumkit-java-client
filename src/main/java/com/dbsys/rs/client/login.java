package com.dbsys.rs.client;

import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.lib.Credential;
import com.dbsys.rs.lib.entity.Token;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 */
public class login extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    public login() {
        initComponents();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btn_login_ok = new javax.swing.JButton();
        btn_login_x = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_login_uname = new javax.swing.JTextField();
        txt_login_pass = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_login_ok.setText("LOGIN");
        btn_login_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_login_okActionPerformed(evt);
            }
        });
        jPanel1.add(btn_login_ok, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, -1, -1));

        btn_login_x.setText("BATAL");
        jPanel1.add(btn_login_x, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel1.setText("USERNAME");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 12, -1, -1));

        jLabel2.setText("PASSWORD");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, -1, -1));
        jPanel1.add(txt_login_uname, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 110, -1));
        jPanel1.add(txt_login_pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 110, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 120));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_login_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_login_okActionPerformed
        Credential credential = new Credential();
        String username = txt_login_uname.getText();
        String pass = String.valueOf(txt_login_pass.getPassword());
        
        credential.setUsername(username);
        credential.setPassword(pass);
        
        TokenService tokenservice = new TokenService();
        
        Token token;
        try {
            token = tokenservice.create(credential);   
            TokenHolder.token = token;
            
            new admin().setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_login_okActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_login_ok;
    private javax.swing.JButton btn_login_x;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txt_login_pass;
    private javax.swing.JTextField txt_login_uname;
    // End of variables declaration//GEN-END:variables
}
