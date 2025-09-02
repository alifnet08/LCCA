package com.wo.module.trcFineApproval.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcFinePicFollowupTableModel<E> extends ListDataModel<TrcFinePicFollowup>
		implements SelectableDataModel<TrcFinePicFollowup>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public TrcFinePicFollowupTableModel(List<TrcFinePicFollowup> data) {
		super(data);
	}

	@Override
	public TrcFinePicFollowup getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcFinePicFollowup> list = (List<TrcFinePicFollowup>) getWrappedData();

		for (TrcFinePicFollowup ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcFinePicFollowup item) {
		return item.getSequence();
	}

}
