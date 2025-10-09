package com.wo.module.user.bean;

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
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.constant.UserConstants;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class UserEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(UserEditBean.class);

	private User user;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private UserService userService;
	private ResponsibilityService responsibilityService;

	public FacesUtil facesUtil;

	private String navigateSearch = UserConstants.NAVIGATE_SEARCH;

	private List<SelectItem> roleList;
	private List<SelectItem> statusList;

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
		statusList = new ArrayList<SelectItem>();		
		statusList.add(new SelectItem(Constants.CONSTANT_YES, "Active"));
		statusList.add(new SelectItem(Constants.CONSTANT_NO, "Inactive"));
		
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
		user = new User();
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
		user = userService.findById(idLong);
	}

	public Boolean validate() {
		Boolean flag = false;
		/*if (user.getResponsibilityId() == null || StringUtils.isEmpty(user.getResponsibilityId().toString())) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formUserRole") + " " + facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}*/
		
		if (user.getStatus() == null || StringUtils.isEmpty(user.getStatus())) {
			facesUtil.addErrMessage(
					facesUtil.retrieveMessage("formUserStatus") + " " + facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				if (user.getUserId() != null) {

					user.setLastUpdateBy(facesUtil.retrieveUserLogin());
					user.setLastUpdateDate(new Timestamp(new Date().getTime()));
					user.setDelId(new Long(0));
					user.setEnabledFlag(Constants.CONSTANT_YES);
					userService.update(user);
				} else {

					user.setCreatedBy(facesUtil.retrieveUserLogin());
					user.setCreationDate(new Timestamp(new Date().getTime()));
					user.setDelId(new Long(0));
					user.setEnabledFlag(Constants.CONSTANT_YES);
					userService.save(user);
				}

				facesUtil.redirect("/pages/user/user.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/user/user.faces");
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
	
	public ResponsibilityService getResponsibilityService() {
		return responsibilityService;
	}

	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

	public List<SelectItem> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SelectItem> roleList) {
		this.roleList = roleList;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

}