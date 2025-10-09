package com.wo.module.tmpFine.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpFinePicFollowupTableModel<E> extends ListDataModel<TmpFinePicFollowup>
		implements SelectableDataModel<TmpFinePicFollowup>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public TmpFinePicFollowupTableModel(List<TmpFinePicFollowup> data) {
		super(data);
	}

	@Override
	public TmpFinePicFollowup getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpFinePicFollowup> list = (List<TmpFinePicFollowup>) getWrappedData();

		for (TmpFinePicFollowup ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpFinePicFollowup item) {
		return item.getSequence();
	}

}
