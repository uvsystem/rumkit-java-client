package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.TindakanFrame;
import com.dbsys.rs.client.UnitFrame;
import com.dbsys.rs.client.tableModel.DokterTableModel;
import com.dbsys.rs.client.tableModel.KategoriTableModel;
import com.dbsys.rs.client.tableModel.PegawaiTableModel;
import com.dbsys.rs.client.tableModel.UnitTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.service.KategoriService;
import com.dbsys.rs.connector.service.PegawaiServices;
import com.dbsys.rs.connector.service.UnitService;
import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.KategoriTindakan;
import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Perawat;
import com.dbsys.rs.lib.entity.Unit;
import java.util.ArrayList;
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
public class Pencarian extends JFrame {
    private final JFrame frame;
    private final Class<?> cls;
    
    private UnitService unitService;
    private KategoriService kategoriService;
    private PegawaiServices dokterService;
    private PegawaiServices perawatService;
    
    /**
     * Creates new form formCari
     * 
     * @param frame frame yang memanggil formCari.
     * @param cls class sebagai pembeda fungsi cari.
     */
    public Pencarian(JFrame frame, Class<?> cls) {
        initComponents();

        this.setLocationRelativeTo(null);
        this.frame = frame;
        this.cls = cls;
        
        chkTambah.setVisible(false);
        pnlKategori.setVisible(false);

        disableTambah();
        
        if (cls.equals(Unit.class)){
            unitService = UnitService.getInstance();
            txtKeyword.setEnabled(false);

            loadTableUnit();
        } else if (cls.equals(KategoriTindakan.class)) {
            kategoriService = KategoriService.getInstance();
            txtKeyword.setEnabled(false);
            chkTambah.setVisible(true);
            pnlKategori.setVisible(true);

            loadTableKategori();
        } else if (cls.equals(Dokter.class)) {
            dokterService = PegawaiServices.getInstance();

            loadTableDokter(null);
        } else if (cls.equals(Perawat.class)) {
            perawatService = PegawaiServices.getInstance();

            loadTablePerawat(null);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCari = new javax.swing.JTable();
        btnPilih = new javax.swing.JButton();
        pnlKategori = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtKategoriNama = new javax.swing.JTextField();
        txtKategoriParent = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        chkTambah = new javax.swing.JCheckBox();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CARI DATA");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Kata Kunci");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 80, 25));

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
        getContentPane().add(txtKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 200, 25));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 460, 154));

        btnPilih.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_pilih.png"))); // NOI18N
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });
        getContentPane().add(btnPilih, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 80, 25));

        pnlKategori.setBackground(Utama.colorTransparentPanel);
        pnlKategori.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "TAMBAH KATEGORI"));
        pnlKategori.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Nama");
        pnlKategori.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, 25));

        jLabel4.setText("Parent");
        pnlKategori.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, 25));
        pnlKategori.add(txtKategoriNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 200, 25));

        txtKategoriParent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKategoriParentMouseClicked(evt);
            }
        });
        pnlKategori.add(txtKategoriParent, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 200, 25));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });
        pnlKategori.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 57, 80, 30));

        getContentPane().add(pnlKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 460, 109));

        chkTambah.setBackground(Utama.colorTransparentPanel);
        chkTambah.setText("Tambah");
        chkTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTambahActionPerformed(evt);
            }
        });
        getContentPane().add(chkTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 70, 25));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Pencarian.jpg"))); // NOI18N
        Background.setText("jLabel2");
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihActionPerformed
        if(cls.equals(Unit.class)){
            pilihUnit();
        } else if (cls.equals(KategoriTindakan.class)) {
            pilihKategori();
        } else if (cls.equals(Dokter.class)) {
            pilihPegawai();
        } else if (cls.equals(Perawat.class)) {
            pilihPegawai();
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
            ((Administrator)frame).setKategoriForTindakan(kategori);
            
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

    private void txtKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKeywordFocusLost
        String keyword = txtKeyword.getText();
        
        if (keyword.equals(""))
            return;
        
        if (cls.equals(Dokter.class)) {
            loadTableDokter(keyword);
        } else if (cls.equals(Perawat.class)) {
            loadTablePerawat(keyword);
        }
    }//GEN-LAST:event_txtKeywordFocusLost

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnPilih.requestFocus();
    }//GEN-LAST:event_txtKeywordKeyPressed
 
    private void enableTambah() {
        btnPilih.setEnabled(false);
        btnSimpan.setEnabled(true);
        txtKategoriNama.setEditable(true);
        txtKategoriParent.setEditable(true);
        
        this.setSize(500, 470);
        pnlKategori.setVisible(true);
    }
    
    private void disableTambah() {
        btnPilih.setEnabled(true);
        btnSimpan.setEnabled(false);
        txtKategoriNama.setEditable(false);
        txtKategoriParent.setEditable(false);

        this.setSize(500, 330);
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

        ((UnitFrame)frame).setUnit(unit);
    }
    
    private void pilihKategori(){
        KategoriTindakan kategori = getKategori();
        ((Administrator)frame).setKategoriForTindakan(kategori);
    }
    
    private void pilihPegawai() {
        int index = tblCari.getSelectedRow();
        Pegawai pegawai = null;

        if (cls.equals(Dokter.class)) {
            DokterTableModel tableModel = (DokterTableModel)tblCari.getModel();
            pegawai = tableModel.getDokter(index);
        } else if (cls.equals(Perawat.class)) {
            PegawaiTableModel tableModel = (PegawaiTableModel)tblCari.getModel();
            pegawai = tableModel.getPegawai(index);
        }

        ((TindakanFrame)frame).setPegawaiForPelayanan(pegawai);
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
    
    private void loadTableKategori() {
        try {
            List<KategoriTindakan> list = kategoriService.getAll();
            KategoriTableModel tableModel = new KategoriTableModel(list);
            tblCari.setModel(tableModel);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void loadTableDokter(String keyword) {
        List<Pegawai> list;
        
        try {
            if (keyword == null) {
                list = dokterService.getAll(Dokter.class);
            } else {
                list = dokterService.cari(keyword, Dokter.class);
            }

            List<Dokter> listDokter = new ArrayList<>();
            for (Pegawai pegawai : list) {
                if (pegawai instanceof Dokter)
                    listDokter.add((Dokter) pegawai);
            }
            
            DokterTableModel tableModel = new DokterTableModel(listDokter);
            tblCari.setModel(tableModel);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    private void loadTablePerawat(String keyword) {
        List<Pegawai> list;
        
        try {
            if (keyword == null) {
                list = perawatService.getAll(Perawat.class);
            } else {
                list = perawatService.cari(keyword, Perawat.class);
            }
            
            List<Perawat> listPerawat = new ArrayList<>();
            PegawaiTableModel tableModel = new PegawaiTableModel(list);
            tblCari.setModel(tableModel);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton btnPilih;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JCheckBox chkTambah;
    private javax.swing.JLabel jLabel1;
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
