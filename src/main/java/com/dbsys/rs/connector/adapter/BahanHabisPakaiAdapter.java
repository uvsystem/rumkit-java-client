package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class BahanHabisPakaiAdapter extends BarangAdapter{

    public BahanHabisPakaiAdapter() {
        super("BHP");
    }

    public BahanHabisPakaiAdapter(BahanHabisPakai bhp) {
        this();
        setBarang(bhp);
    }
    
    @Override
    @JsonIgnore
    public Barang getBarang() {
        BahanHabisPakai bhp = new BahanHabisPakai();
        bhp.setId(id);
        bhp.setKode(kode);
        bhp.setNama(nama);
        bhp.setJumlah(jumlah);
        bhp.setSatuan(satuan);
        bhp.setHarga(harga);
        bhp.setPenanggung(penanggung);
        
        return bhp;
    }
    
    @Override
    public void setBarang(Barang barang) {
        setBarang((BahanHabisPakai) barang);
    }
    
    public void setBarang(BahanHabisPakai bhp) {
        setId(bhp.getId());
        setKode(bhp.getKode());
        setNama(bhp.getNama());
        setJumlah(bhp.getJumlah());
        setSatuan(bhp.getSatuan());
        setHarga(bhp.getHarga());
        setPenanggung(bhp.getPenanggung());
    }
}
