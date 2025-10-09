package com.wo.module.complianceTestingFE.bean;

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
import com.wo.module.complianceTestingFE.constant.ComplianceTestingFEConstants;
import com.wo.module.complianceTestingFE.service.ComplianceTestingFEService;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEDtlVO;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEVO;
import com.wo.module.parameter.model.ParameterDetail;

public class ComplianceTestingFEBean extends CommonPagingFEBean<ComplianceTestingFEDtlVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ComplianceTestingFEBean.class);

	private List<SelectItem> statusList;
	
	private ComplianceTestingFEService  complianceTestingFEService;

	private String navigateEdit = ComplianceTestingFEConstants.NAVIGATE_EDIT;
	private String navigateView = ComplianceTestingFEConstants.NAVIGATE_VIEW;

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
		if (facesUtil.getSessionAttribute("FIRST_COMP_TEST_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_COMP_TEST_FE");
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
		if (facesUtil.getSessionAttribute("FIRST_COMP_TEST_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_COMP_TEST_FE");
		}
	}
	
	public String toEncrypt(Long complianceTestingPICFollowupId){
		try {
			return Constants.encryptString(complianceTestingPICFollowupId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public List<ComplianceTestingFEDtlVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return complianceTestingFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		return complianceTestingFEService.searchCountData(getSearchCriteria());
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ComplianceTestingFEBean.logger = logger;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public ComplianceTestingFEService getComplianceTestingFEService() {
		return complianceTestingFEService;
	}

	public void setComplianceTestingFEService(ComplianceTestingFEService complianceTestingFEService) {
		this.complianceTestingFEService = complianceTestingFEService;
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