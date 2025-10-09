package com.wo.module.rc.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.rc.constant.RCConstants;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;

public class RCBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RCBean.class);

	private String searchVal;

	private int paging;

	private RCService rcService;

	private List<RC> rcList;

	private DBLazyDataModel<RC> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = RCConstants.NAVIGATE_EDIT;

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
		tableModel = new DBLazyDataModel<RC>(rcService, paging);
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchVal != null && !searchVal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(RCConstants.SEARCH_BY_REGION, searchVal));
		}

		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
				RC entity = rcService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				rcService.update(entity);
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

	public RCService getRCService() {
		return rcService;
	}

	public void setRCService(RCService rcService) {
		this.rcService = rcService;
	}

	public List<RC> getRCList() {
		return rcList;
	}

	public void setRCList(List<RC> rcList) {
		this.rcList = rcList;
	}

	public DBLazyDataModel<RC> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<RC> tableModel) {
		this.tableModel = tableModel;
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

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}
	
	

}