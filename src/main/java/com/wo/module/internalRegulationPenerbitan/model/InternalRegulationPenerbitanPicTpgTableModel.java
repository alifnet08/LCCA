package com.wo.module.internalRegulationPenerbitan.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class InternalRegulationPenerbitanPicTpgTableModel<E> extends ListDataModel<InternalRegulationPenerbitanPicTpg>
		implements SelectableDataModel<InternalRegulationPenerbitanPicTpg> {
    
	public InternalRegulationPenerbitanPicTpgTableModel(List<InternalRegulationPenerbitanPicTpg> data)
	{  
	    super(data);
	}  

	  @Override 
	  public InternalRegulationPenerbitanPicTpg getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<InternalRegulationPenerbitanPicTpg> list = (List<InternalRegulationPenerbitanPicTpg>) getWrappedData();  

	    for(InternalRegulationPenerbitanPicTpg ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(InternalRegulationPenerbitanPicTpg item) {
		  return item.getSequence();
	  }

	
}
