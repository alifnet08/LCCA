package com.wo.module.complianceTestingMockup.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.wo.module.complianceTestingMockup.vo.PICTindakLanjutVO;



public class PICTindakLanjutTableModel<E> extends ListDataModel<ComplianceTestingPICFollowup>
		implements SelectableDataModel<ComplianceTestingPICFollowup> {
    
	public PICTindakLanjutTableModel(List<ComplianceTestingPICFollowup> data)
	{  
	    super(data);
	}  

	  @Override 
	  public ComplianceTestingPICFollowup getRowData(String rowKey)
	  {  
	    @SuppressWarnings("unchecked")
		List<ComplianceTestingPICFollowup> list = (List<ComplianceTestingPICFollowup>) getWrappedData();  

	    for(ComplianceTestingPICFollowup ejb : list)
	    {  
	      if(ejb.getSequence() == new Integer(rowKey).intValue()){return ejb;}  
	    }
	    return null;  
	  }  

	  @Override 
	  public Object getRowKey(ComplianceTestingPICFollowup item) {
		  return item.getSequence();
	  }

	
}
