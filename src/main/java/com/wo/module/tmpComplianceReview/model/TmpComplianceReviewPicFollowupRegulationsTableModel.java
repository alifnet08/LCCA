package com.wo.module.tmpComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpComplianceReviewPicFollowupRegulationsTableModel<E>
		extends ListDataModel<TmpComplianceReviewPicFollowupRegulation>
		implements SelectableDataModel<TmpComplianceReviewPicFollowupRegulation> {

	public TmpComplianceReviewPicFollowupRegulationsTableModel(List<TmpComplianceReviewPicFollowupRegulation> data) {
		super(data);
	}

	@Override
	public TmpComplianceReviewPicFollowupRegulation getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpComplianceReviewPicFollowupRegulation> list = (List<TmpComplianceReviewPicFollowupRegulation>) getWrappedData();

		for (TmpComplianceReviewPicFollowupRegulation ejb : list) {
			if (ejb.getSeq() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpComplianceReviewPicFollowupRegulation item) {
		return item.getSeq();
	}

}
