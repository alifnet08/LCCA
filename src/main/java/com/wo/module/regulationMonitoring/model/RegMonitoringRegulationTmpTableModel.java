package com.wo.module.regulationMonitoring.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class RegMonitoringRegulationTmpTableModel<E> extends ListDataModel<RegMonitoringRegulationTmp>
		implements SelectableDataModel<RegMonitoringRegulationTmp> {
    
	public RegMonitoringRegulationTmpTableModel(List<RegMonitoringRegulationTmp> data)
	{  
	    super(data);
	}  

	  @Override 
	  public RegMonitoringRegulationTmp getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<RegMonitoringRegulationTmp> list = (List<RegMonitoringRegulationTmp>) getWrappedData();  

	    for(RegMonitoringRegulationTmp ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(RegMonitoringRegulationTmp item) {
		  return item.getSequence();
	  }

	
}
