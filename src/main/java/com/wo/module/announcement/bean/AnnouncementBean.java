package com.wo.module.announcement.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.announcement.constant.AnnouncementConstant;
import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcement.service.AnnouncementService;
import com.wo.module.announcement.vo.AnnouncementVo;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;

public class AnnouncementBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -2682998887615854558L;
	private static final Logger logger = Logger.getLogger(AnnouncementBean.class);
	private static final String NAVIGATE_EDIT = AnnouncementConstant.NAVIGATE_ANNOUNCEMENT_EDIT;
	
	private AnnouncementService announcementService;
	
	private String searchAnnouncementTitle;
	private Date searchPeriodStart;
	private Date searchPeriodEnd;
	
	private int paging;
	
	private DBLazyDataModel<AnnouncementVo> announcementTableModel;
	
	private FacesUtil facesUtil;
	
	private SimpleDateFormat sdfDateSearch = new SimpleDateFormat("yyyy-MM-dd");
	
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
		paging = Constants.DEFAULT_PAGING_NUMBER;
		announcementTableModel = new DBLazyDataModel<AnnouncementVo>(announcementService, paging);
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent event) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchAnnouncementTitle != null && !searchAnnouncementTitle.equals("") && !searchAnnouncementTitle.isEmpty()) {
			searchCriteria.add(new  DefaultSearchObject(AnnouncementConstant.SEARCH_BY_ANNOUNCEMENT_TITLE, searchAnnouncementTitle));
		}
		if (searchPeriodStart != null) {
			searchCriteria.add(new  DefaultSearchObject(AnnouncementConstant.SEARCH_BY_PERIOD_START, sdfDateSearch.format(searchPeriodStart)));
		}
		if (searchPeriodEnd != null) {
			searchCriteria.add(new  DefaultSearchObject(AnnouncementConstant.SEARCH_BY_PERIOD_END, sdfDateSearch.format(searchPeriodEnd)));
		}
		
		announcementTableModel.setSearchCriteria(searchCriteria);
	}
	
	public void reset(ActionEvent event) {
		searchAnnouncementTitle = "";
		searchPeriodStart = null;
		searchPeriodEnd = null;
		
		search(event);
	}
	
	public void delete(Long announcementId) {
		try {
			Announcement entity = announcementService.findById(announcementId);
			entity.setEnabledFlag(Constants.CONSTANT_NO);
			entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
			entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
			
			announcementService.delete(entity);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public AnnouncementService getAnnouncementService() {
		return announcementService;
	}

	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}

	public String getSearchAnnouncementTitle() {
		return searchAnnouncementTitle;
	}

	public void setSearchAnnouncementTitle(String searchAnnouncementTitle) {
		this.searchAnnouncementTitle = searchAnnouncementTitle;
	}

	public Date getSearchPeriodStart() {
		return searchPeriodStart;
	}

	public void setSearchPeriodStart(Date searchPeriodStart) {
		this.searchPeriodStart = searchPeriodStart;
	}

	public Date getSearchPeriodEnd() {
		return searchPeriodEnd;
	}

	public void setSearchPeriodEnd(Date searchPeriodEnd) {
		this.searchPeriodEnd = searchPeriodEnd;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public DBLazyDataModel<AnnouncementVo> getAnnouncementTableModel() {
		return announcementTableModel;
	}

	public void setAnnouncementTableModel(DBLazyDataModel<AnnouncementVo> announcementTableModel) {
		this.announcementTableModel = announcementTableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public SimpleDateFormat getSdfDateSearch() {
		return sdfDateSearch;
	}

	public void setSdfDateSearch(SimpleDateFormat sdfDateSearch) {
		this.sdfDateSearch = sdfDateSearch;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}
	
}
