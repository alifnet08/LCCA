package com.wo.module.staticPage.bean;

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
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.staticPage.constant.StaticPageConstants;
import com.wo.module.staticPage.model.StaticPage;
import com.wo.module.staticPage.service.StaticPageService;

public class StaticPageEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(StaticPageEditBean.class);

	private StaticPage staticPage;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private StaticPageService staticPageService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> categoryList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeastaticPageh = StaticPageConstants.NAVIGATE_SEARCH;

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
		fileUtil = FileUtil.getInstance();
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_STATIC_PAGE_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	private void checkNewOrEdit() {
		try {
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
		} catch (Exception e) {

		}

	}

	private void handleNew() {
		staticPage = new StaticPage();
		ParameterDetail pd = new ParameterDetail();
		staticPage.setStaticPageCategory(pd);
		lastSequenceOfDtl = 0;
		actionMode = Constants.ACTION_ADD;
		uploadFiles = new ArrayList<UploadedFileWO>();
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
		staticPage = staticPageService.findById(idLong);
		
		
		lastSequenceOfDtl = 0;
		
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(staticPage.getStaticPageCategory().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formFaqCategory") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(staticPage.getTitleIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formStaticPageTitles") + " in "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
	
		

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(staticPage.getStaticPageCategory().getParameterDtlCode());
				staticPage.setStaticPageCategory(pd);

				if (staticPage.getStaticPageId() != null) {
					staticPage.setLastUpdateBy(facesUtil.retrieveUserLogin());
					staticPage.setLastUpdateDate(new Timestamp(new Date().getTime()));
					staticPage.setDelId(new Long(0));
					staticPage.setEnabledFlag(Constants.CONSTANT_YES);
					staticPageService.update(staticPage);

				} else {
					staticPage.setCreatedBy(facesUtil.retrieveUserLogin());
					staticPage.setCreationDate(new Timestamp(new Date().getTime()));
					staticPage.setDelId(new Long(0));
					staticPage.setEnabledFlag(Constants.CONSTANT_YES);
					staticPageService.save(staticPage);
				}

				facesUtil.redirect("/pages/staticPage/staticPage.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	

	public void cancel() {
		try {
			facesUtil.redirect("/pages/staticPage/staticPage.faces");
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

	public StaticPageService getStaticPageService() {
		return staticPageService;
	}

	public void setStaticPageService(StaticPageService staticPageService) {
		this.staticPageService = staticPageService;
	}

	public String getNavigateSeastaticPageh() {
		return navigateSeastaticPageh;
	}

	public void setNavigateSeastaticPageh(String navigateSeastaticPageh) {
		this.navigateSeastaticPageh = navigateSeastaticPageh;
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


	public List<SelectItem> getNameList() {
		return nameList;
	}

	public void setNameList(List<SelectItem> nameList) {
		this.nameList = nameList;
	}

	public List<SelectItem> getSlaTypeList() {
		return slaTypeList;
	}

	public void setSlaTypeList(List<SelectItem> slaTypeList) {
		this.slaTypeList = slaTypeList;
	}

	public boolean isCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}


	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}


	public StaticPageService getRcService() {
		return staticPageService;
	}

	public void setRcService(StaticPageService staticPageService) {
		this.staticPageService = staticPageService;
	}


	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public StaticPage getStaticPage() {
		return staticPage;
	}

	public void setStaticPage(StaticPage staticPage) {
		this.staticPage = staticPage;
	}
	
	

}