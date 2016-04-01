package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.document.DocumentException;
import com.dbsys.rs.client.document.pdf.PdfProcessor;
import com.dbsys.rs.client.document.pdf.RekapBarangPdfView;
import com.dbsys.rs.client.document.pdf.StokKembaliPdfView;
import com.dbsys.rs.client.tableModel.BarangTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.StokKembaliTableModel;
import com.dbsys.rs.client.tableModel.StokTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.BarangService;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.StokService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.entity.BahanHabisPakai;
import com.dbsys.rs.client.entity.Barang;
import com.dbsys.rs.client.entity.ObatFarmasi;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Stok;
import com.dbsys.rs.client.entity.StokKembali;
import com.dbsys.rs.client.entity.Unit;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class GudangFarmasi extends javax.swing.JFrame {

    private final TokenService tokenService = TokenService.getInstance();
    private final BarangService barangService = BarangService.getInstance();
    private final StokService stokService = StokService.getInstance();
    
    private final PasienService pasienService = PasienService.getInstance();
    
    private final ObatEventController obatEventController = new ObatEventController();
    private final BhpEventController bhpEventController = new BhpEventController();
    
    private Barang barang;
    private Pasien pasien;
    
    // digunakan oleh StokInternal
    private Unit unit;
    
    // Digunakan oleh StokKembali
    private List<StokKembali> listKembali;

    /**
     * Creates new FrameFarmasi
     */
    public GudangFarmasi() {
        initComponents();
        setSize(1280, 800);
        
        paneBarang.setVisible(true);
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
    }

    private void setDetailBarangEksternal(Barang barang) {
        if (barang == null) {
            barang = new Barang();

            this.txtEksternalHarga.setText(null);
            this.txtEksternalTanggungan.setText(null);
            this.txtEksternalJumlah.setText(null);
        } else {
            this.txtEksternalHarga.setText(barang.getHarga().toString());
            this.txtEksternalTanggungan.setText(barang.getPenanggung().toString());
            this.txtEksternalJumlah.setText(barang.getJumlah().toString());
        }
        
        this.txtEksternalKode.setText(barang.getKode());
        this.txtEksternalNama.setText(barang.getNama());
        this.txtEksternalSatuan.setText(barang.getSatuan());

        this.txtEksternalStokJumlah.setText("0");
        this.txtEksternalStokJam.setText(DateUtil.getTime().toString());

        Calendar now = Calendar.getInstance();
        now.setTime(DateUtil.getDate());
        this.txtEksternalStokTanggal.setSelectedDate(now);
        
        tblEksternal.removeAll();
        
        this.barang = barang;
    }

    private void setDetailBarangKembali(Barang barang) {
        if (barang == null) {
            barang = new Barang();

            this.txtKembaliHarga.setText(null);
            this.txtKembaliTanggungan.setText(null);
            this.txtKembaliJumlah.setText(null);
        } else {
            this.txtKembaliHarga.setText(barang.getHarga().toString());
            this.txtKembaliTanggungan.setText(barang.getPenanggung().toString());
            this.txtKembaliJumlah.setText(barang.getJumlah().toString());
        }
        
        this.txtKembaliKode.setText(barang.getKode());
        this.txtKembaliNama.setText(barang.getNama());
        this.txtKembaliSatuan.setText(barang.getSatuan());

        this.txtKembaliStokJumlah.setText("0");
        this.txtKembaliStokJam.setText(DateUtil.getTime().toString());

        Calendar now = Calendar.getInstance();
        now.setTime(DateUtil.getDate());
        this.txtKembaliStokTanggal.setSelectedDate(now);
        
        this.barang = barang;
    }

    private void setDetailBarangKembali(Stok stok) {
        if (stok == null)
            stok = new Stok();

        Barang b = stok.getBarang();
        
        if (b == null) {
            b = new Barang();

            this.txtKembaliHarga.setText(null);
            this.txtKembaliTanggungan.setText(null);
            this.txtKembaliJumlah.setText(null);
        } else {
            this.txtKembaliHarga.setText(b.getHarga().toString());
            this.txtKembaliTanggungan.setText(b.getPenanggung().toString());
            this.txtKembaliJumlah.setText(b.getJumlah().toString());
        }
        
        this.txtKembaliKode.setText(b.getKode());
        this.txtKembaliNama.setText(b.getNama());
        this.txtKembaliSatuan.setText(b.getSatuan());

        this.txtKembaliStokJumlah.setText(stok.getJumlah().toString());
        this.txtKembaliStokTanggal.setText(stok.getTanggal().toString());
        this.txtKembaliStokJam.setText(stok.getJam().toString());
    }

    private void reloadTableEksternal() {
        String keyword = txtEksternalKeyword.getText();
        
        if (keyword.equals(""))
            return;

        List<Barang> list = null;
        try {
            list = barangService.cari(keyword);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            BarangTableModel tableModel = new BarangTableModel(list);
            tblEksternal.setModel(tableModel);
            
            setDetailBarangEksternal(null);
        }
    }
    
    /**
     * Cari barang.
     * 
     * @param keyword 
     */
    private void reloadTableKembali(String keyword) {
        if (keyword.equals(""))
            return;
        
        List<Barang> list = null;
        try {
            list = barangService.cari(keyword);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            BarangTableModel tableModel = new BarangTableModel(list);
            tblKembali.setModel(tableModel);
            
            setDetailBarangKembali((Barang) null);
        }        
    }
    
    private void reloadTableKembali(Pasien pasien) {
        if (pasien == null)
            return;
        
        try {
            listKembali = stokService.stokKembali(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            listKembali = new ArrayList<>();
        } finally {
            StokKembaliTableModel tableModel = new StokKembaliTableModel(listKembali);
            tblKembali.setModel(tableModel);
            
            setDetailBarangKembali((Barang) null);
        }
    }
    
    private void reloadTableKembaliByNomor(String nomor) {
        if (nomor.equals(""))
            return;
        
        try {
            listKembali = stokService.stokKembali(nomor);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            listKembali = new ArrayList<>();
        } finally {
            List<Stok> list = new ArrayList<>();
            for (StokKembali sk : listKembali)
                list.add(sk);
            
            StokTableModel tableModel = new StokTableModel(list);
            tblKembali.setModel(tableModel);
            
            setDetailBarangKembali((Barang) null);
        }        
    }

    private void printStokKembali() {
        if (listKembali == null) {
            JOptionPane.showMessageDialog(this, "Silahkan cari stok kembali berdasarkan pasien atau nomor kembali.");
            return;
        }
        
        PdfProcessor pdfProcessor = new PdfProcessor();
        
        List<StokKembali> list = new ArrayList<>();
        for (Stok stok : listKembali)
            list.add((StokKembali) stok);
        
        StokKembaliPdfView pdfView = new StokKembaliPdfView();
        Map<String, Object> model = new HashMap<>();
        model.put("listKembali", list);
        
        try {
            pdfProcessor.process(pdfView, model, String.format("pengembalian-%s.pdf", DateUtil.getTime().hashCode()));
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private class ObatEventController implements EventController<ObatFarmasi> {
        private final BarangService obatService = BarangService.getInstance();
        private ObatFarmasi model;

        @Override
        public ObatFarmasi getModel() {
            return model;
        }

        @Override
        public void setModel(ObatFarmasi t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new ObatFarmasi();

            String harga = txtObatHarga.getText();
            String jumlah = txtObatJumlah.getText();
            String tanggungan = (String)cbObatTanggungan.getSelectedItem();
            
            model.setPenanggung(Penanggung.valueOf(tanggungan));
            model.setHarga(Long.valueOf(harga));
            model.setJumlah(Long.valueOf(jumlah));
            model.setKode(txtObatKode.getText());
            model.setNama(txtObatNama.getText());
            model.setSatuan(txtObatSatuan.getText());
            model.setKeterangan(txtObatKeterangan.getText());

            obatService.simpan(model);
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblObat.getSelectedRow();

            ObatTableModel tableModel = (ObatTableModel)tblObat.getModel();
            model = tableModel.getObat(row);

            txtObatKode.setText(model.getKode());
            txtObatNama.setText(model.getNama());
            txtObatSatuan.setText(model.getSatuan());
            txtObatKeterangan.setText(model.getKeterangan());
            txtObatHarga.setText(model.getHarga().toString());
            txtObatJumlah.setText(model.getJumlah().toString());
            cbObatTanggungan.setSelectedItem(model.getPenanggung().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txtObatKode.setText(Barang.createKode());
            txtObatNama.setText("");
            txtObatSatuan.setText("");
            txtObatKeterangan.setText("");
            txtObatHarga.setText("");
            txtObatJumlah.setText("");
            cbObatTanggungan.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/kode obat");
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtObatKeyword.getText();
            if (keyword.equals(""))
                throw new ServiceException("Silahkan masukan kata kunci.");
            
            List<Barang> list = obatService.cari(keyword);
            List<ObatFarmasi> listObat = new ArrayList<>();
            for (Barang barang : list) {
                if (barang instanceof ObatFarmasi)
                    listObat.add((ObatFarmasi) barang);
            }

            ObatTableModel tableModel = new ObatTableModel(listObat);
            tblObat.setModel(tableModel);
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private class BhpEventController implements EventController<BahanHabisPakai> {
        private final BarangService bhpService = BarangService.getInstance();
        private BahanHabisPakai model;

        @Override
        public BahanHabisPakai getModel() {
            return model;
        }

        @Override
        public void setModel(BahanHabisPakai t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new BahanHabisPakai();

            String harga = txtBhpHarga.getText();
            String jumlah = txtBhpJumlah.getText();
            String tanggungan = (String)cbBhpTanggungan.getSelectedItem();
            
            model.setPenanggung(Penanggung.valueOf(tanggungan));
            model.setHarga(Long.valueOf(harga));
            model.setJumlah(Long.valueOf(jumlah));
            model.setKode(txtBhpKode.getText());
            model.setNama(txtBhpNama.getText());
            model.setSatuan(txtBhpSatuan.getText());

            bhpService.simpan(model);
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblBhp.getSelectedRow();

            BarangTableModel tableModel = (BarangTableModel)tblBhp.getModel();
            model = (BahanHabisPakai) tableModel.getBarang(row);

            txtBhpKode.setText(model.getKode());
            txtBhpNama.setText(model.getNama());
            txtBhpSatuan.setText(model.getSatuan());
            txtBhpHarga.setText(model.getHarga().toString());
            txtBhpJumlah.setText(model.getJumlah().toString());
            cbBhpTanggungan.setSelectedItem(model.getPenanggung().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txtBhpKode.setText(Barang.createKode());
            txtBhpNama.setText("");
            txtBhpSatuan.setText("");
            txtBhpHarga.setText("");
            txtBhpJumlah.setText("");
            cbBhpTanggungan.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/kode bhp");
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtBhpKeyword.getText();
            if (keyword.equals(""))
                throw new ServiceException("Silahkan masukan kata kunci.");
            
            List<Barang> list = bhpService.cari(keyword, BahanHabisPakai.class);
            BarangTableModel tableModel = new BarangTableModel(list);
            tblBhp.setModel(tableModel);
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

        paneBarang = new javax.swing.JTabbedPane();
        pnlStokEksternal = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtEksternalKeyword = new javax.swing.JTextField();
        pnlEksternalDetail = new javax.swing.JPanel();
        txtEksternalKode = new javax.swing.JTextField();
        txtEksternalNama = new javax.swing.JTextField();
        txtEksternalHarga = new javax.swing.JTextField();
        txtEksternalTanggungan = new javax.swing.JTextField();
        txtEksternalSatuan = new javax.swing.JTextField();
        txtEksternalJumlah = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        pnlEksternalStok = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtEksternalStokJam = new javax.swing.JTextField();
        txtEksternalStokJumlah = new javax.swing.JTextField();
        txtEksternalStokTanggal = new datechooser.beans.DateChooserCombo();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEksternal = new javax.swing.JTable();
        btnEksternalStokMasuk = new javax.swing.JButton();
        btnEksternalStokReset = new javax.swing.JButton();
        btnEksternalStokKeluar = new javax.swing.JButton();
        pnlStokKembali = new javax.swing.JPanel();
        pnlInternalDetail1 = new javax.swing.JPanel();
        txtKembaliKode = new javax.swing.JTextField();
        txtKembaliNama = new javax.swing.JTextField();
        txtKembaliHarga = new javax.swing.JTextField();
        txtKembaliTanggungan = new javax.swing.JTextField();
        txtKembaliSatuan = new javax.swing.JTextField();
        txtKembaliJumlah = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        pnlInternalStok1 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtKembaliStokJam = new javax.swing.JTextField();
        txtKembaliStokJumlah = new javax.swing.JTextField();
        txtKembaliStokTanggal = new datechooser.beans.DateChooserCombo();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblKembali = new javax.swing.JTable();
        txtKembaliKeyword = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNomorPasienKembali = new javax.swing.JTextField();
        txtNamaPasienKembali = new javax.swing.JTextField();
        btnKembaliStokMasuk = new javax.swing.JButton();
        btnKembaliStokReset = new javax.swing.JButton();
        txtNomorKembali = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnCetakNomor = new javax.swing.JButton();
        pnlObat = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtObatKode = new javax.swing.JTextField();
        txtObatNama = new javax.swing.JTextField();
        txtObatHarga = new javax.swing.JTextField();
        txtObatKeterangan = new javax.swing.JTextField();
        cbObatTanggungan = new javax.swing.JComboBox();
        txtObatJumlah = new javax.swing.JTextField();
        txtObatSatuan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtObatKeyword = new javax.swing.JTextField();
        btnTambahObat = new javax.swing.JButton();
        btnClearObat = new javax.swing.JButton();
        pnlBhp = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtBhpKode = new javax.swing.JTextField();
        txtBhpNama = new javax.swing.JTextField();
        txtBhpHarga = new javax.swing.JTextField();
        cbBhpTanggungan = new javax.swing.JComboBox();
        txtBhpJumlah = new javax.swing.JTextField();
        txtBhpSatuan = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        txtBhpKeyword = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        btnTambahBhp = new javax.swing.JButton();
        btnClearBhp = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel31 = new javax.swing.JLabel();
        lblOperator = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        btnRekapBarang = new javax.swing.JButton();
        btnApotek = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("RUMAH SAKIT LIUN KENDAGE TAHUNA - FARMASI");
        setBounds(new java.awt.Rectangle(0, 0, 1280, 800));
        setUndecorated(true);
        getContentPane().setLayout(null);

        paneBarang.setBackground(Utama.colorTransparentPanel);
        paneBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneBarangMouseClicked(evt);
            }
        });

        pnlStokEksternal.setBackground(Utama.colorTransparentPanel);
        pnlStokEksternal.setLayout(null);

        jLabel6.setText("NAMA");
        pnlStokEksternal.add(jLabel6);
        jLabel6.setBounds(20, 15, 90, 14);

        txtEksternalKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEksternalKeywordFocusLost(evt);
            }
        });
        txtEksternalKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEksternalKeywordKeyPressed(evt);
            }
        });
        pnlStokEksternal.add(txtEksternalKeyword);
        txtEksternalKeyword.setBounds(120, 10, 540, 25);

        pnlEksternalDetail.setBackground(Utama.colorTransparentPanel);
        pnlEksternalDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlEksternalDetail.setLayout(null);

        txtEksternalKode.setEditable(false);
        pnlEksternalDetail.add(txtEksternalKode);
        txtEksternalKode.setBounds(130, 20, 390, 25);

        txtEksternalNama.setEditable(false);
        pnlEksternalDetail.add(txtEksternalNama);
        txtEksternalNama.setBounds(130, 50, 390, 25);

        txtEksternalHarga.setEditable(false);
        txtEksternalHarga.setToolTipText("");
        pnlEksternalDetail.add(txtEksternalHarga);
        txtEksternalHarga.setBounds(130, 80, 390, 25);

        txtEksternalTanggungan.setEditable(false);
        txtEksternalTanggungan.setToolTipText("");
        pnlEksternalDetail.add(txtEksternalTanggungan);
        txtEksternalTanggungan.setBounds(130, 110, 390, 25);

        txtEksternalSatuan.setEditable(false);
        pnlEksternalDetail.add(txtEksternalSatuan);
        txtEksternalSatuan.setBounds(130, 170, 390, 25);

        txtEksternalJumlah.setEditable(false);
        pnlEksternalDetail.add(txtEksternalJumlah);
        txtEksternalJumlah.setBounds(130, 140, 390, 25);

        jLabel14.setText("Kode");
        pnlEksternalDetail.add(jLabel14);
        jLabel14.setBounds(20, 20, 90, 25);

        jLabel15.setText("Nama");
        pnlEksternalDetail.add(jLabel15);
        jLabel15.setBounds(20, 50, 90, 25);

        jLabel16.setText("Harga");
        pnlEksternalDetail.add(jLabel16);
        jLabel16.setBounds(20, 80, 90, 25);

        jLabel84.setText("Tanggungan");
        pnlEksternalDetail.add(jLabel84);
        jLabel84.setBounds(20, 110, 90, 25);

        jLabel17.setText("Jumlah");
        pnlEksternalDetail.add(jLabel17);
        jLabel17.setBounds(20, 140, 90, 25);

        jLabel18.setText("Satuan");
        pnlEksternalDetail.add(jLabel18);
        jLabel18.setBounds(20, 170, 90, 25);

        pnlStokEksternal.add(pnlEksternalDetail);
        pnlEksternalDetail.setBounds(680, 50, 540, 220);

        pnlEksternalStok.setBackground(Utama.colorTransparentPanel);
        pnlEksternalStok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlEksternalStok.setLayout(null);

        jLabel19.setText("Tanggal");
        pnlEksternalStok.add(jLabel19);
        jLabel19.setBounds(20, 20, 90, 25);

        jLabel20.setText("Jam");
        pnlEksternalStok.add(jLabel20);
        jLabel20.setBounds(20, 50, 90, 25);

        jLabel21.setText("Jumlah");
        pnlEksternalStok.add(jLabel21);
        jLabel21.setBounds(20, 80, 90, 25);
        pnlEksternalStok.add(txtEksternalStokJam);
        txtEksternalStokJam.setBounds(130, 50, 390, 25);
        pnlEksternalStok.add(txtEksternalStokJumlah);
        txtEksternalStokJumlah.setBounds(130, 80, 390, 25);
        pnlEksternalStok.add(txtEksternalStokTanggal);
        txtEksternalStokTanggal.setBounds(130, 20, 390, 25);

        pnlStokEksternal.add(pnlEksternalStok);
        pnlEksternalStok.setBounds(680, 280, 540, 130);

        tblEksternal.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEksternal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEksternalMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEksternal);

        pnlStokEksternal.add(jScrollPane2);
        jScrollPane2.setBounds(20, 50, 640, 470);

        btnEksternalStokMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_masuk.png"))); // NOI18N
        btnEksternalStokMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokMasukActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokMasuk);
        btnEksternalStokMasuk.setBounds(960, 490, 80, 30);

        btnEksternalStokReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnEksternalStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokResetActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokReset);
        btnEksternalStokReset.setBounds(1140, 490, 80, 30);

        btnEksternalStokKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_keluar.png"))); // NOI18N
        btnEksternalStokKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokKeluarActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokKeluar);
        btnEksternalStokKeluar.setBounds(1050, 490, 80, 30);

        paneBarang.addTab("STOK  MASUK", pnlStokEksternal);

        pnlStokKembali.setBackground(Utama.colorTransparentPanel);
        pnlStokKembali.setLayout(null);

        pnlInternalDetail1.setBackground(Utama.colorTransparentPanel);
        pnlInternalDetail1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInternalDetail1.setLayout(null);

        txtKembaliKode.setEditable(false);
        pnlInternalDetail1.add(txtKembaliKode);
        txtKembaliKode.setBounds(130, 10, 430, 25);

        txtKembaliNama.setEditable(false);
        pnlInternalDetail1.add(txtKembaliNama);
        txtKembaliNama.setBounds(130, 40, 430, 25);

        txtKembaliHarga.setEditable(false);
        txtKembaliHarga.setToolTipText("");
        pnlInternalDetail1.add(txtKembaliHarga);
        txtKembaliHarga.setBounds(130, 70, 430, 25);

        txtKembaliTanggungan.setEditable(false);
        txtKembaliTanggungan.setToolTipText("");
        pnlInternalDetail1.add(txtKembaliTanggungan);
        txtKembaliTanggungan.setBounds(130, 100, 430, 25);

        txtKembaliSatuan.setEditable(false);
        pnlInternalDetail1.add(txtKembaliSatuan);
        txtKembaliSatuan.setBounds(130, 160, 430, 25);

        txtKembaliJumlah.setEditable(false);
        pnlInternalDetail1.add(txtKembaliJumlah);
        txtKembaliJumlah.setBounds(130, 130, 430, 25);

        jLabel34.setText("Kode");
        pnlInternalDetail1.add(jLabel34);
        jLabel34.setBounds(20, 10, 90, 25);

        jLabel37.setText("Nama");
        pnlInternalDetail1.add(jLabel37);
        jLabel37.setBounds(20, 40, 90, 25);

        jLabel38.setText("Harga");
        pnlInternalDetail1.add(jLabel38);
        jLabel38.setBounds(20, 70, 90, 25);

        jLabel86.setText("Tanggungan");
        pnlInternalDetail1.add(jLabel86);
        jLabel86.setBounds(20, 100, 90, 25);

        jLabel39.setText("Jumlah");
        pnlInternalDetail1.add(jLabel39);
        jLabel39.setBounds(20, 130, 90, 25);

        jLabel40.setText("Satuan");
        pnlInternalDetail1.add(jLabel40);
        jLabel40.setBounds(20, 160, 90, 25);

        pnlStokKembali.add(pnlInternalDetail1);
        pnlInternalDetail1.setBounds(20, 330, 590, 200);

        pnlInternalStok1.setBackground(Utama.colorTransparentPanel);
        pnlInternalStok1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInternalStok1.setLayout(null);

        jLabel41.setText("Tanggal");
        pnlInternalStok1.add(jLabel41);
        jLabel41.setBounds(20, 10, 90, 25);

        jLabel42.setText("Jam");
        pnlInternalStok1.add(jLabel42);
        jLabel42.setBounds(20, 40, 90, 25);

        jLabel43.setText("Jumlah");
        pnlInternalStok1.add(jLabel43);
        jLabel43.setBounds(20, 70, 90, 25);
        pnlInternalStok1.add(txtKembaliStokJam);
        txtKembaliStokJam.setBounds(130, 40, 430, 25);
        pnlInternalStok1.add(txtKembaliStokJumlah);
        txtKembaliStokJumlah.setBounds(130, 70, 430, 25);
        pnlInternalStok1.add(txtKembaliStokTanggal);
        txtKembaliStokTanggal.setBounds(130, 10, 430, 25);

        pnlStokKembali.add(pnlInternalStok1);
        pnlInternalStok1.setBounds(630, 330, 590, 110);

        tblKembali.setModel(new javax.swing.table.DefaultTableModel(
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
        tblKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKembaliMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblKembali);

        pnlStokKembali.add(jScrollPane4);
        jScrollPane4.setBounds(20, 110, 1200, 210);

        txtKembaliKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKembaliKeywordFocusLost(evt);
            }
        });
        pnlStokKembali.add(txtKembaliKeyword);
        txtKembaliKeyword.setBounds(120, 70, 390, 25);

        jLabel44.setText("NAMA OBAT/BHP");
        pnlStokKembali.add(jLabel44);
        jLabel44.setBounds(20, 70, 90, 25);

        jLabel1.setText("NO. PASIEN");
        pnlStokKembali.add(jLabel1);
        jLabel1.setBounds(20, 10, 90, 25);

        txtNomorPasienKembali.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomorPasienKembaliFocusLost(evt);
            }
        });
        txtNomorPasienKembali.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomorPasienKembaliKeyPressed(evt);
            }
        });
        pnlStokKembali.add(txtNomorPasienKembali);
        txtNomorPasienKembali.setBounds(120, 10, 390, 25);

        txtNamaPasienKembali.setEditable(false);
        pnlStokKembali.add(txtNamaPasienKembali);
        txtNamaPasienKembali.setBounds(520, 10, 420, 25);

        btnKembaliStokMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_masuk.png"))); // NOI18N
        btnKembaliStokMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliStokMasukActionPerformed(evt);
            }
        });
        pnlStokKembali.add(btnKembaliStokMasuk);
        btnKembaliStokMasuk.setBounds(1050, 490, 80, 30);

        btnKembaliStokReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnKembaliStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliStokResetActionPerformed(evt);
            }
        });
        pnlStokKembali.add(btnKembaliStokReset);
        btnKembaliStokReset.setBounds(1140, 490, 80, 30);

        txtNomorKembali.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomorKembaliFocusLost(evt);
            }
        });
        txtNomorKembali.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomorKembaliKeyPressed(evt);
            }
        });
        pnlStokKembali.add(txtNomorKembali);
        txtNomorKembali.setBounds(120, 40, 390, 25);

        jLabel4.setText("NO. KEMBALI");
        pnlStokKembali.add(jLabel4);
        jLabel4.setBounds(20, 40, 90, 25);

        btnCetakNomor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_cetak.png"))); // NOI18N
        btnCetakNomor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakNomorActionPerformed(evt);
            }
        });
        pnlStokKembali.add(btnCetakNomor);
        btnCetakNomor.setBounds(960, 490, 80, 30);

        paneBarang.addTab("STOK KEMBALI DARI PASIEN", pnlStokKembali);

        pnlObat.setBackground(Utama.colorTransparentPanel);
        pnlObat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblObat.setModel(new javax.swing.table.DefaultTableModel(
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
        tblObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblObatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblObat);

        pnlObat.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 1200, 300));

        jPanel1.setBackground(Utama.colorTransparentPanel);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA OBAT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(txtObatKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 460, 25));
        jPanel1.add(txtObatNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 460, 25));
        jPanel1.add(txtObatHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 460, 25));

        txtObatKeterangan.setToolTipText("");
        jPanel1.add(txtObatKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 460, 25));

        cbObatTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel1.add(cbObatTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 20, 460, 25));

        txtObatJumlah.setToolTipText("");
        jPanel1.add(txtObatJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 50, 460, 25));

        txtObatSatuan.setToolTipText("");
        jPanel1.add(txtObatSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 80, 460, 25));

        jLabel5.setText("KODE");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel7.setText("NAMA");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel8.setText("HARGA");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel9.setText("KETERANGAN");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel87.setText("TANGGUNGAN");
        jPanel1.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 90, 25));

        jLabel10.setText("JUMLAH");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 90, 25));

        jLabel11.setText("SATUAN");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, 90, 25));

        pnlObat.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 1200, 143));

        jPanel2.setBackground(Utama.colorTransparentPanel);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PENCARIAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("NAMA OBAT");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        txtObatKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtObatKeywordFocusLost(evt);
            }
        });
        txtObatKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObatKeywordKeyPressed(evt);
            }
        });
        jPanel2.add(txtObatKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 850, 25));

        pnlObat.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1010, 60));

        btnTambahObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_tambah.png"))); // NOI18N
        btnTambahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahObatActionPerformed(evt);
            }
        });
        pnlObat.add(btnTambahObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 25, 80, 30));

        btnClearObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnClearObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearObatActionPerformed(evt);
            }
        });
        pnlObat.add(btnClearObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 25, 80, 30));

        paneBarang.addTab("DATA OBAT", pnlObat);

        pnlBhp.setBackground(Utama.colorTransparentPanel);
        pnlBhp.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        tblBhp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBhpMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblBhp);

        pnlBhp.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 1200, 310));

        jPanel13.setBackground(Utama.colorTransparentPanel);
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA BAHAN HABIS PAKAI", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel46.setText("KODE");
        jPanel13.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel47.setText("NAMA");
        jPanel13.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel48.setText("HARGA");
        jPanel13.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel49.setText("TANGGUNGAN");
        jPanel13.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 90, 25));

        jLabel50.setText("JUMLAH");
        jPanel13.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, 90, 25));

        jLabel51.setText("SATUAN");
        jPanel13.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 80, 90, 25));
        jPanel13.add(txtBhpKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 460, 25));
        jPanel13.add(txtBhpNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 460, 25));
        jPanel13.add(txtBhpHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 460, 25));

        cbBhpTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel13.add(cbBhpTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, 460, 25));

        txtBhpJumlah.setToolTipText("");
        jPanel13.add(txtBhpJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, 460, 25));

        txtBhpSatuan.setToolTipText("");
        jPanel13.add(txtBhpSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 460, 25));

        pnlBhp.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 1200, 120));

        jPanel17.setBackground(Utama.colorTransparentPanel);
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PENCARIAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBhpKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBhpKeywordFocusLost(evt);
            }
        });
        txtBhpKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBhpKeywordKeyPressed(evt);
            }
        });
        jPanel17.add(txtBhpKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 850, 25));

        jLabel83.setText("NAMA BHP");
        jPanel17.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        pnlBhp.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1010, 60));

        btnTambahBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_tambah.png"))); // NOI18N
        btnTambahBhp.setBorder(null);
        btnTambahBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBhpActionPerformed(evt);
            }
        });
        pnlBhp.add(btnTambahBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 25, 80, 30));

        btnClearBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnClearBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearBhpActionPerformed(evt);
            }
        });
        pnlBhp.add(btnClearBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 25, 80, 30));

        paneBarang.addTab("DATA BARANG HABIS PAKAI", pnlBhp);

        getContentPane().add(paneBarang);
        paneBarang.setBounds(20, 180, 1240, 570);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel31.setText("LOGIN SEBAGAI:");
        jToolBar1.add(jLabel31);

        lblOperator.setText("jLabel32");
        jToolBar1.add(lblOperator);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText(" - ");
        jLabel32.setMaximumSize(new java.awt.Dimension(25, 14));
        jToolBar1.add(jLabel32);

        jLabel33.setText("UNIT: ");
        jToolBar1.add(jLabel33);

        lblUnit.setText("jLabel34");
        jToolBar1.add(lblUnit);

        jSeparator1.setMaximumSize(new java.awt.Dimension(20, 32767));
        jToolBar1.add(jSeparator1);

        btnLogout.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        btnLogout.setText("LOGOUT");
        btnLogout.setFocusable(false);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setMaximumSize(new java.awt.Dimension(80, 21));
        btnLogout.setMinimumSize(new java.awt.Dimension(60, 21));
        btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLogout);

        btnRekapBarang.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        btnRekapBarang.setText("REKAP OBAT/BHP");
        btnRekapBarang.setFocusable(false);
        btnRekapBarang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRekapBarang.setMaximumSize(new java.awt.Dimension(120, 21));
        btnRekapBarang.setMinimumSize(new java.awt.Dimension(100, 21));
        btnRekapBarang.setPreferredSize(new java.awt.Dimension(100, 21));
        btnRekapBarang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRekapBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapBarangActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRekapBarang);

        btnApotek.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        btnApotek.setText("APOTEK");
        btnApotek.setFocusable(false);
        btnApotek.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnApotek.setMaximumSize(new java.awt.Dimension(80, 21));
        btnApotek.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnApotek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApotekActionPerformed(evt);
            }
        });
        jToolBar1.add(btnApotek);

        getContentPane().add(jToolBar1);
        jToolBar1.setBounds(0, 770, 1280, 30);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_apotek.png"))); // NOI18N
        getContentPane().add(background);
        background.setBounds(0, 0, 1280, 800);

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

    private void btnRekapBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapBarangActionPerformed
        PdfProcessor pdfProcessor = new PdfProcessor();
        RekapBarangPdfView pdfView = new RekapBarangPdfView();
        
        try {
            List<Barang> list = barangService.getAll();
            Map<String, Object> model = new HashMap<>();
            model.put("list", list);

            pdfProcessor.process(pdfView, model, String.format("rekap-barang-%s-%s.pdf", DateUtil.getDate().hashCode(), DateUtil.getTime().hashCode()));
        } catch (DocumentException | ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapBarangActionPerformed

    private void btnApotekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApotekActionPerformed
        new Apotek().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnApotekActionPerformed

    private void paneBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneBarangMouseClicked
        setDetailBarangEksternal(null);
        setDetailBarangKembali((Barang) null);

        Integer index = paneBarang.getSelectedIndex();
        
        obatEventController.onCleanForm();
        bhpEventController.onCleanForm();
    }//GEN-LAST:event_paneBarangMouseClicked

    private void btnClearBhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearBhpActionPerformed
        bhpEventController.onCleanForm();
    }//GEN-LAST:event_btnClearBhpActionPerformed

    private void btnTambahBhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBhpActionPerformed
        try {
            bhpEventController.onSave();
            bhpEventController.onCleanForm();
            bhpEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTambahBhpActionPerformed

    private void txtBhpKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBhpKeywordKeyPressed
        if (evt.getKeyCode() == 10)
        btnClearBhp.requestFocus();
    }//GEN-LAST:event_txtBhpKeywordKeyPressed

    private void txtBhpKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBhpKeywordFocusLost
        try {
            bhpEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            BarangTableModel tableModel = new BarangTableModel(null);
            tblBhp.setModel(tableModel);
        }
    }//GEN-LAST:event_txtBhpKeywordFocusLost

    private void tblBhpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBhpMouseClicked
        try {
            bhpEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblBhpMouseClicked

    private void btnClearObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearObatActionPerformed
        obatEventController.onCleanForm();
    }//GEN-LAST:event_btnClearObatActionPerformed

    private void btnTambahObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahObatActionPerformed
        try {
            obatEventController.onSave();
            obatEventController.onCleanForm();
            obatEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
        }
    }//GEN-LAST:event_btnTambahObatActionPerformed

    private void txtObatKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObatKeywordKeyPressed
        if (evt.getKeyCode() == 10)
        btnClearObat.requestFocus();
    }//GEN-LAST:event_txtObatKeywordKeyPressed

    private void txtObatKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtObatKeywordFocusLost
        try {
            obatEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            BarangTableModel tableModel = new BarangTableModel(null);
            tblObat.setModel(tableModel);
        }
    }//GEN-LAST:event_txtObatKeywordFocusLost

    private void tblObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblObatMouseClicked
        try {
            obatEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblObatMouseClicked

    private void btnCetakNomorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakNomorActionPerformed
        printStokKembali();
    }//GEN-LAST:event_btnCetakNomorActionPerformed

    private void txtNomorKembaliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomorKembaliKeyPressed
        if (evt.getKeyCode() == 10)
        btnKembaliStokReset.requestFocus();
    }//GEN-LAST:event_txtNomorKembaliKeyPressed

    private void txtNomorKembaliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorKembaliFocusLost
        String nomor = txtNomorKembali.getText();
        reloadTableKembaliByNomor(nomor);
    }//GEN-LAST:event_txtNomorKembaliFocusLost

    private void btnKembaliStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliStokResetActionPerformed
        setDetailBarangKembali((Barang) null);

        pasien = null;
        txtNomorPasienKembali.setText(null);
        txtNamaPasienKembali.setText(null);
        txtKembaliKeyword.setText(null);
        txtNomorKembali.setText(StokKembali.createKode());
    }//GEN-LAST:event_btnKembaliStokResetActionPerformed

    private void btnKembaliStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliStokMasukActionPerformed
        String jumlah = txtKembaliStokJumlah.getText();
        String jam = txtKembaliStokJam.getText();
        String nomorKembali = txtNomorKembali.getText();

        Calendar tanggal = txtKembaliStokTanggal.getSelectedDate();
        long lTime = tanggal.getTimeInMillis();

        try {
            stokService.kembali(barang, Long.valueOf(jumlah), new Date(lTime), DateUtil.getTime(jam), pasien, nomorKembali);
            JOptionPane.showMessageDialog(this, "Berhasil");

            setDetailBarangKembali((Barang) null);

            String nomor = txtNomorKembali.getText();
            reloadTableKembaliByNomor(nomor);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnKembaliStokMasukActionPerformed

    private void txtNomorPasienKembaliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomorPasienKembaliKeyPressed
        if (evt.getKeyCode() == 10)
        btnKembaliStokReset.requestFocus();
    }//GEN-LAST:event_txtNomorPasienKembaliKeyPressed

    private void txtNomorPasienKembaliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorPasienKembaliFocusLost
        String kode = txtNomorPasienKembali.getText();
        if (kode == null || kode.equals(""))
            return;

        try {
            pasien = pasienService.get(kode);

            txtNamaPasienKembali.setText(pasien.getNama());
            txtNomorKembali.setText(StokKembali.createKode());
            txtKembaliKeyword.setText(null);

            reloadTableKembali(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtNomorPasienKembaliFocusLost

    private void txtKembaliKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKembaliKeywordFocusLost
        String keyword = txtKembaliKeyword.getText();
        reloadTableKembali(keyword);
    }//GEN-LAST:event_txtKembaliKeywordFocusLost

    private void tblKembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKembaliMouseClicked
        Integer index = tblKembali.getSelectedRow();

        TableModel tableModel = tblKembali.getModel();

        if (tableModel instanceof BarangTableModel) {
            BarangTableModel barangTableModel = (BarangTableModel) tableModel;
            barang = barangTableModel.getBarang(index);
            setDetailBarangKembali(barang);
        } else if (tableModel instanceof StokTableModel) {
            StokTableModel stokTableModel = (StokTableModel) tableModel;
            Stok stok = stokTableModel.getStok(index);
            setDetailBarangKembali(stok);
        }
    }//GEN-LAST:event_tblKembaliMouseClicked

    private void btnEksternalStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokKeluarActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }

        Calendar tanggal = txtEksternalStokTanggal.getSelectedDate();
        long lTime = tanggal.getTimeInMillis();

        String jam = txtEksternalStokJam.getText();
        String jumlah = txtEksternalStokJumlah.getText();

        try {
            stokService.keluar(barang, new Long(jumlah), new Date(lTime), DateUtil.getTime(jam));

            setDetailBarangEksternal(null);
            reloadTableEksternal();

            JOptionPane.showMessageDialog(this, "Berhasil");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnEksternalStokKeluarActionPerformed

    private void btnEksternalStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokResetActionPerformed
        setDetailBarangEksternal(null);
    }//GEN-LAST:event_btnEksternalStokResetActionPerformed

    private void btnEksternalStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokMasukActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }

        Calendar tanggal = txtEksternalStokTanggal.getSelectedDate();
        long lTime = tanggal.getTimeInMillis();

        String jam = txtEksternalStokJam.getText();
        String jumlah = txtEksternalStokJumlah.getText();

        try {
            stokService.masuk(barang, new Long(jumlah), new Date(lTime), DateUtil.getTime(jam));

            setDetailBarangEksternal(null);
            reloadTableEksternal();

            JOptionPane.showMessageDialog(this, "Berhasil");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnEksternalStokMasukActionPerformed

    private void tblEksternalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEksternalMouseClicked
        Integer index = tblEksternal.getSelectedRow();

        BarangTableModel tableModel = (BarangTableModel)tblEksternal.getModel();
        Barang b = (Barang) tableModel.getBarang(index);

        setDetailBarangEksternal(b);
    }//GEN-LAST:event_tblEksternalMouseClicked

    private void txtEksternalKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEksternalKeywordKeyPressed
        if (evt.getKeyCode() == 10)
        btnEksternalStokReset.requestFocus();
    }//GEN-LAST:event_txtEksternalKeywordKeyPressed

    private void txtEksternalKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEksternalKeywordFocusLost
        reloadTableEksternal();
    }//GEN-LAST:event_txtEksternalKeywordFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JButton btnApotek;
    private javax.swing.JButton btnCetakNomor;
    private javax.swing.JButton btnClearBhp;
    private javax.swing.JButton btnClearObat;
    private javax.swing.JButton btnEksternalStokKeluar;
    private javax.swing.JButton btnEksternalStokMasuk;
    private javax.swing.JButton btnEksternalStokReset;
    private javax.swing.JButton btnKembaliStokMasuk;
    private javax.swing.JButton btnKembaliStokReset;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRekapBarang;
    private javax.swing.JButton btnTambahBhp;
    private javax.swing.JButton btnTambahObat;
    private javax.swing.JComboBox cbBhpTanggungan;
    private javax.swing.JComboBox cbObatTanggungan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JTabbedPane paneBarang;
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlEksternalDetail;
    private javax.swing.JPanel pnlEksternalStok;
    private javax.swing.JPanel pnlInternalDetail1;
    private javax.swing.JPanel pnlInternalStok1;
    private javax.swing.JPanel pnlObat;
    private javax.swing.JPanel pnlStokEksternal;
    private javax.swing.JPanel pnlStokKembali;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblEksternal;
    private javax.swing.JTable tblKembali;
    private javax.swing.JTable tblObat;
    private javax.swing.JTextField txtBhpHarga;
    private javax.swing.JTextField txtBhpJumlah;
    private javax.swing.JTextField txtBhpKeyword;
    private javax.swing.JTextField txtBhpKode;
    private javax.swing.JTextField txtBhpNama;
    private javax.swing.JTextField txtBhpSatuan;
    private javax.swing.JTextField txtEksternalHarga;
    private javax.swing.JTextField txtEksternalJumlah;
    private javax.swing.JTextField txtEksternalKeyword;
    private javax.swing.JTextField txtEksternalKode;
    private javax.swing.JTextField txtEksternalNama;
    private javax.swing.JTextField txtEksternalSatuan;
    private javax.swing.JTextField txtEksternalStokJam;
    private javax.swing.JTextField txtEksternalStokJumlah;
    private datechooser.beans.DateChooserCombo txtEksternalStokTanggal;
    private javax.swing.JTextField txtEksternalTanggungan;
    private javax.swing.JTextField txtKembaliHarga;
    private javax.swing.JTextField txtKembaliJumlah;
    private javax.swing.JTextField txtKembaliKeyword;
    private javax.swing.JTextField txtKembaliKode;
    private javax.swing.JTextField txtKembaliNama;
    private javax.swing.JTextField txtKembaliSatuan;
    private javax.swing.JTextField txtKembaliStokJam;
    private javax.swing.JTextField txtKembaliStokJumlah;
    private datechooser.beans.DateChooserCombo txtKembaliStokTanggal;
    private javax.swing.JTextField txtKembaliTanggungan;
    private javax.swing.JTextField txtNamaPasienKembali;
    private javax.swing.JTextField txtNomorKembali;
    private javax.swing.JTextField txtNomorPasienKembali;
    private javax.swing.JTextField txtObatHarga;
    private javax.swing.JTextField txtObatJumlah;
    private javax.swing.JTextField txtObatKeterangan;
    private javax.swing.JTextField txtObatKeyword;
    private javax.swing.JTextField txtObatKode;
    private javax.swing.JTextField txtObatNama;
    private javax.swing.JTextField txtObatSatuan;
    // End of variables declaration//GEN-END:variables
}
