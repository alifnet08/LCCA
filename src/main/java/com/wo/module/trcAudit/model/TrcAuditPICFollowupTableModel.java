package com.wo.module.trcAudit.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcAuditPICFollowupTableModel<E> extends ListDataModel<TrcAuditPicFollowup>
		implements SelectableDataModel<TrcAuditPicFollowup> {

	public TrcAuditPICFollowupTableModel(List<TrcAuditPicFollowup> data) {
		super(data);
	}

	@Override
	public TrcAuditPicFollowup getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcAuditPicFollowup> list = (List<TrcAuditPicFollowup>) getWrappedData();

		for (TrcAuditPicFollowup ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcAuditPicFollowup item) {
		return item.getSequence();
	}

}
