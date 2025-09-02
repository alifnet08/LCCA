package com.wo.module.litigation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationPihakPenggugatPemohonTableModel<E> extends ListDataModel<LitigationPihakPenggugatPemohon>
		implements SelectableDataModel<LitigationPihakPenggugatPemohon> {
    
	public LitigationPihakPenggugatPemohonTableModel(List<LitigationPihakPenggugatPemohon> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public LitigationPihakPenggugatPemohon getRowData(String rowKey)
	{  
	    @SuppressWarnings("unchecked")
		List<LitigationPihakPenggugatPemohon> list = (List<LitigationPihakPenggugatPemohon>) getWrappedData();  
	
	    for(LitigationPihakPenggugatPemohon ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	}  

	  @Override 
	  public Object getRowKey(LitigationPihakPenggugatPemohon item) {
		  return item.getSequence();
	  }

	
}
