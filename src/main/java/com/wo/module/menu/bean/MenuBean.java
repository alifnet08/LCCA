package com.wo.module.menu.bean;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.wo.module.menu.constant.MenuConstants;
import com.wo.module.menu.model.Menu;
import com.wo.module.menu.service.MenuService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;

public class MenuBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(MenuBean.class);

	
	private String searchName;
	

	private int paging;

	private MenuService menuService;
	private ResponsibilityService responsibilityService;

	private List<Menu> menuList;

	private DBLazyDataModel<Menu> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = MenuConstants.NAVIGATE_EDIT;

	private List<SelectItem> roleList;

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
		tableModel = new DBLazyDataModel<Menu>(menuService, paging);
		initResponsibility();
	}

	public void initResponsibility() {
		roleList = new ArrayList<SelectItem>();
		try {
			List<Responsibility> listRepsonsibility = responsibilityService.getAllResponsibility();
			for (int i = 0; i < listRepsonsibility.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Responsibility) listRepsonsibility.get(i)).getName());
				si.setValue(((Responsibility) listRepsonsibility.get(i)).getResponsibilityId());
				roleList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		
		
		if (searchName != null && !searchName.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(MenuConstants.SEARCH_BY_NAME, searchName));
		}
		
		
		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}
	
	public void reset(ActionEvent actionEvent) {
		searchName = "";
		search(actionEvent);
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

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public DBLazyDataModel<Menu> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Menu> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<SelectItem> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SelectItem> roleList) {
		this.roleList = roleList;
	}

	public ResponsibilityService getResponsibilityService() {
		return responsibilityService;
	}

	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

}