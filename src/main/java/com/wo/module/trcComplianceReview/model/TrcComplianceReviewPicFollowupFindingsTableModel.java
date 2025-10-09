package com.wo.module.trcComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcComplianceReviewPicFollowupFindingsTableModel<E>
		extends ListDataModel<TrcComplianceReviewPicFollowupFindings>
		implements SelectableDataModel<TrcComplianceReviewPicFollowupFindings> {

	public TrcComplianceReviewPicFollowupFindingsTableModel(List<TrcComplianceReviewPicFollowupFindings> data) {
		super(data);
	}

	@Override
	public TrcComplianceReviewPicFollowupFindings getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcComplianceReviewPicFollowupFindings> list = (List<TrcComplianceReviewPicFollowupFindings>) getWrappedData();

		for (TrcComplianceReviewPicFollowupFindings ejb : list) {
			if (ejb.getSeq() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcComplianceReviewPicFollowupFindings item) {
		return item.getSeq();
	}

}
