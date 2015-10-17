package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.ObatFarmasi;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class ObatTableModel extends AbstractTableModel {
    private List<ObatFarmasi> list;

    public ObatTableModel(List<ObatFarmasi> list) {
        super();
        setList(list);
    }
    
    public void setList(List<ObatFarmasi> list) {
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
        return 2;
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0:return "KODE";
            case 1:return "NAMA";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObatFarmasi obat = getObat(rowIndex);
        
        switch(columnIndex) {
            case 0: return obat.getKode();
            case 1: return obat.getNama();
            default: return "";
        }
    }

    public ObatFarmasi getObat(int row) {
        return list.get(row);
    }
}
