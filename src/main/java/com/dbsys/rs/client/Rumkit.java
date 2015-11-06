package com.dbsys.rs.client;

import javax.swing.UIManager;

/**
 *
 * @author Bramwell Kasaedja
 */
public class Rumkit {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        setLookAndFeel();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameLogin frm = new FrameLogin();
                frm.setVisible(true);
            }
        });
    }
    
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
