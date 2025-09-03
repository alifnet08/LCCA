package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcCrpdcPicConfirmTableModel<E> extends ListDataModel<TrcCrpdcPicConfirm>
		implements SelectableDataModel<TrcCrpdcPicConfirm>, Serializable {

	private static final long serialVersionUID = 1596194448439589280L;

	public TrcCrpdcPicConfirmTableModel(List<TrcCrpdcPicConfirm> data) {
		super(data);
	}

	@SuppressWarnings("deprecation")
	@Override
	public TrcCrpdcPicConfirm getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcCrpdcPicConfirm> list = (List<TrcCrpdcPicConfirm>) getWrappedData();

		for (TrcCrpdcPicConfirm ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcCrpdcPicConfirm item) {
		return item.getSequence();
	}

}
