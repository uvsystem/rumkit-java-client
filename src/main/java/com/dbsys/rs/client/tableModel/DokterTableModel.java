package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Dokter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class DokterTableModel extends DefaultTableModel {
    private List<Dokter> listDokter;
    
    public DokterTableModel(List<Dokter> list){
        super();
        listDokter = list;
    }
    
    @Override
    public int getColumnCount(){
        return 5;
    }
    
    @Override
    public int getRowCount(){
        if (listDokter==null)
            return 0;
        return listDokter.size();
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0:return "NAMA";
            case 1:return "NIP";
            case 2:return "NIK";
            case 3:return "KODE";
            case 4:return "JENIS KELAMIN";
            case 5:return "TANGGAL lAHIR";
            case 6:return "GOL DARAH";
            case 7:return "AGAMA";
            case 8:return "TELEPON";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Dokter dokter = getDokter(row);
        switch(column){
            case 0:return dokter.getNama();
            case 1:return dokter.getNip();
            case 2:return dokter.getNik();
            case 3:return dokter.getKode();
            case 4:return dokter.getKelamin();
            case 5:return dokter.getTanggalLahir();
            case 6:return dokter.getDarah();
            case 7:return dokter.getAgama();
            case 8:return dokter.getTelepon();
            default: return "";
        }
    }
    public Dokter getDokter(int row){
        return listDokter.get(row);
    }
}
