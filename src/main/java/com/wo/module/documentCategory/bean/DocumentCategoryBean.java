package com.wo.module.documentCategory.bean;

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
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.lov.bean.FacesUtil;

public class DocumentCategoryBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DocumentCategoryBean.class);

	private String documentCategorySearch;

	private int paging;

	private String searchVal;

	private Long deleteId;

	private DocumentCategoryService documentCategoryService;

	private List<DocumentCategory> documentCategoryList;

	private DBLazyDataModel<DocumentCategory> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = DocumentCategoryConstants.NAVIGATE_EDIT;

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
		tableModel = new DBLazyDataModel<DocumentCategory>(documentCategoryService, paging);

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
			if(documentCategoryService.isUsedInTransaction(deleteId)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentCategoryIsUsedInTransaction"));
			} else {
				DocumentCategory dt = documentCategoryService.findById(deleteId);
				dt.setEnabledFlag(Constants.CONSTANT_NO);
				dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
				dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
				documentCategoryService.update(dt);
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

	public String getDocumentCategorySearch() {
		return documentCategorySearch;
	}

	public void setDocumentCategorySearch(String documentCategorySearch) {
		this.documentCategorySearch = documentCategorySearch;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public List<DocumentCategory> getDocumentCategoryList() {
		return documentCategoryList;
	}

	public void setDocumentCategoryList(List<DocumentCategory> documentCategoryList) {
		this.documentCategoryList = documentCategoryList;
	}

	public DBLazyDataModel<DocumentCategory> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<DocumentCategory> tableModel) {
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