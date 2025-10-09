package com.wo.module.trcRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TrcRmdRegulationTableModel<E> extends ListDataModel<TrcRmdRegulation>
		implements SelectableDataModel<TrcRmdRegulation> {
    
	public TrcRmdRegulationTableModel(List<TrcRmdRegulation> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TrcRmdRegulation getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TrcRmdRegulation> list = (List<TrcRmdRegulation>) getWrappedData();  

	    for(TrcRmdRegulation ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TrcRmdRegulation item) {
		  return item.getSequence();
	  }

	
}
