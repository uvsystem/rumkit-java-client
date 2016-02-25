package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Barang;
import com.dbsys.rs.client.entity.ObatFarmasi;

import java.util.List;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class ObatTableModel extends BarangTableModel {

    public ObatTableModel() {
        super();
    }
    
    public ObatTableModel(List<ObatFarmasi> list) {
        super();
        setList(list);
    }
    
    public void setList(List<ObatFarmasi> list) {
        for (Barang barang : list)
            this.list.add(barang);
    }
    
    public void setListBarang(List<Barang> list) {
        this.list = list;
    }

    @Override
    public int getRowCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    public String getColumnName(int column){
        if (column == 3)
            return "KETERANGAN";
        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObatFarmasi obat = getObat(rowIndex);
        
        if (columnIndex == 3)
            return obat.getKeterangan();
        return super.getValueAt(rowIndex, columnIndex);
    }

    public ObatFarmasi getObat(int row) {
        return (ObatFarmasi) getBarang(row);
    }
}
