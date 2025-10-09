package com.wo.module.cpsaVerification.bean;

import java.io.Serializable;
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
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsaVerification.constant.CpsaVerificationConstant;
import com.wo.module.cpsaVerification.service.CpsaVerificationService;
import com.wo.module.cpsaVerification.vo.CpsaVerificationVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class CpsaVerificationBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4552757808658867726L;
	
	private static final Logger logger = Logger.getLogger(CpsaVerificationBean.class);
	
	private static final String NAVIGATE_EDIT = CpsaVerificationConstant.NAVIGATE_CPSA_VERIFICATION_EDIT;
	
	private CpsaVerificationService cpsaVerificationService;
	
	private String searchCpsaType;
	private String searchCpsaName;	
	private String searchLetterNo;
	private String searchBranchSubBranch;
	
	private Date searchUploadDate;
	private Date searchLetterDate;
	private Date searchPeriodStartFrom;
	private Date searchPeriodStartTo;
	private Date searchPeriodEndFrom;
	private Date searchPeriodEndTo;
	
	private int paging;
	
	private List<SelectItem> cpsaTypeList;
	
	List<String> filesListInDir = new ArrayList<String>();
	
	private DBLazyDataModel<CpsaVerificationVo> cpsaTableModel;
	
	private FacesUtil facesUtil;
	
	private SimpleDateFormat sdfDateSearch = new SimpleDateFormat("yyyy-MM-dd");
	
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
		paging = Constants.DEFAULT_PAGING_NUMBER;
		cpsaTableModel = new DBLazyDataModel<CpsaVerificationVo>(cpsaVerificationService, paging);
	}
	
	private void initComponent() {
		initCpsaTypeList();
	}
	
	private void initCpsaTypeList() {
		try {
			cpsaTypeList = new ArrayList<SelectItem>();
			
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
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent event) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();		
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
		if (searchBranchSubBranch != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_BRANCH_SUB_BRANCH, searchBranchSubBranch));
		}
		
		cpsaTableModel.setSearchCriteria(searchCriteria);		
		PrimeFaces.current().executeScript("reInitSelect2();");
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
		searchBranchSubBranch = null;
		
		search(event);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
		
	public CpsaVerificationService getCpsaVerificationService() {
		return cpsaVerificationService;
	}

	public void setCpsaVerificationService(CpsaVerificationService cpsaVerificationService) {
		this.cpsaVerificationService = cpsaVerificationService;
	}

	public DBLazyDataModel<CpsaVerificationVo> getCpsaTableModel() {
		return cpsaTableModel;
	}

	public void setCpsaTableModel(DBLazyDataModel<CpsaVerificationVo> cpsaTableModel) {
		this.cpsaTableModel = cpsaTableModel;
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

	public Date getSearchUploadDate() {
		return searchUploadDate;
	}

	public void setSearchUploadDate(Date searchUploadDate) {
		this.searchUploadDate = searchUploadDate;
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

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}

	public SimpleDateFormat getSdfDateSearch() {
		return sdfDateSearch;
	}

	public void setSdfDateSearch(SimpleDateFormat sdfDateSearch) {
		this.sdfDateSearch = sdfDateSearch;
	}

	public String getSearchLetterNo() {
		return searchLetterNo;
	}

	public void setSearchLetterNo(String searchLetterNo) {
		this.searchLetterNo = searchLetterNo;
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

	public List<String> getFilesListInDir() {
		return filesListInDir;
	}

	public void setFilesListInDir(List<String> filesListInDir) {
		this.filesListInDir = filesListInDir;
	}

	public String getSearchBranchSubBranch() {
		return searchBranchSubBranch;
	}

	public void setSearchBranchSubBranch(String searchBranchSubBranch) {
		this.searchBranchSubBranch = searchBranchSubBranch;
	}
}
