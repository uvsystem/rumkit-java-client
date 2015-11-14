package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class ObatAdapter extends BarangAdapter {

    private String keterangan;
    
    public ObatAdapter() {
        super("OBAT");
    }

    public ObatAdapter(ObatFarmasi obat) {
        this();
        setBarang(obat);
    }
    
    public String getKeterangan() {
        return keterangan;
    }
    
    public void setKeterangan(String keterangan) {
        this.keterangan= keterangan;
    }

    @Override
    @JsonIgnore
    public Barang getBarang() {
        ObatFarmasi obat = new ObatFarmasi();
        obat.setId(id);
        obat.setKode(kode);
        obat.setNama(nama);
        obat.setJumlah(jumlah);
        obat.setSatuan(satuan);
        obat.setHarga(harga);
        obat.setPenanggung(penanggung);
        obat.setKeterangan(keterangan);
        
        return obat;
    }
    
    @Override
    public void setBarang(Barang barang) {
        setBarang((ObatFarmasi) barang);
    }

    public void setBarang(ObatFarmasi obat) {
        setId(obat.getId());
        setKode(obat.getKode());
        setNama(obat.getNama());
        setJumlah(obat.getJumlah());
        setSatuan(obat.getSatuan());
        setHarga(obat.getHarga());
        setPenanggung(obat.getPenanggung());
        setKeterangan(obat.getKeterangan());
    }
}
