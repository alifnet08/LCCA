package com.wo.module.qa.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.service.QAService;

public class QABean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(QABean.class);

	private String qaSearch;

	private int paging;

	private String searchVal;

	private Long deleteId;

	private QAService qaService;

	private List<QA> qaList;

	private DBLazyDataModel<QA> tableModel;

	public FacesUtil facesUtil;
	
	private Long userIdLogin;

	private String navigateEdit = QAConstants.NAVIGATE_EDIT;

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
		userIdLogin = facesUtil.getUserLogin().getUserId();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<QA>(qaService, paging);

	}

	public void search(ActionEvent actionEvent) {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(QAConstants.WHERE_QUESTION, searchVal)));
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(QAConstants.WHERE_QUESTION, searchVal)));
	}

	public void delete(Long deleteId) {
		try {

			QA dt = qaService.findById(deleteId);
			dt.setEnabledFlag(Constants.CONSTANT_NO);
			dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
			dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
			qaService.update(dt);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));

		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	public String getQaSearch() {
		return qaSearch;
	}

	public void setQaSearch(String qaSearch) {
		this.qaSearch = qaSearch;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		QABean.logger = logger;
	}

	public QAService getQaService() {
		return qaService;
	}

	public void setQaService(QAService qaService) {
		this.qaService = qaService;
	}

	public List<QA> getQaList() {
		return qaList;
	}

	public void setQaList(List<QA> qaList) {
		this.qaList = qaList;
	}

	public DBLazyDataModel<QA> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<QA> tableModel) {
		this.tableModel = tableModel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Long getUserIdLogin() {
		return userIdLogin;
	}

	public void setUserIdLogin(Long userIdLogin) {
		this.userIdLogin = userIdLogin;
	}
	
	

}