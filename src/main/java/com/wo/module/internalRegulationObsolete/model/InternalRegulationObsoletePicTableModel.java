package com.wo.module.internalRegulationObsolete.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class InternalRegulationObsoletePicTableModel<E> extends ListDataModel<InternalRegulationObsoletePic>
		implements SelectableDataModel<InternalRegulationObsoletePic>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2350324969857041715L;

	public InternalRegulationObsoletePicTableModel(List<InternalRegulationObsoletePic> data)
	{  
	    super(data);
	}  

	  @SuppressWarnings("deprecation")
	  @Override 
	  public InternalRegulationObsoletePic getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<InternalRegulationObsoletePic> list = (List<InternalRegulationObsoletePic>) getWrappedData();  

	    for(InternalRegulationObsoletePic ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(InternalRegulationObsoletePic item) {
		  return item.getSequence();
	  }

	
}
