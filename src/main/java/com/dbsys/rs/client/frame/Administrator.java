package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.UnitFrame;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.client.tableModel.PegawaiTableModel;
import com.dbsys.rs.client.tableModel.BarangTableModel;
import com.dbsys.rs.client.tableModel.DokterTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.OperatorTableModel;
import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.PelayananTableModel;
import com.dbsys.rs.client.tableModel.PemakaianTableModel;
import com.dbsys.rs.client.tableModel.TindakanTableModel;
import com.dbsys.rs.client.tableModel.UnitTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.BarangService;
import com.dbsys.rs.connector.service.OperatorService;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PegawaiServices;
import com.dbsys.rs.connector.service.PelayananService;
import com.dbsys.rs.connector.service.PemakaianService;
import com.dbsys.rs.connector.service.PendudukService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.connector.service.UnitService;
import com.dbsys.rs.client.Kelas;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.entity.Apoteker;
import com.dbsys.rs.client.entity.BahanHabisPakai;
import com.dbsys.rs.client.entity.Barang;
import com.dbsys.rs.client.entity.Dokter;
import com.dbsys.rs.client.entity.KategoriTindakan;
import com.dbsys.rs.client.entity.ObatFarmasi;
import com.dbsys.rs.client.entity.Operator;
import com.dbsys.rs.client.entity.Operator.Role;
import com.dbsys.rs.client.entity.Pasien;
import com.dbsys.rs.client.entity.Pegawai;
import com.dbsys.rs.client.entity.Pekerja;
import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.Pemakaian;
import com.dbsys.rs.client.entity.Penduduk;
import com.dbsys.rs.client.entity.Penduduk.Kelamin;
import com.dbsys.rs.client.entity.Perawat;
import com.dbsys.rs.client.entity.Tindakan;
import com.dbsys.rs.client.entity.Unit;

import java.awt.Color;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class Administrator extends javax.swing.JFrame implements  UnitFrame {
    private final UnitEventController unitEventController;
    private final OperatorEventController operatorEventController;
    private final DokterEventController dokterEventController;
    private final PerawatEventController perawatEventController;
    private final ApotekerEventController apotekerEventController;
    private final PekerjaEventController pekerjaEventController;
    private final PendudukEventController pendudukEventController;
    private final ObatEventController obatEventController;
    private final BhpEventController bhpEventController;
    private final TindakanEventController tindakanEventController;
    private final PasienEventController pasienEventController;
    private final PelayananEventController pelayananEventController;
    private final PemakaianEventController pemakaianEventController;

    private final TokenService tokenService = TokenService.getInstance();
    
    private final Color colorTransparentPanel = new Color(255, 255, 255, 50);
    
    /**
     * Creates new form admin
     */
    public Administrator() {
        initComponents();
        resetPanelVisibility();
        
        unitEventController = new UnitEventController();
        operatorEventController = new OperatorEventController();
        dokterEventController = new DokterEventController();
        perawatEventController = new PerawatEventController();
        apotekerEventController = new ApotekerEventController();
        pekerjaEventController = new PekerjaEventController();
        pendudukEventController = new PendudukEventController();
        obatEventController = new ObatEventController();
        bhpEventController = new BhpEventController();
        tindakanEventController = new TindakanEventController();
        pasienEventController = new PasienEventController();
        pelayananEventController = new PelayananEventController();
        pemakaianEventController = new PemakaianEventController();
        
        String nama = TokenHolder.getNamaOperator();
        lbl_status.setText(nama);
    }
    
    private void resetPanelVisibility() {
        pnlBarang.setVisible(false);
        pnlOperator.setVisible(false);
        pnlPasien.setVisible(false);
        pnlPegawai.setVisible(false);
        pnlPelayanan.setVisible(false);
        pnlPemakaian.setVisible(false);
        pnlPenduduk.setVisible(false);
        pnlTindakan.setVisible(false);
        pnlUnit.setVisible(false);
    }

    @Override
    public void setUnit(Unit unit){
        unitEventController.setModel(unit);
        txt_admin_operator_unit.setText(unit.getNama());
    }
    
    public void setKategoriForTindakan(KategoriTindakan kategori) {
        tindakanEventController.setKategori(kategori);
        txtTindakanKategori.setText(kategori.getNama());
    }
    
    private class UnitEventController implements EventController<Unit> {
        private final UnitService unitservice = UnitService.getInstance();
        private Unit model;

        @Override
        public Unit getModel() {
            return model;
        }

        @Override
        public void setModel(Unit unit) {
            if (unit != null) {
                txt_unit_nama.setText(unit.getNama());
                cb_unit_tipe.setSelectedItem(unit.getTipe());
            } else {
                txt_unit_nama.setText(null);
                cb_unit_tipe.setSelectedIndex(0);
            }
            
            this.model = unit;
        }
        
        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Unit();

            String tipe = (String)cb_unit_tipe.getSelectedItem();
            model.setTipe(Unit.TipeUnit.valueOf(tipe));
            model.setBobot(1F); // DEFAULT VALUE
            model.setNama(txt_unit_nama.getText());

            unitservice.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() {
            int row = tbl_unit.getSelectedRow();

            UnitTableModel tableModel = (UnitTableModel)tbl_unit.getModel();
            Unit unit = tableModel.getUnit(row);

            setModel(unit);
            setComponentEnabled(false);
        }

        @Override
        public void onCleanForm() {
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onLoad() throws ServiceException {
            resetPanelVisibility();
            pnlUnit.setVisible(true);

            List<Unit> listUnit = unitservice.getAll();
            UnitTableModel tableModel = new UnitTableModel(listUnit);
            tbl_unit.setModel(tableModel);
            
            onCleanForm();
        }

        @Override
        public void onDelete() throws ServiceException {
            unitservice.hapus(model);
            onLoad();
        }
        
        @Override
        public void setComponentEnabled(boolean enabled) {
            txt_unit_nama.setEnabled(enabled);
            cb_unit_tipe.setEnabled(enabled);
        }
    }

    private class OperatorEventController implements EventController<Operator> {
        private final OperatorService operatorService = OperatorService.getInstance();
        private Operator model;

        @Override
        public Operator getModel() {
            return model;
        }

        @Override
        public void setModel(Operator operator) {
            if (operator != null) {
                txt_op_nama.setText(operator.getNama());
                txt_op_uname.setText(operator.getUsername());
                txt_op_pass.setText(operator.getPassword());
                txt_admin_operator_unit.setText(operator.getUnit().getNama());
                cb_admin_operator_role.setSelectedItem(operator.getRole().toString());
            } else {
                txt_op_nama.setText(null);
                txt_op_uname.setText(null);
                txt_op_pass.setText(null);
                txt_admin_operator_unit.setText(null);
                cb_admin_operator_role.setSelectedIndex(0);
            }

            this.model = operator;
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
            Operator operator = tableModel.getOperator(row);

            setModel(operator);
            setComponentEnabled(false);
        }

        @Override
        public void onCleanForm() {
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onLoad() throws ServiceException {
            resetPanelVisibility();
            pnlOperator.setVisible(true);

            List<Operator> listOperator = operatorService.getAll();
            OperatorTableModel tableModel = new OperatorTableModel(listOperator);
            tbl_op.setModel(tableModel);
            
            onCleanForm();
        }

        @Override
        public void onDelete() throws ServiceException {
            operatorService.hapus(model);
            onLoad();
        }
        
        @Override
        public void setComponentEnabled(boolean enabled) {
            txt_op_nama.setEnabled(enabled);
            txt_op_uname.setEnabled(enabled);
            txt_op_pass.setEnabled(enabled);
            txt_admin_operator_unit.setEnabled(enabled);
            cb_admin_operator_role.setEnabled(enabled);
        }
    }
    
    private abstract class PegawaiEventController implements EventController<Pegawai> {
        protected final PegawaiServices pegawaiService = PegawaiServices.getInstance();
        private Pegawai model;

        @Override
        public Pegawai getModel() {
            return model;
        }

        @Override
        public void setModel(Pegawai t) {
            model = t;
        }
        
        @Override
        public void onSave() throws ServiceException {
            pegawaiService.simpan(getModel());
            onLoad();
        }

        @Override
        public void onCleanForm() {
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onDelete() throws ServiceException {
            if (model != null) {
                pegawaiService.hapus(model);
                onLoad();
            }
        }
    }
    
    private class DokterEventController extends PegawaiEventController {

        @Override
        public Dokter getModel() {
            Pegawai pegawai = super.getModel();
            
            if (pegawai == null)
                pegawai = new Dokter();

            Calendar tglLahir = txt_dokter_lahir.getSelectedDate();
            String kelamin = (String)cb_dokter_kelamin.getSelectedItem();

            pegawai.setKode(txt_dokter_kode.getText());
            pegawai.setNip(txt_dokter_nip.getText());
            pegawai.setNama(txt_dokter_nama.getText());
            pegawai.setNik(txt_dokter_nik.getText());
            pegawai.setTanggalLahir(new Date(tglLahir.getTimeInMillis()));
            pegawai.setTelepon(txt_dokter_telepon.getText());
            pegawai.setAgama(txt_dokter_agama.getText());
            pegawai.setDarah(txt_dokter_darah.getText());
            pegawai.setKelamin(Kelamin.valueOf(kelamin));
            // pegawai.setSpesialisasi(null);
            
            return (Dokter) pegawai;
        }

        public void setModel(Dokter dokter) {
            if (dokter != null) {
                txt_dokter_kode.setText(dokter.getKode());
                txt_dokter_nip.setText(dokter.getNip());
                txt_dokter_nama.setText(dokter.getNama());
                txt_dokter_nik.setText(dokter.getNik());
                txt_dokter_telepon.setText(dokter.getTelepon());
                txt_dokter_agama.setText(dokter.getAgama());
                txt_dokter_darah.setText(dokter.getDarah());
                cb_dokter_kelamin.setSelectedItem(dokter.getKelamin().toString());
                
                Calendar tanggalLahir = Calendar.getInstance();
                tanggalLahir.setTime(dokter.getTanggalLahir());
                txt_dokter_lahir.setSelectedDate(tanggalLahir);
            } else {
                txt_dokter_kode.setText(Penduduk.createKode());
                txt_dokter_nip.setText(null);
                txt_dokter_nama.setText(null);
                txt_dokter_nik.setText(null);
                txt_dokter_lahir.setText(null);
                txt_dokter_telepon.setText(null);
                txt_dokter_agama.setText(null);
                txt_dokter_darah.setText(null);
                cb_dokter_kelamin.setSelectedIndex(0);
            }
            
            super.setModel(dokter);
        }

        @Override
        public void setModel(Pegawai t) {
            setModel((Dokter) t);
        }
        
        @Override
        public void onTableClick() {
            int row = tbl_dokter.getSelectedRow();

            DokterTableModel tableModel = (DokterTableModel)tbl_dokter.getModel();
            Dokter dokter = tableModel.getDokter(row);
            
            setModel(dokter);
            setComponentEnabled(false);
        }

        @Override
        public void onLoad() throws ServiceException {
            resetPanelVisibility();
            pnlPegawai.setVisible(true);

            List<Pegawai> list = pegawaiService.getAll(Dokter.class);
            DokterTableModel tableModel = new DokterTableModel();
            tableModel.setListPegawai(list);
            tbl_dokter.setModel(tableModel);
            
            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txt_dokter_kode.setEnabled(enabled);
            txt_dokter_nip.setEnabled(enabled);
            txt_dokter_nama.setEnabled(enabled);
            txt_dokter_nik.setEnabled(enabled);
            txt_dokter_lahir.setEnabled(enabled);
            txt_dokter_telepon.setEnabled(enabled);
            txt_dokter_agama.setEnabled(enabled);
            txt_dokter_darah.setEnabled(enabled);
            cb_dokter_kelamin.setEnabled(enabled);
        }
    }
    
    private class PerawatEventController extends PegawaiEventController {

        @Override
        public Perawat getModel() {
            Pegawai pegawai = super.getModel();

            if (pegawai == null)
                pegawai = new Perawat();
                
            Calendar tglLahir = txt_perawat_lahir.getSelectedDate();
            String kelamin = (String)cb_perawat_kelamin.getSelectedItem();

            pegawai.setKode(txt_perawat_kode.getText());
            pegawai.setNip(txt_perawat_nip.getText());
            pegawai.setNama(txt_perawat_nama.getText());
            pegawai.setNik(txt_perawat_nik.getText());
            pegawai.setTanggalLahir(new Date(tglLahir.getTimeInMillis()));
            pegawai.setTelepon(txt_perawat_telepon.getText());
            pegawai.setAgama(txt_perawat_agama.getText());
            pegawai.setDarah(txt_perawat_darah.getText());
            pegawai.setKelamin(Kelamin.valueOf(kelamin));

            return (Perawat) pegawai;
        }

        public void setModel(Perawat perawat) {
            if (perawat != null) {
                txt_perawat_kode.setText(perawat.getKode());
                txt_perawat_nip.setText(perawat.getNip());
                txt_perawat_nama.setText(perawat.getNama());
                txt_perawat_nik.setText(perawat.getNik());
                txt_perawat_telepon.setText(perawat.getTelepon());
                txt_perawat_agama.setText(perawat.getAgama());
                txt_perawat_darah.setText(perawat.getDarah());
                cb_perawat_kelamin.setSelectedItem(perawat.getKelamin().toString());

                Calendar tanggalLahir = Calendar.getInstance();
                tanggalLahir.setTime(perawat.getTanggalLahir());
                txt_perawat_lahir.setSelectedDate(tanggalLahir);
            } else {
                txt_perawat_kode.setText(Penduduk.createKode());
                txt_perawat_nip.setText(null);
                txt_perawat_nama.setText(null);
                txt_perawat_nik.setText(null);
                txt_perawat_lahir.setText(null);
                txt_perawat_telepon.setText(null);
                txt_perawat_agama.setText(null);
                txt_perawat_darah.setText(null);
                cb_perawat_kelamin.setSelectedIndex(0);
            }
            
            super.setModel(perawat);
        }

        @Override
        public void setModel(Pegawai t) {
            setModel((Perawat) t);
        }

        @Override
        public void onTableClick() {
            int row = tbl_perawat.getSelectedRow();

            PegawaiTableModel tableModel = (PegawaiTableModel)tbl_perawat.getModel();
            Perawat perawat = (Perawat) tableModel.getPegawai(row);

            setModel(perawat);
            setComponentEnabled(false);
        }

        @Override
        public void onLoad() throws ServiceException {
            List<Pegawai> list = pegawaiService.getAll(Perawat.class);
            PegawaiTableModel tableModel = new PegawaiTableModel(list);
            tbl_perawat.setModel(tableModel);

            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txt_perawat_kode.setEnabled(enabled);
            txt_perawat_nip.setEnabled(enabled);
            txt_perawat_nama.setEnabled(enabled);
            txt_perawat_nik.setEnabled(enabled);
            txt_perawat_lahir.setEnabled(enabled);
            txt_perawat_telepon.setEnabled(enabled);
            txt_perawat_agama.setEnabled(enabled);
            txt_perawat_darah.setEnabled(enabled);
            cb_perawat_kelamin.setEnabled(enabled);
        }
    }
    
    private class ApotekerEventController extends PegawaiEventController {
        
        @Override
        public Apoteker getModel() {
            Pegawai pegawai = super.getModel();

            if (pegawai == null)
                pegawai = new Apoteker();
                
            Calendar tglLahir = txtApotekerLahir.getSelectedDate();
            String kelamin = (String)cbApotekerKelamin.getSelectedItem();

            pegawai.setKode(txtApotekerKode.getText());
            pegawai.setNip(txtApotekerNip.getText());
            pegawai.setNama(txtApotekerNama.getText());
            pegawai.setNik(txtApotekerNik.getText());
            pegawai.setTanggalLahir(new Date(tglLahir.getTimeInMillis()));
            pegawai.setTelepon(txtApotekerTelepon.getText());
            pegawai.setAgama(txtApotekerAgama.getText());
            pegawai.setDarah(txtApotekerDarah.getText());
            pegawai.setKelamin(Kelamin.valueOf(kelamin));
            
            return (Apoteker) pegawai;
        }

        public void setModel(Apoteker apoteker) {
            if (apoteker != null) {
                txtApotekerKode.setText(apoteker.getKode());
                txtApotekerNip.setText(apoteker.getNip());
                txtApotekerNama.setText(apoteker.getNama());
                txtApotekerNik.setText(apoteker.getNik());
                txtApotekerTelepon.setText(apoteker.getTelepon());
                txtApotekerAgama.setText(apoteker.getAgama());
                txtApotekerDarah.setText(apoteker.getDarah());
                cbApotekerKelamin.setSelectedItem(apoteker.getKelamin().toString());
                
                Calendar tanggalLahir = Calendar.getInstance();
                tanggalLahir.setTime(apoteker.getTanggalLahir());
                txtApotekerLahir.setSelectedDate(tanggalLahir);
            } else {
                txtApotekerKode.setText(Penduduk.createKode());
                txtApotekerNip.setText(null);
                txtApotekerNama.setText(null);
                txtApotekerNik.setText(null);
                txtApotekerLahir.setText(null);
                txtApotekerTelepon.setText(null);
                txtApotekerAgama.setText(null);
                txtApotekerDarah.setText(null);
                cbApotekerKelamin.setSelectedIndex(0);
            }
            
            super.setModel(apoteker);
        }

        @Override
        public void setModel(Pegawai t) {
            setModel((Apoteker) t);
        }

        @Override
        public void onTableClick() {
            int row = tbl_apoteker.getSelectedRow();

            PegawaiTableModel tableModel = (PegawaiTableModel)tbl_apoteker.getModel();
            Apoteker apoteker = (Apoteker) tableModel.getPegawai(row);

            setModel(apoteker);
            setComponentEnabled(false);
        }

        @Override
        public void onLoad() throws ServiceException {
            List<Pegawai> list = pegawaiService.getAll(Apoteker.class);
            PegawaiTableModel tableModel = new PegawaiTableModel(list);
            tbl_apoteker.setModel(tableModel);
            
            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txtApotekerKode.setEnabled(enabled);
            txtApotekerNip.setEnabled(enabled);
            txtApotekerNama.setEnabled(enabled);
            txtApotekerNik.setEnabled(enabled);
            txtApotekerLahir.setEnabled(enabled);
            txtApotekerTelepon.setEnabled(enabled);
            txtApotekerAgama.setEnabled(enabled);
            txtApotekerDarah.setEnabled(enabled);
            cbApotekerKelamin.setEnabled(enabled);
        }
    }
    
    private class PekerjaEventController extends PegawaiEventController {

        @Override
        public Pekerja getModel() {
            Pegawai pegawai = super.getModel();
            
            if (pegawai == null)
                pegawai = new Pekerja();
                
            Calendar tglLahir = txtPekerjaLahir.getSelectedDate();
            String kelamin = (String)cbPekerjaKelamin.getSelectedItem();

            pegawai.setNip(txtPekerjaNip.getText());
            pegawai.setKode(txtPekerjaKode.getText());
            pegawai.setNama(txtPekerjaNama.getText());
            pegawai.setNik(txtPekerjaNik.getText());
            pegawai.setTanggalLahir(new Date(tglLahir.getTimeInMillis()));
            pegawai.setTelepon(txtPekerjaTelepon.getText());
            pegawai.setAgama(txtPekerjaAgama.getText());
            pegawai.setDarah(txtPekerjaDarah.getText());
            pegawai.setKelamin(Kelamin.valueOf(kelamin));
            
            return (Pekerja) pegawai;
        }

        public void setModel(Pekerja pekerja) {
            if (pekerja != null) {
                txtPekerjaKode.setText(pekerja.getKode());
                txtPekerjaNip.setText(pekerja.getNip());
                txtPekerjaNama.setText(pekerja.getNama());
                txtPekerjaNik.setText(pekerja.getNik());
                txtPekerjaTelepon.setText(pekerja.getTelepon());
                txtPekerjaAgama.setText(pekerja.getAgama());
                txtPekerjaDarah.setText(pekerja.getDarah());
                cbPekerjaKelamin.setSelectedItem(pekerja.getKelamin().toString());
                
                Calendar tanggalLahir = Calendar.getInstance();
                tanggalLahir.setTime(pekerja.getTanggalLahir());
                txtPekerjaLahir.setSelectedDate(tanggalLahir);
            } else {
                txtPekerjaKode.setText(Penduduk.createKode());
                txtPekerjaNip.setText(null);
                txtPekerjaNama.setText(null);
                txtPekerjaNik.setText(null);
                txtPekerjaLahir.setText(null);
                txtPekerjaTelepon.setText(null);
                txtPekerjaAgama.setText(null);
                txtPekerjaDarah.setText(null);
                cbPekerjaKelamin.setSelectedIndex(0);
            }
            
            super.setModel(pekerja);
        }

        @Override
        public void setModel(Pegawai t) {
            setModel((Pekerja) t);
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tbl_adm.getSelectedRow();

            PegawaiTableModel tableModel = (PegawaiTableModel)tbl_adm.getModel();
            Pekerja pekerja = (Pekerja) tableModel.getPegawai(row);

            setModel(pekerja);
            setComponentEnabled(false);
        }

        @Override
        public void onLoad() throws ServiceException {
            List<Pegawai> list = pegawaiService.getAll(Pekerja.class);
            PegawaiTableModel tableModel = new PegawaiTableModel(list);
            tbl_adm.setModel(tableModel);

            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txtPekerjaKode.setEnabled(enabled);
            txtPekerjaNip.setEnabled(enabled);
            txtPekerjaNama.setEnabled(enabled);
            txtPekerjaNik.setEnabled(enabled);
            txtPekerjaLahir.setEnabled(enabled);
            txtPekerjaTelepon.setEnabled(enabled);
            txtPekerjaAgama.setEnabled(enabled);
            txtPekerjaDarah.setEnabled(enabled);
            cbPekerjaKelamin.setEnabled(enabled);
        }
    }

    private class PendudukEventController implements EventController<Penduduk> {
        private final PendudukService pendudukService = PendudukService.getInstance();
        private Penduduk model;

        @Override
        public Penduduk getModel() {
            return model;
        }

        @Override
        public void setModel(Penduduk t) {
            if (t != null) {
                txtPendudukKode.setText(t.getKode());
                txtPendudukNama.setText(t.getNama());
                txtPendudukNik.setText(t.getNik());
                txtPendudukTelepon.setText(t.getTelepon());
                txtPendudukAgama.setText(t.getAgama());
                txtPendudukDarah.setText(t.getDarah());
                cbPendudukKelamin.setSelectedItem(t.getKelamin().toString());
                
                Calendar tanggalLahir = Calendar.getInstance();
                tanggalLahir.setTime(t.getTanggalLahir());
                txtPendudukLahir.setSelectedDate(tanggalLahir);
            } else {
                txtPendudukKode.setText(Penduduk.createKode());
                txtPendudukNama.setText(null);
                txtPendudukNik.setText(null);
                txtPendudukLahir.setText(null);
                txtPendudukTelepon.setText(null);
                txtPendudukAgama.setText(null);
                txtPendudukDarah.setText(null);
                cbPendudukKelamin.setSelectedIndex(0);
            }
            
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Penduduk();

            Calendar tglLahir = txtPendudukLahir.getSelectedDate();
            String kelamin = (String)cbPendudukKelamin.getSelectedItem();

            model.setKode(txtPendudukKode.getText());
            model.setNama(txtPendudukNama.getText());
            model.setNik(txtPendudukNik.getText());
            model.setTelepon(txtPendudukTelepon.getText());
            model.setAgama(txtPendudukAgama.getText());
            model.setDarah(txtPendudukDarah.getText());
            model.setTanggalLahir(new Date(tglLahir.getTimeInMillis()));
            model.setKelamin(Kelamin.valueOf(kelamin));

            pendudukService.simpan(model);
            
            onSearch();
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblPenduduk.getSelectedRow();

            PendudukTableModel tableModel = (PendudukTableModel)tblPenduduk.getModel();
            Penduduk penduduk = tableModel.getPenduduk(row);
            setModel(penduduk);
            setComponentEnabled(false);
        }

        @Override
        public void onCleanForm() {
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onLoad() throws ServiceException {
            resetPanelVisibility();
            pnlPenduduk.setVisible(true);
            onCleanForm();
         }

        @Override
        public void onDelete() throws ServiceException {
            pendudukService.hapus(model);
            onSearch();
        }
        
        public void onSearch() throws ServiceException {
            String keyword = txtPendudukKeyword.getText();
            if (keyword.equals(""))
                return;
            
            List<Penduduk> listPenduduk = pendudukService.cari(keyword);
            PendudukTableModel tableModel = new PendudukTableModel(listPenduduk);
            tblPenduduk.setModel(tableModel);

            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txtPendudukKode.setEnabled(enabled);
            txtPendudukNama.setEnabled(enabled);
            txtPendudukNik.setEnabled(enabled);
            txtPendudukLahir.setEnabled(enabled);
            txtPendudukTelepon.setEnabled(enabled);
            txtPendudukAgama.setEnabled(enabled);
            txtPendudukDarah.setEnabled(enabled);
            cbPendudukKelamin.setEnabled(enabled);
        }
    }

    private abstract class BarangEventController implements EventController<Barang> {
        protected final BarangService barangService = BarangService.getInstance();
        private Barang model;

        @Override
        public Barang getModel() {
            return model;
        }

        @Override
        public void setModel(Barang t) {
            model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            barangService.simpan(getModel());
            onSearch();
        }

        @Override
        public void onCleanForm() {
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onDelete() throws ServiceException {
            if (model != null) {
                barangService.hapus(model);
                onSearch();
            }
        }
        
        public abstract void onSearch() throws ServiceException;
    }
    
    private class ObatEventController extends BarangEventController {

        @Override
        public ObatFarmasi getModel() {
            Barang barang = super.getModel();
            
            if (barang == null) {
                barang = new ObatFarmasi();
                
                String harga = txtObatHarga.getText();
                String jumlah = txtObatJumlah.getText();
                String tanggungan = (String)cbObatTanggungan.getSelectedItem();

                barang.setPenanggung(Penanggung.valueOf(tanggungan));
                barang.setHarga(Long.valueOf(harga));
                barang.setJumlah(Long.valueOf(jumlah));
                barang.setKode(txtObatKode.getText());
                barang.setNama(txtObatNama.getText());
                barang.setSatuan(txtObatSatuan.getText());
            }
            
            return (ObatFarmasi) barang;
        }

        public void setModel(ObatFarmasi t) {
            if (t != null) {
                txtObatKode.setText(t.getKode());
                txtObatNama.setText(t.getNama());
                txtObatSatuan.setText(t.getSatuan());
                txtObatHarga.setText(t.getHarga().toString());
                txtObatJumlah.setText(t.getJumlah().toString());
                cbObatTanggungan.setSelectedItem(t.getPenanggung().toString());
            } else {
                txtObatKode.setText(Barang.createKode());
                txtObatNama.setText(null);
                txtObatSatuan.setText(null);
                txtObatHarga.setText(null);
                txtObatJumlah.setText(null);
                cbObatTanggungan.setSelectedIndex(0);
            }

            super.setModel(t);
        }

        @Override
        public void setModel(Barang t) {
            setModel((ObatFarmasi) t);
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblObat.getSelectedRow();

            ObatTableModel tableModel = (ObatTableModel)tblObat.getModel();
            ObatFarmasi obat = tableModel.getObat(row);
            setModel(obat);
            setComponentEnabled(false);
        }

        @Override
        public void onLoad() throws ServiceException {
            resetPanelVisibility();
            pnlBarang.setVisible(true);
            setComponentEnabled(true);
        }
        
        @Override
        public void onSearch() throws ServiceException {
            String keyword = txtObatKeyword.getText();
            if (keyword.equals(""))
                return;
            
            List<Barang> list = barangService.cari(keyword, ObatFarmasi.class);

            ObatTableModel tableModel = new ObatTableModel();
            tableModel.setListBarang(list);
            tblObat.setModel(tableModel);
            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txtObatKode.setEnabled(enabled);
            txtObatNama.setEnabled(enabled);
            txtObatSatuan.setEnabled(enabled);
            txtObatHarga.setEnabled(enabled);
            txtObatJumlah.setEnabled(enabled);
            cbObatTanggungan.setEnabled(enabled);
        }
    }

    private class BhpEventController extends BarangEventController {

        @Override
        public BahanHabisPakai getModel() {
            Barang barang = super.getModel();
            
            if (barang == null) {
                barang = new BahanHabisPakai();
                
                String harga = txtBhpHarga.getText();
                String jumlah = txtBhpJumlah.getText();
                String tanggungan = (String)cbBhpTanggungan.getSelectedItem();

                barang.setPenanggung(Penanggung.valueOf(tanggungan));
                barang.setHarga(Long.valueOf(harga));
                barang.setJumlah(Long.valueOf(jumlah));
                barang.setKode(txtBhpKode.getText());
                barang.setNama(txtBhpNama.getText());
                barang.setSatuan(txtBhpSatuan.getText());
            }

            return (BahanHabisPakai) barang;
        }

        public void setModel(BahanHabisPakai t) {
            if (t != null) {
                txtBhpKode.setText(t.getKode());
                txtBhpNama.setText(t.getNama());
                txtBhpSatuan.setText(t.getSatuan());
                txtBhpHarga.setText(t.getHarga().toString());
                txtBhpJumlah.setText(t.getJumlah().toString());
                cbBhpTanggungan.setSelectedItem(t.getPenanggung().toString());
            } else {
                txtBhpKode.setText(Barang.createKode());
                txtBhpNama.setText(null);
                txtBhpSatuan.setText(null);
                txtBhpHarga.setText(null);
                txtBhpJumlah.setText(null);
                cbBhpTanggungan.setSelectedIndex(0);
            }

            super.setModel(t);
        }

        @Override
        public void setModel(Barang t) {
            setModel((BahanHabisPakai) t);
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblBhp.getSelectedRow();

            BarangTableModel tableModel = (BarangTableModel)tblBhp.getModel();
            BahanHabisPakai bhp = (BahanHabisPakai) tableModel.getBarang(row);
            setModel(bhp);
            setComponentEnabled(false);
        }

        @Override
        public void onLoad() throws ServiceException {
            onCleanForm();
        }

        @Override
        public void onSearch() throws ServiceException {
            String keyword = txtBhpKeyword.getText();
            if (keyword.equals(""))
                return;
            
            List<Barang> list = barangService.cari(keyword, BahanHabisPakai.class);
            BarangTableModel tableModel = new BarangTableModel(list);
            tblBhp.setModel(tableModel);

            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txtBhpKode.setEnabled(enabled);
            txtBhpNama.setEnabled(enabled);
            txtBhpSatuan.setEnabled(enabled);
            txtBhpHarga.setEnabled(enabled);
            txtBhpJumlah.setEnabled(enabled);
            cbBhpTanggungan.setEnabled(enabled);
        }
    }
    
    private class TindakanEventController implements EventController<Tindakan> {
        private final TindakanService tindakanService = TindakanService.getInstance();
        private Tindakan model;
        private KategoriTindakan kategori;

        @Override
        public Tindakan getModel() {
            return model;
        }

        @Override
        public void setModel(Tindakan t) {
            if (t != null) {
                txtTindakanKode.setText(t.getKode());
                txtTindakanNama.setText(t.getNama());
                txtTindakanKategori.setText(t.getKategori().getNama());
                txtTindakanKeterangan.setText(t.getKeterangan());
                txtTindakanTarif.setText(t.getTarif().toString());
                cbTindakanKelas.setSelectedItem(t.getKelas().toString());
                cbTindakanTanggungan.setSelectedItem(t.getPenanggung().toString());
                cbTindakanSatuan.setSelectedItem(t.getSatuan().toString());
            } else {
                txtTindakanKode.setText(Tindakan.createKode());
                txtTindakanNama.setText(null);
                txtTindakanKategori.setText(null);
                txtTindakanKeterangan.setText(null);
                txtTindakanTarif.setText(null);
                cbTindakanKelas.setSelectedIndex(0);
                cbTindakanTanggungan.setSelectedIndex(0);
                cbTindakanSatuan.setSelectedIndex(0);
            }
            
            this.model = t;
        }
        
        public KategoriTindakan getKategori() {
            return kategori;
        }
        
        public void setKategori(KategoriTindakan kategori) {
            this.kategori = kategori;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Tindakan();
            
            String kelas = (String)cbTindakanKelas.getSelectedItem();
            String satuan = (String)cbTindakanSatuan.getSelectedItem();
            String tanggungan = (String)cbTindakanTanggungan.getSelectedItem();
            String tarif = txtTindakanTarif.getText();

            model.setKelas(Kelas.valueOf(kelas));
            model.setSatuan(Tindakan.SatuanTindakan.valueOf(satuan));
            model.setPenanggung(Penanggung.valueOf(tanggungan));
            model.setTarif(Long.valueOf(tarif));
            model.setKode(txtTindakanKode.getText());
            model.setNama(txtTindakanNama.getText());
            model.setKeterangan(txtTindakanKeterangan.getText());
            model.setKategori(kategori);
            
            tindakanService.simpan(model);

            onSearch();
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblTindakan.getSelectedRow();

            TindakanTableModel tableModel = (TindakanTableModel)tblTindakan.getModel();
            Tindakan tindakan = tableModel.getTindakan(row);
            setModel(tindakan);
            setKategori(tindakan.getKategori());
            setComponentEnabled(false);
        }

        @Override
        public void onCleanForm() {            
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onLoad() throws ServiceException {
            resetPanelVisibility();
            pnlTindakan.setVisible(true);
            onCleanForm();
        }

        @Override
        public void onDelete() throws ServiceException {
            tindakanService.hapus(model);
            onSearch();
        }
        
        public void onSearch() throws ServiceException {
            String keyword = txtTindakanKeyword.getText();
            if (keyword.equals(""))
                return;
            
            List<Tindakan> list = tindakanService.cari(keyword);
            TindakanTableModel tableModel = new TindakanTableModel(list);
            tblTindakan.setModel(tableModel);
            onCleanForm();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txtTindakanKode.setEnabled(enabled);
            txtTindakanNama.setEnabled(enabled);
            txtTindakanKategori.setEnabled(enabled);
            txtTindakanKeterangan.setEnabled(enabled);
            txtTindakanTarif.setEnabled(enabled);
            cbTindakanKelas.setEnabled(enabled);
            cbTindakanTanggungan.setEnabled(enabled);
            cbTindakanSatuan.setEnabled(enabled);
        }
    }

    private class PasienEventController implements EventController<Pasien> {
        private final PasienService pasienService = PasienService.getInstance();
        private Pasien model;
        
        @Override
        public Pasien getModel() {
            return model;
        }

        @Override
        public void setModel(Pasien t) {
            if (t != null) {
                txtPasienKodePenduduk.setText(t.getKodePenduduk());
                txtPasienNama.setText(t.getNama());
                txtPasienNik.setText(t.getNik());
                txtPasienLahir.setText(t.getTanggalLahir().toString());
                txtPasienTelepon.setText(t.getTelepon());
                txtPasienAgama.setText(t.getAgama());
                txtPasienDarah.setText(t.getDarah());
                txtPasienKelamin.setText(t.getKelamin().toString());

                txtPasienTanggalMasuk.setText(t.getTanggalMasuk() != null ? t.getTanggalMasuk().toString() : null);
                txtPasienTanggalKeluar.setText(t.getTanggalKeluar() != null ? t.getTanggalKeluar().toString() : null);
                txtPasienTipe.setText(t.getTipePerawatan() != null ? t.getTipePerawatan().toString() : null);
                txtPasienKelas.setText(t.getKelas() != null ? t.getKelas().toString() : null);
                cbPasienKeadaan.setSelectedItem(t.getKeadaan() != null ? t.getKeadaan().toString() : null);
                cbPasienStatus.setSelectedItem(t.getStatus() != null ? t.getStatus().toString() : null);
                txtPasienCicilan.setText(t.getCicilan() != null ? t.getCicilan().toString() : null);
            } else {
                txtPasienKodePenduduk.setText(null);
                txtPasienNama.setText(null);
                txtPasienNik.setText(null);
                txtPasienLahir.setText(null);
                txtPasienTelepon.setText(null);
                txtPasienAgama.setText(null);
                txtPasienDarah.setText(null);
                txtPasienKelamin.setText(null);

                txtPasienTanggalMasuk.setText(null);
                txtPasienTanggalKeluar.setText(null);
                txtPasienTipe.setText(null);
                txtPasienKelas.setText(null);
                cbPasienKeadaan.setSelectedIndex(0);
                cbPasienStatus.setSelectedIndex(0);
                txtPasienCicilan.setText(null);
            }
            
            model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                throw new ServiceException("Silahkan cari pasien dahulu");
            
            String keadaan = (String) cbPasienKeadaan.getSelectedItem();
            if (keadaan.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih keadaan pasien");

            String status = (String) cbPasienStatus.getSelectedItem();
            if (keadaan.equals("- Pilih -"))
                throw new ServiceException("Silahkan memilih status pasien");
            
            pasienService.update(model, Pasien.KeadaanPasien.valueOf(keadaan), Pasien.StatusPasien.valueOf(status));
        }

        @Override
        public void onTableClick() throws ServiceException {
            Integer index = tblPasien.getSelectedRow();

            PasienTableModel tableModel = (PasienTableModel) tblPasien.getModel();
            setModel(tableModel.getPasien(index));
            setComponentEnabled(false);
        }

        @Override
        public void onCleanForm() {
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onLoad() throws ServiceException {
            resetPanelVisibility();
            pnlPasien.setVisible(true);
            setComponentEnabled(true);
        }

        @Override
        public void onDelete() throws ServiceException {
            pasienService.hapus(model);
            onSearch();
        }
        
        public void onSearch() throws ServiceException {
            String keyword = txtPasienKeyword.getText();
            if (keyword.equals(""))
                throw new ServiceException("Silahkan masukan kata kunci.");
            
            List<Pasien> listPasien = pasienService.cari(keyword);
            PasienTableModel tableModel = new PasienTableModel(listPasien);
            tblPasien.setModel(tableModel);

            onCleanForm();
        }
        
        public Pasien getByKode(String kode) throws ServiceException {
            return pasienService.get(kode);
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            txtPasienKodePenduduk.setEnabled(enabled);
            txtPasienNama.setEnabled(enabled);
            txtPasienNik.setEnabled(enabled);
            txtPasienLahir.setEnabled(enabled);
            txtPasienTelepon.setEnabled(enabled);
            txtPasienAgama.setEnabled(enabled);
            txtPasienDarah.setEnabled(enabled);
            txtPasienKelamin.setEnabled(enabled);
            
            txtPasienTanggalMasuk.setEnabled(enabled);
            txtPasienTanggalKeluar.setEnabled(enabled);
            txtPasienTipe.setEnabled(enabled);
            txtPasienKelas.setEnabled(enabled);
            cbPasienKeadaan.setEnabled(enabled);
            cbPasienStatus.setEnabled(enabled);
            txtPasienCicilan.setEnabled(enabled);
        }
    }
    
    private class PelayananEventController implements EventController<Pelayanan> {
        private final PelayananService pelayananService = PelayananService.getInstance();
        private Pelayanan model;
        
        @Override
        public Pelayanan getModel() {
            return model;
        }

        @Override
        public void setModel(Pelayanan t) {
            if (t != null) {
                txtPelayananNamaTindakan.setText(t.getNama());
            } else {
                txtPelayananNamaTindakan.setText(null);
            }
            
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onTableClick() throws ServiceException {
            Integer index = tblPelayanan.getSelectedRow();

            PelayananTableModel tableModel = (PelayananTableModel) tblPelayanan.getModel();
            setModel(tableModel.getPelayanan(index));
            setComponentEnabled(false);
        }

        @Override
        public void onCleanForm() {
            txtPelayananNamaPasien.setText(null);
            
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onLoad() throws ServiceException {
            String nomorPasien = txtNomorPasienPelayanan.getText();

            if (nomorPasien == null || nomorPasien.equals(""))
                return;

            Pasien pasien = pasienEventController.getByKode(nomorPasien);

            List<Pelayanan> listPelayanan = pelayananService.getByPasien(pasien);
            PelayananTableModel tableModel = new PelayananTableModel(listPelayanan);
            tblPelayanan.setModel(tableModel);

            onCleanForm();
            txtPelayananNamaPasien.setText(pasien.getNama());
        }

        @Override
        public void onDelete() throws ServiceException {
            pelayananService.hapus(model);
            onLoad();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    private class PemakaianEventController implements EventController<Pemakaian> {
        private final PemakaianService pemakaianService = PemakaianService.getInstance();
        private Pemakaian model;

        @Override
        public Pemakaian getModel() {
            return model;
        }

        @Override
        public void setModel(Pemakaian t) {
            if (t != null) {
                txtPemakaianNamaBarang.setText(t.getNama());
            } else {
                txtPemakaianNamaBarang.setText(null);
            }
            
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onTableClick() throws ServiceException {
            Integer index = tblPemakaian.getSelectedRow();
            
            PemakaianTableModel tableModel = (PemakaianTableModel) tblPemakaian.getModel();
            setModel(tableModel.getPemakaian(index));
            setComponentEnabled(false);
        }

        @Override
        public void onCleanForm() {
            txtPemakaianNamaPasien.setText(null);
            
            setModel(null);
            setComponentEnabled(true);
        }

        @Override
        public void onLoad() throws ServiceException {
            String nomorPasien = txtNomorPasienPemakaian.getText();

            if (nomorPasien == null || nomorPasien.equals(""))
                return;

            Pasien pasien = pasienEventController.getByKode(nomorPasien);

            List<Pemakaian> listPemakaian = pemakaianService.getByPasien(pasien);
            PemakaianTableModel tableModel = new PemakaianTableModel(listPemakaian);
            tblPemakaian.setModel(tableModel);

            onCleanForm();
            txtPemakaianNamaPasien.setText(pasien.getNama());
        }

        @Override
        public void onDelete() throws ServiceException {
            pemakaianService.hapus(model);
            onLoad();
        }

        @Override
        public void setComponentEnabled(boolean enabled) {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

        pnlPenduduk = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtPendudukKode = new javax.swing.JTextField();
        txtPendudukNik = new javax.swing.JTextField();
        txtPendudukNama = new javax.swing.JTextField();
        cbPendudukKelamin = new javax.swing.JComboBox();
        txtPendudukDarah = new javax.swing.JTextField();
        txtPendudukAgama = new javax.swing.JTextField();
        txtPendudukTelepon = new javax.swing.JTextField();
        txtPendudukLahir = new datechooser.beans.DateChooserCombo();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtPendudukKeyword = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        btnPendudukSimpan = new javax.swing.JButton();
        btnPendudukReset = new javax.swing.JButton();
        btnPendudukHapus = new javax.swing.JButton();
        btnPendudukUbah = new javax.swing.JButton();
        pnlPemakaian = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblPemakaian = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        txtNomorPasienPemakaian = new javax.swing.JTextField();
        txtPemakaianNamaPasien = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtPemakaianNamaBarang = new javax.swing.JTextField();
        btnPemakaianHapus = new javax.swing.JButton();
        pnlPasien = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        txtPasienKodePenduduk = new javax.swing.JTextField();
        txtPasienNik = new javax.swing.JTextField();
        txtPasienNama = new javax.swing.JTextField();
        txtPasienKelamin = new javax.swing.JTextField();
        txtPasienLahir = new javax.swing.JTextField();
        txtPasienDarah = new javax.swing.JTextField();
        txtPasienAgama = new javax.swing.JTextField();
        txtPasienTelepon = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        txtPasienTanggalMasuk = new javax.swing.JTextField();
        txtPasienTanggalKeluar = new javax.swing.JTextField();
        txtPasienTipe = new javax.swing.JTextField();
        txtPasienKelas = new javax.swing.JTextField();
        cbPasienKeadaan = new javax.swing.JComboBox();
        cbPasienStatus = new javax.swing.JComboBox();
        txtPasienCicilan = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        txtPasienKeyword = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        btnPasienSimpan = new javax.swing.JButton();
        btnPasienReset = new javax.swing.JButton();
        btnPasienHapus = new javax.swing.JButton();
        btnPasienUbah = new javax.swing.JButton();
        pnlPegawai = new javax.swing.JPanel();
        tab_pane = new javax.swing.JTabbedPane();
        tab_dokter = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_dokter = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        btnDokterSimpan = new javax.swing.JButton();
        btnDokterReset = new javax.swing.JButton();
        btnDokterHapus = new javax.swing.JButton();
        btnDokterUbah = new javax.swing.JButton();
        btnRekapDokter = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        txt_dokter_kode = new javax.swing.JTextField();
        txt_dokter_nama = new javax.swing.JTextField();
        txt_dokter_nip = new javax.swing.JTextField();
        txt_dokter_nik = new javax.swing.JTextField();
        cb_dokter_kelamin = new javax.swing.JComboBox();
        txt_dokter_darah = new javax.swing.JTextField();
        txt_dokter_agama = new javax.swing.JTextField();
        txt_dokter_telepon = new javax.swing.JTextField();
        txt_dokter_lahir = new datechooser.beans.DateChooserCombo();
        tab_perawat = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbl_perawat = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        txt_perawat_kode = new javax.swing.JTextField();
        txt_perawat_nama = new javax.swing.JTextField();
        txt_perawat_nip = new javax.swing.JTextField();
        txt_perawat_nik = new javax.swing.JTextField();
        cb_perawat_kelamin = new javax.swing.JComboBox();
        txt_perawat_darah = new javax.swing.JTextField();
        txt_perawat_agama = new javax.swing.JTextField();
        txt_perawat_telepon = new javax.swing.JTextField();
        txt_perawat_lahir = new datechooser.beans.DateChooserCombo();
        jPanel16 = new javax.swing.JPanel();
        btnPerawatSimpan = new javax.swing.JButton();
        btnPerawatReset = new javax.swing.JButton();
        btnPerawatHapus = new javax.swing.JButton();
        btnPerawatUbah = new javax.swing.JButton();
        btnRekapPerawat = new javax.swing.JButton();
        tab_apoteker = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbl_apoteker = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        btnApotekerSimpan = new javax.swing.JButton();
        btnApotekerReset = new javax.swing.JButton();
        btnApotekerHapus = new javax.swing.JButton();
        btnApotekerUbah = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        txtApotekerKode = new javax.swing.JTextField();
        txtApotekerNama = new javax.swing.JTextField();
        txtApotekerNip = new javax.swing.JTextField();
        txtApotekerNik = new javax.swing.JTextField();
        cbApotekerKelamin = new javax.swing.JComboBox();
        txtApotekerDarah = new javax.swing.JTextField();
        txtApotekerAgama = new javax.swing.JTextField();
        txtApotekerTelepon = new javax.swing.JTextField();
        txtApotekerLahir = new datechooser.beans.DateChooserCombo();
        tab_administrasi = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbl_adm = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        btnPekerjaSimpan = new javax.swing.JButton();
        btnPekerjaReset = new javax.swing.JButton();
        btnPekerjaHapus = new javax.swing.JButton();
        btnPekerjaUbah = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txtPekerjaKode = new javax.swing.JTextField();
        txtPekerjaNama = new javax.swing.JTextField();
        txtPekerjaNip = new javax.swing.JTextField();
        txtPekerjaNik = new javax.swing.JTextField();
        cbPekerjaKelamin = new javax.swing.JComboBox();
        txtPekerjaDarah = new javax.swing.JTextField();
        txtPekerjaAgama = new javax.swing.JTextField();
        txtPekerjaTelepon = new javax.swing.JTextField();
        txtPekerjaLahir = new datechooser.beans.DateChooserCombo();
        pnlPelayanan = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblPelayanan = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel88 = new javax.swing.JLabel();
        txtNomorPasienPelayanan = new javax.swing.JTextField();
        txtPelayananNamaPasien = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPelayananNamaTindakan = new javax.swing.JTextField();
        btnPelayananHapus = new javax.swing.JButton();
        pnlTindakan = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        txtTindakanKode = new javax.swing.JTextField();
        txtTindakanNama = new javax.swing.JTextField();
        txtTindakanKategori = new javax.swing.JTextField();
        txtTindakanKeterangan = new javax.swing.JTextField();
        cbTindakanKelas = new javax.swing.JComboBox();
        cbTindakanTanggungan = new javax.swing.JComboBox();
        cbTindakanSatuan = new javax.swing.JComboBox();
        txtTindakanTarif = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtTindakanKeyword = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        btnTindakanSimpan = new javax.swing.JButton();
        btnTindakanReset = new javax.swing.JButton();
        btnTindakanHapus = new javax.swing.JButton();
        btnTindakanUbah = new javax.swing.JButton();
        pnlBarang = new javax.swing.JPanel();
        tabBarang = new javax.swing.JTabbedPane();
        pnlObat = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtObatKode = new javax.swing.JTextField();
        txtObatNama = new javax.swing.JTextField();
        txtObatHarga = new javax.swing.JTextField();
        cbObatTanggungan = new javax.swing.JComboBox();
        txtObatJumlah = new javax.swing.JTextField();
        txtObatSatuan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtObatKeyword = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        btnObatSimpan = new javax.swing.JButton();
        btnObatReset = new javax.swing.JButton();
        btnObatHapus = new javax.swing.JButton();
        btnObatUbah = new javax.swing.JButton();
        pnlBhp = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtBhpKode = new javax.swing.JTextField();
        txtBhpNama = new javax.swing.JTextField();
        txtBhpHarga = new javax.swing.JTextField();
        cbBhpTanggungan = new javax.swing.JComboBox();
        txtBhpJumlah = new javax.swing.JTextField();
        txtBhpSatuan = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        txtBhpKeyword = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        btnBhpSimpan = new javax.swing.JButton();
        btnBhpReset = new javax.swing.JButton();
        btnBhpHapus = new javax.swing.JButton();
        btnBhpUbah = new javax.swing.JButton();
        pnlOperator = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbl_op = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        txt_op_nama = new javax.swing.JTextField();
        txt_admin_operator_unit = new javax.swing.JTextField();
        txt_op_uname = new javax.swing.JTextField();
        txt_op_pass = new javax.swing.JTextField();
        cb_admin_operator_role = new javax.swing.JComboBox();
        jLabel49 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnOperatorSimpan = new javax.swing.JButton();
        btnOperatorReset = new javax.swing.JButton();
        btnOperatorHapus = new javax.swing.JButton();
        btnOperatorUbah = new javax.swing.JButton();
        pnlUnit = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_unit = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        txt_unit_nama = new javax.swing.JTextField();
        cb_unit_tipe = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnUnitSimpan = new javax.swing.JButton();
        btnUnitReset = new javax.swing.JButton();
        btnUnitHapus = new javax.swing.JButton();
        btnUnitUbah = new javax.swing.JButton();
        btnUnit = new javax.swing.JButton();
        btnOperator = new javax.swing.JButton();
        btnPegawai = new javax.swing.JButton();
        btnBarang = new javax.swing.JButton();
        btnTindakan = new javax.swing.JButton();
        btnPenduduk = new javax.swing.JButton();
        btnPasien = new javax.swing.JButton();
        btnPelayanan = new javax.swing.JButton();
        btnPemakaian = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel89 = new javax.swing.JLabel();
        lbl_status = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Rumah Sakit Liun Kendage Tahuna");
        setMinimumSize(new java.awt.Dimension(1280, 800));
        setName("ADMIN"); // NOI18N
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlPenduduk.setBackground(colorTransparentPanel);
        pnlPenduduk.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PENDUDUK / REKAM MEDIK", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlPenduduk.setBackground(new Color(0,0,0,20));
        pnlPenduduk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("NO. MEDREK");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        jLabel12.setText("NO. JAMINAN");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, 25));

        jLabel13.setText("NAMA");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 100, 25));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("KELAMIN");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, 25));

        jLabel15.setText("TANGGAL LAHIR");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 100, 25));

        jLabel16.setText("GOL. DARAH");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, 100, 25));

        jLabel17.setText("AGAMA");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 100, 25));

        jLabel18.setText("NO. TELEPON");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 100, 25));
        jPanel3.add(txtPendudukKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel3.add(txtPendudukNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel3.add(txtPendudukNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel3.add(cbPendudukKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 350, 25));
        jPanel3.add(txtPendudukDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 350, 25));
        jPanel3.add(txtPendudukAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 350, 25));
        jPanel3.add(txtPendudukTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 110, 350, 25));
        jPanel3.add(txtPendudukLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 350, 25));

        pnlPenduduk.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 1050, 150));

        tblPenduduk.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPenduduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPendudukMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPenduduk);

        pnlPenduduk.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1050, 310));

        jPanel4.setBackground(colorTransparentPanel);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText("NAMA PASIEN");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        txtPendudukKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPendudukKeywordFocusLost(evt);
            }
        });
        txtPendudukKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPendudukKeywordKeyPressed(evt);
            }
        });
        jPanel4.add(txtPendudukKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 480, 25));

        pnlPenduduk.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 660, 60));

        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPendudukSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnPendudukSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukSimpanActionPerformed(evt);
            }
        });
        jPanel28.add(btnPendudukSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnPendudukReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnPendudukReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukResetActionPerformed(evt);
            }
        });
        jPanel28.add(btnPendudukReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnPendudukHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnPendudukHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukHapusActionPerformed(evt);
            }
        });
        jPanel28.add(btnPendudukHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnPendudukUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnPendudukUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukUbahActionPerformed(evt);
            }
        });
        jPanel28.add(btnPendudukUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        pnlPenduduk.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 370, 60));

        getContentPane().add(pnlPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlPemakaian.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DAFTAR PEMAKAIAN OBAT & BHP", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N
        pnlPemakaian.setLayout(null);

        tblPemakaian.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPemakaian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPemakaianMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tblPemakaian);

        pnlPemakaian.add(jScrollPane13);
        jScrollPane13.setBounds(20, 100, 1050, 380);

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel90.setText("NOMOR PASIEN");
        jPanel12.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        txtNomorPasienPemakaian.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomorPasienPemakaianFocusLost(evt);
            }
        });
        txtNomorPasienPemakaian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomorPasienPemakaianKeyPressed(evt);
            }
        });
        jPanel12.add(txtNomorPasienPemakaian, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 430, 25));

        txtPemakaianNamaPasien.setEditable(false);
        jPanel12.add(txtPemakaianNamaPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 430, 25));

        pnlPemakaian.add(jPanel12);
        jPanel12.setBounds(20, 20, 1050, 60);

        jPanel31.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("OBAT / BHP");
        jPanel31.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        txtPemakaianNamaBarang.setEditable(false);
        jPanel31.add(txtPemakaianNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 810, 25));

        btnPemakaianHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnPemakaianHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPemakaianHapusActionPerformed(evt);
            }
        });
        jPanel31.add(btnPemakaianHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 17, 80, 30));

        pnlPemakaian.add(jPanel31);
        jPanel31.setBounds(20, 490, 1050, 70);

        getContentPane().add(pnlPemakaian, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlPasien.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PASIEN", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlPasien.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        tblPasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPasienMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tblPasien);

        pnlPasien.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1050, 190));

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("No. Rekam Medik");
        jPanel19.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, -1));

        jLabel44.setText("No. Jaminan");
        jPanel19.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, -1));

        jLabel45.setText("Nama");
        jPanel19.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 100, -1));

        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel91.setText("Kelamin");
        jPanel19.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, -1));

        jLabel92.setText("Tgl. Lahir");
        jPanel19.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, -1));

        jLabel93.setText("Gol. darah");
        jPanel19.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 90, -1));

        jLabel94.setText("Agama");
        jPanel19.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 90, -1));

        jLabel95.setText("No. Telepon");
        jPanel19.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 90, -1));

        txtPasienKodePenduduk.setEditable(false);
        jPanel19.add(txtPasienKodePenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));

        txtPasienNik.setEditable(false);
        jPanel19.add(txtPasienNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));

        txtPasienNama.setEditable(false);
        jPanel19.add(txtPasienNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));

        txtPasienKelamin.setEditable(false);
        jPanel19.add(txtPasienKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 350, 25));

        txtPasienLahir.setEditable(false);
        jPanel19.add(txtPasienLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 350, 25));

        txtPasienDarah.setEditable(false);
        jPanel19.add(txtPasienDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 350, 25));

        txtPasienAgama.setEditable(false);
        jPanel19.add(txtPasienAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 350, 25));

        txtPasienTelepon.setEditable(false);
        jPanel19.add(txtPasienTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 350, 25));

        jLabel97.setText("Tanggal Masuk");
        jPanel19.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 90, -1));

        jLabel98.setText("Tanggal Keluar");
        jPanel19.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, 90, -1));

        jLabel102.setText("Tipe Perawatan");
        jPanel19.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 80, 90, -1));

        jLabel103.setText("Kelas");
        jPanel19.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 90, -1));

        jLabel104.setText("Keadaan");
        jPanel19.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, 90, -1));

        jLabel101.setText("Status Pelayanan");
        jPanel19.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 170, 90, -1));

        jLabel100.setText("Total Pembayaran");
        jPanel19.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 200, 90, -1));

        txtPasienTanggalMasuk.setEditable(false);
        jPanel19.add(txtPasienTanggalMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 350, 25));

        txtPasienTanggalKeluar.setEditable(false);
        jPanel19.add(txtPasienTanggalKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 350, 25));

        txtPasienTipe.setEditable(false);
        jPanel19.add(txtPasienTipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 350, 25));

        txtPasienKelas.setEditable(false);
        jPanel19.add(txtPasienKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 110, 350, 25));

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI", "LARI" }));
        jPanel19.add(cbPasienKeadaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 140, 350, 25));

        cbPasienStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PERAWATAN", "KELUAR" }));
        jPanel19.add(cbPasienStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 170, 350, 25));
        jPanel19.add(txtPasienCicilan, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 200, 350, 25));

        pnlPasien.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 1050, 270));

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel96.setText("NAMA PASIEN");
        jPanel24.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        txtPasienKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKeywordFocusLost(evt);
            }
        });
        txtPasienKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasienKeywordKeyPressed(evt);
            }
        });
        jPanel24.add(txtPasienKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 490, 25));

        pnlPasien.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 670, 60));

        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPasienSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnPasienSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienSimpanActionPerformed(evt);
            }
        });
        jPanel29.add(btnPasienSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 30));

        btnPasienReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnPasienReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienResetActionPerformed(evt);
            }
        });
        jPanel29.add(btnPasienReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 80, 30));

        btnPasienHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnPasienHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienHapusActionPerformed(evt);
            }
        });
        jPanel29.add(btnPasienHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 80, 30));

        btnPasienUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnPasienUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienUbahActionPerformed(evt);
            }
        });
        jPanel29.add(btnPasienUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 80, 30));

        pnlPasien.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, 370, 50));

        getContentPane().add(pnlPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlPegawai.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PEGAWAI", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlPegawai.setBackground(new Color(0,0,0,20));
        pnlPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        tab_dokter.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1010, 210));

        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDokterSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnDokterSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterSimpanActionPerformed(evt);
            }
        });
        jPanel14.add(btnDokterSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        btnDokterReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnDokterReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDokterResetMouseClicked(evt);
            }
        });
        jPanel14.add(btnDokterReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 17, 80, 30));

        btnDokterHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnDokterHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterHapusActionPerformed(evt);
            }
        });
        jPanel14.add(btnDokterHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnDokterUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnDokterUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterUbahActionPerformed(evt);
            }
        });
        jPanel14.add(btnDokterUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnRekapDokter.setText("REKAP");
        btnRekapDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapDokterActionPerformed(evt);
            }
        });
        jPanel14.add(btnRekapDokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        tab_dokter.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 430, 460, 60));

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setText("NIP");
        jPanel21.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel48.setText("NAMA");
        jPanel21.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel53.setText("KODE");
        jPanel21.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel54.setText("NO. JAMINAN");
        jPanel21.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel56.setText("TGL. LAHIR");
        jPanel21.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        jLabel55.setText("KELAMIN");
        jPanel21.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, 25));

        jLabel57.setText("DARAH");
        jPanel21.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, 25));

        jLabel58.setText("AGAMA");
        jPanel21.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, 25));

        jLabel59.setText("TELEPON");
        jPanel21.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 90, 25));
        jPanel21.add(txt_dokter_kode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel21.add(txt_dokter_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel21.add(txt_dokter_nip, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));
        jPanel21.add(txt_dokter_nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 350, 25));

        cb_dokter_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel21.add(cb_dokter_kelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));
        jPanel21.add(txt_dokter_darah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));
        jPanel21.add(txt_dokter_agama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));
        jPanel21.add(txt_dokter_telepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 350, 25));
        jPanel21.add(txt_dokter_lahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 350, 25));

        tab_dokter.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 1010, 180));

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

        tab_perawat.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1010, 210));

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setText("KODE");
        jPanel15.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel34.setText("NIP");
        jPanel15.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel35.setText("NAMA");
        jPanel15.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel46.setText("NO. JAMINAN");
        jPanel15.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel61.setText("TGL. LAHIR");
        jPanel15.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        jLabel60.setText("KELAMIN");
        jPanel15.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, 25));

        jLabel62.setText("DARAH");
        jPanel15.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, 25));

        jLabel63.setText("AGAMA");
        jPanel15.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, 25));

        jLabel64.setText("TELEPON");
        jPanel15.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 90, 25));
        jPanel15.add(txt_perawat_kode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel15.add(txt_perawat_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel15.add(txt_perawat_nip, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));
        jPanel15.add(txt_perawat_nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 350, 25));

        cb_perawat_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel15.add(cb_perawat_kelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));
        jPanel15.add(txt_perawat_darah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));
        jPanel15.add(txt_perawat_agama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));
        jPanel15.add(txt_perawat_telepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 350, 25));
        jPanel15.add(txt_perawat_lahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 350, 25));

        tab_perawat.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 1010, 180));

        jPanel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPerawatSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnPerawatSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerawatSimpanActionPerformed(evt);
            }
        });
        jPanel16.add(btnPerawatSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        btnPerawatReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnPerawatReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerawatResetActionPerformed(evt);
            }
        });
        jPanel16.add(btnPerawatReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 17, 80, 30));

        btnPerawatHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnPerawatHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerawatHapusActionPerformed(evt);
            }
        });
        jPanel16.add(btnPerawatHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnPerawatUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnPerawatUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerawatUbahActionPerformed(evt);
            }
        });
        jPanel16.add(btnPerawatUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnRekapPerawat.setText("REKAP");
        btnRekapPerawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapPerawatActionPerformed(evt);
            }
        });
        jPanel16.add(btnRekapPerawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        tab_perawat.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 430, 460, 60));

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

        tab_apoteker.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1010, 210));

        jPanel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnApotekerSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnApotekerSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApotekerSimpanActionPerformed(evt);
            }
        });
        jPanel18.add(btnApotekerSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnApotekerReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnApotekerReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApotekerResetActionPerformed(evt);
            }
        });
        jPanel18.add(btnApotekerReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnApotekerHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnApotekerHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApotekerHapusActionPerformed(evt);
            }
        });
        jPanel18.add(btnApotekerHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnApotekerUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnApotekerUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApotekerUbahActionPerformed(evt);
            }
        });
        jPanel18.add(btnApotekerUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        tab_apoteker.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 430, 370, 60));

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel65.setText("KODE");
        jPanel22.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel66.setText("NIP");
        jPanel22.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel67.setText("NAMA");
        jPanel22.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel68.setText("NO. JAMINAN");
        jPanel22.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel69.setText("KELAMIN");
        jPanel22.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, 25));

        jLabel70.setText("TGL. LAHIR");
        jPanel22.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        jLabel71.setText("DARAH");
        jPanel22.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, 25));

        jLabel72.setText("AGAMA");
        jPanel22.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, 25));

        jLabel73.setText("TELEPON");
        jPanel22.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 90, 25));
        jPanel22.add(txtApotekerKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel22.add(txtApotekerNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel22.add(txtApotekerNip, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));
        jPanel22.add(txtApotekerNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 350, 25));

        cbApotekerKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel22.add(cbApotekerKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));
        jPanel22.add(txtApotekerDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));
        jPanel22.add(txtApotekerAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));
        jPanel22.add(txtApotekerTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 350, 25));
        jPanel22.add(txtApotekerLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 350, 25));

        tab_apoteker.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 1010, 180));

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

        tab_administrasi.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1010, 210));

        jPanel20.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPekerjaSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnPekerjaSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPekerjaSimpanActionPerformed(evt);
            }
        });
        jPanel20.add(btnPekerjaSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnPekerjaReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnPekerjaReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPekerjaResetActionPerformed(evt);
            }
        });
        jPanel20.add(btnPekerjaReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnPekerjaHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnPekerjaHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPekerjaHapusActionPerformed(evt);
            }
        });
        jPanel20.add(btnPekerjaHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnPekerjaUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnPekerjaUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPekerjaUbahActionPerformed(evt);
            }
        });
        jPanel20.add(btnPekerjaUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        tab_administrasi.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 430, 370, 60));

        jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel74.setText("KODE");
        jPanel23.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel76.setText("NAMA");
        jPanel23.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel75.setText("NIP");
        jPanel23.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel77.setText("NO. JAMINAN");
        jPanel23.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel79.setText("TGL. LAHIR");
        jPanel23.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        jLabel78.setText("KELAMIN");
        jPanel23.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, 25));

        jLabel80.setText("DARAH");
        jPanel23.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, 25));

        jLabel81.setText("AGAMA");
        jPanel23.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, 25));

        jLabel82.setText("TELEPON");
        jPanel23.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 90, 25));
        jPanel23.add(txtPekerjaKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel23.add(txtPekerjaNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel23.add(txtPekerjaNip, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));
        jPanel23.add(txtPekerjaNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 350, 25));

        cbPekerjaKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel23.add(cbPekerjaKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));
        jPanel23.add(txtPekerjaDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));
        jPanel23.add(txtPekerjaAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));
        jPanel23.add(txtPekerjaTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 350, 25));
        jPanel23.add(txtPekerjaLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 350, 25));

        tab_administrasi.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 1010, 180));

        tab_pane.addTab("TENAGA PENUNJANG", tab_administrasi);

        pnlPegawai.add(tab_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1050, 530));

        getContentPane().add(pnlPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlPelayanan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA PELAYANAN TINDAKAN", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlPelayanan.setLayout(null);

        tblPelayanan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPelayanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPelayananMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblPelayanan);

        pnlPelayanan.add(jScrollPane12);
        jScrollPane12.setBounds(20, 100, 1050, 380);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel88.setText("NOMOR PASIEN");
        jPanel5.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        txtNomorPasienPelayanan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomorPasienPelayananFocusLost(evt);
            }
        });
        txtNomorPasienPelayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomorPasienPelayananKeyPressed(evt);
            }
        });
        jPanel5.add(txtNomorPasienPelayanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 430, 25));

        txtPelayananNamaPasien.setEditable(false);
        jPanel5.add(txtPelayananNamaPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 430, 25));

        pnlPelayanan.add(jPanel5);
        jPanel5.setBounds(20, 20, 1050, 60);

        jPanel30.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("TINDAKAN");
        jPanel30.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 25));

        txtPelayananNamaTindakan.setEditable(false);
        jPanel30.add(txtPelayananNamaTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 810, 25));

        btnPelayananHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnPelayananHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPelayananHapusActionPerformed(evt);
            }
        });
        jPanel30.add(btnPelayananHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 17, 80, 30));

        pnlPelayanan.add(jPanel30);
        jPanel30.setBounds(20, 490, 1050, 70);

        getContentPane().add(pnlPelayanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlTindakan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DAFTAR TINDAKAN", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlTindakan.setBackground(new Color(0,0,0,20));
        pnlTindakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblTindakan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTindakanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblTindakan);

        pnlTindakan.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1050, 310));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(txtTindakanKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 370, 25));
        jPanel6.add(txtTindakanNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 370, 25));

        txtTindakanKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTindakanKategoriMouseClicked(evt);
            }
        });
        jPanel6.add(txtTindakanKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 370, 25));
        jPanel6.add(txtTindakanKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 370, 25));

        cbTindakanKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "NONE", "VVIP", "VIP", "I", "II", "III", "ICU" }));
        jPanel6.add(cbTindakanKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 370, 25));

        cbTindakanTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel6.add(cbTindakanTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 50, 370, 25));

        cbTindakanSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "TINDAKAN", "HARI", "JAM" }));
        jPanel6.add(cbTindakanSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 80, 370, 25));
        jPanel6.add(txtTindakanTarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 110, 370, 25));

        jLabel21.setText("KODE");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel22.setText("NAMA");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel23.setText("TARIF");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 110, 90, 25));

        jLabel24.setText("KETERANGAN");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel32.setText("KATEGORI");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel85.setText("TANGGUNGAN");
        jPanel6.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 50, 90, 25));

        jLabel86.setText("KELAS");
        jPanel6.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 90, 25));

        jLabel87.setText("SATUAN");
        jPanel6.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, 90, 25));

        pnlTindakan.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 1050, 150));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setText("NAMA TINDAKAN");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        txtTindakanKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTindakanKeywordFocusLost(evt);
            }
        });
        txtTindakanKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTindakanKeywordKeyPressed(evt);
            }
        });
        jPanel7.add(txtTindakanKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 490, 25));

        pnlTindakan.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 670, 60));

        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTindakanSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnTindakanSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTindakanSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTindakanSimpanMouseClicked(evt);
            }
        });
        btnTindakanSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanSimpanActionPerformed(evt);
            }
        });
        jPanel27.add(btnTindakanSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnTindakanReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnTindakanReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanResetActionPerformed(evt);
            }
        });
        jPanel27.add(btnTindakanReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnTindakanHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnTindakanHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanHapusActionPerformed(evt);
            }
        });
        jPanel27.add(btnTindakanHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnTindakanUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnTindakanUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanUbahActionPerformed(evt);
            }
        });
        jPanel27.add(btnTindakanUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        pnlTindakan.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 370, 60));

        getContentPane().add(pnlTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlBarang.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA OBAT & BHP", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlBarang.setBackground(new Color(0,0,0,20));
        pnlBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabBarangMouseClicked(evt);
            }
        });

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

        pnlObat.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1020, 290));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(txtObatKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel1.add(txtObatNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel1.add(txtObatHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));

        cbObatTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel1.add(cbObatTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 360, 25));

        txtObatJumlah.setToolTipText("");
        jPanel1.add(txtObatJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 360, 25));

        txtObatSatuan.setToolTipText("");
        jPanel1.add(txtObatSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 360, 25));

        jLabel2.setText("KODE");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel4.setText("NAMA");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel7.setText("HARGA");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel84.setText("TANGGUNGAN");
        jPanel1.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, 25));

        jLabel5.setText("JUMLAH");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, 25));

        jLabel6.setText("SATUAN");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, 25));

        pnlObat.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 1020, 120));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("NAMA OBAT");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 110, 25));

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
        jPanel2.add(txtObatKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 480, 25));

        pnlObat.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 640, 60));

        jPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnObatSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnObatSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatSimpanActionPerformed(evt);
            }
        });
        jPanel25.add(btnObatSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnObatReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnObatReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatResetActionPerformed(evt);
            }
        });
        jPanel25.add(btnObatReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnObatHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnObatHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatHapusActionPerformed(evt);
            }
        });
        jPanel25.add(btnObatHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnObatUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnObatUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnObatUbahActionPerformed(evt);
            }
        });
        jPanel25.add(btnObatUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        pnlObat.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 370, 60));

        tabBarang.addTab("OBAT", pnlObat);

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

        pnlBhp.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1020, 290));

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setText("KODE");
        jPanel13.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel37.setText("NAMA");
        jPanel13.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel40.setText("HARGA");
        jPanel13.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel41.setText("TANGGUNGAN");
        jPanel13.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, 25));

        jLabel38.setText("JUMLAH");
        jPanel13.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, 25));

        jLabel39.setText("SATUAN");
        jPanel13.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, 25));
        jPanel13.add(txtBhpKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel13.add(txtBhpNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel13.add(txtBhpHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));

        cbBhpTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel13.add(cbBhpTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));

        txtBhpJumlah.setToolTipText("");
        jPanel13.add(txtBhpJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));

        txtBhpSatuan.setToolTipText("");
        jPanel13.add(txtBhpSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));

        pnlBhp.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 1020, 120));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
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
        jPanel17.add(txtBhpKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 480, 25));

        jLabel83.setText("NAMA BAHAN");
        jPanel17.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 25));

        pnlBhp.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 640, 60));

        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBhpSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnBhpSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpSimpanActionPerformed(evt);
            }
        });
        jPanel26.add(btnBhpSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnBhpReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnBhpReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpResetActionPerformed(evt);
            }
        });
        jPanel26.add(btnBhpReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnBhpHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnBhpHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpHapusActionPerformed(evt);
            }
        });
        jPanel26.add(btnBhpHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnBhpUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnBhpUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBhpUbahActionPerformed(evt);
            }
        });
        jPanel26.add(btnBhpUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        pnlBhp.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 370, 60));

        tabBarang.addTab("BAHAN HABIS PAKAI", pnlBhp);

        pnlBarang.add(tabBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1050, 540));

        getContentPane().add(pnlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlOperator.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA OPERATOR", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlOperator.setBackground(new Color(0,0,0,20));
        pnlOperator.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        pnlOperator.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1050, 330));

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(txt_op_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 510, 25));

        txt_admin_operator_unit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_admin_operator_unitMouseClicked(evt);
            }
        });
        jPanel10.add(txt_admin_operator_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 510, 25));
        jPanel10.add(txt_op_uname, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 510, 25));
        jPanel10.add(txt_op_pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 510, 25));

        cb_admin_operator_role.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "ADMIN", "OPERATOR" }));
        jPanel10.add(cb_admin_operator_role, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 510, 25));

        jLabel49.setText("NAMA");
        jPanel10.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 90, 25));

        jLabel27.setText("UNIT");
        jPanel10.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 25));

        jLabel50.setText("USERNAME");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 25));

        jLabel51.setText("PASSWORD");
        jPanel10.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        jLabel31.setText("ROLE");
        jPanel10.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        pnlOperator.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 660, 180));

        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOperatorSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnOperatorSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOperatorSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorSimpanActionPerformed(evt);
            }
        });
        jPanel11.add(btnOperatorSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnOperatorReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnOperatorReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorResetActionPerformed(evt);
            }
        });
        jPanel11.add(btnOperatorReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnOperatorHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnOperatorHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorHapusActionPerformed(evt);
            }
        });
        jPanel11.add(btnOperatorHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnOperatorUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnOperatorUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorUbahActionPerformed(evt);
            }
        });
        jPanel11.add(btnOperatorUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        pnlOperator.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 500, 370, 60));

        getContentPane().add(pnlOperator, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        pnlUnit.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA UNIT", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        pnlUnit.setBackground(new Color(0,0,0,20));
        pnlUnit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        pnlUnit.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1050, -1));

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setLayout(null);
        jPanel8.add(txt_unit_nama);
        txt_unit_nama.setBounds(130, 20, 520, 25);

        cb_unit_tipe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "LOKET_PENDAFTARAN", "LOKET_PEMBAYARAN", "POLIKLINIK", "RUANG_PERAWATAN", "APOTEK_FARMASI", "GUDANG_FARMASI", "PENUNJANG_MEDIK", "PENUNJANG_NON_MEDIK", "ICU", "UGD" }));
        jPanel8.add(cb_unit_tipe);
        cb_unit_tipe.setBounds(130, 50, 520, 25);

        jLabel29.setText("NAMA");
        jPanel8.add(jLabel29);
        jLabel29.setBounds(20, 20, 90, 25);

        jLabel52.setText("TIPE");
        jPanel8.add(jLabel52);
        jLabel52.setBounds(20, 50, 90, 25);

        pnlUnit.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 670, 90));

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnUnitSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan.png"))); // NOI18N
        btnUnitSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUnitSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitSimpanActionPerformed(evt);
            }
        });
        jPanel9.add(btnUnitSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 80, 30));

        btnUnitReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_reset.png"))); // NOI18N
        btnUnitReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitResetActionPerformed(evt);
            }
        });
        jPanel9.add(btnUnitReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 17, 80, 30));

        btnUnitHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_hapus.png"))); // NOI18N
        btnUnitHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitHapusActionPerformed(evt);
            }
        });
        jPanel9.add(btnUnitHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 17, 80, 30));

        btnUnitUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_update.png"))); // NOI18N
        btnUnitUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitUbahActionPerformed(evt);
            }
        });
        jPanel9.add(btnUnitUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 17, 80, 30));

        pnlUnit.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 500, 370, 60));

        getContentPane().add(pnlUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1090, 580));

        btnUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_unit.png"))); // NOI18N
        btnUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitActionPerformed(evt);
            }
        });
        getContentPane().add(btnUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 180, 130, 30));

        btnOperator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_operator.png"))); // NOI18N
        btnOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorActionPerformed(evt);
            }
        });
        getContentPane().add(btnOperator, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 220, 130, 30));

        btnPegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_pegawai.png"))); // NOI18N
        btnPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPegawaiActionPerformed(evt);
            }
        });
        getContentPane().add(btnPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 260, 130, 30));

        btnBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_barang.png"))); // NOI18N
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });
        getContentPane().add(btnBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 300, 130, 30));
        btnBarang.getAccessibleContext().setAccessibleName("BHP");

        btnTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_tindakan.png"))); // NOI18N
        btnTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanActionPerformed(evt);
            }
        });
        getContentPane().add(btnTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 340, 130, 30));

        btnPenduduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_medrek.png"))); // NOI18N
        btnPenduduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukActionPerformed(evt);
            }
        });
        getContentPane().add(btnPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 380, 130, 30));

        btnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_pasien.png"))); // NOI18N
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        getContentPane().add(btnPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 420, 130, 30));

        btnPelayanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_pelayanan.png"))); // NOI18N
        btnPelayanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPelayananActionPerformed(evt);
            }
        });
        getContentPane().add(btnPelayanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 460, 130, 30));

        btnPemakaian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_pemakaian.png"))); // NOI18N
        btnPemakaian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPemakaianActionPerformed(evt);
            }
        });
        getContentPane().add(btnPemakaian, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 500, 130, 30));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel89.setText("ANDA LOGIN SEBAGAI : ");
        jToolBar1.add(jLabel89);

        lbl_status.setText("status");
        jToolBar1.add(lbl_status);
        jToolBar1.add(jSeparator1);

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_logout.png"))); // NOI18N
        btnLogout.setFocusable(false);
        btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogout.setMaximumSize(new java.awt.Dimension(80, 20));
        btnLogout.setMinimumSize(new java.awt.Dimension(80, 20));
        btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLogout);

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 770, 1280, 30));

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/bg_admin.png"))); // NOI18N
        getContentPane().add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
        try {
            obatEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBarangActionPerformed

    private void btnPendudukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukActionPerformed
        try {
            pendudukEventController.onLoad();
            pendudukEventController.onCleanForm();
        } catch (ServiceException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPendudukActionPerformed

    private void btnPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPegawaiActionPerformed
        try {
            dokterEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPegawaiActionPerformed

    private void btnTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanActionPerformed
        try {
            tindakanEventController.onLoad();
            tindakanEventController.onCleanForm();
        } catch (ServiceException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanActionPerformed

    private void btnUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnitActionPerformed
        try {
            unitEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnUnitActionPerformed
    
    private void btnUnitSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnitSimpanActionPerformed
        try {
            unitEventController.onSave();
        } catch (ServiceException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnUnitSimpanActionPerformed

    private void tbl_unitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_unitMouseClicked
        unitEventController.onTableClick();
    }//GEN-LAST:event_tbl_unitMouseClicked

    private void btnOperatorSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorSimpanActionPerformed
        try {
            operatorEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnOperatorSimpanActionPerformed

    private void tbl_opMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_opMouseClicked
        operatorEventController.onTableClick();
    }//GEN-LAST:event_tbl_opMouseClicked

    private void btnOperatorResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorResetActionPerformed
        operatorEventController.onCleanForm();
    }//GEN-LAST:event_btnOperatorResetActionPerformed

    private void txt_admin_operator_unitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_admin_operator_unitMouseClicked
        Pencarian cari = new Pencarian(this,Unit.class);
        cari.setVisible(true);
    }//GEN-LAST:event_txt_admin_operator_unitMouseClicked

    private void btnUnitResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnitResetActionPerformed
        unitEventController.onCleanForm();
    }//GEN-LAST:event_btnUnitResetActionPerformed

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
    
    private void btnDokterSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterSimpanActionPerformed
        try {
            dokterEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnDokterSimpanActionPerformed

    private void tbl_dokterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_dokterMouseClicked
        dokterEventController.onTableClick();
    }//GEN-LAST:event_tbl_dokterMouseClicked

    private void btnDokterResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDokterResetMouseClicked
        dokterEventController.onCleanForm();
    }//GEN-LAST:event_btnDokterResetMouseClicked

    private void btnPerawatSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerawatSimpanActionPerformed
        try {
            perawatEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPerawatSimpanActionPerformed

    private void btnPerawatResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerawatResetActionPerformed
        perawatEventController.onCleanForm();
    }//GEN-LAST:event_btnPerawatResetActionPerformed

    private void tbl_perawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_perawatMouseClicked
        perawatEventController.onTableClick();
    }//GEN-LAST:event_tbl_perawatMouseClicked

    private void tbl_apotekerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_apotekerMouseClicked
        apotekerEventController.onTableClick();
    }//GEN-LAST:event_tbl_apotekerMouseClicked

    private void btnPekerjaSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPekerjaSimpanActionPerformed
        try {
            pekerjaEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPekerjaSimpanActionPerformed

    private void btnPekerjaResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPekerjaResetActionPerformed
        pekerjaEventController.onCleanForm();
    }//GEN-LAST:event_btnPekerjaResetActionPerformed

    private void tbl_admMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_admMouseClicked
        try {
            pekerjaEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tbl_admMouseClicked

    private void btnApotekerSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApotekerSimpanActionPerformed
        try {
            apotekerEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnApotekerSimpanActionPerformed

    private void btnApotekerResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApotekerResetActionPerformed
        apotekerEventController.onCleanForm();
    }//GEN-LAST:event_btnApotekerResetActionPerformed

    private void txtPendudukKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPendudukKeywordFocusLost
        try {
            pendudukEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            PendudukTableModel tableModel = new PendudukTableModel(null);
            tblPenduduk.setModel(tableModel);
        }
    }//GEN-LAST:event_txtPendudukKeywordFocusLost

    private void btnPendudukResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukResetActionPerformed
        pendudukEventController.onCleanForm();
    }//GEN-LAST:event_btnPendudukResetActionPerformed

    private void btnPendudukSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukSimpanActionPerformed
        try {
            pendudukEventController.onSave();
            pendudukEventController.onCleanForm();
            pendudukEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPendudukSimpanActionPerformed

    private void tblPendudukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPendudukMouseClicked
        try {
            pendudukEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblPendudukMouseClicked

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

    private void btnObatSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatSimpanActionPerformed
        try {
            obatEventController.onSave();
            obatEventController.onCleanForm();
            obatEventController.onLoad();
       } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
        }
    }//GEN-LAST:event_btnObatSimpanActionPerformed

    private void btnObatResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatResetActionPerformed
        obatEventController.onCleanForm();
    }//GEN-LAST:event_btnObatResetActionPerformed

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

    private void btnBhpSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpSimpanActionPerformed
        try {
            bhpEventController.onSave();
            bhpEventController.onCleanForm();
            bhpEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpSimpanActionPerformed

    private void btnBhpResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpResetActionPerformed
        bhpEventController.onCleanForm();
    }//GEN-LAST:event_btnBhpResetActionPerformed

    private void tabBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabBarangMouseClicked
        int index = tabBarang.getSelectedIndex();
        
        try {
            switch(index) {
                case 0: 
                    obatEventController.onLoad();
                    obatEventController.onCleanForm();
                    break;
                case 1: 
                    bhpEventController.onLoad();
                    bhpEventController.onCleanForm();
                    break;
                default: break;
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tabBarangMouseClicked

    private void txtBhpKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBhpKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnBarang.requestFocus();
    }//GEN-LAST:event_txtBhpKeywordKeyPressed

    private void txtObatKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObatKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnBarang.requestFocus();
    }//GEN-LAST:event_txtObatKeywordKeyPressed

    private void txtPendudukKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPendudukKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnPenduduk.requestFocus();
    }//GEN-LAST:event_txtPendudukKeywordKeyPressed

    private void btnTindakanSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTindakanSimpanMouseClicked
        try {
            tindakanEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanSimpanMouseClicked

    private void txtTindakanKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTindakanKeywordFocusLost
        try {
            tindakanEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            TindakanTableModel tableModel = new TindakanTableModel(null);
            tblTindakan.setModel(tableModel);
        }
    }//GEN-LAST:event_txtTindakanKeywordFocusLost

    private void tblTindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTindakanMouseClicked
        try {
            tindakanEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblTindakanMouseClicked

    private void txtTindakanKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTindakanKategoriMouseClicked
        Pencarian cari = new Pencarian(this,KategoriTindakan.class);
        cari.setVisible(true);
    }//GEN-LAST:event_txtTindakanKategoriMouseClicked

    private void btnOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorActionPerformed
        try {
            operatorEventController.onLoad();
        } catch (ServiceException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnOperatorActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new Utama().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienActionPerformed
        try {
            pasienEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienActionPerformed

    private void tblPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPasienMouseClicked
        try {
            pasienEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblPasienMouseClicked

    private void txtPasienKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasienKeywordFocusLost
        try {
            pasienEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            PasienTableModel tableModel = new PasienTableModel(null);
            tblPasien.setModel(tableModel);
        }
    }//GEN-LAST:event_txtPasienKeywordFocusLost

    private void btnPasienSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienSimpanActionPerformed
        try {
            pasienEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienSimpanActionPerformed

    private void btnPasienResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienResetActionPerformed
        pasienEventController.onCleanForm();
    }//GEN-LAST:event_btnPasienResetActionPerformed

    private void btnTindakanResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanResetActionPerformed
        tindakanEventController.onCleanForm();
    }//GEN-LAST:event_btnTindakanResetActionPerformed

    private void btnTindakanSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanSimpanActionPerformed
        try {
            tindakanEventController.onSave();
            tindakanEventController.onCleanForm();
            tindakanEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanSimpanActionPerformed

    private void btnPelayananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPelayananActionPerformed
        resetPanelVisibility();
        pnlPelayanan.setVisible(true);
    }//GEN-LAST:event_btnPelayananActionPerformed

    private void btnPemakaianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPemakaianActionPerformed
        resetPanelVisibility();
        pnlPemakaian.setVisible(true);
    }//GEN-LAST:event_btnPemakaianActionPerformed

    private void txtNomorPasienPelayananFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorPasienPelayananFocusLost
        try {
            pelayananEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtNomorPasienPelayananFocusLost

    private void txtNomorPasienPemakaianFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomorPasienPemakaianFocusLost
        try {
            pemakaianEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtNomorPasienPemakaianFocusLost

    private void btnUnitHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnitHapusActionPerformed
        try {
            unitEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnUnitHapusActionPerformed

    private void btnOperatorHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorHapusActionPerformed
        try {
            operatorEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnOperatorHapusActionPerformed

    private void btnDokterHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterHapusActionPerformed
        try {
            dokterEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnDokterHapusActionPerformed

    private void btnPerawatHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerawatHapusActionPerformed
        try {
            perawatEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPerawatHapusActionPerformed

    private void btnApotekerHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApotekerHapusActionPerformed
        try {
            apotekerEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnApotekerHapusActionPerformed

    private void btnPekerjaHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPekerjaHapusActionPerformed
        try {
            pekerjaEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPekerjaHapusActionPerformed

    private void btnObatHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatHapusActionPerformed
        try {
            obatEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnObatHapusActionPerformed

    private void btnBhpHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpHapusActionPerformed
        try {
            bhpEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnBhpHapusActionPerformed

    private void btnTindakanHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanHapusActionPerformed
        try {
            tindakanEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTindakanHapusActionPerformed

    private void btnPendudukHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukHapusActionPerformed
        try {
            pendudukEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPendudukHapusActionPerformed

    private void btnPasienHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienHapusActionPerformed
        try {
            pasienEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienHapusActionPerformed

    private void btnPelayananHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPelayananHapusActionPerformed
        try {
            pelayananEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPelayananHapusActionPerformed

    private void btnPemakaianHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPemakaianHapusActionPerformed
        try {
            pemakaianEventController.onDelete();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPemakaianHapusActionPerformed

    private void tblPelayananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPelayananMouseClicked
        try {
            pelayananEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblPelayananMouseClicked

    private void tblPemakaianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPemakaianMouseClicked
        try {
            pemakaianEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblPemakaianMouseClicked

    private void btnUnitUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnitUbahActionPerformed
        unitEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnUnitUbahActionPerformed

    private void btnOperatorUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorUbahActionPerformed
        operatorEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnOperatorUbahActionPerformed

    private void btnDokterUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterUbahActionPerformed
        dokterEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnDokterUbahActionPerformed

    private void btnPerawatUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerawatUbahActionPerformed
        perawatEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnPerawatUbahActionPerformed

    private void btnApotekerUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApotekerUbahActionPerformed
        apotekerEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnApotekerUbahActionPerformed

    private void btnPekerjaUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPekerjaUbahActionPerformed
        pekerjaEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnPekerjaUbahActionPerformed

    private void btnObatUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnObatUbahActionPerformed
        obatEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnObatUbahActionPerformed

    private void btnBhpUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBhpUbahActionPerformed
        bhpEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnBhpUbahActionPerformed

    private void btnTindakanUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTindakanUbahActionPerformed
        tindakanEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnTindakanUbahActionPerformed

    private void btnPendudukUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukUbahActionPerformed
        pendudukEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnPendudukUbahActionPerformed

    private void btnPasienUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienUbahActionPerformed
        pasienEventController.setComponentEnabled(true);
    }//GEN-LAST:event_btnPasienUbahActionPerformed

    private void btnRekapDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapDokterActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Dokter.class);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapDokterActionPerformed

    private void btnRekapPerawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapPerawatActionPerformed
        try {
            RangeTanggal frame = new RangeTanggal(this, Perawat.class);
            frame.setVisible(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnRekapPerawatActionPerformed

    private void txtNomorPasienPemakaianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomorPasienPemakaianKeyPressed
        if (evt.getKeyCode() == 10)
            btnPemakaian.requestFocus();
    }//GEN-LAST:event_txtNomorPasienPemakaianKeyPressed

    private void txtNomorPasienPelayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomorPasienPelayananKeyPressed
        if (evt.getKeyCode() == 10)
            btnPelayanan.requestFocus();
    }//GEN-LAST:event_txtNomorPasienPelayananKeyPressed

    private void txtPasienKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasienKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnPasien.requestFocus();
    }//GEN-LAST:event_txtPasienKeywordKeyPressed

    private void txtTindakanKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTindakanKeywordKeyPressed
        if (evt.getKeyCode() == 10)
            btnTindakan.requestFocus();
    }//GEN-LAST:event_txtTindakanKeywordKeyPressed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApotekerHapus;
    private javax.swing.JButton btnApotekerReset;
    private javax.swing.JButton btnApotekerSimpan;
    private javax.swing.JButton btnApotekerUbah;
    private javax.swing.JButton btnBarang;
    private javax.swing.JButton btnBhpHapus;
    private javax.swing.JButton btnBhpReset;
    private javax.swing.JButton btnBhpSimpan;
    private javax.swing.JButton btnBhpUbah;
    private javax.swing.JButton btnDokterHapus;
    private javax.swing.JButton btnDokterReset;
    private javax.swing.JButton btnDokterSimpan;
    private javax.swing.JButton btnDokterUbah;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnObatHapus;
    private javax.swing.JButton btnObatReset;
    private javax.swing.JButton btnObatSimpan;
    private javax.swing.JButton btnObatUbah;
    private javax.swing.JButton btnOperator;
    private javax.swing.JButton btnOperatorHapus;
    private javax.swing.JButton btnOperatorReset;
    private javax.swing.JButton btnOperatorSimpan;
    private javax.swing.JButton btnOperatorUbah;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPasienHapus;
    private javax.swing.JButton btnPasienReset;
    private javax.swing.JButton btnPasienSimpan;
    private javax.swing.JButton btnPasienUbah;
    private javax.swing.JButton btnPegawai;
    private javax.swing.JButton btnPekerjaHapus;
    private javax.swing.JButton btnPekerjaReset;
    private javax.swing.JButton btnPekerjaSimpan;
    private javax.swing.JButton btnPekerjaUbah;
    private javax.swing.JButton btnPelayanan;
    private javax.swing.JButton btnPelayananHapus;
    private javax.swing.JButton btnPemakaian;
    private javax.swing.JButton btnPemakaianHapus;
    private javax.swing.JButton btnPenduduk;
    private javax.swing.JButton btnPendudukHapus;
    private javax.swing.JButton btnPendudukReset;
    private javax.swing.JButton btnPendudukSimpan;
    private javax.swing.JButton btnPendudukUbah;
    private javax.swing.JButton btnPerawatHapus;
    private javax.swing.JButton btnPerawatReset;
    private javax.swing.JButton btnPerawatSimpan;
    private javax.swing.JButton btnPerawatUbah;
    private javax.swing.JButton btnRekapDokter;
    private javax.swing.JButton btnRekapPerawat;
    private javax.swing.JButton btnTindakan;
    private javax.swing.JButton btnTindakanHapus;
    private javax.swing.JButton btnTindakanReset;
    private javax.swing.JButton btnTindakanSimpan;
    private javax.swing.JButton btnTindakanUbah;
    private javax.swing.JButton btnUnit;
    private javax.swing.JButton btnUnitHapus;
    private javax.swing.JButton btnUnitReset;
    private javax.swing.JButton btnUnitSimpan;
    private javax.swing.JButton btnUnitUbah;
    private javax.swing.JComboBox cbApotekerKelamin;
    private javax.swing.JComboBox cbBhpTanggungan;
    private javax.swing.JComboBox cbObatTanggungan;
    private javax.swing.JComboBox cbPasienKeadaan;
    private javax.swing.JComboBox cbPasienStatus;
    private javax.swing.JComboBox cbPekerjaKelamin;
    private javax.swing.JComboBox cbPendudukKelamin;
    private javax.swing.JComboBox cbTindakanKelas;
    private javax.swing.JComboBox cbTindakanSatuan;
    private javax.swing.JComboBox cbTindakanTanggungan;
    private javax.swing.JComboBox cb_admin_operator_role;
    private javax.swing.JComboBox cb_dokter_kelamin;
    private javax.swing.JComboBox cb_perawat_kelamin;
    private javax.swing.JComboBox cb_unit_tipe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lbl_status;
    private javax.swing.JPanel pnlBarang;
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlObat;
    private javax.swing.JPanel pnlOperator;
    private javax.swing.JPanel pnlPasien;
    private javax.swing.JPanel pnlPegawai;
    private javax.swing.JPanel pnlPelayanan;
    private javax.swing.JPanel pnlPemakaian;
    private javax.swing.JPanel pnlPenduduk;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JPanel pnlUnit;
    private javax.swing.JTabbedPane tabBarang;
    private javax.swing.JPanel tab_administrasi;
    private javax.swing.JPanel tab_apoteker;
    private javax.swing.JPanel tab_dokter;
    private javax.swing.JTabbedPane tab_pane;
    private javax.swing.JPanel tab_perawat;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblObat;
    private javax.swing.JTable tblPasien;
    private javax.swing.JTable tblPelayanan;
    private javax.swing.JTable tblPemakaian;
    private javax.swing.JTable tblPenduduk;
    private javax.swing.JTable tblTindakan;
    private javax.swing.JTable tbl_adm;
    private javax.swing.JTable tbl_apoteker;
    private javax.swing.JTable tbl_dokter;
    private javax.swing.JTable tbl_op;
    private javax.swing.JTable tbl_perawat;
    private javax.swing.JTable tbl_unit;
    private javax.swing.JTextField txtApotekerAgama;
    private javax.swing.JTextField txtApotekerDarah;
    private javax.swing.JTextField txtApotekerKode;
    private datechooser.beans.DateChooserCombo txtApotekerLahir;
    private javax.swing.JTextField txtApotekerNama;
    private javax.swing.JTextField txtApotekerNik;
    private javax.swing.JTextField txtApotekerNip;
    private javax.swing.JTextField txtApotekerTelepon;
    private javax.swing.JTextField txtBhpHarga;
    private javax.swing.JTextField txtBhpJumlah;
    private javax.swing.JTextField txtBhpKeyword;
    private javax.swing.JTextField txtBhpKode;
    private javax.swing.JTextField txtBhpNama;
    private javax.swing.JTextField txtBhpSatuan;
    private javax.swing.JTextField txtNomorPasienPelayanan;
    private javax.swing.JTextField txtNomorPasienPemakaian;
    private javax.swing.JTextField txtObatHarga;
    private javax.swing.JTextField txtObatJumlah;
    private javax.swing.JTextField txtObatKeyword;
    private javax.swing.JTextField txtObatKode;
    private javax.swing.JTextField txtObatNama;
    private javax.swing.JTextField txtObatSatuan;
    private javax.swing.JTextField txtPasienAgama;
    private javax.swing.JTextField txtPasienCicilan;
    private javax.swing.JTextField txtPasienDarah;
    private javax.swing.JTextField txtPasienKelamin;
    private javax.swing.JTextField txtPasienKelas;
    private javax.swing.JTextField txtPasienKeyword;
    private javax.swing.JTextField txtPasienKodePenduduk;
    private javax.swing.JTextField txtPasienLahir;
    private javax.swing.JTextField txtPasienNama;
    private javax.swing.JTextField txtPasienNik;
    private javax.swing.JTextField txtPasienTanggalKeluar;
    private javax.swing.JTextField txtPasienTanggalMasuk;
    private javax.swing.JTextField txtPasienTelepon;
    private javax.swing.JTextField txtPasienTipe;
    private javax.swing.JTextField txtPekerjaAgama;
    private javax.swing.JTextField txtPekerjaDarah;
    private javax.swing.JTextField txtPekerjaKode;
    private datechooser.beans.DateChooserCombo txtPekerjaLahir;
    private javax.swing.JTextField txtPekerjaNama;
    private javax.swing.JTextField txtPekerjaNik;
    private javax.swing.JTextField txtPekerjaNip;
    private javax.swing.JTextField txtPekerjaTelepon;
    private javax.swing.JTextField txtPelayananNamaPasien;
    private javax.swing.JTextField txtPelayananNamaTindakan;
    private javax.swing.JTextField txtPemakaianNamaBarang;
    private javax.swing.JTextField txtPemakaianNamaPasien;
    private javax.swing.JTextField txtPendudukAgama;
    private javax.swing.JTextField txtPendudukDarah;
    private javax.swing.JTextField txtPendudukKeyword;
    private javax.swing.JTextField txtPendudukKode;
    private datechooser.beans.DateChooserCombo txtPendudukLahir;
    private javax.swing.JTextField txtPendudukNama;
    private javax.swing.JTextField txtPendudukNik;
    private javax.swing.JTextField txtPendudukTelepon;
    private javax.swing.JTextField txtTindakanKategori;
    private javax.swing.JTextField txtTindakanKeterangan;
    private javax.swing.JTextField txtTindakanKeyword;
    private javax.swing.JTextField txtTindakanKode;
    private javax.swing.JTextField txtTindakanNama;
    private javax.swing.JTextField txtTindakanTarif;
    private javax.swing.JTextField txt_admin_operator_unit;
    private javax.swing.JTextField txt_dokter_agama;
    private javax.swing.JTextField txt_dokter_darah;
    private javax.swing.JTextField txt_dokter_kode;
    private datechooser.beans.DateChooserCombo txt_dokter_lahir;
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
    private datechooser.beans.DateChooserCombo txt_perawat_lahir;
    private javax.swing.JTextField txt_perawat_nama;
    private javax.swing.JTextField txt_perawat_nik;
    private javax.swing.JTextField txt_perawat_nip;
    private javax.swing.JTextField txt_perawat_telepon;
    private javax.swing.JTextField txt_unit_nama;
    // End of variables declaration//GEN-END:variables
}
