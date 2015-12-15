package com.dbsys.rs.connector.adapter;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class RekapPegawaiAdapter {

    private Long id;
    private String nama;
    private String tindakan;
    private Long tarif;
    private Long jumlah;
    private Long total;
    
    public RekapPegawaiAdapter() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTindakan() {
        return tindakan;
    }

    public void setTindakan(String tindakan) {
        this.tindakan = tindakan;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public Long getTarif() {
        return tarif;
    }

    public void setTarif(Long tarif) {
        this.tarif = tarif;
    }

    public Long getTotal() {
        hitungTotal();
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    
    private void hitungTotal() {
        total = jumlah * tarif;
    }
}
