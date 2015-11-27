package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Unit;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class PasienCariTableModel extends AbstractTableModel {
    private List<Pasien> list;

    public PasienCariTableModel(List<Pasien> list) {
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
        return 3;
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0: return "NAMA";
            case 1: return "PERAWATAN";
            case 2: return "RUANG PERAWATAN";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pasien pasien = getPasien(rowIndex);
        
        switch(columnIndex) {
            case 0: return pasien.getNama();
            case 1: return pasien.getTipePerawatan();
            case 2: 
                Unit ruangPerawatan = pasien.getRuangPerawatan();
                if (ruangPerawatan != null)
                    return ruangPerawatan.getNama();
                return "";
            default: return "";
        }
    }

    public Pasien getPasien(int row) {
        return list.get(row);
    }
}
