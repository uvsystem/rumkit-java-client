package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Pelayanan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
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
            case 0: return "NAMA TINDAKAN";
            case 1: return "NAMA UNIT";
            case 2: return "TANGGAL";
            case 3: return "JUMLAH";
            case 4: return "BIAYA";
            case 5: return "PENANGGUNG";
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
            case 4:
                String tagihan = NumberFormat.getNumberInstance(Locale.US).format(pelayanan.getTagihan());
                return tagihan;
            case 5: return pelayanan.getPenanggung();
            default: return "";
        }
    }
    
    public Pelayanan getPelayanan(int row){
        return list.get(row);
    }
}
