package com.wo.module.trcComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TrcComplianceReviewPicFollowupTableModel<E> extends ListDataModel<TrcComplianceReviewPicFollowup>
		implements SelectableDataModel<TrcComplianceReviewPicFollowup> {
    
	public TrcComplianceReviewPicFollowupTableModel(List<TrcComplianceReviewPicFollowup> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TrcComplianceReviewPicFollowup getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TrcComplianceReviewPicFollowup> list = (List<TrcComplianceReviewPicFollowup>) getWrappedData();  

	    for(TrcComplianceReviewPicFollowup ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TrcComplianceReviewPicFollowup item) {
		  return item.getSequence();
	  }

	
}
