package com.wo.module.advocate.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class AdvocatePartnerTableModel<E> extends ListDataModel<AdvocatePartners>
		implements SelectableDataModel<AdvocatePartners>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public AdvocatePartnerTableModel(List<AdvocatePartners> data) {
		super(data);
	}

	@Override
	public AdvocatePartners getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<AdvocatePartners> list = (List<AdvocatePartners>) getWrappedData();

		for (AdvocatePartners ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(AdvocatePartners item) {
		return item.getSequence();
	}

}
