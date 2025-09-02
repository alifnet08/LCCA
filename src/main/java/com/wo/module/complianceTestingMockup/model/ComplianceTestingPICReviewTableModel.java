package com.wo.module.complianceTestingMockup.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ComplianceTestingPICReviewTableModel<E> extends ListDataModel<ComplianceTestingPICReview>
		implements SelectableDataModel<ComplianceTestingPICReview> {
    
	public ComplianceTestingPICReviewTableModel(List<ComplianceTestingPICReview> data)
	{  
	    super(data);
	}  

	  @Override 
	  public ComplianceTestingPICReview getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<ComplianceTestingPICReview> list = (List<ComplianceTestingPICReview>) getWrappedData();  

	    for(ComplianceTestingPICReview ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(ComplianceTestingPICReview item) {
		  return item.getSequence();
	  }

	
}
