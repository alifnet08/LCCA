package com.wo.module.tmpRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TmpRmdDueDateTableModel<E> extends ListDataModel<TmpRmdDueDate>
		implements SelectableDataModel<TmpRmdDueDate> {
    
	public TmpRmdDueDateTableModel(List<TmpRmdDueDate> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpRmdDueDate getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpRmdDueDate> list = (List<TmpRmdDueDate>) getWrappedData();  

	    for(TmpRmdDueDate ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpRmdDueDate item) {
		  return item.getSequence();
	  }

	
}
