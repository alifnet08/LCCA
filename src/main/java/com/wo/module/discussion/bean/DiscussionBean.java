package com.wo.module.discussion.bean;

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

import com.wo.module.discussion.constant.DiscussionConstants;
import com.wo.module.discussion.model.Discussion;
import com.wo.module.discussion.service.DiscussionService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class DiscussionBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DiscussionBean.class);

	private String keyword;
	
	private String status;
	
	private String type;
	
	private Date dateFrom;
	
	private Date dateTo;

	private int paging;

	private DiscussionService discussionService;
	
	private List<Discussion> discussionList;
	
	private List<SelectItem> threadTypeList;
	
	private List<SelectItem> statusList;

	private DBLazyDataModel<Discussion> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = DiscussionConstants.NAVIGATE_EDIT;

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
		initList();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<Discussion>(discussionService, paging);
	}
	
	public void initList(){
		try {
		threadTypeList = new ArrayList<SelectItem>();
		List<ParameterDetail> listDiscussionType = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);
		

		for (ParameterDetail vo : listDiscussionType) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			threadTypeList.add(si);
		}
		
		statusList = new ArrayList<SelectItem>();
		List<ParameterDetail> listStatus = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);
		

		for (ParameterDetail vo : listStatus) {
			if (!(vo.getParameterDtlCode().equals("COMPLIANCE_NOT_APPROPRIATE") 
					|| vo.getParameterDtlCode().equals("COMPLIANCE_APPROPRIATE"))) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			statusList.add(si);
			}
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
			searchCriteria.add(new DefaultSearchObject(DiscussionConstants.SEARCH_BY_KEYWORD, keyword));
		}
		if (status != null && !status.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(DiscussionConstants.SEARCH_BY_STATUS, status));
		}
		if (type != null && !type.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(DiscussionConstants.SEARCH_BY_TYPE, type));
		}
		if (dateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(DiscussionConstants.SEARCH_BY_CREATED_DATE_FROM, sdf.format(dateFrom)));
		}
		if (dateTo != null) {
			searchCriteria.add(new DefaultSearchObject(DiscussionConstants.SEARCH_BY_CREATED_DATE_FROM, sdf.format(dateTo)));
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
			
				Discussion entity = discussionService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				discussionService.update(entity);
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

	

	public DiscussionService getDiscussionService() {
		return discussionService;
	}

	public void setDiscussionService(DiscussionService discussionService) {
		this.discussionService = discussionService;
	}

	public DBLazyDataModel<Discussion> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Discussion> tableModel) {
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

	public List<Discussion> getDiscussionList() {
		return discussionList;
	}

	public void setDiscussionList(List<Discussion> discussionList) {
		this.discussionList = discussionList;
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
		DiscussionBean.logger = logger;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getThreadTypeList() {
		return threadTypeList;
	}

	public void setThreadTypeList(List<SelectItem> threadTypeList) {
		this.threadTypeList = threadTypeList;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	

	
	

}