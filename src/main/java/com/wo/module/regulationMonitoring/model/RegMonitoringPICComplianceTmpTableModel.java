package com.wo.module.regulationMonitoring.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class RegMonitoringPICComplianceTmpTableModel<E> extends ListDataModel<RegMonitoringPICComplianceTmp>
		implements SelectableDataModel<RegMonitoringPICComplianceTmp> {

	public RegMonitoringPICComplianceTmpTableModel(List<RegMonitoringPICComplianceTmp> data) {
		super(data);
	}

	@Override
	public RegMonitoringPICComplianceTmp getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<RegMonitoringPICComplianceTmp> list = (List<RegMonitoringPICComplianceTmp>) getWrappedData();

		for (RegMonitoringPICComplianceTmp ejb : list) {
			if (ejb.getSequence() == new Integer(rowKey).intValue()) {
				return ejb;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(RegMonitoringPICComplianceTmp item) {
		return item.getSequence();
	}

}