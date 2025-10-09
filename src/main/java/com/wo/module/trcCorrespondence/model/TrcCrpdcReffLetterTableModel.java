package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcCrpdcReffLetterTableModel<E> extends ListDataModel<TrcCrpdcReffLetter>
		implements SelectableDataModel<TrcCrpdcReffLetter>, Serializable {

	private static final long serialVersionUID = -7093677392468016019L;

	public TrcCrpdcReffLetterTableModel(List<TrcCrpdcReffLetter> data) {
		super(data);
	}

	@SuppressWarnings("deprecation")
	@Override
	public TrcCrpdcReffLetter getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcCrpdcReffLetter> list = (List<TrcCrpdcReffLetter>) getWrappedData();

		for (TrcCrpdcReffLetter ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcCrpdcReffLetter item) {
		return item.getSequence();
	}

}
