package com.wo.module.trcComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcComplianceReviewPicFollowupPointsTableModel<E>
		extends ListDataModel<TrcComplianceReviewPicFollowupPoints>
		implements SelectableDataModel<TrcComplianceReviewPicFollowupPoints> {

	public TrcComplianceReviewPicFollowupPointsTableModel(List<TrcComplianceReviewPicFollowupPoints> data) {
		super(data);
	}

	@Override
	public TrcComplianceReviewPicFollowupPoints getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcComplianceReviewPicFollowupPoints> list = (List<TrcComplianceReviewPicFollowupPoints>) getWrappedData();

		for (TrcComplianceReviewPicFollowupPoints ejb : list) {
			if (ejb.getSeq() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcComplianceReviewPicFollowupPoints item) {
		return item.getSeq();
	}

}
