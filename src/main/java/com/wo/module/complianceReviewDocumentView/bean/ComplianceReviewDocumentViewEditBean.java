package com.wo.module.complianceReviewDocumentView.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceReviewDocumentView.constant.ComplianceReviewDocumentViewConstants;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentAttachmentView;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentPicComplianceView;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentPicComplianceViewTableModel;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentView;
import com.wo.module.complianceReviewDocumentView.service.ComplianceReviewDocumentViewService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class ComplianceReviewDocumentViewEditBean extends CommonBean implements SelectorListener<Object>, Serializable{

	private static final long serialVersionUID = -8778079743346089985L;
	
	private ComplianceReviewDocumentView complianceReviewDocumentView;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private List<SelectItem> documentTypes;
	private List<SelectItem> documentSubmitters;
	private List<SelectItem> divisions;

	private List<UploadedFileWO> uploadFilesDocumentMemo;
	
	private SelectorInfo selectorCompliance;
	
	private ComplianceReviewDocumentPicComplianceView[] selectedDataCompliance;
	
	private ComplianceReviewDocumentPicComplianceViewTableModel<ComplianceReviewDocumentPicComplianceView> tableModelCompliance;

	private Integer lastSequenceOfCompliance;
	private Integer indexDtlCompliance;
	
	private ComplianceReviewDocumentViewService complianceReviewDocumentViewService;
	private UserService userService;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
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
		selectDocumentType();
		selectDocumentSubmitter();
		selectDivision();
		
		selectorCompliance = ComplianceReviewDocumentViewConstants.buildSelectorPICCompliance(facesUtil);
		
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	private void selectDocumentType() {
		documentTypes = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_TYPE);
			for (ParameterDetail vo: pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				documentTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectDocumentSubmitter() {
		documentSubmitters = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_SUBMITTER);
			for (ParameterDetail vo : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				documentSubmitters.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (Division vo : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		String viewId = facesUtil.retrieveRequestParam("viewId");
		
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty()) {
			if(viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			
		} else {
			this.handleEdit(editId);
		}
	}
	
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		complianceReviewDocumentView = complianceReviewDocumentViewService.findById(idLong);
		lastSequenceOfCompliance = 0;
		
		if(complianceReviewDocumentView.getComplianceReviewDocumentPicComplianceViews() != null) {
			lastSequenceOfCompliance = complianceReviewDocumentView.getComplianceReviewDocumentPicComplianceViews().size();
			for (int i = 0; i < complianceReviewDocumentView.getComplianceReviewDocumentPicComplianceViews().size(); i++) {
				ComplianceReviewDocumentPicComplianceView dtl = (ComplianceReviewDocumentPicComplianceView) complianceReviewDocumentView.getComplianceReviewDocumentPicComplianceViews().get(i);
				
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);
				
				if(dtl.getUser() != null) {
					dtl.setUserNIKTemp(dtl.getUser().getNik());
					dtl.setUserNameTemp(dtl.getUser().getName());
					dtl.setUserEmailTemp(dtl.getUser().getEmail());
				}
			}
		}
		
		uploadFilesDocumentMemo = new ArrayList<UploadedFileWO>();
		
		for (int i = 0; i < complianceReviewDocumentView.getComplianceReviewDocumentAttachmentViews().size(); i++) {
			ComplianceReviewDocumentAttachmentView ra = complianceReviewDocumentView.getComplianceReviewDocumentAttachmentViews().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFilesDocumentMemo.add(uf);
		}
		
		tableModelCompliance = new ComplianceReviewDocumentPicComplianceViewTableModel<ComplianceReviewDocumentPicComplianceView>(
					complianceReviewDocumentView.getComplianceReviewDocumentPicComplianceViews());
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/complianceReviewDocumentView/complianceReviewDocumentView.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public ComplianceReviewDocumentView getComplianceReviewDocumentView() {
		return complianceReviewDocumentView;
	}

	public void setComplianceReviewDocumentView(ComplianceReviewDocumentView complianceReviewDocumentView) {
		this.complianceReviewDocumentView = complianceReviewDocumentView;
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

	public List<SelectItem> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<SelectItem> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<SelectItem> getDocumentSubmitters() {
		return documentSubmitters;
	}

	public void setDocumentSubmitters(List<SelectItem> documentSubmitters) {
		this.documentSubmitters = documentSubmitters;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public List<UploadedFileWO> getUploadFilesDocumentMemo() {
		return uploadFilesDocumentMemo;
	}

	public void setUploadFilesDocumentMemo(List<UploadedFileWO> uploadFilesDocumentMemo) {
		this.uploadFilesDocumentMemo = uploadFilesDocumentMemo;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}
	
	public ComplianceReviewDocumentPicComplianceView[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(ComplianceReviewDocumentPicComplianceView[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public ComplianceReviewDocumentPicComplianceViewTableModel<ComplianceReviewDocumentPicComplianceView> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			ComplianceReviewDocumentPicComplianceViewTableModel<ComplianceReviewDocumentPicComplianceView> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}

	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		
	}

	public ComplianceReviewDocumentViewService getComplianceReviewDocumentViewService() {
		return complianceReviewDocumentViewService;
	}

	public void setComplianceReviewDocumentViewService(ComplianceReviewDocumentViewService complianceReviewDocumentViewService) {
		this.complianceReviewDocumentViewService = complianceReviewDocumentViewService;
	}
}