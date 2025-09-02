package com.wo.module.advocate.bean;

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
import org.primefaces.PrimeFaces;

import com.wo.module.advocate.constant.AdvocateConstants;
import com.wo.module.advocate.model.Advocate;
import com.wo.module.advocate.model.AdvocateInfo;
import com.wo.module.advocate.model.AdvocateInfoTableModel;
import com.wo.module.advocate.model.AdvocatePartnerTableModel;
import com.wo.module.advocate.model.AdvocatePartners;
import com.wo.module.advocate.service.AdvocateService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class AdvocateEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(AdvocateEditBean.class);

	private Advocate advocate;

	private Boolean isViewOnly;

	private String actionMode;
	
	private List<String> keywords;

	private String editedId;

	private AdvocateService advocateService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> categoryList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private AdvocateInfo[] selectedInfoData;
	private AdvocateInfoTableModel<AdvocateInfo> advocateInfoModel;
	private Integer lastSequenceOfInfo;
	
	private AdvocatePartners[] selectedPartnerData;
	private AdvocatePartnerTableModel<AdvocatePartners> advocatePartnerModel;
	private Integer lastSequenceOfPartner;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeaadvocath = AdvocateConstants.NAVIGATE_SEARCH;

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
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);
		

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
		advocate = new Advocate();
		
		lastSequenceOfDtl = 0;
		actionMode = Constants.ACTION_ADD;
		uploadFiles = new ArrayList<UploadedFileWO>();
		facesUtil.setSessionAttribute("token", null);
		
		advocateInfoModel = new AdvocateInfoTableModel<AdvocateInfo>(
				advocate.getAdvocateInfos());
		
		advocatePartnerModel = new AdvocatePartnerTableModel<AdvocatePartners>(
				advocate.getAdvocatePartners());
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		advocate = advocateService.findById(idLong);
		
		
		lastSequenceOfDtl = 0;
		
		if(advocate.getAdvocateInfos() != null) {
			lastSequenceOfInfo = advocate.getAdvocateInfos().size();
			advocateInfoModel = new AdvocateInfoTableModel<AdvocateInfo>(
					advocate.getAdvocateInfos());
		}else {
			lastSequenceOfInfo = 0;
			advocateInfoModel = new AdvocateInfoTableModel<AdvocateInfo>(
					advocate.getAdvocateInfos());
		}
		
		if(advocate.getAdvocatePartners() != null) {
			lastSequenceOfPartner = advocate.getAdvocatePartners().size();
			advocatePartnerModel = new AdvocatePartnerTableModel<AdvocatePartners>(
					advocate.getAdvocatePartners());
		}else {
			lastSequenceOfPartner = 0;
			advocatePartnerModel = new AdvocatePartnerTableModel<AdvocatePartners>(
					advocate.getAdvocatePartners());
		}
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(advocate.getRegion())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formAdvocatRegion") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				
				if (advocate.getAdvocateInfos() != null) {
					for (int i = 0; i < advocate.getAdvocateInfos().size(); i++) {
						AdvocateInfo dtl = (AdvocateInfo) advocate.getAdvocateInfos().get(i);
						dtl.setAdvocate(advocate);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
				
				if (advocate.getAdvocatePartners() != null) {
					for (int i = 0; i < advocate.getAdvocatePartners().size(); i++) {
						AdvocatePartners dtl = (AdvocatePartners) advocate.getAdvocatePartners().get(i);
						dtl.setAdvocate(advocate);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}

				if (advocate.getAdvocateId() != null) {
					advocate.setLastUpdateBy(facesUtil.retrieveUserLogin());
					advocate.setLastUpdateDate(new Timestamp(new Date().getTime()));
					advocate.setDelId(new Long(0));
					advocate.setEnabledFlag(Constants.CONSTANT_YES);
					advocateService.update(advocate);

				} else {
					advocate.setCreatedBy(facesUtil.retrieveUserLogin());
					advocate.setCreationDate(new Timestamp(new Date().getTime()));
					advocate.setDelId(new Long(0));
					advocate.setEnabledFlag(Constants.CONSTANT_YES);
					advocateService.save(advocate);
				}

				facesUtil.redirect("/pages/advocate/advocate.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void onAddNewInfo() {
		if (advocate.getAdvocateInfos() == null) {
			advocate.setAdvocateInfos(new ArrayList<AdvocateInfo>());
			lastSequenceOfInfo = 0;
		} else {
			if (advocate.getAdvocateInfos().size() == 0) {
				lastSequenceOfInfo = 0;
			}
		}

		AdvocateInfo d = new AdvocateInfo();
		lastSequenceOfInfo = lastSequenceOfInfo + 1;
		d.setSequence(lastSequenceOfInfo);
		advocate.getAdvocateInfos().add(d);
		advocateInfoModel.setWrappedData(advocate.getAdvocateInfos());
		
		PrimeFaces.current().executeScript("reinitSelect2()");

	}

	public void onDeleteRowInfo() {
		for (int i = 0; i < selectedInfoData.length; i++) {
			advocate.getAdvocateInfos().remove(selectedInfoData[i]);
		}

		if (advocate.getAdvocateInfos() == null
				|| advocate.getAdvocateInfos().size() == 0) {
			lastSequenceOfInfo = 0;
		}

		advocateInfoModel.setWrappedData(advocate.getAdvocateInfos());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPartner() {
		if (advocate.getAdvocatePartners() == null) {
			advocate.setAdvocatePartners(new ArrayList<AdvocatePartners>());
			lastSequenceOfPartner = 0;
		} else {
			if (advocate.getAdvocatePartners().size() == 0) {
				lastSequenceOfPartner = 0;
			}
		}

		AdvocatePartners d = new AdvocatePartners();
		lastSequenceOfPartner = lastSequenceOfPartner + 1;
		d.setSequence(lastSequenceOfPartner);
		advocate.getAdvocatePartners().add(d);
		advocatePartnerModel.setWrappedData(advocate.getAdvocatePartners());
		
		PrimeFaces.current().executeScript("reinitSelect2()");
	}

	public void onDeleteRowPartner() {
		for (int i = 0; i < selectedPartnerData.length; i++) {
			advocate.getAdvocatePartners().remove(selectedPartnerData[i]);
		}

		if (advocate.getAdvocatePartners() == null
				|| advocate.getAdvocatePartners().size() == 0) {
			lastSequenceOfPartner = 0;
		}

		advocatePartnerModel.setWrappedData(advocate.getAdvocatePartners());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/advocate/advocate.faces");
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

	public String getNavigateSeaadvocath() {
		return navigateSeaadvocath;
	}

	public void setNavigateSeaadvocath(String navigateSeaadvocath) {
		this.navigateSeaadvocath = navigateSeaadvocath;
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

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Integer getLastSequenceOfInfo() {
		return lastSequenceOfInfo;
	}

	public void setLastSequenceOfInfo(Integer lastSequenceOfInfo) {
		this.lastSequenceOfInfo = lastSequenceOfInfo;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AdvocateEditBean.logger = logger;
	}

	public Advocate getAdvocate() {
		return advocate;
	}

	public void setAdvocate(Advocate advocate) {
		this.advocate = advocate;
	}

	public AdvocateService getAdvocateService() {
		return advocateService;
	}

	public void setAdvocateService(AdvocateService advocateService) {
		this.advocateService = advocateService;
	}

	public AdvocateInfo[] getSelectedInfoData() {
		return selectedInfoData;
	}

	public void setSelectedInfoData(AdvocateInfo[] selectedInfoData) {
		this.selectedInfoData = selectedInfoData;
	}

	public AdvocateInfoTableModel<AdvocateInfo> getAdvocateInfoModel() {
		return advocateInfoModel;
	}

	public void setAdvocateInfoModel(AdvocateInfoTableModel<AdvocateInfo> advocateInfoModel) {
		this.advocateInfoModel = advocateInfoModel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AdvocatePartnerTableModel<AdvocatePartners> getAdvocatePartnerModel() {
		return advocatePartnerModel;
	}

	public void setAdvocatePartnerModel(AdvocatePartnerTableModel<AdvocatePartners> advocatePartnerModel) {
		this.advocatePartnerModel = advocatePartnerModel;
	}

	public AdvocatePartners[] getSelectedPartnerData() {
		return selectedPartnerData;
	}

	public void setSelectedPartnerData(AdvocatePartners[] selectedPartnerData) {
		this.selectedPartnerData = selectedPartnerData;
	}

	public Integer getLastSequenceOfPartner() {
		return lastSequenceOfPartner;
	}

	public void setLastSequenceOfPartner(Integer lastSequenceOfPartner) {
		this.lastSequenceOfPartner = lastSequenceOfPartner;
	}

}