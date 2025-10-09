package com.wo.module.fineFE.bean;

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
import com.wo.module.fineFE.constant.FineFEConstant;
import com.wo.module.fineFE.service.FineFEService;
import com.wo.module.fineFE.vo.FineFEVo;
import com.wo.module.parameter.model.ParameterDetail;

public class FineFEBean extends CommonPagingFEBean<FineFEVo> implements Serializable{

	private static final long serialVersionUID = 5143762214442742878L;
	private static final Logger logger = Logger.getLogger(FineFEBean.class);
	private static final String NAVIGATE_EDIT = FineFEConstant.NAVIGATE_FINE_FE_EDIT;
	private static final String NAVIGATE_VIEW = FineFEConstant.NAVIGATE_FINE_FE_VIEW;
	
	private FineFEService fineFEService;
	private List<SelectItem> statusLists;
	
	
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
		
		if (facesUtil.getSessionAttribute("FIRST_FINE_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_FINE_FE");
			setInitFirst((Integer) dataInt);
		}
		
		initComponent();
		
		
		setSearchStatus(ParameterDetail.PARAM_DET_PIC_INPROGRESS); 
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_FINE_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_FINE_FE");
		}
	}
	
	private void initComponent() {
		initSelectStatusList();
	}
	
	private void initSelectStatusList() {
		statusLists = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("PIC_FOLLOWUP_STATUS");
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
	
	public String toEncrypt(Long finePicFollowupId){
		try {
			return Constants.encryptString(finePicFollowupId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public List<FineFEVo> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return fineFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return fineFEService.searchCountData(getSearchCriteria());
	}
	
	public FineFEService getFineFEService() {
		return fineFEService;
	}

	public void setFineFEService(FineFEService fineFEService) {
		this.fineFEService = fineFEService;
	}

	
	public List<SelectItem> getStatusLists() {
		return statusLists;
	}

	public void setStatusLists(List<SelectItem> statusLists) {
		this.statusLists = statusLists;
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

	public static String getNavigateView() {
		return NAVIGATE_VIEW;
	}
	
}
