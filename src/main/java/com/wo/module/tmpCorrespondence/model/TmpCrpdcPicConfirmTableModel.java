package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TmpCrpdcPicConfirmTableModel<E> extends ListDataModel<TmpCrpdcPicConfirm>
		implements SelectableDataModel<TmpCrpdcPicConfirm>, Serializable {

	private static final long serialVersionUID = 7099809144744093769L;

	public TmpCrpdcPicConfirmTableModel(List<TmpCrpdcPicConfirm> data) {
		super(data);
	}

	@SuppressWarnings("deprecation")
	@Override
	public TmpCrpdcPicConfirm getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TmpCrpdcPicConfirm> list = (List<TmpCrpdcPicConfirm>) getWrappedData();

		for (TmpCrpdcPicConfirm ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TmpCrpdcPicConfirm item) {
		return item.getSequence();
	}

}
