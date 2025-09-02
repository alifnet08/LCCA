package com.wo.module.litigation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationPutusanPengadilanTableModel<E> extends ListDataModel<LitigationPutusanPengadilan>
		implements SelectableDataModel<LitigationPutusanPengadilan> {
    
	public LitigationPutusanPengadilanTableModel(List<LitigationPutusanPengadilan> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public LitigationPutusanPengadilan getRowData(String rowKey)
	{  
	    @SuppressWarnings("unchecked")
		List<LitigationPutusanPengadilan> list = (List<LitigationPutusanPengadilan>) getWrappedData();  
	
	    for(LitigationPutusanPengadilan ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	}  

	  @Override 
	  public Object getRowKey(LitigationPutusanPengadilan item) {
		  return item.getSequence();
	  }

	
}
