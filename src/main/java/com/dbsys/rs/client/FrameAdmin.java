package com.dbsys.rs.client;

import static com.dbsys.rs.client.EventController.host;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.client.tableModel.ApotekerTableModel;
import com.dbsys.rs.client.tableModel.BhpTableModel;
import com.dbsys.rs.client.tableModel.DokterTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.OperatorTableModel;
import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.PekerjaTableModel;
import com.dbsys.rs.client.tableModel.PerawatTableModel;
import com.dbsys.rs.client.tableModel.TindakanTableModel;
import com.dbsys.rs.client.tableModel.UnitTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.ApotekerService;
import com.dbsys.rs.connector.service.BhpService;
import com.dbsys.rs.connector.service.DokterService;
import com.dbsys.rs.connector.service.ObatService;
import com.dbsys.rs.connector.service.OperatorService;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PekerjaService;
import com.dbsys.rs.connector.service.PendudukService;
import com.dbsys.rs.connector.service.PerawatService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.connector.service.UnitService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Tanggungan;
import com.dbsys.rs.lib.entity.Apoteker;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.KategoriTindakan;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import com.dbsys.rs.lib.entity.Operator;
import com.dbsys.rs.lib.entity.Operator.Role;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pekerja;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.dbsys.rs.lib.entity.Perawat;
import com.dbsys.rs.lib.entity.Tindakan;
import com.dbsys.rs.lib.entity.Unit;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class FrameAdmin extends javax.swing.JFrame {
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

    private final TokenService tokenService = TokenService.getInstance(EventController.host);
    
    /**
     * Creates new form admin
     */
    public FrameAdmin() {
        initComponents();
        
        pnlTindakan.setVisible(false);
        pnlUnit.setVisible(false);
        pnlBarang.setVisible(false);
        pnlPenduduk.setVisible(false);
        pnlPegawai.setVisible(false);
        pnlOperator.setVisible(false);

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
        
        String nama = TokenHolder.getNamaOperator();
        lbl_status.setText(nama);
    }

    public void setUnitForOperator(Unit unit){
        unitEventController.setModel(unit);
        txt_admin_operator_unit.setText(unit.getNama());
    }
    
    public void setKategoriForTindakan(KategoriTindakan kategori) {
        tindakanEventController.setKategori(kategori);
        txtTindakanKategori.setText(kategori.getNama());
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
            pnlTindakan.setVisible(false);
            pnlUnit.setVisible(true);
            pnlBarang.setVisible(false);
            pnlPenduduk.setVisible(false);
            pnlPegawai.setVisible(false);
            pnlOperator.setVisible(false);
            pnlPasien.setVisible(false);

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
            pnlTindakan.setVisible(false);
            pnlUnit.setVisible(false);
            pnlBarang.setVisible(false);
            pnlPenduduk.setVisible(false);
            pnlPegawai.setVisible(false);
            pnlOperator.setVisible(true);
            pnlPasien.setVisible(false);

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

            String tglLahir = txt_dokter_lahir.getText();
            String kelamin = (String)cb_dokter_kelamin.getSelectedItem();

            model.setKode(txt_dokter_kode.getText());
            model.setNip(txt_dokter_nip.getText());
            model.setNama(txt_dokter_nama.getText());
            model.setNik(txt_dokter_nik.getText());
            model.setTanggalLahir(DateUtil.getDate(tglLahir));
            model.setTelepon(txt_dokter_telepon.getText());
            model.setAgama(txt_dokter_agama.getText());
            model.setDarah(txt_dokter_darah.getText());
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
            pnlTindakan.setVisible(false);
            pnlUnit.setVisible(false);
            pnlBarang.setVisible(false);
            pnlPenduduk.setVisible(false);
            pnlPegawai.setVisible(true);
            pnlOperator.setVisible(false);
            pnlPasien.setVisible(false);

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

            model.setKode(txt_perawat_kode.getText());
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

            model.setKode(txtApotekerKode.getText());
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
            model.setKode(txtPekerjaKode.getText());
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

    private class PendudukEventController implements EventController<Penduduk> {
        private final PendudukService pendudukService = PendudukService.getInstance(host);
        private Penduduk model;

        @Override
        public Penduduk getModel() {
            return model;
        }

        @Override
        public void setModel(Penduduk t) {
            this.model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            if (model == null)
                model = new Penduduk();

            String tglLahir = txtPendudukLahir.getText();
            String kelamin = (String)cbPendudukKelamin.getSelectedItem();

            model.setKode(txtPendudukKode.getText());
            model.setNama(txtPendudukNama.getText());
            model.setNik(txtPendudukNik.getText());
            model.setTelepon(txtPendudukTelepon.getText());
            model.setAgama(txtPendudukAgama.getText());
            model.setDarah(txtPendudukDarah.getText());
            model.setTanggalLahir(DateUtil.getDate(tglLahir));
            model.setKelamin(Kelamin.valueOf(kelamin));

            pendudukService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblPenduduk.getSelectedRow();

            PendudukTableModel tableModel = (PendudukTableModel)tblPenduduk.getModel();
            model = tableModel.getPenduduk(row);

            txtPendudukKode.setText(model.getKode());
            txtPendudukNama.setText(model.getNama());
            txtPendudukNik.setText(model.getNik());
            txtPendudukLahir.setText(model.getTanggalLahir().toString());
            txtPendudukTelepon.setText(model.getTelepon());
            txtPendudukAgama.setText(model.getAgama());
            txtPendudukDarah.setText(model.getDarah());
            cbPendudukKelamin.setSelectedItem(model.getKelamin().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txtPendudukKode.setText("");
            txtPendudukNama.setText("");
            txtPendudukNik.setText("");
            txtPendudukLahir.setText("");
            txtPendudukTelepon.setText("");
            txtPendudukAgama.setText("");
            txtPendudukDarah.setText("");
            cbPendudukKelamin.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            pnlTindakan.setVisible(false);
            pnlUnit.setVisible(false);
            pnlBarang.setVisible(false);
            pnlPenduduk.setVisible(true);
            pnlPegawai.setVisible(false);
            pnlOperator.setVisible(false);
            pnlPasien.setVisible(false);

           JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/kode rekam medik");
         }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtPendudukKeyword.getText();
            if (keyword.equals(""))
                throw new ServiceException("Silahkan masukan kata kunci.");
            
            List<Penduduk> listPenduduk = pendudukService.cari(keyword);
            PendudukTableModel tableModel = new PendudukTableModel(listPenduduk);
            tblPenduduk.setModel(tableModel);
        }
        
    }

    private class ObatEventController implements EventController<ObatFarmasi> {
        private final ObatService obatService = ObatService.getInstance(host);
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
            
            model.setTanggungan(Tanggungan.valueOf(tanggungan));
            model.setHarga(Long.valueOf(harga));
            model.setJumlah(Long.valueOf(jumlah));
            model.setKode(txtObatKode.getText());
            model.setNama(txtObatNama.getText());
            model.setSatuan(txtObatSatuan.getText());
            model.setKeterangan(txtObatKeterangan.getText());

            obatService.simpan(model);
            onLoad();
            onCleanForm();
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
            cbObatTanggungan.setSelectedItem(model.getTanggungan().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txtObatKode.setText("");
            txtObatNama.setText("");
            txtObatSatuan.setText("");
            txtObatKeterangan.setText("");
            txtObatHarga.setText("");
            txtObatJumlah.setText("");
            cbObatTanggungan.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            pnlTindakan.setVisible(false);
            pnlUnit.setVisible(false);
            pnlBarang.setVisible(true);
            pnlPenduduk.setVisible(false);
            pnlPegawai.setVisible(false);
            pnlOperator.setVisible(false);
            pnlPasien.setVisible(false);

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
            
            List<ObatFarmasi> listObat = obatService.cari(keyword);
            ObatTableModel tableModel = new ObatTableModel(listObat);
            tblObat.setModel(tableModel);
        }
        
    }

    private class BhpEventController implements EventController<BahanHabisPakai> {
        private final BhpService bhpService = BhpService.getInstance(host);
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
            System.out.println("simpan bhp");
            
            if (model == null)
                model = new BahanHabisPakai();

            String harga = txtBhpHarga.getText();
            String jumlah = txtBhpJumlah.getText();
            String tanggungan = (String)cbBhpTanggungan.getSelectedItem();
            
            model.setTanggungan(Tanggungan.valueOf(tanggungan));
            model.setHarga(Long.valueOf(harga));
            model.setJumlah(Long.valueOf(jumlah));
            model.setKode(txtBhpKode.getText());
            model.setNama(txtBhpNama.getText());
            model.setSatuan(txtBhpSatuan.getText());

            bhpService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblBhp.getSelectedRow();

            BhpTableModel tableModel = (BhpTableModel)tblBhp.getModel();
            model = tableModel.getBhp(row);

            txtBhpKode.setText(model.getKode());
            txtBhpNama.setText(model.getNama());
            txtBhpSatuan.setText(model.getSatuan());
            txtBhpHarga.setText(model.getHarga().toString());
            txtBhpJumlah.setText(model.getJumlah().toString());
            cbObatTanggungan.setSelectedItem(model.getTanggungan().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;

            txtBhpKode.setText("");
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
            
            List<BahanHabisPakai> list = bhpService.cari(keyword);
            BhpTableModel tableModel = new BhpTableModel(list);
            tblBhp.setModel(tableModel);
        }
        
    }
    
    private class TindakanEventController implements EventController<Tindakan> {
        private final TindakanService tindakanService = TindakanService.getInstance(host);
        private Tindakan model;
        private KategoriTindakan kategori;

        @Override
        public Tindakan getModel() {
            return model;
        }

        @Override
        public void setModel(Tindakan t) {
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
            model.setSatuan(Tindakan.Satuan.valueOf(satuan));
            model.setTanggungan(Tanggungan.valueOf(tanggungan));
            model.setTarif(Long.valueOf(tarif));
            model.setKode(txtTindakanKode.getText());
            model.setNama(txtTindakanNama.getText());
            model.setKeterangan(txtTindakanKeterangan.getText());
            model.setKategori(kategori);
            
            tindakanService.simpan(model);
            onLoad();
            onCleanForm();
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblTindakan.getSelectedRow();

            TindakanTableModel tableModel = (TindakanTableModel)tblTindakan.getModel();
            model = tableModel.getTindakan(row);

            txtTindakanKode.setText(model.getKode());
            txtTindakanNama.setText(model.getNama());
            txtTindakanKategori.setText(model.getKategori().getNama());
            txtTindakanKeterangan.setText(model.getKeterangan());
            txtTindakanTarif.setText(model.getTarif().toString());
            cbTindakanKelas.setSelectedItem(model.getKelas().toString());
            cbTindakanTanggungan.setSelectedItem(model.getTanggungan().toString());
            cbTindakanSatuan.setSelectedItem(model.getSatuan().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;
            
            txtTindakanKode.setText("");
            txtTindakanNama.setText("");
            txtTindakanKategori.setText("");
            txtTindakanKeterangan.setText("");
            txtTindakanTarif.setText("");
            cbTindakanKelas.setSelectedIndex(0);
            cbTindakanTanggungan.setSelectedIndex(0);
            cbTindakanSatuan.setSelectedIndex(0);
        }

        @Override
        public void onLoad() throws ServiceException {
            pnlTindakan.setVisible(true);
            pnlUnit.setVisible(false);
            pnlBarang.setVisible(false);
            pnlPenduduk.setVisible(false);
            pnlPegawai.setVisible(false);
            pnlOperator.setVisible(false);
            pnlPasien.setVisible(false);

            JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/kode tindakan");
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtTindakanKeyword.getText();
            if (keyword.equals(""))
                throw new ServiceException("Silahkan masukan kata kunci.");
            
            List<Tindakan> list = tindakanService.cari(keyword);
            TindakanTableModel tableModel = new TindakanTableModel(list);
            tblTindakan.setModel(tableModel);
        }
        
    }

    private class PasienEventController implements EventController<Pasien> {
        private final PasienService pasienService = PasienService.getInstance(host);
        private Pasien model;
        
        @Override
        public Pasien getModel() {
            return model;
        }

        @Override
        public void setModel(Pasien t) {
            model = t;
        }

        @Override
        public void onSave() throws ServiceException {
            String keadaan = (String)cbPasienKeadaan.getSelectedItem();
            String status = (String) cbPasienStatus.getSelectedItem();
            String cicilan = txtPasienCicilan.getText();
            
            if (keadaan.equals("- Pilih -"))
                throw new ServiceException("Silahkan pilih keadaan pasien.");
            
            if (status.equals("- Pilih -"))
                throw new ServiceException("Silahkan pilih status pasien.");
            
            if (cicilan.contains(""))
                throw new ServiceException("Silahkan masukan pembayaran pasien.");
            
            model.setKeadaan(Pasien.KeadaanPasien.valueOf(keadaan));
            model.setStatus(Pasien.StatusPasien.valueOf(status));
            model.setCicilan(new Long(cicilan));
            
            // pasienService.simpan(model);
        }

        @Override
        public void onTableClick() throws ServiceException {
            Integer index = tblPasien.getSelectedRow();

            PasienTableModel tableModel = (PasienTableModel) tblPasien.getModel();
            model = tableModel.getPasien(index);

            txtPasienKodePenduduk.setText(model.getKodePenduduk());
            txtPasienNama.setText(model.getNama());
            txtPasienNik.setText(model.getNik());
            txtPasienLahir.setText(model.getTanggalLahir().toString());
            txtPasienTelepon.setText(model.getTelepon());
            txtPasienAgama.setText(model.getAgama());
            txtPasienDarah.setText(model.getDarah());
            txtPasienKelamin.setText(model.getKelamin().toString());
            
            txtPasienTanggalMasuk.setText(model.getTanggalMasuk() != null ? model.getTanggalMasuk().toString() : null);
            txtPasienTanggalKeluar.setText(model.getTanggalKeluar() != null ? model.getTanggalKeluar().toString() : null);
            txtPasienTipe.setText(model.getTipe() != null ? model.getTipe().toString() : null);
            txtPasienKelas.setText(model.getKelas() != null ? model.getKelas().toString() : null);
            cbPasienKeadaan.setSelectedItem(model.getKeadaan() != null ? model.getKeadaan().toString() : null);
            cbPasienStatus.setSelectedItem(model.getStatus() != null ? model.getStatus().toString() : null);
            txtPasienCicilan.setText(model.getCicilan() != null ? model.getCicilan().toString() : null);
        }

        @Override
        public void onCleanForm() {
            model = null;

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

        @Override
        public void onLoad() throws ServiceException {
            pnlTindakan.setVisible(false);
            pnlUnit.setVisible(false);
            pnlBarang.setVisible(false);
            pnlPenduduk.setVisible(false);
            pnlPegawai.setVisible(false);
            pnlOperator.setVisible(false);
            pnlPasien.setVisible(true);

            JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/nomor rekam medik/nik/nomor pasie.");
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtPasienKeyword.getText();
            if (keyword.equals(""))
                throw new ServiceException("Silahkan masukan kata kunci.");
            
            List<Pasien> listPasien = pasienService.cari(keyword);
            PasienTableModel tableModel = new PasienTableModel(listPasien);
            tblPasien.setModel(tableModel);
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

        pnlPasien = new javax.swing.JPanel();
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
        jScrollPane11 = new javax.swing.JScrollPane();
        tblPasien = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        txtPasienKeyword = new javax.swing.JTextField();
        btnPasienSimpan = new javax.swing.JButton();
        btnPasienEdit = new javax.swing.JButton();
        pnlOperator = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
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
        btn_tambah_op = new javax.swing.JButton();
        btn_clear_op = new javax.swing.JButton();
        pnlUnit = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_unit = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        txt_unit_nama = new javax.swing.JTextField();
        txt_unit_bobot = new javax.swing.JTextField();
        cb_unit_tipe = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btn_simpan_unit = new javax.swing.JButton();
        btn_clear_unit = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        pnlBarang = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabBhp = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtObatKeyword = new javax.swing.JTextField();
        btnTambahObat = new javax.swing.JButton();
        btnClearObat = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
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
        btnTambahBhp = new javax.swing.JButton();
        btnClearBhp = new javax.swing.JButton();
        pnlPegawai = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        tab_pane = new javax.swing.JTabbedPane();
        tab_dokter = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_dokter = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        btn_simpan_dokter = new javax.swing.JButton();
        btn_clear_dokter = new javax.swing.JButton();
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
        txt_dokter_lahir = new javax.swing.JTextField();
        cb_dokter_kelamin = new javax.swing.JComboBox();
        txt_dokter_darah = new javax.swing.JTextField();
        txt_dokter_agama = new javax.swing.JTextField();
        txt_dokter_telepon = new javax.swing.JTextField();
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
        txt_perawat_lahir = new javax.swing.JTextField();
        cb_perawat_kelamin = new javax.swing.JComboBox();
        txt_perawat_darah = new javax.swing.JTextField();
        txt_perawat_agama = new javax.swing.JTextField();
        txt_perawat_telepon = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        btn_tambah_perawat = new javax.swing.JButton();
        btn_clear_perawat = new javax.swing.JButton();
        tab_apoteker = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbl_apoteker = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        btn_tambah_apoteker = new javax.swing.JButton();
        btn_clear_apoteker = new javax.swing.JButton();
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
        txtApotekerLahir = new javax.swing.JTextField();
        cbApotekerKelamin = new javax.swing.JComboBox();
        txtApotekerDarah = new javax.swing.JTextField();
        txtApotekerAgama = new javax.swing.JTextField();
        txtApotekerTelepon = new javax.swing.JTextField();
        tab_administrasi = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbl_adm = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        btn_tambah_adm = new javax.swing.JButton();
        btn_clear_adm = new javax.swing.JButton();
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
        txtPekerjaLahir = new javax.swing.JTextField();
        cbPekerjaKelamin = new javax.swing.JComboBox();
        txtPekerjaDarah = new javax.swing.JTextField();
        txtPekerjaAgama = new javax.swing.JTextField();
        txtPekerjaTelepon = new javax.swing.JTextField();
        pnlTindakan = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
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
        btnSimpanTindakan = new javax.swing.JButton();
        btnResetTindakan = new javax.swing.JButton();
        pnlPenduduk = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
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
        txtPendudukLahir = new javax.swing.JTextField();
        txtPendudukDarah = new javax.swing.JTextField();
        txtPendudukAgama = new javax.swing.JTextField();
        txtPendudukTelepon = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtPendudukKeyword = new javax.swing.JTextField();
        btnPendudukSimpan = new javax.swing.JButton();
        btnPendudukEdit = new javax.swing.JButton();
        btnUnit = new javax.swing.JButton();
        btnOperator = new javax.swing.JButton();
        btnPegawai = new javax.swing.JButton();
        btnBarang = new javax.swing.JButton();
        btnTindakan = new javax.swing.JButton();
        btnPenduduk = new javax.swing.JButton();
        btnPasien = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel89 = new javax.swing.JLabel();
        lbl_status = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnLogout = new javax.swing.JButton();
        jLabel90 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Rumah Sakit Liun Kendage Tahuna");
        setMinimumSize(new java.awt.Dimension(1280, 800));
        setName("ADMIN"); // NOI18N
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlPasien.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPasien.setLayout(null);

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel19.setLayout(null);

        jLabel43.setText("KODE");
        jPanel19.add(jLabel43);
        jLabel43.setBounds(20, 20, 100, 14);

        jLabel44.setText("NIK");
        jPanel19.add(jLabel44);
        jLabel44.setBounds(20, 50, 100, 14);

        jLabel45.setText("NAMA");
        jPanel19.add(jLabel45);
        jLabel45.setBounds(20, 80, 100, 14);

        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel91.setText("KELAMIN");
        jPanel19.add(jLabel91);
        jLabel91.setBounds(20, 110, 100, 14);

        jLabel92.setText("TGL LAHIR");
        jPanel19.add(jLabel92);
        jLabel92.setBounds(20, 140, 90, 14);

        jLabel93.setText("GOL. DARAH");
        jPanel19.add(jLabel93);
        jLabel93.setBounds(20, 170, 90, 14);

        jLabel94.setText("AGAMA");
        jPanel19.add(jLabel94);
        jLabel94.setBounds(20, 200, 90, 14);

        jLabel95.setText("NO TELEPON");
        jPanel19.add(jLabel95);
        jLabel95.setBounds(20, 230, 90, 14);

        txtPasienKodePenduduk.setEditable(false);
        jPanel19.add(txtPasienKodePenduduk);
        txtPasienKodePenduduk.setBounds(130, 20, 350, 20);

        txtPasienNik.setEditable(false);
        jPanel19.add(txtPasienNik);
        txtPasienNik.setBounds(130, 50, 350, 20);

        txtPasienNama.setEditable(false);
        jPanel19.add(txtPasienNama);
        txtPasienNama.setBounds(130, 80, 350, 20);

        txtPasienKelamin.setEditable(false);
        jPanel19.add(txtPasienKelamin);
        txtPasienKelamin.setBounds(130, 110, 350, 20);

        txtPasienLahir.setEditable(false);
        jPanel19.add(txtPasienLahir);
        txtPasienLahir.setBounds(130, 140, 350, 20);

        txtPasienDarah.setEditable(false);
        jPanel19.add(txtPasienDarah);
        txtPasienDarah.setBounds(130, 170, 350, 20);

        txtPasienAgama.setEditable(false);
        jPanel19.add(txtPasienAgama);
        txtPasienAgama.setBounds(130, 200, 350, 20);

        txtPasienTelepon.setEditable(false);
        jPanel19.add(txtPasienTelepon);
        txtPasienTelepon.setBounds(130, 230, 350, 20);

        jLabel97.setText("Tanggal Masuk");
        jPanel19.add(jLabel97);
        jLabel97.setBounds(540, 20, 90, 14);

        jLabel98.setText("Tanggal Keluar");
        jPanel19.add(jLabel98);
        jLabel98.setBounds(540, 50, 90, 14);

        jLabel102.setText("Tipe Perawatan");
        jPanel19.add(jLabel102);
        jLabel102.setBounds(540, 80, 90, 14);

        jLabel103.setText("Kelas");
        jPanel19.add(jLabel103);
        jLabel103.setBounds(540, 110, 90, 14);

        jLabel104.setText("Keadaan");
        jPanel19.add(jLabel104);
        jLabel104.setBounds(540, 140, 90, 14);

        jLabel101.setText("Status Pelayanan");
        jPanel19.add(jLabel101);
        jLabel101.setBounds(540, 170, 90, 14);

        jLabel100.setText("Total Pembayaran");
        jPanel19.add(jLabel100);
        jLabel100.setBounds(540, 200, 90, 14);

        txtPasienTanggalMasuk.setEditable(false);
        jPanel19.add(txtPasienTanggalMasuk);
        txtPasienTanggalMasuk.setBounds(640, 20, 350, 20);

        txtPasienTanggalKeluar.setEditable(false);
        jPanel19.add(txtPasienTanggalKeluar);
        txtPasienTanggalKeluar.setBounds(640, 50, 350, 20);

        txtPasienTipe.setEditable(false);
        jPanel19.add(txtPasienTipe);
        txtPasienTipe.setBounds(640, 80, 350, 20);

        txtPasienKelas.setEditable(false);
        jPanel19.add(txtPasienKelas);
        txtPasienKelas.setBounds(640, 110, 350, 20);

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI", "LARI" }));
        jPanel19.add(cbPasienKeadaan);
        cbPasienKeadaan.setBounds(640, 140, 350, 20);

        cbPasienStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "OPEN", "PAID", "UNPAID" }));
        jPanel19.add(cbPasienStatus);
        cbPasienStatus.setBounds(640, 170, 350, 20);
        jPanel19.add(txtPasienCicilan);
        txtPasienCicilan.setBounds(640, 200, 350, 20);

        pnlPasien.add(jPanel19);
        jPanel19.setBounds(10, 350, 1040, 270);

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

        pnlPasien.add(jScrollPane11);
        jScrollPane11.setBounds(10, 40, 1040, 220);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel24.setLayout(null);

        jLabel96.setText("KATA KUNCI");
        jPanel24.add(jLabel96);
        jLabel96.setBounds(20, 30, 90, 14);

        txtPasienKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKeywordFocusLost(evt);
            }
        });
        jPanel24.add(txtPasienKeyword);
        txtPasienKeyword.setBounds(130, 30, 350, 20);

        pnlPasien.add(jPanel24);
        jPanel24.setBounds(10, 270, 520, 70);

        btnPasienSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPasienSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienSimpanActionPerformed(evt);
            }
        });
        pnlPasien.add(btnPasienSimpan);
        btnPasienSimpan.setBounds(960, 280, 80, 40);

        btnPasienEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnPasienEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienEditActionPerformed(evt);
            }
        });
        pnlPasien.add(btnPasienEdit);
        btnPasienEdit.setBounds(860, 280, 80, 40);

        getContentPane().add(pnlPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlOperator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlOperator.setBackground(new Color(0,0,0,20));
        pnlOperator.setLayout(null);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_operator.png"))); // NOI18N
        pnlOperator.add(jLabel28);
        jLabel28.setBounds(0, 10, 1060, 20);

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

        pnlOperator.add(jScrollPane9);
        jScrollPane9.setBounds(10, 60, 1030, 380);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setLayout(null);
        jPanel10.add(txt_op_nama);
        txt_op_nama.setBounds(140, 13, 350, 20);

        txt_admin_operator_unit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_admin_operator_unitMouseClicked(evt);
            }
        });
        jPanel10.add(txt_admin_operator_unit);
        txt_admin_operator_unit.setBounds(140, 40, 350, 20);
        jPanel10.add(txt_op_uname);
        txt_op_uname.setBounds(140, 70, 350, 20);
        jPanel10.add(txt_op_pass);
        txt_op_pass.setBounds(140, 100, 350, 20);

        cb_admin_operator_role.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "ADMIN", "OPERATOR" }));
        jPanel10.add(cb_admin_operator_role);
        cb_admin_operator_role.setBounds(140, 130, 350, 20);

        jLabel49.setText("NAMA");
        jPanel10.add(jLabel49);
        jLabel49.setBounds(30, 16, 90, 14);

        jLabel27.setText("UNIT");
        jPanel10.add(jLabel27);
        jLabel27.setBounds(30, 43, 90, 14);

        jLabel50.setText("USERNAME");
        jPanel10.add(jLabel50);
        jLabel50.setBounds(30, 70, 90, 14);

        jLabel51.setText("PASSWORD");
        jPanel10.add(jLabel51);
        jLabel51.setBounds(30, 100, 90, 14);

        jLabel31.setText("ROLE");
        jPanel10.add(jLabel31);
        jLabel31.setBounds(30, 130, 90, 14);

        pnlOperator.add(jPanel10);
        jPanel10.setBounds(10, 450, 700, 170);

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_op.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btn_tambah_op.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_tambah_op.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_opActionPerformed(evt);
            }
        });
        jPanel11.add(btn_tambah_op, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        btn_clear_op.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btn_clear_op.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_opActionPerformed(evt);
            }
        });
        jPanel11.add(btn_clear_op, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 100, 40));

        pnlOperator.add(jPanel11);
        jPanel11.setBounds(800, 560, 240, 60);

        getContentPane().add(pnlOperator, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlUnit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlUnit.setBackground(new Color(0,0,0,20));
        pnlUnit.setLayout(null);

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

        pnlUnit.add(jScrollPane4);
        jScrollPane4.setBounds(10, 60, 1030, 440);

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setLayout(null);
        jPanel8.add(txt_unit_nama);
        txt_unit_nama.setBounds(140, 10, 350, 25);
        jPanel8.add(txt_unit_bobot);
        txt_unit_bobot.setBounds(140, 40, 350, 25);

        cb_unit_tipe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "LOKET_PENDAFTARAN", "LOKET_PEMBAYARAN", "POLIKLINIK", "RUANG_PERAWATAN", "FARMASI", "LABORATORIUM", "RADIOLOGI", "TRANSFUSI_DARAH", "UGD", "UNIT_LAIN" }));
        jPanel8.add(cb_unit_tipe);
        cb_unit_tipe.setBounds(140, 70, 350, 25);

        jLabel29.setText("NAMA");
        jPanel8.add(jLabel29);
        jLabel29.setBounds(30, 16, 90, 14);

        jLabel30.setText("BOBOT");
        jPanel8.add(jLabel30);
        jLabel30.setBounds(30, 40, 90, 14);

        jLabel52.setText("TIPE");
        jPanel8.add(jLabel52);
        jLabel52.setBounds(30, 70, 90, 14);

        pnlUnit.add(jPanel8);
        jPanel8.setBounds(10, 510, 510, 110);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_simpan_unit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btn_simpan_unit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_simpan_unit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpan_unitActionPerformed(evt);
            }
        });
        jPanel9.add(btn_simpan_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        btn_clear_unit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btn_clear_unit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_unitActionPerformed(evt);
            }
        });
        jPanel9.add(btn_clear_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 100, 40));

        pnlUnit.add(jPanel9);
        jPanel9.setBounds(800, 560, 240, 60);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_unit.png"))); // NOI18N
        pnlUnit.add(jLabel26);
        jLabel26.setBounds(0, 0, 1060, 30);

        getContentPane().add(pnlUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlBarang.setBackground(new Color(0,0,0,20));
        pnlBarang.setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_barang.png"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlBarang.add(jLabel1);
        jLabel1.setBounds(260, 0, 210, 30);

        tabBhp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabBhpMouseClicked(evt);
            }
        });

        jPanel5.setLayout(null);

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

        jPanel5.add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 1015, 320);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);
        jPanel1.add(txtObatKode);
        txtObatKode.setBounds(140, 20, 350, 20);
        jPanel1.add(txtObatNama);
        txtObatNama.setBounds(140, 50, 350, 20);
        jPanel1.add(txtObatHarga);
        txtObatHarga.setBounds(140, 80, 350, 20);

        txtObatKeterangan.setToolTipText("");
        jPanel1.add(txtObatKeterangan);
        txtObatKeterangan.setBounds(140, 110, 350, 20);

        cbObatTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel1.add(cbObatTanggungan);
        cbObatTanggungan.setBounds(640, 20, 350, 20);

        txtObatJumlah.setToolTipText("");
        jPanel1.add(txtObatJumlah);
        txtObatJumlah.setBounds(640, 50, 350, 20);

        txtObatSatuan.setToolTipText("");
        jPanel1.add(txtObatSatuan);
        txtObatSatuan.setBounds(640, 80, 350, 20);

        jLabel2.setText("KODE");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(30, 20, 90, 14);

        jLabel4.setText("NAMA");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(30, 50, 90, 14);

        jLabel7.setText("HARGA");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(30, 80, 90, 14);

        jLabel8.setText("KETERANGAN");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(30, 110, 90, 14);

        jLabel84.setText("TANGGUNGAN");
        jPanel1.add(jLabel84);
        jLabel84.setBounds(540, 20, 90, 14);

        jLabel5.setText("JUMLAH");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(540, 50, 90, 14);

        jLabel6.setText("SATUAN");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(540, 80, 90, 14);

        jPanel5.add(jPanel1);
        jPanel1.setBounds(10, 408, 1010, 143);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel2.setLayout(null);

        jLabel3.setText("KATA KUNCI");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(30, 20, 90, 14);

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
        jPanel2.add(txtObatKeyword);
        txtObatKeyword.setBounds(140, 20, 350, 20);

        jPanel5.add(jPanel2);
        jPanel2.setBounds(10, 340, 510, 60);

        btnTambahObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTambahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahObatActionPerformed(evt);
            }
        });
        jPanel5.add(btnTambahObat);
        btnTambahObat.setBounds(940, 350, 80, 40);

        btnClearObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnClearObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearObatActionPerformed(evt);
            }
        });
        jPanel5.add(btnClearObat);
        btnClearObat.setBounds(850, 350, 80, 40);

        tabBhp.addTab("OBAT", jPanel5);

        jPanel12.setLayout(null);

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

        jPanel12.add(jScrollPane10);
        jScrollPane10.setBounds(10, 0, 1010, 350);

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setLayout(null);

        jLabel36.setText("KODE");
        jPanel13.add(jLabel36);
        jLabel36.setBounds(30, 20, 90, 14);

        jLabel37.setText("NAMA");
        jPanel13.add(jLabel37);
        jLabel37.setBounds(30, 50, 90, 14);

        jLabel40.setText("HARGA");
        jPanel13.add(jLabel40);
        jLabel40.setBounds(30, 80, 90, 14);

        jLabel41.setText("TANGGUNGAN");
        jPanel13.add(jLabel41);
        jLabel41.setBounds(540, 20, 90, 14);

        jLabel38.setText("JUMLAH");
        jPanel13.add(jLabel38);
        jLabel38.setBounds(540, 50, 90, 14);

        jLabel39.setText("SATUAN");
        jPanel13.add(jLabel39);
        jLabel39.setBounds(540, 80, 90, 14);
        jPanel13.add(txtBhpKode);
        txtBhpKode.setBounds(140, 20, 350, 20);
        jPanel13.add(txtBhpNama);
        txtBhpNama.setBounds(140, 50, 350, 20);
        jPanel13.add(txtBhpHarga);
        txtBhpHarga.setBounds(140, 80, 350, 20);

        cbBhpTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel13.add(cbBhpTanggungan);
        cbBhpTanggungan.setBounds(640, 20, 350, 20);

        txtBhpJumlah.setToolTipText("");
        jPanel13.add(txtBhpJumlah);
        txtBhpJumlah.setBounds(640, 50, 350, 20);

        txtBhpSatuan.setToolTipText("");
        jPanel13.add(txtBhpSatuan);
        txtBhpSatuan.setBounds(640, 80, 350, 20);

        jPanel12.add(jPanel13);
        jPanel13.setBounds(10, 430, 1015, 120);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel17.setLayout(null);

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
        jPanel17.add(txtBhpKeyword);
        txtBhpKeyword.setBounds(140, 20, 350, 20);

        jLabel83.setText("KATA KUNCI");
        jPanel17.add(jLabel83);
        jLabel83.setBounds(30, 20, 80, 14);

        jPanel12.add(jPanel17);
        jPanel17.setBounds(10, 360, 500, 60);

        btnTambahBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTambahBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBhpActionPerformed(evt);
            }
        });
        jPanel12.add(btnTambahBhp);
        btnTambahBhp.setBounds(930, 370, 80, 40);

        btnClearBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnClearBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearBhpActionPerformed(evt);
            }
        });
        jPanel12.add(btnClearBhp);
        btnClearBhp.setBounds(840, 370, 80, 40);

        tabBhp.addTab("BARANG HABIS PAKAI", jPanel12);

        pnlBarang.add(tabBhp);
        tabBhp.setBounds(10, 30, 1040, 590);

        getContentPane().add(pnlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlPegawai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPegawai.setBackground(new Color(0,0,0,20));
        pnlPegawai.setLayout(null);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_pegawai.png"))); // NOI18N
        jLabel25.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlPegawai.add(jLabel25);
        jLabel25.setBounds(0, 10, 1060, 20);

        tab_pane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_paneMouseClicked(evt);
            }
        });

        tab_dokter.setLayout(null);

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

        tab_dokter.add(jScrollPane5);
        jScrollPane5.setBounds(10, 30, 1010, 250);

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_simpan_dokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btn_simpan_dokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpan_dokterActionPerformed(evt);
            }
        });
        jPanel14.add(btn_simpan_dokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 80, 40));

        btn_clear_dokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btn_clear_dokter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_clear_dokterMouseClicked(evt);
            }
        });
        jPanel14.add(btn_clear_dokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        tab_dokter.add(jPanel14);
        jPanel14.setBounds(820, 480, 200, 60);

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel21.setLayout(null);

        jLabel47.setText("NIP");
        jPanel21.add(jLabel47);
        jLabel47.setBounds(30, 80, 90, 14);

        jLabel48.setText("NAMA");
        jPanel21.add(jLabel48);
        jLabel48.setBounds(30, 50, 90, 14);

        jLabel53.setText("KODE");
        jPanel21.add(jLabel53);
        jLabel53.setBounds(30, 20, 90, 14);

        jLabel54.setText("NIK");
        jPanel21.add(jLabel54);
        jLabel54.setBounds(30, 110, 90, 10);

        jLabel56.setText("TGL LAHIR");
        jPanel21.add(jLabel56);
        jLabel56.setBounds(30, 140, 90, 10);

        jLabel55.setText("KELAMIN");
        jPanel21.add(jLabel55);
        jLabel55.setBounds(540, 20, 90, 10);

        jLabel57.setText("DARAH");
        jPanel21.add(jLabel57);
        jLabel57.setBounds(540, 50, 90, 14);

        jLabel58.setText("AGAMA");
        jPanel21.add(jLabel58);
        jLabel58.setBounds(540, 80, 90, 14);

        jLabel59.setText("TELEPON");
        jPanel21.add(jLabel59);
        jLabel59.setBounds(540, 110, 90, 14);
        jPanel21.add(txt_dokter_kode);
        txt_dokter_kode.setBounds(140, 20, 350, 20);
        jPanel21.add(txt_dokter_nama);
        txt_dokter_nama.setBounds(140, 50, 350, 20);
        jPanel21.add(txt_dokter_nip);
        txt_dokter_nip.setBounds(140, 80, 350, 20);
        jPanel21.add(txt_dokter_nik);
        txt_dokter_nik.setBounds(140, 110, 350, 20);
        jPanel21.add(txt_dokter_lahir);
        txt_dokter_lahir.setBounds(140, 140, 350, 20);

        cb_dokter_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel21.add(cb_dokter_kelamin);
        cb_dokter_kelamin.setBounds(640, 20, 350, 20);
        jPanel21.add(txt_dokter_darah);
        txt_dokter_darah.setBounds(640, 50, 350, 20);
        jPanel21.add(txt_dokter_agama);
        txt_dokter_agama.setBounds(640, 80, 350, 20);
        jPanel21.add(txt_dokter_telepon);
        txt_dokter_telepon.setBounds(640, 110, 350, 20);

        tab_dokter.add(jPanel21);
        jPanel21.setBounds(10, 290, 1010, 180);

        tab_pane.addTab("DOKTER", tab_dokter);

        tab_perawat.setLayout(null);

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

        tab_perawat.add(jScrollPane6);
        jScrollPane6.setBounds(10, 30, 1010, 250);

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel15.setLayout(null);

        jLabel33.setText("KODE");
        jPanel15.add(jLabel33);
        jLabel33.setBounds(30, 20, 90, 14);

        jLabel34.setText("NIP");
        jPanel15.add(jLabel34);
        jLabel34.setBounds(30, 80, 90, 14);

        jLabel35.setText("NAMA");
        jPanel15.add(jLabel35);
        jLabel35.setBounds(30, 50, 90, 14);

        jLabel46.setText("NIK");
        jPanel15.add(jLabel46);
        jLabel46.setBounds(30, 110, 90, 14);

        jLabel61.setText("TGL. LAHIR");
        jPanel15.add(jLabel61);
        jLabel61.setBounds(30, 140, 90, 14);

        jLabel60.setText("KELAMIN");
        jPanel15.add(jLabel60);
        jLabel60.setBounds(540, 20, 90, 14);

        jLabel62.setText("DARAH");
        jPanel15.add(jLabel62);
        jLabel62.setBounds(540, 50, 90, 14);

        jLabel63.setText("AGAMA");
        jPanel15.add(jLabel63);
        jLabel63.setBounds(540, 80, 90, 14);

        jLabel64.setText("TELEPON");
        jPanel15.add(jLabel64);
        jLabel64.setBounds(540, 110, 90, 14);
        jPanel15.add(txt_perawat_kode);
        txt_perawat_kode.setBounds(140, 20, 350, 20);
        jPanel15.add(txt_perawat_nama);
        txt_perawat_nama.setBounds(140, 50, 350, 20);
        jPanel15.add(txt_perawat_nip);
        txt_perawat_nip.setBounds(140, 80, 350, 20);
        jPanel15.add(txt_perawat_nik);
        txt_perawat_nik.setBounds(140, 110, 350, 20);
        jPanel15.add(txt_perawat_lahir);
        txt_perawat_lahir.setBounds(140, 140, 350, 20);

        cb_perawat_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel15.add(cb_perawat_kelamin);
        cb_perawat_kelamin.setBounds(640, 20, 350, 20);
        jPanel15.add(txt_perawat_darah);
        txt_perawat_darah.setBounds(640, 50, 350, 20);
        jPanel15.add(txt_perawat_agama);
        txt_perawat_agama.setBounds(640, 80, 350, 20);
        jPanel15.add(txt_perawat_telepon);
        txt_perawat_telepon.setBounds(640, 110, 350, 20);

        tab_perawat.add(jPanel15);
        jPanel15.setBounds(10, 290, 1010, 180);

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_perawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btn_tambah_perawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_perawatActionPerformed(evt);
            }
        });
        jPanel16.add(btn_tambah_perawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 80, 40));

        btn_clear_perawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btn_clear_perawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_perawatActionPerformed(evt);
            }
        });
        jPanel16.add(btn_clear_perawat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        tab_perawat.add(jPanel16);
        jPanel16.setBounds(820, 480, 200, 60);

        tab_pane.addTab("PERAWAT", tab_perawat);

        tab_apoteker.setLayout(null);

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

        tab_apoteker.add(jScrollPane7);
        jScrollPane7.setBounds(10, 30, 1010, 250);

        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_apoteker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btn_tambah_apoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_apotekerActionPerformed(evt);
            }
        });
        jPanel18.add(btn_tambah_apoteker, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 80, 40));

        btn_clear_apoteker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btn_clear_apoteker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_apotekerActionPerformed(evt);
            }
        });
        jPanel18.add(btn_clear_apoteker, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        tab_apoteker.add(jPanel18);
        jPanel18.setBounds(820, 480, 200, 60);

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel22.setLayout(null);

        jLabel65.setText("KODE");
        jPanel22.add(jLabel65);
        jLabel65.setBounds(30, 20, 90, 14);

        jLabel66.setText("NIP");
        jPanel22.add(jLabel66);
        jLabel66.setBounds(30, 80, 90, 14);

        jLabel67.setText("NAMA");
        jPanel22.add(jLabel67);
        jLabel67.setBounds(30, 50, 90, 14);

        jLabel68.setText("NIK");
        jPanel22.add(jLabel68);
        jLabel68.setBounds(30, 110, 90, 14);

        jLabel69.setText("KELAMIN");
        jPanel22.add(jLabel69);
        jLabel69.setBounds(540, 20, 90, 14);

        jLabel70.setText("TGL. LAHIR");
        jPanel22.add(jLabel70);
        jLabel70.setBounds(30, 140, 90, 14);

        jLabel71.setText("DARAH");
        jPanel22.add(jLabel71);
        jLabel71.setBounds(540, 50, 90, 14);

        jLabel72.setText("AGAMA");
        jPanel22.add(jLabel72);
        jLabel72.setBounds(540, 80, 90, 14);

        jLabel73.setText("TELEPON");
        jPanel22.add(jLabel73);
        jLabel73.setBounds(540, 110, 90, 14);
        jPanel22.add(txtApotekerKode);
        txtApotekerKode.setBounds(140, 20, 350, 20);
        jPanel22.add(txtApotekerNama);
        txtApotekerNama.setBounds(140, 50, 350, 20);
        jPanel22.add(txtApotekerNip);
        txtApotekerNip.setBounds(140, 80, 350, 20);
        jPanel22.add(txtApotekerNik);
        txtApotekerNik.setBounds(140, 110, 350, 20);
        jPanel22.add(txtApotekerLahir);
        txtApotekerLahir.setBounds(140, 140, 350, 20);

        cbApotekerKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel22.add(cbApotekerKelamin);
        cbApotekerKelamin.setBounds(640, 20, 350, 20);
        jPanel22.add(txtApotekerDarah);
        txtApotekerDarah.setBounds(640, 50, 350, 20);
        jPanel22.add(txtApotekerAgama);
        txtApotekerAgama.setBounds(640, 80, 350, 20);
        jPanel22.add(txtApotekerTelepon);
        txtApotekerTelepon.setBounds(640, 110, 350, 20);

        tab_apoteker.add(jPanel22);
        jPanel22.setBounds(10, 290, 1010, 180);

        tab_pane.addTab("APOTEKER", tab_apoteker);

        tab_administrasi.setLayout(null);

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

        tab_administrasi.add(jScrollPane8);
        jScrollPane8.setBounds(10, 30, 1010, 250);

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_tambah_adm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btn_tambah_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_admActionPerformed(evt);
            }
        });
        jPanel20.add(btn_tambah_adm, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 80, 40));

        btn_clear_adm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btn_clear_adm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear_admActionPerformed(evt);
            }
        });
        jPanel20.add(btn_clear_adm, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 40));

        tab_administrasi.add(jPanel20);
        jPanel20.setBounds(820, 480, 200, 60);

        jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel23.setLayout(null);

        jLabel74.setText("KODE");
        jPanel23.add(jLabel74);
        jLabel74.setBounds(30, 20, 90, 14);

        jLabel76.setText("NAMA");
        jPanel23.add(jLabel76);
        jLabel76.setBounds(30, 50, 90, 14);

        jLabel75.setText("NIP");
        jPanel23.add(jLabel75);
        jLabel75.setBounds(30, 80, 90, 14);

        jLabel77.setText("NIK");
        jPanel23.add(jLabel77);
        jLabel77.setBounds(30, 110, 90, 14);

        jLabel79.setText("TGL. LAHIR");
        jPanel23.add(jLabel79);
        jLabel79.setBounds(30, 140, 90, 14);

        jLabel78.setText("KELAMIN");
        jPanel23.add(jLabel78);
        jLabel78.setBounds(540, 20, 90, 14);

        jLabel80.setText("DARAH");
        jPanel23.add(jLabel80);
        jLabel80.setBounds(540, 50, 90, 14);

        jLabel81.setText("AGAMA");
        jPanel23.add(jLabel81);
        jLabel81.setBounds(540, 80, 90, 14);

        jLabel82.setText("TELEPON");
        jPanel23.add(jLabel82);
        jLabel82.setBounds(540, 110, 90, 14);
        jPanel23.add(txtPekerjaKode);
        txtPekerjaKode.setBounds(140, 20, 350, 20);
        jPanel23.add(txtPekerjaNama);
        txtPekerjaNama.setBounds(140, 50, 350, 20);
        jPanel23.add(txtPekerjaNip);
        txtPekerjaNip.setBounds(140, 80, 350, 20);
        jPanel23.add(txtPekerjaNik);
        txtPekerjaNik.setBounds(140, 110, 350, 20);
        jPanel23.add(txtPekerjaLahir);
        txtPekerjaLahir.setBounds(140, 140, 350, 20);

        cbPekerjaKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel23.add(cbPekerjaKelamin);
        cbPekerjaKelamin.setBounds(640, 20, 350, 20);
        jPanel23.add(txtPekerjaDarah);
        txtPekerjaDarah.setBounds(640, 50, 350, 20);
        jPanel23.add(txtPekerjaAgama);
        txtPekerjaAgama.setBounds(640, 80, 350, 20);
        jPanel23.add(txtPekerjaTelepon);
        txtPekerjaTelepon.setBounds(640, 110, 350, 20);

        tab_administrasi.add(jPanel23);
        jPanel23.setBounds(10, 290, 1010, 180);

        tab_pane.addTab("ADMINSTRASI", tab_administrasi);

        pnlPegawai.add(tab_pane);
        tab_pane.setBounds(10, 40, 1040, 580);

        getContentPane().add(pnlPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlTindakan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlTindakan.setBackground(new Color(0,0,0,20));
        pnlTindakan.setLayout(null);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_tindakan.png"))); // NOI18N
        pnlTindakan.add(jLabel19);
        jLabel19.setBounds(0, 10, 1060, 20);

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

        pnlTindakan.add(jScrollPane3);
        jScrollPane3.setBounds(10, 40, 1030, 340);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);
        jPanel6.add(txtTindakanKode);
        txtTindakanKode.setBounds(140, 20, 350, 20);
        jPanel6.add(txtTindakanNama);
        txtTindakanNama.setBounds(140, 50, 350, 20);

        txtTindakanKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTindakanKategoriMouseClicked(evt);
            }
        });
        jPanel6.add(txtTindakanKategori);
        txtTindakanKategori.setBounds(140, 80, 350, 20);
        jPanel6.add(txtTindakanKeterangan);
        txtTindakanKeterangan.setBounds(140, 110, 350, 20);

        cbTindakanKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "NONE", "VVIP", "VIP", "I", "II", "III", "ICU" }));
        jPanel6.add(cbTindakanKelas);
        cbTindakanKelas.setBounds(640, 20, 350, 20);

        cbTindakanTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel6.add(cbTindakanTanggungan);
        cbTindakanTanggungan.setBounds(640, 50, 350, 20);

        cbTindakanSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "TINDAKAN", "HARI", "JAM" }));
        jPanel6.add(cbTindakanSatuan);
        cbTindakanSatuan.setBounds(640, 80, 350, 20);
        jPanel6.add(txtTindakanTarif);
        txtTindakanTarif.setBounds(640, 110, 350, 20);

        jLabel21.setText("KODE");
        jPanel6.add(jLabel21);
        jLabel21.setBounds(30, 20, 90, 14);

        jLabel22.setText("NAMA");
        jPanel6.add(jLabel22);
        jLabel22.setBounds(30, 50, 90, 14);

        jLabel23.setText("TARIF");
        jPanel6.add(jLabel23);
        jLabel23.setBounds(540, 110, 90, 14);

        jLabel24.setText("KETERANGAN");
        jPanel6.add(jLabel24);
        jLabel24.setBounds(30, 110, 90, 14);

        jLabel32.setText("KATEGORI");
        jPanel6.add(jLabel32);
        jLabel32.setBounds(30, 80, 90, 14);

        jLabel85.setText("TANGGUNGAN");
        jPanel6.add(jLabel85);
        jLabel85.setBounds(540, 50, 90, 14);

        jLabel86.setText("KELAS");
        jPanel6.add(jLabel86);
        jLabel86.setBounds(540, 20, 90, 14);

        jLabel87.setText("SATUAN");
        jPanel6.add(jLabel87);
        jLabel87.setBounds(540, 80, 90, 14);

        pnlTindakan.add(jPanel6);
        jPanel6.setBounds(20, 460, 1020, 150);

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(null);

        jLabel20.setText("CARI");
        jPanel7.add(jLabel20);
        jLabel20.setBounds(30, 20, 70, 14);

        txtTindakanKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTindakanKeywordFocusLost(evt);
            }
        });
        txtTindakanKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTindakanKeywordKeyTyped(evt);
            }
        });
        jPanel7.add(txtTindakanKeyword);
        txtTindakanKeyword.setBounds(140, 20, 350, 20);

        pnlTindakan.add(jPanel7);
        jPanel7.setBounds(20, 390, 510, 60);

        btnSimpanTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnSimpanTindakan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimpanTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanTindakanMouseClicked(evt);
            }
        });
        pnlTindakan.add(btnSimpanTindakan);
        btnSimpanTindakan.setBounds(860, 400, 80, 40);

        btnResetTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnResetTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetTindakanMouseClicked(evt);
            }
        });
        pnlTindakan.add(btnResetTindakan);
        btnResetTindakan.setBounds(950, 400, 80, 40);

        getContentPane().add(pnlTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlPenduduk.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPenduduk.setBackground(new Color(0,0,0,20));
        pnlPenduduk.setLayout(null);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_rekmed.png"))); // NOI18N
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlPenduduk.add(jLabel9);
        jLabel9.setBounds(0, 10, 1060, 20);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        jLabel11.setText("KODE");
        jPanel3.add(jLabel11);
        jLabel11.setBounds(20, 20, 100, 14);

        jLabel12.setText("NIK");
        jPanel3.add(jLabel12);
        jLabel12.setBounds(20, 50, 100, 14);

        jLabel13.setText("NAMA");
        jPanel3.add(jLabel13);
        jLabel13.setBounds(20, 80, 100, 14);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("KELAMIN");
        jPanel3.add(jLabel14);
        jLabel14.setBounds(20, 110, 100, 14);

        jLabel15.setText("TGL LAHIR");
        jPanel3.add(jLabel15);
        jLabel15.setBounds(550, 20, 110, 14);

        jLabel16.setText("GOL. DARAH");
        jPanel3.add(jLabel16);
        jLabel16.setBounds(550, 50, 110, 14);

        jLabel17.setText("AGAMA");
        jPanel3.add(jLabel17);
        jLabel17.setBounds(550, 80, 110, 14);

        jLabel18.setText("NO TELEPON");
        jPanel3.add(jLabel18);
        jLabel18.setBounds(550, 110, 110, 14);
        jPanel3.add(txtPendudukKode);
        txtPendudukKode.setBounds(130, 20, 350, 20);
        jPanel3.add(txtPendudukNik);
        txtPendudukNik.setBounds(130, 50, 350, 20);
        jPanel3.add(txtPendudukNama);
        txtPendudukNama.setBounds(130, 80, 350, 20);

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel3.add(cbPendudukKelamin);
        cbPendudukKelamin.setBounds(130, 110, 350, 20);
        jPanel3.add(txtPendudukLahir);
        txtPendudukLahir.setBounds(670, 20, 350, 20);
        jPanel3.add(txtPendudukDarah);
        txtPendudukDarah.setBounds(670, 50, 350, 20);
        jPanel3.add(txtPendudukAgama);
        txtPendudukAgama.setBounds(670, 80, 350, 20);
        jPanel3.add(txtPendudukTelepon);
        txtPendudukTelepon.setBounds(670, 110, 350, 20);

        pnlPenduduk.add(jPanel3);
        jPanel3.setBounds(10, 470, 1040, 150);

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

        pnlPenduduk.add(jScrollPane2);
        jScrollPane2.setBounds(10, 40, 1040, 340);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel4.setLayout(null);

        jLabel10.setText("KATA KUNCI");
        jPanel4.add(jLabel10);
        jLabel10.setBounds(20, 30, 60, 14);

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
        jPanel4.add(txtPendudukKeyword);
        txtPendudukKeyword.setBounds(130, 30, 350, 20);

        pnlPenduduk.add(jPanel4);
        jPanel4.setBounds(10, 390, 530, 70);

        btnPendudukSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPendudukSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukSimpanActionPerformed(evt);
            }
        });
        pnlPenduduk.add(btnPendudukSimpan);
        btnPendudukSimpan.setBounds(960, 400, 80, 40);

        btnPendudukEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnPendudukEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukEditActionPerformed(evt);
            }
        });
        pnlPenduduk.add(btnPendudukEdit);
        btnPendudukEdit.setBounds(860, 400, 80, 40);

        getContentPane().add(pnlPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        btnUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/UnitIcon.png"))); // NOI18N
        btnUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitActionPerformed(evt);
            }
        });
        getContentPane().add(btnUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 130, 130, 51));

        btnOperator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/OperatorIcon.png"))); // NOI18N
        btnOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorActionPerformed(evt);
            }
        });
        getContentPane().add(btnOperator, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 190, 130, 51));

        btnPegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/PegawaiIcon.png"))); // NOI18N
        btnPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPegawaiActionPerformed(evt);
            }
        });
        getContentPane().add(btnPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 250, 130, 51));

        btnBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/BarangIcon.png"))); // NOI18N
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });
        getContentPane().add(btnBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 310, 130, 51));
        btnBarang.getAccessibleContext().setAccessibleName("BHP");

        btnTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/TindakanIcon.png"))); // NOI18N
        btnTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanActionPerformed(evt);
            }
        });
        getContentPane().add(btnTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 370, 130, 51));

        btnPenduduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/RekamMedikIcon.png"))); // NOI18N
        btnPenduduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukActionPerformed(evt);
            }
        });
        getContentPane().add(btnPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 430, 130, 51));

        btnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/RekamMedikIcon.png"))); // NOI18N
        btnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienActionPerformed(evt);
            }
        });
        getContentPane().add(btnPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 490, 130, 51));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel89.setText("ANDA LOGIN SEBAGAI : ");
        jToolBar1.add(jLabel89);

        lbl_status.setText("status");
        jToolBar1.add(lbl_status);
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

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 770, 1270, 30));

        jLabel90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/Admin_Bg.jpg"))); // NOI18N
        getContentPane().add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 800));

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
    
    private void btn_simpan_unitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpan_unitActionPerformed
        try {
            unitEventController.onSave();
        } catch (ServiceException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage());
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

    private void txtPendudukKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPendudukKeywordFocusLost
        try {
            pendudukEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtPendudukKeywordFocusLost

    private void btnPendudukEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukEditActionPerformed
        pendudukEventController.onCleanForm();
    }//GEN-LAST:event_btnPendudukEditActionPerformed

    private void btnPendudukSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukSimpanActionPerformed
        try {
            pendudukEventController.onSave();
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
        }
    }//GEN-LAST:event_txtObatKeywordFocusLost

    private void tblObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblObatMouseClicked
        try {
            obatEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblObatMouseClicked

    private void btnTambahObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahObatActionPerformed
        try {
            obatEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
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
        }
    }//GEN-LAST:event_txtBhpKeywordFocusLost

    private void btnTambahBhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBhpActionPerformed
        try {
            bhpEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTambahBhpActionPerformed

    private void btnClearBhpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearBhpActionPerformed
        bhpEventController.onCleanForm();
    }//GEN-LAST:event_btnClearBhpActionPerformed

    private void tabBhpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabBhpMouseClicked
        int index = tabBhp.getSelectedIndex();
        
        try {
            switch(index) {
                case 0: obatEventController.onLoad();
                    break;
                case 1: bhpEventController.onLoad();
                    break;
                default: break;
            }
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tabBhpMouseClicked

    private void txtBhpKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBhpKeywordKeyPressed
        int i = evt.getKeyCode();
        if (i == 10) {
            txtBhpKode.requestFocus();
        }
    }//GEN-LAST:event_txtBhpKeywordKeyPressed

    private void txtObatKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObatKeywordKeyPressed
        int i = evt.getKeyCode();
        if (i == 10) {
            txtObatKode.requestFocus();
        }
    }//GEN-LAST:event_txtObatKeywordKeyPressed

    private void txtPendudukKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPendudukKeywordKeyPressed
        int i = evt.getKeyCode();
        if (i == 10) {
            txtPendudukKode.requestFocus();
        }
    }//GEN-LAST:event_txtPendudukKeywordKeyPressed

    private void btnSimpanTindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanTindakanMouseClicked
        try {
            tindakanEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanTindakanMouseClicked

    private void btnResetTindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetTindakanMouseClicked
        tindakanEventController.onCleanForm();
    }//GEN-LAST:event_btnResetTindakanMouseClicked

    private void txtTindakanKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTindakanKeywordFocusLost
        try {
            tindakanEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_txtTindakanKeywordFocusLost

    private void txtTindakanKeywordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTindakanKeywordKeyTyped
        int i = evt.getKeyCode();
        if (i == 10) {
            txtTindakanKode.requestFocus();
        }
    }//GEN-LAST:event_txtTindakanKeywordKeyTyped

    private void tblTindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTindakanMouseClicked
        try {
            tindakanEventController.onTableClick();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_tblTindakanMouseClicked

    private void txtTindakanKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTindakanKategoriMouseClicked
        FrameCari cari = new FrameCari(this,KategoriTindakan.class);
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
            
            new FrameLogin().setVisible(true);
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
        }
    }//GEN-LAST:event_txtPasienKeywordFocusLost

    private void btnPasienSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienSimpanActionPerformed
        try {
            pasienEventController.onSave();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPasienSimpanActionPerformed

    private void btnPasienEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienEditActionPerformed
        pasienEventController.onCleanForm();
    }//GEN-LAST:event_btnPasienEditActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBarang;
    private javax.swing.JButton btnClearBhp;
    private javax.swing.JButton btnClearObat;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOperator;
    private javax.swing.JButton btnPasien;
    private javax.swing.JButton btnPasienEdit;
    private javax.swing.JButton btnPasienSimpan;
    private javax.swing.JButton btnPegawai;
    private javax.swing.JButton btnPenduduk;
    private javax.swing.JButton btnPendudukEdit;
    private javax.swing.JButton btnPendudukSimpan;
    private javax.swing.JButton btnResetTindakan;
    private javax.swing.JButton btnSimpanTindakan;
    private javax.swing.JButton btnTambahBhp;
    private javax.swing.JButton btnTambahObat;
    private javax.swing.JButton btnTindakan;
    private javax.swing.JButton btnUnit;
    private javax.swing.JButton btn_clear_adm;
    private javax.swing.JButton btn_clear_apoteker;
    private javax.swing.JButton btn_clear_dokter;
    private javax.swing.JButton btn_clear_op;
    private javax.swing.JButton btn_clear_perawat;
    private javax.swing.JButton btn_clear_unit;
    private javax.swing.JButton btn_simpan_dokter;
    private javax.swing.JButton btn_simpan_unit;
    private javax.swing.JButton btn_tambah_adm;
    private javax.swing.JButton btn_tambah_apoteker;
    private javax.swing.JButton btn_tambah_op;
    private javax.swing.JButton btn_tambah_perawat;
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
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
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
    private javax.swing.JLabel lbl_status;
    private javax.swing.JPanel pnlBarang;
    private javax.swing.JPanel pnlOperator;
    private javax.swing.JPanel pnlPasien;
    private javax.swing.JPanel pnlPegawai;
    private javax.swing.JPanel pnlPenduduk;
    private javax.swing.JPanel pnlTindakan;
    private javax.swing.JPanel pnlUnit;
    private javax.swing.JTabbedPane tabBhp;
    private javax.swing.JPanel tab_administrasi;
    private javax.swing.JPanel tab_apoteker;
    private javax.swing.JPanel tab_dokter;
    private javax.swing.JTabbedPane tab_pane;
    private javax.swing.JPanel tab_perawat;
    private javax.swing.JTable tblBhp;
    private javax.swing.JTable tblObat;
    private javax.swing.JTable tblPasien;
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
    private javax.swing.JTextField txtApotekerLahir;
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
    private javax.swing.JTextField txtObatHarga;
    private javax.swing.JTextField txtObatJumlah;
    private javax.swing.JTextField txtObatKeterangan;
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
    private javax.swing.JTextField txtPekerjaLahir;
    private javax.swing.JTextField txtPekerjaNama;
    private javax.swing.JTextField txtPekerjaNik;
    private javax.swing.JTextField txtPekerjaNip;
    private javax.swing.JTextField txtPekerjaTelepon;
    private javax.swing.JTextField txtPendudukAgama;
    private javax.swing.JTextField txtPendudukDarah;
    private javax.swing.JTextField txtPendudukKeyword;
    private javax.swing.JTextField txtPendudukKode;
    private javax.swing.JTextField txtPendudukLahir;
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
    private javax.swing.JTextField txt_unit_bobot;
    private javax.swing.JTextField txt_unit_nama;
    // End of variables declaration//GEN-END:variables
}
