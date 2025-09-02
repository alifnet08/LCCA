package com.wo.module.trcComplianceReviewApproval.bean;

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
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.trcComplianceReviewApproval.constant.TrcComplianceReviewApprovalConstants;
import com.wo.module.trcComplianceReviewApproval.service.TrcComplianceReviewApprovalService;
import com.wo.module.trcComplianceReviewApproval.vo.TrcComplianceReviewApprovalVO;
import com.wo.module.user.model.User;

public class TrcComplianceReviewApprovalBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TrcComplianceReviewApprovalBean.class);

	private int paging;

	private String searchVal;

	private Long deleteId;

	private DBLazyDataModel<ComplianceTestingVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = TrcComplianceReviewApprovalConstants.NAVIGATE_EDIT;

	private TrcComplianceReviewApprovalService trcComplianceReviewApprovalService;

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
		User user = facesUtil.getUserLogin();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<ComplianceTestingVO>(trcComplianceReviewApprovalService, paging);
		tableModel.setSearchCriteria(
				Arrays.asList(new DefaultSearchObject(TrcComplianceReviewApprovalConstants.WHERE_USER_ID, user.getUserId())// user.getUserId())
				));

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
	}

	public void search(ActionEvent actionEvent) {
		User user = facesUtil.getUserLogin();
		tableModel.setSearchCriteria(Arrays
				.asList(new DefaultSearchObject(TrcComplianceReviewApprovalConstants.WHERE_USER_ID, user.getUserId())));
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		tableModel.setSearchCriteria(Arrays.asList(

				new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrcComplianceReviewApprovalBean.logger = logger;
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

	

	public DBLazyDataModel<ComplianceTestingVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ComplianceTestingVO> tableModel) {
		this.tableModel = tableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public TrcComplianceReviewApprovalService getTrcComplianceReviewApprovalService() {
		return trcComplianceReviewApprovalService;
	}

	public void setTrcComplianceReviewApprovalService(
			TrcComplianceReviewApprovalService trcComplianceReviewApprovalService) {
		this.trcComplianceReviewApprovalService = trcComplianceReviewApprovalService;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}