package com.dbsys.rs.client.entity;

public class ObatFarmasi extends Barang {

    private String keterangan;

    public ObatFarmasi() {
        super("OBAT");
    }

    public ObatFarmasi(String keterangan) {
        this();
        this.keterangan = keterangan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                        + ((keterangan == null) ? 0 : keterangan.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        ObatFarmasi other = (ObatFarmasi) obj;
        if (keterangan == null) {
            if (other.keterangan != null)
                return false;
        } else if (!keterangan.equals(other.keterangan))
            return false;
        return true;
    }
}
