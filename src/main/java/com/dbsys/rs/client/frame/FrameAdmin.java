package com.dbsys.rs.client.frame;

import com.dbsys.rs.client.EventController;
import com.dbsys.rs.client.UnitFrame;
import static com.dbsys.rs.client.EventController.host;
import com.dbsys.rs.client.tableModel.PendudukTableModel;
import com.dbsys.rs.client.tableModel.PegawaiTableModel;
import com.dbsys.rs.client.tableModel.BarangTableModel;
import com.dbsys.rs.client.tableModel.DokterTableModel;
import com.dbsys.rs.client.tableModel.ObatTableModel;
import com.dbsys.rs.client.tableModel.OperatorTableModel;
import com.dbsys.rs.client.tableModel.PasienTableModel;
import com.dbsys.rs.client.tableModel.TindakanTableModel;
import com.dbsys.rs.client.tableModel.UnitTableModel;
import com.dbsys.rs.connector.ServiceException;
import com.dbsys.rs.connector.TokenHolder;
import com.dbsys.rs.connector.service.BarangService;
import com.dbsys.rs.connector.service.OperatorService;
import com.dbsys.rs.connector.service.PasienService;
import com.dbsys.rs.connector.service.PegawaiServices;
import com.dbsys.rs.connector.service.PendudukService;
import com.dbsys.rs.connector.service.TindakanService;
import com.dbsys.rs.connector.service.TokenService;
import com.dbsys.rs.connector.service.UnitService;
import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.Kelas;
import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.Apoteker;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.KategoriTindakan;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import com.dbsys.rs.lib.entity.Operator;
import com.dbsys.rs.lib.entity.Operator.Role;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Pekerja;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.dbsys.rs.lib.entity.Perawat;
import com.dbsys.rs.lib.entity.Tindakan;
import com.dbsys.rs.lib.entity.Unit;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class FrameAdmin extends javax.swing.JFrame implements  UnitFrame {
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
            model.setTipe(Unit.TipeUnit.valueOf(tipe));
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
        private final PegawaiServices dokterService = PegawaiServices.getInstance(host);
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

            List<Pegawai> list = dokterService.getAll(Dokter.class);
            List<Dokter> listDokter = new ArrayList<>();
            for (Pegawai pegawai : list) {
                if (pegawai instanceof Dokter)
                    listDokter.add((Dokter) pegawai);
            }

            DokterTableModel tableModel = new DokterTableModel(listDokter);
            tbl_dokter.setModel(tableModel);
        }

        @Override
        public void onDelete() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class PerawatEventController implements EventController<Perawat> {
        private final PegawaiServices perawatService = PegawaiServices.getInstance(host);
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

            PegawaiTableModel tableModel = (PegawaiTableModel)tbl_perawat.getModel();
            model = (Perawat) tableModel.getPegawai(row);

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
            List<Pegawai> list = perawatService.getAll(Perawat.class);
            PegawaiTableModel tableModel = new PegawaiTableModel(list);
            tbl_perawat.setModel(tableModel);
        }

        @Override
        public void onDelete() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class ApotekerEventController implements EventController<Apoteker> {
        private final PegawaiServices apotekerService = PegawaiServices.getInstance(host);
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

            PegawaiTableModel tableModel = (PegawaiTableModel)tbl_apoteker.getModel();
            model = (Apoteker) tableModel.getPegawai(row);

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
            List<Pegawai> list = apotekerService.getAll(Apoteker.class);
            PegawaiTableModel tableModel = new PegawaiTableModel(list);
            tbl_apoteker.setModel(tableModel);
        }

        @Override
        public void onDelete() throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class PekerjaEventController implements EventController<Pekerja> {
        private final PegawaiServices pekerjaService = PegawaiServices.getInstance(host);
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

            PegawaiTableModel tableModel = (PegawaiTableModel)tbl_adm.getModel();
            model = (Pekerja) tableModel.getPegawai(row);

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
            List<Pegawai> list = pekerjaService.getAll(Pekerja.class);
            PegawaiTableModel tableModel = new PegawaiTableModel(list);
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

            txtPendudukKode.setText(Penduduk.createKode());
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
            model.setSatuan(Tindakan.SatuanTindakan.valueOf(satuan));
            model.setPenanggung(Penanggung.valueOf(tanggungan));
            model.setTarif(Long.valueOf(tarif));
            model.setKode(txtTindakanKode.getText());
            model.setNama(txtTindakanNama.getText());
            model.setKeterangan(txtTindakanKeterangan.getText());
            model.setKategori(kategori);
            
            tindakanService.simpan(model);
        }

        @Override
        public void onTableClick() throws ServiceException {
            int row = tblTindakan.getSelectedRow();

            TindakanTableModel tableModel = (TindakanTableModel)tblTindakan.getModel();
            model = tableModel.getTindakan(row);
            kategori= model.getKategori();

            txtTindakanKode.setText(model.getKode());
            txtTindakanNama.setText(model.getNama());
            txtTindakanKategori.setText(kategori.getNama());
            txtTindakanKeterangan.setText(model.getKeterangan());
            txtTindakanTarif.setText(model.getTarif().toString());
            cbTindakanKelas.setSelectedItem(model.getKelas().toString());
            cbTindakanTanggungan.setSelectedItem(model.getPenanggung().toString());
            cbTindakanSatuan.setSelectedItem(model.getSatuan().toString());
        }

        @Override
        public void onCleanForm() {
            model = null;
            
            txtTindakanKode.setText(Tindakan.createKode());
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
            txtPasienTipe.setText(model.getTipePerawatan() != null ? model.getTipePerawatan().toString() : null);
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
        pnlPasien = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
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
        btnPasienSimpan = new javax.swing.JButton();
        btnPasienEdit = new javax.swing.JButton();
        pnlBarang = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabBarang = new javax.swing.JTabbedPane();
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
        btnTambahBhp = new javax.swing.JButton();
        btnClearBhp = new javax.swing.JButton();
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

        pnlUnit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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

        pnlUnit.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1030, 440));

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setLayout(null);
        jPanel8.add(txt_unit_nama);
        txt_unit_nama.setBounds(140, 10, 350, 25);
        jPanel8.add(txt_unit_bobot);
        txt_unit_bobot.setBounds(140, 40, 350, 25);

        cb_unit_tipe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "LOKET_PENDAFTARAN", "LOKET_PEMBAYARAN", "POLIKLINIK", "RUANG_PERAWATAN", "APOTEK_FARMASI", "GUDANG_FARMASI", "PENUNJANG_MEDIK", "PENUNJANG_NON_MEDIK", "ICU", "UGD" }));
        jPanel8.add(cb_unit_tipe);
        cb_unit_tipe.setBounds(140, 70, 350, 25);

        jLabel29.setText("NAMA");
        jPanel8.add(jLabel29);
        jLabel29.setBounds(30, 10, 90, 14);

        jLabel30.setText("BOBOT");
        jPanel8.add(jLabel30);
        jLabel30.setBounds(30, 40, 90, 14);

        jLabel52.setText("TIPE");
        jPanel8.add(jLabel52);
        jLabel52.setBounds(30, 70, 90, 14);

        pnlUnit.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 510, 110));

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

        pnlUnit.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 560, 240, 60));

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_unit.png"))); // NOI18N
        pnlUnit.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 30));

        getContentPane().add(pnlUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlPasien.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPasien.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("PASIEN");
        jLabel42.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlPasien.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 20));

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

        pnlPasien.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1040, 220));

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("No. Rekam Medik");
        jPanel19.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, -1));

        jLabel44.setText("Nik");
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
        jPanel19.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, -1));

        jLabel98.setText("Tanggal Keluar");
        jPanel19.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, -1));

        jLabel102.setText("Tipe Perawatan");
        jPanel19.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, -1));

        jLabel103.setText("Kelas");
        jPanel19.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 90, -1));

        jLabel104.setText("Keadaan");
        jPanel19.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, 90, -1));

        jLabel101.setText("Status Pelayanan");
        jPanel19.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 90, -1));

        jLabel100.setText("Total Pembayaran");
        jPanel19.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 200, 90, -1));

        txtPasienTanggalMasuk.setEditable(false);
        jPanel19.add(txtPasienTanggalMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));

        txtPasienTanggalKeluar.setEditable(false);
        jPanel19.add(txtPasienTanggalKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));

        txtPasienTipe.setEditable(false);
        jPanel19.add(txtPasienTipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));

        txtPasienKelas.setEditable(false);
        jPanel19.add(txtPasienKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 350, 25));

        cbPasienKeadaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "SEMBUH", "RUJUK", "SAKIT", "MATI", "LARI" }));
        jPanel19.add(cbPasienKeadaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 140, 350, 25));

        cbPasienStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "OPEN", "PAID", "UNPAID" }));
        jPanel19.add(cbPasienStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 170, 350, 25));
        jPanel19.add(txtPasienCicilan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 200, 350, 25));

        pnlPasien.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 1040, 270));

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel96.setText("KATA KUNCI");
        jPanel24.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, -1));

        txtPasienKeyword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasienKeywordFocusLost(evt);
            }
        });
        jPanel24.add(txtPasienKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 350, 25));

        pnlPasien.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 520, 70));

        btnPasienSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPasienSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienSimpanActionPerformed(evt);
            }
        });
        pnlPasien.add(btnPasienSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 50, 80, 40));

        btnPasienEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnPasienEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasienEditActionPerformed(evt);
            }
        });
        pnlPasien.add(btnPasienEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 50, 80, 40));

        getContentPane().add(pnlPasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlBarang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlBarang.setBackground(new Color(0,0,0,20));
        pnlBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_barang.png"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlBarang.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, -1));

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

        pnlObat.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1015, 320));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(txtObatKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));
        jPanel1.add(txtObatNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 350, 25));
        jPanel1.add(txtObatHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 350, 25));

        txtObatKeterangan.setToolTipText("");
        jPanel1.add(txtObatKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 350, 25));

        cbObatTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel1.add(cbObatTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));

        txtObatJumlah.setToolTipText("");
        jPanel1.add(txtObatJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));

        txtObatSatuan.setToolTipText("");
        jPanel1.add(txtObatSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));

        jLabel2.setText("KODE");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, -1));

        jLabel4.setText("NAMA");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 90, -1));

        jLabel7.setText("HARGA");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 90, -1));

        jLabel8.setText("KETERANGAN");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 90, -1));

        jLabel84.setText("TANGGUNGAN");
        jPanel1.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, -1));

        jLabel5.setText("JUMLAH");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, -1));

        jLabel6.setText("SATUAN");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, -1));

        pnlObat.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 408, 1010, 143));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("KATA KUNCI");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, -1));

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

        pnlObat.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 510, 60));

        btnTambahObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTambahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahObatActionPerformed(evt);
            }
        });
        pnlObat.add(btnTambahObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 20, 80, 40));

        btnClearObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnClearObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearObatActionPerformed(evt);
            }
        });
        pnlObat.add(btnClearObat, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, 80, 40));

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

        pnlBhp.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1010, 340));

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setText("KODE");
        jPanel13.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, -1));

        jLabel37.setText("NAMA");
        jPanel13.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 90, -1));

        jLabel40.setText("HARGA");
        jPanel13.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 90, -1));

        jLabel41.setText("TANGGUNGAN");
        jPanel13.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, -1));

        jLabel38.setText("JUMLAH");
        jPanel13.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, -1));

        jLabel39.setText("SATUAN");
        jPanel13.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, -1));
        jPanel13.add(txtBhpKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));
        jPanel13.add(txtBhpNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 350, 25));
        jPanel13.add(txtBhpHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 350, 25));

        cbBhpTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel13.add(cbBhpTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));

        txtBhpJumlah.setToolTipText("");
        jPanel13.add(txtBhpJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));

        txtBhpSatuan.setToolTipText("");
        jPanel13.add(txtBhpSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));

        pnlBhp.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 1015, 120));

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
        jPanel17.add(txtBhpKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));

        jLabel83.setText("KATA KUNCI");
        jPanel17.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 80, -1));

        pnlBhp.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 510, 60));

        btnTambahBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnTambahBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBhpActionPerformed(evt);
            }
        });
        pnlBhp.add(btnTambahBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 20, 80, 40));

        btnClearBhp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnClearBhp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearBhpActionPerformed(evt);
            }
        });
        pnlBhp.add(btnClearBhp, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 20, 80, 40));

        tabBarang.addTab("BARANG HABIS PAKAI", pnlBhp);

        pnlBarang.add(tabBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1040, 590));

        getContentPane().add(pnlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlTindakan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlTindakan.setBackground(new Color(0,0,0,20));
        pnlTindakan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_tindakan.png"))); // NOI18N
        pnlTindakan.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 20));

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

        pnlTindakan.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 1020, 330));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(txtTindakanKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));
        jPanel6.add(txtTindakanNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 350, 25));

        txtTindakanKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTindakanKategoriMouseClicked(evt);
            }
        });
        jPanel6.add(txtTindakanKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 350, 25));
        jPanel6.add(txtTindakanKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 350, 25));

        cbTindakanKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "NONE", "VVIP", "VIP", "I", "II", "III" }));
        jPanel6.add(cbTindakanKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));

        cbTindakanTanggungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "BPJS", "UMUM" }));
        jPanel6.add(cbTindakanTanggungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));

        cbTindakanSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "TINDAKAN", "HARI", "JAM" }));
        jPanel6.add(cbTindakanSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));
        jPanel6.add(txtTindakanTarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 350, 25));

        jLabel21.setText("KODE");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, -1));

        jLabel22.setText("NAMA");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 90, -1));

        jLabel23.setText("TARIF");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 90, -1));

        jLabel24.setText("KETERANGAN");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 90, -1));

        jLabel32.setText("KATEGORI");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 90, -1));

        jLabel85.setText("TANGGUNGAN");
        jPanel6.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, -1));

        jLabel86.setText("KELAS");
        jPanel6.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, -1));

        jLabel87.setText("SATUAN");
        jPanel6.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, -1));

        pnlTindakan.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 1020, 150));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setText("KATA KUNCI");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, 25));

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
        jPanel7.add(txtTindakanKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));

        pnlTindakan.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 510, 60));

        btnSimpanTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnSimpanTindakan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSimpanTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanTindakanMouseClicked(evt);
            }
        });
        btnSimpanTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanTindakanActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnSimpanTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 60, 80, 40));

        btnResetTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnResetTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetTindakanActionPerformed(evt);
            }
        });
        pnlTindakan.add(btnResetTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 60, 80, 40));

        getContentPane().add(pnlTindakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlOperator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlOperator.setBackground(new Color(0,0,0,20));
        pnlOperator.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_operator.png"))); // NOI18N
        pnlOperator.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 20));

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

        pnlOperator.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1030, 380));

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(txt_op_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 350, 25));

        txt_admin_operator_unit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_admin_operator_unitMouseClicked(evt);
            }
        });
        jPanel10.add(txt_admin_operator_unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 350, 25));
        jPanel10.add(txt_op_uname, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 350, 25));
        jPanel10.add(txt_op_pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 350, 25));

        cb_admin_operator_role.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "ADMIN", "OPERATOR" }));
        jPanel10.add(cb_admin_operator_role, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 350, 25));

        jLabel49.setText("NAMA");
        jPanel10.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 90, -1));

        jLabel27.setText("UNIT");
        jPanel10.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 43, 90, -1));

        jLabel50.setText("USERNAME");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 90, -1));

        jLabel51.setText("PASSWORD");
        jPanel10.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 90, -1));

        jLabel31.setText("ROLE");
        jPanel10.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 90, -1));

        pnlOperator.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 520, 170));

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

        pnlOperator.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 560, 240, 60));

        getContentPane().add(pnlOperator, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlPenduduk.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPenduduk.setBackground(new Color(0,0,0,20));
        pnlPenduduk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_rekmed.png"))); // NOI18N
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlPenduduk.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 20));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("No. Rekam Medik");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, -1));

        jLabel12.setText("Nik");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, -1));

        jLabel13.setText("Nama");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 100, -1));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Kelamin");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, -1));

        jLabel15.setText("Tgl. Lahir");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 110, -1));

        jLabel16.setText("Gol. Darah");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 50, 110, -1));

        jLabel17.setText("Agama");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 110, -1));

        jLabel18.setText("No. Telepon");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 110, -1));
        jPanel3.add(txtPendudukKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 350, 25));
        jPanel3.add(txtPendudukNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 350, 25));
        jPanel3.add(txtPendudukNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 350, 25));

        cbPendudukKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel3.add(cbPendudukKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 350, 25));
        jPanel3.add(txtPendudukLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 350, 25));
        jPanel3.add(txtPendudukDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 350, 25));
        jPanel3.add(txtPendudukAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 350, 25));
        jPanel3.add(txtPendudukTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 110, 350, 25));

        pnlPenduduk.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 1040, 150));

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

        pnlPenduduk.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1040, 340));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pencarian"));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText("KATA KUNCI");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

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
        jPanel4.add(txtPendudukKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 350, 25));

        pnlPenduduk.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 520, 70));

        btnPendudukSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_simpan small.png"))); // NOI18N
        btnPendudukSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukSimpanActionPerformed(evt);
            }
        });
        pnlPenduduk.add(btnPendudukSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 50, 80, 40));

        btnPendudukEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/btn_Clear Small.png"))); // NOI18N
        btnPendudukEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendudukEditActionPerformed(evt);
            }
        });
        pnlPenduduk.add(btnPendudukEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 50, 80, 40));

        getContentPane().add(pnlPenduduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

        pnlPegawai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPegawai.setBackground(new Color(0,0,0,20));
        pnlPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dbsys/rs/client/images/lbl_pegawai.png"))); // NOI18N
        jLabel25.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlPegawai.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 20));

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

        tab_dokter.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1010, 250));

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

        tab_dokter.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 480, 200, 60));

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setText("NIP");
        jPanel21.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 90, -1));

        jLabel48.setText("NAMA");
        jPanel21.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 90, -1));

        jLabel53.setText("KODE");
        jPanel21.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, -1));

        jLabel54.setText("NIK");
        jPanel21.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 90, 10));

        jLabel56.setText("TGL LAHIR");
        jPanel21.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 90, 10));

        jLabel55.setText("KELAMIN");
        jPanel21.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 90, 10));

        jLabel57.setText("DARAH");
        jPanel21.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 90, -1));

        jLabel58.setText("AGAMA");
        jPanel21.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 90, -1));

        jLabel59.setText("TELEPON");
        jPanel21.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 90, -1));
        jPanel21.add(txt_dokter_kode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 350, 25));
        jPanel21.add(txt_dokter_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 350, 25));
        jPanel21.add(txt_dokter_nip, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 350, 25));
        jPanel21.add(txt_dokter_nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 350, 25));
        jPanel21.add(txt_dokter_lahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 350, 25));

        cb_dokter_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel21.add(cb_dokter_kelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 350, 25));
        jPanel21.add(txt_dokter_darah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 350, 25));
        jPanel21.add(txt_dokter_agama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 350, 25));
        jPanel21.add(txt_dokter_telepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 350, 25));

        tab_dokter.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 1010, 180));

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

        tab_perawat.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1010, 250));

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setText("KODE");
        jPanel15.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 90, -1));

        jLabel34.setText("NIP");
        jPanel15.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 90, -1));

        jLabel35.setText("NAMA");
        jPanel15.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 90, -1));

        jLabel46.setText("NIK");
        jPanel15.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 90, -1));

        jLabel61.setText("TGL. LAHIR");
        jPanel15.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 90, -1));

        jLabel60.setText("KELAMIN");
        jPanel15.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 90, -1));

        jLabel62.setText("DARAH");
        jPanel15.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 90, -1));

        jLabel63.setText("AGAMA");
        jPanel15.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 70, 90, -1));

        jLabel64.setText("TELEPON");
        jPanel15.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, 90, -1));
        jPanel15.add(txt_perawat_kode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 350, 25));
        jPanel15.add(txt_perawat_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 350, 25));
        jPanel15.add(txt_perawat_nip, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 350, 25));
        jPanel15.add(txt_perawat_nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 350, 25));
        jPanel15.add(txt_perawat_lahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 350, 25));

        cb_perawat_kelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel15.add(cb_perawat_kelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 350, 25));
        jPanel15.add(txt_perawat_darah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, 350, 25));
        jPanel15.add(txt_perawat_agama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, 350, 25));
        jPanel15.add(txt_perawat_telepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 100, 350, 25));

        tab_perawat.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 1010, 180));

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

        tab_perawat.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 480, 200, 60));

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

        tab_apoteker.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1010, 250));

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

        tab_apoteker.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 480, 200, 60));

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel65.setText("KODE");
        jPanel22.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 90, -1));

        jLabel66.setText("NIP");
        jPanel22.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 90, -1));

        jLabel67.setText("NAMA");
        jPanel22.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 90, -1));

        jLabel68.setText("NIK");
        jPanel22.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 90, -1));

        jLabel69.setText("KELAMIN");
        jPanel22.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 90, -1));

        jLabel70.setText("TGL. LAHIR");
        jPanel22.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 90, -1));

        jLabel71.setText("DARAH");
        jPanel22.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 90, -1));

        jLabel72.setText("AGAMA");
        jPanel22.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 70, 90, -1));

        jLabel73.setText("TELEPON");
        jPanel22.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, 90, -1));
        jPanel22.add(txtApotekerKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 350, 25));
        jPanel22.add(txtApotekerNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 350, 25));
        jPanel22.add(txtApotekerNip, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 350, 25));
        jPanel22.add(txtApotekerNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 350, 25));
        jPanel22.add(txtApotekerLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 350, 25));

        cbApotekerKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel22.add(cbApotekerKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 350, 25));
        jPanel22.add(txtApotekerDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, 350, 25));
        jPanel22.add(txtApotekerAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, 350, 25));
        jPanel22.add(txtApotekerTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 100, 350, 25));

        tab_apoteker.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 1010, 180));

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

        tab_administrasi.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1010, 250));

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

        tab_administrasi.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 480, 200, 60));

        jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel74.setText("KODE");
        jPanel23.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 90, -1));

        jLabel76.setText("NAMA");
        jPanel23.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 90, -1));

        jLabel75.setText("NIP");
        jPanel23.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 90, -1));

        jLabel77.setText("NIK");
        jPanel23.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 90, -1));

        jLabel79.setText("TGL. LAHIR");
        jPanel23.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 90, -1));

        jLabel78.setText("KELAMIN");
        jPanel23.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 90, -1));

        jLabel80.setText("DARAH");
        jPanel23.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 90, -1));

        jLabel81.setText("AGAMA");
        jPanel23.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 70, 90, -1));

        jLabel82.setText("TELEPON");
        jPanel23.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, 90, -1));
        jPanel23.add(txtPekerjaKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 350, 25));
        jPanel23.add(txtPekerjaNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 350, 25));
        jPanel23.add(txtPekerjaNip, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 350, 25));
        jPanel23.add(txtPekerjaNik, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 350, 25));
        jPanel23.add(txtPekerjaLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 350, 25));

        cbPekerjaKelamin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "PRIA", "WANITA" }));
        jPanel23.add(cbPekerjaKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 350, 25));
        jPanel23.add(txtPekerjaDarah, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, 350, 25));
        jPanel23.add(txtPekerjaAgama, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, 350, 25));
        jPanel23.add(txtPekerjaTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 100, 350, 25));

        tab_administrasi.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 1010, 180));

        tab_pane.addTab("ADMINSTRASI", tab_administrasi);

        pnlPegawai.add(tab_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 1040, 580));

        getContentPane().add(pnlPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1060, 630));

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

        btnPasien.setText("PASIEN");
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
            PendudukTableModel tableModel = new PendudukTableModel(null);
            tblPenduduk.setModel(tableModel);
        }
    }//GEN-LAST:event_txtPendudukKeywordFocusLost

    private void btnPendudukEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendudukEditActionPerformed
        pendudukEventController.onCleanForm();
    }//GEN-LAST:event_btnPendudukEditActionPerformed

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

    private void txtTindakanKeywordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTindakanKeywordFocusLost
        try {
            tindakanEventController.onSearch();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            TindakanTableModel tableModel = new TindakanTableModel(null);
            tblTindakan.setModel(tableModel);
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

    private void btnPasienEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasienEditActionPerformed
        pasienEventController.onCleanForm();
    }//GEN-LAST:event_btnPasienEditActionPerformed

    private void btnResetTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTindakanActionPerformed
        tindakanEventController.onCleanForm();
    }//GEN-LAST:event_btnResetTindakanActionPerformed

    private void btnSimpanTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanTindakanActionPerformed
        try {
            tindakanEventController.onSave();
            tindakanEventController.onCleanForm();
            tindakanEventController.onLoad();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSimpanTindakanActionPerformed
    
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
    private javax.swing.JPanel pnlBhp;
    private javax.swing.JPanel pnlObat;
    private javax.swing.JPanel pnlOperator;
    private javax.swing.JPanel pnlPasien;
    private javax.swing.JPanel pnlPegawai;
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
