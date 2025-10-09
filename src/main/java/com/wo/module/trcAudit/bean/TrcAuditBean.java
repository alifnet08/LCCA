package com.wo.module.trcAudit.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.trcAudit.constant.TrcAuditConstants;
import com.wo.module.trcAudit.service.TrcAuditService;
import com.wo.module.trcAudit.vo.TrcAuditVO;
import com.wo.module.user.model.User;

public class TrcAuditBean extends CommonBean implements Serializable, TrcAuditConstants {

	private static final long serialVersionUID = -4473116966298105934L;

	static Logger logger = Logger.getLogger(TrcAuditBean.class);

	private String searchVal;

	private Long deleteId;

	private DBLazyDataModel<TrcAuditVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = NAVIGATE_EDIT;

	private TrcAuditService trcAuditService;

	private String localLanguange;

	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
		super.init();
		User user = facesUtil.getUserLogin();
		tableModel = new DBLazyDataModel<TrcAuditVO>(trcAuditService, paging);
		tableModel.setSearchCriteria(Arrays
				.asList(new DefaultSearchObject(WHERE_USER_ID, user.getUserId())));

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
	}

	public void search(ActionEvent actionEvent) {
		User user = facesUtil.getUserLogin();
		tableModel.setSearchCriteria(Arrays
				.asList(new DefaultSearchObject(WHERE_USER_ID, user.getUserId())));
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
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
		TrcAuditBean.logger = logger;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public DBLazyDataModel<TrcAuditVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TrcAuditVO> tableModel) {
		this.tableModel = tableModel;
	}

	public TrcAuditService getTrcAuditService() {
		return trcAuditService;
	}

	public void setTrcAuditService(TrcAuditService trcAuditService) {
		this.trcAuditService = trcAuditService;
	}

}