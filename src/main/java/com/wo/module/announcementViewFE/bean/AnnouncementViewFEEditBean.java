package com.wo.module.announcementViewFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcementViewFE.constant.AnnouncementViewFEConstant;
import com.wo.module.announcementViewFE.service.AnnouncementViewFEService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.user.service.UserService;

public class AnnouncementViewFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -3935710146080570352L;
	private static final Logger logger = Logger.getLogger(AnnouncementViewFEEditBean.class);
	private static final String NAVIGATE_BACK = AnnouncementViewFEConstant.NAVIGATE_ANNOUNCEMENT;
	
	private AnnouncementViewFEService announcementViewFEService;
	private UserService userService;
	
	private Announcement announcement;
	
	private String editId;
	private String periode;
	
	private FacesUtil facesUtil;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
	private Integer testFirst;
	
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
		if(facesUtil.retrieveRequestParam("first") !=null) {
			testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first"));
		}else {
			testFirst = 1;
		}
		facesUtil.setSessionAttribute("FIRST_ANNOUNC_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
	}

	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("editId");
		
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		
		if (StringUtils.isBlank(editId)) {
			// do nothing
		} else {
			handleView(editId);
		}
	}
	
	private void handleView(String viewId) {
		
		Long viewIdLong = Long.parseLong(viewId);
		
		announcement = announcementViewFEService.findById(viewIdLong);
		if (announcement.getPeriodStart() != null) {
			periode = sdf.format(announcement.getPeriodStart());
			if (announcement.getPeriodEnd() != null) {
				periode = sdf.format(announcement.getPeriodStart()).concat(" - ").concat(sdf.format(announcement.getPeriodEnd()));	
			}
		} else {
			if (announcement.getPeriodEnd() != null) {
				periode = sdf.format(announcement.getPeriodEnd());
			}
		}
	}
	
	public void cancel() {
		try {
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/announcementViewFE/announcementViewFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AnnouncementViewFEService getAnnouncementViewFEService() {
		return announcementViewFEService;
	}

	public void setAnnouncementViewFEService(AnnouncementViewFEService announcementViewFEService) {
		this.announcementViewFEService = announcementViewFEService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

}
