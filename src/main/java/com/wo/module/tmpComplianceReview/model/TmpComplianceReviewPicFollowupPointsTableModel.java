package com.wo.module.tmpComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpComplianceReviewPicFollowupPointsTableModel<E>
		extends ListDataModel<TmpComplianceReviewPicFollowupPoints>
		implements SelectableDataModel<TmpComplianceReviewPicFollowupPoints> {

	public TmpComplianceReviewPicFollowupPointsTableModel(List<TmpComplianceReviewPicFollowupPoints> data) {
		super(data);
	}

	@Override
	public TmpComplianceReviewPicFollowupPoints getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpComplianceReviewPicFollowupPoints> list = (List<TmpComplianceReviewPicFollowupPoints>) getWrappedData();

		for (TmpComplianceReviewPicFollowupPoints ejb : list) {
			if (ejb.getSeq() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpComplianceReviewPicFollowupPoints item) {
		return item.getSeq();
	}

}
