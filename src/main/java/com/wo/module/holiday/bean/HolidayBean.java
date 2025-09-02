package com.wo.module.holiday.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
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
import com.wo.module.holiday.constant.HolidayConstants;
import com.wo.module.holiday.model.Holiday;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.FacesUtil;

public class HolidayBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HolidayBean.class);
	
	private String holidaySearch;
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private HolidayService holidayService;
	
	private List<Holiday> holidayList;
	
	private DBLazyDataModel<Holiday> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateEdit = HolidayConstants.NAVIGATE_EDIT;
    
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
    	tableModel =  new DBLazyDataModel<Holiday>(holidayService, paging);
    	
	}
	
    public void search(ActionEvent actionEvent) {
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}
    
    public void reset(ActionEvent actionEvent) {
    	searchVal = "";
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
	}

    public void delete(Long deleteId) {
    	try {
		Holiday dt = holidayService.findById(deleteId);
		dt.setEnabledFlag(Constants.CONSTANT_NO);
		dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
		dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
    	holidayService.update(dt);
		facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
    	}catch(Exception e) {
    		facesUtil.addFacesMsg(
                    FacesMessage.SEVERITY_ERROR, 
                    null, 
                    "Operation Failed : " + e.getMessage(), "");
    	}
	}
    
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if(facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null 
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null )
			return true;
		else
			return false;
	}

	public String getHolidaySearch() {
		return holidaySearch;
	}

	public void setHolidaySearch(String holidaySearch) {
		this.holidaySearch = holidaySearch;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public List<Holiday> getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(List<Holiday> holidayList) {
		this.holidayList = holidayList;
	}

	public DBLazyDataModel<Holiday> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Holiday> tableModel) {
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

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}
	
	

	
   
}