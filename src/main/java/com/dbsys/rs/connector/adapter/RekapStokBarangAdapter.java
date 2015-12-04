package com.dbsys.rs.connector.adapter;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class RekapStokBarangAdapter {

    private String nama;
    private Long jumlahMasuk;
    private Long jumlahKeluar;
    private Long jumlahKembali;
    private Long jumlahSupply;
    private Long jumlahResep;
    private Long jumlahPemakaian;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Long getJumlahMasuk() {
        return jumlahMasuk;
    }

    public void setJumlahMasuk(Long jumlahMasuk) {
        this.jumlahMasuk = jumlahMasuk;
    }

    public Long getJumlahKeluar() {
        return jumlahKeluar;
    }

    public void setJumlahKeluar(Long jumlahKeluar) {
        this.jumlahKeluar = jumlahKeluar;
    }

    public Long getJumlahKembali() {
        return jumlahKembali;
    }

    public void setJumlahKembali(Long jumlahKembali) {
        this.jumlahKembali = jumlahKembali;
    }

    public Long getJumlahSupply() {
        return jumlahSupply;
    }

    public void setJumlahSupply(Long jumlahSupply) {
        this.jumlahSupply = jumlahSupply;
    }

    public Long getJumlahResep() {
        return jumlahResep;
    }

    public void setJumlahResep(Long jumlahResep) {
        this.jumlahResep = jumlahResep;
    }

    public Long getJumlahPemakaian() {
        return jumlahPemakaian;
    }

    public void setJumlahPemakaian(Long jumlahPemakaian) {
        this.jumlahPemakaian = jumlahPemakaian;
    }
    
    public Long getTotalMasuk() {
        return jumlahMasuk + jumlahKembali;
    }
    
    public Long getTotalKeluar() {
        return jumlahKeluar + jumlahResep + jumlahSupply;
    }
}
