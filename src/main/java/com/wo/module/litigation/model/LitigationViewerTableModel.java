package com.wo.module.litigation.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LitigationViewerTableModel<E> extends ListDataModel<LitigationViewer>
		implements SelectableDataModel<LitigationViewer>, Serializable {

	private static final long serialVersionUID = -7509731352426979502L;

	public LitigationViewerTableModel(List<LitigationViewer> data) {
		super(data);
	}

	@Override
	public LitigationViewer getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<LitigationViewer> list = (List<LitigationViewer>) getWrappedData();

		for (LitigationViewer ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(LitigationViewer item) {
		return item.getSequence();
	}

}
