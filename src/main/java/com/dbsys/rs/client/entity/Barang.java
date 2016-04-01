package com.dbsys.rs.client.entity;

import com.dbsys.rs.client.CodedEntity;
import com.dbsys.rs.client.DateUtil;
import com.dbsys.rs.client.NumberException;
import com.dbsys.rs.client.Tanggungan;
import com.dbsys.rs.client.Penanggung;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipe"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BahanHabisPakai.class, name = "BHP"),
    @JsonSubTypes.Type(value = ObatFarmasi.class, name = "OBAT"),
    @JsonSubTypes.Type(value = Barang.class, name = "BARANG")
})
public class Barang implements Tanggungan, CodedEntity {

    protected Long id;
    protected String kode;
    protected String nama;
    protected Long jumlah;
    protected String satuan;
    protected Long harga;
    protected Penanggung penanggung;

    // Untuk JSON bukan JPA
    private String tipeBarang;

    public Barang() {
        super();
        this.tipeBarang = "BARANG";
    }

    public Barang(String name) {
        super();
        this.tipeBarang = name;
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

    @Override
    public String getKode() {
        return kode;
    }

    @Override
    public void setKode(String kode) {
        this.kode = kode;
    }

    @Override
    public String generateKode() {
        kode = createKode();
        return kode;
    }

    public static String createKode() {
        Integer d = Math.abs(DateUtil.getDate().hashCode());
        Integer t = Math.abs(DateUtil.getTime().hashCode());

        return String.format("50%s00%s", d, t);
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

    public void tambah(Long jumlah) {
        this.jumlah += jumlah;
    }

    public void kurang(Long jumlah) throws NumberException {
        if (this.jumlah < jumlah)
            throw new NumberException(String.format("Jumlah barang tidak cukup untuk dikurangi.\n Jumlah saat ini %d", this.jumlah));
        this.jumlah -= jumlah;
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

    @Override
    public Penanggung getPenanggung() {
        return penanggung;
    }

    public void setPenanggung(Penanggung penanggung) {
        this.penanggung = penanggung;
    }

    public Long add(Integer jumlah) {
        this.jumlah += jumlah;

        return this.jumlah;
    }

    public Long substract(Integer jumlah) throws NumberException {
        if (this.jumlah < jumlah)
            throw new NumberException("Maaf, jumlah tidak mencukupi.");
        this.jumlah -= jumlah;

        return this.jumlah;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((harga == null) ? 0 : harga.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((jumlah == null) ? 0 : jumlah.hashCode());
        result = prime * result + ((kode == null) ? 0 : kode.hashCode());
        result = prime * result + ((nama == null) ? 0 : nama.hashCode());
        result = prime * result + ((satuan == null) ? 0 : satuan.hashCode());
        result = prime * result
                        + ((penanggung == null) ? 0 : penanggung.hashCode());
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

        Barang other = (Barang) obj;
        if (harga == null) {
            if (other.harga != null)
                return false;
        } else if (!harga.equals(other.harga))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jumlah == null) {
            if (other.jumlah != null)
                return false;
        } else if (!jumlah.equals(other.jumlah))
            return false;
        if (kode == null) {
            if (other.kode != null)
                return false;
        } else if (!kode.equals(other.kode))
            return false;
        if (nama == null) {
            if (other.nama != null)
                return false;
        } else if (!nama.equals(other.nama))
            return false;
        if (satuan == null) {
            if (other.satuan != null)
                return false;
        } else if (!satuan.equals(other.satuan))
            return false;
        if (penanggung != other.penanggung)
            return false;
        return true;
    }
}
