package com.wo.module.cpsa.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class CompliancePlanSelfAssessmentPicTableModel<E> extends ListDataModel<CompliancePlanSelfAssessmentPic>
		implements SelectableDataModel<CompliancePlanSelfAssessmentPic> {
    
	public CompliancePlanSelfAssessmentPicTableModel(List<CompliancePlanSelfAssessmentPic> data)
	{  
	    super(data);
	}  

	@SuppressWarnings("deprecation")
	@Override 
	public CompliancePlanSelfAssessmentPic getRowData(String rowKey)
	{  
	    @SuppressWarnings("unchecked")
		List<CompliancePlanSelfAssessmentPic> list = (List<CompliancePlanSelfAssessmentPic>) getWrappedData();  
	
	    for(CompliancePlanSelfAssessmentPic ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	}  

	  @Override 
	  public Object getRowKey(CompliancePlanSelfAssessmentPic item) {
		  return item.getSequence();
	  }

	
}
