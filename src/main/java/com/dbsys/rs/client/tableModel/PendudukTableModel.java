package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Penduduk;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class PendudukTableModel extends AbstractTableModel {
    private List<Penduduk> list;

    public PendudukTableModel(List<Penduduk> list) {
        super();
        this.list = list;
    }
    
    public void setList(List<Penduduk> list) {
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
            case 0:return "NAMA";
            case 1:return "NOMOR JAMINAN";
            case 2:return "NOMOR REKAM MEDIK";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Penduduk penduduk = getPenduduk(rowIndex);
        
        switch(columnIndex) {
            case 0: return penduduk.getNama();
            case 1: return penduduk.getNik();
            case 2: return penduduk.getKode();
            default: return "";
        }
    }

    public Penduduk getPenduduk(int row) {
        return list.get(row);
    }
}
