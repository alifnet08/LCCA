package com.wo.module.calendar.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;

public class Calendar extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long calendarId;
	private String calendarEvent;
	private Date startDate;
	private Date endDate;
	private String description;
	private Integer allDayFlag;
	
	public Long getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}
	public String getCalendarEvent() {
		return calendarEvent;
	}
	public void setCalendarEvent(String calendarEvent) {
		this.calendarEvent = calendarEvent;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAllDayFlag() {
		return allDayFlag;
	}
	public void setAllDayFlag(Integer allDayFlag) {
		this.allDayFlag = allDayFlag;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
