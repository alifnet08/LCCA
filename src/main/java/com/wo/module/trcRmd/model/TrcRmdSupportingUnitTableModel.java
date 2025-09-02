package com.wo.module.trcRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TrcRmdSupportingUnitTableModel<E> extends ListDataModel<TrcRmdSupportingUnit>
		implements SelectableDataModel<TrcRmdSupportingUnit> {
    
	public TrcRmdSupportingUnitTableModel(List<TrcRmdSupportingUnit> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TrcRmdSupportingUnit getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TrcRmdSupportingUnit> list = (List<TrcRmdSupportingUnit>) getWrappedData();  

	    for(TrcRmdSupportingUnit ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TrcRmdSupportingUnit item) {
		  return item.getSequence();
	  }

	
}
