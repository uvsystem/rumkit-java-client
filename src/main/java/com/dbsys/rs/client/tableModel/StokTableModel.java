package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Stok;
import com.dbsys.rs.lib.entity.StokKembali;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class StokTableModel extends  AbstractTableModel {
    protected List<Stok> list;

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
        return 4;
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0: return "KODE";
            case 1: return "NAMA";
            case 2: return "JUMLAH";
            case 3: return "PENGEMBALIAN";
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
            case 3: return ((StokKembali)stok).hitungPengembalian();
            default: return "";
        }
    }

    public Stok getStok(int row) {
        return list.get(row);
    }
}
