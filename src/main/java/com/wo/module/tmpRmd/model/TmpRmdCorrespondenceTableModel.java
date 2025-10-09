package com.wo.module.tmpRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TmpRmdCorrespondenceTableModel<E> extends ListDataModel<TmpRmdCorrespondence>
		implements SelectableDataModel<TmpRmdCorrespondence> {
    
	public TmpRmdCorrespondenceTableModel(List<TmpRmdCorrespondence> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpRmdCorrespondence getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpRmdCorrespondence> list = (List<TmpRmdCorrespondence>) getWrappedData();  

	    for(TmpRmdCorrespondence ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpRmdCorrespondence item) {
		  return item.getSequence();
	  }

	
}
