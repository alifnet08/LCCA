package com.wo.module.menu.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.menu.constant.MenuConstants;
import com.wo.module.menu.model.Menu;
import com.wo.module.menu.service.MenuService;

public class MenuEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(MenuEditBean.class);

	private Menu menu;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private List<SelectItem> parentList;

	private MenuService menuService;
	

	public FacesUtil facesUtil;

	private String navigateSearch = MenuConstants.NAVIGATE_SEARCH;

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
		checkNewOrEdit();
	}

	public void initList() {
		try {
			parentList = new ArrayList<SelectItem>();
			List<Menu> listMenu = menuService.getAllParentMenuList();
			
			for (Menu vo : listMenu) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getMenuName());
				si.setValue(vo.getMenuId());
				parentList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
		
		String viewId = facesUtil.retrieveRequestParam("viewId");
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty()) {
			if (viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	private void handleNew() {
		menu = new Menu();
		actionMode = Constants.ACTION_ADD;
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		menu = menuService.findById(idLong);
	}

	public Boolean validate() {
		Boolean flag = false;
		
		if (menu.getNameIn() == null || StringUtils.isEmpty(menu.getNameIn())) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formMenuMenuName") + " in " + facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		/*if (menu.getNameEn() == null || StringUtils.isEmpty(menu.getNameEn())) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formMenuMenuName") + " en " + facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}*/
		
		if (menu.getDescription() == null || StringUtils.isEmpty(menu.getDescription())) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formMenuDescription") + " " + facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (menu.getMenuLevel().longValue() == 2) {
			if (menu.getParentId() == null || StringUtils.isEmpty(menu.getParentId().toString())) {
				facesUtil.addErrMessage(
						facesUtil.retrieveMessage("formMenuParentMenu") + " " + facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		} else if (menu.getMenuLevel().longValue() == 1) {
			if (menu.getParentId() != null ) {
				facesUtil.addErrMessage(
						facesUtil.retrieveMessage("formMenuParentMenu") + " " + facesUtil.retrieveMessage("validateNotRequired"));
				flag = true;
			}
		}		
		
		

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				if (menu.getMenuId() != null) {

					menu.setLastUpdateBy(facesUtil.retrieveUserLogin());
					menu.setLastUpdateDate(new Timestamp(new Date().getTime()));
					menu.setDelId(new Long(0));
					menu.setEnabledFlag(Constants.CONSTANT_YES);
					menuService.update(menu);
				} else {

					menu.setCreatedBy(facesUtil.retrieveUserLogin());
					menu.setCreationDate(new Timestamp(new Date().getTime()));
					menu.setDelId(new Long(0));
					menu.setEnabledFlag(Constants.CONSTANT_YES);
					menuService.save(menu);
				}

				facesUtil.redirect("/pages/menu/menu.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/menu/menu.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
	}

	public List<SelectItem> getParentList() {
		return parentList;
	}

	public void setParentList(List<SelectItem> parentList) {
		this.parentList = parentList;
	}

	

}