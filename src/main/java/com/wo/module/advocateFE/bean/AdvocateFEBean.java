package com.wo.module.advocateFE.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.wo.module.advocateFE.constant.AdvocateFEConstants;
import com.wo.module.advocateFE.service.AdvocateFEService;
import com.wo.module.advocateFE.vo.AdvocateFEVO;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;

public class AdvocateFEBean extends CommonPagingFEBean<AdvocateFEVO>  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(AdvocateFEBean.class);

	private AdvocateFEService advocateFEService;

	private String navigateEdit = AdvocateFEConstants.NAVIGATE_VIEW;

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
		//setPageSize(6);
		if (facesUtil.getSessionAttribute("FIRST_ADVOCATE_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_ADVOCATE_FE");
			setInitFirst((Integer) dataInt);
		}
		searchData();
		
		if (facesUtil.getSessionAttribute("FIRST_ADVOCATE_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_ADVOCATE_FE");
		}
	}
	
	public String toEncrypt(Long qaid){
		try {
			return Constants.encryptString(qaid.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public List<AdvocateFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return advocateFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		return advocateFEService.searchCountData(getSearchCriteria());
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AdvocateFEBean.logger = logger;
	}

	public AdvocateFEService getAdvocateFEService() {
		return advocateFEService;
	}

	public void setAdvocateFEService(AdvocateFEService advocateFEService) {
		this.advocateFEService = advocateFEService;
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