package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Apoteker;
import com.dbsys.rs.lib.entity.Pegawai;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class ApotekerAdapter extends PegawaiAdapter {

    public ApotekerAdapter() {
        super("APOTEKER");
    }

    public ApotekerAdapter(Apoteker apoteker) {
        this();
        setPegawai(apoteker);
    }

    @Override
    @JsonIgnore
    public Pegawai getPegawai() {
        Apoteker apoteker = new Apoteker();
        apoteker.setId(id);
        apoteker.setNip(nip);
        apoteker.setPenduduk(penduduk);
        
        return apoteker;
    }
    
    @Override
    public void setPegawai(Pegawai pegawai) {
        setPegawai((Apoteker) pegawai);
    }
    
    public void setPegawai(Apoteker apoteker) {
        setId(apoteker.getId());
        setNip(apoteker.getNip());
        setPenduduk(apoteker.getPenduduk());
    }
}
