package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Dokter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class DokterTableModel extends DefaultTableModel {
    private final List<Dokter> listDokter;
    
    public DokterTableModel(List<Dokter> list){
        super();
        listDokter = list;
    }
    
    @Override
    public int getColumnCount(){
        return 4;
    }
    
    @Override
    public int getRowCount(){
        if (listDokter == null)
            return 0;
        return listDokter.size();
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0:return "NAMA";
            case 1:return "NIP";
            case 2:return "KODE";
            case 3:return "SPESIALISASI";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Dokter dokter = getDokter(row);
        switch(column){
            case 0:return dokter.getNama();
            case 1:return dokter.getNip();
            case 2:return dokter.getKode();
            case 3:return dokter.getSpesialisasi();
            default: return "";
        }
    }
    public Dokter getDokter(int row){
        return listDokter.get(row);
    }
}
