package com.wo.module.trcRmd.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcRmdCorrespondenceTableModel<E> extends ListDataModel<TrcRmdCorrespondence> implements SelectableDataModel<TrcRmdCorrespondence>{

	public TrcRmdCorrespondenceTableModel(List<TrcRmdCorrespondence> data) {
		super(data);
	}
	
	@Override
	public Object getRowKey(TrcRmdCorrespondence obj) {
		return obj.getSequence();
	}

	@Override
	public TrcRmdCorrespondence getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<TrcRmdCorrespondence> list = (List<TrcRmdCorrespondence>) getWrappedData();
		
		for (TrcRmdCorrespondence trcRmdCorrespondence : list) {
			if(trcRmdCorrespondence.getSequence() == new Integer(rowKey).intValue()) {
				return trcRmdCorrespondence;
			}
		}
		
		return null;
	}
}