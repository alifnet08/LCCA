package com.wo.module.holiday.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class Holiday extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long holidayId;
	private String holidayName;
	private Date holidayDateFrom;
	private Date holidayDateTo;
	
	private Long delId;

	public Long getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public Date getHolidayDateFrom() {
		return holidayDateFrom;
	}

	public void setHolidayDateFrom(Date holidayDateFrom) {
		this.holidayDateFrom = holidayDateFrom;
	}

	public Date getHolidayDateTo() {
		return holidayDateTo;
	}

	public void setHolidayDateTo(Date holidayDateTo) {
		this.holidayDateTo = holidayDateTo;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}
	

}
