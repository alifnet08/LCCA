package com.wo.module.parameter.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.constant.ParameterDetailConstant;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.parameter.service.ParameterHeaderService;

public class ParameterDetailBean extends CommonBean implements Serializable{
	
	private static final long serialVersionUID = -640195510399640535L;
	static Logger logger = Logger.getLogger(ParameterDetailBean.class);
	
	private String parameterDetailSearch;
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private String parameterCode;
	
	private ParameterHeaderService parameterHeaderService;
	
	private List<ParameterDetail> parameterDetailList;
	
	private List<SelectItem> parameterList;
	
	private DBLazyDataModel<ParameterDetail> tableModel;
	
	public FacesUtil facesUtil;
	
	private String navigateEdit = ParameterDetailConstant.NAVIGATE_EDIT;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@SuppressWarnings("static-access")
	public void selectParamList() {
    	parameterList = new ArrayList<SelectItem>();
    	try {
			List<ParameterHeader> pd = parameterHeaderService.getListParameterAllOrderByName();
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
				if(locale!=null && locale.equals(locale.ENGLISH)) {
					si.setLabel(((ParameterHeader)pd.get(i)).getNameEn());
				}else {
					si.setLabel(((ParameterHeader)pd.get(i)).getNameIn());
				}
				si.setValue(((ParameterHeader)pd.get(i)).getParameterCode());
				parameterList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
    }
	
	@PostConstruct
	public void init() {
		super.init();
		selectParamList();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<ParameterDetail>(parameterDetailService, paging);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void search(ActionEvent actionEvent) {
		tableModel.setSearchCriteria(
				Arrays.asList(
					new DefaultSearchObject(ParameterDetailConstant.WHERE_PARAMETER_DETAIL, searchVal),
					new DefaultSearchObject(ParameterDetailConstant.WHERE_PARAMETER_CODE, parameterCode)));
	}
	
	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		parameterCode = "";
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}
	
	public void delete(Long deleteId) {
		try {
			ParameterDetail pd = parameterDetailService.findById(deleteId);
			pd.setEnabledFlag(Constants.CONSTANT_NO);
			pd.setLastUpdateBy(facesUtil.retrieveUserLogin());
			pd.setLastUpdateDate(new Timestamp(new Date().getTime()));
			parameterDetailService.update(pd);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR
					, null
					,"Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public Boolean getIsLogin() {
		if(facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null 
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null )
			return true;
		else
			return false;
	}
	
	public String getParameterDetailSearch() {
		return parameterDetailSearch;
	}

	public void setParameterDetailSearch(String parameterDetailSearch) {
		this.parameterDetailSearch = parameterDetailSearch;
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

	public List<ParameterDetail> getParameterDetailList() {
		return parameterDetailList;
	}

	public void setParameterDetailList(List<ParameterDetail> parameterDetailList) {
		this.parameterDetailList = parameterDetailList;
	}

	public DBLazyDataModel<ParameterDetail> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ParameterDetail> tableModel) {
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

	public ParameterHeaderService getParameterHeaderService() {
		return parameterHeaderService;
	}

	public void setParameterHeaderService(ParameterHeaderService parameterHeaderService) {
		this.parameterHeaderService = parameterHeaderService;
	}

	public List<SelectItem> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<SelectItem> parameterList) {
		this.parameterList = parameterList;
	}

	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}

}