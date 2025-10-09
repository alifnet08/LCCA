package com.wo.module.regulationSocialization.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class SocializationPICComplianceTrcTableModel<E> extends ListDataModel<SocializationPICComplianceTrc>
		implements SelectableDataModel<SocializationPICComplianceTrc> {
    
	public SocializationPICComplianceTrcTableModel(List<SocializationPICComplianceTrc> data)
	{  
	    super(data);
	}  

	  @Override 
	  public SocializationPICComplianceTrc getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<SocializationPICComplianceTrc> list = (List<SocializationPICComplianceTrc>) getWrappedData();  

	    for(SocializationPICComplianceTrc ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(SocializationPICComplianceTrc item) {
		  return item.getSequence();
	  }

	
}
