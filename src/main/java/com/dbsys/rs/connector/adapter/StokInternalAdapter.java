package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokInternal;
import com.dbsys.rs.lib.entity.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class StokInternalAdapter extends StokAdapter {

    private Unit unit;

    public StokInternalAdapter() {
        super("INTERNAL");
    }

    public StokInternalAdapter(StokInternal stok) {
        this();
        setStok(stok);
    }
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    @JsonIgnore
    public Stok getStok() {
        StokInternal stok = new StokInternal();
        stok.setId(id);
        stok.setJumlah(jumlah);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setBarang(barang);
        stok.setJenis(jenis);
        stok.setUnit(unit);
        
        return stok;
    }
    
    @Override
    public void setStok(Stok stok) {
        setStok((StokInternal) stok);
    }
    
    public void setStok(StokInternal stok) {
        setId(stok.getId());
        setJumlah(stok.getJumlah());
        setTanggal(stok.getTanggal());
        setJam(stok.getJam());
        setBarang(stok.getBarang());
        setJenis(stok.getJenis());
        setUnit(stok.getUnit());
    }
}
