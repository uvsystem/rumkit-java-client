package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Pemakaian;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class PemakaianTableModel extends DefaultTableModel {
    private final List<Pemakaian> list;
    
    public PemakaianTableModel(List<Pemakaian> list){
        super();
        this.list = list;
    }
    
    @Override
    public int getColumnCount(){
        return 4;
    }
    
    @Override
    public int getRowCount(){
        if (list == null)
            return 0;
        return list.size();
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0: return "NAMA BARANG";
            case 1: return "NAMA UNIT";
            case 2: return "TANGGAL";
            case 3: return "JUMLAH";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Pemakaian pemakaian = getPemakaian(row);

        switch(column){
            case 0: return pemakaian.getBarang().getNama();
            case 1: return pemakaian.getUnit().getNama();
            case 2: return pemakaian.getTanggal();
            case 3: return pemakaian.getJumlah();
            default: return "";
        }
    }
    public Pemakaian getPemakaian(int row){
        return list.get(row);
    }
}
