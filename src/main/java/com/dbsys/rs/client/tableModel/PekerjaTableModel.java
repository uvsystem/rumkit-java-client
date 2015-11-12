package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Pekerja;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class PekerjaTableModel extends DefaultTableModel {
    private final List<Pekerja> list;
    
    public PekerjaTableModel(List<Pekerja> list){
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
        Pekerja pekerja = getPekerja(row);
        switch(column){
            case 0:return pekerja.getNama();
            case 1:return pekerja.getNip();
            case 2:return pekerja.getKode();
            default: return "";
        }
    }
    
    public Pekerja getPekerja(int row){
        return list.get(row);
    }
}
