package com.dbsys.rs.connector.adapter;

import com.dbsys.rs.client.Penanggung;
import com.dbsys.rs.client.entity.Unit.TipeUnit;

/**
 *
 * @author Deddy Christoper Kakunsi - deddy.kakunsi@gmail.com
 */
public class RekapTagihanAdapter {

    private Long id;
    private String pasien;
    private String nama;
    private Long jumlah;
    private Long tarif;
    private Long tambahan;
    private String unit;
    private Penanggung penanggung;
    private TipeUnit tipe;
    private Long total;
    
    private String tipeTagihan;

    public RekapTagihanAdapter() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPasien() {
        return pasien;
    }

    public void setPasien(String pasien) {
        this.pasien = pasien;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Penanggung getPenanggung() {
        return penanggung;
    }

    public void setPenanggung(Penanggung penanggung) {
        this.penanggung = penanggung;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public Long getTarif() {
        return tarif;
    }

    public void setTarif(Long tarif) {
        this.tarif = tarif;
    }

    public Long getTambahan() {
        return tambahan;
    }

    public void setTambahan(Long tambahan) {
        this.tambahan = tambahan;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public TipeUnit getTipe() {
        return tipe;
    }

    public void setTipe(TipeUnit tipe) {
        this.tipe = tipe;
    }

    public String getTipeTagihan() {
        return tipeTagihan;
    }

    public void setTipeTagihan(String tipeTagihan) {
        this.tipeTagihan = tipeTagihan;
    }
    
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Long getTagihan() {
        /**
         * Jika tindakan dilakukan di ICU, maka biaya tindakan dikali 2.
         */
        if (TipeUnit.ICU.equals(tipe))
                return (tarif * 2) * jumlah + tambahan;

        /**
         * Jika tindakan dilakukan di UGD, maka biaya tindakan ditambah 25%.
         */
        if (TipeUnit.UGD.equals(tipe)) {
                Long tambahanUgd = tarif * 25 / 100;
                return (tarif + tambahanUgd) * jumlah + tambahan;
        }

        return tarif * jumlah + tambahan;
    }

    @Override
    public String toString() {
        return "RekapTagihanAdapter{" + "id=" + id + ", pasien=" + pasien + ", nama=" + nama + ", jumlah=" + jumlah + ", tarif=" + tarif + ", tambahan=" + tambahan + ", unit=" + unit + ", penanggung=" + penanggung + ", tipe=" + tipe + ", tipeTagihan=" + tipeTagihan + '}';
    }
}
