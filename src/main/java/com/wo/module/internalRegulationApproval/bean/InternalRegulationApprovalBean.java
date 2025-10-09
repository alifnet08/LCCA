package com.wo.module.internalRegulationApproval.bean;

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
import com.wo.module.internalRegulationApproval.constant.InternalRegulationApprovalConstants;
import com.wo.module.internalRegulationApproval.model.InternalRegulationApproval;
import com.wo.module.internalRegulationApproval.service.InternalRegulationApprovalService;
import com.wo.module.lov.bean.FacesUtil;

public class InternalRegulationApprovalBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 4959995539644585289L;

	static Logger logger = Logger.getLogger(InternalRegulationApprovalBean.class);
	
	private String internalRegulationApprovalSearch;
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private DBLazyDataModel<InternalRegulationApproval> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateEdit = InternalRegulationApprovalConstants.NAVIGATE_EDIT;
	
	private InternalRegulationApprovalService internalRegulationApprovalService;
	
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
    	tableModel =  new DBLazyDataModel<InternalRegulationApproval>(internalRegulationApprovalService, paging);
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    new DefaultSearchObject(InternalRegulationApprovalConstants.WHERE_JENIS_KETENTUAN, 
                    		InternalRegulationApprovalConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)
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
                  //  new DefaultSearchObject(SearchObject.ALL_COLUMNS,searchVal),
                    new DefaultSearchObject(InternalRegulationApprovalConstants.WHERE_JENIS_KETENTUAN, 
                    		InternalRegulationApprovalConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)
                ));
	}
    
    public void reset(ActionEvent actionEvent) {
    	searchVal = "";
    	tableModel.setSearchCriteria(
                Arrays.asList(
                   // new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal),
                    new DefaultSearchObject(InternalRegulationApprovalConstants.WHERE_JENIS_KETENTUAN, 
                    		InternalRegulationApprovalConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)
                ));
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

	public static void setLogger(Logger logger) {
		InternalRegulationApprovalBean.logger = logger;
	}

	public String getInternalRegulationApprovalSearch() {
		return internalRegulationApprovalSearch;
	}

	public void setInternalRegulationApprovalSearch(String internalRegulationApprovalSearch) {
		this.internalRegulationApprovalSearch = internalRegulationApprovalSearch;
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

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}

	public DBLazyDataModel<InternalRegulationApproval> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<InternalRegulationApproval> tableModel) {
		this.tableModel = tableModel;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public InternalRegulationApprovalService getInternalRegulationApprovalService() {
		return internalRegulationApprovalService;
	}

	public void setInternalRegulationApprovalService(InternalRegulationApprovalService internalRegulationApprovalService) {
		this.internalRegulationApprovalService = internalRegulationApprovalService;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}
	
	
   
}