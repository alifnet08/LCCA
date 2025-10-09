package com.wo.module.qaCategory.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.qaCategory.constant.QACategoryConstants;
import com.wo.module.qaCategory.model.QACategory;
import com.wo.module.qaCategory.service.QACategoryService;

public class QACategoryBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(QACategoryBean.class);

	private String qaSearch;

	private int paging;

	private String searchVal;

	private QACategoryService qaCategoryService;

	private DBLazyDataModel<QACategory> tableModel;

	public FacesUtil facesUtil;

	private Long userIdLogin;

	private String navigateEdit = QACategoryConstants.NAVIGATE_EDIT;

	private List<SelectItem> categoryList;

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
		addCategoryList();
		userIdLogin = facesUtil.getUserLogin().getUserId();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<QACategory>(qaCategoryService, paging);

	}

	public void search(ActionEvent actionEvent) {
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(QACategoryConstants.WHERE_CATEGORY, searchVal)));
		PrimeFaces.current().executeScript("initSelect2();");
		
		
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(QACategoryConstants.WHERE_CATEGORY, searchVal)));
		//PrimeFaces.current().executeScript("initSelect2();");
	}

	public void addCategoryList() {
		categoryList = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_CATEGORY);
			for (ParameterDetail param : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(param.getName());
				si.setValue(param.getParameterDtlCode());
				categoryList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		QACategoryBean.logger = logger;
	}

	public QACategoryService getQaCategoryService() {
		return qaCategoryService;
	}

	public void setQaCategoryService(QACategoryService qaCategoryService) {
		this.qaCategoryService = qaCategoryService;
	}

	public DBLazyDataModel<QACategory> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<QACategory> tableModel) {
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

	public Long getUserIdLogin() {
		return userIdLogin;
	}

	public void setUserIdLogin(Long userIdLogin) {
		this.userIdLogin = userIdLogin;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

}