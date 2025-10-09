package com.wo.module.templateViewFE.bean;

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

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.templateViewFE.constant.TemplateViewFEConstant;
import com.wo.module.templateViewFE.service.TemplateViewFEService;
import com.wo.module.templateViewFE.vo.TemplateViewFEVo;

public class TemplateViewFEBean extends CommonPagingFEBean<TemplateViewFEVo> implements Serializable{
	
	private static final long serialVersionUID = -9000588778333420715L;
	private static final Logger logger = Logger.getLogger(TemplateViewFEBean.class);
	private static final String NAVIGATE_EDIT = TemplateViewFEConstant.NAVIGATE_TEMPLATE_VIEW_FE_EDIT;

	private TemplateViewFEService templateViewFEService;
	
	private List<SelectItem> templateCatLists;
	
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
		if (facesUtil.getSessionAttribute("FIRST_TEMPLATE_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_TEMPLATE_FE");
			setInitFirst((Integer) dataInt);
		}
		initComponent();
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_TEMPLATE_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_TEMPLATE_FE");
		}
	}
	
	private void initComponent() {
		initSelectTemplateCatLists();
	}

	private void initSelectTemplateCatLists() {
		templateCatLists = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_TEMPLATE_CATEGORY);
		
		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			templateCatLists.add(si);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toEncrypt(Long templateId){
		try {
			return Constants.encryptString(templateId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<TemplateViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return templateViewFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes") 
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return templateViewFEService.searchCountData(getSearchCriteria());
	}
	
	public TemplateViewFEService getTemplateViewFEService() {
		return templateViewFEService;
	}

	public void setTemplateViewFEService(TemplateViewFEService templateViewFEService) {
		this.templateViewFEService = templateViewFEService;
	}

	public List<SelectItem> getTemplateCatLists() {
		return templateCatLists;
	}

	public void setTemplateCatLists(List<SelectItem> templateCatLists) {
		this.templateCatLists = templateCatLists;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}
	
}
