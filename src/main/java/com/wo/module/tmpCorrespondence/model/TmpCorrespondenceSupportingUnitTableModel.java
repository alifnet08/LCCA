package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpCorrespondenceSupportingUnitTableModel<E> extends ListDataModel<TmpCorrespondenceSupportingUnit>
		implements SelectableDataModel<TmpCorrespondenceSupportingUnit>, Serializable {

	private static final long serialVersionUID = 6478248486102251357L;

	public TmpCorrespondenceSupportingUnitTableModel(List<TmpCorrespondenceSupportingUnit> data) {
		super(data);
	}

	@Override
	public TmpCorrespondenceSupportingUnit getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpCorrespondenceSupportingUnit> list = (List<TmpCorrespondenceSupportingUnit>) getWrappedData();

		for (TmpCorrespondenceSupportingUnit ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpCorrespondenceSupportingUnit item) {
		return item.getSequence();
	}

}
