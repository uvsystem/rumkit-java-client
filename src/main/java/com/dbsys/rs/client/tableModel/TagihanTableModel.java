package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Pelayanan;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.PemakaianObat;
import com.dbsys.rs.lib.entity.Tagihan;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public final class TagihanTableModel extends DefaultTableModel {
    private List<Tagihan> list;
    
    public TagihanTableModel(List<Tagihan> list){
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
            case 0: return "NAMA";
            case 1: return "UNIT";
            case 2: return "TANGGAL";
            case 3: return "JUMLAH";
            case 4: return "BIAYA";
            case 5: return "TANGGUNGAN";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Tagihan tagihan = getTindakan(row);
        
        switch(column){
            case 0: return tagihan.getNama();
            case 1: return tagihan.getNamaUnit();
            case 2: return tagihan.getTanggal();
            case 3: return tagihan.getJumlah();
            case 4:
                return NumberFormat.getNumberInstance(Locale.US).format(tagihan.getTagihan());
            case 5: return tagihan.getTanggungan();
            default: return "";
        }
    }
    public Tagihan getTindakan(int index){
        return list.get(index);
    }

    public void setList(List<Tagihan> list) {
        if (list == null)
            list = new ArrayList<>();
        this.list = list;
    }
    
    public void addListPelayanan(List<Pelayanan> list) {
        for (Pelayanan pelayanan : list)
            this.list.add(pelayanan);
    }
    
    public void addListPemakaian(List<Pemakaian> list) {
        for (Pemakaian pemakaian : list)
            this.list.add(pemakaian);
    }
    
    public void addListPemakaianObat(List<PemakaianObat> list) {
        for (Pemakaian pemakaian : list)
            this.list.add(pemakaian);
    }
    
    public void addListPemakaianBhp(List<PemakaianBhp> list) {
        for (Pemakaian pemakaian : list)
            this.list.add(pemakaian);
    }
}
