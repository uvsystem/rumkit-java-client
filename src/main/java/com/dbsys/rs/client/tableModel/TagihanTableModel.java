package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Pelayanan;
import com.dbsys.rs.client.entity.Pemakaian;
import com.dbsys.rs.client.entity.Tagihan;

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
        Tagihan tagihan = getTagihan(row);
        Long totalTagihan = tagihan.getTagihan();
        
        switch(column){
            case 0: return tagihan.getNama();
            case 1: return tagihan.getNamaUnit();
            case 2: return tagihan.getTanggal();
            case 3: return tagihan.getJumlah();
            case 4:
                return NumberFormat.getNumberInstance(Locale.US).format(totalTagihan);
            case 5: return tagihan.getPenanggung();
            default: return "";
        }
    }
    
    public Tagihan getTagihan(int index){
        return list.get(index);
    }
    
    public void addTagihan(Tagihan tagihan) {
        list.add(tagihan);
    }
    
    public void removeTagihan(Tagihan tagihan) {
        list.remove(tagihan);
    }

    public List<Tagihan> getList() {
        return list;
    }

    public void setList(List<Tagihan> list) {
        if (list == null)
            list = new ArrayList<>();
        this.list = list;
    }
    
    public void addListPelayanan(List<Pelayanan> list) {
        if (list == null)
            list = new ArrayList<>();

        for (Pelayanan pelayanan : list)
            this.list.add(pelayanan);
    }
    
    public void addListPemakaian(List<Pemakaian> list) {
        if (list == null)
            list = new ArrayList<>();

        for (Pemakaian pemakaian : list)
            this.list.add(pemakaian);
    }
    
    public Long getTotal() {
        Long total = 0L;
        for (Tagihan tagihan : list)
            total += tagihan.getTagihanCounted();
        return total;
    }
    
    public TagihanTableModel filter(Tagihan.StatusTagihan status) {
        List<Tagihan> listBaru = new ArrayList<>();
        for (Tagihan tagihan : list) {
            if (status.equals(tagihan.getStatus()))
                listBaru.add(tagihan);
        }
        
        return new TagihanTableModel(listBaru);
    }
}
