package com.wo.module.regulationMonitoring.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class RegMonitoringPICFollowUpTmpTableModel<E> extends ListDataModel<RegMonitoringPICFollowUpTmp>
		implements SelectableDataModel<RegMonitoringPICFollowUpTmp> {
    
	public RegMonitoringPICFollowUpTmpTableModel(List<RegMonitoringPICFollowUpTmp> data)
	{  
	    super(data);
	}  

	  @Override 
	  public RegMonitoringPICFollowUpTmp getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<RegMonitoringPICFollowUpTmp> list = (List<RegMonitoringPICFollowUpTmp>) getWrappedData();  

	    for(RegMonitoringPICFollowUpTmp ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(RegMonitoringPICFollowUpTmp item) {
		  return item.getSequence();
	  }

	
}
