package com.wo.module.litigationView.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.litigation.constant.LitigationConstants;
import com.wo.module.litigationView.constant.LitigationViewConstant;
import com.wo.module.litigationView.service.LitigationViewService;
import com.wo.module.litigationView.vo.LitigationViewVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class LitigationViewBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 2786909546005022939L;
	private static final Logger logger = Logger.getLogger(LitigationViewBean.class);
	private static final String NAVIGATE_VIEW = LitigationViewConstant.NAVIGATE_VIEW;
	
	private LitigationViewService litigationViewService;
	
	private String searchCaseType;
	private String searchCaseTypeDtlPerdata;
	private String searchCaseTypeDtlPidana;
	private String searchLitigationNo;
	private String searchDivNameOrBranchOffice;
	private String searchSegment;
	private String searchDataStatus;
	private String searchDebtor;
	private String searchCaseNumber;
	private String searchReportNumber;
	
	private String caseType;
	private String debitur;
	private String litigationNo;
	private String searchProgress;
	private String searchPutusan;
	private String searchUpayaHukum;

	private int paging;
	
	private List<SelectItem> caseTypeList;
	private List<SelectItem> dataStatusList;
	
	private DBLazyDataModel<LitigationViewVo> litigationViewTableModel;
	
	private FacesUtil facesUtil;
	
	public void addMesssage(String summary) {
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
		litigationViewTableModel = new DBLazyDataModel<>(getLitigationViewService(), paging);
		
		searchDataStatus = CommonConstants.RECORD_FLAG_YES;
		search();
	}
	
	private void initComponent() {
		initCaseType();
	}
	
	private void initCaseType() {
		caseTypeList = new ArrayList<>();
		dataStatusList = new ArrayList<>();
		try {
			List<ParameterDetail> getJenisPerkaraList = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE);

			for (ParameterDetail pd : getJenisPerkaraList) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				caseTypeList.add(si);
			}
			
			SelectItem siYes = new SelectItem();
			siYes.setLabel("Aktif");
			siYes.setValue(CommonConstants.RECORD_FLAG_YES);
			dataStatusList.add(siYes);
			
			SelectItem siNo = new SelectItem();
			siNo.setLabel("Tidak Aktif");
			siNo.setValue(CommonConstants.RECORD_FLAG_NO);
			dataStatusList.add(siNo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void search() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchCaseType != null && !searchCaseType.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_JENIS_PERKARA, searchCaseType));
		}
		if (searchLitigationNo != null && !searchLitigationNo.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_NO_PERKARA, searchLitigationNo));
		}
		if(searchDivNameOrBranchOffice != null && !searchDivNameOrBranchOffice.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_DIV_NAME_OR_BRANCH_OFFICE, searchDivNameOrBranchOffice));
		}
		if(searchSegment != null && !searchSegment.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_SEGMENT, searchSegment));
		}
		if(searchDataStatus != null && !searchDataStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_ACTIVE_STATUS, searchDataStatus));
		}
		if(searchDebtor != null && !searchDebtor.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_DEBTOR, searchDebtor));
		}
		if(searchCaseNumber != null && !searchCaseNumber.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_CASE_NUMBER, searchCaseNumber));
		}
		if(searchReportNumber != null && !searchReportNumber.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_REPORT_NUMBER, searchReportNumber));
		}

		if (searchPutusan != null && !searchPutusan.isEmpty() && !searchPutusan.equals("")) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_PUTUSAN, searchPutusan));
		}
		if (searchUpayaHukum != null && !searchUpayaHukum.isEmpty() && !searchUpayaHukum.equals("")) {
			searchCriteria.add(new DefaultSearchObject(LitigationConstants.SEARCH_BY_UPAYA_HUKUM, searchUpayaHukum));
		}

		litigationViewTableModel.setSearchCriteria(searchCriteria);

	}
	
	public void reset(ActionEvent event) {
		searchCaseType = "";
		searchCaseTypeDtlPerdata = "";
		searchCaseTypeDtlPidana = "";
		searchLitigationNo = "";
		searchDivNameOrBranchOffice = "";
		searchSegment = "";
		searchDataStatus = CommonConstants.RECORD_FLAG_YES;
		searchDebtor = "";
		searchCaseNumber = "";
		searchReportNumber = "";
		
		caseType = "";
		debitur = null;
		litigationNo = null;
		searchProgress = "";
		searchPutusan = null;
		searchUpayaHukum = null;

		search();

		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public LitigationViewService getLitigationViewService() {
		return litigationViewService;
	}

	public void setLitigationViewService(LitigationViewService litigationViewService) {
		this.litigationViewService = litigationViewService;
	}

	public String getSearchProgress() {
		return searchProgress;
	}

	public void setSearchProgress(String searchProgress) {
		this.searchProgress = searchProgress;
	}

	public String getSearchPutusan() {
		return searchPutusan;
	}

	public void setSearchPutusan(String searchPutusan) {
		this.searchPutusan = searchPutusan;
	}

	public DBLazyDataModel<LitigationViewVo> getLitigationViewTableModel() {
		return litigationViewTableModel;
	}

	public void setLitigationViewTableModel(DBLazyDataModel<LitigationViewVo> litigationViewTableModel) {
		this.litigationViewTableModel = litigationViewTableModel;
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

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getSearchUpayaHukum() {
		return searchUpayaHukum;
	}

	public void setSearchUpayaHukum(String searchUpayaHukum) {
		this.searchUpayaHukum = searchUpayaHukum;
	}

	public String getSearchCaseType() {
		return searchCaseType;
	}

	public void setSearchCaseType(String searchCaseType) {
		this.searchCaseType = searchCaseType;
	}

	public String getSearchCaseTypeDtlPerdata() {
		return searchCaseTypeDtlPerdata;
	}

	public void setSearchCaseTypeDtlPerdata(String searchCaseTypeDtlPerdata) {
		this.searchCaseTypeDtlPerdata = searchCaseTypeDtlPerdata;
	}

	public String getSearchCaseTypeDtlPidana() {
		return searchCaseTypeDtlPidana;
	}

	public void setSearchCaseTypeDtlPidana(String searchCaseTypeDtlPidana) {
		this.searchCaseTypeDtlPidana = searchCaseTypeDtlPidana;
	}

	public String getSearchLitigationNo() {
		return searchLitigationNo;
	}

	public void setSearchLitigationNo(String searchLitigationNo) {
		this.searchLitigationNo = searchLitigationNo;
	}

	public String getSearchDivNameOrBranchOffice() {
		return searchDivNameOrBranchOffice;
	}

	public void setSearchDivNameOrBranchOffice(String searchDivNameOrBranchOffice) {
		this.searchDivNameOrBranchOffice = searchDivNameOrBranchOffice;
	}

	public String getSearchSegment() {
		return searchSegment;
	}

	public void setSearchSegment(String searchSegment) {
		this.searchSegment = searchSegment;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getDebitur() {
		return debitur;
	}

	public void setDebitur(String debitur) {
		this.debitur = debitur;
	}

	public String getLitigationNo() {
		return litigationNo;
	}

	public void setLitigationNo(String litigationNo) {
		this.litigationNo = litigationNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getNavigateView() {
		return NAVIGATE_VIEW;
	}

	public List<SelectItem> getCaseTypeList() {
		return caseTypeList;
	}

	public void setCaseTypeList(List<SelectItem> caseTypeList) {
		this.caseTypeList = caseTypeList;
	}

	public String getSearchDataStatus() {
		return searchDataStatus;
	}

	public void setSearchDataStatus(String searchDataStatus) {
		this.searchDataStatus = searchDataStatus;
	}

	public List<SelectItem> getDataStatusList() {
		return dataStatusList;
	}

	public void setDataStatusList(List<SelectItem> dataStatusList) {
		this.dataStatusList = dataStatusList;
	}

	public String getSearchDebtor() {
		return searchDebtor;
	}

	public void setSearchDebtor(String searchDebtor) {
		this.searchDebtor = searchDebtor;
	}

	public String getSearchCaseNumber() {
		return searchCaseNumber;
	}

	public void setSearchCaseNumber(String searchCaseNumber) {
		this.searchCaseNumber = searchCaseNumber;
	}

	public String getSearchReportNumber() {
		return searchReportNumber;
	}

	public void setSearchReportNumber(String searchReportNumber) {
		this.searchReportNumber = searchReportNumber;
	}
	
	
	
	
	
}
