package com.wo.module.staticPage.bean;

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
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.wo.module.staticPage.constant.StaticPageConstants;
import com.wo.module.staticPage.model.StaticPage;
import com.wo.module.staticPage.service.StaticPageService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class StaticPageBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(StaticPageBean.class);

	private String category;
	
	private String title;
	
	private String content;

	private int paging;

	private StaticPageService staticPageService;
	
	private List<StaticPage> staticPageList;

	private DBLazyDataModel<StaticPage> tableModel;
	
	private List<SelectItem> categoryList;
	
	private List<SelectItem> statusList;

	public FacesUtil facesUtil;

	private String navigateEdit = StaticPageConstants.NAVIGATE_EDIT;

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
		initList();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<StaticPage>(staticPageService, paging);
		
		String categoryParam = facesUtil.retrieveRequestParam("CATEGORY");
		if(categoryParam!=null){
			category = categoryParam;
		}
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_STATIC_PAGE_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (title != null && !title.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(StaticPageConstants.SEARCH_BY_TITLE, title));
		}
		if (category != null && !category.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(StaticPageConstants.SEARCH_BY_CATEGORY, category));
		}
		if (content != null && !content.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(StaticPageConstants.SEARCH_BY_CONTENT, content));
		}

		tableModel.setSearchCriteria(searchCriteria);
		
	}

	public void reset(ActionEvent actionEvent) {
		title = "";
		category = null;
		content = null;
		
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
				StaticPage entity = staticPageService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				staticPageService.update(entity);
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

	

	public StaticPageService getStaticPageService() {
		return staticPageService;
	}

	public void setStaticPageService(StaticPageService staticPageService) {
		this.staticPageService = staticPageService;
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


	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		StaticPageBean.logger = logger;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<StaticPage> getStaticPageList() {
		return staticPageList;
	}

	public void setStaticPageList(List<StaticPage> staticPageList) {
		this.staticPageList = staticPageList;
	}

	public DBLazyDataModel<StaticPage> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<StaticPage> tableModel) {
		this.tableModel = tableModel;
	}

	

	

	

	
	

}