package com.wo.module.regulationSocialization.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class SocializationPICComplianceTableModel<E> extends ListDataModel<SocializationPICComplianceTmp>
		implements SelectableDataModel<SocializationPICComplianceTmp> {
    
	public SocializationPICComplianceTableModel(List<SocializationPICComplianceTmp> data)
	{  
	    super(data);
	}  

	  @Override 
	  public SocializationPICComplianceTmp getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<SocializationPICComplianceTmp> list = (List<SocializationPICComplianceTmp>) getWrappedData();  

	    for(SocializationPICComplianceTmp ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(SocializationPICComplianceTmp item) {
		  return item.getSequence();
	  }

	
}
