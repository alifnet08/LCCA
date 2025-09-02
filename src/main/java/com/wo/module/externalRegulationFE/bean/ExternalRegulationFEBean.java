package com.wo.module.externalRegulationFE.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.SortOrder;
import org.springframework.util.StringUtils;

import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulationFE.constants.ExternalRegulationFEConstants;
import com.wo.module.externalRegulationFE.service.ExternalRegulationFEService;
import com.wo.module.externalRegulationFE.vo.ExternalRegulationFEVO;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;

public class ExternalRegulationFEBean extends CommonPagingFEBean<ExternalRegulationFEVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ExternalRegulationFEBean.class);
	
	private List<SelectItem> statusList;
	
	private List<SelectItem> tipePeraturanList;

	private ExternalRegulationFEService externalRegulationFEService;
	
	private DocumentTypeService documentTypeService;
	
	private String navigateEdit = ExternalRegulationFEConstants.NAVIGATE_EDIT;

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
		
		if (facesUtil.getSessionAttribute("FIRST_EXTER_REGULATION_FE") != null) {
			Object data = facesUtil.getSessionAttribute("FIRST_EXTER_REGULATION_FE");
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
							ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)), 0, Integer.MAX_VALUE,
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
		
		Long docType = facesUtil.retrieveRequestParam("DOC_TYPE") != null ?
				Long.parseLong(facesUtil.retrieveRequestParam("DOC_TYPE")) : null;
		if(!StringUtils.isEmpty(docType)){
			setDocType(docType);
		}
		
		setSearchStatus("DATA_ACTIVE");
		
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_EXTER_REGULATION_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_EXTER_REGULATION_FE");
		}
	}
	
	public String toEncrypt(Long regulationId){
		try {
			return Constants.encryptString(regulationId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public List<ExternalRegulationFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return externalRegulationFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return externalRegulationFEService.searchCountData(getSearchCriteria());
	}
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ExternalRegulationFEBean.logger = logger;
	}

	public ExternalRegulationFEService getExternalRegulationFEService() {
		return externalRegulationFEService;
	}

	public void setExternalRegulationFEService(ExternalRegulationFEService externalRegulationFEService) {
		this.externalRegulationFEService = externalRegulationFEService;
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

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
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
	
	
}