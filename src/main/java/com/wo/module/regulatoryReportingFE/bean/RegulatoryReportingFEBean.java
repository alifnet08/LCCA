package com.wo.module.regulatoryReportingFE.bean;

import java.io.Serializable;
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
import com.wo.module.regulatoryReportingFE.constant.RegulatoryReportingFEConstant;
import com.wo.module.regulatoryReportingFE.service.RegulatoryReportingFEService;
import com.wo.module.regulatoryReportingFE.vo.RegulatoryReportingFEVo;

public class RegulatoryReportingFEBean extends CommonPagingFEBean<RegulatoryReportingFEVo> implements Serializable{

	private static final long serialVersionUID = -5768722191571792437L;
	private static final Logger logger = Logger.getLogger(RegulatoryReportingFEBean.class);
	private static final String NAVIGATE_EDIT = RegulatoryReportingFEConstant.NAVIGATE_REGULATORY_REPORTING_FE_EDIT;
	private static final String NAVIGATE_VIEW = RegulatoryReportingFEConstant.NAVIGATE_REGULATORY_REPORTING_FE_VIEW;

	private RegulatoryReportingFEService regulatoryReportingFEService;
	
	private List<SelectItem> statusLists;
	
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
		if (facesUtil.getSessionAttribute("FIRST_REG_REPORT_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_REG_REPORT_FE");
			setInitFirst((Integer) dataInt);
		}
		setSearchStatus(ParameterDetail.PARAM_DET_PIC_INPROGRESS); 
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_REG_REPORT_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_REG_REPORT_FE");
		}
	}

	private void initComponent() {
		initStatusList();
	}
	
	private void initStatusList() {
		statusLists = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_PIC_FOLLOWUP_STATUS);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusLists.add(si);
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
	
	@SuppressWarnings("rawtypes")
	public List<RegulatoryReportingFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return regulatoryReportingFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes") 
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return regulatoryReportingFEService.searchCountData(getSearchCriteria());
	}
	
	public RegulatoryReportingFEService getRegulatoryReportingFEService() {
		return regulatoryReportingFEService;
	}

	public void setRegulatoryReportingFEService(RegulatoryReportingFEService regulatoryReportingFEService) {
		this.regulatoryReportingFEService = regulatoryReportingFEService;
	}
	
	public List<SelectItem> getStatusLists() {
		return statusLists;
	}

	public void setStatusLists(List<SelectItem> statusLists) {
		this.statusLists = statusLists;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}

	public static String getNavigateView() {
		return NAVIGATE_VIEW;
	}

	
}
