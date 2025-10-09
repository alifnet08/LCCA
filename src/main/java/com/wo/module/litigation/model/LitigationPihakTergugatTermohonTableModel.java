package com.wo.module.litigation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationPihakTergugatTermohonTableModel<E> extends ListDataModel<LitigationPihakTergugatTermohon>
		implements SelectableDataModel<LitigationPihakTergugatTermohon> {
    
	public LitigationPihakTergugatTermohonTableModel(List<LitigationPihakTergugatTermohon> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public LitigationPihakTergugatTermohon getRowData(String rowKey)
	{  
	    @SuppressWarnings("unchecked")
		List<LitigationPihakTergugatTermohon> list = (List<LitigationPihakTergugatTermohon>) getWrappedData();  
	
	    for(LitigationPihakTergugatTermohon ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	}  

	  @Override 
	  public Object getRowKey(LitigationPihakTergugatTermohon item) {
		  return item.getSequence();
	  }

	
}
