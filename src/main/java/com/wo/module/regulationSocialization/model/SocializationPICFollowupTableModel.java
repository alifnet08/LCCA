package com.wo.module.regulationSocialization.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class SocializationPICFollowupTableModel<E> extends ListDataModel<SocializationPICFollowupTmp>
		implements SelectableDataModel<SocializationPICFollowupTmp> {
    
	public SocializationPICFollowupTableModel(List<SocializationPICFollowupTmp> data)
	{  
	    super(data);
	}  

	  @Override 
	  public SocializationPICFollowupTmp getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<SocializationPICFollowupTmp> list = (List<SocializationPICFollowupTmp>) getWrappedData();  

	    for(SocializationPICFollowupTmp ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(SocializationPICFollowupTmp item) {
		  return item.getSequence();
	  }

	
}
