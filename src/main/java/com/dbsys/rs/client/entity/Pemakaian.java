package com.dbsys.rs.client.entity;

import com.dbsys.rs.client.CodedEntity;
import com.dbsys.rs.client.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Pemakaian extends Tagihan implements CodedEntity {

    private Barang barang;
    private String nomorResep;

    public Pemakaian() {
        super("PEMAKAIAN");
    }
    
    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
        this.tanggungan = barang;
    }

    public String getNomorResep() {
        return nomorResep;
    }

    public void setNomorResep(String nomorResep) {
        this.nomorResep = nomorResep;
    }

    @Override
    public Long getTagihan() {
        return barang.getHarga() * jumlah + biayaTambahan;
    }

    @Override
    public String getNama() {
        return barang.getNama();
    }

    @Override
    public String generateKode() {
        nomorResep = createKode();
        return nomorResep;
    }

    public static String createKode() {
        Integer d = Math.abs(DateUtil.getDate().hashCode());
        Integer t = Math.abs(DateUtil.getTime().hashCode());

        return String.format("20%s00%s", d, t);
    }

    @Override
    @JsonIgnore
    public String getKode() {
        return getNomorResep();
    }

    @Override
    public void setKode(String kode) {
        setNomorResep(kode);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((barang == null) ? 0 : barang.hashCode());
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

        Pemakaian other = (Pemakaian) obj;
        if (barang == null) {
            if (other.barang != null)
                return false;
        } else if (!barang.equals(other.barang))
            return false;
        return true;
    }
}
