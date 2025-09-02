package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationDetailTableModel<E> extends ListDataModel<LitigationDetail>
		implements SelectableDataModel<LitigationDetail>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public LitigationDetailTableModel(List<LitigationDetail> data) {
		super(data);
	}

	@Override
	public LitigationDetail getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<LitigationDetail> list = (List<LitigationDetail>) getWrappedData();

		for (LitigationDetail ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(LitigationDetail item) {
		return item.getSequence();
	}

}
