package com.wo.module.qaFE.bean;

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
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.qaFE.constant.QAFEConstant;
import com.wo.module.qaFE.service.QAFEService;
import com.wo.module.qaFE.vo.QAFEVo;

public class QAFEBean extends CommonPagingFEBean<QAFEVo> implements Serializable{

	private static final long serialVersionUID = 2147803816717566101L;
	private static final Logger logger = Logger.getLogger(QAFEVo.class);
	private static final String NAVIGATE_EDIT = QAFEConstant.NAVIGATE_QA_FE_EDIT;
	private static final String NAVIGATE_MENJAWAB = QAFEConstant.NAVIGATE_QA_FE_MENJAWAB;
	private static final String NAVIGATE_BERTANYA = QAFEConstant.NAVIGATE_QA_FE_BERTANYA;
	
	private QAFEService qafeService;

	private Long isAdmin;
	
	private List<SelectItem> categoryTypeLists;
	
	private List<SelectItem> statusLists;
	
	private String categoryAdmin;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
	private String qnaWarning;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {
		super.init();
		if (facesUtil.getSessionAttribute("FIRST_QA_FE") != null) {
			Object data = facesUtil.getSessionAttribute("FIRST_QA_FE");
			setInitFirst((Integer) data);
		}
		initComponent();
		
		Number admin = qafeService.getIsAdmin(facesUtil.getUserLogin().getUserId());
		
		if (admin == null) {
			isAdmin = new Long(0);
		} else {
			isAdmin = admin.longValue();
		}
		
		if(isAdmin>0){
			setSearchCategory(qafeService.getCategoryIsAdmin(facesUtil.getUserLogin().getUserId()));
			setCategoryAdmin(getSearchCategory());
			//setSearchStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_NEW);
		} else {
			try {
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_QNA_WARNING_TEXT);
				qnaWarning =  pd.getNameIn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		searchData();
		
		if (facesUtil.getSessionAttribute("FIRST_QA_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_QA_FE");
		}
	}
	
	public String toEncrypt(Long qaid){
		try {
			return Constants.encryptString(qaid.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String toEncryptEdit(Long qaid){
		try {
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_HOST_NAME_APPLICATION);
			return pd.getNameIn().concat("pages/qaFE/qaFEEdit?token=").concat(Constants.encryptString(qaid.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String toEncryptMenjawab(Long qaid){
		try {
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_HOST_NAME_APPLICATION);
			return pd.getNameIn().concat("pages/qaFE/qaFEAnswer?token=").concat(Constants.encryptString(qaid.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	
	
	private void initComponent() {
		initSelectCategoryTypeList();
		initSelectStatusList();
	}
	
	private void initSelectCategoryTypeList() {
		categoryTypeLists = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_CATEGORY);
			for (ParameterDetail vo : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				categoryTypeLists.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initSelectStatusList() {
		statusLists = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_STATUS);
			for (ParameterDetail vo : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				statusLists.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<QAFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return qafeService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return qafeService.searchCountData(getSearchCriteria());
	}
	
	public QAFEService getQafeService() {
		return qafeService;
	}

	public void setQafeService(QAFEService qafeService) {
		this.qafeService = qafeService;
	}

	public List<SelectItem> getCategoryTypeLists() {
		return categoryTypeLists;
	}

	public void setCategoryTypeLists(List<SelectItem> categoryTypeLists) {
		this.categoryTypeLists = categoryTypeLists;
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

	public static String getNavigateBertanya() {
		return NAVIGATE_BERTANYA;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}

	public static String getNavigateMenjawab() {
		return NAVIGATE_MENJAWAB;
	}

	public Long getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Long isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<SelectItem> getStatusLists() {
		return statusLists;
	}

	public void setStatusLists(List<SelectItem> statusLists) {
		this.statusLists = statusLists;
	}

	public String getCategoryAdmin() {
		return categoryAdmin;
	}

	public void setCategoryAdmin(String categoryAdmin) {
		this.categoryAdmin = categoryAdmin;
	}

	public String getQnaWarning() {
		return qnaWarning;
	}

	public void setQnaWarning(String qnaWarning) {
		this.qnaWarning = qnaWarning;
	}

	
}
