package com.wo.module.tmpAudit.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpAuditPICFollowupTableModel<E> extends ListDataModel<TmpAuditPicFollowup>
		implements SelectableDataModel<TmpAuditPicFollowup> {
    
	public TmpAuditPICFollowupTableModel(List<TmpAuditPicFollowup> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpAuditPicFollowup getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpAuditPicFollowup> list = (List<TmpAuditPicFollowup>) getWrappedData();  

	    for(TmpAuditPicFollowup ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpAuditPicFollowup item) {
		  return item.getSequence();
	  }

	
}
