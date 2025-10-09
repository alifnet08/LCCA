package com.wo.module.log.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.log.constant.LogConstant;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.model.SearchValueObject;
import com.wo.module.parameter.model.ParameterHeader;

public class LogHeaderBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 3981404116134256542L;
	private Date searchProcessDateFrom;
	private Date searchProcessDateTo;
	private String searchFilename;
	private String searchRemarks;
	private String searchFunctionId;
	private String searchProcessId;
	private String searchProcessStatus;
	private List<SelectItem> searchProcessStatusList;
	private List<SelectItem> searchFunctionList;
	public FacesUtil facesUtil;
	private String searchVal;
	private DBLazyDataModel<LogHeader> tableModel;
	private String navigateDetail = LogConstant.NAVIGATE_DETAIL;
	
	/*
	 * Services
	 */
	private LogService logService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void init() {
		super.init();
		constructSelectComponent();
		tableModel = new DBLazyDataModel<LogHeader>(logService, paging);
		tableModel.setSearchCriteria(new ArrayList<SearchValueObject>());
	}
	
	private void constructSelectComponent() {
		setupProcessStatus();
		setupFunctionList();
	}
	
	private void setupProcessStatus() {
		try {
			searchProcessStatusList = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_LOG_PROCESS_STATUS, false,true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupFunctionList() {
		try {
			searchFunctionList = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_LOG_FUNCTION_ID, false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchProcessDateFrom != null)
			searchCriteria.add(new SearchValueObject("searchProcessDateFrom", searchProcessDateFrom));

		if (searchProcessDateTo != null)
			searchCriteria.add(new SearchValueObject("searchProcessDateTo", searchProcessDateTo));

		if (StringUtils.isNotBlank(searchProcessId) && StringUtils.isNotEmpty(searchProcessId))
			searchCriteria.add(new SearchValueObject("processId", searchProcessId));

		if (StringUtils.isNotBlank(searchProcessStatus) && StringUtils.isNotEmpty(searchProcessStatus))
			searchCriteria.add(new SearchValueObject("processSts", searchProcessStatus));

		if (StringUtils.isNotBlank(searchFunctionId) && StringUtils.isNotEmpty(searchFunctionId))
			searchCriteria.add(new SearchValueObject("functionId", searchFunctionId));

		if (StringUtils.isNotBlank(searchFilename))
			searchCriteria.add(new SearchValueObject("searchFilename", searchFilename));

		if (StringUtils.isNotBlank(searchRemarks))
			searchCriteria.add(new SearchValueObject("remarks", searchRemarks));

		try {
			tableModel.setSearchCriteria(searchCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void reset(ActionEvent actionEvent) {
		searchProcessDateFrom = null;
		searchProcessDateTo = null;
		searchProcessId = null;
		searchProcessStatus = null;
		searchFunctionId= null;
		searchFilename= null;
		searchRemarks = null;
		
		tableModel.setSearchCriteria(new ArrayList<SearchValueObject>());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public Date getSearchProcessDateFrom() {
		return searchProcessDateFrom;
	}

	public void setSearchProcessDateFrom(Date searchProcessDateFrom) {
		this.searchProcessDateFrom = searchProcessDateFrom;
	}

	public Date getSearchProcessDateTo() {
		return searchProcessDateTo;
	}

	public void setSearchProcessDateTo(Date searchProcessDateTo) {
		this.searchProcessDateTo = searchProcessDateTo;
	}

	public List<SelectItem> getSearchProcessStatusList() {
		return searchProcessStatusList;
	}

	public void setSearchProcessStatusList(List<SelectItem> searchProcessStatusList) {
		this.searchProcessStatusList = searchProcessStatusList;
	}

	public List<SelectItem> getSearchFunctionList() {
		return searchFunctionList;
	}

	public void setSearchFunctionList(List<SelectItem> searchFunctionList) {
		this.searchFunctionList = searchFunctionList;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getSearchFilename() {
		return searchFilename;
	}

	public void setSearchFilename(String searchFilename) {
		this.searchFilename = searchFilename;
	}

	public String getSearchRemarks() {
		return searchRemarks;
	}

	public void setSearchRemarks(String searchRemarks) {
		this.searchRemarks = searchRemarks;
	}

	public String getSearchFunctionId() {
		return searchFunctionId;
	}

	public void setSearchFunctionId(String searchFunctionId) {
		this.searchFunctionId = searchFunctionId;
	}

	public String getSearchProcessId() {
		return searchProcessId;
	}

	public void setSearchProcessId(String searchProcessId) {
		this.searchProcessId = searchProcessId;
	}

	public String getSearchProcessStatus() {
		return searchProcessStatus;
	}

	public void setSearchProcessStatus(String searchProcessStatus) {
		this.searchProcessStatus = searchProcessStatus;
	}

	public DBLazyDataModel<LogHeader> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<LogHeader> tableModel) {
		this.tableModel = tableModel;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public String getNavigateDetail() {
		return navigateDetail;
	}

	public void setNavigateDetail(String navigateDetail) {
		this.navigateDetail = navigateDetail;
	}
}
