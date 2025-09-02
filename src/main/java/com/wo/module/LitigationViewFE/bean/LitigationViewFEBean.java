package com.wo.module.LitigationViewFE.bean;

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

import com.wo.module.LitigationViewFE.constant.LitigationViewFEConstant;
import com.wo.module.LitigationViewFE.service.LitigationViewFEService;
import com.wo.module.LitigationViewFE.vo.LitigationViewFEVo;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class LitigationViewFEBean extends CommonPagingFEBean<LitigationViewFEVo> implements Serializable{

	private static final long serialVersionUID = -4698420043912343643L;
	private static final Logger logger = Logger.getLogger(LitigationViewFEBean.class);
	private static final String NAVIGATE_VIEW = LitigationViewFEConstant.NAVIGATE_LITIGATION_VIEW_FE_VIEW;
	
	private LitigationViewFEService litigationViewFEService;
	
	private List<SelectItem> jenisPerkaraList;
	
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
		setPageSize(20);
		if (facesUtil.getSessionAttribute("FIRST_LITIG_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_LITIG_FE");
			setInitFirst((Integer) dataInt);
		}
//		initComponent();
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_LITIG_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_LITIG_FE");
		}
	}

	private void initComponent() {
		initSelectJenisPerkaraList();
	}
	
	private void initSelectJenisPerkaraList() {
		jenisPerkaraList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listCaseType = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE);

			for (ParameterDetail vo : listCaseType) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				jenisPerkaraList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toEncrypt(Long litigationId){
		try {
			return Constants.encryptString(litigationId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<LitigationViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return litigationViewFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes") 
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return litigationViewFEService.searchCountData(getSearchCriteria());
	}
	
	public LitigationViewFEService getLitigationViewFEService() {
		return litigationViewFEService;
	}

	public void setLitigationViewFEService(LitigationViewFEService litigationViewFEService) {
		this.litigationViewFEService = litigationViewFEService;
	}

	
	public List<SelectItem> getJenisPerkaraList() {
		return jenisPerkaraList;
	}

	public void setJenisPerkaraList(List<SelectItem> jenisPerkaraList) {
		this.jenisPerkaraList = jenisPerkaraList;
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

	public static String getNavigateView() {
		return NAVIGATE_VIEW;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

}
