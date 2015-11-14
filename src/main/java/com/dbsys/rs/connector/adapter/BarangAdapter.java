package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.Penanggung;
import com.dbsys.rs.lib.entity.Barang;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
    @JsonSubTypes.Type(value = BahanHabisPakaiAdapter.class, name = "BHP"),
    @JsonSubTypes.Type(value = ObatAdapter.class, name = "OBAT"),
    @JsonSubTypes.Type(value = Barang.class, name = "BARANG")
})
public class BarangAdapter {

    protected Long id;
    protected String kode;
    protected String nama;
    protected Long jumlah;
    protected String satuan;
    protected Long harga;
    protected Penanggung penanggung;

    // Untuk JSON bukan JPA
    private String tipeBarang;

    public BarangAdapter() {
        super();
        this.tipeBarang = "BARANG";
    }
    
    public BarangAdapter(Barang barang) {
        this();
        setBarang(barang);
    }

    public BarangAdapter(String tipeBarang) {
        super();
        this.tipeBarang = tipeBarang;
    }

    public String getTipe() {
        return tipeBarang;
    }

    public void setTipe(String tipeBarang) {
        this.tipeBarang = tipeBarang;
    }

    public String getTipeBarang() {
        return tipeBarang;
    }

    public void setTipeBarang(String tipeBarang) {
        this.tipeBarang = tipeBarang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public Long getHarga() {
        return harga;
    }

    public void setHarga(Long harga) {
        this.harga = harga;
    }

    public Penanggung getPenanggung() {
        return penanggung;
    }

    public void setPenanggung(Penanggung penanggung) {
        this.penanggung = penanggung;
    }
    
    @JsonIgnore
    public Barang getBarang() {
        Barang barang = new Barang();
        barang.setId(id);
        barang.setKode(kode);
        barang.setNama(nama);
        barang.setJumlah(jumlah);
        barang.setSatuan(satuan);
        barang.setHarga(harga);
        barang.setPenanggung(penanggung);
        
        return barang;
    }
    
    public void setBarang(Barang barang) {
        setId(barang.getId());
        setKode(barang.getKode());
        setNama(barang.getNama());
        setJumlah(barang.getJumlah());
        setSatuan(barang.getSatuan());
        setHarga(barang.getHarga());
        setPenanggung(barang.getPenanggung());
    }
}
