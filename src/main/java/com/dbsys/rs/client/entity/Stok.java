package com.dbsys.rs.client.entity;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipe"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StokKembali.class, name = "KEMBALI"),
    @JsonSubTypes.Type(value = Stok.class, name = "STOK")
})
public class Stok {

    public enum JenisStok {
        MASUK,
        KELUAR
    }

    protected Long id;
    protected Long jumlah;
    protected Date tanggal;
    protected Time jam;
    protected Barang barang;
    protected JenisStok jenis;

    // Untuk JSON bukan JPA
    private String tipeStok;

    public Stok() {
        super();
        this.tipeStok = "STOK";
    }

    public Stok(String name) {
        this();
        this.tipeStok = name;
    }

    public String getTipeStok() {
        return tipeStok;
    }

    public void setTipeStok(String tipeStok) {
        this.tipeStok = tipeStok;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Time getJam() {
        return jam;
    }

    public void setJam(Time jam) {
        this.jam = jam;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public JenisStok getJenis() {
        return jenis;
    }

    public void setJenis(JenisStok jenis) {
        this.jenis = jenis;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((jam == null) ? 0 : jam.hashCode());
        result = prime * result + ((jumlah == null) ? 0 : jumlah.hashCode());
        result = prime * result + ((tanggal == null) ? 0 : tanggal.hashCode());
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

        Stok other = (Stok) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jam == null) {
            if (other.jam != null)
                return false;
        } else if (!jam.equals(other.jam))
            return false;
        if (jumlah == null) {
            if (other.jumlah != null)
                return false;
        } else if (!jumlah.equals(other.jumlah))
            return false;
        if (tanggal == null) {
            if (other.tanggal != null)
                return false;
        } else if (!tanggal.equals(other.tanggal))
            return false;
        return true;
    }

}
