package com.wo.module.announcement.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.announcement.constant.AnnouncementConstant;
import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcement.service.AnnouncementService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;

public class AnnouncementEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 6427666794990736282L;
	private static final Logger logger = Logger.getLogger(AnnouncementEditBean.class);
	
	private AnnouncementService announcementService;
	
	private Announcement announcement;
	
	private String editId;
	private String actionMode;
	
	private FacesUtil facesUtil;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@PostConstruct
	public void init() {
		super.init();
		
		checkNewOrEdit();
	}

	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token");
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			handleNew();
		} else {
			handleEdit(editId);
		}
	}
	
	private void handleNew() {
		actionMode = Constants.ACTION_ADD;
		facesUtil.setSessionAttribute("token", null);
		announcement = new Announcement();
	}
	
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotBlank(token)) {
			editId = Constants.decryptString(token);
		}
		actionMode = Constants.ACTION_EDIT;
		Long editIdLong = Long.parseLong(editId);
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		
		announcement = announcementService.findById(editIdLong);	
	}
	
	public boolean isValidate() {
		boolean flag = true;
		
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(announcement.getPeriodStart());
		endCalendar.setTime(announcement.getPeriodEnd());
		
		int startYear = startCalendar.get(Calendar.YEAR);
		int endYear = endCalendar.get(Calendar.YEAR);
		int startMonth = startCalendar.get(Calendar.MONTH);
		int endMonth = endCalendar.get(Calendar.MONTH);
		int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
		int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
		
		if (endYear < startYear) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formAnnouncementPeriodStart") + " "
					+ facesUtil.retrieveMessage("validateDateByYearMustBigger") + " "
					+ facesUtil.retrieveMessage("formAnnouncementPeriodEnd"));
			flag = false;
		} else {
			if (endMonth < startMonth) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formAnnouncementPeriodStart") + " "
						+ facesUtil.retrieveMessage("validateDateByMonthMustBigger") + " "
						+ facesUtil.retrieveMessage("formAnnouncementPeriodEnd"));
				flag = false;
			} else {
				if (endYear == startYear) {
					if (endMonth == startMonth) {
						if (endDay < startDay) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formAnnouncementPeriodStart") + " "
									+ facesUtil.retrieveMessage("validateDateByDaysMustBigger") + " "
									+ facesUtil.retrieveMessage("formAnnouncementPeriodEnd"));
							flag = false;
						}
					}
				}
			}
		}
		
		return flag;
	}

	public void save() {
		try {
			if (isValidate()) {
				if (announcement.getAnnouncementId() != null) {
					announcement.setLastUpdateBy(facesUtil.retrieveUserLogin());
					announcement.setLastUpdateDate(new Timestamp(new Date().getTime()));
					announcement.setDelId(new Long(0));
					announcement.setEnabledFlag(Constants.CONSTANT_YES);
					announcementService.update(announcement);
				} else {
					announcement.setCreatedBy(facesUtil.retrieveUserLogin());
					announcement.setCreationDate(new Timestamp(new Date().getTime()));
					announcement.setDelId(new Long(0));
					announcement.setEnabledFlag(Constants.CONSTANT_YES);
					announcementService.save(announcement);
				}
				
				facesUtil.redirect("/pages/announcement/"+AnnouncementConstant.NAVIGATE_ANNOUNCEMENT);
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/announcement/"+AnnouncementConstant.NAVIGATE_ANNOUNCEMENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AnnouncementService getAnnouncementService() {
		return announcementService;
	}

	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}
	
}
