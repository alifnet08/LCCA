package com.wo.module.articleApproval.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.articleApproval.constant.ArticleApprovalConstant;
import com.wo.module.articleApproval.service.ArticleApprovalService;
import com.wo.module.articleApproval.vo.ArticleApprovalVo;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.user.model.User;

public class ArticleApprovalBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -1116905657047236560L;
	private static final Logger logger = Logger.getLogger(ArticleApprovalBean.class);
	private static final String NAVIGATE_EDIT = ArticleApprovalConstant.NAVIGATE_ARTICLE_APPROVAL_EDIT;
	
	private ArticleApprovalService articleApprovalService;
	
	private int paging;
	
	private DBLazyDataModel<ArticleApprovalVo> articleApprovalDataModel;
	
	private FacesUtil facesUtil;
	
	private String divisionNameLogin;
	
	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		articleApprovalDataModel = new DBLazyDataModel<ArticleApprovalVo>(articleApprovalService, paging);
		
		User userLogin = facesUtil.getUserLogin();
		this.divisionNameLogin = userLogin.getDivisionName();
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (userLogin != null && userLogin.getDivisionName()!=null) {
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_DIVISION_NAME, divisionNameLogin));
		}
		articleApprovalDataModel.setSearchCriteria(searchCriteria);
	}

	public ArticleApprovalService getArticleApprovalService() {
		return articleApprovalService;
	}

	public void setArticleApprovalService(ArticleApprovalService articleApprovalService) {
		this.articleApprovalService = articleApprovalService;
	}

	public DBLazyDataModel<ArticleApprovalVo> getArticleApprovalDataModel() {
		return articleApprovalDataModel;
	}

	public void setArticleApprovalDataModel(DBLazyDataModel<ArticleApprovalVo> articleApprovalDataModel) {
		this.articleApprovalDataModel = articleApprovalDataModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getDivisionNameLogin() {
		return divisionNameLogin;
	}

	public void setDivisionNameLogin(String divisionNameLogin) {
		this.divisionNameLogin = divisionNameLogin;
	}

	
	
}
