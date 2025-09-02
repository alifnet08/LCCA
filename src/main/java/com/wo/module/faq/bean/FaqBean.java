package com.wo.module.faq.bean;

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

import com.wo.module.faq.constant.FaqConstants;
import com.wo.module.faq.model.Faq;
import com.wo.module.faq.model.TmpFaq;
import com.wo.module.faq.service.FaqService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class FaqBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(FaqBean.class);

	private String keyword;
	
	private String category;
	
	private String status;
	
	private Date startDate;
	
	private Date endDate;

	private int paging;

	private FaqService faqService;
	
	private UserService userService;
	
	private List<Faq> faqList;

	private DBLazyDataModel<TmpFaq> tableModel;
	
	private List<SelectItem> categoryList;
	
	private List<SelectItem> statusList;

	public FacesUtil facesUtil;

	private String navigateEdit = FaqConstants.NAVIGATE_EDIT;

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
		tableModel = new DBLazyDataModel<TmpFaq>(faqService, paging);
		User userLogin = facesUtil.getUserLogin();
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}

		tableModel.setSearchCriteria(searchCriteria);
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		statusList = new ArrayList<SelectItem>();
		List<ParameterDetail> listStatus = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_STATUS);
		

		for (ParameterDetail vo : listStatus) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			statusList.add(si);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (keyword != null && !keyword.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(FaqConstants.SEARCH_BY_KEYWORD, keyword));
		}
		if (category != null && !category.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(FaqConstants.SEARCH_BY_CATEGORY, category));
		}
		if (startDate != null) {
			searchCriteria.add(new DefaultSearchObject(FaqConstants.SEARCH_BY_START_DATE, sdf.format(startDate)));
		}
		if (endDate != null) {
			searchCriteria.add(new DefaultSearchObject(FaqConstants.SEARCH_BY_END_DATE, sdf.format(endDate)));
		}
		User userLogin = facesUtil.getUserLogin();
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}

		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}

	public void reset(ActionEvent actionEvent) {
		keyword = "";
		startDate = null;
		endDate = null;
		category = null;
		
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
				TmpFaq entity = faqService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				faqService.update(entity);
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

	

	public FaqService getFaqService() {
		return faqService;
	}

	public void setFaqService(FaqService faqService) {
		this.faqService = faqService;
	}

	public DBLazyDataModel<TmpFaq> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TmpFaq> tableModel) {
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

	public List<Faq> getFaqList() {
		return faqList;
	}

	public void setFaqList(List<Faq> faqList) {
		this.faqList = faqList;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		FaqBean.logger = logger;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	
	

	

	
	

}