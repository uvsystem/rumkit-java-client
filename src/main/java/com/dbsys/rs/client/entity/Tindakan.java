package com.dbsys.rs.client.entity;

import com.dbsys.rs.client.CodedEntity;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.Kelas;
import com.dbsys.rs.client.Tanggungan;
import com.dbsys.rs.client.Penanggung;

public class Tindakan implements Tanggungan, CodedEntity {

    public enum SatuanTindakan {
        TINDAKAN, HARI, JAM
    }

    protected Long id;
    protected String kode;
    protected String nama;
    protected Long tarif;
    protected KategoriTindakan kategori;
    protected Kelas kelas;
    protected Penanggung penanggung;
    protected SatuanTindakan satuan;
    protected String keterangan;

    public Tindakan() {
        super();
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

    @Override
    public String generateKode() {
        kode = createKode();
        return kode;
    }

    public static String createKode() {
    Integer d = Math.abs(DateUtil.getDate().hashCode());
    Integer t = Math.abs(DateUtil.getTime().hashCode());

    return String.format("70%s00%s", d, t);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Long getTarif() {
        return tarif;
    }

    public void setTarif(Long tarif) {
        this.tarif = tarif;
    }

    public KategoriTindakan getKategori() {
        return kategori;
    }

    public void setKategori(KategoriTindakan kategori) {
        this.kategori = kategori;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    @Override
    public Penanggung getPenanggung() {
        return penanggung;
    }

    public void setPenanggung(Penanggung penanggung) {
        this.penanggung = penanggung;
    }

    public SatuanTindakan getSatuan() {
        return satuan;
    }

    public void setSatuan(SatuanTindakan satuan) {
        this.satuan = satuan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((kategori == null) ? 0 : kategori.hashCode());
        result = prime * result + ((kelas == null) ? 0 : kelas.hashCode());
        result = prime * result
                + ((keterangan == null) ? 0 : keterangan.hashCode());
        result = prime * result + ((kode == null) ? 0 : kode.hashCode());
        result = prime * result + ((nama == null) ? 0 : nama.hashCode());
        result = prime * result + ((satuan == null) ? 0 : satuan.hashCode());
        result = prime * result
                + ((penanggung == null) ? 0 : penanggung.hashCode());
        result = prime * result + ((tarif == null) ? 0 : tarif.hashCode());
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

        Tindakan other = (Tindakan) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (kategori == null) {
            if (other.kategori != null)
                return false;
        } else if (!kategori.equals(other.kategori))
            return false;
        if (kelas != other.kelas)
            return false;
        if (keterangan == null) {
            if (other.keterangan != null)
                return false;
        } else if (!keterangan.equals(other.keterangan))
            return false;
        if (kode == null) {
            if (other.kode != null)
                return false;
        } else if (!kode.equals(other.kode))
            return false;
        if (nama == null) {
            if (other.nama != null)
                return false;
        } else if (!nama.equals(other.nama))
            return false;
        if (satuan != other.satuan)
            return false;
        if (penanggung != other.penanggung)
            return false;
        if (tarif == null) {
            if (other.tarif != null)
                return false;
        } else if (!tarif.equals(other.tarif))
            return false;
        return true;
    }
}
