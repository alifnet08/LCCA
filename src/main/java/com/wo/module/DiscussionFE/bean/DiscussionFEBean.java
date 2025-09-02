package com.wo.module.DiscussionFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.wo.module.DiscussionFE.constant.DiscussionFEConstant;
import com.wo.module.DiscussionFE.service.DiscussionFEService;
import com.wo.module.DiscussionFE.vo.DiscussionFEVo;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;

public class DiscussionFEBean extends CommonPagingFEBean<DiscussionFEVo> implements Serializable{

	private static final long serialVersionUID = 3958511738542880006L;
	private static final Logger logger = Logger.getLogger(DiscussionFEBean.class);
	private static final String NAVIGATE_EDIT = DiscussionFEConstant.NAVIGATE_DISCUSSION_EDIT;
	
	private DiscussionFEService discussionFEService;
	
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
		if (facesUtil.getSessionAttribute("FIRST_DISC_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_DISC_FE");
			setInitFirst((Integer) dataInt);
		}
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_DISC_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_DISC_FE");
		}
	}
	
	public String toEncrypt(Long divisionId){
		try {
			return Constants.encryptString(divisionId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<DiscussionFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return discussionFEService.searchData(searchCriteria, first, pageSize, null, null);
	}
	
	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return discussionFEService.searchCountData(getSearchCriteria());
	}
	public DiscussionFEService getDiscussionFEService() {
		return discussionFEService;
	}

	public void setDiscussionFEService(DiscussionFEService discussionFEService) {
		this.discussionFEService = discussionFEService;
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
	
}
