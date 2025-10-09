package com.wo.module.tmpComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TmpComplianceReviewPicComplianceTableModel<E> extends ListDataModel<TmpComplianceReviewPicCompliance>
		implements SelectableDataModel<TmpComplianceReviewPicCompliance> {
    
	public TmpComplianceReviewPicComplianceTableModel(List<TmpComplianceReviewPicCompliance> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpComplianceReviewPicCompliance getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpComplianceReviewPicCompliance> list = (List<TmpComplianceReviewPicCompliance>) getWrappedData();  

	    for(TmpComplianceReviewPicCompliance ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpComplianceReviewPicCompliance item) {
		  return item.getSequence();
	  }

	
}
