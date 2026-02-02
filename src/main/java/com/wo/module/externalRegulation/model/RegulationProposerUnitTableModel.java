package com.wo.module.externalRegulation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;


public class RegulationProposerUnitTableModel<E> extends ListDataModel<RegulationProposerUnit>
		implements SelectableDataModel<RegulationProposerUnit> {
	
	public RegulationProposerUnitTableModel(List<RegulationProposerUnit> data)
	{  
	    super(data);
	}  

	@Override
	public Object getRowKey(RegulationProposerUnit object) {
		 return object.getSequence();
	}

	@SuppressWarnings("deprecation")
	@Override
	public RegulationProposerUnit getRowData(String rowKey) {
		 @SuppressWarnings("unchecked")
			List<RegulationProposerUnit> list = (List<RegulationProposerUnit>) getWrappedData();  

		    for(RegulationProposerUnit ejb : list)
		    {  
		      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
		    }
		    return null;  	}
    

	
}
