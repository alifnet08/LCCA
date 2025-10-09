package com.wo.module.regulationMonitoringFE.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.SortOrder;
import org.springframework.util.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regulationMonitoringFE.constants.RegulationMonitoringFEConstants;
import com.wo.module.regulationMonitoringFE.service.RegulationMonitoringFEService;
import com.wo.module.regulationMonitoringFE.vo.RegulationMonitoringFEVO;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class RegulationMonitoringFEBean extends CommonPagingFEBean<RegulationMonitoringFEVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegulationMonitoringFEBean.class);
	
	private List<SelectItem> statusList;
	
	private RegulationMonitoringFEService  regulationMonitoringFEService;
	
	private String navigateEdit = RegulationMonitoringFEConstants.NAVIGATE_EDIT;

	private String navigateView = RegulationMonitoringFEConstants.NAVIGATE_VIEW;
	
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
		
		if (facesUtil.getSessionAttribute("FIRST_REG_MONITOR_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_REG_MONITOR_FE");
			setInitFirst((Integer) dataInt);
		}
		
		setSearchStatus(ParameterDetail.PARAM_DET_PIC_INPROGRESS); 
		searchData();
		
		statusList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("PIC_FOLLOWUP_STATUS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (facesUtil.getSessionAttribute("FIRST_REG_MONITOR_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_REG_MONITOR_FE");
		}
	}
	
	public String toEncrypt(Long regMonitoringPicFpId){
		try {
			return Constants.encryptString(regMonitoringPicFpId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public List<RegulationMonitoringFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return regulationMonitoringFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return regulationMonitoringFEService.searchCountData(getSearchCriteria());
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegulationMonitoringFEBean.logger = logger;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public RegulationMonitoringFEService getRegulationMonitoringFEService() {
		return regulationMonitoringFEService;
	}

	public void setRegulationMonitoringFEService(RegulationMonitoringFEService regulationMonitoringFEService) {
		this.regulationMonitoringFEService = regulationMonitoringFEService;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public String getNavigateView() {
		return navigateView;
	}

	public void setNavigateView(String navigateView) {
		this.navigateView = navigateView;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}