package com.wo.module.cpsaView.bean;

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
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.task.TaskExecutorBean;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsaView.service.CpsaPicViewService;
import com.wo.module.cpsaView.vo.CpsaPicViewVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.Branch;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class CpsaViewBean extends CommonBean{
	

	private static final long serialVersionUID = 5459259018664531233L;

	Logger logger = Logger.getLogger(CpsaViewBean.class);
	
	private static final String NAVIGATE_DETAIL = CompliancePlanSelfAssessmentConstant.NAVIGATE_CPSA_VIEW_DETAIL;
	private static final String RE_INIT_SELECT2 = "reInitSelect2();";

	private CpsaPicViewService cpsaPicViewService;
	private UserService userService;
	
	private String searchCpsaType;
	private String searchCpsaName;	
	private String searchLetterNo;
	private String searchWorkingUnit;
	private String searchBranch;
	private String searchPic1;
	
	private Date searchUploadDate;
	private Date searchLetterDate;
	private Date searchPeriodStartFrom;
	private Date searchPeriodStartTo;
	private Date searchPeriodEndFrom;
	private Date searchPeriodEndTo;
	
	private List<SelectItem> cpsaTypeList;
	private List<SelectItem> workingUnits;
	private List<SelectItem> branchs;
	
	List<String> filesListInDir = new ArrayList<>();
	
	private DBLazyDataModel<CpsaPicViewVo> cpsaPicTableModel;
	
	private FacesUtil facesUtil;
	
	private SimpleDateFormat sdfDateSearch = new SimpleDateFormat("yyyy-MM-dd");
	
	private TaskExecutorBean taskExecutorBean;
	
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
		initComponent();
		cpsaPicTableModel = new DBLazyDataModel<>(cpsaPicViewService, paging);
	}
	
	private void initComponent() {
		initCpsaTypeList();
		initWorkingUnit();
		initBranch();
	}
	
	private void initBranch() {
		branchs = new ArrayList<>();
		try {
			List<Branch> pd = userService.getAllBranch();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Branch) pd.get(i)).getBranchName());
				si.setValue(((Branch) pd.get(i)).getBranchCode());
				branchs.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript(RE_INIT_SELECT2);
	}
	
	private void initWorkingUnit() {
		workingUnits = new ArrayList<>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				workingUnits.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript(RE_INIT_SELECT2);
	}
	
	private void initCpsaTypeList() {
		try {
			cpsaTypeList = new ArrayList<>();
			
			List<ParameterDetail> geteCpsaType = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CPSA_TYPE);
			for (ParameterDetail pd : geteCpsaType) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				cpsaTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript(RE_INIT_SELECT2);
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent event) {
		List<SearchObject> searchCriteria = new ArrayList<>();
		if (searchCpsaType != null && !searchCpsaType.isEmpty() && !searchCpsaType.equals("")) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_TYPE, searchCpsaType));
		}
		if (searchCpsaName != null && !searchCpsaName.isEmpty() && !searchCpsaName.equals("")) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_NAME, searchCpsaName));
		}
		if (searchUploadDate != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_UPLOAD_DATE, sdfDateSearch.format(searchUploadDate)));
		}
		if (searchLetterNo != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_NO, searchLetterNo));
		}
		if (searchLetterDate != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_DATE, sdfDateSearch.format(searchLetterDate)));
		}
		if (searchPeriodStartFrom != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_FROM, sdfDateSearch.format(searchPeriodStartFrom)));
		}
		if (searchPeriodStartTo != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_TO, sdfDateSearch.format(searchPeriodStartTo)));
		}
		if (searchPeriodEndFrom != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_FROM, sdfDateSearch.format(searchPeriodEndFrom)));
		}
		if (searchPeriodEndTo != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_TO, sdfDateSearch.format(searchPeriodEndTo)));
		}
		if (searchWorkingUnit != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_WORKING_UNIT, searchWorkingUnit));
		}
		if (searchBranch != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_BRANCH, searchBranch));
		}
		if (searchPic1 != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PIC_1, searchPic1));
		}
		
		cpsaPicTableModel.setSearchCriteria(searchCriteria);
		
		PrimeFaces.current().executeScript(RE_INIT_SELECT2);
	}
	
	public void reset(ActionEvent event) {
		searchCpsaType = null;
		searchCpsaName = "";
		searchUploadDate = null;
		searchLetterNo = "";
		searchLetterDate = null;
		searchPeriodStartFrom = null;
		searchPeriodStartTo = null;
		searchPeriodEndFrom = null;
		searchPeriodEndTo = null;
		searchWorkingUnit = null;
		searchBranch = null;
		searchPic1 = null;
		
		search(event);
		
		PrimeFaces.current().executeScript(RE_INIT_SELECT2);
	}

	public String getSearchCpsaType() {
		return searchCpsaType;
	}

	public void setSearchCpsaType(String searchCpsaType) {
		this.searchCpsaType = searchCpsaType;
	}

	public String getSearchCpsaName() {
		return searchCpsaName;
	}

	public void setSearchCpsaName(String searchCpsaName) {
		this.searchCpsaName = searchCpsaName;
	}

	public String getSearchLetterNo() {
		return searchLetterNo;
	}

	public void setSearchLetterNo(String searchLetterNo) {
		this.searchLetterNo = searchLetterNo;
	}

	public Date getSearchUploadDate() {
		return searchUploadDate;
	}

	public void setSearchUploadDate(Date searchUploadDate) {
		this.searchUploadDate = searchUploadDate;
	}

	public Date getSearchLetterDate() {
		return searchLetterDate;
	}

	public void setSearchLetterDate(Date searchLetterDate) {
		this.searchLetterDate = searchLetterDate;
	}

	public Date getSearchPeriodStartFrom() {
		return searchPeriodStartFrom;
	}

	public void setSearchPeriodStartFrom(Date searchPeriodStartFrom) {
		this.searchPeriodStartFrom = searchPeriodStartFrom;
	}

	public Date getSearchPeriodStartTo() {
		return searchPeriodStartTo;
	}

	public void setSearchPeriodStartTo(Date searchPeriodStartTo) {
		this.searchPeriodStartTo = searchPeriodStartTo;
	}

	public Date getSearchPeriodEndFrom() {
		return searchPeriodEndFrom;
	}

	public void setSearchPeriodEndFrom(Date searchPeriodEndFrom) {
		this.searchPeriodEndFrom = searchPeriodEndFrom;
	}

	public Date getSearchPeriodEndTo() {
		return searchPeriodEndTo;
	}

	public void setSearchPeriodEndTo(Date searchPeriodEndTo) {
		this.searchPeriodEndTo = searchPeriodEndTo;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public List<SelectItem> getCpsaTypeList() {
		return cpsaTypeList;
	}

	public void setCpsaTypeList(List<SelectItem> cpsaTypeList) {
		this.cpsaTypeList = cpsaTypeList;
	}

	public List<String> getFilesListInDir() {
		return filesListInDir;
	}

	public void setFilesListInDir(List<String> filesListInDir) {
		this.filesListInDir = filesListInDir;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public SimpleDateFormat getSdfDateSearch() {
		return sdfDateSearch;
	}

	public void setSdfDateSearch(SimpleDateFormat sdfDateSearch) {
		this.sdfDateSearch = sdfDateSearch;
	}

	public TaskExecutorBean getTaskExecutorBean() {
		return taskExecutorBean;
	}

	public void setTaskExecutorBean(TaskExecutorBean taskExecutorBean) {
		this.taskExecutorBean = taskExecutorBean;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getWorkingUnits() {
		return workingUnits;
	}

	public void setWorkingUnits(List<SelectItem> workingUnits) {
		this.workingUnits = workingUnits;
	}

	public List<SelectItem> getBranchs() {
		return branchs;
	}

	public void setBranchs(List<SelectItem> branchs) {
		this.branchs = branchs;
	}

	public String getSearchWorkingUnit() {
		return searchWorkingUnit;
	}

	public void setSearchWorkingUnit(String searchWorkingUnit) {
		this.searchWorkingUnit = searchWorkingUnit;
	}

	public String getSearchBranch() {
		return searchBranch;
	}

	public void setSearchBranch(String searchBranch) {
		this.searchBranch = searchBranch;
	}

	public String getSearchPic1() {
		return searchPic1;
	}

	public void setSearchPic1(String searchPic1) {
		this.searchPic1 = searchPic1;
	}

	public CpsaPicViewService getCpsaPicViewService() {
		return cpsaPicViewService;
	}

	public void setCpsaPicViewService(CpsaPicViewService cpsaPicViewService) {
		this.cpsaPicViewService = cpsaPicViewService;
	}

	public DBLazyDataModel<CpsaPicViewVo> getCpsaPicTableModel() {
		return cpsaPicTableModel;
	}

	public void setCpsaPicTableModel(DBLazyDataModel<CpsaPicViewVo> cpsaPicTableModel) {
		this.cpsaPicTableModel = cpsaPicTableModel;
	}

	public static String getNavigateDetail() {
		return NAVIGATE_DETAIL;
	}
}
