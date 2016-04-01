package com.dbsys.rs.client.entity;

import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.Tanggungan;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.sql.Date;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipeTagihan"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Pelayanan.class, name = "PELAYANAN"),
    @JsonSubTypes.Type(value = Pemakaian.class, name = "PEMAKAIAN")
})
public abstract class Tagihan {

    public enum StatusTagihan {
        MENUNGGAK,
        LUNAS
    }

    protected Long id;
    protected Date tanggal;
    protected Integer jumlah;
    protected Long biayaTambahan;
    protected String keterangan;

    protected Pasien pasien;
    protected Unit unit;
    protected Pembayaran pembayaran;

    protected StatusTagihan status;
    protected Tanggungan tanggungan;

    private String tipeTagihan;
    
    protected Tagihan() {
        super();
        this.status = StatusTagihan.MENUNGGAK;
    }
    
    protected Tagihan(String tipeTagihan) {
        super();
        this.tipeTagihan = tipeTagihan;
    }
    
    public String getTipeTagihan() {
        return tipeTagihan;
    }
    
    public void setTipeTagihan(String tipeTagihan) {
        this.tipeTagihan = tipeTagihan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Long getBiayaTambahan() {
        return biayaTambahan;
    }

    public void setBiayaTambahan(Long biayaTambahan) {
        this.biayaTambahan = biayaTambahan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Pasien getPasien() {
        return pasien;
    }

    public void setPasien(Pasien pasien) {
        this.pasien = pasien;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @JsonBackReference
    public Pembayaran getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }

    public StatusTagihan getStatus() {
        return status;
    }

    public void setStatus(StatusTagihan status) {
        this.status = status;
    }

    public abstract String getNama();

    public void setNama(String nama) { }

    public String getNamaUnit() {
        return unit.getNama();
    }

    public void setNamaUnit(String namaUnit) { }

    public Penanggung getPenanggung() {
        return tanggungan.getPenanggung();
    }

    public void setPenanggung(Penanggung penanggung) { }

    public abstract Long getTagihan();
    public void setTagihan(Long tagihan) { }

    public Long getTagihanCounted() {
            /**
             * Untuk pasien BPJS yang mendapatkan tagihan yang ditanggung BPJS, dihitung 0.
             */
            if (Penanggung.BPJS.equals(pasien.getPenanggung()) && Penanggung.BPJS.equals(tanggungan.getPenanggung()))
                    return 0L;
            return getTagihan();
    }

    public void setTagihanCounted(Long tagihanCounted) { }

    @JsonIgnore
    public boolean isPersisted() {
        return !((id == null) || id.equals(0L));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((biayaTambahan == null) ? 0 : biayaTambahan.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((jumlah == null) ? 0 : jumlah.hashCode());
        result = prime * result
                + ((keterangan == null) ? 0 : keterangan.hashCode());
        result = prime * result + ((pasien == null) ? 0 : pasien.hashCode());
        result = prime * result
                + ((pembayaran == null) ? 0 : pembayaran.hashCode());
        result = prime * result + ((tanggal == null) ? 0 : tanggal.hashCode());
        result = prime * result
                + ((tanggungan == null) ? 0 : tanggungan.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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

        Tagihan other = (Tagihan) obj;
        if (biayaTambahan == null) {
            if (other.biayaTambahan != null)
                return false;
        } else if (!biayaTambahan.equals(other.biayaTambahan))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jumlah == null) {
            if (other.jumlah != null)
                return false;
        } else if (!jumlah.equals(other.jumlah))
            return false;
        if (keterangan == null) {
            if (other.keterangan != null)
                return false;
        } else if (!keterangan.equals(other.keterangan))
            return false;
        if (pasien == null) {
            if (other.pasien != null)
                return false;
        } else if (!pasien.equals(other.pasien))
            return false;
        if (pembayaran == null) {
            if (other.pembayaran != null)
                return false;
        } else if (!pembayaran.equals(other.pembayaran))
            return false;
        if (tanggal == null) {
            if (other.tanggal != null)
                return false;
        } else if (!tanggal.equals(other.tanggal))
            return false;
        if (tanggungan == null) {
            if (other.tanggungan != null)
                return false;
        } else if (!tanggungan.equals(other.tanggungan))
            return false;
        if (unit == null) {
            if (other.unit != null)
                return false;
        } else if (!unit.equals(other.unit))
            return false;
        return true;
    }
}
