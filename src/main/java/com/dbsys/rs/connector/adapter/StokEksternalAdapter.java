package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokEksternal;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class StokEksternalAdapter extends StokAdapter {

    public StokEksternalAdapter() {
        super("EKSTERNAL");
    }

    public StokEksternalAdapter(StokEksternal stok) {
        this();
        setStok(stok);
    }

    @Override
    @JsonIgnore
    public Stok getStok() {
        StokEksternal stok = new StokEksternal();
        stok.setId(id);
        stok.setJumlah(jumlah);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setBarang(barang);
        stok.setJenis(jenis);
        
        return stok;
    }
}
