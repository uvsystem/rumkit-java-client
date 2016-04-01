package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Pasien;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class PasienTableModel extends AbstractTableModel {
    private List<Pasien> list;

    public PasienTableModel(List<Pasien> list) {
        super();
        this.list = list;
    }
    
    public void setList(List<Pasien> list) {
        this.list = list;
    }

    @Override
    public int getRowCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0: return "KODE";
            case 1: return "NAMA";
            case 2: return "KELAS";
            case 3: return "TANGGUNGAN";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pasien pasien = getPasien(rowIndex);
        
        switch(columnIndex) {
            case 0: return pasien.getKode();
            case 1: return pasien.getNama();
            case 2: return pasien.getKelas();
            case 3: return pasien.getPenanggung();
            default: return "";
        }
    }

    public Pasien getPasien(int row) {
        return list.get(row);
    }
}
