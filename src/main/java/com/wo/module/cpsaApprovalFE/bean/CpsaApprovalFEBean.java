package com.wo.module.cpsaApprovalFE.bean;

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
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsaApprovalFE.constant.CpsaApprovalFEConstant;
import com.wo.module.cpsaApprovalFE.service.CpsaApprovalFEService;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalFEVo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class CpsaApprovalFEBean extends CommonPagingFEBean<CpsaApprovalFEVo> implements Serializable{

	
	private static final long serialVersionUID = 210266965886287169L;
	
	private static final Logger logger = Logger.getLogger(CpsaApprovalFEBean.class);
	
	private static final String NAVIGATE_VIEW = CpsaApprovalFEConstant.NAVIGATE_CPSA_APPROVAL_EDIT;
	
	private CpsaApprovalFEService cpsaApprovalFEService;
	
	private List<SelectItem> cpsaTypeList;
	
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
		if (facesUtil.getSessionAttribute("FIRST_CPSA_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_CPSA_FE");
			setInitFirst((Integer) dataInt);
		}
		initComponent();
		searchData();		
		if (facesUtil.getSessionAttribute("FIRST_CPSA_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_CPSA_FE");
		}
	}
	
	private void initComponent() {
		initSelectCpsaTypeList();
	}
	
	private void initSelectCpsaTypeList() {
		cpsaTypeList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> geteCpsaType = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CPSA_TYPE);
			for (ParameterDetail pd : geteCpsaType) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				cpsaTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public StreamedContent getDownloadExcel() {
		StreamedContent downloadExcelSc = null;
		try {
			String cpsaId = facesUtil.retrieveRequestParam("cpsaId");
			String userId1 = facesUtil.retrieveRequestParam("userId1");
			String lineManager = facesUtil.retrieveRequestParam("userId3");
			downloadExcelSc = cpsaApprovalFEService.generateDataExcel(new Long(cpsaId), new Long(userId1), new Long(lineManager));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return downloadExcelSc;
	}
	
	public String toEncrypt(Long cpsaId){
		try {
			return Constants.encryptString(cpsaId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public List<CpsaApprovalFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return cpsaApprovalFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes") 
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return cpsaApprovalFEService.searchCountData(getSearchCriteria());
	}	

	public CpsaApprovalFEService getCpsaApprovalFEService() {
		return cpsaApprovalFEService;
	}

	public void setCpsaApprovalFEService(CpsaApprovalFEService cpsaApprovalFEService) {
		this.cpsaApprovalFEService = cpsaApprovalFEService;
	}

	public List<SelectItem> getCpsaTypeList() {
		return cpsaTypeList;
	}

	public void setCpsaTypeList(List<SelectItem> cpsaTypeList) {
		this.cpsaTypeList = cpsaTypeList;
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
	
}
