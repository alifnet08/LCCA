package com.wo.module.trcFineApproval.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcFinePicComplianceTableModel<E> extends ListDataModel<TrcFinePicCompliance>
		implements SelectableDataModel<TrcFinePicCompliance>, Serializable {

	private static final long serialVersionUID = -7712357224027187632L;

	public TrcFinePicComplianceTableModel(List<TrcFinePicCompliance> data) {
		super(data);
	}

	@Override
	public TrcFinePicCompliance getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcFinePicCompliance> list = (List<TrcFinePicCompliance>) getWrappedData();

		for (TrcFinePicCompliance ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcFinePicCompliance item) {
		return item.getSequence();
	}

}
