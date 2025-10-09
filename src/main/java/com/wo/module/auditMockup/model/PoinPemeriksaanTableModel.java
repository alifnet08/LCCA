package com.wo.module.auditMockup.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class PoinPemeriksaanTableModel<E> extends ListDataModel<PoinPemeriksaan>
		implements SelectableDataModel<PoinPemeriksaan> {
    
	public PoinPemeriksaanTableModel(List<PoinPemeriksaan> data)
	{  
	    super(data);
	}  

	  @Override 
	  public PoinPemeriksaan getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<PoinPemeriksaan> list = (List<PoinPemeriksaan>) getWrappedData();  

	    for(PoinPemeriksaan ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(PoinPemeriksaan item) {
		  return item.getSequence();
	  }

	
}
