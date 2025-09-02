package com.wo.module.litigation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationPicTableModel<E> extends ListDataModel<LitigationPic>
		implements SelectableDataModel<LitigationPic> {
    
	public LitigationPicTableModel(List<LitigationPic> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public LitigationPic getRowData(String rowKey)
	{  
	    @SuppressWarnings("unchecked")
		List<LitigationPic> list = (List<LitigationPic>) getWrappedData();  
	
	    for(LitigationPic ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	}  

	  @Override 
	  public Object getRowKey(LitigationPic item) {
		  return item.getSequence();
	  }

	
}
