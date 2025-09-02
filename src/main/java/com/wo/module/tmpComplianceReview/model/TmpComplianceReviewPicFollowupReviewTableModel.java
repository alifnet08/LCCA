package com.wo.module.tmpComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpComplianceReviewPicFollowupReviewTableModel<E>
		extends ListDataModel<TmpComplianceReviewPicFollowupReview>
		implements SelectableDataModel<TmpComplianceReviewPicFollowupReview> {

	public TmpComplianceReviewPicFollowupReviewTableModel(List<TmpComplianceReviewPicFollowupReview> data) {
		super(data);
	}

	@Override
	public TmpComplianceReviewPicFollowupReview getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpComplianceReviewPicFollowupReview> list = (List<TmpComplianceReviewPicFollowupReview>) getWrappedData();

		for (TmpComplianceReviewPicFollowupReview ejb : list) {
			if (ejb.getSeq() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpComplianceReviewPicFollowupReview item) {
		return item.getSeq();
	}

}
