package com.wo.module.externalRegulation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;


public class RegulationProposerUnitMstTableModel<E> extends ListDataModel<RegulationProposerUnitMst>
		implements SelectableDataModel<RegulationProposerUnitMst> {
	
	public RegulationProposerUnitMstTableModel(List<RegulationProposerUnitMst> data)
	{  
	    super(data);
	}  

	@Override
	public Object getRowKey(RegulationProposerUnitMst object) {
		 return object.getSequence();
	}

	@SuppressWarnings("deprecation")
	@Override
	public RegulationProposerUnitMst getRowData(String rowKey) {
		 @SuppressWarnings("unchecked")
			List<RegulationProposerUnitMst> list = (List<RegulationProposerUnitMst>) getWrappedData();  

		    for(RegulationProposerUnitMst ejb : list)
		    {  
		      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
		    }
		    return null;  	}
    

	
}
