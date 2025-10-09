package com.wo.module.externalRegulation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class RegulationTrackRecordTableModel<E> extends ListDataModel<RegulationTrackRecord>
		implements SelectableDataModel<RegulationTrackRecord> {
    
	public RegulationTrackRecordTableModel(List<RegulationTrackRecord> data)
	{  
	    super(data);
	}  

	  @Override 
	  public RegulationTrackRecord getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<RegulationTrackRecord> list = (List<RegulationTrackRecord>) getWrappedData();  

	    for(RegulationTrackRecord ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(RegulationTrackRecord item) {
		  return item.getSequence();
	  }

	
}
