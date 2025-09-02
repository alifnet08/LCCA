package com.wo.module.advocate.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class AdvocateInfoTableModel<E> extends ListDataModel<AdvocateInfo>
		implements SelectableDataModel<AdvocateInfo>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public AdvocateInfoTableModel(List<AdvocateInfo> data) {
		super(data);
	}

	@Override
	public AdvocateInfo getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<AdvocateInfo> list = (List<AdvocateInfo>) getWrappedData();

		for (AdvocateInfo ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(AdvocateInfo item) {
		return item.getSequence();
	}

}
