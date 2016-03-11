package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.StokKembali;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class StokKembaliTableModel extends  StokTableModel {

    private final List<StokKembali> list = new ArrayList<>();
    
    public StokKembaliTableModel(List<StokKembali> list) {
        super();
        for (StokKembali stok : list)
            this.list.add(stok);
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
        StokKembali stok = getStok(rowIndex);
        
        switch(columnIndex) {
            case 0: return stok.getKode();
            case 1: return stok.getBarang().getNama();
            case 2: return stok.getJumlah();
            case 3: return stok.hitungPengembalian();
            default: return "";
        }
    }

    @Override
    public StokKembali getStok(int row) {
        return (StokKembali) list.get(row);
    }
    
}
