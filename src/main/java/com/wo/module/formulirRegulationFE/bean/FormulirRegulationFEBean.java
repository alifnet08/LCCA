package com.wo.module.formulirRegulationFE.bean;

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
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.formulirRegulationFE.constant.FormulirRegulationFEConstants;
import com.wo.module.formulirRegulationFE.service.FormulirRegulationFEService;
import com.wo.module.formulirRegulationFE.vo.FormulirRegulationFEVO;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;

@ManagedBean(name="formulirRegulationFEBean")
@RequestScoped
public class FormulirRegulationFEBean extends CommonPagingFEBean<FormulirRegulationFEVO> implements Serializable {

	private static final long serialVersionUID = 3154960314246150910L;

	static Logger logger = Logger.getLogger(FormulirRegulationFEBean.class);

	private List<SelectItem> statusList;
		
	private List<SelectItem> tipeCategoryPeraturanList;
	
	private List<SelectItem> directorateList;
	
	private FormulirRegulationFEService  formulirRegulationFEService;
	
	private DocumentCategoryService documentCategoryService;
	
	private DocumentTypeService documentTypeService;
	
	@ManagedProperty(value ="#{formulirRegulationFEViewBean}")
	private FormulirRegulationFEViewBean formulirRegulationFEViewBean;
	
	private String navigateEdit = FormulirRegulationFEConstants.NAVIGATE_EDIT;
	
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
		
		if (facesUtil.getSessionAttribute("FIRST_FORMULIR_REGULATION_FE") != null) {
			Object data = facesUtil.getSessionAttribute("FIRST_FORMULIR_REGULATION_FE");
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
		
		directorateList = new ArrayList<SelectItem>();
		try {
			List<SelectItem> dataString = formulirRegulationFEService.getDataDirectorateList();
			for (int i = 0; i < dataString.size(); i++) {
				SelectItem si = (SelectItem) dataString.get(i);
				directorateList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		if(facesUtil.retrieveRequestParam("docType") != null && !facesUtil.retrieveRequestParam("docType").equals("")) {
			setDocType(Long.parseLong(facesUtil.retrieveRequestParam("docType")));
		}
		
		if(facesUtil.retrieveRequestParam("docCategory") != null && !facesUtil.retrieveRequestParam("docCategory").equals("")) {
			setDocCategory(Long.parseLong(facesUtil.retrieveRequestParam("docCategory")));
		}
		
		if(facesUtil.retrieveRequestParam("directorateName") != null && 
				!facesUtil.retrieveRequestParam("directorateName").equals("")) {
			setDirectorateName(facesUtil.retrieveRequestParam("directorateName"));
		}
		
		if(facesUtil.retrieveRequestParam("searchStatus") != null) { 
			setSearchStatus(facesUtil.retrieveRequestParam("searchStatus"));
		}
		
		if(facesUtil.retrieveRequestParam("searchTahun") != null)
			setSearchTahun(facesUtil.retrieveRequestParam("searchTahun"));
		
		if(facesUtil.retrieveRequestParam("searchVal") != null)
			setSearchVal(facesUtil.retrieveRequestParam("searchVal"));
		
		searchData();
		
		if (facesUtil.getSessionAttribute("FIRST_FORMULIR_REGULATION_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_FORMULIR_REGULATION_FE");
		}
	}
	
	public String toEncrypt(Long regulationId){
		try {
			return Constants.encryptString(regulationId.toString()); // this return empty string, dunno why
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void saveSearchData() {
		formulirRegulationFEViewBean.saveSearchData(stringDocType, stringSearchCategory,
				stringSearchStatus,stringSearchTahun,stringSearchVal);
	}
	
	public String viewRegulation() {
		return getNavigateEdit();
	}
	
	public void onChangeSearchParam() {
	}
	
	@Override
	public List<FormulirRegulationFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return formulirRegulationFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		return formulirRegulationFEService.searchCountData(getSearchCriteria());
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		FormulirRegulationFEBean.logger = logger;
	}

	public FormulirRegulationFEService getFormulirRegulationFEService() {
		return formulirRegulationFEService;
	}

	public void setFormulirRegulationFEService(FormulirRegulationFEService formulirRegulationFEService) {
		this.formulirRegulationFEService = formulirRegulationFEService;
	}

	public FormulirRegulationFEViewBean getFormulirRegulationFEViewBean() {
		return formulirRegulationFEViewBean;
	}

	public void setFormulirRegulationFEViewBean(FormulirRegulationFEViewBean formulirRegulationFEViewBean) {
		this.formulirRegulationFEViewBean = formulirRegulationFEViewBean;
	}

	public List<SelectItem> getDirectorateList() {
		return directorateList;
	}

	public void setDirectorateList(List<SelectItem> directorateList) {
		this.directorateList = directorateList;
	}
	
}