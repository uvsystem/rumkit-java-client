package com.dbsys.rs.client;

import com.dbsys.rs.client.tableModel.ApotekerTableModel;
import com.dbsys.rs.client.tableModel.DokterTableModel;
import com.dbsys.rs.client.tableModel.OperatorTableModel;
import com.dbsys.rs.client.tableModel.PekerjaTableModel;
import com.dbsys.rs.client.tableModel.PerawatTableModel;
import com.dbsys.rs.client.tableModel.UnitTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.ApotekerService;
import com.dbsys.rs.connector.service.DokterService;
import com.dbsys.rs.connector.service.OperatorService;
import com.dbsys.rs.connector.service.PekerjaService;
import com.dbsys.rs.connector.service.PerawatService;
import com.dbsys.rs.connector.service.UnitService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.Apoteker;
import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.Operator;
import com.dbsys.rs.lib.entity.Operator.Role;
import com.dbsys.rs.lib.entity.Pekerja;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.dbsys.rs.lib.entity.Perawat;
import com.dbsys.rs.lib.entity.Unit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 */
public class FrameAdmin extends javax.swing.JFrame {
    private final UnitEventController unitEventController;
    private final OperatorEventController operatorEventController;
    private final DokterEventController dokterEventController;
    private final PerawatEventController perawatEventController;
    private final ApotekerEventController apotekerEventController;
    private final PekerjaEventController pekerjaEventController;

    /**
     * Creates new form admin
     */
    public FrameAdmin() {
        initComponents();
        
        pnl_tindakan.setVisible(false);
        pnl_unit.setVisible(false);
        pnl_barang.setVisible(false);
        pnl_rekam.setVisible(false);
        pnl_pegawai.setVisible(false);
        pnl_op.setVisible(false);

        unitEventController = new UnitEventController();
        operatorEventController = new OperatorEventController();
        dokterEventController = new DokterEventController();
        perawatEventController = new PerawatEventController();
        apotekerEventController = new ApotekerEventController();
        pekerjaEventController = new PekerjaEventController();
        
        String nama = TokenHolder.getNamaOperator();
        lbl_status.setText(nama);
    }

    public void setUnitForOperator(Unit unit){
        unitEventController.setModel(unit);
        txt_admin_operator_unit.setText(unit.getNama());
    }
    
    private class UnitEventController implements EventController<Unit> {
        private final UnitService unitservice = UnitService.getInstance(host);
        private Unit model;

        @Override
        public Unit getModel() {
            return model;
        }

        @Override
        public void setModel(Unit t) {
            this.model = t;
        }
        
        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Unit();

            String tipe = (String)cb_unit_tipe.getSelectedItem();
            model.setTipe(Unit.Type.valueOf(tipe));
            model.setBobot(Float.valueOf(txt_unit_bobot.getText()));
            model.setNama(txt_unit_nama.getText());

            unitservice.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() {
            int row = tbl_unit.getSelectedRow();

            UnitTableModel tableModel = (UnitTableModel)tbl_unit.getModel();
            model = tableModel.getUnit(row);

            txt_unit_nama.setText(model.getNama());
            txt_unit_bobot.setText(model.getBobot().toString());
            cb_unit_tipe.setSelectedItem(model.getTipe().toString());
        }

        @Override
        public void onCleanForm() {
            txt_unit_nama.setText("");
            txt_unit_bobot.setText("");
            cb_unit_tipe.setSelectedIndex(0);
            
            model = null;
        }

        @Override
        public void onLoad() throws ServiceException {
            pnl_tindakan.setVisible(false);
            pnl_unit.setVisible(true);
            pnl_barang.setVisible(false);
            pnl_rekam.setVisible(false);
            pnl_pegawai.setVisible(false);
            pnl_op.setVisible(false);

            List<Unit> listUnit = unitservice.getAll();
            UnitTableModel tableModel = new UnitTableModel(listUnit);
            tbl_unit.setModel(tableModel);
        }

        @Override
        public void onDelete() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

    private class OperatorEventController implements EventController<Operator> {
        private final OperatorService operatorService = OperatorService.getInstance(host);
        private Operator model;

        @Override
        public Operator getModel() {
            return model;
        }

        @Override
        public void setModel(Operator t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Operator();

            String role = (String)cb_admin_operator_role.getSelectedItem();
            model.setRole(Role.valueOf(role));
            model.setNama(txt_op_nama.getText());
            model.setUsername(txt_op_uname.getText());
            model.setPassword(txt_op_pass.getText());
            model.setUnit(unitEventController.getModel());

            operatorService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() {
            int row = tbl_op.getSelectedRow();

            OperatorTableModel tableModel = (OperatorTableModel)tbl_op.getModel();
            model = tableModel.getOperator(row);

            txt_op_nama.setText(model.getNama());
            txt_op_uname.setText(model.getUsername());
            txt_op_pass.setText(model.getPassword());
            txt_admin_operator_unit.setText(model.getUnit().getNama());
            cb_admin_operator_role.setSelectedItem(model.getRole().toString());
        }

        @Override
        public void onCleanForm() {
            txt_op_nama.setText("");
            txt_op_uname.setText("");
            txt_op_pass.setText("");
            txt_admin_operator_unit.setText("");
            cb_admin_operator_role.setSelectedIndex(0);

            model = null;
        }

        @Override
        public void onLoad() throws ServiceException {
            pnl_tindakan.setVisible(false);
            pnl_unit.setVisible(false);
            pnl_barang.setVisible(false);
            pnl_rekam.setVisible(false);
            pnl_pegawai.setVisible(false);
            pnl_op.setVisible(true);

            List<Operator> listOperator = operatorService.getAll();
            OperatorTableModel tableModel = new OperatorTableModel(listOperator);
            tbl_op.setModel(tableModel);
        }

        @Override
        public void onDelete() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class DokterEventController implements EventController<Dokter> {
        private final DokterService dokterService = DokterService.getInstance(host);
        private Dokter model;

        @Override
        public Dokter getModel() {
            return model;
        }

        @Override
        public void setModel(Dokter t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Dokter();

            model.setNip(txt_dokter_nip.getText());
            model.setNama(txt_dokter_nama.getText());
            model.setNik(txt_dokter_nik.getText());
            String tglLahir = txt_dokter_lahir.getText();
            model.setTanggalLahir(DateUtil.getDate(tglLahir));
            model.setTelepon(txt_dokter_telepon.getText());
            model.setAgama(txt_dokter_agama.getText());
            model.setDarah(txt_dokter_darah.getText());
            String kelamin = (String)cb_dokter_kelamin.getSelectedItem();
            model.setKelamin(Kelamin.valueOf(kelamin));
            model.setSpesialisasi(null);

            dokterService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() {
            int row = tbl_dokter.getSelectedRow();

            DokterTableModel tableModel = (DokterTableModel)tbl_dokter.getModel();
            model = tableModel.getDokter(row);

            txt_dokter_kode.setText(model.getKode());
            txt_dokter_nip.setText(model.getNip());
            txt_dokter_nama.setText(model.getNama());
            txt_dokter_nik.setText(model.getNik());
            txt_dokter_lahir.setText(model.getTanggalLahir().toString());
            txt_dokter_telepon.setText(model.getTelepon());
            txt_dokter_agama.setText(model.getAgama());
            txt_dokter_darah.setText(model.getDarah());
            cb_dokter_kelamin.setSelectedItem(model.getKelamin().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txt_dokter_kode.setText("");
            txt_dokter_nip.setText("");
            txt_dokter_nama.setText("");
            txt_dokter_nik.setText("");
            txt_dokter_lahir.setText("");
            txt_dokter_telepon.setText("");
            txt_dokter_agama.setText("");
            txt_dokter_darah.setText("");
            cb_dokter_kelamin.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            pnl_tindakan.setVisible(false);
            pnl_unit.setVisible(false);
            pnl_barang.setVisible(false);
            pnl_rekam.setVisible(false);
            pnl_pegawai.setVisible(true);
            pnl_op.setVisible(false);

            List<Dokter> listDokter = dokterService.getAll();
            DokterTableModel tableModel = new DokterTableModel(listDokter);
            tbl_dokter.setModel(tableModel);
        }

        @Override
        public void onDelete() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class PerawatEventController implements EventController<Perawat> {
        private final PerawatService perawatService = PerawatService.getInstance(host);
        private Perawat model;

        @Override
        public Perawat getModel() {
            return model;
        }

        @Override
        public void setModel(Perawat t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Perawat();

            String tglLahir = txt_perawat_lahir.getText();
            String kelamin = (String)cb_perawat_kelamin.getSelectedItem();

            model.setNip(txt_perawat_nip.getText());
            model.setNama(txt_perawat_nama.getText());
            model.setNik(txt_perawat_nik.getText());
            model.setTanggalLahir(DateUtil.getDate(tglLahir));
            model.setTelepon(txt_perawat_telepon.getText());
            model.setAgama(txt_perawat_agama.getText());
            model.setDarah(txt_perawat_darah.getText());
            model.setKelamin(Kelamin.valueOf(kelamin));

            perawatService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() {
            int row = tbl_perawat.getSelectedRow();

            PerawatTableModel tableModel = (PerawatTableModel)tbl_perawat.getModel();
            model = tableModel.getPerawat(row);

            txt_perawat_kode.setText(model.getKode());
            txt_perawat_nip.setText(model.getNip());
            txt_perawat_nama.setText(model.getNama());
            txt_perawat_nik.setText(model.getNik());
            txt_perawat_lahir.setText(model.getTanggalLahir().toString());
            txt_perawat_telepon.setText(model.getTelepon());
            txt_perawat_agama.setText(model.getAgama());
            txt_perawat_darah.setText(model.getDarah());
            cb_perawat_kelamin.setSelectedItem(model.getKelamin().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txt_perawat_kode.setText("");
            txt_perawat_nip.setText("");
            txt_perawat_nama.setText("");
            txt_perawat_nik.setText("");
            txt_perawat_lahir.setText("");
            txt_perawat_telepon.setText("");
            txt_perawat_agama.setText("");
            txt_perawat_darah.setText("");
            cb_perawat_kelamin.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            List<Perawat> listPerawat = perawatService.getAll();
            PerawatTableModel tableModel = new PerawatTableModel(listPerawat);
            tbl_perawat.setModel(tableModel);
        }

        @Override
        public void onDelete() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class ApotekerEventController implements EventController<Apoteker> {
        private final ApotekerService apotekerService = ApotekerService.getInstance(host);
        private Apoteker model;
        
        @Override
        public Apoteker getModel() {
            return model;
        }

        @Override
        public void setModel(Apoteker t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Apoteker();

            String tglLahir = txtApotekerLahir.getText();
            String kelamin = (String)cbApotekerKelamin.getSelectedItem();

            model.setNip(txtApotekerNip.getText());
            model.setNama(txtApotekerNama.getText());
            model.setNik(txtApotekerNik.getText());
            model.setTanggalLahir(DateUtil.getDate(tglLahir));
            model.setTelepon(txtApotekerTelepon.getText());
            model.setAgama(txtApotekerAgama.getText());
            model.setDarah(txtApotekerDarah.getText());
            model.setKelamin(Kelamin.valueOf(kelamin));

            apotekerService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() {
            int row = tbl_perawat.getSelectedRow();

            ApotekerTableModel tableModel = (ApotekerTableModel)tbl_apoteker.getModel();
            model = tableModel.getApoteker(row);

            txtApotekerKode.setText(model.getKode());
            txtApotekerNip.setText(model.getNip());
            txtApotekerNama.setText(model.getNama());
            txtApotekerNik.setText(model.getNik());
            txtApotekerLahir.setText(model.getTanggalLahir().toString());
            txtApotekerTelepon.setText(model.getTelepon());
            txtApotekerAgama.setText(model.getAgama());
            txtApotekerDarah.setText(model.getDarah());
            cbApotekerKelamin.setSelectedItem(model.getKelamin().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txtApotekerKode.setText("");
            txtApotekerNip.setText("");
            txtApotekerNama.setText("");
            txtApotekerNik.setText("");
            txtApotekerLahir.setText("");
            txtApotekerTelepon.setText("");
            txtApotekerAgama.setText("");
            txtApotekerDarah.setText("");
            cbApotekerKelamin.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            List<Apoteker> listApoteker = apotekerService.getAll();
            ApotekerTableModel tableModel = new ApotekerTableModel(listApoteker );
            tbl_apoteker.setModel(tableModel);
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class PekerjaEventController implements EventController<Pekerja> {
        private final PekerjaService pekerjaService = PekerjaService.getInstance(host);
        private Pekerja model;

        @Override
        public Pekerja getModel() {
            return model;
        }

        @Override
        public void setModel(Pekerja t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Pekerja();

            String tglLahir = txtPekerjaLahir.getText();
            String kelamin = (String)cbPekerjaKelamin.getSelectedItem();

            model.setNip(txtPekerjaNip.getText());
            model.setNama(txtPekerjaNama.getText());
            model.setNik(txtPekerjaNik.getText());
            model.setTanggalLahir(DateUtil.getDate(tglLahir));
            model.setTelepon(txtPekerjaTelepon.getText());
            model.setAgama(txtPekerjaAgama.getText());
            model.setDarah(txtPekerjaDarah.getText());
            model.setKelamin(Kelamin.valueOf(kelamin));

            pekerjaService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tbl_perawat.getSelectedRow();

            PekerjaTableModel tableModel = (PekerjaTableModel)tbl_adm.getModel();
            model = tableModel.getPekerja(row);

            txtPekerjaKode.setText(model.getKode());
            txtPekerjaNip.setText(model.getNip());
            txtPekerjaNama.setText(model.getNama());
            txtPekerjaNik.setText(model.getNik());
            txtPekerjaLahir.setText(model.getTanggalLahir().toString());
            txtPekerjaTelepon.setText(model.getTelepon());
            txtPekerjaAgama.setText(model.getAgama());
            txtPekerjaDarah.setText(model.getDarah());
            cbPekerjaKelamin.setSelectedItem(model.getKelamin().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txtPekerjaKode.setText("");
            txtPekerjaNip.setText("");
            txtPekerjaNama.setText("");
            txtPekerjaNik.setText("");
            txtPekerjaLahir.setText("");
            txtPekerjaTelepon.setText("");
            txtPekerjaAgama.setText("");
            txtPekerjaDarah.setText("");
            cbPekerjaKelamin.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            List<Pekerja> listPekerja = pekerjaService.getAll();
            PekerjaTableModel tableModel = new PekerjaTableModel(listPekerja);
            tbl_adm.setModel(tableModel);
        }

        @Override
        public void onDelete() throws ServiceException {
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

        pnl_pegawai = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        tab_pane = new javax.swing.JTabbedPane();
        tab_dokter = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_dokter = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        btn_simpan_dokter = new javax.swing.JButton();
        btn_clear_dokter = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        txt_dokter_nip = new javax.swing.JTextField();
        txt_dokter_nama = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        txt_dokter_kode = new javax.swing.JTextField();
        txt_dokter_nik = new javax.swing.JTextField();
        txt_dokter_lahir = new javax.swing.JTextField();
        txt_dokter_telepon = new javax.swing.JTextField();
        cb_dokter_kelamin = new javax.swing.JComboBox();
        txt_dokter_darah = new javax.swing.JTextField();
        txt_dokter_agama = new javax.swing.JTextField();
        tab_perawat = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbl_perawat = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        txt_perawat_kode = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txt_perawat_nip = new javax.swing.JTextField();
        txt_perawat_nama = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        txt_perawat_lahir = new javax.swing.JTextField();
        txt_perawat_nik = new javax.swing.JTextField();
        cb_perawat_kelamin = new javax.swing.JComboBox();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        txt_perawat_darah = new javax.swing.JTextField();
        txt_perawat_agama = new javax.swing.JTextField();
        txt_perawat_telepon = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        btn_tambah_perawat = new javax.swing.JButton();
        btn_clear_perawat = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        tab_apoteker = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbl_apoteker = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        btn_tambah_apoteker = new javax.swing.JButton();
        btn_clear_apoteker = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        txtApotekerKode = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txtApotekerNip = new javax.swing.JTextField();
        txtApotekerNama = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        txtApotekerLahir = new javax.swing.JTextField();
        txtApotekerNik = new javax.swing.JTextField();
        cbApotekerKelamin = new javax.swing.JComboBox();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        txtApotekerDarah = new javax.swing.JTextField();
        txtApotekerAgama = new javax.swing.JTextField();
        txtApotekerTelepon = new javax.swing.JTextField();
        tab_administrasi = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbl_adm = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        btn_tambah_adm = new javax.swing.JButton();
        btn_clear_adm = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        txtPekerjaKode = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        txtPekerjaNip = new javax.swing.JTextField();
        txtPekerjaNama = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        txtPekerjaLahir = new javax.swing.JTextField();
        txtPekerjaNik = new javax.swing.JTextField();
        cbPekerjaKelamin = new javax.swing.JComboBox();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txtPekerjaDarah = new javax.swing.JTextField();
        txtPekerjaAgama = new javax.swing.JTextField();
        txtPekerjaTelepon = new javax.swing.JTextField();
        pnl_unit = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_unit = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        txt_unit_nama = new javax.swing.JTextField();
        txt_unit_bobot = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        cb_unit_tipe = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        btn_simpan_unit = new javax.swing.JButton();
        btn_clear_unit = new javax.swing.JButton();
        pnl_op = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbl_op = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        txt_op_nama = new javax.swing.JTextField();
        txt_op_uname = new javax.swing.JTextField();
        txt_op_pass = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txt_admin_operator_unit = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cb_admin_operator_role = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        btn_tambah_op = new javax.swing.JButton();
        btn_clear_op = new javax.swing.JButton();
        pnl_tindakan = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_admin_tindakan = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        txt_admin_tindakan_id = new javax.swing.JTextField();
        txt_admin_tindakan_kode = new javax.swing.JTextField();
        txt_admin_tindakan_nama = new javax.swing.JTextField();
        txt_admin_tindakan_tarif = new javax.swing.JTextField();
        txt_admin_tindakan_keterangan = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txt_admin_tindakan_kategori = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        btnTambahTindakan = new javax.swing.JButton();
        btnResetTindakan = new javax.swing.JButton();
        pnl_barang = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_barang = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_barang_id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_barang_kode = new javax.swing.JTextField();
        txt_barang_nama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_barang_jumlah = new javax.swing.JTextField();
        cb_barang_satuan = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_barang_harga = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_barang_ket = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btn_tambah_barang = new javax.swing.JButton();
        btn_edit_barang = new javax.swing.JButton();
        btn_clear_barang = new javax.swing.JButton();
        pnl_rekam = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_rekam_id = new javax.swing.JTextField();
        txt_rekam_kode = new javax.swing.JTextField();
        txt_rekam_nik = new javax.swing.JTextField();
        txt_rekam_nama = new javax.swing.JTextField();
        cb_rekam_kelamin = new javax.swing.JComboBox();
        cb_rekam_gol = new javax.swing.JComboBox();
        cb_rekam_agama = new javax.swing.JComboBox();
        txt_rekam_telp = new javax.swing.JTextField();
        txt_rekam_tl = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_rekam = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btn_rekam_edit = new javax.swing.JButton();
        btn_rekam_tambah = new javax.swing.JButton();
        pnl_menu = new javax.swing.JPanel();
        btnBarang = new javax.swing.JButton();
        btn_unit = new javax.swing.JButton();
        btn_rekam = new javax.swing.JButton();
        btn_pegawai = new javax.swing.JButton();
        btn_op = new javax.swing.JButton();
        btn_tindakan = new javax.swing.JButton();
        lbl_status = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(12345);
        setName("ADMIN"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_pegawai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_pegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("PEGAWAI");
        jLabel25.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnl_pegawai.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, 20));

        tab_pane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_paneMouseClicked(evt);
            }
        });

        tab_dokter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_dokter.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_dokter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_dokterMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tbl_dokter);

        tab_dokter.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 680, 250));

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_simpan_dokter.setText("SIMPAN");
        btn_simpan_dokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpan_dokterActionPerformed(evt);
            }
        });
        jPanel14.add(btn_simpan_dokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 80, 40));

        btn_clear_dokter.setText("X Field");
        btn_clear_dokter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_clear_dokterMouseClicked(evt);
            }
        });
        jPanel14.add(btn_clear_dokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        tab_dokter.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 680, 60));

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("DOKTER");
        tab_dokter.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 690, -1));

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel21.setLayout(null);

        jLabel47.setText("NIP");
        jPanel21.add(jLabel47);
        jLabel47.setBounds(12, 16, 17, 14);
        jPanel21.add(txt_dokter_nip);
        txt_dokter_nip.setBounds(47, 13, 132, 20);
        jPanel21.add(txt_dokter_nama);
        txt_dokter_nama.setBounds(51, 39, 132, 20);

        jLabel48.setText("NAMA");
        jPanel21.add(jLabel48);
        jLabel48.setBounds(12, 42, 29, 14);

        jLabel53.setText("KODE");
        jPanel21.add(jLabel53);
        jLabel53.setBounds(12, 68, 27, 14);

        jLabel54.setText("NIK");
        jPanel21.add(jLabel54);
        jLabel54.setBounds(250, 20, 17, 10);

        jLabel55.setText("KELAMIN");
        jPanel21.add(jLabel55);
        jLabel55.setBounds(250, 40, 43, 10);

        jLabel56.setText("TGL LAHIR");
        jPanel21.add(jLabel56);
        jLabel56.setBounds(250, 70, 51, 10);

        jLabel57.setText("DARAH");
        jPanel21.add(jLabel57);
        jLabel57.setBounds(500, 10, 35, 14);

        jLabel58.setText("AGAMA");
        jPanel21.add(jLabel58);
        jLabel58.setBounds(500, 40, 36, 14);

        jLabel59.setText("TELEPON");
        jPanel21.add(jLabel59);
        jLabel59.setBounds(500, 70, 44, 14);
        jPanel21.add(txt_dokter_kode);
        txt_dokter_kode.setBounds(49, 65, 176, 20);
        jPanel21.add(txt_dokter_nik);
        txt_dokter_nik.setBounds(310, 10, 150, 20);
        jPanel21.add(txt_dokter_lahir);
        txt_dokter_lahir.setBounds(310, 70, 92, 20);
        jPanel21.add(txt_dokter_telepon);
        txt_dokter_telepon.setBounds(560, 70, 90, 20);

        cb_dokter_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel21.add(cb_dokter_kelamin);
        cb_dokter_kelamin.setBounds(310, 40, 110, 20);
        jPanel21.add(txt_dokter_darah);
        txt_dokter_darah.setBounds(560, 10, 90, 20);
        jPanel21.add(txt_dokter_agama);
        txt_dokter_agama.setBounds(560, 40, 90, 20);

        tab_dokter.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 680, 110));

        tab_pane.addTab("DOKTER", tab_dokter);

        tab_perawat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_perawat.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_perawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_perawatMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tbl_perawat);

        tab_perawat.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 680, 300));

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel33.setText("KODE");

        jLabel34.setText("NIP");

        jLabel35.setText("NAMA");

        jLabel46.setText("NIK");

        jLabel60.setText("KELAMIN");

        jLabel61.setText("TGL. LAHIR");

        cb_perawat_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));

        jLabel62.setText("DARAH");

        jLabel63.setText("AGAMA");

        jLabel64.setText("TELEPON");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(18, 18, 18)
                        .addComponent(txt_perawat_nip, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel33))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_perawat_nama, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(txt_perawat_kode, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel61)
                            .addComponent(jLabel60))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_perawat_lahir, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(txt_perawat_nik, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(cb_perawat_kelamin, 0, 120, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addGap(18, 18, 18)
                        .addComponent(txt_perawat_darah, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel63)
                            .addComponent(jLabel64))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_perawat_telepon, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(txt_perawat_agama, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62)
                        .addComponent(txt_perawat_darah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(txt_perawat_nip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(txt_perawat_nik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_perawat_kode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)
                            .addComponent(jLabel60)
                            .addComponent(cb_perawat_kelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel63)
                            .addComponent(txt_perawat_agama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_perawat_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(jLabel61)
                            .addComponent(txt_perawat_lahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel64)
                            .addComponent(txt_perawat_telepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        tab_perawat.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 570, 110));

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_perawat.setText("+ DATA");
        btn_tambah_perawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_perawatActionPerformed(evt);
            }
        });
        jPanel16.add(btn_tambah_perawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        btn_clear_perawat.setText("X Field");
        btn_clear_perawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_perawatActionPerformed(evt);
            }
        });
        jPanel16.add(btn_clear_perawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 80, 40));

        tab_perawat.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 340, 100, 110));

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("PERAWAT");
        tab_perawat.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 690, -1));

        tab_pane.addTab("PERAWAT", tab_perawat);

        tab_apoteker.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_apoteker.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_apoteker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_apotekerMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tbl_apoteker);

        tab_apoteker.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 680, 300));

        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_apoteker.setText("+ DATA");
        btn_tambah_apoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_apotekerActionPerformed(evt);
            }
        });
        jPanel18.add(btn_tambah_apoteker, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        btn_clear_apoteker.setText("X Field");
        btn_clear_apoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_apotekerActionPerformed(evt);
            }
        });
        jPanel18.add(btn_clear_apoteker, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 80, 40));

        tab_apoteker.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 340, 100, 110));

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("APOTEKER");
        tab_apoteker.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 690, -1));

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel65.setText("KODE");

        jLabel66.setText("NIP");

        jLabel67.setText("NAMA");

        jLabel68.setText("NIK");

        jLabel69.setText("KELAMIN");

        jLabel70.setText("TGL. LAHIR");

        cbApotekerKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));

        jLabel71.setText("DARAH");

        jLabel72.setText("AGAMA");

        jLabel73.setText("TELEPON");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel66)
                        .addGap(18, 18, 18)
                        .addComponent(txtApotekerNip, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel67)
                            .addComponent(jLabel65))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApotekerNama, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(txtApotekerKode, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel70)
                            .addComponent(jLabel69))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApotekerLahir, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(txtApotekerNik, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(cbApotekerKelamin, 0, 120, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addGap(18, 18, 18)
                        .addComponent(txtApotekerDarah, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel72)
                            .addComponent(jLabel73))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtApotekerTelepon, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(txtApotekerAgama, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel71)
                        .addComponent(txtApotekerDarah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel68)
                            .addComponent(txtApotekerNip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel66)
                            .addComponent(txtApotekerNik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApotekerKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel65)
                            .addComponent(jLabel69)
                            .addComponent(cbApotekerKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel72)
                            .addComponent(txtApotekerAgama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApotekerNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel67)
                            .addComponent(jLabel70)
                            .addComponent(txtApotekerLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel73)
                            .addComponent(txtApotekerTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        tab_apoteker.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 570, 110));

        tab_pane.addTab("APOTEKER", tab_apoteker);

        tab_administrasi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_adm.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_adm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_admMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tbl_adm);

        tab_administrasi.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 680, 300));

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_adm.setText("+ DATA");
        btn_tambah_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_admActionPerformed(evt);
            }
        });
        jPanel20.add(btn_tambah_adm, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        btn_clear_adm.setText("X Field");
        btn_clear_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_admActionPerformed(evt);
            }
        });
        jPanel20.add(btn_clear_adm, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 80, 40));

        tab_administrasi.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 340, 100, 110));

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("ADMINISTRASI");
        tab_administrasi.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 690, -1));

        jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel74.setText("KODE");

        jLabel75.setText("NIP");

        jLabel76.setText("NAMA");

        jLabel77.setText("NIK");

        jLabel78.setText("KELAMIN");

        jLabel79.setText("TGL. LAHIR");

        cbPekerjaKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));

        jLabel80.setText("DARAH");

        jLabel81.setText("AGAMA");

        jLabel82.setText("TELEPON");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addGap(18, 18, 18)
                        .addComponent(txtPekerjaNip, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel76)
                            .addComponent(jLabel74))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPekerjaNama, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(txtPekerjaKode, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel77)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel79)
                            .addComponent(jLabel78))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPekerjaLahir, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(txtPekerjaNik, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(cbPekerjaKelamin, 0, 120, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel80)
                        .addGap(18, 18, 18)
                        .addComponent(txtPekerjaDarah, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel81)
                            .addComponent(jLabel82))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPekerjaTelepon, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(txtPekerjaAgama, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel80)
                        .addComponent(txtPekerjaDarah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel77)
                            .addComponent(txtPekerjaNip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel75)
                            .addComponent(txtPekerjaNik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPekerjaKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel74)
                            .addComponent(jLabel78)
                            .addComponent(cbPekerjaKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel81)
                            .addComponent(txtPekerjaAgama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPekerjaNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel76)
                            .addComponent(jLabel79)
                            .addComponent(txtPekerjaLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel82)
                            .addComponent(txtPekerjaTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        tab_administrasi.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 570, 110));

        tab_pane.addTab("ADMINSTRASI", tab_administrasi);

        pnl_pegawai.add(tab_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 700, 490));

        getContentPane().add(pnl_pegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 720, 540));

        pnl_unit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_unit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("UNIT");
        pnl_unit.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, -1));

        tbl_unit.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_unit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_unitMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_unit);

        pnl_unit.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 700, 270));

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel29.setText("NAMA");

        jLabel30.setText("BOBOT");

        jLabel52.setText("TIPE");

        cb_unit_tipe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "LOKET_PENDAFTARAN", "LOKET_PEMBAYARAN", "POLIKLINIK", "RUANG_PERAWATAN", "FARMASI", "UNIT_LAIN" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel29)
                .addGap(11, 11, 11)
                .addComponent(txt_unit_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jLabel30)
                .addGap(6, 6, 6)
                .addComponent(txt_unit_bobot, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel52)
                .addGap(8, 8, 8)
                .addComponent(cb_unit_tipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel29))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_unit_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel30))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_unit_bobot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel52))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cb_unit_tipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnl_unit.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 700, 80));

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_simpan_unit.setText("SIMPAN");
        btn_simpan_unit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_simpan_unit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpan_unitActionPerformed(evt);
            }
        });
        jPanel9.add(btn_simpan_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        btn_clear_unit.setText("X FIELDS");
        btn_clear_unit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_unitActionPerformed(evt);
            }
        });
        jPanel9.add(btn_clear_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 80, 40));

        pnl_unit.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 700, 60));

        getContentPane().add(pnl_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnl_op.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_op.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("OPERATOR");
        pnl_op.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, -1));

        tbl_op.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_op.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_opMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tbl_op);

        pnl_op.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 700, 270));

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(txt_op_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 140, -1));
        jPanel10.add(txt_op_uname, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 100, -1));
        jPanel10.add(txt_op_pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 160, -1));

        jLabel49.setText("NAMA");
        jPanel10.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel50.setText("USERNAME");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 15, -1, -1));

        jLabel51.setText("PASSWORD");
        jPanel10.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, -1, -1));

        txt_admin_operator_unit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_admin_operator_unitMouseClicked(evt);
            }
        });
        jPanel10.add(txt_admin_operator_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 140, -1));

        jLabel27.setText("UNIT");
        jPanel10.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel31.setText("ROLE");
        jPanel10.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, -1));

        cb_admin_operator_role.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "ADMIN", "OPERATOR" }));
        jPanel10.add(cb_admin_operator_role, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 120, 20));

        pnl_op.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 700, 110));

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_op.setText("SIMPAN");
        btn_tambah_op.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_tambah_op.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_opActionPerformed(evt);
            }
        });
        jPanel11.add(btn_tambah_op, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        btn_clear_op.setText("X FIELDS");
        btn_clear_op.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_opActionPerformed(evt);
            }
        });
        jPanel11.add(btn_clear_op, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 80, 40));

        pnl_op.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 700, 60));

        getContentPane().add(pnl_op, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnl_tindakan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_tindakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("TINDAKAN");
        pnl_tindakan.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, -1));

        tbl_admin_tindakan.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tbl_admin_tindakan);

        pnl_tindakan.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 700, 270));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(txt_admin_tindakan_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 50, -1));
        jPanel6.add(txt_admin_tindakan_kode, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 50, -1));
        jPanel6.add(txt_admin_tindakan_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 140, -1));
        jPanel6.add(txt_admin_tindakan_tarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 100, -1));
        jPanel6.add(txt_admin_tindakan_keterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 160, -1));

        jLabel20.setText("ID");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

        jLabel21.setText("KODE");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, -1, -1));

        jLabel22.setText("NAMA");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 73, -1, -1));

        jLabel23.setText("TARIF");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 15, -1, -1));

        jLabel24.setText("KETERANGAN");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, -1, -1));

        jLabel32.setText("KATEGORI");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, -1, -1));
        jPanel6.add(txt_admin_tindakan_kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 140, -1));

        pnl_tindakan.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 700, 110));

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTambahTindakan.setText("+ TINDAKAN");
        btnTambahTindakan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(btnTambahTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        btnResetTindakan.setText("X FIELDS");
        jPanel7.add(btnResetTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 80, 40));

        pnl_tindakan.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 700, 60));

        getContentPane().add(pnl_tindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnl_barang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_barang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("OBAT DAN BAHAN HABIS PAKAI");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnl_barang.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, 20));

        tbl_barang.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_barang);

        pnl_barang.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 700, 220));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("ID");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel1.add(txt_barang_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 80, -1));

        jLabel2.setText("KODE");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel1.add(txt_barang_kode, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 80, -1));
        jPanel1.add(txt_barang_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 250, -1));

        jLabel4.setText("NAMA");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel5.setText("JUMLAH");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        txt_barang_jumlah.setToolTipText("");
        jPanel1.add(txt_barang_jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 60, -1));

        cb_barang_satuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cb_barang_satuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 110, -1));

        jLabel6.setText("SATUAN");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, -1, -1));

        jLabel7.setText("HARGA");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 70, -1, -1));
        jPanel1.add(txt_barang_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 110, -1));

        jLabel8.setText("KETERANGAN");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        txt_barang_ket.setToolTipText("");
        jPanel1.add(txt_barang_ket, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 250, -1));

        pnl_barang.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 700, 130));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_barang.setText("+ BARANG");
        jPanel2.add(btn_tambah_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, 40));

        btn_edit_barang.setText("EDIT");
        jPanel2.add(btn_edit_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 40));

        btn_clear_barang.setText("X Fields");
        jPanel2.add(btn_clear_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, -1, 40));

        pnl_barang.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 700, 60));

        getContentPane().add(pnl_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnl_rekam.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_rekam.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("REKAM MEDIK");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnl_rekam.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, 20));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText("ID");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 22, -1, -1));

        jLabel11.setText("KODE");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, -1, -1));

        jLabel12.setText("NIK");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, -1, -1));

        jLabel13.setText("NAMA");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 115, -1, -1));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("JENIS KELAMIN");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 145, -1, -1));

        jLabel15.setText("TGL LAHIR");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 22, -1, -1));

        jLabel16.setText("GOL. DARAH");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 55, -1, -1));

        jLabel17.setText("AGAMA");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 85, -1, -1));

        jLabel18.setText("NO TELEPON");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 115, -1, -1));
        jPanel3.add(txt_rekam_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 50, -1));
        jPanel3.add(txt_rekam_kode, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 50, -1));
        jPanel3.add(txt_rekam_nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 160, -1));
        jPanel3.add(txt_rekam_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 200, -1));

        cb_rekam_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cb_rekam_kelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 200, -1));

        cb_rekam_gol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cb_rekam_gol, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, -1, -1));

        cb_rekam_agama.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cb_rekam_agama, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, -1, -1));
        jPanel3.add(txt_rekam_telp, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 160, -1));
        jPanel3.add(txt_rekam_tl, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 150, -1));

        pnl_rekam.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 700, 190));

        tbl_rekam.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tbl_rekam);

        pnl_rekam.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 700, 200));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_rekam_edit.setText("EDIT");
        jPanel4.add(btn_rekam_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        btn_rekam_tambah.setLabel("+ DATA");
        jPanel4.add(btn_rekam_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 80, 40));

        pnl_rekam.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 700, 60));

        getContentPane().add(pnl_rekam, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnl_menu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBarang.setText("BARANG"); // NOI18N
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });
        pnl_menu.add(btnBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 130, 51));
        btnBarang.getAccessibleContext().setAccessibleName("BHP");

        btn_unit.setText("UNIT");
        btn_unit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_unitActionPerformed(evt);
            }
        });
        pnl_menu.add(btn_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 130, 51));

        btn_rekam.setText("REKAM MEDIK");
        btn_rekam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rekamActionPerformed(evt);
            }
        });
        pnl_menu.add(btn_rekam, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 130, 51));

        btn_pegawai.setLabel("PEGAWAI");
        btn_pegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pegawaiActionPerformed(evt);
            }
        });
        pnl_menu.add(btn_pegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 130, 51));

        btn_op.setText("OPERATOR");
        btn_op.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_opActionPerformed(evt);
            }
        });
        pnl_menu.add(btn_op, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 130, 51));

        btn_tindakan.setText("TINDAKAN");
        btn_tindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tindakanActionPerformed(evt);
            }
        });
        pnl_menu.add(btn_tindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 130, 51));

        getContentPane().add(pnl_menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 180, 540));

        lbl_status.setText("status");
        getContentPane().add(lbl_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
        pnl_tindakan.setVisible(false);
        pnl_unit.setVisible(false);
        pnl_barang.setVisible(true);
        pnl_rekam.setVisible(false);
        pnl_pegawai.setVisible(false);
        pnl_op.setVisible(false);
    }//GEN-LAST:event_btnBarangActionPerformed

    private void btn_rekamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rekamActionPerformed
        pnl_tindakan.setVisible(false);
        pnl_unit.setVisible(false);
        pnl_barang.setVisible(false);
        pnl_rekam.setVisible(true);
        pnl_pegawai.setVisible(false);
        pnl_op.setVisible(false);
    }//GEN-LAST:event_btn_rekamActionPerformed

    private void btn_pegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pegawaiActionPerformed
        try {
            dokterEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_pegawaiActionPerformed

    private void btn_tindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tindakanActionPerformed
        pnl_tindakan.setVisible(true);
        pnl_unit.setVisible(false);
        pnl_barang.setVisible(false);
        pnl_rekam.setVisible(false);
        pnl_pegawai.setVisible(false);
        pnl_op.setVisible(false);
    }//GEN-LAST:event_btn_tindakanActionPerformed

    private void btn_unitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_unitActionPerformed
        try {
            unitEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_unitActionPerformed

    private void btn_opActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_opActionPerformed
        try {
            operatorEventController.onLoad();
        } catch (ServiceException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_opActionPerformed
    
    private void btn_simpan_unitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpan_unitActionPerformed
        try {
            unitEventController.onSave();
        } catch (ServiceException ex) {
            Logger.getLogger(FrameAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_simpan_unitActionPerformed

    private void tbl_unitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_unitMouseClicked
        unitEventController.onTableClick();
    }//GEN-LAST:event_tbl_unitMouseClicked

    private void btn_tambah_opActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_opActionPerformed
        try {
            operatorEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_tambah_opActionPerformed

    private void tbl_opMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_opMouseClicked
        operatorEventController.onTableClick();
    }//GEN-LAST:event_tbl_opMouseClicked

    private void btn_clear_opActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_opActionPerformed
        operatorEventController.onCleanForm();
    }//GEN-LAST:event_btn_clear_opActionPerformed

    private void txt_admin_operator_unitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_admin_operator_unitMouseClicked
        FrameCari cari = new FrameCari(this,Unit.class);
        cari.setVisible(true);
    }//GEN-LAST:event_txt_admin_operator_unitMouseClicked

    private void btn_clear_unitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_unitActionPerformed
        unitEventController.onCleanForm();
    }//GEN-LAST:event_btn_clear_unitActionPerformed

    private void tab_paneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_paneMouseClicked
        int selectedIndex = tab_pane.getSelectedIndex();

        try {
            switch(selectedIndex) {
                case 0: dokterEventController.onLoad();
                    break;
                case 1: perawatEventController.onLoad();
                    break;
                case 2: apotekerEventController.onLoad();
                    break;
                case 3: pekerjaEventController.onLoad();
                    break;
                default: break;
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tab_paneMouseClicked
    
    private void btn_simpan_dokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpan_dokterActionPerformed
        try {
            dokterEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_simpan_dokterActionPerformed

    private void tbl_dokterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_dokterMouseClicked
        dokterEventController.onTableClick();
    }//GEN-LAST:event_tbl_dokterMouseClicked

    private void btn_clear_dokterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_clear_dokterMouseClicked
        dokterEventController.onCleanForm();
    }//GEN-LAST:event_btn_clear_dokterMouseClicked

    private void btn_tambah_perawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_perawatActionPerformed
        try {
            perawatEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_tambah_perawatActionPerformed

    private void btn_clear_perawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_perawatActionPerformed
        perawatEventController.onCleanForm();
    }//GEN-LAST:event_btn_clear_perawatActionPerformed

    private void tbl_perawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_perawatMouseClicked
        perawatEventController.onTableClick();
    }//GEN-LAST:event_tbl_perawatMouseClicked

    private void tbl_apotekerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_apotekerMouseClicked
        apotekerEventController.onTableClick();
    }//GEN-LAST:event_tbl_apotekerMouseClicked

    private void btn_tambah_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_admActionPerformed
        try {
            pekerjaEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_tambah_admActionPerformed

    private void btn_clear_admActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_admActionPerformed
        pekerjaEventController.onCleanForm();
    }//GEN-LAST:event_btn_clear_admActionPerformed

    private void tbl_admMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_admMouseClicked
        try {
            pekerjaEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tbl_admMouseClicked

    private void btn_tambah_apotekerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_apotekerActionPerformed
        try {
            apotekerEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btn_tambah_apotekerActionPerformed

    private void btn_clear_apotekerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear_apotekerActionPerformed
        apotekerEventController.onCleanForm();
    }//GEN-LAST:event_btn_clear_apotekerActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBarang;
    private javax.swing.JButton btnResetTindakan;
    private javax.swing.JButton btnTambahTindakan;
    private javax.swing.JButton btn_clear_adm;
    private javax.swing.JButton btn_clear_apoteker;
    private javax.swing.JButton btn_clear_barang;
    private javax.swing.JButton btn_clear_dokter;
    private javax.swing.JButton btn_clear_op;
    private javax.swing.JButton btn_clear_perawat;
    private javax.swing.JButton btn_clear_unit;
    private javax.swing.JButton btn_edit_barang;
    private javax.swing.JButton btn_op;
    private javax.swing.JButton btn_pegawai;
    private javax.swing.JButton btn_rekam;
    private javax.swing.JButton btn_rekam_edit;
    private javax.swing.JButton btn_rekam_tambah;
    private javax.swing.JButton btn_simpan_dokter;
    private javax.swing.JButton btn_simpan_unit;
    private javax.swing.JButton btn_tambah_adm;
    private javax.swing.JButton btn_tambah_apoteker;
    private javax.swing.JButton btn_tambah_barang;
    private javax.swing.JButton btn_tambah_op;
    private javax.swing.JButton btn_tambah_perawat;
    private javax.swing.JButton btn_tindakan;
    private javax.swing.JButton btn_unit;
    private javax.swing.JComboBox cbApotekerKelamin;
    private javax.swing.JComboBox cbPekerjaKelamin;
    private javax.swing.JComboBox cb_admin_operator_role;
    private javax.swing.JComboBox cb_barang_satuan;
    private javax.swing.JComboBox cb_dokter_kelamin;
    private javax.swing.JComboBox cb_perawat_kelamin;
    private javax.swing.JComboBox cb_rekam_agama;
    private javax.swing.JComboBox cb_rekam_gol;
    private javax.swing.JComboBox cb_rekam_kelamin;
    private javax.swing.JComboBox cb_unit_tipe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lbl_status;
    private javax.swing.JPanel pnl_barang;
    private javax.swing.JPanel pnl_menu;
    private javax.swing.JPanel pnl_op;
    private javax.swing.JPanel pnl_pegawai;
    private javax.swing.JPanel pnl_rekam;
    private javax.swing.JPanel pnl_tindakan;
    private javax.swing.JPanel pnl_unit;
    private javax.swing.JPanel tab_administrasi;
    private javax.swing.JPanel tab_apoteker;
    private javax.swing.JPanel tab_dokter;
    private javax.swing.JTabbedPane tab_pane;
    private javax.swing.JPanel tab_perawat;
    private javax.swing.JTable tbl_adm;
    private javax.swing.JTable tbl_admin_tindakan;
    private javax.swing.JTable tbl_apoteker;
    private javax.swing.JTable tbl_barang;
    private javax.swing.JTable tbl_dokter;
    private javax.swing.JTable tbl_op;
    private javax.swing.JTable tbl_perawat;
    private javax.swing.JTable tbl_rekam;
    private javax.swing.JTable tbl_unit;
    private javax.swing.JTextField txtApotekerAgama;
    private javax.swing.JTextField txtApotekerDarah;
    private javax.swing.JTextField txtApotekerKode;
    private javax.swing.JTextField txtApotekerLahir;
    private javax.swing.JTextField txtApotekerNama;
    private javax.swing.JTextField txtApotekerNik;
    private javax.swing.JTextField txtApotekerNip;
    private javax.swing.JTextField txtApotekerTelepon;
    private javax.swing.JTextField txtPekerjaAgama;
    private javax.swing.JTextField txtPekerjaDarah;
    private javax.swing.JTextField txtPekerjaKode;
    private javax.swing.JTextField txtPekerjaLahir;
    private javax.swing.JTextField txtPekerjaNama;
    private javax.swing.JTextField txtPekerjaNik;
    private javax.swing.JTextField txtPekerjaNip;
    private javax.swing.JTextField txtPekerjaTelepon;
    private javax.swing.JTextField txt_admin_operator_unit;
    private javax.swing.JTextField txt_admin_tindakan_id;
    private javax.swing.JTextField txt_admin_tindakan_kategori;
    private javax.swing.JTextField txt_admin_tindakan_keterangan;
    private javax.swing.JTextField txt_admin_tindakan_kode;
    private javax.swing.JTextField txt_admin_tindakan_nama;
    private javax.swing.JTextField txt_admin_tindakan_tarif;
    private javax.swing.JTextField txt_barang_harga;
    private javax.swing.JTextField txt_barang_id;
    private javax.swing.JTextField txt_barang_jumlah;
    private javax.swing.JTextField txt_barang_ket;
    private javax.swing.JTextField txt_barang_kode;
    private javax.swing.JTextField txt_barang_nama;
    private javax.swing.JTextField txt_dokter_agama;
    private javax.swing.JTextField txt_dokter_darah;
    private javax.swing.JTextField txt_dokter_kode;
    private javax.swing.JTextField txt_dokter_lahir;
    private javax.swing.JTextField txt_dokter_nama;
    private javax.swing.JTextField txt_dokter_nik;
    private javax.swing.JTextField txt_dokter_nip;
    private javax.swing.JTextField txt_dokter_telepon;
    private javax.swing.JTextField txt_op_nama;
    private javax.swing.JTextField txt_op_pass;
    private javax.swing.JTextField txt_op_uname;
    private javax.swing.JTextField txt_perawat_agama;
    private javax.swing.JTextField txt_perawat_darah;
    private javax.swing.JTextField txt_perawat_kode;
    private javax.swing.JTextField txt_perawat_lahir;
    private javax.swing.JTextField txt_perawat_nama;
    private javax.swing.JTextField txt_perawat_nik;
    private javax.swing.JTextField txt_perawat_nip;
    private javax.swing.JTextField txt_perawat_telepon;
    private javax.swing.JTextField txt_rekam_id;
    private javax.swing.JTextField txt_rekam_kode;
    private javax.swing.JTextField txt_rekam_nama;
    private javax.swing.JTextField txt_rekam_nik;
    private javax.swing.JTextField txt_rekam_telp;
    private javax.swing.JTextField txt_rekam_tl;
    private javax.swing.JTextField txt_unit_bobot;
    private javax.swing.JTextField txt_unit_nama;
    // End of variables declaration//GEN-END:variables
}
