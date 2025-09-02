package com.wo.module.trcComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;



public class TrcComplianceReviewPicComplianceTableModel<E> extends ListDataModel<TrcComplianceReviewPicCompliance>
		implements SelectableDataModel<TrcComplianceReviewPicCompliance> {
    
	public TrcComplianceReviewPicComplianceTableModel(List<TrcComplianceReviewPicCompliance> data)
	{  
	    super(data);
	}  

	  @Override 
	  public TrcComplianceReviewPicCompliance getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<TrcComplianceReviewPicCompliance> list = (List<TrcComplianceReviewPicCompliance>) getWrappedData();  

	    for(TrcComplianceReviewPicCompliance ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(TrcComplianceReviewPicCompliance item) {
		  return item.getSequence();
	  }

	
}
