package com.dbsys.rs.client.entity;

import java.sql.Date;
import java.sql.Time;

import com.dbsys.rs.client.CodedEntity;
import com.dbsys.rs.client.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class StokKembali implements CodedEntity {

    private Long id;
    private Long jumlah;
    private Date tanggal;
    private Time jam;
    private Barang barang;

    private Pasien pasien;
    private String nomor;

    public StokKembali() {
        super();
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

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
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

    public Pasien getPasien() {
        return pasien;
    }

    public void setPasien(Pasien pasien) {
        this.pasien = pasien;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public Long hitungPengembalian() {
        return barang.getHarga() * jumlah;
    }

    @Override
    public String generateKode() {
        return createKode();
    }

    public static String createKode() {
        Integer d = Math.abs(DateUtil.getDate().hashCode());
        Integer t = Math.abs(DateUtil.getTime().hashCode());

        return String.format("30%s00%s", d, t);
    }

    @Override
    @JsonIgnore
    public String getKode() {
        return getNomor();
    }

    @Override
    public void setKode(String kode) {
        setNomor(kode);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((pasien == null) ? 0 : pasien.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        StokKembali other = (StokKembali) obj;
        if (pasien == null) {
            if (other.pasien != null)
                return false;
        } else if (!pasien.equals(other.pasien))
            return false;
        return true;
    }
}
