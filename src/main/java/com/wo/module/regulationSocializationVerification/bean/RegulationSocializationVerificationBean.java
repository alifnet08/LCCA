package com.wo.module.regulationSocializationVerification.bean;

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
import com.wo.module.regulationSocializationVerification.constant.RegulationSocializationVerificationConstants;
import com.wo.module.regulationSocializationVerification.service.RegulationSocializationVerificationService;
import com.wo.module.regulationSocializationVerification.vo.RegulationSocializationVerificationSearchVO;
import com.wo.module.user.model.User;

public class RegulationSocializationVerificationBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegulationSocializationVerificationBean.class);	
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private DBLazyDataModel<RegulationSocializationVerificationSearchVO> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateEdit = RegulationSocializationVerificationConstants.NAVIGATE_EDIT;
	
	private RegulationSocializationVerificationService regulationSocializationVerificationService;
	
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
//    	User user = facesUtil.getUserLogin();
    	super.init();
    	paging = Constants.DEFAULT_PAGING_NUMBER;
    	tableModel =  new DBLazyDataModel<RegulationSocializationVerificationSearchVO>(regulationSocializationVerificationService, paging);
    	tableModel.setSearchCriteria(
                Arrays.asList(
                		new DefaultSearchObject(RegulationSocializationVerificationConstants.WHERE_USER_ID,"")//user.getUserId())
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
                		new DefaultSearchObject(RegulationSocializationVerificationConstants.WHERE_USER_ID,user.getUserId())
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
		RegulationSocializationVerificationBean.logger = logger;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}



	public RegulationSocializationVerificationService getRegulationSocializationVerificationService() {
		return regulationSocializationVerificationService;
	}

	public void setRegulationSocializationVerificationService(RegulationSocializationVerificationService regulationSocializationVerificationService) {
		this.regulationSocializationVerificationService = regulationSocializationVerificationService;
	}

	public DBLazyDataModel<RegulationSocializationVerificationSearchVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<RegulationSocializationVerificationSearchVO> tableModel) {
		this.tableModel = tableModel;
	}
	
	
	
}