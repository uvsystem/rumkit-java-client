/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.lib.entity.Unit;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ronald
 */
public class UnitTableModel extends DefaultTableModel {
    private List<Unit> listUnit;
    
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
        if (listUnit==null)
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
            default: return"";
        }
    }
    public Unit getUnit(int row){
        return listUnit.get(row);
    }
}
