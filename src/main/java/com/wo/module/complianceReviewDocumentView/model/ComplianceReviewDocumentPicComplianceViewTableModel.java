package com.wo.module.complianceReviewDocumentView.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ComplianceReviewDocumentPicComplianceViewTableModel<E> extends ListDataModel<ComplianceReviewDocumentPicComplianceView> 
	implements SelectableDataModel<ComplianceReviewDocumentPicComplianceView>{

	public ComplianceReviewDocumentPicComplianceViewTableModel(List<ComplianceReviewDocumentPicComplianceView> data) {
		super(data);
	}
	
	@Override
	public Object getRowKey(ComplianceReviewDocumentPicComplianceView object) {
		return object.getSequence();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ComplianceReviewDocumentPicComplianceView getRowData(String rowKey) {
		List<ComplianceReviewDocumentPicComplianceView> list = (List<ComplianceReviewDocumentPicComplianceView>) getWrappedData();
		
		for (ComplianceReviewDocumentPicComplianceView complianceReviewDocumentPicComplianceView : list) {
			if(complianceReviewDocumentPicComplianceView.getSequence() == new Integer(rowKey)) {
				return complianceReviewDocumentPicComplianceView;
			}
		}
		return null;
	}
	
}