package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Barang;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class BarangTableModel extends AbstractTableModel {

    protected List<Barang> list;

    public BarangTableModel() {
        super();
        this.list = new ArrayList<>();
    }
    
    public BarangTableModel(List<Barang> list) {
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
            case 0:return "KODE";
            case 1:return "NAMA";
            case 2:return "HARGA";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Barang barang = getBarang(rowIndex);
        
        switch(columnIndex) {
            case 0: return barang.getKode();
            case 1: return barang.getNama();
            case 2: return barang.getHarga();
            default: return "";
        }
    }

    public Barang getBarang(int row) {
        return list.get(row);
    }

}
