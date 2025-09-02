package com.wo.module.socializationFE.bean;

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
import com.wo.module.socializationFE.constant.SocializationFEConstant;
import com.wo.module.socializationFE.service.SocializationFEService;
import com.wo.module.socializationFE.vo.SocializationFEVo;

public class SocializationFEBean extends CommonPagingFEBean<SocializationFEVo> implements Serializable{

	private static final long serialVersionUID = 5810406903368462132L;
	private static final Logger logger = Logger.getLogger(SocializationFEBean.class);
	private static final String NAVIGATE_EDIT = SocializationFEConstant.NAVIGATE_SOCIALIZATION_FE_EDIT;
	private static final String NAVIGATE_VIEW = SocializationFEConstant.NAVIGATE_SOCIALIZATION_FE_VIEW;
	
	private SocializationFEService socializationFEService;
	
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
		if (facesUtil.getSessionAttribute("FIRST_SOCIAL_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_SOCIAL_FE");
			setInitFirst((Integer) dataInt);
		}
		initComponent();
		setSearchStatus(ParameterDetail.PARAM_DET_PIC_INPROGRESS); 
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_SOCIAL_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_SOCIAL_FE");
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
	
	public String toEncrypt(Long trcSocializationFollowupId){
		try {
			return Constants.encryptString(trcSocializationFollowupId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("rawtypes") 
	public List<SocializationFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return socializationFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		return socializationFEService.searchCountData(getSearchCriteria());
	}
	
	public SocializationFEService getSocializationFEService() {
		return socializationFEService;
	}

	public void setSocializationFEService(SocializationFEService socializationFEService) {
		this.socializationFEService = socializationFEService;
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
