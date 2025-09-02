package com.wo.module.tmpAudit.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpAuditCheckPointTableModel<E> extends ListDataModel<TmpAuditCheckPoint>
		implements SelectableDataModel<TmpAuditCheckPoint> {
    
	public TmpAuditCheckPointTableModel(List<TmpAuditCheckPoint> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpAuditCheckPoint getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpAuditCheckPoint> list = (List<TmpAuditCheckPoint>) getWrappedData();  

	    for(TmpAuditCheckPoint ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpAuditCheckPoint item) {
		  return item.getSequence();
	  }

	
}
