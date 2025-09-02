package com.wo.module.notaryFE.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.model.SortOrder;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.notaryFE.constant.NotaryFEConstant;
import com.wo.module.notaryFE.service.NotaryFEService;
import com.wo.module.notaryFE.vo.NotaryFEVo;
import com.wo.module.parameter.model.ParameterDetail;

public class NotaryFEBean extends CommonPagingFEBean<NotaryFEVo>{

	private static final long serialVersionUID = 6764104293241963522L;

	private NotaryFEService notaryFEService;
	
	private List<SelectItem> categoryList;
	
	private String navigateEdit = NotaryFEConstant.NAVIGATE_EDIT;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public String toEncrypt(Long notaryId){
		try {
			return Constants.encryptString(notaryId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@PostConstruct
	public void init() {
		super.init();
		//setPageSize(6);
		if (facesUtil.getSessionAttribute("FIRST_NOTARY_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_NOTARY_FE");
			setInitFirst((Integer) dataInt);
		}
		categoryList = new ArrayList<SelectItem>();
		
		try {
			List<ParameterDetail> catList = parameterDetailService.getParameterDetailByParamCode("NOTARY_CATEGORY");
			for (int i = 0; i < catList.size(); i++) {
				ParameterDetail data = catList.get(i);
				SelectItem si = new SelectItem();
				si.setLabel(data.getName());
				si.setValue(data.getParameterDtlCode());
				
				categoryList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_NOTARY_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_NOTARY_FE");
		}
	}
	
	@Override
	public List<NotaryFEVo> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return notaryFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return notaryFEService.searchCountData(getSearchCriteria());
	}

	
	public NotaryFEService getNotaryFEService() {
		return notaryFEService;
	}

	public void setNotaryFEService(NotaryFEService notaryFEService) {
		this.notaryFEService = notaryFEService;
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

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}
}
