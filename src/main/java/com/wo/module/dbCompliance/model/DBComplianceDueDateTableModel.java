package com.wo.module.dbCompliance.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class DBComplianceDueDateTableModel<E> extends ListDataModel<DBComplianceDueDate>
		implements SelectableDataModel<DBComplianceDueDate> {
    
	public DBComplianceDueDateTableModel(List<DBComplianceDueDate> data)
	{  
	    super(data);
	}  

	  @Override 
	  public DBComplianceDueDate getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<DBComplianceDueDate> list = (List<DBComplianceDueDate>) getWrappedData();  

	    for(DBComplianceDueDate ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(DBComplianceDueDate item) {
		  return item.getSequence();
	  }

	
}
