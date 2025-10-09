package com.wo.module.auditFEMockup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.SortOrder;
import org.springframework.util.StringUtils;

import com.wo.module.auditFE.constant.AuditFEConstants;
import com.wo.module.auditFE.service.AuditFEService;
import com.wo.module.auditFE.vo.AuditFEVO;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class AuditFEBean extends CommonPagingFEBean<AuditFEVO> implements Serializable, AuditFEConstants {

	private static final long serialVersionUID = -4473116966298105934L;

	static Logger logger = Logger.getLogger(AuditFEBean.class);;

	private List<SelectItem> statusList;
	
	private String navigateEdit = NAVIGATE_EDIT;
	private String navigateView = NAVIGATE_VIEW;

	private AuditFEService auditFEService;
	
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
		if (facesUtil.getSessionAttribute("FIRST_AUDIT_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_AUDIT_FE");
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
		
		if (facesUtil.getSessionAttribute("FIRST_AUDIT_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_AUDIT_FE");
		}
	}
	
	@Override
	public List<AuditFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return auditFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return auditFEService.searchCountData(getSearchCriteria());
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AuditFEBean.logger = logger;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
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

	public AuditFEService getAuditFEService() {
		return auditFEService;
	}

	public void setAuditFEService(AuditFEService auditFEService) {
		this.auditFEService = auditFEService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}