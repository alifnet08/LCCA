package com.wo.module.aboutUs.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.aboutUs.constant.AboutUsConstants;
import com.wo.module.aboutUs.model.AboutUs;
import com.wo.module.aboutUs.service.AboutUsService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.faq.model.FaqDocument;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.User;

public class AboutUsEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(AboutUsEditBean.class);

	private AboutUs aboutUs;

	private Boolean isViewOnly;

	private String actionMode;
	
	private List<String> keywords;

	private String editedId;

	private AboutUsService aboutUsService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> categoryList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FileUtil fileUtil;

	private boolean checkAll;
	
	private SelectorInfo selectorPUK;

	private String navigateSearch = AboutUsConstants.NAVIGATE_SEARCH;

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
		selectorPUK = buildSelectorPUK();
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	public static SelectorInfo buildSelectorPUK() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select ABOUT_US_ID, OUR_NAME, OUR_JOB, EMAIL from wo_mst_about_us "
                + " where 1=1 "
                + " and ( upper(OUR_JOB) like upper('%{0}%') or upper(OUR_NAME) like upper('%{0}%') or upper(EMAIL) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' "
                + " order by OUR_NAME "  ,
                " SELECT COUNT(1) from wo_mst_about_us "
                + " where 1=1 "
                + " and ( upper(OUR_JOB) like upper('%{0}%') or upper(OUR_NAME) like upper('%{0}%') or upper(EMAIL) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("Name", "Job", "Email"),
                Arrays.asList("1", "2", "3"),false);
        return info;
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
		aboutUs = new AboutUs();
		AboutUs puk = new AboutUs();
		aboutUs.setPuk(puk);
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
		aboutUs = aboutUsService.findById(idLong);
		
		if(aboutUs.getPuk() == null){
		AboutUs puk = new AboutUs();
		aboutUs.setPuk(puk);
		}	
		
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		if(aboutUs.getPhotoFile() !=null){
		UploadedFileWO uf = new UploadedFileWO();
		uf.setFileName(aboutUs.getPhotoFile());
		uf.setFileId(aboutUs.getFileId());
		uf.setIsNew(false);
		uf.setFileSize(aboutUs.getFileSize());
		uploadFiles.add(uf);
		}
		
			
		lastSequenceOfDtl = 0;
		
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(aboutUs.getOurName())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formAboutUsName") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (StringUtils.isEmpty(aboutUs.getOurJob())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formAboutUsPosition") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (StringUtils.isEmpty(aboutUs.getEmail())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formAboutUsEmail") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}


		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				if(uploadFiles != null) {
					for (int i = 0; i < uploadFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
						aboutUs.setPhotoFile(uf.getFileName());
						aboutUs.setFileId(uf.getFileId());
						aboutUs.setFileSize(uf.getFileSize());
					}
				}
				
				if (aboutUs.getAboutUsId() != null) {
					if(aboutUs.getPuk()!=null && aboutUs.getPuk().getAboutUsId() == null){
						aboutUs.setPuk(null);
					}
					aboutUs.setLastUpdateBy(facesUtil.retrieveUserLogin());
					aboutUs.setLastUpdateDate(new Timestamp(new Date().getTime()));
					aboutUs.setDelId(new Long(0));
					aboutUs.setEnabledFlag(Constants.CONSTANT_YES);
					aboutUsService.update(aboutUs);

				} else {
					if(aboutUs.getPuk()!=null && aboutUs.getPuk().getAboutUsId() == null){
						aboutUs.setPuk(null);
					}
					aboutUs.setCreatedBy(facesUtil.retrieveUserLogin());
					aboutUs.setCreationDate(new Timestamp(new Date().getTime()));
					aboutUs.setDelId(new Long(0));
					aboutUs.setEnabledFlag(Constants.CONSTANT_YES);
					aboutUsService.save(aboutUs);
				}

				facesUtil.redirect("/pages/aboutUs/aboutUs.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void handleFileUpload (FileUploadEvent event) {
		try {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
			uploadFiles.clear();
			uploadFiles.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadFiles.remove(uploadFiles.get(index));
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/aboutUs/aboutUs.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clearPuk() {
		aboutUs.setPuk(null);
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public AboutUsService getAboutUsService() {
		return aboutUsService;
	}

	public void setAboutUsService(AboutUsService aboutUsService) {
		this.aboutUsService = aboutUsService;
	}

	

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public AboutUs getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(AboutUs aboutUs) {
		this.aboutUs = aboutUs;
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

	public AboutUs getRc() {
		return aboutUs;
	}

	public void setRc(AboutUs aboutUs) {
		this.aboutUs = aboutUs;
	}

	public AboutUsService getRcService() {
		return aboutUsService;
	}

	public void setRcService(AboutUsService aboutUsService) {
		this.aboutUsService = aboutUsService;
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

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		// TODO Auto-generated method stub
		if (StringUtils.equals("pukDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			AboutUs puk = aboutUsService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (puk != null) {
				aboutUs.setPuk(puk);
				
			}
		}
	}

	public SelectorInfo getSelectorPUK() {
		return selectorPUK;
	}

	public void setSelectorPUK(SelectorInfo selectorPUK) {
		this.selectorPUK = selectorPUK;
	}
	
	

}