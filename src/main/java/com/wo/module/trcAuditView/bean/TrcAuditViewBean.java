package com.wo.module.trcAuditView.bean;

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
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.trcAuditView.constant.AuditViewConstant;
import com.wo.module.trcAuditView.service.TrcAuditViewService;
import com.wo.module.trcAuditView.vo.TrcAuditViewVO;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TrcAuditViewBean extends CommonBean implements Serializable, AuditViewConstant {

	private static final long serialVersionUID = -8196206199491066951L;

	static Logger logger = Logger.getLogger(TrcAuditViewBean.class);

	private String regulationSocializationTmpSearch;

	private String searchVal;

	private String searchAuditFollowUp;
	private String searchAuditObject;
	private String searchStatusCode;
	private Date searchAuditPeriodFrom;
	private Date searchAuditPeriodTo;
	private Date searchAuditTargetFrom;
	private Date searchAuditTargetTo;
	private Long deleteId;
	private String searchDivision;
	private String searchFollowupStatus;
	private String searchComplianceStatus;
	private String searchFindingName;
	private Long searchAuditTemplateName;
	
	private Date effDateFrom;
	private Date effDateTo;
	private Date targetDateFrom;
	private Date targetDateTo;

	private Long regulationId;

	private List<SelectItem> selectAuditFollowUp;
	private List<SelectItem> selectAuditObject;
	private List<SelectItem> status;
	private List<SelectItem> selectDivisions;
	private List<SelectItem> selectFollowupStatus;
	private List<SelectItem> selectComplianceStatus;
	private List<SelectItem> selectAuditTemplateName;

	private SelectorInfo selectorRekamJejak;

	// private ParameterDetailService parameterDetailService;

	private TrcAuditViewService trcAuditViewService;
	private UserService userService;
	private MstAuditService mstAuditService;

	private DBLazyDataModel<TrcAuditViewVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateView = NAVIGATE_VIEW_DETAIL;

	@PostConstruct
	public void init() {
		super.init();
		constructSelectComponent();
		tableModel = new DBLazyDataModel<TrcAuditViewVO>(trcAuditViewService, paging);
	}

	private void constructSelectComponent() {
		setupAuditFollowUp();
		setupAuditObject();
		setupStatus();
		setupAuditPicFollowupDivision();
		setupFollowupStatus();
		setupComplianceStatus();
		setupAuditTemplateName();
	}

	private void setupAuditFollowUp() {
		selectAuditFollowUp = new ArrayList<SelectItem>();
		try {
			selectAuditFollowUp = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDITOR,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupAuditObject() {
		selectAuditObject = new ArrayList<SelectItem>();
		try {
			selectAuditObject = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDIT_OBJECT,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupStatus() {
		status = new ArrayList<SelectItem>();
		try {
			status = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupAuditPicFollowupDivision() {
		selectDivisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				selectDivisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//PrimeFaces.current().executeScript("initSelect2();");
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

	private void setupAuditTemplateName() {
		selectAuditTemplateName = new ArrayList<SelectItem>();
		try {
			List<MstAudit> listMstAudit = mstAuditService.getAllMstAuditData();
			for (int i = 0; i < listMstAudit.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel((listMstAudit).get(i).getAuditTemplate());
				si.setValue((listMstAudit).get(i).getMstAuditId());
				selectAuditTemplateName.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void search(ActionEvent actionEvent) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(WHERE_AUDIT_FOLLOWUP, searchAuditFollowUp),
				new DefaultSearchObject(WHERE_AUDIT_OBJECT, searchAuditObject),
				new DefaultSearchObject(WHERE_AUDIT_STATUS, searchStatusCode),
				new DefaultSearchObject(WHERE_AUDIT_PERIOD_FROM, searchAuditPeriodFrom != null ? sdf.format(searchAuditPeriodFrom) : ""),
				new DefaultSearchObject(WHERE_AUDIT_PERIOD_TO, searchAuditPeriodTo != null ? sdf.format(searchAuditPeriodTo) : ""),
				new DefaultSearchObject(WHERE_AUDIT_TARGET_FROM, searchAuditTargetFrom != null ? sdf.format(searchAuditTargetFrom) : ""),
				new DefaultSearchObject(WHERE_AUDIT_TARGET_TO, searchAuditTargetTo != null ? sdf.format(searchAuditTargetTo) : ""),
				new DefaultSearchObject(WHERE_AUDIT_DIVISION_ID, searchDivision),
				new DefaultSearchObject(WHERE_AUDIT_FOLLOWUP_STATUS, searchFollowupStatus),
				new DefaultSearchObject(WHERE_AUDIT_COMPLIANCE_STATUS, searchComplianceStatus),
				new DefaultSearchObject(WHERE_AUDIT_FINDING_NAME, searchFindingName),
				new DefaultSearchObject(WHERE_AUDIT_TEMPLATE_NAME, searchAuditTemplateName)));
	}

	public void reset(ActionEvent actionEvent) {
		searchAuditFollowUp = null;
		searchAuditObject = null;
		searchStatusCode = null;
		searchAuditPeriodFrom = null;
		searchAuditPeriodTo= null;
		searchAuditTargetFrom= null;
		searchAuditTargetTo = null;
		searchFindingName = null;
		searchAuditTemplateName = null;
		searchDivision = null;
		searchFollowupStatus = null;
		searchComplianceStatus = null;
		
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
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
	
	public String getRegulationSocializationTmpSearch() {
		return regulationSocializationTmpSearch;
	}

	public void setRegulationSocializationTmpSearch(String regulationSocializationTmpSearch) {
		this.regulationSocializationTmpSearch = regulationSocializationTmpSearch;
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

	public Date getEffDateFrom() {
		return effDateFrom;
	}

	public void setEffDateFrom(Date effDateFrom) {
		this.effDateFrom = effDateFrom;
	}

	public Date getEffDateTo() {
		return effDateTo;
	}

	public void setEffDateTo(Date effDateTo) {
		this.effDateTo = effDateTo;
	}

	public Date getTargetDateFrom() {
		return targetDateFrom;
	}

	public void setTargetDateFrom(Date targetDateFrom) {
		this.targetDateFrom = targetDateFrom;
	}

	public Date getTargetDateTo() {
		return targetDateTo;
	}

	public void setTargetDateTo(Date targetDateTo) {
		this.targetDateTo = targetDateTo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getStatus() {
		return status;
	}

	public void setStatus(List<SelectItem> status) {
		this.status = status;
	}

	public SelectorInfo getSelectorRekamJejak() {
		// return
		// RegulationSocializationTmpConstants.buildSelectorRekamJejak(regulationId);
		return selectorRekamJejak;
	}

	public void setSelectorRekamJejak(SelectorInfo selectorRekamJejak) {
		this.selectorRekamJejak = selectorRekamJejak;
	}

	public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}

	public DBLazyDataModel<TrcAuditViewVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TrcAuditViewVO> tableModel) {
		this.tableModel = tableModel;
	}

	public List<SelectItem> getSelectAuditFollowUp() {
		return selectAuditFollowUp;
	}

	public void setSelectAuditFollowUp(List<SelectItem> selectAuditFollowUp) {
		this.selectAuditFollowUp = selectAuditFollowUp;
	}

	public List<SelectItem> getSelectAuditObject() {
		return selectAuditObject;
	}

	public void setSelectAuditObject(List<SelectItem> selectAuditObject) {
		this.selectAuditObject = selectAuditObject;
	}

	public String getSearchAuditFollowUp() {
		return searchAuditFollowUp;
	}

	public void setSearchAuditFollowUp(String searchAuditFollowUp) {
		this.searchAuditFollowUp = searchAuditFollowUp;
	}

	public String getSearchAuditObject() {
		return searchAuditObject;
	}

	public void setSearchAuditObject(String searchAuditObject) {
		this.searchAuditObject = searchAuditObject;
	}

	public String getSearchStatusCode() {
		return searchStatusCode;
	}

	public void setSearchStatusCode(String searchStatusCode) {
		this.searchStatusCode = searchStatusCode;
	}

	public Date getSearchAuditPeriodFrom() {
		return searchAuditPeriodFrom;
	}

	public void setSearchAuditPeriodFrom(Date searchAuditPeriodFrom) {
		this.searchAuditPeriodFrom = searchAuditPeriodFrom;
	}

	public Date getSearchAuditPeriodTo() {
		return searchAuditPeriodTo;
	}

	public void setSearchAuditPeriodTo(Date searchAuditPeriodTo) {
		this.searchAuditPeriodTo = searchAuditPeriodTo;
	}

	public Date getSearchAuditTargetFrom() {
		return searchAuditTargetFrom;
	}

	public void setSearchAuditTargetFrom(Date searchAuditTargetFrom) {
		this.searchAuditTargetFrom = searchAuditTargetFrom;
	}

	public Date getSearchAuditTargetTo() {
		return searchAuditTargetTo;
	}

	public void setSearchAuditTargetTo(Date searchAuditTargetTo) {
		this.searchAuditTargetTo = searchAuditTargetTo;
	}

	public TrcAuditViewService getTrcAuditViewService() {
		return trcAuditViewService;
	}

	public void setTrcAuditViewService(TrcAuditViewService trcAuditViewService) {
		this.trcAuditViewService = trcAuditViewService;
	}

	public String getNavigateView() {
		return navigateView;
	}

	public void setNavigateView(String navigateView) {
		this.navigateView = navigateView;
	}

	public String getSearchDivision() {
		return searchDivision;
	}

	public void setSearchDivision(String searchDivision) {
		this.searchDivision = searchDivision;
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

	public List<SelectItem> getSelectDivisions() {
		return selectDivisions;
	}

	public void setSelectDivisions(List<SelectItem> selectDivisions) {
		this.selectDivisions = selectDivisions;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSearchFindingName() {
		return searchFindingName;
	}

	public void setSearchFindingName(String searchFindingName) {
		this.searchFindingName = searchFindingName;
	}

	public Long getSearchAuditTemplateName() {
		return searchAuditTemplateName;
	}

	public void setSearchAuditTemplateName(Long searchAuditTemplateName) {
		this.searchAuditTemplateName = searchAuditTemplateName;
	}

	public List<SelectItem> getSelectAuditTemplateName() {
		return selectAuditTemplateName;
	}

	public void setSelectAuditTemplateName(List<SelectItem> selectAuditTemplateName) {
		this.selectAuditTemplateName = selectAuditTemplateName;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

}