package com.wo.module.internalRegulationPenerbitan.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class InternalRegulationPenerbitanPicTpkTableModel<E> extends ListDataModel<InternalRegulationPenerbitanPicTpk>
		implements SelectableDataModel<InternalRegulationPenerbitanPicTpk> {
    
	public InternalRegulationPenerbitanPicTpkTableModel(List<InternalRegulationPenerbitanPicTpk> data)
	{  
	    super(data);
	}  

	  @Override 
	  public InternalRegulationPenerbitanPicTpk getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<InternalRegulationPenerbitanPicTpk> list = (List<InternalRegulationPenerbitanPicTpk>) getWrappedData();  

	    for(InternalRegulationPenerbitanPicTpk ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(InternalRegulationPenerbitanPicTpk item) {
		  return item.getSequence();
	  }

	
}
