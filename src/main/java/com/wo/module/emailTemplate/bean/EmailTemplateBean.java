package com.wo.module.emailTemplate.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.wo.module.common.paging.SearchObject;
import com.wo.module.emailTemplate.constant.EmailTemplateConstants;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;

public class EmailTemplateBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(EmailTemplateBean.class);

	private String searchVal;

	private int paging;

	private EmailTemplateService emailTemplateService;

	private List<EmailTemplate> emailTemplateList;

	private DBLazyDataModel<EmailTemplate> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = EmailTemplateConstants.NAVIGATE_EDIT;

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
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<EmailTemplate>(emailTemplateService, paging);
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchVal != null && !searchVal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(EmailTemplateConstants.SEARCH_BY_NAME, searchVal));
		}

		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}
	
	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		search(actionEvent);
	}	

	public void delete(Long deleteId) {
		try {
			EmailTemplate entity = emailTemplateService.findById(deleteId);
			entity.setEnabledFlag(Constants.CONSTANT_NO);
			entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
			entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
			emailTemplateService.update(entity);
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

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public List<EmailTemplate> getEmailTemplateList() {
		return emailTemplateList;
	}

	public void setEmailTemplateList(List<EmailTemplate> emailTemplateList) {
		this.emailTemplateList = emailTemplateList;
	}

	public DBLazyDataModel<EmailTemplate> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<EmailTemplate> tableModel) {
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

}