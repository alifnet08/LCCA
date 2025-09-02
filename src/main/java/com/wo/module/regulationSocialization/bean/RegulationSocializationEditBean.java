package com.wo.module.regulationSocialization.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
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
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.faq.model.FaqDocument;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationSocialization.constant.RegulationSocializationConstants;
import com.wo.module.regulationSocialization.model.SocializationDocumentTmp;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTableModel;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTmp;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTableModel;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTmp;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;
import com.wo.module.regulationSocialization.model.SocializationRegulationTableModel;
import com.wo.module.regulationSocialization.model.SocializationRegulationTmp;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocialization.service.RegulationSocializationService;
import com.wo.module.regulationSocialization.service.SocializationPICFollowupTrcService;
import com.wo.module.regulationSocialization.service.SocializationTmpService;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.regulationSocializationApproval.service.RegulationSocializationApprovalService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class RegulationSocializationEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegulationSocializationBean.class);

	private SocializationTmp socializationTmp;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long oldCounterTypeId;
	private Long newCounterTypeId;

	private Long docTypeId;

	private Long docCategoryId;

	private Long docTopicId;
	
	private Boolean disabledFollowUpStatus;

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

	private SocializationRegulationTableModel<SocializationRegulationTmp> tableModel;

	private SocializationPICComplianceTableModel<SocializationPICComplianceTmp> tableModelCompliance;

	private SocializationPICFollowupTableModel<SocializationPICFollowupTmp> tableModelFollowup;
	
	private Integer lastSequenceOfRegulation;
	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private List<SocializationApprovalVO> tableApproval;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;

	private Integer indexDtlCompliance;

	private Integer indexDtlFollowup;

	private RegulationSocializationService regulationSocializationService;

	private SocializationTmpService socializationTmpService;
	
	private SocializationTmpService socializationTmpService2;

	private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private RegulationService regulationService;

	private UserService userService;

	private CounterTypeService counterTypeService;
	
	private SocializationPICFollowupTrcService socializationPICFollowupTrcService;

	private EmailTemplateService emailTemplateService;
	
	private RegulationSocializationApprovalService regulationSocializationApprovalService;
	
	public FacesUtil facesUtil;

	private String navigateSearch = ExternalRegulationConstants.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private String textWarningUpload;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FileUtil fileUtil;
	
	private List<SelectItem> searchType; 

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

		tableApproval = new ArrayList<SocializationApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
		
		setSearchType(new ArrayList<SelectItem>());
		for (String s : Arrays.asList("No", "Judul")) {
			getSearchType().add(new SelectItem(s));
		}
		
		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onChangeJenisKetentuan() {
		socializationTmp.getSocializationRegulationTmps().clear();
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
	
	public void onChangeDivision(int i) {		
		SocializationPICFollowupTmp data = socializationTmp.getSocializationPICFollowupTmps().get(i);
		data.setUser1(null);
		data.setUser2(null);
		data.setUser3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void clearPicDetail2(int i) {	
		socializationTmp.getSocializationPICFollowupTmps().get(i).setUser2(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void clearPicDetail3(int i) {	
		socializationTmp.getSocializationPICFollowupTmps().get(i).setUser3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
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
		
		PrimeFaces.current().executeScript("reInitSelect2();");
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
	
	public void onChangeFollowupStatus() {
		if(socializationTmp.getFollowUp() != null) {
			/*if("Y".equals(socializationTmp.getFollowUp())) {				
				if(socializationTmp.getCounterType() == null) {
					socializationTmp.setCounterType(new CounterType());
				}				
				
			} else if("N".equals(socializationTmp.getFollowUp())) {				
				if(socializationTmp.getSocializationPICFollowupTmps() != null) {
					socializationTmp.getSocializationPICFollowupTmps().clear();
				}
				counterTypeId = null;
				socializationTmp.setCounterType(null);
			}*/
			
			
			if("Y".equals(socializationTmp.getFollowUp())) {				
				if(socializationTmp.getCounterType() == null) {
					socializationTmp.setCounterType(new CounterType());
				}
			} else if("N".equals(socializationTmp.getFollowUp())) {				
				socializationTmp.setCounterType(null);
				counterTypeId = null;
				
				if (socializationTmp.getSocializationPICFollowupTmps() != null) {				
					for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
						SocializationPICFollowupTmp dtl = socializationTmp.getSocializationPICFollowupTmps().get(i);
						dtl.setTargetDate(null);
					}
				}
			}
			
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
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
			disabledFollowUpStatus = false;
			socializationTmp = new SocializationTmp();
			socializationTmp.setReminderStatus("REMINDER_ACTIVE");
			lastSequenceOfRegulation = 0;
			lastSequenceOfCompliance = 0;
			lastSequenceOfFollowup = 0;
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("KETENTUAN_EKSTERNAL");
			socializationTmp.setJenisKetentuan(pd);

			actionMode = Constants.ACTION_ADD;
			tableModel = new SocializationRegulationTableModel<SocializationRegulationTmp>(
					socializationTmp.getSocializationRegulationTmps());

			tableModelCompliance = new SocializationPICComplianceTableModel<SocializationPICComplianceTmp>(
					socializationTmp.getSocializationPICComplianceTmps());

			tableModelFollowup = new SocializationPICFollowupTableModel<SocializationPICFollowupTmp>(
					socializationTmp.getSocializationPICFollowupTmps());

			onAddNew();

			// onAddNewCompliance();
			if (socializationTmp.getSocializationPICComplianceTmps() == null
					|| socializationTmp.getSocializationPICComplianceTmps().size() == 0) {
				socializationTmp.setSocializationPICComplianceTmps(new ArrayList<SocializationPICComplianceTmp>());
				lastSequenceOfCompliance = 0;
			}

			SocializationPICComplianceTmp rt = new SocializationPICComplianceTmp();
			lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
			rt.setSequence(lastSequenceOfCompliance);
			
			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			rt.setUser(user);
			socializationTmp.getSocializationPICComplianceTmps().add(rt);
			tableModelCompliance.setWrappedData(socializationTmp.getSocializationPICComplianceTmps());
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
		disabledFollowUpStatus = false;
		Long idLong = Long.parseLong(editId);
		socializationTmp = socializationTmpService.findById(idLong);
		
		lastSequenceOfRegulation = 0;
		lastSequenceOfCompliance = 0;
		lastSequenceOfFollowup = 0;
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < socializationTmp.getSocializationDocumentTmps().size(); i++) {
			SocializationDocumentTmp ra = socializationTmp.getSocializationDocumentTmps().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
			
		}

		if (socializationTmp.getCounterType() != null) {
			counterTypeId = socializationTmp.getCounterType().getCounterTypeId();
			oldCounterTypeId = new Long(counterTypeId);
		}		
		
		if (socializationTmp.getSocializationRegulationTmps() != null) {
			lastSequenceOfRegulation = socializationTmp.getSocializationRegulationTmps().size();
			for (int i = 0; i < socializationTmp.getSocializationRegulationTmps().size(); i++) {
				SocializationRegulationTmp dtl = socializationTmp.getSocializationRegulationTmps().get(i);
				lastSequenceOfRegulation = lastSequenceOfRegulation + 1;
				dtl.setSequence(lastSequenceOfRegulation);
			}
		}		
		
		if (socializationTmp.getSocializationPICComplianceTmps() != null) {
			lastSequenceOfCompliance = socializationTmp.getSocializationPICComplianceTmps().size();
			for (int i = 0; i < socializationTmp.getSocializationPICComplianceTmps().size(); i++) {
				SocializationPICComplianceTmp dtl = socializationTmp.getSocializationPICComplianceTmps().get(i);
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);				
			}
		}
		
		
		
		if (socializationTmp.getSocializationPICFollowupTmps() != null) {
			lastSequenceOfFollowup = socializationTmp.getSocializationPICFollowupTmps().size();
			for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
				SocializationPICFollowupTmp dtl = socializationTmp.getSocializationPICFollowupTmps().get(i);
				lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
				dtl.setSequence(lastSequenceOfFollowup);				
				dtl.setOldTargetDate(dtl.getTargetDate());
				
				SocializationPICFollowupTrc picFollowupTrc = socializationPICFollowupTrcService.findById(dtl.getSocializationPicFollowupId());
				if(picFollowupTrc != null) {			
					if(picFollowupTrc.getFollowupStatus() != null) {
						disabledFollowUpStatus = true;
						dtl.setIsEditableTemp(false);
					} else {
						dtl.setIsEditableTemp(true);
					}
				} else {
					dtl.setIsEditableTemp(true);
				}
			}
		}

		tableModel = new SocializationRegulationTableModel<SocializationRegulationTmp>(
				socializationTmp.getSocializationRegulationTmps());
		tableModelCompliance = new SocializationPICComplianceTableModel<SocializationPICComplianceTmp>(
				socializationTmp.getSocializationPICComplianceTmps());
		tableModelFollowup = new SocializationPICFollowupTableModel<SocializationPICFollowupTmp>(
				socializationTmp.getSocializationPICFollowupTmps());

		tableApproval = regulationSocializationService.getDataApprovalBySocializationId(idLong);
		tableStatus = regulationSocializationService.getDataConfirmStatusBySocializationId(idLong);

	}
	
	public void onTargetDateChange(SelectEvent event) {
		String rowKey = String.valueOf((Integer) event.getComponent().getAttributes().get("seq"));
		int rowIdx = (Integer) event.getComponent().getAttributes().get("rowIdx");
		
		if(tableModelFollowup.getRowData(rowKey).getOldTargetDate() != null
				&& !tableModelFollowup.getRowData(rowKey).getOldTargetDate().equals(tableModelFollowup.getRowData(rowKey).getTargetDate()))
			tableModelFollowup.getRowData(rowKey).setRescheduleReason(null);

		PrimeFaces.current().ajax().update("form:dataTableFollowup:" + rowIdx + ":rescheduleReason");
	}

	public void onAddNew() {
		// Add one new car to the table:

		if (socializationTmp.getSocializationRegulationTmps() == null) {
			socializationTmp.setSocializationRegulationTmps(new ArrayList<SocializationRegulationTmp>());
			lastSequenceOfRegulation = 0;
		}  else {
			if(socializationTmp.getSocializationRegulationTmps().size() == 0) {
				lastSequenceOfRegulation = 0;
			}			
		} 

		SocializationRegulationTmp rt = new SocializationRegulationTmp();
		lastSequenceOfRegulation = lastSequenceOfRegulation + 1;
		rt.setSequence(lastSequenceOfRegulation);
		/*
		 * Regulation reg = new Regulation(); DocumentType dt = new DocumentType();
		 * DocumentCategory dc = new DocumentCategory(); DocumentTopic dto = new
		 * DocumentTopic(); reg.setDocumentType(dt); reg.setDocumentCategory(dc);
		 * reg.setDocumentTopic(dto); rt.setRegulation(reg);
		 */

		socializationTmp.getSocializationRegulationTmps().add(rt);

		tableModel.setWrappedData(socializationTmp.getSocializationRegulationTmps());
		
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");

	}

	public void onDeleteRow() {
		for (int i = 0; i < selectedData.length; i++) {
			socializationTmp.getSocializationRegulationTmps().remove(selectedData[i]);
		}
		
		if (socializationTmp.getSocializationRegulationTmps() == null
				|| socializationTmp.getSocializationRegulationTmps().size() == 0) {
			lastSequenceOfRegulation = 0;
		}

		tableModel.setWrappedData(socializationTmp.getSocializationRegulationTmps());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void onAddNewCompliance() {
		// Add one new car to the table:
		if (socializationTmp.getSocializationPICComplianceTmps() == null) {
			socializationTmp.setSocializationPICComplianceTmps(new ArrayList<SocializationPICComplianceTmp>());
			lastSequenceOfCompliance = 0;
		}  else {
			if(socializationTmp.getSocializationPICComplianceTmps().size() == 0) {
				lastSequenceOfCompliance = 0;
			}			
		} 

		SocializationPICComplianceTmp rt = new SocializationPICComplianceTmp();
		lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
		rt.setSequence(lastSequenceOfCompliance);
		// rt.setUser(new User());
		socializationTmp.getSocializationPICComplianceTmps().add(rt);
		tableModelCompliance.setWrappedData(socializationTmp.getSocializationPICComplianceTmps());

	}

	public void onDeleteRowCompliance() {
		for (int i = 0; i < selectedDataCompliance.length; i++) {
			socializationTmp.getSocializationPICComplianceTmps().remove(selectedDataCompliance[i]);
		}
		
		if (socializationTmp.getSocializationPICComplianceTmps() == null
				|| socializationTmp.getSocializationPICComplianceTmps().size() == 0) {
			lastSequenceOfCompliance = 0;
		}

		tableModelCompliance.setWrappedData(socializationTmp.getSocializationPICComplianceTmps());
	}

	public void onAddNewFollowup() {
		if (socializationTmp.getSocializationPICFollowupTmps() == null
				|| socializationTmp.getSocializationPICFollowupTmps().size() == 0) {
			socializationTmp.setSocializationPICFollowupTmps(new ArrayList<SocializationPICFollowupTmp>());
			lastSequenceOfFollowup = 0;
		}  else {
			if(socializationTmp.getSocializationPICFollowupTmps().size() == 0) {
				lastSequenceOfFollowup = 0;
			}			
		} 

		SocializationPICFollowupTmp rt = new SocializationPICFollowupTmp();
		lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
		rt.setSequence(lastSequenceOfFollowup);
		rt.setIsEditableTemp(true);
		socializationTmp.getSocializationPICFollowupTmps().add(rt);

		tableModelFollowup.setWrappedData(socializationTmp.getSocializationPICFollowupTmps());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");

	}

	public void onDeleteRowFollowup() {
		for (int i = 0; i < selectedDataFollowup.length; i++) {
			socializationTmp.getSocializationPICFollowupTmps().remove(selectedDataFollowup[i]);
		}
		
		if (socializationTmp.getSocializationPICFollowupTmps() == null
				|| socializationTmp.getSocializationPICFollowupTmps().size() == 0) {
			lastSequenceOfFollowup = 0;
		}

		tableModelFollowup.setWrappedData(socializationTmp.getSocializationPICFollowupTmps());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(socializationTmp.getJenisKetentuan().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationProvType") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(socializationTmp.getFollowUp())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationFollowup") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}else {
			if(socializationTmp.getFollowUp().equals(Constants.CONSTANT_NO) && StringUtils.isEmpty(socializationTmp.getNotes())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationNote") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}
		
		

		if (StringUtils.isEmpty(socializationTmp.getReminderStatus())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationReminderStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (socializationTmp.getSocializationRegulationTmps() == null
				|| socializationTmp.getSocializationRegulationTmps().size() == 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationRegTitle") + " "
					+ facesUtil.retrieveMessage("validateDetailMinOneData"));
			flag = true;
		} else if (socializationTmp.getSocializationRegulationTmps() != null
				&& socializationTmp.getSocializationRegulationTmps().size() > 0) {
			
			Set<Long> setRegulationTemp = new HashSet<Long>();
			
			for (int i = 0; i < socializationTmp.getSocializationRegulationTmps().size(); i++) {
				SocializationRegulationTmp dtl = (SocializationRegulationTmp) socializationTmp
						.getSocializationRegulationTmps().get(i);
				if (dtl.getRegulation() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationLinkJudul") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if (dtl.getRegulation()!= null && dtl.getRegulation().getRegulationId() != null) {
					if(!setRegulationTemp.add(dtl.getRegulation().getRegulationId())) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationLinkJudul") + " "
								+ facesUtil.retrieveMessage("errorDuplicate"));
						flag = true;
					}					
				}	
			}

		}

		if (socializationTmp.getSocializationPICComplianceTmps() == null
				|| socializationTmp.getSocializationPICComplianceTmps().size() == 0) {
			/*
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPICCompliance") + " "
					+ facesUtil.retrieveMessage("validateDetailMinOneData"));
			flag = true;
			*/
		} else if (socializationTmp.getSocializationPICComplianceTmps() != null
				&& socializationTmp.getSocializationPICComplianceTmps().size() > 0) {
			Set<Long> setUserComplianceTemp = new HashSet<Long>();
			
			for (int i = 0; i < socializationTmp.getSocializationPICComplianceTmps().size(); i++) {
				SocializationPICComplianceTmp dtl = (SocializationPICComplianceTmp) socializationTmp
						.getSocializationPICComplianceTmps().get(i);
				if (dtl.getUser() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPICCompliance")
							+ " " + facesUtil.retrieveMessage("formRegulationSocializationNIK") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if (dtl.getUser() != null && dtl.getUser().getUserId() != null) {
					if(!setUserComplianceTemp.add(dtl.getUser().getUserId()) ) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPICCompliance")
								+ " " + facesUtil.retrieveMessage("formRegulationSocializationNIK") + " "
								+ facesUtil.retrieveMessage("errorDuplicate"));
						flag = true;
					}					
				}	
			}

		}
		
		

		if ("Y".equals(socializationTmp.getFollowUp())) {
			if (counterTypeId == null || StringUtils.isEmpty(counterTypeId.toString())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationReminderType") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			if (socializationTmp.getSocializationPICFollowupTmps() == null
					|| socializationTmp.getSocializationPICFollowupTmps().size() == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPICAndWorkUnit") + " "
						+ facesUtil.retrieveMessage("validateDetailMinOneData"));
				flag = true;

			} else {
				for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
					boolean isErrReachMaxhit = false;
					SocializationPICFollowupTmp dtl = (SocializationPICFollowupTmp) socializationTmp
							.getSocializationPICFollowupTmps().get(i);
					
					if (dtl.getUser1() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPIC1") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					} 
					
					if (dtl.getTargetDate() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationTargetDate")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					} else {					
						if (dtl.getSocializationPicFollowupId() != null) {
							if (regulationSocializationService
									.hasReachedMaximumReschedule(dtl.getSocializationPicFollowupId())) {
								if(!dtl.getTargetDate().equals(dtl.getOldTargetDate())) {
									facesUtil.addErrMessage(facesUtil.
											retrieveMessage("formRegulationSocializationTargetDateErrorReachMaximum"));
									isErrReachMaxhit = true;
									flag = true;
								}
							}
						}
					}
					
					// target date is reschedule
					if (StringUtils.isNotBlank(socializationTmp.getLastUpdateBy())
							&& dtl.getOldTargetDate() != null
							&& !dtl.getTargetDate().equals(dtl.getOldTargetDate())
							&& StringUtils.isBlank(dtl.getRescheduleReason())
							&& !isErrReachMaxhit) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationRescheduleReason")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}
				}
			}
		}
		
		
		/*if ("N".equals(socializationTmp.getFollowUp())) {
			if (socializationTmp.getSocializationPICFollowupTmps() != null
					&& socializationTmp.getSocializationPICFollowupTmps().size() > 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPICAndWorkUnit")
						+ " " + facesUtil.retrieveMessage("validateNotRequired"));
				flag = true;
			}
		}*/

		if ("N".equals(socializationTmp.getFollowUp())) {			
			if (socializationTmp.getSocializationPICFollowupTmps() != null
					&& socializationTmp.getSocializationPICFollowupTmps().size() > 0) {
				for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
					SocializationPICFollowupTmp dtl = (SocializationPICFollowupTmp) socializationTmp
							.getSocializationPICFollowupTmps().get(i);
					
					if (dtl.getUser1() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPIC1") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;						
					}				
					
					
					
				}
			}
		}
		
//		comment/uncomment temporary due to user decision
//		Set<Long> setUserFollowupTemp = new HashSet<Long>();
//		for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
//			SocializationPICFollowupTmp dtl = (SocializationPICFollowupTmp) socializationTmp
//					.getSocializationPICFollowupTmps().get(i);
//			if (dtl.getUser1() != null && dtl.getUser1().getUserId() != null) {
//				if (!setUserFollowupTemp.add(dtl.getUser1().getUserId())) {
//					facesUtil.addErrMessage(facesUtil.retrieveMessage("formRegulationSocializationPICAndWorkUnit")
//							+ " " + facesUtil.retrieveMessage("formRegulationSocializationPIC1") + " "
//							+ facesUtil.retrieveMessage("errorDuplicate"));
//					flag = true;
//				}
//			}
//		} 

		return flag;
	}

	public void save() {
		try {

			if (!validate()) {
				
				if(socializationTmp.getSocializationDocumentTmps() == null || socializationTmp.getSocializationDocumentTmps().size() <= 0) {
					socializationTmp.setSocializationDocumentTmps(new ArrayList<SocializationDocumentTmp>());
				}
				socializationTmp.getSocializationDocumentTmps().clear();
				
				if(uploadFiles != null) {
					for (int i = 0; i < uploadFiles.size(); i++) {
						SocializationDocumentTmp doc = new SocializationDocumentTmp();
						UploadedFileWO uf = (UploadedFileWO) uploadFiles.get(i);
						doc.setSocializationTmp(socializationTmp);
						
						doc.setAttachmentFile(uf.getFileName());
						
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);
						
						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						socializationTmp.getSocializationDocumentTmps().add(doc);
					}
				}

				if (StringUtils.isEmpty(socializationTmp.getJenisKetentuan().getParameterDtlCode())) {
					ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(
							socializationTmp.getJenisKetentuan().getParameterDtlCode());
					socializationTmp.setJenisKetentuan(pd);
				}

				if (counterTypeId != null) {
					CounterType ct = counterTypeService.findById(counterTypeId);
					socializationTmp.setCounterType(ct);
				}

				socializationTmp.setStatus(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED);

				if (socializationTmp.getSocializationRegulationTmps() != null) {
					for (int i = 0; i < socializationTmp.getSocializationRegulationTmps().size(); i++) {
						SocializationRegulationTmp dtl = socializationTmp.getSocializationRegulationTmps().get(i);
						if(i==0) {
							dtl.setPrimaryFlag(Constants.CONSTANT_YES);
						}else {
							dtl.setPrimaryFlag(Constants.CONSTANT_NO);
						}
						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
						dtl.setSocialization(socializationTmp);
					}
				}

				if (socializationTmp.getSocializationPICComplianceTmps() != null) {
					for (int i = 0; i < socializationTmp.getSocializationPICComplianceTmps().size(); i++) {
						SocializationPICComplianceTmp dtl = socializationTmp.getSocializationPICComplianceTmps().get(i);
						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
						dtl.setSocializationTmp(socializationTmp);
					}
				}

				if (socializationTmp.getSocializationPICFollowupTmps() != null) {
					for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
						SocializationPICFollowupTmp dtl = socializationTmp.getSocializationPICFollowupTmps().get(i);

						if (oldCounterTypeId != null && counterTypeId != null) {
							if (oldCounterTypeId.longValue() != counterTypeId.longValue()) {
								if (dtl.getSocializationPicFollowupId() != null) {
									if (!regulationSocializationService
											.hasReachedMaximumReschedule(dtl.getSocializationPicFollowupId())) {
										dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
										dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
									}
								}
							}
						}

						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
						dtl.setSocialization(socializationTmp);
					}
				}
				
				if (socializationTmp.getSocializationId() != null) {

					SocializationTmp socializationDb = socializationTmpService.findById(socializationTmp.getSocializationId());
					socializationTmpService.updateDataAlreadyExist(socializationTmp, socializationDb, facesUtil.retrieveUserLogin());
					/*socializationTmp.setLastUpdateBy(facesUtil.retrieveUserLogin());
					socializationTmp.setLastUpdateDate(new Timestamp(new Date().getTime()));
					socializationTmp.setDelId(new Long(0));
					socializationTmp.setEnabledFlag(Constants.CONSTANT_YES);
					socializationTmpService.update(socializationTmp);
					socializationTmp.setSocializationId(null);*/
					
				} else {
					socializationTmp.setCreatedBy(facesUtil.retrieveUserLogin());
					socializationTmp.setCreationDate(new Timestamp(new Date().getTime()));
					socializationTmp.setDelId(new Long(0));
					socializationTmp.setEnabledFlag(Constants.CONSTANT_YES);
					socializationTmpService.save(socializationTmp);
				}
				
				
				SocializationTmp socializationTmpNew = socializationTmpService2.findById(socializationTmp.getSocializationId());
				
				regulationSocializationApprovalService.processApprove(socializationTmpNew, "Approve By System", facesUtil.retrieveUserLogin(),
						ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE,ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED);
				
				
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail();
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
				
				facesUtil.redirect("/pages/regulationSocialization/regulationSocialization.faces");

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
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
	
	public String replaceAll(String INPUT,String REGEX,String REPLACE) {
		Pattern p = Pattern.compile(REGEX);
	      //get a matcher object
	      Matcher m = p.matcher(INPUT);
	      INPUT = m.replaceAll(REPLACE);
		return INPUT;
	}
	
	@SuppressWarnings("unused")
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_SOSIALISASI");
			String emailSubject = replaceAll(emailTemplate.getEmailSubject(),"counter_type","NOTIFICATION");
				   emailSubject = replaceAll(emailSubject,"regulation_title_in",socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameIn());
				   emailSubject = replaceAll(emailSubject,"regulation_title_en",socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameEn());
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc = "";
			String emailCcCompliance = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			if (socializationTmp.getReminderStatus()!=null && socializationTmp.getReminderStatus().equals(Constants.REMINDER_ACTIVE) && 
		    		socializationTmp.getFollowUp() != null && socializationTmp.getFollowUp().equals(Constants.CONSTANT_YES) &&
		    		socializationTmp.getSocializationPICFollowupTmps() != null && socializationTmp.getSocializationPICFollowupTmps().size() > 0
		    		) {
				for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
					SocializationPICFollowupTmp tmp = socializationTmp.getSocializationPICFollowupTmps().get(i);
					emailContent = emailTemplate.getEmailContent().replaceAll("target_date", tmp.getTargetDate()!=null?sdf.format(tmp.getTargetDate()):"");
					emailContent = emailContent.replaceAll("regulation_title_in", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameIn());
					emailContent = emailContent.replaceAll("regulation_title_en", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameEn());
					emailContent = emailContent.replaceAll("document_number", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getDocumentNo());
					emailContent = emailContent.replaceAll("published_date", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getPublishedDate()!=null?sdf.format(socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getPublishedDate()):"");
					emailContent = emailContent.replaceAll("effective_date", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getEffectiveDate()!=null?sdf.format(socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getEffectiveDate()):"");
					String token = "";
					String urlLink = "";
					String menuId = "";
					if (socializationTmp.getFollowUp() != null && socializationTmp.getFollowUp().equals(Constants.CONSTANT_YES)) {
						token = Constants.encryptString(tmp.getSocializationPicFollowupId().toString());
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_SOCIALIZATION);
						urlLink = pdHostName.getNameIn().concat("pages/socializationFE/socializationFEEdit.faces?token="+token+"&menuId="+menuId);
					} /*else if (socializationTmp.getFollowUp() != null && socializationTmp.getFollowUp().equals("N")) {
						token = Constants.encryptString(socializationTmp.getSocializationId().toString());
						menuId = Constants.encryptString(Constants.MENU_ID_SOCIALIZATION_VIEW);
						urlLink = pdHostName.getNameIn().concat("pages/regulationSocializationView/regulationSocializationViewDetail.faces?token="+token+"&menuId="+menuId);
					  }*/
					emailContent = emailContent.replaceAll("url_link", urlLink);
					emailCc = "";
					emailCcCompliance = "";
					
					if (socializationTmp != null) {
						if (socializationTmp.getSocializationPICComplianceTmps() != null && socializationTmp.getSocializationPICComplianceTmps().size() > 0) {
							for (SocializationPICComplianceTmp spct : socializationTmp.getSocializationPICComplianceTmps()) {
								User ue = null;
								if (spct.getUser() != null) {
									ue = userService.findById(spct.getUser().getUserId());
									if (ue != null && StringUtils.isNotBlank(ue.getEmail())) {
										emailCcCompliance = StringUtils.isNotEmpty(emailCcCompliance)?emailCcCompliance.concat(",").concat(ue.getEmail()):emailCcCompliance.concat(ue.getEmail());
									}
								}
							}
						}
					}
					
//					for(int x=0;x<socializationTmp.getCounterType().getDetails().size();x++) {
					if (socializationTmp != null) {
						if (socializationTmp.getCounterType() != null && socializationTmp.getCounterType().getDetails() != null && socializationTmp.getCounterType().getDetails().size() > 0) {
							CounterTypeDtl cd = socializationTmp.getCounterType().getDetails().get(0);
							emailTo = cd.getEmailTo();
							emailCc1 = cd.getEmailCc1();
							emailCc2 = cd.getEmailCc2();
							
							if(emailTo.equals(Constants.REMINDER_PIC1)) {
								emailTo = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC2)) {
								emailTo = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC3)) {
								emailTo = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
								emailCc1 = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
								emailCc1 = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
								emailCc1 = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
								emailCc2 = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
								emailCc2 = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
								emailCc2 = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							
							
//					        emailExecutor.execute(new Runnable() {
//					            @Override
//					            public void run() {
//									
//					            	try {
//										//CallApiManager.sendEmail(subject, content, to,cc, parameterDetailService);
//										CallApiManager.sendEmailAPI(to,cc, subject,
//												content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//					            }
//					        });
					        
							
							
						} else {
							emailTo = tmp.getUser1() != null ? tmp.getUser1().getEmail() : "";
							emailCc1 = tmp.getUser2() != null ? tmp.getUser2().getEmail() : "";
							emailCc2 = tmp.getUser3() != null ? tmp.getUser3().getEmail() : "";
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						if (StringUtils.isNotEmpty(emailCcCompliance)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCcCompliance):emailCc.concat(emailCcCompliance);
						}
						
						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to,cc, subject,
								content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
					}
					
					
				}
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	/*
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject();
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String documentNumberTemp = "";
			String documentTitleTemp = "";
			for (int i = 0; i < socializationTmp.getSocializationRegulationTmps().size(); i++) {
				SocializationRegulationTmp temp = socializationTmp.getSocializationRegulationTmps().get(0);
				documentNumberTemp = temp.getRegulation().getDocumentNo() != null ? " - " + temp.getRegulation().getDocumentNo() : "";
				documentTitleTemp = temp.getRegulation().getName() != null ? " - " + temp.getRegulation().getName() : "";
			}
			
			emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, "Sosialisasi"
					+ documentNumberTemp + documentTitleTemp);
						
			ParameterDetail paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER);
			emailTo = paramEmail.getNameIn();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			//final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;
						
			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
						
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	*/
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/regulationSocialization/regulationSocialization.faces");
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

	public RegulationSocializationService getRegulationSocializationService() {
		return regulationSocializationService;
	}

	public void setRegulationSocializationService(RegulationSocializationService regulationSocializationService) {
		this.regulationSocializationService = regulationSocializationService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
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

	public List<javax.faces.model.SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<javax.faces.model.SelectItem> provTypes) {
		this.provTypes = provTypes;
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegulationSocializationEditBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
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

	public SelectorInfo getSelectorJdlPeraturan() {
		return selectorJdlPeraturan;
	}

	public void setSelectorJdlPeraturan(SelectorInfo selectorJdlPeraturan) {
		this.selectorJdlPeraturan = selectorJdlPeraturan;
	}

	public List<SelectItem> getTrackCodes() {
		return trackCodes;
	}

	public void setTrackCodes(List<SelectItem> trackCodes) {
		this.trackCodes = trackCodes;
	}

	public RegulationTrackRecord getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecord selectedRow) {
		this.selectedRow = selectedRow;
	}

	public SocializationRegulationTmp[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(SocializationRegulationTmp[] selectedData) {
		this.selectedData = selectedData;
	}

	public SocializationTmp getSocializationTmp() {
		return socializationTmp;
	}

	public void setSocializationTmp(SocializationTmp socializationTmp) {
		this.socializationTmp = socializationTmp;
	}

	public SocializationRegulationTableModel<SocializationRegulationTmp> getTableModel() {
		return tableModel;
	}

	public void setTableModel(SocializationRegulationTableModel<SocializationRegulationTmp> tableModel) {
		this.tableModel = tableModel;
	}

	public SocializationTmpService getSocializationTmpService() {
		return socializationTmpService;
	}

	public void setSocializationTmpService(SocializationTmpService socializationTmpService) {
		this.socializationTmpService = socializationTmpService;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public SocializationPICComplianceTableModel<SocializationPICComplianceTmp> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			SocializationPICComplianceTableModel<SocializationPICComplianceTmp> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public SocializationPICComplianceTmp[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(SocializationPICComplianceTmp[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
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

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public SelectorInfo getSelectorFollowup() {
		return selectorFollowup;
	}

	public void setSelectorFollowup(SelectorInfo selectorFollowup) {
		this.selectorFollowup = selectorFollowup;
	}

	public SocializationPICFollowupTmp[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(SocializationPICFollowupTmp[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public SocializationPICFollowupTableModel<SocializationPICFollowupTmp> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(
			SocializationPICFollowupTableModel<SocializationPICFollowupTmp> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
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

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<StatusConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public List<SocializationApprovalVO> getTableApproval() {
		return tableApproval;
	}

	public void setTableApproval(List<SocializationApprovalVO> tableApproval) {
		this.tableApproval = tableApproval;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("jdlPeraturanDialog", widgetVar)) {
			Regulation reg = (Regulation) selectedItem;
			//Regulation reg = regulationService.findById(((java.math.BigInteger) objects[0]).longValue());
			//Regulation reg = regulationService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			socializationTmp.getSocializationRegulationTmps().get(indexDtl).setRegulation(reg);

			tableModel.setWrappedData(socializationTmp.getSocializationRegulationTmps());
		}

		else if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			socializationTmp.getSocializationPICComplianceTmps().get(indexDtlCompliance).setUser(user);

			tableModelCompliance.setWrappedData(socializationTmp.getSocializationPICComplianceTmps());
		}

		else if (StringUtils.equals("divisionDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup)
					//.setDivisionId(((java.math.BigInteger) objects[0]).longValue());
					  .setDivisionId(MathUtil.returnIdObjectToLong(objects[0]));
			;

			tableModelFollowup.setWrappedData(socializationTmp.getSocializationPICFollowupTmps());
		}

		else if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup).setUser1(user);
			/*socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/

			User user2 = userService.getUserByNik(user.getPukNik());
			if (user2 != null) {
				socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup).setUser2(user2);

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup).setUser3(user3);
				}
			}

			tableModelFollowup.setWrappedData(socializationTmp.getSocializationPICFollowupTmps());
		}

		else if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup).setUser2(user);
			/*socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/
			User user3 = userService.getUserByNik(user.getPukNik());
			if (user3 != null) {
				socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup).setUser3(user3);
			}

			tableModelFollowup.setWrappedData(socializationTmp.getSocializationPICFollowupTmps());
		}

		else if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User user = userService.findById(((java.math.BigInteger) objects[0]).longValue());
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup).setUser3(user);
			/*socializationTmp.getSocializationPICFollowupTmps().get(indexDtlFollowup)
					.setDivisionId(user.getDivisionId());*/

			tableModelFollowup.setWrappedData(socializationTmp.getSocializationPICFollowupTmps());
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public Long getOldCounterTypeId() {
		return oldCounterTypeId;
	}

	public void setOldCounterTypeId(Long oldCounterTypeId) {
		this.oldCounterTypeId = oldCounterTypeId;
	}

	public Long getNewCounterTypeId() {
		return newCounterTypeId;
	}

	public void setNewCounterTypeId(Long newCounterTypeId) {
		this.newCounterTypeId = newCounterTypeId;
	}

	public List<SelectItem> getSearchType() {
		return searchType;
	}

	public void setSearchType(List<SelectItem> searchType) {
		this.searchType = searchType;
	}

	public Integer getLastSequenceOfRegulation() {
		return lastSequenceOfRegulation;
	}

	public void setLastSequenceOfRegulation(Integer lastSequenceOfRegulation) {
		this.lastSequenceOfRegulation = lastSequenceOfRegulation;
	}

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}

	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}

	public Integer getLastSequenceOfFollowup() {
		return lastSequenceOfFollowup;
	}

	public void setLastSequenceOfFollowup(Integer lastSequenceOfFollowup) {
		this.lastSequenceOfFollowup = lastSequenceOfFollowup;
	}

	public SocializationPICFollowupTrcService getSocializationPICFollowupTrcService() {
		return socializationPICFollowupTrcService;
	}

	public void setSocializationPICFollowupTrcService(SocializationPICFollowupTrcService socializationPICFollowupTrcService) {
		this.socializationPICFollowupTrcService = socializationPICFollowupTrcService;
	}

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public RegulationSocializationApprovalService getRegulationSocializationApprovalService() {
		return regulationSocializationApprovalService;
	}

	public void setRegulationSocializationApprovalService(
			RegulationSocializationApprovalService regulationSocializationApprovalService) {
		this.regulationSocializationApprovalService = regulationSocializationApprovalService;
	}

	public SocializationTmpService getSocializationTmpService2() {
		return socializationTmpService2;
	}

	public void setSocializationTmpService2(SocializationTmpService socializationTmpService2) {
		this.socializationTmpService2 = socializationTmpService2;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
	
	
}