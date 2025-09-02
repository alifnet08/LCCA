package com.wo.module.trcAudit.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcAuditPICComplianceTableModel<E> extends ListDataModel<TrcAuditPicCompliance>
		implements SelectableDataModel<TrcAuditPicCompliance> {

	public TrcAuditPICComplianceTableModel(List<TrcAuditPicCompliance> data) {
		super(data);
	}

	@Override
	public TrcAuditPicCompliance getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcAuditPicCompliance> list = (List<TrcAuditPicCompliance>) getWrappedData();

		for (TrcAuditPicCompliance ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(TrcAuditPicCompliance item) {
		return item.getSequence();
	}

}
