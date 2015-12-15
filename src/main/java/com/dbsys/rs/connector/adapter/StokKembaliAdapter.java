package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokKembali;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class StokKembaliAdapter extends StokAdapter{
    
    private Pasien pasien;
    private String nomor;

    public StokKembaliAdapter() {
        super("KEMBALI");
    }

    public StokKembaliAdapter(StokKembali stok) {
        this();
        setStok(stok);
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
    
    @Override
    @JsonIgnore
    public Stok getStok() {
        StokKembali stok = new StokKembali();
        stok.setId(id);
        stok.setJumlah(jumlah);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setBarang(barang);
        stok.setJenis(jenis);
        stok.setPasien(pasien);
        stok.setNomor(nomor);
        
        return stok;
    }
    
    @Override
    public void setStok(Stok stok) {
        setStok((StokKembali) stok);
    }
    
    public void setStok(StokKembali stok) {
        setId(stok.getId());
        setJumlah(stok.getJumlah());
        setTanggal(stok.getTanggal());
        setJam(stok.getJam());
        setBarang(stok.getBarang());
        setJenis(stok.getJenis());
        setPasien(stok.getPasien());
        setNomor(stok.getNomor());
    }
}
