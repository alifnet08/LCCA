package com.wo.module.regulationSocialization.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class SocializationPICFollowupTrcTableModel<E> extends ListDataModel<SocializationPICFollowupTrc>
		implements SelectableDataModel<SocializationPICFollowupTrc> {
    
	public SocializationPICFollowupTrcTableModel(List<SocializationPICFollowupTrc> data)
	{  
	    super(data);
	}  

	  @Override 
	  public SocializationPICFollowupTrc getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<SocializationPICFollowupTrc> list = (List<SocializationPICFollowupTrc>) getWrappedData();  

	    for(SocializationPICFollowupTrc ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(SocializationPICFollowupTrc item) {
		  return item.getSequence();
	  }

	
}
