package com.wo.module.tmpAuditApproval.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.tmpAudit.constant.AuditConstant;
import com.wo.module.tmpAuditApproval.service.TmpAuditApprovalService;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;

public class TmpAuditApprovalBean extends CommonBean implements Serializable, AuditConstant {
 
	private static final long serialVersionUID = -5072000851348937316L;

	static Logger logger = Logger.getLogger(TmpAuditApprovalBean.class);
	
	private String regulationSocializationApprovalSearch;
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private DBLazyDataModel<TmpAuditApprovalVO> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateEdit = TMP_AUDIT_APPROVAL_EDIT;
	
	private TmpAuditApprovalService TmpAuditApprovalService;
	
	private String localLanguange;
    
    @SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
    	super.init();
    	paging = Constants.DEFAULT_PAGING_NUMBER;
    	tableModel =  new DBLazyDataModel<TmpAuditApprovalVO>(TmpAuditApprovalService, paging);
    	tableModel.setSearchCriteria(
                Arrays.asList(
                		new DefaultSearchObject(SearchObject.ALL_COLUMNS,searchVal)
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
                    
                		new DefaultSearchObject(SearchObject.ALL_COLUMNS,searchVal)
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


	public String getRegulationSocializationApprovalSearch() {
		return regulationSocializationApprovalSearch;
	}

	public void setRegulationSocializationApprovalSearch(String regulationSocializationApprovalSearch) {
		this.regulationSocializationApprovalSearch = regulationSocializationApprovalSearch;
	}
	
	public DBLazyDataModel<TmpAuditApprovalVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TmpAuditApprovalVO> tableModel) {
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
		TmpAuditApprovalBean.logger = logger;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public TmpAuditApprovalService getTmpAuditApprovalService() {
		return TmpAuditApprovalService;
	}

	public void setTmpAuditApprovalService(TmpAuditApprovalService tmpAuditApprovalService) {
		TmpAuditApprovalService = tmpAuditApprovalService;
	}
	
}