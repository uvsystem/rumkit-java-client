package com.dbsys.rs.client.entity;

import java.sql.Date;

import com.dbsys.rs.client.CodedEntity;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.Kelas;
import com.dbsys.rs.client.Tanggungan;
import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.entity.Penduduk.Kelamin;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Pasien implements Tanggungan, CodedEntity {

    public enum StatusPasien {
        PERAWATAN, KELUAR
    }

    public enum KeadaanPasien {
        SEMBUH, RUJUK, SAKIT, MATI, LARI
    }

    public enum Perawatan {
        RAWAT_JALAN, RAWAT_INAP, UGD
    }

    public enum Pendaftaran {
        LOKET, UGD, NICU
    }

    private Long id;
    private String kode;
    private Date tanggalMasuk;
    private Date tanggalRawatInap;
    private Date tanggalKeluar;

    private Long totalTagihan;
    private Long cicilan;

    private StatusPasien status;
    private Penanggung penanggung;
    private Kelas kelas;
    private KeadaanPasien keadaan;
    private Perawatan tipePerawatan;
    private Pendaftaran pendaftaran;

    private Penduduk penduduk;
    private Unit tujuan;
    private PelayananTemporal perawatan;

    /*
     * Digunakan untuk JSON mapping
     */
    private Unit ruangPerawatan;

    public Pasien() {
        super();
        this.penduduk = new Penduduk();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getKode() {
        return kode;
    }

    @Override
    public void setKode(String kode) {
        this.kode = kode;
    }

    public Date getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(Date tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    public Date getTanggalRawatInap() {
        return tanggalRawatInap;
    }

    public void setTanggalRawatInap(Date tanggalRawatInap) {
        this.tanggalRawatInap = tanggalRawatInap;
    }

    public Long getTotalTagihan() {
        return totalTagihan;
    }

    public void setTotalTagihan(Long totalTagihan) {
        this.totalTagihan = totalTagihan;
    }

    public void addTotalTagihan(Long tagihan) {
        if (totalTagihan == null)
            totalTagihan = 0L;
        totalTagihan += tagihan;
    }

    public void substractTotalTagihan(Long tagihan) {
        if (totalTagihan == null) {
            totalTagihan = 0L;
        } else {
            totalTagihan -= tagihan;
        }
    }

    public Long getCicilan() {
        return cicilan;
    }

    public void setCicilan(Long cicilan) {
        this.cicilan = cicilan;
    }

    public void addCicilan(Long jumlah) {
        if (cicilan == null)
            cicilan = 0L;
        cicilan += jumlah;
    }

    public void substractCicilan(Long jumlah) {
        if (cicilan == null) {
            cicilan = 0L;
        } else {
            cicilan -= jumlah;
        }
    }

    public StatusPasien getStatus() {
        return status;
    }

    public void setStatus(StatusPasien status) {
        this.status = status;
    }

    @Override
    public Penanggung getPenanggung() {
        return penanggung;
    }

    public void setPenanggung(Penanggung penanggung) {
        this.penanggung = penanggung;
    }

    public Penduduk getPenduduk() {
        return penduduk;
    }

    public void setPenduduk(Penduduk penduduk) {
        this.penduduk = penduduk;
    }

    public Perawatan getTipePerawatan() {
        return tipePerawatan;
    }

    public void setTipePerawatan(Perawatan tipePerawatan) {
        this.tipePerawatan = tipePerawatan;
    }

    public Pendaftaran getPendaftaran() {
        return pendaftaran;
    }

    public void setPendaftaran(Pendaftaran pendaftaran) {
        this.pendaftaran = pendaftaran;
    }

    public Date getTanggalKeluar() {
        return tanggalKeluar;
    }

    public void setTanggalKeluar(Date tanggalKeluar) {
        this.tanggalKeluar = tanggalKeluar;
    }

    /**
     * Keadaan pasien saat keluar.
     * 
     * @return keadaan pasien
     */
    public KeadaanPasien getKeadaan() {
        return keadaan;
    }

    public void setKeadaan(KeadaanPasien keadaan) {
        this.keadaan = keadaan;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    @JsonBackReference
    public PelayananTemporal getPerawatan() {
        return perawatan;
    }

    public void setPerawatan(PelayananTemporal perawatan) {
        this.perawatan = perawatan;
        if (perawatan != null)
            this.ruangPerawatan = perawatan.getUnit();
    }

    public Unit getRuangPerawatan() {
        return ruangPerawatan;
    }

    public void setRuangPerawatan(Unit ruangPerawatan) {
        this.ruangPerawatan = ruangPerawatan;
    }	

    public Unit getTujuan() {
        return tujuan;
    }

    public void setTujuan(Unit tujuan) {
        this.tujuan = tujuan;
    }

    @JsonIgnore
    public Long getIdPenduduk() {
        return penduduk.getId();
    }

    public void setIdPenduduk(Long idPenduduk) {
        penduduk.setId(idPenduduk);
    }

    @JsonIgnore
    public String getNama() {
        return penduduk.getNama();
    }

    public void setNama(String nama) {
        penduduk.setNama(nama);
    }

    @JsonIgnore
    public Kelamin getKelamin() {
        return penduduk.getKelamin();
    }

    public void setKelamin(Kelamin kelamin) {
        penduduk.setKelamin(kelamin);
    }

    @JsonIgnore
    public Date getTanggalLahir() {
        return penduduk.getTanggalLahir();
    }

    public void setTanggalLahir(Date tanggalLahir) {
        penduduk.setTanggalLahir(tanggalLahir);
    }

    @JsonIgnore
    public String getDarah() {
        return penduduk.getDarah();
    }

    public void setDarah(String darah) {
        penduduk.setDarah(darah);
    }

    @JsonIgnore
    public String getAgama() {
        return penduduk.getAgama();
    }

    public void setAgama(String agama) {
        penduduk.setAgama(agama);
    }

    @JsonIgnore
    public String getTelepon() {
        return penduduk.getTelepon();
    }

    public void setTelepon(String telepon) {
        penduduk.setTelepon(telepon);
    }

    @JsonIgnore
    public String getNik() {
        return penduduk.getNik();
    }

    public void setNik(String nik) {
        penduduk.setNik(nik);
    }

    @JsonIgnore
    public String getKodePenduduk() {
        return penduduk.getKode();
    }

    public void setKodePenduduk(String kode) {
        penduduk.setKode(kode);
    }

    @Override
    public String generateKode() {
        kode = createKode();
        return kode;
    }

    public static String createKode() {
        Integer d = Math.abs(DateUtil.getDate().hashCode());
        Integer t = Math.abs(DateUtil.getTime().hashCode());

        return String.format("10%s", (d + t));
    }

    public void bayar(Long jumlah) {
        if (this.cicilan == null)
            this.cicilan = 0L;

        this.cicilan += jumlah;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cicilan == null) ? 0 : cicilan.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((keadaan == null) ? 0 : keadaan.hashCode());
        result = prime * result + ((kelas == null) ? 0 : kelas.hashCode());
        result = prime * result + ((kode == null) ? 0 : kode.hashCode());
        result = prime * result
                + ((penduduk == null) ? 0 : penduduk.hashCode());
        result = prime * result
                + ((perawatan == null) ? 0 : perawatan.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result
                + ((tanggalKeluar == null) ? 0 : tanggalKeluar.hashCode());
        result = prime * result
                + ((tanggalMasuk == null) ? 0 : tanggalMasuk.hashCode());
        result = prime * result
                + ((tanggalRawatInap == null) ? 0 : tanggalRawatInap.hashCode());
        result = prime * result
                + ((penanggung == null) ? 0 : penanggung.hashCode());
        result = prime * result
                + ((tipePerawatan == null) ? 0 : tipePerawatan.hashCode());
        result = prime * result
                + ((totalTagihan == null) ? 0 : totalTagihan.hashCode());
        result = prime * result + ((tujuan == null) ? 0 : tujuan.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Pasien other = (Pasien) obj;
        if (cicilan == null) {
            if (other.cicilan != null)
                return false;
        } else if (!cicilan.equals(other.cicilan))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
                return false;
        if (keadaan != other.keadaan)
                return false;
        if (kelas != other.kelas)
                return false;
        if (kode == null) {
            if (other.kode != null)
                return false;
        } else if (!kode.equals(other.kode))
            return false;
        if (penduduk == null) {
            if (other.penduduk != null)
                return false;
        } else if (!penduduk.equals(other.penduduk))
            return false;
        if (perawatan == null) {
            if (other.perawatan != null)
                return false;
        } else if (!perawatan.equals(other.perawatan))
            return false;
        if (status != other.status)
            return false;
        if (tanggalKeluar == null) {
            if (other.tanggalKeluar != null)
                return false;
        } else if (!tanggalKeluar.equals(other.tanggalKeluar))
            return false;
        if (tanggalMasuk == null) {
            if (other.tanggalMasuk != null)
                return false;
        } else if (!tanggalMasuk.equals(other.tanggalMasuk))
            return false;
        if (tanggalRawatInap == null) {
            if (other.tanggalRawatInap != null)
                return false;
        } else if (!tanggalRawatInap.equals(other.tanggalRawatInap))
            return false;
        if (penanggung != other.penanggung)
            return false;
        if (tipePerawatan != other.tipePerawatan)
            return false;
        if (totalTagihan == null) {
            if (other.totalTagihan != null)
                return false;
        } else if (!totalTagihan.equals(other.totalTagihan))
            return false;
        if (tujuan == null) {
            if (other.tujuan != null)
                return false;
        } else if (!tujuan.equals(other.tujuan))
            return false;
        return true;
    }
}
