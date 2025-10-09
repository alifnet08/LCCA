package com.wo.module.tmpRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TmpRmdRegulationTableModel<E> extends ListDataModel<TmpRmdRegulation>
		implements SelectableDataModel<TmpRmdRegulation> {
    
	public TmpRmdRegulationTableModel(List<TmpRmdRegulation> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpRmdRegulation getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpRmdRegulation> list = (List<TmpRmdRegulation>) getWrappedData();  

	    for(TmpRmdRegulation ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpRmdRegulation item) {
		  return item.getSequence();
	  }

	
}
