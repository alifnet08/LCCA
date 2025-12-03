package com.wo.module.calendar.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.wo.module.calendar.constant.CalendarConstants;
import com.wo.module.calendar.model.Calendar;
import com.wo.module.calendar.service.CalendarService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.holiday.model.Holiday;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.FacesUtil;

public class CalendarBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CalendarBean.class);

	private String searchVal;

	private int paging;

	private CalendarService calendarService;
	
	private HolidayService holidayService;

	private Calendar calendar;

	public FacesUtil facesUtil;
	
	private ScheduleModel eventModel;
	
	private ScheduleEvent event = new DefaultScheduleEvent();

	private String navigateEdit = CalendarConstants.NAVIGATE_EDIT;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@SuppressWarnings("serial")
	@PostConstruct
	public void init() {
		super.init();
		 //eventModel = new DefaultScheduleModel();
		eventModel = new LazyScheduleModel() {
			
			@Override
			public void loadEvents(Date start, Date end) {
				@SuppressWarnings("rawtypes")
				List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
				if (start != null) {
					searchCriteria.add(new DefaultSearchObject(CalendarConstants.SEARCH_BY_START_DATE, sdf.format(start)));
				}
				if (end != null) {
					searchCriteria.add(new DefaultSearchObject(CalendarConstants.SEARCH_BY_END_DATE, sdf.format(end)));
				}
				System.out.println("StartDate=="+sdf.format(start));
				System.out.println("EndDate=="+sdf.format(end));
				List<Calendar> list;
				try {
					list = calendarService.searchData(searchCriteria, 0, 100, null, null);
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				for(int i=0;i<list.size();i++){
					Calendar cal = list.get(i);
					DefaultScheduleEvent eventNew = new DefaultScheduleEvent();
					eventNew.setData(cal);
					eventNew.setTitle(cal.getCalendarEvent());
					eventNew.setStartDate(cal.getStartDate());
					eventNew.setEndDate(cal.getEndDate());
					//eventNew.setStartDate(localTime2PrimeScheduleGMT(sdf2.parse(sdf.format(cal.getStartDate())+" 08:00")));
					//eventNew.setEndDate(localTime2PrimeScheduleGMT(sdf2.parse(sdf.format(cal.getEndDate())+" 17:00")));
					eventNew.setDescription(eventNew.getDescription());
					eventNew.setAllDay(cal.getAllDayFlag().equals(new Integer(1))?true:false);
					addEvent(eventNew);
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}	
		};
		 
	}
	
	@SuppressWarnings("deprecation")
	public Date localTime2PrimeScheduleGMT(Date date)  {

        java.util.Calendar evjavacal = java.util.Calendar.getInstance();
        evjavacal.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        evjavacal.set(java.util.Calendar.YEAR, date.getYear());
        evjavacal.set(java.util.Calendar.MONTH, date.getMonth());
        evjavacal.set(java.util.Calendar.DAY_OF_MONTH, date.getDay());
        evjavacal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        evjavacal.set(java.util.Calendar.MINUTE, 0);
        evjavacal.set(java.util.Calendar.SECOND, 0);
    	return evjavacal.getTime();
    }
	
	public void addEvent(ActionEvent actionEvent) {
		try {
		System.out.println("calendar Event=="+event.getTitle());
		System.out.println("start Date=="+event.getStartDate());
		System.out.println("end Date=="+event.getEndDate());
		System.out.println("flag=="+event.isAllDay());
		
		if(event.getId() == null){
			eventModel.addEvent(event);
			calendar = new Calendar();
			calendar.setCalendarEvent(event.getTitle());
			calendar.setStartDate(event.getStartDate());
			calendar.setEndDate(event.getEndDate());
			calendar.setAllDayFlag(event.isAllDay()?new Integer(1):new Integer(0));
			calendar.setCreatedBy(facesUtil.retrieveUserLogin());
			calendar.setCreationDate(new Timestamp(new Date().getTime()));
			calendar.setDelId(new Long(0));
			calendar.setEnabledFlag(Constants.CONSTANT_YES);
			if(calendar.getAllDayFlag()!= null && calendar.getAllDayFlag().equals(1)){
				Holiday holiday = holidayService.getHolidayDataByName(calendar.getCalendarEvent());
				if(holiday!=null && holiday.getHolidayId()!=null){
					holiday.setHolidayDateFrom(calendar.getStartDate());
					holiday.setHolidayDateTo(calendar.getEndDate());
					holiday.setEnabledFlag(Constants.CONSTANT_YES);
					holiday.setLastUpdateBy(facesUtil.retrieveUserLogin());
					holiday.setLastUpdateDate(new Timestamp(new Date().getTime()));
					holidayService.update(holiday);
				}else{
					holiday = new Holiday();
					holiday.setHolidayName(calendar.getCalendarEvent());
					holiday.setHolidayDateFrom(calendar.getStartDate());
					holiday.setHolidayDateTo(calendar.getEndDate());
					holiday.setEnabledFlag(Constants.CONSTANT_YES);
					holiday.setCreatedBy(facesUtil.retrieveUserLogin());
					holiday.setCreationDate(new Timestamp(new Date().getTime()));
					holiday.setDelId(new Long(0));
					holidayService.save(holiday);
				}
			}
			calendarService.save(calendar);
		}else{
			eventModel.updateEvent(event);
			Calendar calendarNew = calendarService.findById(((Calendar)event.getData()).getCalendarId());
			calendarNew.setCalendarEvent(event.getTitle());
			calendarNew.setStartDate(event.getStartDate());
			calendarNew.setEndDate(event.getEndDate());
			calendarNew.setAllDayFlag(event.isAllDay()?new Integer(1):new Integer(0));
			calendarNew.setLastUpdateBy(facesUtil.retrieveUserLogin());
			calendarNew.setLastUpdateDate(new Timestamp(new Date().getTime()));
			calendarNew.setDelId(new Long(0));
			calendarNew.setEnabledFlag(Constants.CONSTANT_YES);
			if(calendarNew.getAllDayFlag()!= null && calendarNew.getAllDayFlag().equals(1)){
				Holiday holiday = holidayService.getHolidayDataByName(calendarNew.getCalendarEvent());
				if(holiday!=null && holiday.getHolidayId()!=null){
					holiday.setHolidayDateFrom(calendarNew.getStartDate());
					holiday.setHolidayDateTo(calendarNew.getEndDate());
					holiday.setEnabledFlag(Constants.CONSTANT_YES);
					holiday.setLastUpdateBy(facesUtil.retrieveUserLogin());
					holiday.setLastUpdateDate(new Timestamp(new Date().getTime()));
					holidayService.update(holiday);
				}else{
					holiday = new Holiday();
					holiday.setHolidayName(calendarNew.getCalendarEvent());
					holiday.setHolidayDateFrom(calendarNew.getStartDate());
					holiday.setHolidayDateTo(calendarNew.getEndDate());
					holiday.setEnabledFlag(Constants.CONSTANT_YES);
					holiday.setCreatedBy(facesUtil.retrieveUserLogin());
					holiday.setCreationDate(new Timestamp(new Date().getTime()));
					holiday.setDelId(new Long(0));
					holidayService.save(holiday);
				}
			}
			calendarService.update(calendarNew);
		}
		
		event = new DefaultScheduleEvent();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}
	}
	
	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}
	
	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}
	
	public void onEventMove(ScheduleEntryMoveEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
		
		addMessage(message);
	}
	
	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
		
		addMessage(message);
	}
	
	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CalendarBean.logger = logger;
	}

	public CalendarService getCalendarService() {
		return calendarService;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	

}