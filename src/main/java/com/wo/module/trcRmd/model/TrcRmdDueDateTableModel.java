package com.wo.module.trcRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TrcRmdDueDateTableModel<E> extends ListDataModel<TrcRmdDueDate>
		implements SelectableDataModel<TrcRmdDueDate> {
    
	public TrcRmdDueDateTableModel(List<TrcRmdDueDate> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TrcRmdDueDate getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TrcRmdDueDate> list = (List<TrcRmdDueDate>) getWrappedData();  

	    for(TrcRmdDueDate ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TrcRmdDueDate item) {
		  return item.getSequence();
	  }

	
}
