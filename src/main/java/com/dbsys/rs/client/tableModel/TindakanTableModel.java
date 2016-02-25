package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Tindakan;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public final class TindakanTableModel extends DefaultTableModel {
    private List<Tindakan> list;
    
    public TindakanTableModel(List<Tindakan> list){
        super();
        setList(list);
    }
    
    @Override
    public int getColumnCount(){
        return 6;
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
            case 1: return "NAMA";
            case 2: return "KATEGORI";
            case 3: return "KELAS";
            case 4: return "TANGGUNGAN";
            case 5: return "BIAYA";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Tindakan tindakan = getTindakan(row);
        
        switch(column){
            case 0: return tindakan.getKode();
            case 1: return tindakan.getNama();
            case 2: return tindakan.getKategori().getNama();
            case 3: return tindakan.getKelas();
            case 4: return tindakan.getPenanggung();
            case 5: return tindakan.getTarif();
            default: return "";
        }
    }
    public Tindakan getTindakan(int index){
        return list.get(index);
    }

    public void setList(List<Tindakan> list) {
        this.list = list;
    }
}
