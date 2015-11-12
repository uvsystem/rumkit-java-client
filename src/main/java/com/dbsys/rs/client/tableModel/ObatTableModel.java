package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.ObatFarmasi;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Deddy Christoper Kakunsi
 */
public final class ObatTableModel extends BarangTableModel {

    public ObatTableModel(List<ObatFarmasi> list) {
        super();
        List<Barang> listBarang = new ArrayList<>();
        for (Barang barang : list)
            listBarang.add(barang);
        
        this.list = listBarang;
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
        return (ObatFarmasi) super.getBarang(row);
    }
}
