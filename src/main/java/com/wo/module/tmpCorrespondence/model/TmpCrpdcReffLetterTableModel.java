package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpCrpdcReffLetterTableModel<E> extends ListDataModel<TmpCrpdcReffLetter>
		implements SelectableDataModel<TmpCrpdcReffLetter>, Serializable {

	private static final long serialVersionUID = -6562755039319227214L;

	public TmpCrpdcReffLetterTableModel(List<TmpCrpdcReffLetter> data) {
		super(data);
	}

	@SuppressWarnings("deprecation")
	@Override
	public TmpCrpdcReffLetter getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpCrpdcReffLetter> list = (List<TmpCrpdcReffLetter>) getWrappedData();

		for (TmpCrpdcReffLetter ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpCrpdcReffLetter item) {
		return item.getSequence();
	}

}
