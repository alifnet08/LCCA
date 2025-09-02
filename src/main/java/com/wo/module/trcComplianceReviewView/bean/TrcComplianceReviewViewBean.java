package com.wo.module.trcComplianceReviewView.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.complianceTestingMockup.constant.ComplianceTestingMockupConstants;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.trcComplianceReview.constant.TrcComplianceReviewConstants;
import com.wo.module.trcComplianceReviewView.constant.TrcComplianceReviewViewConstants;
import com.wo.module.trcComplianceReviewView.service.TrcComplianceReviewViewService;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TrcComplianceReviewViewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 7789378048838667090L;

	static Logger logger = Logger.getLogger(TrcComplianceReviewViewBean.class);

	private String searchVal;
	
	private String searchInspectionTitle;
	
	private String searchInspectionNo;
	
	private Date searchPeriodFrom;
	private Date searchPeriodTo;
	
	private String searchFollowupStatus;
	private String searchComplianceStatus;
	
	private List<SelectItem> divisions;

	private List<SelectItem> statusList;
	
	private List<SelectItem> selectFollowupStatus;
	private List<SelectItem> selectComplianceStatus;

	private DBLazyDataModel<ComplianceTestingVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = TrcComplianceReviewViewConstants.NAVIGATE_EDIT;

	
	private TrcComplianceReviewViewService trcComplianceReviewViewService;
	private UserService userService;
	
	

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
		selectStatus();
		selectDivision();
		setupFollowupStatus();
		setupComplianceStatus();
	
		tableModel = new DBLazyDataModel<ComplianceTestingVO>(trcComplianceReviewViewService, paging);
		
		
	}
	
	public void setupFollowupStatus() {
		selectFollowupStatus = new ArrayList<SelectItem>();
		try {
			selectFollowupStatus = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_PIC_FOLLOWUP_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupComplianceStatus() {
		selectComplianceStatus = new ArrayList<SelectItem>();
		try {
			selectComplianceStatus = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsUtil.reInitSelect2();
	}
	

	public void selectStatus() {
		statusList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("DATA_STATUS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search(ActionEvent actionEvent) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_INSPECTION_TITLE, searchInspectionTitle),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_INSPECTION_NO, searchInspectionNo),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_COMPLIANCE_STATUS, searchComplianceStatus),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_FOLLOWUP_STATUS, searchFollowupStatus),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_PERIOD_FROM, searchPeriodFrom!=null?sdf.format(searchPeriodFrom):null),
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_PERIOD_TO, searchPeriodTo!=null?sdf.format(searchPeriodTo):null)
				));
	}

	public static Logger getLogger() {
		return logger;
	}

	
	public static void setLogger(Logger logger) {
		TrcComplianceReviewViewBean.logger = logger;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}


	public DBLazyDataModel<ComplianceTestingVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ComplianceTestingVO> tableModel) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	

	public String getSearchInspectionTitle() {
		return searchInspectionTitle;
	}

	public void setSearchInspectionTitle(String searchInspectionTitle) {
		this.searchInspectionTitle = searchInspectionTitle;
	}

	public String getSearchInspectionNo() {
		return searchInspectionNo;
	}

	public void setSearchInspectionNo(String searchInspectionNo) {
		this.searchInspectionNo = searchInspectionNo;
	}


	public TrcComplianceReviewViewService getTrcComplianceReviewViewService() {
		return trcComplianceReviewViewService;
	}

	public void setTrcComplianceReviewViewService(TrcComplianceReviewViewService trcComplianceReviewViewService) {
		this.trcComplianceReviewViewService = trcComplianceReviewViewService;
	}

	public List<SelectItem> getSelectFollowupStatus() {
		return selectFollowupStatus;
	}

	public void setSelectFollowupStatus(List<SelectItem> selectFollowupStatus) {
		this.selectFollowupStatus = selectFollowupStatus;
	}

	public List<SelectItem> getSelectComplianceStatus() {
		return selectComplianceStatus;
	}

	public void setSelectComplianceStatus(List<SelectItem> selectComplianceStatus) {
		this.selectComplianceStatus = selectComplianceStatus;
	}

	public String getSearchFollowupStatus() {
		return searchFollowupStatus;
	}

	public void setSearchFollowupStatus(String searchFollowupStatus) {
		this.searchFollowupStatus = searchFollowupStatus;
	}

	public String getSearchComplianceStatus() {
		return searchComplianceStatus;
	}

	public void setSearchComplianceStatus(String searchComplianceStatus) {
		this.searchComplianceStatus = searchComplianceStatus;
	}

	public Date getSearchPeriodFrom() {
		return searchPeriodFrom;
	}

	public void setSearchPeriodFrom(Date searchPeriodFrom) {
		this.searchPeriodFrom = searchPeriodFrom;
	}

	public Date getSearchPeriodTo() {
		return searchPeriodTo;
	}

	public void setSearchPeriodTo(Date searchPeriodTo) {
		this.searchPeriodTo = searchPeriodTo;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
}