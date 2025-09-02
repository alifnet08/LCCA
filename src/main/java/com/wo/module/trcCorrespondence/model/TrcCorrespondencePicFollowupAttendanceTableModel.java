package com.wo.module.trcCorrespondence.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TrcCorrespondencePicFollowupAttendanceTableModel<E> extends ListDataModel<TrcCorrespondencePicFollowupAttendance>
	implements SelectableDataModel<TrcCorrespondencePicFollowupAttendance>{
	
	public TrcCorrespondencePicFollowupAttendanceTableModel(List<TrcCorrespondencePicFollowupAttendance> data)
	{  
	    super(data);
	}

	@Override
	public Object getRowKey(TrcCorrespondencePicFollowupAttendance item) {
		return item.getSequence();
	}

	@SuppressWarnings("unchecked")
	public TrcCorrespondencePicFollowupAttendance getRowData(String rowKey) {
		
		List<TrcCorrespondencePicFollowupAttendance> list = (List<TrcCorrespondencePicFollowupAttendance>) getWrappedData();
		
		for (TrcCorrespondencePicFollowupAttendance trcCorrespondencePicFollowupAttendance : list) {
			if(trcCorrespondencePicFollowupAttendance.getSequence() == new Integer(rowKey).intValue()) {
				return trcCorrespondencePicFollowupAttendance;
			}
		}
		
		return null;
	}  
	
}