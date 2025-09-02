package com.wo.module.picFollowupConfirmation.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.picFollowupConfirmation.constant.PICFollowupConfirmationConstants;
import com.wo.module.picFollowupConfirmation.service.PICFollowupConfirmationService;
import com.wo.module.picFollowupConfirmation.vo.PICFollowupConfirmationVO;
import com.wo.module.user.model.User;

public class PICFollowupConfirmationBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(PICFollowupConfirmationBean.class);
	
	private String picFollowupConfirmationSearch;
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private DBLazyDataModel<PICFollowupConfirmationVO> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateEdit = PICFollowupConfirmationConstants.NAVIGATE_EDIT;
	
	private PICFollowupConfirmationService picFollowupConfirmationService;
	
	private String localLanguange;
    
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void addErrMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    @SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
    	User user = facesUtil.getUserLogin();
    	paging = Constants.DEFAULT_PAGING_NUMBER;
    	tableModel =  new DBLazyDataModel<PICFollowupConfirmationVO>(picFollowupConfirmationService, paging);
    	tableModel.setSearchCriteria(
                Arrays.asList(
                		new DefaultSearchObject(PICFollowupConfirmationConstants.WHERE_USER_ID,user.getUserId())
                ));
    	
    	Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    	localLanguange ="IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		} 
	}
	
    public void search(ActionEvent actionEvent) {  
    	User user = facesUtil.getUserLogin();
    	tableModel.setSearchCriteria(
    			Arrays.asList(
                		new DefaultSearchObject(PICFollowupConfirmationConstants.WHERE_USER_ID,user.getUserId())
                ));
	}
    
    public void reset(ActionEvent actionEvent) {
    	searchVal = "";
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    
                		new DefaultSearchObject(SearchObject.ALL_COLUMNS,searchVal)
                ));
	}

    
    
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}


	public String getPICFollowupConfirmationSearch() {
		return picFollowupConfirmationSearch;
	}

	public void setPICFollowupConfirmationSearch(String picFollowupConfirmationSearch) {
		this.picFollowupConfirmationSearch = picFollowupConfirmationSearch;
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

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		PICFollowupConfirmationBean.logger = logger;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public String getPicFollowupConfirmationSearch() {
		return picFollowupConfirmationSearch;
	}

	public void setPicFollowupConfirmationSearch(String picFollowupConfirmationSearch) {
		this.picFollowupConfirmationSearch = picFollowupConfirmationSearch;
	}

	public PICFollowupConfirmationService getPicFollowupConfirmationService() {
		return picFollowupConfirmationService;
	}

	public void setPicFollowupConfirmationService(PICFollowupConfirmationService picFollowupConfirmationService) {
		this.picFollowupConfirmationService = picFollowupConfirmationService;
	}

	public DBLazyDataModel<PICFollowupConfirmationVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<PICFollowupConfirmationVO> tableModel) {
		this.tableModel = tableModel;
	}
	
	
	
}