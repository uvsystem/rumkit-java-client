package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Pegawai;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class PegawaiTableModel extends DefaultTableModel {
    protected List<Pegawai> list;

    public PegawaiTableModel() {
        super();
        this.list = new ArrayList<>();
    }
    
    public PegawaiTableModel(List<Pegawai> list){
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
            case 0: return "KODE";
            case 1: return "NIP";
            case 2: return "NAMA";
            case 3: return "TELEPON";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Pegawai pegawai = getPegawai(row);
        
        switch(column){
            case 0: return pegawai.getKode();
            case 1: return pegawai.getNip();
            case 2: return pegawai.getNama();
            case 3: return pegawai.getTelepon();
            default: return "";
        }
    }
    
    public Pegawai getPegawai(int row){
        return list.get(row);
    }
}
