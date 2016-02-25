package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Pemakaian;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public class PemakaianTableModel extends DefaultTableModel {
    private final List<Pemakaian> list;
    
    public PemakaianTableModel(List<Pemakaian> list){
        super();
        this.list = list;
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
        switch(column){
            case 0: return "NOMOR RESEP";
            case 1: return "NAMA OBAT/BHP";
            case 2: return "TANGGAL";
            case 3: return "JUMLAH";
            case 4: return "BIAYA";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Pemakaian pemakaian = getPemakaian(row);

        switch(column){
            case 0: return pemakaian.getNomorResep();
            case 1: return pemakaian.getBarang().getNama();
            case 2: return pemakaian.getTanggal();
            case 3: return pemakaian.getJumlah();
            case 4: 
                String tagihan = NumberFormat.getNumberInstance(Locale.US).format(pemakaian.getTagihan());
                return tagihan;
            default: return "";
        }
    }
    
    public Pemakaian getPemakaian(int row){
        return list.get(row);
    }
}
