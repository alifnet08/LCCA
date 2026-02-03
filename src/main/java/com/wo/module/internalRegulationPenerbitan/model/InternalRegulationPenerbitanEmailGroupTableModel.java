package com.wo.module.internalRegulationPenerbitan.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class InternalRegulationPenerbitanEmailGroupTableModel<E> extends ListDataModel<InternalRegulationPenerbitanEmailGroup>
		implements SelectableDataModel<InternalRegulationPenerbitanEmailGroup> {
    
	public InternalRegulationPenerbitanEmailGroupTableModel(List<InternalRegulationPenerbitanEmailGroup> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public InternalRegulationPenerbitanEmailGroup getRowData(String rowKey)
	{  
		@SuppressWarnings("unchecked")
		List<InternalRegulationPenerbitanEmailGroup> list = (List<InternalRegulationPenerbitanEmailGroup>) getWrappedData();  

		for(InternalRegulationPenerbitanEmailGroup ejb : list)
		{  
			if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
		}
		
		return null;  
	}  

	@Override 
	public Object getRowKey(InternalRegulationPenerbitanEmailGroup item) {
		return item.getSequence();
	}
	
}
