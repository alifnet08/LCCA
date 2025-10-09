package com.wo.module.regulationSocializationView.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
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
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationSocialization.constant.RegulationSocializationConstants;
import com.wo.module.regulationSocialization.model.SocializationDocumentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTmp;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTrc;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTrcTableModel;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTmp;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrcTableModel;
import com.wo.module.regulationSocialization.model.SocializationRegulationTmp;
import com.wo.module.regulationSocialization.model.SocializationRegulationTrc;
import com.wo.module.regulationSocialization.model.SocializationRegulationTrcTableModel;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocialization.service.RegulationSocializationService;
import com.wo.module.regulationSocialization.service.SocializationTrcService;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.regulationSocializationView.service.RegulationSocializationViewService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class RegulationSocializationViewDetailBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = -3670080705253204661L;

	static Logger logger = Logger.getLogger(RegulationSocializationViewDetailBean.class);

	private SocializationTrc socializationTrc;

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
	
	private SelectorInfo selectorJdlPeraturan;
	
	private SelectorInfo selectorCompliance;
	
	private SelectorInfo selectorFollowup;
	
	private SelectorInfo selectorPic1;
	
	private SelectorInfo selectorPic2;
	
	private SelectorInfo selectorPic3;

	private SocializationRegulationTmp[] selectedData;
	
	private SocializationPICComplianceTmp[] selectedDataCompliance;
	
	private SocializationPICFollowupTmp[] selectedDataFollowup;

	private SocializationRegulationTrcTableModel<SocializationRegulationTrc> tableModel;
	
	private SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc> tableModelCompliance;
	
	private SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc> tableModelFollowup;
	
	private List<StatusConfirmationVO> tableStatus;
	
	private List<SocializationApprovalVO> tableApproval;

	private RegulationTrackRecord selectedRow;
	
	private Integer indexDtl;
	
	private Integer indexDtlCompliance;
	
	private Integer indexDtlFollowup;

	private RegulationSocializationViewService regulationSocializationViewService;
	
	private RegulationSocializationService regulationSocializationService;

	private SocializationTrcService socializationTrcService;

	private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;
	
	private RegulationService regulationService;
	
	private UserService userService;
	
	private CounterTypeService counterTypeService;

	public FacesUtil facesUtil;
	
	private List<UploadedFileWO> uploadFiles;

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
	
	public void redirectCurrentPage(ComponentSystemEvent event) {
		String url = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		if (facesUtil.getSessionAttribute(Constants.SESSION_NEED_REDIRECT) != null 
				&& ((String) facesUtil.getSessionAttribute(Constants.SESSION_NEED_REDIRECT)).equals("Y")
				&& facesUtil.getSessionAttribute("token") != null
				&& !url.contains("token")) {
			facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
			url = url + "?token=" + ((String) facesUtil.getSessionAttribute("token"));
			try {
				facesUtil.removeSessionAttribute("token");
				facesUtil.redirect(url);
				return;
			} catch (IOException ex) {
				ex.printStackTrace();
				logger.error(ex.getMessage());
			}
		}
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

		selectorJdlPeraturan = RegulationSocializationConstants.buildSelectorJdlPeraturan(facesUtil);
		selectorCompliance = RegulationSocializationConstants.buildSelectorPICCompliance(facesUtil);
		selectorFollowup = RegulationSocializationConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		
		//tableApproval = new ArrayList<SocializationApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
		facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
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
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
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
		try {
			socializationTrc = new SocializationTrc();
			
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("KETENTUAN_EKSTERNAL");
			socializationTrc.setJenisKetentuan(pd);
			
			List<SocializationRegulationTrc> srList = new ArrayList<SocializationRegulationTrc>();
			SocializationRegulationTrc sr = new SocializationRegulationTrc();
			Regulation reg = new Regulation();
			DocumentType dt = new DocumentType();
			DocumentCategory dc = new DocumentCategory();
			DocumentTopic dto = new DocumentTopic();
			reg.setDocumentType(dt);
			reg.setDocumentCategory(dc);
			reg.setDocumentTopic(dto);
			sr.setRegulation(reg);
			srList.add(sr);
			socializationTrc.setSocializationRegulationTrcs(srList);
			
			List<SocializationPICComplianceTrc> listRt = new ArrayList<SocializationPICComplianceTrc>();
			SocializationPICComplianceTrc sp = new SocializationPICComplianceTrc();
			User user = (User)facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			sp.setUser(user);
			listRt.add(sp);
			socializationTrc.setSocializationPICComplianceTrcs(listRt);
			
			List<SocializationPICFollowupTrc> listFp = new ArrayList<SocializationPICFollowupTrc>();
			SocializationPICFollowupTrc fp = new SocializationPICFollowupTrc();
			fp.setUser1(new User());
			fp.setUser2(new User());
			fp.setUser3(new User());
			listFp.add(fp);
			socializationTrc.setSocializationPICFollowupTrcs(listFp);

			actionMode = Constants.ACTION_ADD;
			tableModel = new SocializationRegulationTrcTableModel<SocializationRegulationTrc>(
					socializationTrc.getSocializationRegulationTrcs());
			
			tableModelCompliance = new SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc>(
					socializationTrc.getSocializationPICComplianceTrcs());
			
			tableModelFollowup = new SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc>(
					socializationTrc.getSocializationPICFollowupTrcs());
			
			facesUtil.setSessionAttribute("token", null);
		} catch (Exception e) {
			e.printStackTrace();
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
		socializationTrc = socializationTrcService.findById(idLong);

		if (socializationTrc.getCounterType() != null) {
			counterTypeId = socializationTrc.getCounterType().getCounterTypeId();
		}
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < socializationTrc.getSocializationDocumentTrcs().size(); i++) {
			SocializationDocumentTrc ra = socializationTrc.getSocializationDocumentTrcs().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
			
		}
		
		tableModel = new SocializationRegulationTrcTableModel<SocializationRegulationTrc>(socializationTrc.getSocializationRegulationTrcs());
		tableModelCompliance = new SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc>(
				socializationTrc.getSocializationPICComplianceTrcs());
		tableModelFollowup = new SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc>(
				socializationTrc.getSocializationPICFollowupTrcs());
		
	//	tableApproval = regulationSocializationService.getDataApprovalBySocializationId(idLong);
		tableStatus = regulationSocializationService.getDataConfirmStatusBySocializationId(idLong);

	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void back() {
		try {
			facesUtil.redirect("/pages/regulationSocializationView/regulationSocializationView.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegulationSocializationViewDetailBean.logger = logger;
	}

	public SocializationTrc getSocializationTrc() {
		return socializationTrc;
	}

	public void setSocializationTrc(SocializationTrc socializationTrc) {
		this.socializationTrc = socializationTrc;
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

	public SocializationRegulationTmp[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(SocializationRegulationTmp[] selectedData) {
		this.selectedData = selectedData;
	}

	public SocializationPICComplianceTmp[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(SocializationPICComplianceTmp[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public SocializationPICFollowupTmp[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(SocializationPICFollowupTmp[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public SocializationRegulationTrcTableModel<SocializationRegulationTrc> getTableModel() {
		return tableModel;
	}

	public void setTableModel(SocializationRegulationTrcTableModel<SocializationRegulationTrc> tableModel) {
		this.tableModel = tableModel;
	}

	public SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(
			SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public List<StatusConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public List<SocializationApprovalVO> getTableApproval() {
		return tableApproval;
	}

	public void setTableApproval(List<SocializationApprovalVO> tableApproval) {
		this.tableApproval = tableApproval;
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

	public RegulationSocializationViewService getRegulationSocializationViewService() {
		return regulationSocializationViewService;
	}

	public void setRegulationSocializationViewService(
			RegulationSocializationViewService regulationSocializationViewService) {
		this.regulationSocializationViewService = regulationSocializationViewService;
	}

	public SocializationTrcService getSocializationTrcService() {
		return socializationTrcService;
	}

	public void setSocializationTrcService(SocializationTrcService socializationTrcService) {
		this.socializationTrcService = socializationTrcService;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
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

	public RegulationSocializationService getRegulationSocializationService() {
		return regulationSocializationService;
	}

	public void setRegulationSocializationService(RegulationSocializationService regulationSocializationService) {
		this.regulationSocializationService = regulationSocializationService;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}
	
	

}