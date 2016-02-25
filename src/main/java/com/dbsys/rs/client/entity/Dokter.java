package com.dbsys.rs.client.entity;

public class Dokter extends Pegawai {

    public enum Spesialisasi {
        UMUM, MATA, BEDAH, KANDUNGAN
    }

    private Spesialisasi spesialisasi;

    public Dokter() {
        super("DOKTER");
    }

    public Dokter(Spesialisasi spesialisasi) {
        this();
        this.spesialisasi = spesialisasi;
    }

    public Spesialisasi getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(Spesialisasi spesialisasi) {
        this.spesialisasi = spesialisasi;
    }
}
