package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.lib.entity.Pegawai;
import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Tagihan;
import com.dbsys.rs.lib.entity.Tindakan;
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
    @JsonSubTypes.Type(value = PelayananAdapter.class, name = "PELAYANAN"),
    @JsonSubTypes.Type(value = PelayananTemporalAdapter.class, name = "TEMPORAL")
})
public class PelayananAdapter extends Tagihan {

    protected Tindakan tindakan;
    protected Pegawai pelaksana;

    // Untuk JSON bukan JPA
    private String tipePelayanan;

    public PelayananAdapter() {
        super();
        this.tipePelayanan = "PELAYANAN";
    }

    public PelayananAdapter(String name) {
        super();
        this.tipePelayanan = name;
    }
    
    public PelayananAdapter(Pelayanan pelayanan) {
        this();
        setPelayanan(pelayanan);
    }

    public String getTipePelayanan() {
        return tipePelayanan;
    }

    public void setTipePelayanan(String tipePelayanan) {
        this.tipePelayanan = tipePelayanan;
    }

    public Tindakan getTindakan() {
        return tindakan;
    }

    public void setTindakan(Tindakan tindakan) {
        this.tindakan = tindakan;
        this.tanggungan = tindakan;
    }

    public Pegawai getPelaksana() {
        return pelaksana;
    }

    public void setPelaksana(Pegawai pelaksana) {
        this.pelaksana = pelaksana;
    }

    @JsonIgnore
    public Pelayanan getPelayanan() {
        Pelayanan pelayanan = new Pelayanan();
        pelayanan.setId(id);
        pelayanan.setTanggal(tanggal);
        pelayanan.setJumlah(jumlah);
        pelayanan.setBiayaTambahan(biayaTambahan);
        pelayanan.setKeterangan(keterangan);
        pelayanan.setPasien(pasien);
        pelayanan.setUnit(unit);
        pelayanan.setPembayaran(pembayaran);
        pelayanan.setStatus(status);
        pelayanan.setPelaksana(pelaksana);
        pelayanan.setTindakan(tindakan);

        return pelayanan;
    }
    
    public void setPelayanan(Pelayanan pelayanan) {
        setId(pelayanan.getId());
        setTanggal(pelayanan.getTanggal());
        setJumlah(pelayanan.getJumlah());
        setBiayaTambahan(pelayanan.getBiayaTambahan());
        setKeterangan(pelayanan.getKeterangan());
        setPasien(pelayanan.getPasien());
        setUnit(pelayanan.getUnit());
        setPembayaran(pelayanan.getPembayaran());
        setStatus(pelayanan.getStatus());
        setPelaksana(pelayanan.getPelaksana());
        setTindakan(pelayanan.getTindakan());
    }

    @Override
    @JsonIgnore
    public String getNama() {
        return getPelayanan().getNama();
    }

    @Override
    @JsonIgnore
    public Long getTagihan() {
        return getPelayanan().getTagihan();
    }
}
