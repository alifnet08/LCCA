package com.wo.module.regulationMonitoring.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class RegMonitoringPICComplianceTrcTableModel<E> extends ListDataModel<RegMonitoringPICComplianceTrc>
		implements SelectableDataModel<RegMonitoringPICComplianceTrc> {
    
	public RegMonitoringPICComplianceTrcTableModel(List<RegMonitoringPICComplianceTrc> data)
	{  
	    super(data);
	}  

	  @Override 
	  public RegMonitoringPICComplianceTrc getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<RegMonitoringPICComplianceTrc> list = (List<RegMonitoringPICComplianceTrc>) getWrappedData();  

	    for(RegMonitoringPICComplianceTrc ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(RegMonitoringPICComplianceTrc item) {
		  return item.getSequence();
	  }

	
}
