package com.wo.module.trcAuditVerification.bean;

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
import com.wo.module.trcAuditVerification.constant.TrcAuditVerificationConstants;
import com.wo.module.trcAuditVerification.service.TrcAuditVerificationService;
import com.wo.module.trcAuditVerification.vo.TrcAuditVerificationSearchVO;
import com.wo.module.user.model.User;

public class TrcAuditVerificationBean extends CommonBean implements Serializable, TrcAuditVerificationConstants {

	private static final long serialVersionUID = 1795895139612485592L;

	static Logger logger = Logger.getLogger(TrcAuditVerificationBean.class);

	private String searchVal;

	private DBLazyDataModel<TrcAuditVerificationSearchVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = NAVIGATE_EDIT;

	private TrcAuditVerificationService trcAuditVerificationService;

	private String localLanguange;

	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
    	User user = facesUtil.getUserLogin();
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<TrcAuditVerificationSearchVO>(trcAuditVerificationService, paging);
		tableModel.setSearchCriteria(
				Arrays.asList(new DefaultSearchObject(WHERE_USER_ID, user.getUserId())// user.getUserId())
				));

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
	}

	public void search(ActionEvent actionEvent) {
		User user = facesUtil.getUserLogin();
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(WHERE_USER_ID, user.getUserId())));
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrcAuditVerificationBean.logger = logger;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public DBLazyDataModel<TrcAuditVerificationSearchVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TrcAuditVerificationSearchVO> tableModel) {
		this.tableModel = tableModel;
	}

	public TrcAuditVerificationService getTrcAuditVerificationService() {
		return trcAuditVerificationService;
	}

	public void setTrcAuditVerificationService(TrcAuditVerificationService trcAuditVerificationService) {
		this.trcAuditVerificationService = trcAuditVerificationService;
	}

}