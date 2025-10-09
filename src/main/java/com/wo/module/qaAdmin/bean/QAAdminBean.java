package com.wo.module.qaAdmin.bean;

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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qaAdmin.constant.QAAdminConstants;
import com.wo.module.qaAdmin.service.QAAdminService;
import com.wo.module.qaAdmin.vo.QAAdminVo;
import com.wo.module.user.service.UserService;

public class QAAdminBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(QAAdminBean.class);

	private String searchKeyword;
	private String searchTitle;
	private String searchStatus;
	private String searchTicketNo;
	private Date searchCreateFrom;
	private Date searchCreateTo;

	private int paging;

	private String searchVal;
	
	private String status;

	private Long deleteId;

	private QAAdminService qaAdminService;
	private UserService userService;

	private List<QA> qaList;
	
	private List<SelectItem> statusList;

	private DBLazyDataModel<QAAdminVo> qAdmintableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = QAAdminConstants.NAVIGATE_EDIT;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void init() {
		super.init();
		selectStatus();
		//this.setSearchStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_NEW);
		paging = Constants.DEFAULT_PAGING_NUMBER;
		qAdmintableModel = new DBLazyDataModel<QAAdminVo>(qaAdminService, paging);
		
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (facesUtil.getUserLogin().getUserId() != null) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_USER_LOGIN, Long.toString(facesUtil.getUserLogin().getUserId())));
		}
		if (searchStatus != null && !searchStatus.equals("") && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_STATUS, searchStatus));
		}
		qAdmintableModel.setSearchCriteria(searchCriteria);
		
	}
	
	public void selectStatus() {
	    	statusList = new ArrayList<SelectItem>();
	    	try {
				List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_STATUS);
				for(int i=0;i<pd.size();i++) {
					SelectItem si = new SelectItem();
					si.setLabel(((ParameterDetail)pd.get(i)).getName());
					si.setValue(((ParameterDetail)pd.get(i)).getParameterDtlCode());
					statusList.add(si);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdfDateSearch = new SimpleDateFormat("yyyy-mm-dd");
		
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchKeyword != null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_KEYWORD, searchKeyword));
		}
		if (searchStatus != null && !searchStatus.equals("") && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_STATUS, searchStatus));
		}
		if (searchTicketNo != null && !searchTicketNo.equals("") && !searchTicketNo.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_TICKET_NO, searchTicketNo));
		}
		if (searchCreateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_CREATED_DATE_FROM, sdfDateSearch.format(searchCreateFrom)));
		}
		if (searchCreateTo != null) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_CREATED_DATE_TO, sdfDateSearch.format(searchCreateTo)));
		}
		if (searchTitle != null) {
			searchCriteria.add(new DefaultSearchObject(QAAdminConstants.SEARCH_BY_TITLE, searchTitle));
		}
		
		qAdmintableModel.setSearchCriteria(searchCriteria);
	}

	public void reset(ActionEvent actionEvent) {
		searchTitle = "";
		searchVal = "";
		searchKeyword = "";
		searchStatus = null;
		searchTicketNo = "";
		searchCreateFrom = null;
		searchCreateTo = null;
		status = QAConstants.QNA_STATUS_NEW;
//		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(QAConstants.WHERE_QUESTION, searchVal),new DefaultSearchObject(QAConstants.WHERE_STATUS, QAConstants.QNA_STATUS_NEW)));
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {

			QA dt = qaAdminService.findById(deleteId);
			dt.setEnabledFlag(Constants.CONSTANT_NO);
			dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
			dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
			qaAdminService.update(dt);
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		QAAdminBean.logger = logger;
	}
	
	public QAAdminService getQaAdminService() {
		return qaAdminService;
	}

	public void setQaAdminService(QAAdminService qaAdminService) {
		this.qaAdminService = qaAdminService;
	}

	public List<QA> getQaList() {
		return qaList;
	}

	public void setQaList(List<QA> qaList) {
		this.qaList = qaList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public String getSearchTicketNo() {
		return searchTicketNo;
	}

	public void setSearchTicketNo(String searchTicketNo) {
		this.searchTicketNo = searchTicketNo;
	}

	public Date getSearchCreateFrom() {
		return searchCreateFrom;
	}

	public void setSearchCreateFrom(Date searchCreateFrom) {
		this.searchCreateFrom = searchCreateFrom;
	}

	public Date getSearchCreateTo() {
		return searchCreateTo;
	}

	public void setSearchCreateTo(Date searchCreateTo) {
		this.searchCreateTo = searchCreateTo;
	}

	public DBLazyDataModel<QAAdminVo> getqAdmintableModel() {
		return qAdmintableModel;
	}

	public void setqAdmintableModel(DBLazyDataModel<QAAdminVo> qAdmintableModel) {
		this.qAdmintableModel = qAdmintableModel;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}
	
	

}