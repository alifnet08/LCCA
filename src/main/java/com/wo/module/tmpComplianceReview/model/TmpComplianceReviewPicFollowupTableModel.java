package com.wo.module.tmpComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TmpComplianceReviewPicFollowupTableModel<E> extends ListDataModel<TmpComplianceReviewPicFollowup>
		implements SelectableDataModel<TmpComplianceReviewPicFollowup> {
    
	public TmpComplianceReviewPicFollowupTableModel(List<TmpComplianceReviewPicFollowup> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TmpComplianceReviewPicFollowup getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TmpComplianceReviewPicFollowup> list = (List<TmpComplianceReviewPicFollowup>) getWrappedData();  

	    for(TmpComplianceReviewPicFollowup ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TmpComplianceReviewPicFollowup item) {
		  return item.getSequence();
	  }

	
}
