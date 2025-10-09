package com.wo.module.regulationMonitoring.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class RegMonitoringPICFollowUpTrcTableModel<E> extends ListDataModel<RegMonitoringPICFollowUpTrc>
		implements SelectableDataModel<RegMonitoringPICFollowUpTrc> {
    
	public RegMonitoringPICFollowUpTrcTableModel(List<RegMonitoringPICFollowUpTrc> data)
	{  
	    super(data);
	}  

	  @Override 
	  public RegMonitoringPICFollowUpTrc getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<RegMonitoringPICFollowUpTrc> list = (List<RegMonitoringPICFollowUpTrc>) getWrappedData();  

	    for(RegMonitoringPICFollowUpTrc ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(RegMonitoringPICFollowUpTrc item) {
		  return item.getSequence();
	  }

	
}
