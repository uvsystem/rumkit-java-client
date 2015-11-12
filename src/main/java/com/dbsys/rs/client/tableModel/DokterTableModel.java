package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Dokter;
import com.dbsys.rs.lib.entity.Pegawai;
import java.util.List;

/**
 *
 * @author Bramwell Kasaedja
 * @author Deddy Christoper Kakunsi
 */
public class DokterTableModel extends PegawaiTableModel {
    
    public DokterTableModel(List<Dokter> list){
        super();
        for (Pegawai pegawai : list)
            this.list.add(pegawai);
    }
    
    @Override
    public int getColumnCount(){
        return 5;
    }
    
    @Override
    public int getRowCount(){
        if (list == null)
            return 0;
        return list.size();
    }
    
    @Override
    public String getColumnName(int column){
        if (column == 4)
            return "SPESIALISASI";
        return super.getColumnName(column);
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Dokter dokter = getDokter(row);
        
        if (column == 4)
            return dokter.getSpesialisasi();
        return super.getValueAt(row, column);
    }
    
    public Dokter getDokter(int row){
        return (Dokter) getPegawai(row);
    }
}
