package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Pekerja;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class PekerjaAdapter extends PegawaiAdapter {

    public PekerjaAdapter() {
        super("PEKERJA");
    }

    public PekerjaAdapter(Pekerja pekerja) {
        this();
        setPegawai(pekerja);
    }

    @Override
    @JsonIgnore
    public Pegawai getPegawai() {
        Pekerja pekerja = new Pekerja();
        pekerja.setId(id);
        pekerja.setNip(nip);
        pekerja.setPenduduk(penduduk);
        
        return pekerja;
    }
}
