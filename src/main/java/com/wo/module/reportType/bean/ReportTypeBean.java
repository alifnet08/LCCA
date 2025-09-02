package com.wo.module.reportType.bean;

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
import com.wo.module.reportType.constant.ReportTypeConstants;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.lov.bean.FacesUtil;

public class ReportTypeBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ReportTypeBean.class);

	private String reportTypeSearch;

	private int paging;

	private String searchVal;

	private Long deleteId;

	private ReportTypeService reportTypeService;

	private List<ReportType> reportTypeList;

	private DBLazyDataModel<ReportType> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = ReportTypeConstants.NAVIGATE_EDIT;

	private String YES = Constants.CONSTANT_YES;

	private String NO = Constants.CONSTANT_NO;

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
		tableModel = new DBLazyDataModel<ReportType>(reportTypeService, paging);

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
			if(reportTypeService.isUsedInTransaction(deleteId)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportTypeIsUsedInTransaction"));
			} else {			
				ReportType dt = reportTypeService.findById(deleteId);
				dt.setEnabledFlag(Constants.CONSTANT_NO);
				dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
				dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
				reportTypeService.update(dt);
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

	public String getReportTypeSearch() {
		return reportTypeSearch;
	}

	public void setReportTypeSearch(String reportTypeSearch) {
		this.reportTypeSearch = reportTypeSearch;
	}

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public List<ReportType> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<ReportType> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public DBLazyDataModel<ReportType> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ReportType> tableModel) {
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

	public String getYES() {
		return YES;
	}

	public void setYES(String yES) {
		YES = yES;
	}

	public String getNO() {
		return NO;
	}

	public void setNO(String nO) {
		NO = nO;
	}

}