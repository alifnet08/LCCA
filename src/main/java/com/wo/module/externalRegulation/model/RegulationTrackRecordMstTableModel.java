package com.wo.module.externalRegulation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class RegulationTrackRecordMstTableModel<E> extends ListDataModel<RegulationTrackRecordMst>
		implements SelectableDataModel<RegulationTrackRecordMst> {
	
	public RegulationTrackRecordMstTableModel(List<RegulationTrackRecordMst> data)
	{  
	    super(data);
	}  

	@Override
	public Object getRowKey(RegulationTrackRecordMst object) {
		 return object.getSequence();
	}

	@Override
	public RegulationTrackRecordMst getRowData(String rowKey) {
		 @SuppressWarnings("unchecked")
			List<RegulationTrackRecordMst> list = (List<RegulationTrackRecordMst>) getWrappedData();  

		    for(RegulationTrackRecordMst ejb : list)
		    {  
		      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
		    }
		    return null;  	}
    

	
}
