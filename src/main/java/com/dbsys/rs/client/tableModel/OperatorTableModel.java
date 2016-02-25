package com.dbsys.rs.client.tableModel;

import com.dbsys.rs.client.entity.Operator;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bramwell Kasaedja
 */
public class OperatorTableModel extends DefaultTableModel {

    private final List<Operator> listOperator;
    
    public OperatorTableModel(List<Operator> list){
        super();
        listOperator = list;
    }
    
    @Override
    public int getColumnCount(){
        return 5;
    }
    
    @Override
    public int getRowCount(){
        if (listOperator==null)
            return 0;
        return listOperator.size();
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0:return "NAMA";
            case 1:return "USER NAME";
            case 2:return "PASSWORD";
            case 3:return "UNIT";
            case 4:return "ROLE";
            default: return "";
        }
    }
    
    @Override
    public Object getValueAt(int row, int column){
        Operator operator = getOperator(row);
        switch(column){
            case 0:return operator.getNama();
            case 1:return operator.getUsername();
            case 2:return operator.getPassword();
            case 3:return operator.getUnit().getNama();
            case 4:return operator.getRole();
            default: return "";
        }
    }
    public Operator getOperator(int row){
        return listOperator.get(row);
    }
}
