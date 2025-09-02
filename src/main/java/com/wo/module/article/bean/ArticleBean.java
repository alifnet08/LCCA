package com.wo.module.article.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.article.model.Article;
import com.wo.module.article.model.TmpArticle;
import com.wo.module.article.service.ArticleService;
import com.wo.module.articleFE.service.ArticleFEService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ArticleBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ArticleBean.class);

	private String keyword;
	
	private Date publishDateFrom;
	
	private Date publishDateTo;

	private int paging;
	
	private String divisionNameLogin;

	private ArticleService articleService;
	
	private ArticleFEService articleFEService;
	
	private UserService userService;
	
	private List<TmpArticle> articleList;

	private DBLazyDataModel<TmpArticle> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = ArticleConstants.NAVIGATE_EDIT;

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
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<TmpArticle>(articleService, paging);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		User userLogin = facesUtil.getUserLogin();
		this.divisionNameLogin = userLogin.getDivisionName();
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (userLogin != null && userLogin.getDivisionName()!=null) {
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_DIVISION_NAME, divisionNameLogin));
		}

		tableModel.setSearchCriteria(searchCriteria);
		
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (keyword != null && !keyword.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_KEYWORD, keyword));
		}
		if (publishDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_PUBLISH_DATE_FROM, sdf.format(publishDateFrom)));
		}
		if (publishDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_PUBLISH_DATE_TO, sdf.format(publishDateTo)));
		}
		if (divisionNameLogin != null) {
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_DIVISION_NAME, divisionNameLogin));
		}

		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}

	public void reset(ActionEvent actionEvent) {
		keyword = "";
		
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
			TmpArticle entity = articleService.findById(deleteId);
			entity.setEnabledFlag(Constants.CONSTANT_YES);
			entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
			entity.setLastUpdateDate(new Timestamp(new Date().getTime()));

			Article article = articleFEService.findById(deleteId);
			if(article!=null && article.getArticleId()!=null){
				entity.setArticleTitleIn(entity.getArticleTitleIn().concat(" (deleted)"));
				ParameterDetail pdNewStatus = parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_NEW);
				entity.setStatus(pdNewStatus);
			}
			articleService.update(entity);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	

	public ArticleService getArticleService() {
		return articleService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public DBLazyDataModel<TmpArticle> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TmpArticle> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}


	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<TmpArticle> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<TmpArticle> articleList) {
		this.articleList = articleList;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getPublishDateFrom() {
		return publishDateFrom;
	}

	public void setPublishDateFrom(Date publishDateFrom) {
		this.publishDateFrom = publishDateFrom;
	}

	public Date getPublishDateTo() {
		return publishDateTo;
	}

	public void setPublishDateTo(Date publishDateTo) {
		this.publishDateTo = publishDateTo;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getDivisionNameLogin() {
		return divisionNameLogin;
	}

	public void setDivisionNameLogin(String divisionNameLogin) {
		this.divisionNameLogin = divisionNameLogin;
	}

	public ArticleFEService getArticleFEService() {
		return articleFEService;
	}

	public void setArticleFEService(ArticleFEService articleFEService) {
		this.articleFEService = articleFEService;
	}

	
	

	

	
	

}