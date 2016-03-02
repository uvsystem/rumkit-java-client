package com.dbsys.rs.client.entity;

import com.dbsys.rs.client.entity.Unit.TipeUnit;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipePelayanan"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Pelayanan.class, name = "PELAYANAN"),
    @JsonSubTypes.Type(value = PelayananTemporal.class, name = "TEMPORAL")
})
public class Pelayanan extends Tagihan {

    protected Tindakan tindakan;
    protected Pegawai pelaksana;

    // Untuk JSON bukan JPA
    private String tipePelayanan;

    public Pelayanan() {
        this("PELAYANAN");
    }

    public Pelayanan(String name) {
        super("PELAYANAN");
        this.tipePelayanan = name;
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

    @Override
    public Long getTagihan() {
        /**
         * Jika tindakan dilakukan di ICU, maka biaya tindakan dikali 2.
         */
        if (TipeUnit.ICU.equals(unit.getTipe()))
                return (tindakan.getTarif() * 2) * jumlah + biayaTambahan;

        /**
         * Jika tindakan dilakukan di UGD, maka biaya tindakan ditambah 25%.
         */
        if (TipeUnit.UGD.equals(unit.getTipe())) {
                Long tambahanUgd = tindakan.getTarif() * 25 / 100;
                return (tindakan.getTarif() + tambahanUgd) * jumlah + biayaTambahan;
        }

        return tindakan.getTarif() * jumlah + biayaTambahan;
    }

    @Override
    public String getNama() {
        return tindakan.getNama();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                        + ((tindakan == null) ? 0 : tindakan.hashCode());
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

        Pelayanan other = (Pelayanan) obj;
        if (tindakan == null) {
            if (other.tindakan != null)
                return false;
        } else if (!tindakan.equals(other.tindakan))
            return false;
        return true;
    }
}
