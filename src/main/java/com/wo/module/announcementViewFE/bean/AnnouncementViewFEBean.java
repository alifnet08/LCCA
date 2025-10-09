package com.wo.module.announcementViewFE.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.wo.module.announcementViewFE.constant.AnnouncementViewFEConstant;
import com.wo.module.announcementViewFE.service.AnnouncementViewFEService;
import com.wo.module.announcementViewFE.vo.AnnouncementViewFEVo;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;

public class AnnouncementViewFEBean extends CommonPagingFEBean<AnnouncementViewFEVo> implements Serializable{

	private static final long serialVersionUID = -2756573697545039055L;
	private static final Logger logger = Logger.getLogger(AnnouncementViewFEBean.class);
	private static final String NAVIGATE_EDIT = AnnouncementViewFEConstant.NAVIGATE_ANNOUNCEMENT_EDIT;
	
	private AnnouncementViewFEService announcementViewFEService;

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
		if (facesUtil.getSessionAttribute("FIRST_ANNOUNC_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_ANNOUNC_FE");
			setInitFirst((Integer) dataInt);
		}
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_ANNOUNC_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_ANNOUNC_FE");
		}
	}
	
	public String toEncrypt(Long announcementId){
		try {
			return Constants.encryptString(announcementId.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<AnnouncementViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return announcementViewFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return announcementViewFEService.searchCountData(getSearchCriteria());
	}

	public AnnouncementViewFEService getAnnouncementViewFEService() {
		return announcementViewFEService;
	}

	public void setAnnouncementViewFEService(AnnouncementViewFEService announcementViewFEService) {
		this.announcementViewFEService = announcementViewFEService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}
	
}
