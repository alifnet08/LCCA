package com.wo.module.counterType.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class CounterTypeDtlTableModel<E> extends ListDataModel<CounterTypeDtl>
		implements SelectableDataModel<CounterTypeDtl> {
    
	public CounterTypeDtlTableModel(List<CounterTypeDtl> data)
	{  
	    super(data);
	}  

	  @Override 
	  public CounterTypeDtl getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<CounterTypeDtl> list = (List<CounterTypeDtl>) getWrappedData();  

	    for(CounterTypeDtl ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(CounterTypeDtl item) {
		  return item.getSequence();
	  }

	
}
