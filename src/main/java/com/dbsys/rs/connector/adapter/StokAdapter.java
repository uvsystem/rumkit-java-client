package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.Stok.JenisStok;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.sql.Date;
import java.sql.Time;

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
    @JsonSubTypes.Type(value = StokInternalAdapter.class, name = "INTERNAL"),
    @JsonSubTypes.Type(value = StokEksternalAdapter.class, name = "EKSTERNAL"),
    @JsonSubTypes.Type(value = StokKembaliAdapter.class, name = "KEMBALI"),
    @JsonSubTypes.Type(value = StokAdapter.class, name = "STOK")
})
public class StokAdapter {

    protected Long id;
    protected Long jumlah;
    protected Date tanggal;
    protected Time jam;
    protected Barang barang;
    protected JenisStok jenis;

    // Untuk JSON bukan JPA
    private String tipeStok;

    public StokAdapter() {
        super();
        this.tipeStok = "STOK";
        this.tanggal = DateUtil.getDate();
        this.jam = DateUtil.getTime();
    }
    
    public StokAdapter(Stok stok) {
        this();
        setStok(stok);
    }

    public StokAdapter(String name) {
        this();
        this.tipeStok = name;
    }

    public String getTipe() {
        return tipeStok;
    }

    public void setTipe(String tipeStok) {
        this.tipeStok = tipeStok;
    }

    public String getTipeStok() {
        return tipeStok;
    }

    public void setTipeStok(String tipeStok) {
        this.tipeStok = tipeStok;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }
    
    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Time getJam() {
        return jam;
    }

    public void setJam(Time jam) {
        this.jam = jam;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public JenisStok getJenis() {
        return jenis;
    }

    public void setJenis(JenisStok jenis) {
        this.jenis = jenis;
    }

    @JsonIgnore
    public Stok getStok() {
        Stok stok = new Stok();
        stok.setId(id);
        stok.setJumlah(jumlah);
        stok.setTanggal(tanggal);
        stok.setJam(jam);
        stok.setBarang(barang);
        stok.setJenis(jenis);
        
        return stok;
    }
    
    public void setStok(Stok stok) {
        setId(stok.getId());
        setJumlah(stok.getJumlah());
        setTanggal(stok.getTanggal());
        setJam(stok.getJam());
        setBarang(stok.getBarang());
        setJenis(stok.getJenis());
    }
}
