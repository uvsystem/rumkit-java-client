package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.KategoriTableModel;
import com.dbsys.rs.client.tableModel.UnitTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.service.KategoriService;
import com.dbsys.rs.connector.service.UnitService;
import com.dbsys.rs.lib.entity.KategoriTindakan;
import com.dbsys.rs.lib.entity.Unit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Frame untuk melakukan pencarian data. Dapat digunakan untuk semua data.<br />
 * Untuk membedakan pencarian kelas menggunakan {@code cls} pada constructor dan {@code frame} pemanggil,
 * supaya setelah pencarian berhasil akan kembali ke frame pemanggil tersebut.
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class FrameCari extends javax.swing.JFrame {
    private final JFrame frame;
    private final Class<?> cls;
    
    private UnitService unitService;
    private KategoriService kategoriService;
    
    /**
     * Creates new form formCari
     * 
     * @param frame frame yang memanggil formCari.
     * @param cls class sebagai pembeda fungsi cari.
     */
    public FrameCari(JFrame frame, Class<?> cls) {
        initComponents();

        this.setLocationRelativeTo(null);
        this.frame = frame;
        this.cls = cls;
        
        chkTambah.setVisible(false);

        disableTambah();
        
        if (cls.equals(Unit.class)){
            unitService = UnitService.getInstance(EventController.host);
            txtKeyword.setEnabled(false);
            btnCari.setEnabled(false);

            loadTableUnit();
        } else if (cls.equals(KategoriTindakan.class)) {
            kategoriService = KategoriService.getInstance(EventController.host);
            chkTambah.setVisible(true);

            loadTableKategori();
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

        jLabel1 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCari = new javax.swing.JTable();
        btnPilih = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        pnlKategori = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtKategoriNama = new javax.swing.JTextField();
        txtKategoriParent = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        chkTambah = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CARI DATA");
        setResizable(false);

        jLabel1.setText("Kata Kunci");

        btnCari.setText("OK");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        tblCari.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCariMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCari);

        btnPilih.setText("PILIH");
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });

        jLabel2.setText("PENCARIAN");

        pnlKategori.setBorder(javax.swing.BorderFactory.createTitledBorder("Tambah Kategori"));
        pnlKategori.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Nama");
        pnlKategori.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 30, -1, -1));

        jLabel4.setText("Parent");
        pnlKategori.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 74, -1, -1));
        pnlKategori.add(txtKategoriNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 230, -1));

        txtKategoriParent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKategoriParentMouseClicked(evt);
            }
        });
        pnlKategori.add(txtKategoriParent, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 66, 229, -1));

        btnSimpan.setText("TAMBAH");
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });
        pnlKategori.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, -1, -1));

        chkTambah.setText("Tambah");
        chkTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(226, 226, 226))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtKeyword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPilih)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkTambah))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlKategori, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari)
                    .addComponent(btnPilih)
                    .addComponent(chkTambah))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(pnlKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String keyword = txtKeyword.getText();
        if (keyword.equals(""))
            return;
        
        if (cls.equals(Unit.class)) {
            cariUnit(keyword);
        } else if (cls.equals(KategoriTindakan.class)) {
            cariKategori(keyword);
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihActionPerformed
        if(cls.equals(Unit.class)){
            pilihUnit();
        } else if (cls.equals(KategoriTindakan.class)) {
            pilihKategori();
        }

        this.dispose();
    }//GEN-LAST:event_btnPilihActionPerformed

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
        KategoriTindakan parent = null;
        if (!txtKategoriParent.getText().equals(""))
            parent = getKategori();
        
        KategoriTindakan kategori = new KategoriTindakan();
        kategori.setParent(parent);
        kategori.setNama(txtKategoriNama.getText());
        
        try {
            kategori = kategoriService.simpan(kategori);
            ((FrameAdmin)frame).setKategoriForTindakan(kategori);
            
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanMouseClicked

    private void txtKategoriParentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKategoriParentMouseClicked
        JOptionPane.showMessageDialog(this, "Silahkan pilih dari tabel di atas");
    }//GEN-LAST:event_txtKategoriParentMouseClicked

    private void tblCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCariMouseClicked
        if (cls.equals(KategoriTindakan.class)) {
            KategoriTindakan kategori = getKategori();
            txtKategoriParent.setText(kategori.getNama());
        }
    }//GEN-LAST:event_tblCariMouseClicked

    private void chkTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTambahActionPerformed
        if (chkTambah.isSelected()) {
            enableTambah();
        } else {
            disableTambah();
        }
    }//GEN-LAST:event_chkTambahActionPerformed
 
    private void enableTambah() {
        btnPilih.setEnabled(false);
        btnSimpan.setEnabled(true);
        txtKategoriNama.setEnabled(true);
        txtKategoriParent.setEnabled(true);
        
        this.setSize(500, 420);
        pnlKategori.setVisible(true);
    }
    
    private void disableTambah() {
        btnPilih.setEnabled(true);
        btnSimpan.setEnabled(false);
        txtKategoriNama.setEditable(false);
        txtKategoriParent.setEditable(false);

        this.setSize(500, 300);
        pnlKategori.setVisible(false);
    }
    
    private KategoriTindakan getKategori() {
        int row = tblCari.getSelectedRow();

        KategoriTableModel tableModel = (KategoriTableModel)tblCari.getModel();
        KategoriTindakan kategori = tableModel.getKategori(row);
        
        return kategori;
    }
    
    private void pilihUnit(){
        int row = tblCari.getSelectedRow();
        UnitTableModel model = (UnitTableModel)tblCari.getModel();
        Unit unit = model.getUnit(row);

        ((FrameAdmin)frame).setUnitForOperator(unit);
    }
    
    private void pilihKategori(){
        KategoriTindakan kategori = getKategori();
        ((FrameAdmin)frame).setKategoriForTindakan(kategori);
    }
   
    public final void loadTableUnit(){
        try {
            List<Unit> listUnit = unitService.getAll();
            UnitTableModel model = new UnitTableModel(listUnit);
            tblCari.setModel(model);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void cariUnit(String keyword) {
        JOptionPane.showMessageDialog(this, "Maaf belum dapat mencari unit");
    }
    
    private void loadTableKategori() {
        try {
            List<KategoriTindakan> list = kategoriService.getAll();
            KategoriTableModel tableModel = new KategoriTableModel(list);
            tblCari.setModel(tableModel);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void cariKategori(String keyword) {
        JOptionPane.showMessageDialog(this, "Maaf belum dapat mencari kategori");
    }
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnPilih;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JCheckBox chkTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlKategori;
    private javax.swing.JTable tblCari;
    private javax.swing.JTextField txtKategoriNama;
    private javax.swing.JTextField txtKategoriParent;
    private javax.swing.JTextField txtKeyword;
    // End of variables declaration//GEN-END:variables
}
