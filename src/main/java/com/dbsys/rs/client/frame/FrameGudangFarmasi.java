package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import static com.dbsys.rs.client.EventController.host;
import com.dbsys.rs.client.UnitFrame;
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
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokKembali;
import com.dbsys.rs.lib.entity.Unit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class FrameGudangFarmasi extends javax.swing.JFrame implements UnitFrame {

    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    private final BarangService barangService = BarangService.getInstance(EventController.host);
    private final StokService stokService = StokService.getInstance(EventController.host);
    
    private final PasienService pasienService = PasienService.getInstance(EventController.host);
    
    private final ObatEventController obatEventController = new ObatEventController();
    private final BhpEventController bhpEventController = new BhpEventController();
    
    private Barang barang;
    private Pasien pasien;
    
    // digunakan oleh StokInternal
    private Unit unit;

    /**
     * Creates new FrameFarmasi
     */
    public FrameGudangFarmasi() {
        initComponents();
        setSize(1280, 800);
        
        paneBarang.setVisible(true);
        
        lblOperator.setText(TokenHolder.getNamaOperator());
        lblUnit.setText(TokenHolder.getNamaUnit());
    }
    
    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
        
        txtInternalStokUnit.setText(unit.getNama());
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
        this.txtEksternalStokTanggal.setText(DateUtil.getDate().toString());
        this.txtEksternalStokJam.setText(DateUtil.getTime().toString());
        
        tblEksternal.removeAll();
        
        this.barang = barang;
    }

    private void setDetailBarangInternal(Barang barang) {
        if (barang == null) {
            barang = new Barang();

            this.txtInternalHarga.setText(null);
            this.txtInternalTanggungan.setText(null);
            this.txtInternalJumlah.setText(null);
        } else {
            this.txtInternalHarga.setText(barang.getHarga().toString());
            this.txtInternalTanggungan.setText(barang.getPenanggung().toString());
            this.txtInternalJumlah.setText(barang.getJumlah().toString());
        }
        
        this.txtInternalKode.setText(barang.getKode());
        this.txtInternalNama.setText(barang.getNama());
        this.txtInternalSatuan.setText(barang.getSatuan());

        this.txtInternalStokJumlah.setText("0");
        this.txtInternalStokTanggal.setText(DateUtil.getDate().toString());
        this.txtInternalStokJam.setText(DateUtil.getTime().toString());
        
        tblInternal.removeAll();
        
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
        this.txtKembaliStokTanggal.setText(DateUtil.getDate().toString());
        this.txtKembaliStokJam.setText(DateUtil.getTime().toString());
        
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
    
    private void reloadTableInternal() {
        String keyword = txtInternalKeyword.getText();
        
        if (keyword.equals(""))
            return;
        
        List<Barang> list = null;
        try {
            list = barangService.cari(keyword);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            BarangTableModel tableModel = new BarangTableModel(list);
            tblInternal.setModel(tableModel);
            
            setDetailBarangInternal(null);
        }        
    }
    
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
        
        List<Stok> list = new ArrayList<>();
        try {
            list = stokService.stokKembali(pasien);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            StokKembaliTableModel tableModel = new StokKembaliTableModel(list);
            tblKembali.setModel(tableModel);
            
            setDetailBarangKembali((Barang) null);
        }
    }
    
    private void reloadTableKembaliByNomor(String nomor) {
        if (nomor.equals(""))
            return;
        
        List<Stok> list = new ArrayList<>();
        try {
            list = stokService.stokKembali(nomor);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            StokTableModel tableModel = new StokTableModel(list);
            tblKembali.setModel(tableModel);
            
            setDetailBarangKembali((Barang) null);
        }        
    }

    private class ObatEventController implements EventController<ObatFarmasi> {
        private final BarangService obatService = BarangService.getInstance(host);
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
        
    }

    private class BhpEventController implements EventController<BahanHabisPakai> {
        private final BarangService bhpService = BarangService.getInstance(host);
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
        txtEksternalStokTanggal = new javax.swing.JTextField();
        txtEksternalStokJam = new javax.swing.JTextField();
        txtEksternalStokJumlah = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEksternal = new javax.swing.JTable();
        btnEksternalStokMasuk = new javax.swing.JButton();
        btnEksternalStokReset = new javax.swing.JButton();
        btnEksternalStokKeluar = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        pnlStokInternal = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtInternalKeyword = new javax.swing.JTextField();
        pnlInternalDetail = new javax.swing.JPanel();
        txtInternalKode = new javax.swing.JTextField();
        txtInternalNama = new javax.swing.JTextField();
        txtInternalHarga = new javax.swing.JTextField();
        txtInternalTanggungan = new javax.swing.JTextField();
        txtInternalSatuan = new javax.swing.JTextField();
        txtInternalJumlah = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        pnlInternalStok = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtInternalStokTanggal = new javax.swing.JTextField();
        txtInternalStokJam = new javax.swing.JTextField();
        txtInternalStokJumlah = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtInternalStokUnit = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblInternal = new javax.swing.JTable();
        btnInternalStokReset = new javax.swing.JButton();
        btnInternalStokKeluar = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
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
        txtKembaliStokTanggal = new javax.swing.JTextField();
        txtKembaliStokJam = new javax.swing.JTextField();
        txtKembaliStokJumlah = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblKembali = new javax.swing.JTable();
        txtKembaliKeyword = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNomorPasienKembali = new javax.swing.JTextField();
        txtNamaPasienKembali = new javax.swing.JTextField();
        btnKembaliStokMasuk = new javax.swing.JButton();
        btnKembaliStokReset = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtNomorKembali = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
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
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("RUMAH SAKIT LIUN KENDAGE TAHUNA - FARMASI");
        setBounds(new java.awt.Rectangle(0, 0, 1280, 800));
        setUndecorated(true);
        getContentPane().setLayout(null);

        paneBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paneBarangMouseClicked(evt);
            }
        });

        pnlStokEksternal.setLayout(null);

        jLabel6.setText("KATA KUNCI");
        pnlStokEksternal.add(jLabel6);
        jLabel6.setBounds(20, 15, 90, 14);

        txtEksternalKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEksternalKeywordFocusLost(evt);
            }
        });
        pnlStokEksternal.add(txtEksternalKeyword);
        txtEksternalKeyword.setBounds(120, 10, 280, 25);

        pnlEksternalDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlEksternalDetail.setLayout(null);

        txtEksternalKode.setEditable(false);
        pnlEksternalDetail.add(txtEksternalKode);
        txtEksternalKode.setBounds(140, 30, 420, 25);

        txtEksternalNama.setEditable(false);
        pnlEksternalDetail.add(txtEksternalNama);
        txtEksternalNama.setBounds(140, 60, 420, 25);

        txtEksternalHarga.setEditable(false);
        txtEksternalHarga.setToolTipText("");
        pnlEksternalDetail.add(txtEksternalHarga);
        txtEksternalHarga.setBounds(140, 90, 420, 25);

        txtEksternalTanggungan.setEditable(false);
        txtEksternalTanggungan.setToolTipText("");
        pnlEksternalDetail.add(txtEksternalTanggungan);
        txtEksternalTanggungan.setBounds(140, 120, 420, 25);

        txtEksternalSatuan.setEditable(false);
        pnlEksternalDetail.add(txtEksternalSatuan);
        txtEksternalSatuan.setBounds(140, 180, 420, 25);

        txtEksternalJumlah.setEditable(false);
        pnlEksternalDetail.add(txtEksternalJumlah);
        txtEksternalJumlah.setBounds(140, 150, 420, 25);

        jLabel14.setText("Kode");
        pnlEksternalDetail.add(jLabel14);
        jLabel14.setBounds(30, 30, 90, 14);

        jLabel15.setText("Nama");
        pnlEksternalDetail.add(jLabel15);
        jLabel15.setBounds(30, 60, 90, 14);

        jLabel16.setText("Harga");
        pnlEksternalDetail.add(jLabel16);
        jLabel16.setBounds(30, 90, 90, 14);

        jLabel84.setText("Tanggungan");
        pnlEksternalDetail.add(jLabel84);
        jLabel84.setBounds(30, 120, 90, 14);

        jLabel17.setText("Jumlah");
        pnlEksternalDetail.add(jLabel17);
        jLabel17.setBounds(30, 150, 90, 14);

        jLabel18.setText("Satuan");
        pnlEksternalDetail.add(jLabel18);
        jLabel18.setBounds(30, 180, 90, 14);

        pnlStokEksternal.add(pnlEksternalDetail);
        pnlEksternalDetail.setBounds(20, 310, 590, 220);

        pnlEksternalStok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlEksternalStok.setLayout(null);

        jLabel19.setText("Tanggal");
        pnlEksternalStok.add(jLabel19);
        jLabel19.setBounds(30, 30, 90, 14);

        jLabel20.setText("Jam");
        pnlEksternalStok.add(jLabel20);
        jLabel20.setBounds(30, 60, 90, 14);

        jLabel21.setText("Jumlah");
        pnlEksternalStok.add(jLabel21);
        jLabel21.setBounds(30, 90, 90, 14);
        pnlEksternalStok.add(txtEksternalStokTanggal);
        txtEksternalStokTanggal.setBounds(140, 30, 420, 25);
        pnlEksternalStok.add(txtEksternalStokJam);
        txtEksternalStokJam.setBounds(140, 60, 420, 25);
        pnlEksternalStok.add(txtEksternalStokJumlah);
        txtEksternalStokJumlah.setBounds(140, 90, 420, 25);

        pnlStokEksternal.add(pnlEksternalStok);
        pnlEksternalStok.setBounds(630, 310, 590, 140);

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
        jScrollPane2.setBounds(20, 50, 1200, 240);

        btnEksternalStokMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/barangMasuk_Icon.png"))); // NOI18N
        btnEksternalStokMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokMasukActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokMasuk);
        btnEksternalStokMasuk.setBounds(870, 470, 100, 40);

        btnEksternalStokReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/BarangReset_Icon.png"))); // NOI18N
        btnEksternalStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokResetActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokReset);
        btnEksternalStokReset.setBounds(1130, 470, 90, 39);

        btnEksternalStokKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/barangKeluar_Icon.png"))); // NOI18N
        btnEksternalStokKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEksternalStokKeluarActionPerformed(evt);
            }
        });
        pnlStokEksternal.add(btnEksternalStokKeluar);
        btnEksternalStokKeluar.setBounds(1000, 470, 100, 39);

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/stockBarang_icon.png"))); // NOI18N
        pnlStokEksternal.add(jLabel35);
        jLabel35.setBounds(1110, 10, 110, 30);

        paneBarang.addTab("STOK KELUAR / MASUK", pnlStokEksternal);

        pnlStokInternal.setLayout(null);

        jLabel22.setText("KATA KUNCI");
        pnlStokInternal.add(jLabel22);
        jLabel22.setBounds(20, 10, 90, 25);

        txtInternalKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInternalKeywordFocusLost(evt);
            }
        });
        pnlStokInternal.add(txtInternalKeyword);
        txtInternalKeyword.setBounds(120, 10, 280, 25);

        pnlInternalDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInternalDetail.setLayout(null);

        txtInternalKode.setEditable(false);
        pnlInternalDetail.add(txtInternalKode);
        txtInternalKode.setBounds(130, 10, 430, 25);

        txtInternalNama.setEditable(false);
        pnlInternalDetail.add(txtInternalNama);
        txtInternalNama.setBounds(130, 40, 430, 25);

        txtInternalHarga.setEditable(false);
        txtInternalHarga.setToolTipText("");
        pnlInternalDetail.add(txtInternalHarga);
        txtInternalHarga.setBounds(130, 70, 430, 25);

        txtInternalTanggungan.setEditable(false);
        txtInternalTanggungan.setToolTipText("");
        pnlInternalDetail.add(txtInternalTanggungan);
        txtInternalTanggungan.setBounds(130, 100, 430, 25);

        txtInternalSatuan.setEditable(false);
        pnlInternalDetail.add(txtInternalSatuan);
        txtInternalSatuan.setBounds(130, 160, 430, 25);

        txtInternalJumlah.setEditable(false);
        pnlInternalDetail.add(txtInternalJumlah);
        txtInternalJumlah.setBounds(130, 130, 430, 25);

        jLabel23.setText("Kode");
        pnlInternalDetail.add(jLabel23);
        jLabel23.setBounds(20, 10, 90, 25);

        jLabel24.setText("Nama");
        pnlInternalDetail.add(jLabel24);
        jLabel24.setBounds(20, 40, 90, 25);

        jLabel25.setText("Harga");
        pnlInternalDetail.add(jLabel25);
        jLabel25.setBounds(20, 70, 90, 25);

        jLabel85.setText("Tanggungan");
        pnlInternalDetail.add(jLabel85);
        jLabel85.setBounds(20, 100, 90, 25);

        jLabel26.setText("Jumlah");
        pnlInternalDetail.add(jLabel26);
        jLabel26.setBounds(20, 130, 90, 25);

        jLabel27.setText("Satuan");
        pnlInternalDetail.add(jLabel27);
        jLabel27.setBounds(20, 160, 90, 25);

        pnlStokInternal.add(pnlInternalDetail);
        pnlInternalDetail.setBounds(20, 330, 590, 200);

        pnlInternalStok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInternalStok.setLayout(null);

        jLabel28.setText("Tanggal");
        pnlInternalStok.add(jLabel28);
        jLabel28.setBounds(20, 10, 90, 25);

        jLabel29.setText("Jam");
        pnlInternalStok.add(jLabel29);
        jLabel29.setBounds(20, 40, 90, 25);

        jLabel30.setText("Jumlah");
        pnlInternalStok.add(jLabel30);
        jLabel30.setBounds(20, 70, 90, 25);
        pnlInternalStok.add(txtInternalStokTanggal);
        txtInternalStokTanggal.setBounds(130, 10, 430, 25);
        pnlInternalStok.add(txtInternalStokJam);
        txtInternalStokJam.setBounds(130, 40, 430, 25);
        pnlInternalStok.add(txtInternalStokJumlah);
        txtInternalStokJumlah.setBounds(130, 70, 430, 25);

        jLabel3.setText("Unit");
        pnlInternalStok.add(jLabel3);
        jLabel3.setBounds(20, 100, 90, 25);

        txtInternalStokUnit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtInternalStokUnitMouseClicked(evt);
            }
        });
        pnlInternalStok.add(txtInternalStokUnit);
        txtInternalStokUnit.setBounds(130, 100, 430, 25);

        pnlStokInternal.add(pnlInternalStok);
        pnlInternalStok.setBounds(630, 330, 590, 140);

        tblInternal.setModel(new javax.swing.table.DefaultTableModel(
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
        tblInternal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInternalMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblInternal);

        pnlStokInternal.add(jScrollPane3);
        jScrollPane3.setBounds(20, 50, 1200, 270);

        btnInternalStokReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/BarangReset_Icon.png"))); // NOI18N
        btnInternalStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInternalStokResetActionPerformed(evt);
            }
        });
        pnlStokInternal.add(btnInternalStokReset);
        btnInternalStokReset.setBounds(1130, 490, 80, 39);

        btnInternalStokKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/barangKeluar_Icon.png"))); // NOI18N
        btnInternalStokKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInternalStokKeluarActionPerformed(evt);
            }
        });
        pnlStokInternal.add(btnInternalStokKeluar);
        btnInternalStokKeluar.setBounds(1030, 490, 90, 39);

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/stockBarang_icon.png"))); // NOI18N
        pnlStokInternal.add(jLabel36);
        jLabel36.setBounds(1110, 10, 110, 30);

        paneBarang.addTab("STOK KE UNIT", pnlStokInternal);

        pnlStokKembali.setLayout(null);

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
        pnlInternalStok1.add(txtKembaliStokTanggal);
        txtKembaliStokTanggal.setBounds(130, 10, 430, 25);
        pnlInternalStok1.add(txtKembaliStokJam);
        txtKembaliStokJam.setBounds(130, 40, 430, 25);
        pnlInternalStok1.add(txtKembaliStokJumlah);
        txtKembaliStokJumlah.setBounds(130, 70, 430, 25);

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
        jScrollPane4.setBounds(20, 80, 1200, 240);

        txtKembaliKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKembaliKeywordFocusLost(evt);
            }
        });
        pnlStokKembali.add(txtKembaliKeyword);
        txtKembaliKeyword.setBounds(540, 40, 280, 25);

        jLabel44.setText("KATA KUNCI");
        pnlStokKembali.add(jLabel44);
        jLabel44.setBounds(440, 40, 90, 25);

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/stockBarang_icon.png"))); // NOI18N
        pnlStokKembali.add(jLabel45);
        jLabel45.setBounds(1110, 10, 110, 30);

        jLabel1.setText("NO. PASIEN");
        pnlStokKembali.add(jLabel1);
        jLabel1.setBounds(20, 10, 90, 25);

        txtNomorPasienKembali.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomorPasienKembaliFocusLost(evt);
            }
        });
        pnlStokKembali.add(txtNomorPasienKembali);
        txtNomorPasienKembali.setBounds(120, 10, 280, 25);

        txtNamaPasienKembali.setEditable(false);
        pnlStokKembali.add(txtNamaPasienKembali);
        txtNamaPasienKembali.setBounds(540, 10, 280, 25);

        btnKembaliStokMasuk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/barangMasuk_Icon.png"))); // NOI18N
        btnKembaliStokMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliStokMasukActionPerformed(evt);
            }
        });
        pnlStokKembali.add(btnKembaliStokMasuk);
        btnKembaliStokMasuk.setBounds(1020, 490, 100, 40);

        btnKembaliStokReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/BarangReset_Icon.png"))); // NOI18N
        btnKembaliStokReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliStokResetActionPerformed(evt);
            }
        });
        pnlStokKembali.add(btnKembaliStokReset);
        btnKembaliStokReset.setBounds(1130, 490, 90, 39);

        jLabel2.setText("NAMA PASIEN");
        pnlStokKembali.add(jLabel2);
        jLabel2.setBounds(440, 10, 90, 25);

        txtNomorKembali.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomorKembaliFocusLost(evt);
            }
        });
        pnlStokKembali.add(txtNomorKembali);
        txtNomorKembali.setBounds(120, 40, 280, 25);

        jLabel4.setText("NO. KEMBALI");
        pnlStokKembali.add(jLabel4);
        jLabel4.setBounds(20, 40, 90, 25);

        paneBarang.addTab("STOK KEMBALI DARI PASIEN", pnlStokKembali);

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PENCARIAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("KATA KUNCI");
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
        jPanel2.add(txtObatKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));

        pnlObat.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 510, 60));

        btnTambahObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTambahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahObatActionPerformed(evt);
            }
        });
        pnlObat.add(btnTambahObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 20, 80, 40));

        btnClearObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnClearObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearObatActionPerformed(evt);
            }
        });
        pnlObat.add(btnClearObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 20, 80, 40));

        paneBarang.addTab("DATA OBAT", pnlObat);

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
        jPanel17.add(txtBhpKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));

        jLabel83.setText("KATA KUNCI");
        jPanel17.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        pnlBhp.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 510, 60));

        btnTambahBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTambahBhp.setBorder(null);
        btnTambahBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBhpActionPerformed(evt);
            }
        });
        pnlBhp.add(btnTambahBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 20, 80, 40));

        btnClearBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnClearBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearBhpActionPerformed(evt);
            }
        });
        pnlBhp.add(btnClearBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 20, 80, 40));

        paneBarang.addTab("DATA BARANG HABIS PAKAI", pnlBhp);

        getContentPane().add(paneBarang);
        paneBarang.setBounds(20, 180, 1240, 570);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel31.setText("LOGIN SEBAGAI:");
        jToolBar1.add(jLabel31);

        lblOperator.setText("jLabel32");
        jToolBar1.add(lblOperator);

        jLabel32.setText(" - ");
        jToolBar1.add(jLabel32);

        jLabel33.setText("UNIT: ");
        jToolBar1.add(jLabel33);

        lblUnit.setText("jLabel34");
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
        jToolBar1.setBounds(0, 770, 1280, 30);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Farmasi_Bg.jpg"))); // NOI18N
        getContentPane().add(background);
        background.setBounds(0, 0, 1280, 800);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paneBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paneBarangMouseClicked
        setDetailBarangEksternal(null);
        Integer index = paneBarang.getSelectedIndex();
        setDetailBarangInternal(null);
        setDetailBarangKembali((Barang) null);
        obatEventController.onCleanForm();
        bhpEventController.onCleanForm();
    }//GEN-LAST:event_paneBarangMouseClicked

    private void txtEksternalKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEksternalKeywordFocusLost
        reloadTableEksternal();
    }//GEN-LAST:event_txtEksternalKeywordFocusLost

    private void tblEksternalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEksternalMouseClicked
        Integer index = tblEksternal.getSelectedRow();
        
        BarangTableModel tableModel = (BarangTableModel)tblEksternal.getModel();
        Barang b = (Barang) tableModel.getBarang(index);
        
        setDetailBarangEksternal(b);
    }//GEN-LAST:event_tblEksternalMouseClicked

    private void btnEksternalStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokMasukActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtEksternalStokTanggal.getText();
        String jam = txtEksternalStokJam.getText();
        String jumlah = txtEksternalStokJumlah.getText();
        
        try {
            stokService.masuk(barang, new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
            setDetailBarangEksternal(null);
            reloadTableEksternal();
            
            JOptionPane.showMessageDialog(this, "Berhasil");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnEksternalStokMasukActionPerformed

    private void btnEksternalStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEksternalStokKeluarActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        String tanggal = txtEksternalStokTanggal.getText();
        String jam = txtEksternalStokJam.getText();
        String jumlah = txtEksternalStokJumlah.getText();
        
        try {
            stokService.keluar(barang, new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam));
            
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

    private void txtInternalKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInternalKeywordFocusLost
        reloadTableInternal();
    }//GEN-LAST:event_txtInternalKeywordFocusLost

    private void tblInternalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInternalMouseClicked
        Integer index = tblInternal.getSelectedRow();
        
        BarangTableModel tableModel = (BarangTableModel)tblInternal.getModel();
        Barang b = tableModel.getBarang(index);
        
        setDetailBarangInternal(b);
    }//GEN-LAST:event_tblInternalMouseClicked

    private void btnInternalStokKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInternalStokKeluarActionPerformed
        if (new Long(0).equals(barang.getId())) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih barang dari tabel");
            return;
        }
        
        if (unit == null) {
            JOptionPane.showMessageDialog(this, "Silahkan memilih unit");
            return;
        }
        
        String tanggal = txtInternalStokTanggal.getText();
        String jam = txtInternalStokJam.getText();
        String jumlah = txtInternalStokJumlah.getText();
        
        try {
            stokService.supply(barang, new Long(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam), unit); // TODO null ganti dengan unit.
            
            setDetailBarangInternal(null);
            reloadTableInternal();
            
            JOptionPane.showMessageDialog(this, "Berhasil");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnInternalStokKeluarActionPerformed

    private void btnInternalStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInternalStokResetActionPerformed
        setDetailBarangInternal(null);
    }//GEN-LAST:event_btnInternalStokResetActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameUtama().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

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

    private void txtKembaliKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKembaliKeywordFocusLost
        String keyword = txtKembaliKeyword.getText();
        reloadTableKembali(keyword);
    }//GEN-LAST:event_txtKembaliKeywordFocusLost

    private void btnKembaliStokMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliStokMasukActionPerformed
        String jumlah = txtKembaliStokJumlah.getText();
        String tanggal = txtKembaliStokTanggal.getText();
        String jam = txtKembaliStokJam.getText();
        String nomorKembali = txtNomorKembali.getText();
        
        try {
            stokService.kembali(barang, Long.valueOf(jumlah), DateUtil.getDate(tanggal), DateUtil.getTime(jam), pasien, nomorKembali);
            JOptionPane.showMessageDialog(this, "Berhasil");
            
            setDetailBarangKembali((Barang) null);
            
            String nomor = txtNomorKembali.getText();
            reloadTableKembaliByNomor(nomor);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnKembaliStokMasukActionPerformed

    private void btnKembaliStokResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliStokResetActionPerformed
        setDetailBarangKembali((Barang) null);
        
        pasien = null;
        txtNomorPasienKembali.setText(null);
        txtNamaPasienKembali.setText(null);
        txtKembaliKeyword.setText(null);
        txtNomorKembali.setText(StokKembali.createKode());
    }//GEN-LAST:event_btnKembaliStokResetActionPerformed

    private void txtInternalStokUnitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInternalStokUnitMouseClicked
        FrameCari frameCari = new FrameCari(this, Unit.class);
        frameCari.setVisible(true);
    }//GEN-LAST:event_txtInternalStokUnitMouseClicked

    private void txtNomorPasienKembaliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorPasienKembaliFocusLost
        String kode = txtNomorPasienKembali.getText();
        
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

    private void txtNomorKembaliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorKembaliFocusLost
        String nomor = txtNomorKembali.getText();
        reloadTableKembaliByNomor(nomor);
    }//GEN-LAST:event_txtNomorKembaliFocusLost

    private void tblObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblObatMouseClicked
        try {
            obatEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblObatMouseClicked

    private void txtObatKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtObatKeywordFocusLost
        try {
            obatEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            BarangTableModel tableModel = new BarangTableModel(null);
            tblObat.setModel(tableModel);
        }
    }//GEN-LAST:event_txtObatKeywordFocusLost

    private void txtObatKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObatKeywordKeyPressed
        int i = evt.getKeyCode();
        if (i == 10) {
            txtObatKode.requestFocus();
        }
    }//GEN-LAST:event_txtObatKeywordKeyPressed

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

    private void btnClearObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearObatActionPerformed
        obatEventController.onCleanForm();
    }//GEN-LAST:event_btnClearObatActionPerformed

    private void tblBhpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBhpMouseClicked
        try {
            bhpEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblBhpMouseClicked

    private void txtBhpKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBhpKeywordFocusLost
        try {
            bhpEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            BarangTableModel tableModel = new BarangTableModel(null);
            tblBhp.setModel(tableModel);
        }
    }//GEN-LAST:event_txtBhpKeywordFocusLost

    private void txtBhpKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBhpKeywordKeyPressed
        int i = evt.getKeyCode();
        if (i == 10) {
            txtBhpKode.requestFocus();
        }
    }//GEN-LAST:event_txtBhpKeywordKeyPressed

    private void btnTambahBhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBhpActionPerformed
        try {
            bhpEventController.onSave();
            bhpEventController.onCleanForm();
            bhpEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTambahBhpActionPerformed

    private void btnClearBhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearBhpActionPerformed
        bhpEventController.onCleanForm();
    }//GEN-LAST:event_btnClearBhpActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JButton btnClearBhp;
    private javax.swing.JButton btnClearObat;
    private javax.swing.JButton btnEksternalStokKeluar;
    private javax.swing.JButton btnEksternalStokMasuk;
    private javax.swing.JButton btnEksternalStokReset;
    private javax.swing.JButton btnInternalStokKeluar;
    private javax.swing.JButton btnInternalStokReset;
    private javax.swing.JButton btnKembaliStokMasuk;
    private javax.swing.JButton btnKembaliStokReset;
    private javax.swing.JButton btnLogout;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
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
    private javax.swing.JLabel jLabel85;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblOperator;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JTabbedPane paneBarang;
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlEksternalDetail;
    private javax.swing.JPanel pnlEksternalStok;
    private javax.swing.JPanel pnlInternalDetail;
    private javax.swing.JPanel pnlInternalDetail1;
    private javax.swing.JPanel pnlInternalStok;
    private javax.swing.JPanel pnlInternalStok1;
    private javax.swing.JPanel pnlObat;
    private javax.swing.JPanel pnlStokEksternal;
    private javax.swing.JPanel pnlStokInternal;
    private javax.swing.JPanel pnlStokKembali;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblEksternal;
    private javax.swing.JTable tblInternal;
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
    private javax.swing.JTextField txtEksternalStokTanggal;
    private javax.swing.JTextField txtEksternalTanggungan;
    private javax.swing.JTextField txtInternalHarga;
    private javax.swing.JTextField txtInternalJumlah;
    private javax.swing.JTextField txtInternalKeyword;
    private javax.swing.JTextField txtInternalKode;
    private javax.swing.JTextField txtInternalNama;
    private javax.swing.JTextField txtInternalSatuan;
    private javax.swing.JTextField txtInternalStokJam;
    private javax.swing.JTextField txtInternalStokJumlah;
    private javax.swing.JTextField txtInternalStokTanggal;
    private javax.swing.JTextField txtInternalStokUnit;
    private javax.swing.JTextField txtInternalTanggungan;
    private javax.swing.JTextField txtKembaliHarga;
    private javax.swing.JTextField txtKembaliJumlah;
    private javax.swing.JTextField txtKembaliKeyword;
    private javax.swing.JTextField txtKembaliKode;
    private javax.swing.JTextField txtKembaliNama;
    private javax.swing.JTextField txtKembaliSatuan;
    private javax.swing.JTextField txtKembaliStokJam;
    private javax.swing.JTextField txtKembaliStokJumlah;
    private javax.swing.JTextField txtKembaliStokTanggal;
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
