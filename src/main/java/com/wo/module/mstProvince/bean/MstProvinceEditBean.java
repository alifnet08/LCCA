package com.wo.module.mstProvince.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstProvince.constants.MstProvinceConstants;
import com.wo.module.mstProvince.model.MstProvince;
import com.wo.module.mstProvince.service.MstProvinceService;

public class MstProvinceEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = -7040725697581633681L;
	
	private static Logger logger = Logger.getLogger(MstProvinceEditBean.class);
	
	private MstProvince mstProvince;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private MstProvinceService mstProvinceService;
	
	public FacesUtil facesUtil;
	
	private String navigateSearch = MstProvinceConstants.NAVIGATE_SEARCH;

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
		checkNewOrEdit();
	}
	
	private void checkNewOrEdit() {
		try {
			String editId = facesUtil.retrieveRequestParam("id");
			String token = facesUtil.retrieveRequestParam("token"); 
			
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
				//this.handleEdit(editId);
			}
		} catch (Exception e) {

		}

	}
	
	private void handleNew() {
		mstProvince = new MstProvince();
		actionMode = Constants.ACTION_ADD;
	}
	
	public Boolean validate() {
		boolean valid = true;
		
		if (StringUtils.isEmpty(mstProvince.getBranchCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstProvinceBranchCode") 
					+ facesUtil.retrieveMessage("validateRequired"));
			
			valid = false;
		}
		
		if (!StringUtils.isEmpty(mstProvince.getBranchCode())) {
			Integer duplicate = mstProvinceService.countBranchCodeDuplicate(mstProvince.getBranchCode());
			
			if (duplicate.intValue() > 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstProvinceBranchCode") 
						+ " " + facesUtil.retrieveMessage("errorDuplicate"));
				
				valid = false;
			}
		}
		
		
		if (StringUtils.isEmpty(mstProvince.getProvince())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstProvinceProvince") 
					+ " " + facesUtil.retrieveMessage("validateRequired"));
			
			valid = false;
		}
		
		return valid;
	}
	
	public void save() {
		try {
			if (validate()) {
				mstProvince.setCreatedBy(facesUtil.retrieveUserLogin());
				mstProvince.setCreationDate(new Timestamp(new Date().getTime()));
				mstProvince.setDelId(new Long(0));
				mstProvince.setEnabledFlag(Constants.CONSTANT_YES);
				mstProvinceService.save(mstProvince);
				
				facesUtil.redirect("/pages/mstProvince/mstProvince.faces");
			}
		} catch(Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/mstProvince/mstProvince.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MstProvinceEditBean.logger = logger;
	}

	public MstProvince getMstProvince() {
		return mstProvince;
	}

	public void setMstProvince(MstProvince mstProvince) {
		this.mstProvince = mstProvince;
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

	public MstProvinceService getMstProvinceService() {
		return mstProvinceService;
	}

	public void setMstProvinceService(MstProvinceService mstProvinceService) {
		this.mstProvinceService = mstProvinceService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}