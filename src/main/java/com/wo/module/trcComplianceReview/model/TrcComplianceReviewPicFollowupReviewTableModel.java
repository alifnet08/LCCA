package com.wo.module.trcComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcComplianceReviewPicFollowupReviewTableModel<E>
		extends ListDataModel<TrcComplianceReviewPicFollowupReview>
		implements SelectableDataModel<TrcComplianceReviewPicFollowupReview> {

	public TrcComplianceReviewPicFollowupReviewTableModel(List<TrcComplianceReviewPicFollowupReview> data) {
		super(data);
	}

	@Override
	public TrcComplianceReviewPicFollowupReview getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcComplianceReviewPicFollowupReview> list = (List<TrcComplianceReviewPicFollowupReview>) getWrappedData();

		for (TrcComplianceReviewPicFollowupReview ejb : list) {
			if (ejb.getSeq() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcComplianceReviewPicFollowupReview item) {
		return item.getSeq();
	}

}
