package com.wo.module.tmpComplianceReviewApproval.bean;

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
import com.wo.module.tmpComplianceReviewApproval.constant.TmpComplianceReviewApprovalConstants;
import com.wo.module.tmpComplianceReviewApproval.service.ComplianceReviewApprovalService;
import com.wo.module.tmpComplianceReviewApproval.vo.ComplianceReviewApprovalVO;
import com.wo.module.lov.bean.FacesUtil;

public class TmpComplianceReviewApprovalBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TmpComplianceReviewApprovalBean.class);

	private String regulationSocializationApprovalSearch;

	private int paging;

	private String searchVal;

	private Long deleteId;

	private DBLazyDataModel<ComplianceReviewApprovalVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = TmpComplianceReviewApprovalConstants.NAVIGATE_EDIT;

	private ComplianceReviewApprovalService complianceReviewApprovalService;

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
		tableModel = new DBLazyDataModel<ComplianceReviewApprovalVO>(complianceReviewApprovalService, paging);
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
	}

	public void search(ActionEvent actionEvent) {
		tableModel.setSearchCriteria(Arrays.asList(

				new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		tableModel.setSearchCriteria(Arrays.asList(

				new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
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
		TmpComplianceReviewApprovalBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public DBLazyDataModel<ComplianceReviewApprovalVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ComplianceReviewApprovalVO> tableModel) {
		this.tableModel = tableModel;
	}

	public ComplianceReviewApprovalService getComplianceReviewApprovalService() {
		return complianceReviewApprovalService;
	}

	public void setComplianceReviewApprovalService(ComplianceReviewApprovalService complianceReviewApprovalService) {
		this.complianceReviewApprovalService = complianceReviewApprovalService;
	}

}