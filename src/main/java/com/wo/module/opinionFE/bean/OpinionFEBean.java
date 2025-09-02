package com.wo.module.opinionFE.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.wo.module.opinionFE.constant.OpinionFEConstants;
import com.wo.module.opinionFE.service.OpinionFEService;
import com.wo.module.opinionFE.vo.OpinionFEVO;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;

public class OpinionFEBean extends CommonPagingFEBean<OpinionFEVO>  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(OpinionFEBean.class);

	private List<SelectItem> opinionTypeList;
	
	private OpinionFEService opinionFEService;

	private String navigateView = OpinionFEConstants.NAVIGATE_VIEW;

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
		if (facesUtil.getSessionAttribute("FIRST_OPINION_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_OPINION_FE");
			setInitFirst((Integer) dataInt);
		}
		
		initSelectOpinionTypeList();
		searchData();
		if (facesUtil.getSessionAttribute("FIRST_OPINION_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_OPINION_FE");
		}
	}
	
	private void initSelectOpinionTypeList() {
		opinionTypeList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listCaseType = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_ARTICLE_TYPE);

			for (ParameterDetail vo : listCaseType) {
				if (vo.getParameterDtlCode().toUpperCase().contains("OPINION"))
				{
					SelectItem si = new SelectItem();
					si.setLabel(vo.getName());
					si.setValue(vo.getParameterDtlCode());
					opinionTypeList.add(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toEncrypt(Long articleId){
		try {
			return Constants.encryptString(articleId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public List<OpinionFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return opinionFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return opinionFEService.searchCountData(getSearchCriteria());
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		OpinionFEBean.logger = logger;
	}

	public OpinionFEService getOpinionFEService() {
		return opinionFEService;
	}

	public void setOpinionFEService(OpinionFEService opinionFEService) {
		this.opinionFEService = opinionFEService;
	}

	public String getNavigateView() {
		return navigateView;
	}

	public void setNavigateView(String navigateView) {
		this.navigateView = navigateView;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getOpinionTypeList() {
		return opinionTypeList;
	}

	public void setOpinionTypeList(List<SelectItem> opinionTypeList) {
		this.opinionTypeList = opinionTypeList;
	}
}