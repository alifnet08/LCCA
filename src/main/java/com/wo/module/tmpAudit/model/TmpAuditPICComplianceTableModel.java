package com.wo.module.tmpAudit.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpAuditPICComplianceTableModel<E> extends ListDataModel<TmpAuditPicCompliance>
		implements SelectableDataModel<TmpAuditPicCompliance> {
    
	public TmpAuditPICComplianceTableModel(List<TmpAuditPicCompliance> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpAuditPicCompliance getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpAuditPicCompliance> list = (List<TmpAuditPicCompliance>) getWrappedData();  

	    for(TmpAuditPicCompliance ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpAuditPicCompliance item) {
		  return item.getSequence();
	  }

	
}
