package com.dbsys.rs.client.entity;

import com.dbsys.rs.client.entity.Penduduk.Kelamin;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipe"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Dokter.class, name = "DOKTER"),
    @JsonSubTypes.Type(value = Perawat.class, name = "PERAWAT"),
    @JsonSubTypes.Type(value = Apoteker.class, name = "APOTEKER"),
    @JsonSubTypes.Type(value = Pekerja.class, name = "PEKERJA"),
    @JsonSubTypes.Type(value = Pegawai.class, name = "PEGAWAI")
})
public class Pegawai {

    protected Long id;
    protected String nip;
    protected Penduduk penduduk;

    public Pegawai() {
        super();
        this.penduduk = new Penduduk();
    }

    public Pegawai(String name) {
        this();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Penduduk getPenduduk() {
        return penduduk;
    }

    public void setPenduduk(Penduduk penduduk) {
        this.penduduk = penduduk;
    }

    public Long getIdPenduduk() {
        return penduduk.getId();
    }

    public void setIdPenduduk(Long idPenduduk) {
        penduduk.setId(idPenduduk);
    }

    public String getKode() {
        return penduduk.getKode();
    }

    public void setKode(String kode) {
        penduduk.setKode(kode);
    }

    public String getNik() {
        return penduduk.getNik();
    }

    public void setNik(String nik) {
        penduduk.setNik(nik);
    }

    public String getNama() {
        return penduduk.getNama();
    }

    public void setNama(String nama) {
        penduduk.setNama(nama);
    }

    public Kelamin getKelamin() {
        return penduduk.getKelamin();
    }

    public void setKelamin(Kelamin kelamin) {
        penduduk.setKelamin(kelamin);
    }

    public Date getTanggalLahir() {
        return penduduk.getTanggalLahir();
    }

    public void setTanggalLahir(Date tanggalLahir) {
        penduduk.setTanggalLahir(tanggalLahir);
    }

    public String getDarah() {
        return penduduk.getDarah();
    }

    public void setDarah(String darah) {
        penduduk.setDarah(darah);
    }

    public String getAgama() {
        return penduduk.getAgama();
    }

    public void setAgama(String agama) {
        penduduk.setAgama(agama);
    }

    public String getTelepon() {
        return penduduk.getTelepon();
    }

    public void setTelepon(String telepon) {
        penduduk.setTelepon(telepon);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nip == null) ? 0 : nip.hashCode());
        result = prime * result
                + ((penduduk == null) ? 0 : penduduk.hashCode());
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
        Pegawai other = (Pegawai) obj;
        if (id == null) {
                if (other.id != null)
                        return false;
        } else if (!id.equals(other.id))
                return false;
        if (nip == null) {
                if (other.nip != null)
                        return false;
        } else if (!nip.equals(other.nip))
                return false;
        if (penduduk == null) {
                if (other.penduduk != null)
                        return false;
        } else if (!penduduk.equals(other.penduduk))
                return false;
        return true;
    }
}
