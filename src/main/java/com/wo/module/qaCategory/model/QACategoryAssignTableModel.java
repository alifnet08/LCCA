package com.wo.module.qaCategory.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class QACategoryAssignTableModel<E> extends ListDataModel<QACategory>
		implements SelectableDataModel<QACategory> {
    
	public QACategoryAssignTableModel(List<QACategory> data)
	{  
	    super(data);
	}  

	  @Override 
	  public QACategory getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<QACategory> list = (List<QACategory>) getWrappedData();  

	    for(QACategory ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(QACategory item) {
		  return item.getSequence();
	  }

	
}
