package com.wo.module.internalRegulationFE.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.internalRegulationFE.constant.InternalRegulationFEConstants;
import com.wo.module.internalRegulationFE.service.InternalRegulationFEService;
import com.wo.module.internalRegulationFE.vo.InternalRegulationFEVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;

@ManagedBean(name="internalRegulationFEBean")
@RequestScoped
public class InternalRegulationFEBean extends CommonPagingFEBean<InternalRegulationFEVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(InternalRegulationFEBean.class);

	private List<SelectItem> statusList;
	
	private List<SelectItem> tipePeraturanList;
	
	private List<SelectItem> tipeCategoryPeraturanList;
	
	private InternalRegulationFEService  internalRegulationFEService;
	
	private DocumentCategoryService documentCategoryService;
	
	private DocumentTypeService documentTypeService;
	
	@ManagedProperty(value ="#{internalRegulationFEViewBean}")
	private InternalRegulationFEViewBean internalRegulationFEViewBean;
	
	private String navigateEdit = InternalRegulationFEConstants.NAVIGATE_EDIT;
	
	private String stringDocType;
	private String stringSearchCategory;
	private String stringSearchStatus;
	private String stringSearchTahun;
	private String stringSearchVal;

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
		
		if (facesUtil.getSessionAttribute("FIRST_INTER_REGULATION_FE") != null) {
			Object data = facesUtil.getSessionAttribute("FIRST_INTER_REGULATION_FE");
			setInitFirst((Integer) data);
		}
		
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
		
		tipePeraturanList = new ArrayList<SelectItem>();
		try {
			
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
							InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)), 0, Integer.MAX_VALUE,
					null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				tipePeraturanList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tipeCategoryPeraturanList = new ArrayList<SelectItem>();
		try {
			List<DocumentCategory> pd = documentCategoryService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE,
							InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)),
					0, Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory) pd.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory) pd.get(i)).getDocumentCategoryId());
				tipeCategoryPeraturanList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(facesUtil.retrieveRequestParam("docType") != null && !facesUtil.retrieveRequestParam("docType").equals("")) {
			//System.out.println(facesUtil.retrieveRequestParam("docType"));
			setDocType(Long.parseLong(facesUtil.retrieveRequestParam("docType")));
		}
		
		if(facesUtil.retrieveRequestParam("docCategory") != null && !facesUtil.retrieveRequestParam("docCategory").equals("")) {
			setDocCategory(Long.parseLong(facesUtil.retrieveRequestParam("docCategory")));
		}
		
		if(facesUtil.retrieveRequestParam("searchStatus") != null) { 
			setSearchStatus(facesUtil.retrieveRequestParam("searchStatus"));
		}
//		else {
//			setSearchStatus("DATA_ACTIVE");
//		}
		
		if(facesUtil.retrieveRequestParam("searchTahun") != null)
			setSearchTahun(facesUtil.retrieveRequestParam("searchTahun"));
		
		if(facesUtil.retrieveRequestParam("searchVal") != null)
			setSearchVal(facesUtil.retrieveRequestParam("searchVal"));
		
		searchData();
		
		if (facesUtil.getSessionAttribute("FIRST_INTER_REGULATION_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_INTER_REGULATION_FE");
		}
		
		
		//Snippet Code: retrieve a session that hold search param - UNUSED UNLESS NEEDED
//		if(facesUtil.getSessionAttribute("SEARCH_PARAM_INTER_REG_FE") != null) {
//			
//			Map searchParamInterRegFE = (Map) facesUtil.getSessionAttribute("searchParamInterRegFE");
//			
//			if(searchParamInterRegFE.containsKey("DOC_TYPE"))
//				setDocType(searchParamInterRegFE.get("DOC_TYPE").toString());
//			if(searchParamInterRegFE.containsKey("SEARCH_STATUS"))
//				setSearchStatus(searchParamInterRegFE.get("SEARCH_STATUS").toString());
//			if(searchParamInterRegFE.containsKey("SEARCH_TAHUN"))
//				setSearchTahun(searchParamInterRegFE.get("SEARCH_TAHUN").toString());
//			if(searchParamInterRegFE.containsKey("SEARCH_VAL"))
//				setSearchVal(searchParamInterRegFE.get("SEARCH_VAL").toString());
//			
//			facesUtil.removeSessionAttribute("SEARCH_PARAM_INTER_REG_FE");
//		}
	}
	
	public String toEncrypt(Long regulationId){
		try {
			//TODO
			return Constants.encryptString(regulationId.toString()); // this return empty string, dunno why
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void saveSearchData() {
		internalRegulationFEViewBean.saveSearchData(stringDocType, stringSearchCategory,
				stringSearchStatus,stringSearchTahun,stringSearchVal);
	}
	
	public String viewRegulation() {
		
		//Snippet Code: using Session as a way to transport search param - UNUSED UNLESS NEEDED
//		try {
//			Map searchParamInterRegFE = new HashMap();
//				
//			if(stringDocType != null)
//				searchParamInterRegFE.put("DOC_TYPE", stringDocType);
//			if(stringSearchStatus != null)
//				searchParamInterRegFE.put("SEARCH_STATUS", stringSearchStatus);
//			if(stringSearchTahun != null)
//				searchParamInterRegFE.put("SEARCH_TAHUN", stringSearchTahun);
//			if(stringSearchVal != null)
//				searchParamInterRegFE.put("SEARCH_VAL", stringSearchVal != null ? stringSearchVal : "");
//			
//			if(searchParamInterRegFE != null)	
//				facesUtil.setSessionAttribute("SEARCH_PARAM_INTER_REG_FE", searchParamInterRegFE);
//			
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
//		internalRegulationFEViewBean.saveSearchData(stringDocType,
//				stringSearchStatus,stringSearchTahun,stringSearchVal);
		
		return getNavigateEdit();
	}
	
	public void onChangeSearchParam() {
		//USED FOR TESTING, REMOVE IF NESSECARY
//		System.out.println("onChangeSearchParam func called");
//		stringDocType = getDocType().toString();
//		stringSearchStatus = getSearchStatus();
//		stringSearchTahun = getSearchTahun();
//		stringSearchVal = getSearchVal();
//		
//		if(stringDocType != null)
//			System.out.printf("stringDocType = "+stringDocType+"\n");
//		if(stringSearchStatus != null)
//			System.out.printf("stringSearchStatus = "+stringSearchStatus+"\n");
//		if(stringSearchTahun != null) 
//			System.out.printf("stringSearchTahun = "+stringSearchTahun+"\n");
//		if(stringSearchVal != null)
//			System.out.printf("stringSearchVal = "+stringSearchVal+"\n");
	}
	
	@Override
	public List<InternalRegulationFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return internalRegulationFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return internalRegulationFEService.searchCountData(getSearchCriteria());
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public InternalRegulationFEService getInternalRegulationFEService() {
		return internalRegulationFEService;
	}

	public void setInternalRegulationFEService(InternalRegulationFEService internalRegulationFEService) {
		this.internalRegulationFEService = internalRegulationFEService;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public List<SelectItem> getTipePeraturanList() {
		return tipePeraturanList;
	}

	public void setTipePeraturanList(List<SelectItem> tipePeraturanList) {
		this.tipePeraturanList = tipePeraturanList;
	}

	public List<SelectItem> getTipeCategoryPeraturanList() {
		return tipeCategoryPeraturanList;
	}

	public void setTipeCategoryPeraturanList(List<SelectItem> tipeCategoryPeraturanList) {
		this.tipeCategoryPeraturanList = tipeCategoryPeraturanList;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public InternalRegulationFEViewBean getInternalRegulationFEViewBean() {
		return internalRegulationFEViewBean;
	}

	public void setInternalRegulationFEViewBean(InternalRegulationFEViewBean internalRegulationFEViewBean) {
		this.internalRegulationFEViewBean = internalRegulationFEViewBean;
	}

	public String getStringDocType() {
		return stringDocType;
	}

	public void setStringDocType(String stringDocType) {
		this.stringDocType = stringDocType;
	}

	public String getStringSearchCategory() {
		return stringSearchCategory;
	}

	public void setStringSearchCategory(String stringSearchCategory) {
		this.stringSearchCategory = stringSearchCategory;
	}

	public String getStringSearchStatus() {
		return stringSearchStatus;
	}

	public void setStringSearchStatus(String stringSearchStatus) {
		this.stringSearchStatus = stringSearchStatus;
	}

	public String getStringSearchTahun() {
		return stringSearchTahun;
	}

	public void setStringSearchTahun(String stringSearchTahun) {
		this.stringSearchTahun = stringSearchTahun;
	}

	public String getStringSearchVal() {
		return stringSearchVal;
	}

	public void setStringSearchVal(String stringSearchVal) {
		this.stringSearchVal = stringSearchVal;
	}
	
	
	
}