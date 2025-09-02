package com.wo.module.litigation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationProgressPerkaraTableModel<E> extends ListDataModel<LitigationProgressPerkara>
		implements SelectableDataModel<LitigationProgressPerkara> {
    
	public LitigationProgressPerkaraTableModel(List<LitigationProgressPerkara> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public LitigationProgressPerkara getRowData(String rowKey)
	{  
	    @SuppressWarnings("unchecked")
		List<LitigationProgressPerkara> list = (List<LitigationProgressPerkara>) getWrappedData();  
	
	    for(LitigationProgressPerkara ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	}  

	  @Override 
	  public Object getRowKey(LitigationProgressPerkara item) {
		  return item.getSequence();
	  }

	
}
