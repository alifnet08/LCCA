package com.wo.module.complianceReviewDocument.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ComplianceReviewDocumentPicComplianceTableModel<E> extends ListDataModel<ComplianceReviewDocumentPicCompliance> 
	implements SelectableDataModel<ComplianceReviewDocumentPicCompliance>{

	public ComplianceReviewDocumentPicComplianceTableModel(List<ComplianceReviewDocumentPicCompliance> data) {
		super(data);
	}
	
	@Override
	public Object getRowKey(ComplianceReviewDocumentPicCompliance object) {
		return object.getSequence();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ComplianceReviewDocumentPicCompliance getRowData(String rowKey) {
		
		List<ComplianceReviewDocumentPicCompliance> list = (List<ComplianceReviewDocumentPicCompliance>) getWrappedData();
		
		for (ComplianceReviewDocumentPicCompliance complianceReviewDocumentPicCompliance : list) {
			if(complianceReviewDocumentPicCompliance.getSequence() == new Integer(rowKey)) {
				return complianceReviewDocumentPicCompliance;
			}
		}
		return null;
	}
	
}