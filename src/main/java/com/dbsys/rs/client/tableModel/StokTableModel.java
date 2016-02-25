package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Stok;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class StokTableModel extends  AbstractTableModel {

    private List<Stok> list;

    public StokTableModel() {
        super();
        this.list = new ArrayList<>();
    }
    
    public StokTableModel(List<Stok> list) {
        super();
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
            case 0: return "KODE";
            case 1: return "NAMA";
            case 2: return "JUMLAH";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Stok stok = getStok(rowIndex);
        
        switch(columnIndex) {
            case 0: return stok.getBarang().getKode();
            case 1: return stok.getBarang().getNama();
            case 2: return stok.getJumlah();
            default: return "";
        }
    }

    public Stok getStok(int row) {
        return list.get(row);
    }
}
