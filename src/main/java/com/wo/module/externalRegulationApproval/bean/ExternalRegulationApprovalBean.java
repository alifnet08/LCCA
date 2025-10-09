package com.wo.module.externalRegulationApproval.bean;

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
import com.wo.module.externalRegulationApproval.constant.ExternalRegulationApprovalConstants;
import com.wo.module.externalRegulationApproval.model.ExternalRegulationApproval;
import com.wo.module.externalRegulationApproval.service.ExternalRegulationApprovalService;
import com.wo.module.lov.bean.FacesUtil;

public class ExternalRegulationApprovalBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ExternalRegulationApprovalBean.class);
	
	private String externalRegulationApprovalSearch;
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private DBLazyDataModel<ExternalRegulationApproval> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateEdit = ExternalRegulationApprovalConstants.NAVIGATE_EDIT;
	
	private ExternalRegulationApprovalService externalRegulationApprovalService;
	
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
    	super.init();
    	paging = Constants.DEFAULT_PAGING_NUMBER;
    	tableModel =  new DBLazyDataModel<ExternalRegulationApproval>(externalRegulationApprovalService, paging);
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    new DefaultSearchObject(ExternalRegulationApprovalConstants.WHERE_JENIS_KETENTUAN, 
                    		ExternalRegulationApprovalConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)
                ));
    	
    	Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    	localLanguange ="IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		} 
	}
	
    public void search(ActionEvent actionEvent) {    	
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    //new DefaultSearchObject(SearchObject.ALL_COLUMNS,searchVal),
                    new DefaultSearchObject(ExternalRegulationApprovalConstants.WHERE_JENIS_KETENTUAN, 
                    		ExternalRegulationApprovalConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)
                ));
	}
    
    public void reset(ActionEvent actionEvent) {
    	searchVal = "";
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    //new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal),
                    new DefaultSearchObject(ExternalRegulationApprovalConstants.WHERE_JENIS_KETENTUAN, 
                    		ExternalRegulationApprovalConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)
                ));
	}

    
    
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}


	public String getExternalRegulationApprovalSearch() {
		return externalRegulationApprovalSearch;
	}

	public void setExternalRegulationApprovalSearch(String externalRegulationApprovalSearch) {
		this.externalRegulationApprovalSearch = externalRegulationApprovalSearch;
	}

	public ExternalRegulationApprovalService getExternalRegulationApprovalService() {
		return externalRegulationApprovalService;
	}

	public void setExternalRegulationApprovalService(ExternalRegulationApprovalService externalRegulationApprovalService) {
		this.externalRegulationApprovalService = externalRegulationApprovalService;
	}

	
	public DBLazyDataModel<ExternalRegulationApproval> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ExternalRegulationApproval> tableModel) {
		this.tableModel = tableModel;
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
		ExternalRegulationApprovalBean.logger = logger;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}
	
}