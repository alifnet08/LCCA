package com.wo.module.dbCompliance.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.wo.module.dbCompliance.constant.DBComplianceConstants;
import com.wo.module.dbCompliance.model.DBCompliance;
import com.wo.module.dbCompliance.service.DBComplianceService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;

public class DBComplianceBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DBComplianceBean.class);

	private Long reportTypeId;
	
	private String reportName;
	
	private Date date;
	
	private int paging;

	private DBComplianceService dbComplianceService;
	
	private ReportTypeService reportTypeService;
	
	private List<DBCompliance> dbComplianceList;

	private DBLazyDataModel<DBCompliance> tableModel;
	
	private List<SelectItem> reportTypeList;
	
	private List<SelectItem> statusList;

	public FacesUtil facesUtil;

	private String navigateEdit = DBComplianceConstants.NAVIGATE_EDIT;

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
		initList();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<DBCompliance>(dbComplianceService, paging);
	}
	
	public void initList(){
		try {
		reportTypeList = new ArrayList<SelectItem>();
		List<ReportType> listReportType = reportTypeService.getAllReportType();
		

		for (ReportType vo : listReportType) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getReportTypeIn());
			si.setValue(vo.getReportTypeId());
			reportTypeList.add(si);
		}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (reportTypeId != null ) {
			searchCriteria.add(new DefaultSearchObject(DBComplianceConstants.SEARCH_BY_REPORT_TYPE, reportTypeId));
		}
		if (reportName != null && !reportName.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(DBComplianceConstants.SEARCH_BY_REPORT_NAME, reportName));
		}
		if (date != null) {
			searchCriteria.add(new DefaultSearchObject(DBComplianceConstants.SEARCH_BY_DATE, sdf.format(date)));
		}

		tableModel.setSearchCriteria(searchCriteria);
		
	}

	public void reset(ActionEvent actionEvent) {
		reportTypeId = null;
		reportName = null;
		date = null;
		
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
				DBCompliance entity = dbComplianceService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				dbComplianceService.update(entity);
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

	

	public DBComplianceService getDBComplianceService() {
		return dbComplianceService;
	}

	public void setDBComplianceService(DBComplianceService dbComplianceService) {
		this.dbComplianceService = dbComplianceService;
	}

	public DBLazyDataModel<DBCompliance> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<DBCompliance> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}


	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<DBCompliance> getDBComplianceList() {
		return dbComplianceList;
	}

	public void setDBComplianceList(List<DBCompliance> dbComplianceList) {
		this.dbComplianceList = dbComplianceList;
	}


	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		DBComplianceBean.logger = logger;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public Long getReportTypeId() {
		return reportTypeId;
	}

	public void setReportTypeId(Long reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public DBComplianceService getDbComplianceService() {
		return dbComplianceService;
	}

	public void setDbComplianceService(DBComplianceService dbComplianceService) {
		this.dbComplianceService = dbComplianceService;
	}

	public List<DBCompliance> getDbComplianceList() {
		return dbComplianceList;
	}

	public void setDbComplianceList(List<DBCompliance> dbComplianceList) {
		this.dbComplianceList = dbComplianceList;
	}

	public List<SelectItem> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<SelectItem> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	

	

	

	
	

}