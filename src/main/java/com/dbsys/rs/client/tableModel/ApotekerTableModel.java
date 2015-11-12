package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Apoteker;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class ApotekerTableModel extends DefaultTableModel {
    private final List<Apoteker> list;
    
    public ApotekerTableModel(List<Apoteker> list){
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
        Apoteker apoteker = getApoteker(row);
        switch(column){
            case 0:return apoteker.getNama();
            case 1:return apoteker.getNip();
            case 2:return apoteker.getKode();
            default: return "";
        }
    }
    
    public Apoteker getApoteker(int row){
        return list.get(row);
    }
}
