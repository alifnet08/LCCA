package com.wo.module.discussion.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.discussion.constant.DiscussionConstants;
import com.wo.module.discussion.model.Discussion;
import com.wo.module.discussion.service.DiscussionService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.outgoingLetter.model.OutgoingLetterAttachment;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.qa.model.QAKeyword;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class DiscussionEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DiscussionEditBean.class);

	private Discussion discussion;

	private Boolean isViewOnly;

	private String actionMode;
	
	private List<String> tagName;

	private String editedId;

	private DiscussionService discussionService;
	
	private UserService userService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> threadTypeList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private List<SelectItem> statusList;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSeadiscussionh = DiscussionConstants.NAVIGATE_SEARCH;
	
	Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

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
		threadTypeList = new ArrayList<SelectItem>();
		List<ParameterDetail> listDiscussionType = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);
		

		for (ParameterDetail vo : listDiscussionType) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			threadTypeList.add(si);
		}
		
		statusList = new ArrayList<SelectItem>();
		List<ParameterDetail> listStatus = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);
		

		for (ParameterDetail vo : listStatus) {
			if (!(vo.getParameterDtlCode().equals("COMPLIANCE_NOT_APPROPRIATE") 
					|| vo.getParameterDtlCode().equals("COMPLIANCE_APPROPRIATE"))) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			statusList.add(si);
			}
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
		discussion = new Discussion();
		ParameterDetail pd = new ParameterDetail();
		discussion.setThreadType(pd);
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
		discussion = discussionService.findById(idLong);
		
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(discussion.getThreadType().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formDiscussionThreadType") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				if (discussion.getDiscussionId() != null) {
					User user = userService.getUserByNik(facesUtil.retrieveUserLogin());
					discussion.setThreadUser(user);
					discussion.setLastUpdateBy(facesUtil.retrieveUserLogin());
					discussion.setLastUpdateDate(new Timestamp(new Date().getTime()));
					discussion.setDelId(new Long(0));
					discussion.setEnabledFlag(Constants.CONSTANT_YES);
					discussionService.update(discussion);

				} else {
					User user = userService.getUserByNik(facesUtil.retrieveUserLogin());
					discussion.setThreadUser(user);
					discussion.setThreadStartDate(new Date());
					discussion.setCreatedBy(facesUtil.retrieveUserLogin());
					discussion.setCreationDate(new Timestamp(new Date().getTime()));
					discussion.setDelId(new Long(0));
					discussion.setEnabledFlag(Constants.CONSTANT_YES);
					discussionService.save(discussion);
				}

				facesUtil.redirect("/pages/discussion/discussion.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void handleFileUpload (FileUploadEvent event) {
		try {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
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
			facesUtil.redirect("/pages/discussion/discussion.faces");
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

	public DiscussionService getDiscussionService() {
		return discussionService;
	}

	public void setDiscussionService(DiscussionService discussionService) {
		this.discussionService = discussionService;
	}

	public String getNavigateSeadiscussionh() {
		return navigateSeadiscussionh;
	}

	public void setNavigateSeadiscussionh(String navigateSeadiscussionh) {
		this.navigateSeadiscussionh = navigateSeadiscussionh;
	}

	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
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

	public Discussion getRc() {
		return discussion;
	}

	public void setRc(Discussion discussion) {
		this.discussion = discussion;
	}

	public DiscussionService getRcService() {
		return discussionService;
	}

	public void setRcService(DiscussionService discussionService) {
		this.discussionService = discussionService;
	}

	

	public List<SelectItem> getThreadTypeList() {
		return threadTypeList;
	}

	public void setThreadTypeList(List<SelectItem> threadTypeList) {
		this.threadTypeList = threadTypeList;
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

	public List<String> getTagName() {
		return tagName;
	}

	public void setTagName(List<String> tagName) {
		this.tagName = tagName;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		DiscussionEditBean.logger = logger;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}
	
	

}