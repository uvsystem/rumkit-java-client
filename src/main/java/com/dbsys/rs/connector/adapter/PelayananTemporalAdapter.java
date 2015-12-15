package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.PelayananTemporal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class PelayananTemporalAdapter extends PelayananAdapter {

    private Date tanggalSelesai;
    private Time jamMasuk;
    private Time jamKeluar;

    public PelayananTemporalAdapter() {
        super("TEMPORAL");
    }
    
    public PelayananTemporalAdapter(PelayananTemporal pelayanan) {
        this();
        setPelayanan(pelayanan);
    }

    public Date getTanggalMulai() {
        return tanggal;
    }

    public void setTanggalMulai(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Date getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(Date tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public Time getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(Time jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public Time getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(Time jamKeluar) {
        this.jamKeluar = jamKeluar;
    }
    
    @Override
    @JsonIgnore
    public Pelayanan getPelayanan() {
        PelayananTemporal pelayanan = new PelayananTemporal();
        pelayanan.setId(id);
        pelayanan.setTanggal(tanggal);
        pelayanan.setJumlah(jumlah);
        pelayanan.setBiayaTambahan(biayaTambahan);
        pelayanan.setKeterangan(keterangan);
        pelayanan.setPasien(pasien);
        pelayanan.setUnit(unit);
        pelayanan.setPembayaran(pembayaran);
        pelayanan.setStatus(status);
        pelayanan.setPelaksana(pelaksana);
        pelayanan.setTindakan(tindakan);
        
        pelayanan.setTanggalSelesai(tanggalSelesai);
        pelayanan.setJamMasuk(jamMasuk);
        pelayanan.setJamKeluar(jamKeluar);
        
        return pelayanan;
    }
    
    @Override
    public void setPelayanan(Pelayanan pelayanan) {
        setPelayanan((PelayananTemporal) pelayanan);
    }
    
    public void setPelayanan(PelayananTemporal pelayanan) {
        setId(pelayanan.getId());
        setTanggal(pelayanan.getTanggal());
        setJumlah(pelayanan.getJumlah());
        setBiayaTambahan(pelayanan.getBiayaTambahan());
        setKeterangan(pelayanan.getKeterangan());
        setPasien(pelayanan.getPasien());
        setUnit(pelayanan.getUnit());
        setPembayaran(pelayanan.getPembayaran());
        setStatus(pelayanan.getStatus());
        setPelaksana(pelayanan.getPelaksana());
        setTindakan(pelayanan.getTindakan());

        setTanggalSelesai(pelayanan.getTanggalSelesai());
        setJamMasuk(pelayanan.getJamMasuk());
        setJamKeluar(pelayanan.getJamKeluar());
    }
}
