package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcCorrespondencePicComplianceTableModel<E> extends ListDataModel<TrcCorrespondencePicCompliance>
		implements SelectableDataModel<TrcCorrespondencePicCompliance>, Serializable {

	private static final long serialVersionUID = -7712357224027187632L;

	public TrcCorrespondencePicComplianceTableModel(List<TrcCorrespondencePicCompliance> data) {
		super(data);
	}

	@Override
	public TrcCorrespondencePicCompliance getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcCorrespondencePicCompliance> list = (List<TrcCorrespondencePicCompliance>) getWrappedData();

		for (TrcCorrespondencePicCompliance ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcCorrespondencePicCompliance item) {
		return item.getSequence();
	}

}
