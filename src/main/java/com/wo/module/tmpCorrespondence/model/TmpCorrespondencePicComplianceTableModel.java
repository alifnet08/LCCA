package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpCorrespondencePicComplianceTableModel<E> extends ListDataModel<TmpCorrespondencePicCompliance>
		implements SelectableDataModel<TmpCorrespondencePicCompliance>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public TmpCorrespondencePicComplianceTableModel(List<TmpCorrespondencePicCompliance> data) {
		super(data);
	}

	@Override
	public TmpCorrespondencePicCompliance getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpCorrespondencePicCompliance> list = (List<TmpCorrespondencePicCompliance>) getWrappedData();

		for (TmpCorrespondencePicCompliance ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpCorrespondencePicCompliance item) {
		return item.getSequence();
	}

}
