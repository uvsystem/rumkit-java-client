package com.dbsys.rs.client;

import static com.dbsys.rs.client.EventController.host;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.client.tableModel.ApotekerTableModel;
import com.dbsys.rs.client.tableModel.BhpTableModel;
import com.dbsys.rs.client.tableModel.DokterTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.OperatorTableModel;
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
import com.dbsys.rs.lib.entity.Pekerja;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.dbsys.rs.lib.entity.Perawat;
import com.dbsys.rs.lib.entity.Tindakan;
import com.dbsys.rs.lib.entity.Unit;
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

           JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/kode rekam medik");
         }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtPendudukKeyword.getText();
            
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
            JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/kode obat");
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtObatKeyword.getText();
            
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

            JOptionPane.showMessageDialog(null, "Silahkan cari menggunakan nama/kode tindakan");
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public void onSearch() throws ServiceException {
            onCleanForm();
            String keyword = txtTindakanKeyword.getText();
            
            List<Tindakan> list = tindakanService.cari(keyword);
            TindakanTableModel tableModel = new TindakanTableModel(list);
            tblTindakan.setModel(tableModel);
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

        pnlBarang = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabBhp = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtObatKode = new javax.swing.JTextField();
        txtObatNama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtObatSatuan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtObatHarga = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtObatKeterangan = new javax.swing.JTextField();
        txtObatJumlah = new javax.swing.JTextField();
        cbObatTanggungan = new javax.swing.JComboBox();
        jLabel84 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnTambahObat = new javax.swing.JButton();
        btnClearObat = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtObatKeyword = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblBhp = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        txtBhpKode = new javax.swing.JTextField();
        txtBhpNama = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtBhpSatuan = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txtBhpHarga = new javax.swing.JTextField();
        txtBhpJumlah = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        cbBhpTanggungan = new javax.swing.JComboBox();
        jPanel17 = new javax.swing.JPanel();
        btnTambahBhp = new javax.swing.JButton();
        btnClearBhp = new javax.swing.JButton();
        txtBhpKeyword = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        pnlOperator = new javax.swing.JPanel();
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
        pnlUnit = new javax.swing.JPanel();
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
        pnlPegawai = new javax.swing.JPanel();
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
        txtPendudukTelepon = new javax.swing.JTextField();
        txtPendudukAgama = new javax.swing.JTextField();
        txtPendudukLahir = new javax.swing.JTextField();
        txtPendudukDarah = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPenduduk = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnPendudukEdit = new javax.swing.JButton();
        btnPendudukSimpan = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtPendudukKeyword = new javax.swing.JTextField();
        pnlTindakan = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTindakan = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        txtTindakanKode = new javax.swing.JTextField();
        txtTindakanNama = new javax.swing.JTextField();
        txtTindakanTarif = new javax.swing.JTextField();
        txtTindakanKeterangan = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtTindakanKategori = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        cbTindakanTanggungan = new javax.swing.JComboBox();
        jLabel86 = new javax.swing.JLabel();
        cbTindakanKelas = new javax.swing.JComboBox();
        jLabel87 = new javax.swing.JLabel();
        cbTindakanSatuan = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        btnSimpanTindakan = new javax.swing.JButton();
        btnResetTindakan = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txtTindakanKeyword = new javax.swing.JTextField();
        pnlMenu = new javax.swing.JPanel();
        btnBarang = new javax.swing.JButton();
        btnUnit = new javax.swing.JButton();
        btnPenduduk = new javax.swing.JButton();
        btnPegawai = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnTindakan = new javax.swing.JButton();
        btnOperator = new javax.swing.JButton();
        lbl_status = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rumah Sakit Liun Kendage Tahuna");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setName("ADMIN"); // NOI18N
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("BARANG");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlBarang.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 20));

        tabBhp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabBhpMouseClicked(evt);
            }
        });

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

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("KODE");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));
        jPanel1.add(txtObatKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 200, -1));
        jPanel1.add(txtObatNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 200, -1));

        jLabel4.setText("NAMA");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jLabel5.setText("JUMLAH");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));

        txtObatSatuan.setToolTipText("");
        jPanel1.add(txtObatSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 200, -1));

        jLabel6.setText("SATUAN");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, -1, -1));

        jLabel7.setText("HARGA");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));
        jPanel1.add(txtObatHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 200, -1));

        jLabel8.setText("KETERANGAN");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        txtObatKeterangan.setToolTipText("");
        jPanel1.add(txtObatKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 200, -1));

        txtObatJumlah.setToolTipText("");
        jPanel1.add(txtObatJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 200, -1));

        cbObatTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel1.add(cbObatTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 200, -1));

        jLabel84.setText("TANGGUNGAN");
        jPanel1.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTambahObat.setText("+ BARANG");
        btnTambahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahObatActionPerformed(evt);
            }
        });
        jPanel2.add(btnTambahObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, 40));

        btnClearObat.setText("X Fields");
        btnClearObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearObatActionPerformed(evt);
            }
        });
        jPanel2.add(btnClearObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, 40));

        jLabel3.setText("CARI");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

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
        jPanel2.add(txtObatKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 200, -1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabBhp.addTab("OBAT", jPanel5);

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

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setText("KODE");
        jPanel13.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));
        jPanel13.add(txtBhpKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 200, -1));
        jPanel13.add(txtBhpNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 200, -1));

        jLabel37.setText("NAMA");
        jPanel13.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jLabel38.setText("JUMLAH");
        jPanel13.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));

        txtBhpSatuan.setToolTipText("");
        jPanel13.add(txtBhpSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 200, -1));

        jLabel39.setText("SATUAN");
        jPanel13.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, -1, -1));

        jLabel40.setText("HARGA");
        jPanel13.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));
        jPanel13.add(txtBhpHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 200, -1));

        txtBhpJumlah.setToolTipText("");
        jPanel13.add(txtBhpJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 200, -1));

        jLabel41.setText("TANGGUNGAN");
        jPanel13.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        cbBhpTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel13.add(cbBhpTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 200, -1));

        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTambahBhp.setText("+ BARANG");
        btnTambahBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBhpActionPerformed(evt);
            }
        });
        jPanel17.add(btnTambahBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, 40));

        btnClearBhp.setText("X Fields");
        btnClearBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearBhpActionPerformed(evt);
            }
        });
        jPanel17.add(btnClearBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, 40));

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
        jPanel17.add(txtBhpKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 200, -1));

        jLabel83.setText("CARI");
        jPanel17.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabBhp.addTab("BARANG HABIS PAKAI", jPanel12);

        pnlBarang.add(tabBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 700, 500));

        getContentPane().add(pnlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnlOperator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlOperator.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("OPERATOR");
        pnlOperator.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, -1));

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

        pnlOperator.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 700, 270));

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

        pnlOperator.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 700, 110));

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

        pnlOperator.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 700, 60));

        getContentPane().add(pnlOperator, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnlUnit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlUnit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("UNIT");
        pnlUnit.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, -1));

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

        pnlUnit.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 700, 270));

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

        pnlUnit.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 700, 80));

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

        pnlUnit.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 700, 60));

        getContentPane().add(pnlUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnlPegawai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("PEGAWAI");
        jLabel25.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlPegawai.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, 20));

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

        pnlPegawai.add(tab_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 700, 490));

        getContentPane().add(pnlPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 720, 540));

        pnlPenduduk.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPenduduk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("REKAM MEDIK");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlPenduduk.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, 20));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("KODE");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jLabel12.setText("NIK");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        jLabel13.setText("NAMA");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("KELAMIN");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));

        jLabel15.setText("TGL LAHIR");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        jLabel16.setText("GOL. DARAH");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));

        jLabel17.setText("AGAMA");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, -1, -1));

        jLabel18.setText("NO TELEPON");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, -1, -1));
        jPanel3.add(txtPendudukKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 200, -1));
        jPanel3.add(txtPendudukNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 200, -1));
        jPanel3.add(txtPendudukNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 200, -1));

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel3.add(cbPendudukKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 200, -1));
        jPanel3.add(txtPendudukTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, 200, -1));
        jPanel3.add(txtPendudukAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 200, -1));
        jPanel3.add(txtPendudukLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 200, -1));
        jPanel3.add(txtPendudukDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 200, -1));

        pnlPenduduk.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 700, 150));

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

        pnlPenduduk.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 700, 250));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPendudukEdit.setText("CLEAN");
        btnPendudukEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukEditActionPerformed(evt);
            }
        });
        jPanel4.add(btnPendudukEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 80, 40));

        btnPendudukSimpan.setLabel("+ DATA");
        btnPendudukSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukSimpanActionPerformed(evt);
            }
        });
        jPanel4.add(btnPendudukSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 80, 40));

        jLabel10.setText("CARI");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

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
        jPanel4.add(txtPendudukKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 200, -1));

        pnlPenduduk.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 700, 60));

        getContentPane().add(pnlPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnlTindakan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlTindakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("TINDAKAN");
        pnlTindakan.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, -1));

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

        pnlTindakan.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 700, 270));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(txtTindakanKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 200, -1));
        jPanel6.add(txtTindakanNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 200, -1));
        jPanel6.add(txtTindakanTarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 200, -1));
        jPanel6.add(txtTindakanKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 200, -1));

        jLabel21.setText("KODE");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel22.setText("NAMA");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jLabel23.setText("TARIF");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, -1, -1));

        jLabel24.setText("KETERANGAN");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jLabel32.setText("KATEGORI");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        txtTindakanKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTindakanKategoriMouseClicked(evt);
            }
        });
        jPanel6.add(txtTindakanKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 200, -1));

        jLabel85.setText("TANGGUNGAN");
        jPanel6.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, -1, -1));

        cbTindakanTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel6.add(cbTindakanTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 200, -1));

        jLabel86.setText("KELAS");
        jPanel6.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

        cbTindakanKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "NONE", "VVIP", "VIP", "I", "II", "III", "ICU" }));
        jPanel6.add(cbTindakanKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 200, -1));

        jLabel87.setText("SATUAN");
        jPanel6.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, -1, -1));

        cbTindakanSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "TINDAKAN", "HARI", "JAM" }));
        jPanel6.add(cbTindakanSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, 200, -1));

        pnlTindakan.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 700, 130));

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSimpanTindakan.setText("+ TINDAKAN");
        btnSimpanTindakan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimpanTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanTindakanMouseClicked(evt);
            }
        });
        jPanel7.add(btnSimpanTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 100, 40));

        btnResetTindakan.setText("X FIELDS");
        btnResetTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetTindakanMouseClicked(evt);
            }
        });
        jPanel7.add(btnResetTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 80, 40));

        jLabel20.setText("CARI");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

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
        jPanel7.add(txtTindakanKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 200, -1));

        pnlTindakan.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 700, 60));

        getContentPane().add(pnlTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 540));

        pnlMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBarang.setText("BARANG"); // NOI18N
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });
        pnlMenu.add(btnBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 130, 51));
        btnBarang.getAccessibleContext().setAccessibleName("BHP");

        btnUnit.setText("UNIT");
        btnUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitActionPerformed(evt);
            }
        });
        pnlMenu.add(btnUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 130, 51));

        btnPenduduk.setText("REKAM MEDIK");
        btnPenduduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukActionPerformed(evt);
            }
        });
        pnlMenu.add(btnPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 130, 51));

        btnPegawai.setLabel("PEGAWAI");
        btnPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPegawaiActionPerformed(evt);
            }
        });
        pnlMenu.add(btnPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 130, 51));

        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        pnlMenu.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 130, 51));

        btnTindakan.setText("TINDAKAN");
        btnTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTindakanActionPerformed(evt);
            }
        });
        pnlMenu.add(btnTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 130, 51));

        btnOperator.setText("OPERATOR");
        btnOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorActionPerformed(evt);
            }
        });
        pnlMenu.add(btnOperator, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 130, 51));

        getContentPane().add(pnlMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 180, 540));

        lbl_status.setText("status");
        getContentPane().add(lbl_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
        pnlTindakan.setVisible(false);
        pnlUnit.setVisible(false);
        pnlBarang.setVisible(true);
        pnlPenduduk.setVisible(false);
        pnlPegawai.setVisible(false);
        pnlOperator.setVisible(false);
        
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

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            tokenService.lock(TokenHolder.getKode());
            
            new FrameLogin().setVisible(true);
            this.dispose();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnLogoutActionPerformed
    
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBarang;
    private javax.swing.JButton btnClearBhp;
    private javax.swing.JButton btnClearObat;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOperator;
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
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lbl_status;
    private javax.swing.JPanel pnlBarang;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlOperator;
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
