package com.wo.module.complianceTestingMockup.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingMockup.constant.ComplianceTestingMockupConstants;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.tmpAudit.vo.TmpAuditVO;
import com.wo.module.tmpComplianceReview.constant.TmpComplianceReviewConstants;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupFindings;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupPoints;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupRegulation;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicFollowupReview;
import com.wo.module.tmpComplianceReview.service.ComplianceReviewService;
import com.wo.module.tmpComplianceReview.service.TmpComplianceReviewService;
import com.wo.module.tmpComplianceReview.vo.ComplianceReviewVO;
import com.wo.module.tmpComplianceReview.vo.StatusConfirmationVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupPoints;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;
import com.wo.module.complianceTestingMockup.service.ComplianceTestingService;

public class ComplianceTestingMockupBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 7789378048838667090L;

	static Logger logger = Logger.getLogger(ComplianceTestingMockupBean.class);

	private String searchVal;
	
	private String searchInspectionTitle;
	
	private String searchInspectionNo;
	
	private List<SelectItem> divisions;

	private List<SelectItem> statusList;

	private DBLazyDataModel<ComplianceTestingVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = ComplianceTestingMockupConstants.NAVIGATE_EDIT;

	private UserService userService;
	
	private ComplianceTestingService complianceTestingService;
	

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
	
		tableModel = new DBLazyDataModel<ComplianceTestingVO>(complianceTestingService, paging);
		
		
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
				new DefaultSearchObject(ComplianceTestingMockupConstants.WHERE_INSPECTION_NO, searchInspectionNo)
				));
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ComplianceTestingMockupBean.logger = logger;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
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

	public ComplianceTestingService getComplianceTestingService() {
		return complianceTestingService;
	}

	public void setComplianceTestingService(ComplianceTestingService complianceTestingService) {
		this.complianceTestingService = complianceTestingService;
	}

	

}