package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.sql.Date;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	include = JsonTypeInfo.As.PROPERTY,
	property = "tipe"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = DokterAdapter.class, name = "DOKTER"),
	@JsonSubTypes.Type(value = PerawatAdapter.class, name = "PERAWAT"),
	@JsonSubTypes.Type(value = ApotekerAdapter.class, name = "APOTEKER"),
	@JsonSubTypes.Type(value = PekerjaAdapter.class, name = "PEKERJA"),
	@JsonSubTypes.Type(value = PegawaiAdapter.class, name = "PEGAWAI")
})
public class PegawaiAdapter {

    protected Long id;
    protected String nip;
    protected Penduduk penduduk;

    // Untuk JSON buka JPA
    private String tipePegawai;

    public PegawaiAdapter() {
        super();
        setTipePegawai("PEGAWAI");
    }

    public PegawaiAdapter(Pegawai pegawai) {
        this();
        setPegawai(pegawai);
    }
    
    public PegawaiAdapter(String tipePegawai) {
        super();
        setTipePegawai(tipePegawai);
    }

    public String getTipe() {
        return tipePegawai;
    }

    public void setTipe(String tipe) {
        this.tipePegawai = tipe;
    }

    public String getTipePegawai() {
        return tipePegawai;
    }

    public final void setTipePegawai(String tipePegawai) {
        this.tipePegawai = tipePegawai;
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
    
    @JsonIgnore
    public Pegawai getPegawai() {
        Pegawai pegawai = new Pegawai();
        pegawai.setId(id);
        pegawai.setNip(nip);
        pegawai.setPenduduk(penduduk);
        
        return pegawai;
    }
    
    public void setPegawai(Pegawai pegawai) {
        setId(pegawai.getId());
        setNip(pegawai.getNip());
        setPenduduk(pegawai.getPenduduk());
    }
}
