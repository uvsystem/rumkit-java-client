package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Pelayanan;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 * 
 */
public class PelayananTableModel extends DefaultTableModel {
    private final List<Pelayanan> list;
    
    public PelayananTableModel(List<Pelayanan> list){
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
            case 0: return "NAMA TINDAKAN";
            case 1: return "NAMA UNIT";
            case 2: return "TANGGAL";
            case 3: return "JUMLAH";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Pelayanan pelayanan = getPelayanan(row);

        switch(column){
            case 0: return pelayanan.getTindakan().getNama();
            case 1: return pelayanan.getUnit().getNama();
            case 2: return pelayanan.getTanggal();
            case 3: return pelayanan.getJumlah();
            default: return "";
        }
    }
    
    public Pelayanan getPelayanan(int row){
        return list.get(row);
    }
}
