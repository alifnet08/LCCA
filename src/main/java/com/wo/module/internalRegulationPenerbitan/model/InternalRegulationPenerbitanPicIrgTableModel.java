package com.wo.module.internalRegulationPenerbitan.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class InternalRegulationPenerbitanPicIrgTableModel<E> extends ListDataModel<InternalRegulationPenerbitanPicIrg>
		implements SelectableDataModel<InternalRegulationPenerbitanPicIrg> {
    
	public InternalRegulationPenerbitanPicIrgTableModel(List<InternalRegulationPenerbitanPicIrg> data)
	{  
	    super(data);
	}  

	  @Override 
	  public InternalRegulationPenerbitanPicIrg getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<InternalRegulationPenerbitanPicIrg> list = (List<InternalRegulationPenerbitanPicIrg>) getWrappedData();  

	    for(InternalRegulationPenerbitanPicIrg ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(InternalRegulationPenerbitanPicIrg item) {
		  return item.getSequence();
	  }

	
}
