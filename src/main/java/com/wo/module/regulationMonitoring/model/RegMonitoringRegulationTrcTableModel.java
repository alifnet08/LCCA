package com.wo.module.regulationMonitoring.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class RegMonitoringRegulationTrcTableModel<E> extends ListDataModel<RegMonitoringRegulationTrc>
		implements SelectableDataModel<RegMonitoringRegulationTrc> {
    
	public RegMonitoringRegulationTrcTableModel(List<RegMonitoringRegulationTrc> data)
	{  
	    super(data);
	}  

	  @Override 
	  public RegMonitoringRegulationTrc getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<RegMonitoringRegulationTrc> list = (List<RegMonitoringRegulationTrc>) getWrappedData();  

	    for(RegMonitoringRegulationTrc ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(RegMonitoringRegulationTrc item) {
		  return item.getSequence();
	  }

	
}
