package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Perawat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class PerawatAdapter extends PegawaiAdapter {

    public PerawatAdapter() {
        super("PERAWAT");
    }

    public PerawatAdapter(Perawat perawat) {
        this();
        setPegawai(perawat);
    }

    @Override
    @JsonIgnore
    public Pegawai getPegawai() {
        Perawat perawat = new Perawat();
        perawat.setId(id);
        perawat.setNip(nip);
        perawat.setPenduduk(penduduk);
        
        return perawat;
    }
    
    @Override
    public void setPegawai(Pegawai pegawai) {
        setPegawai((Perawat) pegawai);
    }
    
    public void setPegawai(Perawat perawat) {
        setId(perawat.getId());
        setNip(perawat.getNip());
        setPenduduk(perawat.getPenduduk());
    }
}
