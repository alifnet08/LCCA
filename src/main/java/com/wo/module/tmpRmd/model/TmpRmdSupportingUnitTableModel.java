package com.wo.module.tmpRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TmpRmdSupportingUnitTableModel<E> extends ListDataModel<TmpRmdSupportingUnit>
		implements SelectableDataModel<TmpRmdSupportingUnit> {
    
	public TmpRmdSupportingUnitTableModel(List<TmpRmdSupportingUnit> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpRmdSupportingUnit getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpRmdSupportingUnit> list = (List<TmpRmdSupportingUnit>) getWrappedData();  

	    for(TmpRmdSupportingUnit ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpRmdSupportingUnit item) {
		  return item.getSequence();
	  }

	
}
