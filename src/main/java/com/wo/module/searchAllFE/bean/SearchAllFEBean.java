package com.wo.module.searchAllFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsaFE.service.CompliancePlanSelfAssessmentFEService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qaFE.service.QAFEService;
import com.wo.module.searchAllFE.constant.SearchAllFEConstants;
import com.wo.module.searchAllFE.service.SearchAllFEService;
import com.wo.module.searchAllFE.vo.SearchAllFEVO;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class SearchAllFEBean extends CommonPagingFEBean<SearchAllFEVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(SearchAllFEBean.class);
	
	private List<SelectItem> categoryList;
	
	private SearchAllFEService  searchAllFEService;
	
	private QAFEService qaFEService;
	
	private CompliancePlanSelfAssessmentFEService compliancePlanSelfAssessmentFEService;
	
	private String navigatePeraturanInternal = SearchAllFEConstants.NAVIGATE_PERATURAN_INTERNAL;
	private String navigatePeraturanEksternal = SearchAllFEConstants.NAVIGATE_PERATURAN_EKSTERNAL;
	private String navigateGroupDiskusi = SearchAllFEConstants.NAVIGATE_DISKUSI_GRUP;
	private String navigateFaq = SearchAllFEConstants.NAVIGATE_FAQ;
	private String navigateQa = SearchAllFEConstants.NAVIGATE_QA;
	private String navigateNotaris = SearchAllFEConstants.NAVIGATE_NOTARIS;
	private String navigateCpsa = SearchAllFEConstants.NAVIGATE_CPSA;
	private String navigateKantorHukum = SearchAllFEConstants.NAVIGATE_KANTOR_HUKUM;
	private String navigateArtikel = SearchAllFEConstants.NAVIGATE_ARTKEL;
	private String navigateOpini = SearchAllFEConstants.NAVIGATE_OPINI;
	private String navigateTugasSaya = SearchAllFEConstants.NAVIGATE_TUGAS_SAYA;
	
	private String categoryAdmin;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

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
		String searchAll = facesUtil.retrieveRequestParam("SEARCH_VAL");

		if(searchAll!=null){
			setSearchVal(searchAll.replace(":and", "&").replace(":percent", "%"));
		}
		
		System.out.println("searchVal=="+getSearchVal());
		searchData();
		
		categoryAdmin = qaFEService.getCategoryIsAdmin(facesUtil.getUserLogin().getUserId());
		
		categoryList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("SEARCH_CATEGORY");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				categoryList.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toEncrypt(Long id){
		try {
			return Constants.encryptString(id.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String checkFCCType(String no, String searchVal) {
		
		if(no.equalsIgnoreCase("Country")) 
			return "/pages/countryFE/countryFE?SEARCH_VAL="+searchVal;
		else if(no.equalsIgnoreCase("Economy Sector")) 
			return "/pages/economySectorFE/economySectorFE?SEARCH_VAL="+searchVal;
		else if(no.equalsIgnoreCase("Occupation")) 
			return "/pages/occupationFE/occupationFE?SEARCH_VAL="+searchVal;
		else
			return "/pages/searchAllFE/searchAllFE";
	}
	
	public StreamedContent downloadCPSAExcel() {
		StreamedContent downloadExcelSc = null;
		try {
			String cpsaId = facesUtil.retrieveRequestParam("cpsaId");
			User userLogin = getUserService().getUserByNik(facesUtil.retrieveUserLogin());
			Long userId1 = userLogin.getUserId();
			downloadExcelSc = compliancePlanSelfAssessmentFEService.generateDataExcel(Long.valueOf(cpsaId), userId1);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return downloadExcelSc;
	}
	
	@Override
	public List<SearchAllFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return searchAllFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return searchAllFEService.searchCountData(getSearchCriteria());
	}
	

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		SearchAllFEBean.logger = logger;
	}

	public SearchAllFEService getSearchAllFEService() {
		return searchAllFEService;
	}

	public void setSearchAllFEService(SearchAllFEService searchAllFEService) {
		this.searchAllFEService = searchAllFEService;
	}

	public String getNavigatePeraturanInternal() {
		return navigatePeraturanInternal;
	}

	public void setNavigatePeraturanInternal(String navigatePeraturanInternal) {
		this.navigatePeraturanInternal = navigatePeraturanInternal;
	}

	public String getNavigatePeraturanEksternal() {
		return navigatePeraturanEksternal;
	}

	public void setNavigatePeraturanEksternal(String navigatePeraturanEksternal) {
		this.navigatePeraturanEksternal = navigatePeraturanEksternal;
	}

	public String getNavigateGroupDiskusi() {
		return navigateGroupDiskusi;
	}

	public void setNavigateGroupDiskusi(String navigateGroupDiskusi) {
		this.navigateGroupDiskusi = navigateGroupDiskusi;
	}

	public String getNavigateFaq() {
		return navigateFaq;
	}

	public void setNavigateFaq(String navigateFaq) {
		this.navigateFaq = navigateFaq;
	}

	public String getNavigateQa() {
		return navigateQa;
	}

	public void setNavigateQa(String navigateQa) {
		this.navigateQa = navigateQa;
	}

	public String getNavigateNotaris() {
		return navigateNotaris;
	}

	public void setNavigateNotaris(String navigateNotaris) {
		this.navigateNotaris = navigateNotaris;
	}

	public String getNavigateCpsa() {
		return navigateCpsa;
	}

	public void setNavigateCpsa(String navigateCpsa) {
		this.navigateCpsa = navigateCpsa;
	}

	public String getNavigateKantorHukum() {
		return navigateKantorHukum;
	}

	public void setNavigateKantorHukum(String navigateKantorHukum) {
		this.navigateKantorHukum = navigateKantorHukum;
	}

	public String getNavigateArtikel() {
		return navigateArtikel;
	}

	public void setNavigateArtikel(String navigateArtikel) {
		this.navigateArtikel = navigateArtikel;
	}

	public String getNavigateOpini() {
		return navigateOpini;
	}

	public void setNavigateOpini(String navigateOpini) {
		this.navigateOpini = navigateOpini;
	}

	public String getNavigateTugasSaya() {
		return navigateTugasSaya;
	}

	public void setNavigateTugasSaya(String navigateTugasSaya) {
		this.navigateTugasSaya = navigateTugasSaya;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public CompliancePlanSelfAssessmentFEService getCompliancePlanSelfAssessmentFEService() {
		return compliancePlanSelfAssessmentFEService;
	}

	public void setCompliancePlanSelfAssessmentFEService(
			CompliancePlanSelfAssessmentFEService compliancePlanSelfAssessmentFEService) {
		this.compliancePlanSelfAssessmentFEService = compliancePlanSelfAssessmentFEService;
	}

	public QAFEService getQaFEService() {
		return qaFEService;
	}

	public void setQaFEService(QAFEService qaFEService) {
		this.qaFEService = qaFEService;
	}

	public String getCategoryAdmin() {
		return categoryAdmin;
	}

	public void setCategoryAdmin(String categoryAdmin) {
		this.categoryAdmin = categoryAdmin;
	}

	

	

	

	
	
	

}