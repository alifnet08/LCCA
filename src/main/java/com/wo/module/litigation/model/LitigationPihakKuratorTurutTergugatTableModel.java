package com.wo.module.litigation.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationPihakKuratorTurutTergugatTableModel<E> extends ListDataModel<LitigationPihakKuratorTurutTergugat>
		implements SelectableDataModel<LitigationPihakKuratorTurutTergugat> {
    
	public LitigationPihakKuratorTurutTergugatTableModel(List<LitigationPihakKuratorTurutTergugat> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public LitigationPihakKuratorTurutTergugat getRowData(String rowKey)
	{  
	    @SuppressWarnings("unchecked")
		List<LitigationPihakKuratorTurutTergugat> list = (List<LitigationPihakKuratorTurutTergugat>) getWrappedData();  
	
	    for(LitigationPihakKuratorTurutTergugat ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	}  

	  @Override 
	  public Object getRowKey(LitigationPihakKuratorTurutTergugat item) {
		  return item.getSequence();
	  }

	
}
