package com.wo.module.articleFE.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.wo.module.articleFE.constant.ArticleFEConstants;
import com.wo.module.articleFE.service.ArticleFEService;
import com.wo.module.articleFE.vo.ArticleFEVO;
import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class ArticleFEBean extends CommonPagingFEBean<ArticleFEVO>  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ArticleFEBean.class);
	
	private List<SelectItem> articleTypeList;

	private ArticleFEService articleFEService;

	private String navigateView = ArticleFEConstants.NAVIGATE_VIEW;

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
		Object data = facesUtil.getSessionAttribute(com.wo.module.common.constant.Constants.SESSION_PAGE_SELECTED);
		
		if (facesUtil.getSessionAttribute("FIRST_ARTICLE_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_ARTICLE_FE");
			setInitFirst((Integer) dataInt);
		}
		
		initSelectArticleTypeList();
		searchData();
		
		if (facesUtil.getSessionAttribute("FIRST_ARTICLE_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_ARTICLE_FE");
		}
	}
	
	private void initSelectArticleTypeList() {
		articleTypeList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listCaseType = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_ARTICLE_TYPE);

			for (ParameterDetail vo : listCaseType) {
				if (vo.getParameterDtlCode().toUpperCase().contains("OPINION"))
					continue;
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				articleTypeList.add(si);
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
	public List<ArticleFEVO> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return articleFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return articleFEService.searchCountData(getSearchCriteria());
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ArticleFEBean.logger = logger;
	}

	public ArticleFEService getArticleFEService() {
		return articleFEService;
	}

	public void setArticleFEService(ArticleFEService articleFEService) {
		this.articleFEService = articleFEService;
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

	public List<SelectItem> getArticleTypeList() {
		return articleTypeList;
	}

	public void setArticleTypeList(List<SelectItem> articleTypeList) {
		this.articleTypeList = articleTypeList;
	}
	
	
}