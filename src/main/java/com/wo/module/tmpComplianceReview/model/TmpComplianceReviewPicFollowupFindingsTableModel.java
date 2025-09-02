package com.wo.module.tmpComplianceReview.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpComplianceReviewPicFollowupFindingsTableModel<E>
		extends ListDataModel<TmpComplianceReviewPicFollowupFindings>
		implements SelectableDataModel<TmpComplianceReviewPicFollowupFindings> {

	public TmpComplianceReviewPicFollowupFindingsTableModel(List<TmpComplianceReviewPicFollowupFindings> data) {
		super(data);
	}

	@Override
	public TmpComplianceReviewPicFollowupFindings getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpComplianceReviewPicFollowupFindings> list = (List<TmpComplianceReviewPicFollowupFindings>) getWrappedData();

		for (TmpComplianceReviewPicFollowupFindings ejb : list) {
			if (ejb.getSeq() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpComplianceReviewPicFollowupFindings item) {
		return item.getSeq();
	}

}
