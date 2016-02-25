package com.dbsys.rs.client.entity;

public class Unit {

    public enum TipeUnit {
        LOKET_PENDAFTARAN, 
        LOKET_PEMBAYARAN, 
        POLIKLINIK, 
        RUANG_PERAWATAN, 
        APOTEK_FARMASI,
        GUDANG_FARMASI,
        PENUNJANG_MEDIK,
        PENUNJANG_NON_MEDIK,
        ICU,
        UGD
    }

    private Long id;
    private String nama;
    private Float bobot;
    private TipeUnit tipe;

    public Unit() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Float getBobot() {
        return bobot;
    }

    public void setBobot(Float bobot) {
        this.bobot = bobot;
    }

    public TipeUnit getTipe() {
        return tipe;
    }

    public void setTipe(TipeUnit tipe) {
        this.tipe = tipe;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bobot == null) ? 0 : bobot.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nama == null) ? 0 : nama.hashCode());
        result = prime * result + ((tipe == null) ? 0 : tipe.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        Unit other = (Unit) obj;
        if (bobot == null) {
            if (other.bobot != null)
                return false;
        } else if (!bobot.equals(other.bobot))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nama == null) {
            if (other.nama != null)
                return false;
        } else if (!nama.equals(other.nama))
            return false;
        if (tipe == null) {
            if (tipe != other.tipe)
                return false;
        }
        return true;
    }
}
