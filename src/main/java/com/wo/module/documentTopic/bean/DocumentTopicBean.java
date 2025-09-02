package com.wo.module.documentTopic.bean;

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
import com.wo.module.common.paging.SearchObject;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.lov.bean.FacesUtil;

public class DocumentTopicBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DocumentTopicBean.class);

	private String documentTopicSearch;

	private int paging;

	private String searchVal;

	private Long deleteId;

	private DocumentTopicService documentTopicService;

	private List<DocumentTopic> documentTopicList;

	private DBLazyDataModel<DocumentTopic> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = DocumentTopicConstants.NAVIGATE_EDIT;

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
		tableModel = new DBLazyDataModel<DocumentTopic>(documentTopicService, paging);

	}

	public void search(ActionEvent actionEvent) {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}

	public void delete(Long deleteId) {
		try {
			if(documentTopicService.isUsedInTransaction(deleteId)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTopicIsUsedInTransaction"));
			} else {
				DocumentTopic dt = documentTopicService.findById(deleteId);
				dt.setEnabledFlag(Constants.CONSTANT_NO);
				dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
				dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
				documentTopicService.update(dt);
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			}
			
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

	public String getDocumentTopicSearch() {
		return documentTopicSearch;
	}

	public void setDocumentTopicSearch(String documentTopicSearch) {
		this.documentTopicSearch = documentTopicSearch;
	}

	public DocumentTopicService getDocumentTopicService() {
		return documentTopicService;
	}

	public void setDocumentTopicService(DocumentTopicService documentTopicService) {
		this.documentTopicService = documentTopicService;
	}

	public List<DocumentTopic> getDocumentTopicList() {
		return documentTopicList;
	}

	public void setDocumentTopicList(List<DocumentTopic> documentTopicList) {
		this.documentTopicList = documentTopicList;
	}

	public DBLazyDataModel<DocumentTopic> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<DocumentTopic> tableModel) {
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

}