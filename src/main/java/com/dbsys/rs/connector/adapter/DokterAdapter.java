package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.Dokter.Spesialisasi;
import com.dbsys.rs.lib.entity.Pegawai;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class DokterAdapter extends PegawaiAdapter {

    private Spesialisasi spesialisasi;

    public DokterAdapter() {
        super("DOKTER");
    }

    public DokterAdapter(Dokter dokter) {
        this();
        setPegawai(dokter);
    }

    public Spesialisasi getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(Spesialisasi spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    @Override
    @JsonIgnore
    public Pegawai getPegawai() {
        Dokter dokter = new Dokter();
        dokter.setId(id);
        dokter.setNip(nip);
        dokter.setPenduduk(penduduk);
        dokter.setSpesialisasi(spesialisasi);
        
        return dokter;
    }
    
    @Override
    public void setPegawai(Pegawai pegawai) {
        setPegawai((Dokter) pegawai);
    }
    
    public void setPegawai(Dokter dokter) {
        setId(dokter.getId());
        setNip(dokter.getNip());
        setPenduduk(dokter.getPenduduk());
        setSpesialisasi(dokter.getSpesialisasi());
    }
}
