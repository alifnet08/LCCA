package com.wo.module.regulationSocialization.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class SocializationRegulationTrcTableModel<E> extends ListDataModel<SocializationRegulationTrc>
		implements SelectableDataModel<SocializationRegulationTrc> {
    
	public SocializationRegulationTrcTableModel(List<SocializationRegulationTrc> data)
	{  
	    super(data);
	}  

	  @Override 
	  public SocializationRegulationTrc getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<SocializationRegulationTrc> list = (List<SocializationRegulationTrc>) getWrappedData();  

	    for(SocializationRegulationTrc ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(SocializationRegulationTrc item) {
		  return item.getSequence();
	  }

	
}
