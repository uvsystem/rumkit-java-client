package com.dbsys.rs.client.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.dbsys.rs.client.CodedEntity;
import com.dbsys.rs.client.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Pembayaran implements CodedEntity {

    private String kode;
    private Date tanggal;
    private Time jam;
    private Long jumlah;

    private Pasien pasien;

    private List<Pelayanan> listPelayanan;
    private List<Pemakaian> listPemakaian;

    public Pembayaran() {
        super();
        listPelayanan = new ArrayList<>();
        listPemakaian = new ArrayList<>();
    }

    @Override
    public String getKode() {
        return kode;
    }

    @Override
    public void setKode(String kode) {
        this.kode = kode;
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

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public Pasien getPasien() {
        return pasien;
    }

    public void setPasien(Pasien pasien) {
        this.pasien = pasien;
    }

    public List<Pelayanan> getListPelayanan() {
        return listPelayanan;
    }

    public void setListPelayanan(List<Pelayanan> listPelayanan) {
        this.listPelayanan = listPelayanan;
    }

    public void addPelayanan(Pelayanan pelayanan) {
        listPelayanan.add(pelayanan);
        pelayanan.setPembayaran(this);
    }

    public List<Pemakaian> getListPemakaian() {
        return listPemakaian;
    }

    public void setListPemakaian(List<Pemakaian> listPemakaian) {
        this.listPemakaian = listPemakaian;
    }

    public void addPemakaian(Pemakaian pemakaian) {
        listPemakaian.add(pemakaian);
        pemakaian.setPembayaran(this);
    }

    @JsonIgnore
    public List<Tagihan> getListTagihan() {
        List<Tagihan> list = new ArrayList<>();

        for (Tagihan tagihan : listPelayanan)
            list.add(tagihan);

        for (Tagihan tagihan : listPemakaian)
            list.add(tagihan);

        return list;
    }

    @Override
    public String generateKode() {
        kode = createKode();
        return kode;
    }

    public static String createKode() {
        Integer d = Math.abs(DateUtil.getDate().hashCode());
        Integer t = Math.abs(DateUtil.getTime().hashCode());

        return String.format("40%s00%s", d, t);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jam == null) ? 0 : jam.hashCode());
        result = prime * result + ((jumlah == null) ? 0 : jumlah.hashCode());
        result = prime * result + ((kode == null) ? 0 : kode.hashCode());
        result = prime * result + ((pasien == null) ? 0 : pasien.hashCode());
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

        Pembayaran other = (Pembayaran) obj;
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
        if (kode == null) {
            if (other.kode != null)
                return false;
        } else if (!kode.equals(other.kode))
            return false;
        if (pasien == null) {
            if (other.pasien != null)
                return false;
        } else if (!pasien.equals(other.pasien))
            return false;
        if (tanggal == null) {
            if (other.tanggal != null)
                return false;
        } else if (!tanggal.equals(other.tanggal))
            return false;
        return true;
    }
}
