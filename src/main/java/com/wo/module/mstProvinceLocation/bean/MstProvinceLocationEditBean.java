package com.wo.module.mstProvinceLocation.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.event.ImageAreaSelectEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstProvince.constants.MstProvinceConstants;
import com.wo.module.mstProvince.model.MstProvince;
import com.wo.module.mstProvince.model.MstProvinceLocation;
import com.wo.module.mstProvince.service.MstProvinceService;
import com.wo.module.mstProvinceLocation.service.MstProvinceLocationService;

public class MstProvinceLocationEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = -7040725697581633681L;
	
	private static Logger logger = Logger.getLogger(MstProvinceLocationEditBean.class);
	
	private MstProvinceLocation mstProvinceLocation;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private MstProvinceLocationService mstProvinceLocationService;
	
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
				this.handleEdit(editId);
			}
		} catch (Exception e) {

		}

	}
	
	private void handleNew() {
		mstProvinceLocation = new MstProvinceLocation();
		actionMode = Constants.ACTION_ADD;
	}
	
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		//Long idLong = Long.parseLong(editId);
		String province = editId;
		
		MstProvinceLocation prov  = mstProvinceLocationService.getProvinceLocationByProvince(province);
		if(prov!=null && prov.getProvince()!=null ){
			mstProvinceLocation = prov;
			PrimeFaces.current().executeScript("setPin("+mstProvinceLocation.getPinTop()+","+mstProvinceLocation.getPinLeft()+");");
		}else{
			MstProvince mstProv = mstProvinceService.getProvinceByProvinceName(province);
			mstProvinceLocation = new MstProvinceLocation();
			mstProvinceLocation.setProvince(mstProv.getProvince());
		}
	}
	
	public Boolean validate() {
		boolean valid = true;
		
		return valid;
	}
	
	public void save() {
		try {
			if (validate()) {
				mstProvinceLocation.setCreatedBy(facesUtil.retrieveUserLogin());
				mstProvinceLocation.setCreationDate(new Timestamp(new Date().getTime()));
				mstProvinceLocation.setDelId(new Long(0));
				mstProvinceLocation.setEnabledFlag(Constants.CONSTANT_YES);
		
				MstProvinceLocation provLoc  = mstProvinceLocationService.getProvinceLocationByProvince(mstProvinceLocation.getProvince());
				if(provLoc!=null && provLoc.getProvince() != null){
					mstProvinceLocationService.update(mstProvinceLocation);
				}else{
					mstProvinceLocationService.save(mstProvinceLocation);
				}
				
				
				facesUtil.redirect("/pages/mstProvinceLocation/mstProvinceLocation.faces");
			}
		} catch(Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}
	
	public void selectEndListener(final ImageAreaSelectEvent e){
		try{
		PrimeFaces.current().executeScript("setPin("+e.getX1()+","+e.getY1()+");");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/mstProvinceLocation/mstProvinceLocation.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MstProvinceLocationEditBean.logger = logger;
	}

	

	public MstProvinceLocation getMstProvinceLocation() {
		return mstProvinceLocation;
	}

	public void setMstProvinceLocation(MstProvinceLocation mstProvinceLocation) {
		this.mstProvinceLocation = mstProvinceLocation;
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

	

	public MstProvinceLocationService getMstProvinceLocationService() {
		return mstProvinceLocationService;
	}

	public void setMstProvinceLocationService(MstProvinceLocationService mstProvinceLocationService) {
		this.mstProvinceLocationService = mstProvinceLocationService;
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

	public MstProvinceService getMstProvinceService() {
		return mstProvinceService;
	}

	public void setMstProvinceService(MstProvinceService mstProvinceService) {
		this.mstProvinceService = mstProvinceService;
	}
	
	
}