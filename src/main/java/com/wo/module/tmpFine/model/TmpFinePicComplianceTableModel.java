package com.wo.module.tmpFine.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpFinePicComplianceTableModel<E> extends ListDataModel<TmpFinePicCompliance>
		implements SelectableDataModel<TmpFinePicCompliance>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public TmpFinePicComplianceTableModel(List<TmpFinePicCompliance> data) {
		super(data);
	}

	@Override
	public TmpFinePicCompliance getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpFinePicCompliance> list = (List<TmpFinePicCompliance>) getWrappedData();

		for (TmpFinePicCompliance ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpFinePicCompliance item) {
		return item.getSequence();
	}

}
