package com.dbsys.rs.client.entity;

import com.dbsys.rs.client.CodedEntity;
import com.dbsys.rs.client.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class StokKembali extends Stok implements CodedEntity {

    private Pasien pasien;
    private String nomor;

    public StokKembali() {
        super("KEMBALI");
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
        nomor = createKode();
        return nomor;
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
