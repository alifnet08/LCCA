package com.wo.module.correspondenceFE.bean;

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
import com.wo.module.correspondenceFE.constant.CorrespondenceFEConstants;
import com.wo.module.correspondenceFE.service.CorrespondenceFEService;
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;
import com.wo.module.parameter.model.ParameterDetail;

public class CorrespondenceFEBean extends CommonPagingFEBean<CorrespondenceFEVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CorrespondenceFEBean.class);
	
	private List<SelectItem> statusList;
	
	private CorrespondenceFEService  correspondenceFEService;
	
	private String navigateEdit = CorrespondenceFEConstants.NAVIGATE_EDIT;
	private String navigateVerify = CorrespondenceFEConstants.NAVIGATE_VERIFY;
	private String navigateView = CorrespondenceFEConstants.NAVIGATE_VIEW;
	
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
		
		if (facesUtil.getSessionAttribute("FIRST_CORRES_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_CORRES_FE");
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
		if (facesUtil.getSessionAttribute("FIRST_CORRES_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_CORRES_FE");
		}
	}
	
	public String toEncrypt(Long correspondenceId){
		try {
			return Constants.encryptString(correspondenceId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public List<CorrespondenceFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return correspondenceFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return correspondenceFEService.searchCountData(getSearchCriteria());
	}
	

	public CorrespondenceFEService getCorrespondenceFEService() {
		return correspondenceFEService;
	}

	public void setCorrespondenceFEService(CorrespondenceFEService correspondenceFEService) {
		this.correspondenceFEService = correspondenceFEService;
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

	public String getNavigateVerify() {
		return navigateVerify;
	}

	public void setNavigateVerify(String navigateVerify) {
		this.navigateVerify = navigateVerify;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getNavigateView() {
		return navigateView;
	}

	public void setNavigateView(String navigateView) {
		this.navigateView = navigateView;
	}

	

	

	

	
	
	

}