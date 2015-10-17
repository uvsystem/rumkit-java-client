package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.BahanHabisPakai;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class BhpTableModel extends AbstractTableModel {
    private List<BahanHabisPakai> list;

    public BhpTableModel(List<BahanHabisPakai> list) {
        super();
        setList(list);
    }
    
    public void setList(List<BahanHabisPakai> list) {
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
        BahanHabisPakai Bhp = getBhp(rowIndex);
        
        switch(columnIndex) {
            case 0: return Bhp.getKode();
            case 1: return Bhp.getNama();
            default: return "";
        }
    }

    public BahanHabisPakai getBhp(int row) {
        return list.get(row);
    }
}
