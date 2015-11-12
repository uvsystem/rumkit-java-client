package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Perawat;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class PerawatTableModel extends DefaultTableModel {
    private final List<Perawat> list;
    
    public PerawatTableModel(List<Perawat> list){
        super();
        this.list = list;
    }
    
    @Override
    public int getColumnCount(){
        return 3;
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
            case 0:return "NAMA";
            case 1:return "NIP";
            case 2:return "KODE";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Perawat perawat = getPerawat(row);
        switch(column){
            case 0:return perawat.getNama();
            case 1:return perawat.getNip();
            case 2:return perawat.getKode();
            default: return "";
        }
    }
    
    public Perawat getPerawat(int row){
        return list.get(row);
    }
}
