package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcCorrespondenceSupportingUnitTableModel<E> extends ListDataModel<TrcCorrespondenceSupportingUnit>
		implements SelectableDataModel<TrcCorrespondenceSupportingUnit>, Serializable {

	private static final long serialVersionUID = -238176157579631293L;

	public TrcCorrespondenceSupportingUnitTableModel(List<TrcCorrespondenceSupportingUnit> data) {
		super(data);
	}

	@Override
	public TrcCorrespondenceSupportingUnit getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcCorrespondenceSupportingUnit> list = (List<TrcCorrespondenceSupportingUnit>) getWrappedData();

		for (TrcCorrespondenceSupportingUnit ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcCorrespondenceSupportingUnit item) {
		return item.getSequence();
	}

}
