package com.wo.module.regulationSocialization.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class SocializationRegulationTableModel<E> extends ListDataModel<SocializationRegulationTmp>
		implements SelectableDataModel<SocializationRegulationTmp> {
    
	public SocializationRegulationTableModel(List<SocializationRegulationTmp> data)
	{  
	    super(data);
	}  

	  @Override 
	  public SocializationRegulationTmp getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<SocializationRegulationTmp> list = (List<SocializationRegulationTmp>) getWrappedData();  

	    for(SocializationRegulationTmp ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(SocializationRegulationTmp item) {
		  return item.getSequence();
	  }

	
}
