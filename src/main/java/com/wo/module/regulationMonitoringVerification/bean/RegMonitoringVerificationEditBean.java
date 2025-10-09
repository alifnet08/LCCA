package com.wo.module.regulationMonitoringVerification.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.regulationMonitoring.constant.RegMonitoringConstants;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICComplianceTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICComplianceTrcTableModel;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTrcTableModel;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTrc;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTrcTableModel;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoring.service.RegMonitoringTrcService;
import com.wo.module.regulationMonitoring.service.RegulationMonitoringService;
import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;
import com.wo.module.regulationMonitoringVerification.service.RegMonitoringVerificationService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class RegMonitoringVerificationEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegMonitoringVerificationEditBean.class);

	private RegMonitoringTrc regMonitoringTrc;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long docTypeId;

	private Long docCategoryId;

	private Long docTopicId;

	private Long counterTypeId;

	private List<SelectItem> divisions;

	private List<SelectItem> followUps;

	private List<SelectItem> provTypes;

	private List<SelectItem> trackCodes;

	private List<SelectItem> docTypes;

	private List<SelectItem> docCategories;

	private List<SelectItem> docTopics;

	private List<SelectItem> counterTypes;

	private List<SelectItem> reminderStatusList;

	private List<SelectItem> complianceStatusList;

	private SelectorInfo selectorJdlPeraturan;

	private SelectorInfo selectorCompliance;

	private SelectorInfo selectorFollowup;

	private SelectorInfo selectorPic1;

	private SelectorInfo selectorPic2;

	private SelectorInfo selectorPic3;

	private List<UploadedFile> uploadedFiles;

	private RegMonitoringRegulationTrc[] selectedData;

	private RegMonitoringPICComplianceTrc[] selectedDataCompliance;

	private RegMonitoringPICFollowUpTrc[] selectedDataFollowup;

	private RegMonitoringRegulationTrcTableModel<RegMonitoringRegulationTrc> tableModel;

	private RegMonitoringPICComplianceTrcTableModel<RegMonitoringPICComplianceTrc> tableModelCompliance;

	private RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc> tableModelFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;

	private Integer indexDtlCompliance;

	private Integer indexDtlFollowup;

	private RegMonitoringVerificationService regMonitoringVerificationService;

	private RegulationMonitoringService regulationMonitoringService;

	private RegMonitoringTrcService regMonitoringTrcService;

	// private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private RegulationService regulationService;

	private UserService userService;

	private CounterTypeService counterTypeService;

	public FacesUtil facesUtil;

	private String navigateSearch = ExternalRegulationConstants.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

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
		selectDivision();
		selectFollowup();
		selectProvType();
		selectTrackCode();
		selectDocType();
		selectDocCategory();
		selectDocTopic();
		selectCounterType();
		selectReminderStatus();
		selectComplianceStatus();

		selectorJdlPeraturan = RegMonitoringConstants.buildSelectorJdlPeraturan(facesUtil);
		selectorCompliance = RegMonitoringConstants.buildSelectorPICCompliance(facesUtil);
		selectorFollowup = RegMonitoringConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = RegMonitoringConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = RegMonitoringConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = RegMonitoringConstants.buildSelectorPIC(facesUtil);

		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
	}

	public void selectReminderStatus() {
		reminderStatusList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectComplianceStatus() {
		complianceStatusList = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> listComplianceDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);

			for (ParameterDetail vo : listComplianceDtl) {
				if (!(vo.getParameterDtlCode().equals("COMPLIANCE_NOT_APPROPRIATE") 
						|| vo.getParameterDtlCode().equals("COMPLIANCE_APPROPRIATE"))) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				complianceStatusList.add(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectFollowup() {
		followUps = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue("Y");
		followUps.add(si);

		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue("N");
		followUps.add(si2);
	}

	public void selectDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("JENIS_KETENTUAN");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				provTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectTrackCode() {
		trackCodes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TRACK_RECORD");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				trackCodes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocType() {
		docTypes = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocCategory() {
		docCategories = new ArrayList<SelectItem>();
		try {
			List<DocumentCategory> pd = documentCategoryService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE, null)),
					0, Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory) pd.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory) pd.get(i)).getDocumentCategoryId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocTopic() {
		docTopics = new ArrayList<SelectItem>();
		try {
			List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic) pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic) pd.get(i)).getDocumentTopicId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
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
			if (viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			// this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFile>() : uploadedFiles;
		uploadedFiles.add(event.getFile());
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		regMonitoringTrc = regMonitoringTrcService.findById(idLong);
		if (regMonitoringTrc.getCounterType() != null) {
			counterTypeId = regMonitoringTrc.getCounterType().getCounterTypeId();
		}

		tableModel = new RegMonitoringRegulationTrcTableModel<RegMonitoringRegulationTrc>(
				regMonitoringTrc.getRegMonitoringRegulationTrcs());
		tableModelCompliance = new RegMonitoringPICComplianceTrcTableModel<RegMonitoringPICComplianceTrc>(
				regMonitoringTrc.getRegMonitoringPICComplianceTrcs());
		tableModelFollowup = new RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc>(
				regMonitoringTrc.getRegMonitoringPICFollowUpTrcs());

		tableStatus = regulationMonitoringService.getDataConfirmStatusByRegMonitoringId(idLong);

		if (regMonitoringTrc.getRegMonitoringPICFollowUpTrcs() != null) {
			for (int i = 0; i < regMonitoringTrc.getRegMonitoringPICFollowUpTrcs().size(); i++) {
				RegMonitoringPICFollowUpTrc dtl = (RegMonitoringPICFollowUpTrc) regMonitoringTrc
						.getRegMonitoringPICFollowUpTrcs().get(i);
				if (dtl.getComplianceStatus() != null && dtl.getComplianceStatus().getParameterDtlCode() != null
						&& dtl.getComplianceStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN)) {
					dtl.setComplianceStatus(new ParameterDetail());
				} else if (dtl.getComplianceStatus() != null && dtl.getComplianceStatus().getParameterDtlCode() != null) {
					ParameterDetail pd = new ParameterDetail();
					pd.setParameterDtlCode(dtl.getComplianceStatus().getParameterDtlCode());
					dtl.setComplianceStatus(pd);
				} else {
					dtl.setComplianceStatus(new ParameterDetail());
				}
			}
		}

	}

	public Boolean isFoundError() {
		Boolean flag = false;
		
		if (regMonitoringTrc.getRegMonitoringPICFollowUpTrcs() != null) {
			for (int i = 0; i < regMonitoringTrc.getRegMonitoringPICFollowUpTrcs().size(); i++) {
				RegMonitoringPICFollowUpTrc dtl = (RegMonitoringPICFollowUpTrc) regMonitoringTrc
						.getRegMonitoringPICFollowUpTrcs().get(i);				
								
				if((dtl.getComplianceStatus() == null ||
						StringUtils.isBlank(dtl.getComplianceStatus().getParameterDtlCode())) && dtl.getFollowUpStatus() != null 
						&& dtl.getFollowUpStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceComplianceCheckerStatus") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				} else {				
					if(dtl.getComplianceStatus() != null && dtl.getComplianceStatus().getParameterDtlCode() != null &&
							dtl.getComplianceStatus().getParameterDtlCode().
							equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN)) {
						if (StringUtils.isEmpty(dtl.getComplianceNote())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceComplianceCheckerNote") + " "
									+ facesUtil.retrieveMessage("validateRequired"));
							
							flag = true;
						}
					}
				}
			}
		}

		return flag;
	}

	public void save() {
		try {
			if (!isFoundError()) {
				User user = (User) facesUtil.getUserLogin();

				regMonitoringVerificationService.processConfirm(regMonitoringTrc, user);

				facesUtil.redirect(
						"/pages/regulationMonitoringVerification/regulationMonitoringVerification.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/regulationMonitoringVerification/regulationMonitoringVerification.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegMonitoringVerificationEditBean.logger = logger;
	}

	public RegMonitoringTrc getRegMonitoringTrc() {
		return regMonitoringTrc;
	}

	public void setRegMonitoringTrc(RegMonitoringTrc regMonitoringTrc) {
		this.regMonitoringTrc = regMonitoringTrc;
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

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Long getDocCategoryId() {
		return docCategoryId;
	}

	public void setDocCategoryId(Long docCategoryId) {
		this.docCategoryId = docCategoryId;
	}

	public Long getDocTopicId() {
		return docTopicId;
	}

	public void setDocTopicId(Long docTopicId) {
		this.docTopicId = docTopicId;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public List<SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<SelectItem> provTypes) {
		this.provTypes = provTypes;
	}

	public List<SelectItem> getTrackCodes() {
		return trackCodes;
	}

	public void setTrackCodes(List<SelectItem> trackCodes) {
		this.trackCodes = trackCodes;
	}

	public List<SelectItem> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<SelectItem> docTypes) {
		this.docTypes = docTypes;
	}

	public List<SelectItem> getDocCategories() {
		return docCategories;
	}

	public void setDocCategories(List<SelectItem> docCategories) {
		this.docCategories = docCategories;
	}

	public List<SelectItem> getDocTopics() {
		return docTopics;
	}

	public void setDocTopics(List<SelectItem> docTopics) {
		this.docTopics = docTopics;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public SelectorInfo getSelectorJdlPeraturan() {
		return selectorJdlPeraturan;
	}

	public void setSelectorJdlPeraturan(SelectorInfo selectorJdlPeraturan) {
		this.selectorJdlPeraturan = selectorJdlPeraturan;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}

	public SelectorInfo getSelectorFollowup() {
		return selectorFollowup;
	}

	public void setSelectorFollowup(SelectorInfo selectorFollowup) {
		this.selectorFollowup = selectorFollowup;
	}

	public SelectorInfo getSelectorPic1() {
		return selectorPic1;
	}

	public void setSelectorPic1(SelectorInfo selectorPic1) {
		this.selectorPic1 = selectorPic1;
	}

	public SelectorInfo getSelectorPic2() {
		return selectorPic2;
	}

	public void setSelectorPic2(SelectorInfo selectorPic2) {
		this.selectorPic2 = selectorPic2;
	}

	public SelectorInfo getSelectorPic3() {
		return selectorPic3;
	}

	public void setSelectorPic3(SelectorInfo selectorPic3) {
		this.selectorPic3 = selectorPic3;
	}

	public List<UploadedFile> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public RegMonitoringRegulationTrc[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(RegMonitoringRegulationTrc[] selectedData) {
		this.selectedData = selectedData;
	}

	public RegMonitoringPICComplianceTrc[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(RegMonitoringPICComplianceTrc[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public RegMonitoringPICFollowUpTrc[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(RegMonitoringPICFollowUpTrc[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public RegMonitoringRegulationTrcTableModel<RegMonitoringRegulationTrc> getTableModel() {
		return tableModel;
	}

	public void setTableModel(RegMonitoringRegulationTrcTableModel<RegMonitoringRegulationTrc> tableModel) {
		this.tableModel = tableModel;
	}

	public RegMonitoringPICComplianceTrcTableModel<RegMonitoringPICComplianceTrc> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			RegMonitoringPICComplianceTrcTableModel<RegMonitoringPICComplianceTrc> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(
			RegMonitoringPICFollowUpTrcTableModel<RegMonitoringPICFollowUpTrc> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public List<StatusConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public RegulationTrackRecord getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecord selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public RegMonitoringVerificationService getRegMonitoringVerificationService() {
		return regMonitoringVerificationService;
	}

	public void setRegMonitoringVerificationService(RegMonitoringVerificationService regMonitoringVerificationService) {
		this.regMonitoringVerificationService = regMonitoringVerificationService;
	}

	public RegulationMonitoringService getRegulationMonitoringService() {
		return regulationMonitoringService;
	}

	public void setRegulationMonitoringService(RegulationMonitoringService regulationMonitoringService) {
		this.regulationMonitoringService = regulationMonitoringService;
	}

	public RegMonitoringTrcService getRegMonitoringTrcService() {
		return regMonitoringTrcService;
	}

	public void setRegMonitoringTrcService(RegMonitoringTrcService regMonitoringTrcService) {
		this.regMonitoringTrcService = regMonitoringTrcService;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public DocumentTopicService getDocumentTopicService() {
		return documentTopicService;
	}

	public void setDocumentTopicService(DocumentTopicService documentTopicService) {
		this.documentTopicService = documentTopicService;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}