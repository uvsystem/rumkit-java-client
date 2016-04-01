package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Unit;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class UnitTableModel extends DefaultTableModel {

    private final List<Unit> listUnit;
    
    public UnitTableModel(List<Unit> list){
        super();
        listUnit = list;
    }
    
    @Override
    public int getColumnCount(){
        return 2;
    }
    
    @Override
    public int getRowCount(){
        if (listUnit == null)
            return 0;
        return listUnit.size();
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0:return "NAMA";
            case 1:return "TIPE";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Unit unit = getUnit(row);
        switch(column){
            case 0:return unit.getNama();
            case 1:return unit.getTipe();
            default: return "";
        }
    }
    public Unit getUnit(int row){
        return listUnit.get(row);
    }
}
