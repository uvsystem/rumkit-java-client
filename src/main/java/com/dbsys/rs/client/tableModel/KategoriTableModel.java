package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.KategoriTindakan;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class KategoriTableModel extends DefaultTableModel {
    
    private final List<KategoriTindakan> list;
    
    public KategoriTableModel(List<KategoriTindakan> list){
        super();
        this.list = list;
    }
    
    @Override
    public int getColumnCount(){
        return 2;
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
            case 1:return "KATEGORI";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        KategoriTindakan tindakan = getKategori(row);
        
        switch(column){
            case 0: return tindakan.getNama();
            case 1: {
                if (tindakan.getParent() == null)
                    return "";
                return tindakan.getParent().getNama();
            }
            default: return "";
        }
    }
    public KategoriTindakan getKategori(int index){
        return list.get(index);
    }
}
