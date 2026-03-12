package com.wo.module.internalRegulation.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterType;
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
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.externalRegulation.model.RegulationProposerUnit;
import com.wo.module.externalRegulation.model.RegulationProposerUnitTableModel;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.model.RegulationTrackRecordTableModel;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.internalRegulation.service.InternalRegulationService;
import com.wo.module.internalRegulationApproval.service.InternalRegulationApprovalService;
import com.wo.module.logActivity.model.LogActivity;
import com.wo.module.logActivity.service.LogActivityService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class InternalRegulationEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = -2257369434190860786L;
	static Logger logger = Logger.getLogger(InternalRegulationEditBean.class);
	private String navigateSearch = InternalRegulationConstants.NAVIGATE_SEARCH;

	private LogActivity logActivity;	
	private LogActivityService logActivityService;
	private InternalRegulationService internalRegulationService;
	private InternalRegulationApprovalService internalRegulationApprovalService;
	private RegulationService regulationService;
	private RegulationService regulationService2;
	private EmailTemplateService emailTemplateService;
	private DocumentTypeService documentTypeService;
	private DocumentCategoryService documentCategoryService;
	private DocumentTopicService documentTopicService;
	private CounterTypeService counterTypeService;	
	private UserService userService;
	
	private Regulation regulation;
	private RegulationTrackRecord selectedRow;
	private Regulation regulationValided;

	private String typeReviewDateParamCode;
	private String actionMode;
	private String editedId;
	private String checkValidate;
	private String textWarningUpload;
	private Long docTypeId;
	private Long docCategoryId;
	private Long docTopicId;
	private Long counterTypeId;
	private Boolean isViewOnly;
	private Boolean isShowPanelTipeTanggalUlasan;
	private boolean tanggalUlasanPeraturanSementaraFlag;
	private Integer indexDtl;
	private Integer effectiveYear;
	private Integer lastSequenceOfDtl;
	private Integer indexDtlPuPic;
	private Integer lastSequenceOfPuDtl;
	
	private List<SelectItem> provTypes;
	private List<SelectItem> trackCodes;
	private List<SelectItem> docTypes;
	private List<SelectItem> docCategories;
	private List<SelectItem> docTopics;
	private List<SelectItem> directorateLists;
	private List<SelectItem> unitPenerbitList;
	private List<SelectItem> emailReminderList;
	private List<SelectItem> typeReviewDateList;
	
	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesPeraturanId;
	private List<UploadedFileWO> uploadedFilesPeraturanEn;
	private List<UploadedFileWO> uploadedFilesFAQ;
	private List<UploadedFileWO> uploadedFilesLampiran;
	private List<UploadedFileWO> uploadedFilesOther;	
	private List<UploadedFileWO> uploadedFilesFormRingkasan;

	private SelectorInfo selectorJdlPeraturan;
	private SelectorInfo selectorPic;
	private SelectorInfo selectorPuk;
	private SelectorInfo selectorProposerUnit;
	
	private RegulationTrackRecord[] selectedData;

	private RegulationTrackRecordTableModel<RegulationTrackRecord> tableModel;
	
	private RegulationProposerUnit[] selectedDataProposerUnit;
	private RegulationProposerUnitTableModel<RegulationProposerUnit> tableModelProposerUnit;

	private HashMap<String, String> recordMap = new HashMap<String, String>();	
	private HashMap<String, String> reviewDateMonth = new HashMap<String,String>();

	public FacesUtil facesUtil;
	private FileUtil fileUtil;

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
		selectProvType();
		selectTrackCode();
		selectDocType();
		selectDocCategory();
		selectDirectorateList();
		selectUnitPenerbitList();
		selectEmailReminder();
		selectDocTopic();
		selectTypeReviewDate();

		selectorPic = InternalRegulationConstants.buildSelectorUser();
		selectorPuk = InternalRegulationConstants.buildSelectorUser();
		
		selectorJdlPeraturan = InternalRegulationConstants.buildSelectorJdlPeraturan(facesUtil);

		selectorProposerUnit = InternalRegulationConstants.buildSelectorProposerUnit();
		
		isShowPanelTipeTanggalUlasan = false;
		
		checkNewOrEdit();
		regulationValided = new Regulation();
		checkValidate = "";
		fileUtil = FileUtil.getInstance();
		
		try {
			effectiveYear = Integer.valueOf(((ParameterDetail) parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_INTERNAL_REGULATION_EXPIRED_YEAR)).getName());
		} catch (Exception ex) {	
			effectiveYear = 3;
		}
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void selectTypeReviewDate() {
		typeReviewDateList = new ArrayList<SelectItem>();
		
		try {
			List<ParameterDetail> getTypeReviewDateList = parameterDetailService.getParameterDetailByParamCode(
					ParameterDetail.PARAM_CODE_INTERNAL_REGULATION_REVIEW_DATE);
			for(ParameterDetail pd : getTypeReviewDateList) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				typeReviewDateList.add(si);
				reviewDateMonth.put(pd.getParameterDtlCode(), pd.getName());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private void selectEmailReminder() {
		emailReminderList = new ArrayList<SelectItem>();
		
		try {
			List<CounterType> getEmailReminder = counterTypeService.getAllCounterType();
			for (CounterType ct : getEmailReminder) {
				SelectItem si = new SelectItem();
				si.setLabel(ct.getCounterTypeName());
				si.setValue(ct.getCounterTypeId());
				
				emailReminderList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectDirectorateList() {
		directorateLists = new ArrayList<SelectItem>();
		try {
			/*List<ParameterDetail> getDirectorate = parameterDetailService.getParameterDetailByParamCodeOrDtlCode(ParameterDetail.PARAM_CODE_DIRECTORATE, null);
			for (ParameterDetail pd : getDirectorate) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				directorateLists.add(si);
			}*/
			List<String> getDirectorate = userService.getAllDirectorate();
			for (String pd : getDirectorate) {
				SelectItem si = new SelectItem();
				si.setLabel(pd);
				si.setValue(pd);
				
				directorateLists.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectUnitPenerbitList() {
		unitPenerbitList = new ArrayList<SelectItem>();
		try {
			/*List<ParameterDetail> getUnitPublish = parameterDetailService.getParameterDetailByParamCodeOrDtlCode(ParameterDetail.PARAM_CODE_PUBLISHER_UNIT, null);
			for (ParameterDetail pd : getUnitPublish) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				unitPenerbitList.add(si);
			}*/
			List<String> getDivision = userService.getDivisionByDirectorate("%");
			unitPenerbitList = new ArrayList<SelectItem>();
			for (String pd : getDivision) {
				SelectItem si = new SelectItem();
				si.setLabel(pd);
				si.setValue(pd);
				
				unitPenerbitList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updatePublisherUnit() {
		if (regulation.getDirectorate() != null) {
			List<String> getDivision = userService.getDivisionByDirectorate(regulation.getDirectorate());
			if(unitPenerbitList.size()>0){
				unitPenerbitList.clear();
			}
			
			for (String pd : getDivision) {
				SelectItem si = new SelectItem();
				si.setLabel(pd);
				si.setValue(pd);
				
				unitPenerbitList.add(si);
			}
			PrimeFaces.current().executeScript("reInitSelect2();");
		}
	}
	
	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL);
			//for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				provTypes.add(si);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectTrackCode() {
		trackCodes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TRACK_RECORD");
			for (int i = 0; i < pd.size(); i++) {
				ParameterDetail paramDtl = (ParameterDetail) pd.get(i);
				SelectItem si = new SelectItem();				
				if(paramDtl.getParameterDtlCode() !=null && 
				     (paramDtl.getParameterDtlCode().equals("RECORD_CHANGE") || 
				    	paramDtl.getParameterDtlCode().equals("RECORD_NEW_REGULATION") || 
				    	paramDtl.getParameterDtlCode().equals("RECORD_REVOKE"))) {
					si.setLabel(((ParameterDetail) pd.get(i)).getName());
					si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
					trackCodes.add(si);
					recordMap.put(pd.get(i).getParameterDtlCode(),pd.get(i).getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocType() {
		docTypes = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
							InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)), 0, Integer.MAX_VALUE,
					null, null);
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
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE,
							InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)), 0, Integer.MAX_VALUE,
					null, null);
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
		/*try {
			List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(InternalRegulationConstants.JENIS_KETENTUAN_INTERNAL, 
							InternalRegulationConstants.SYSTEM_CODE_KETENTUAN_INTERNAL, docCategoryId)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic) pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic) pd.get(i)).getDocumentTopicId());
				docTopics.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, ParameterDetail.PARAM_DET_CODE_KETENTUAN_INTERNAL)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic) pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic) pd.get(i)).getDocumentTopicId());
				docTopics.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setExpiredDateValue(SelectEvent event) {
		if (regulation.getEffectiveDate() != null) {
			DocumentType docType = documentTypeService.findById(docTypeId);			
			if(docType.getDocumentType().trim().equalsIgnoreCase("Peraturan Sementara")) {
				// no action
			}else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(regulation.getEffectiveDate());
				cal.add(Calendar.YEAR, effectiveYear);
				regulation.setExpiredDate(cal.getTime());
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
			regulation = new Regulation();
			List<RegulationTrackRecord> trList = new ArrayList<RegulationTrackRecord>();
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL);
			regulation.setJenisKetentuan(pd);
			regulation.setCounterType(new CounterType());
			//regulation.setDirectorate(new ParameterDetail());
			//regulation.setPublisherUnit(new ParameterDetail());

			uploadedFilesPeraturanId = new ArrayList<UploadedFileWO>();
			uploadedFilesPeraturanEn = new ArrayList<UploadedFileWO>();
			uploadedFilesFAQ = new ArrayList<UploadedFileWO>();
			uploadedFilesLampiran = new ArrayList<UploadedFileWO>();
			uploadedFilesOther = new ArrayList<UploadedFileWO>();
			uploadedFilesFormRingkasan = new ArrayList<UploadedFileWO>();
			
			RegulationTrackRecord rt = new RegulationTrackRecord();
			lastSequenceOfDtl = 1;
			rt.setSequence(lastSequenceOfDtl);
			trList.add(rt);
			regulation.setRegulationTrackRecords(trList);

			actionMode = Constants.ACTION_ADD;
			tableModel = new RegulationTrackRecordTableModel<RegulationTrackRecord>(
					regulation.getRegulationTrackRecords());
			
			tableModelProposerUnit = new RegulationProposerUnitTableModel<RegulationProposerUnit>(
					regulation.getRegulationProposerUnits());
			
			facesUtil.setSessionAttribute("token", null);
			
			setTanggalUlasanPeraturanSementaraFlag(false);
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
		regulation = regulationService.findById(idLong);
		regulation.setNameInOld(regulation.getNameIn());
		
		if(regulation.getPublishedDate()!=null) {
			regulation.setPublishedDateStr(sdf.format(regulation.getPublishedDate()));
		}
		
		if(regulation.getEffectiveDate()!=null) {
			regulation.setEffectiveDateStr(sdf.format(regulation.getEffectiveDate()));
		}
		
		if(regulation.getExpiredDate()!=null) {		
			if(regulation.getDocumentType().getDocumentType().trim().equalsIgnoreCase("Peraturan Sementara")) {
				setTanggalUlasanPeraturanSementaraFlag(true);
				onChangeDocType();
			}else {
				setTanggalUlasanPeraturanSementaraFlag(false);
				regulation.setExpiredDateStr(sdf.format(regulation.getExpiredDate()));
			}
		}
		
		if(regulation.getDocumentType()!=null && regulation.getDocumentType().getDocumentTypeId()!=null) {
			docTypeId = regulation.getDocumentType().getDocumentTypeId();
		}
		if(regulation.getDocumentCategory()!=null && regulation.getDocumentCategory().getDocumentCategoryId()!=null) {
			docCategoryId = regulation.getDocumentCategory().getDocumentCategoryId();
		}
		if (regulation.getCounterType() != null && regulation.getCounterType().getCounterTypeId() != null) {
			counterTypeId = regulation.getCounterType().getCounterTypeId();
		}
		if(regulation.getDocumentTopic()!=null && regulation.getDocumentTopic().getDocumentTopicId()!=null) {
			docTopicId = regulation.getDocumentTopic().getDocumentTopicId();
		}
		
		if(regulation.getTypeReviewDate() != null && regulation.getTypeReviewDate().getParameterDtlCode() != null) {
			typeReviewDateParamCode = regulation.getTypeReviewDate().getParameterDtlCode();
		}
	    
		if (regulation.getPic() != null) {
			regulation.setPicNameTemp(regulation.getPic().getName());
		}
		if (regulation.getPuk() != null) {
			regulation.setPukNameTemp(regulation.getPuk().getName());
		}
		
		lastSequenceOfDtl = 0;
		
		if (regulation.getRegulationTrackRecords() != null) {
			lastSequenceOfDtl = regulation.getRegulationTrackRecords().size();
			for (int i = 0; i < regulation.getRegulationTrackRecords().size(); i++) {
				RegulationTrackRecord dtl = regulation.getRegulationTrackRecords().get(i);
				lastSequenceOfDtl = lastSequenceOfDtl + 1;
				dtl.setSequence(lastSequenceOfDtl);
				dtl.setTrackNameTemp(dtl.getTrackCode());
			}
		}
		
		tableModel = new RegulationTrackRecordTableModel<RegulationTrackRecord>(regulation.getRegulationTrackRecords());
		
		uploadedFilesPeraturanId = new ArrayList<UploadedFileWO>();
		uploadedFilesPeraturanEn = new ArrayList<UploadedFileWO>();
		uploadedFilesFAQ = new ArrayList<UploadedFileWO>();
		uploadedFilesLampiran = new ArrayList<UploadedFileWO>();
		uploadedFilesOther = new ArrayList<UploadedFileWO>();
		uploadedFilesFormRingkasan = new ArrayList<UploadedFileWO>();
		
		for(int i=0; i<regulation.getRegulationAttachments().size();i++) {
			RegulationAttachment ra= regulation.getRegulationAttachments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			
			if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals("ATTACHMENT_REGULATIONS_IN")) {
				uploadedFilesPeraturanId.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals("ATTACHMENT_FAQ")) {
				uploadedFilesFAQ.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals("ATTACHMENT_LAMPIRAN")) {
				uploadedFilesLampiran.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals("ATTACHMENT_OTHER")) {
				uploadedFilesOther.add(uf);
			}
			else if (ra.getAttachmentCode() != null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_SUMMARY)) {
				uploadedFilesFormRingkasan.add(uf);
			}
		}
		
		for (int i = 0; i < regulation.getRegulationTrackRecords().size(); i++) {
			RegulationTrackRecord rtr = (RegulationTrackRecord) regulation.getRegulationTrackRecords().get(i);
			if (rtr.getRegulationLinkId() != null) {
//				Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
//				String name = "";

				Regulation r = regulationService.findById(rtr.getRegulationLinkId());
//				if (locale != null && locale.equals(locale.ENGLISH)) {
//					name = r.getNameEn();
//				} else {
//					name = r.getNameIn();
//				}
//				rtr.setRegulationLinkName(name);
				rtr.setRegulationLinkName(r.getDocumentNo());
			}
		}
		
		lastSequenceOfPuDtl = 0;
		if (regulation.getRegulationProposerUnits() != null) {
			lastSequenceOfPuDtl = regulation.getRegulationProposerUnits().size();
			for (int i = 0; i < regulation.getRegulationProposerUnits().size(); i++) {
				RegulationProposerUnit dtl = regulation.getRegulationProposerUnits().get(i);
				lastSequenceOfDtl = lastSequenceOfDtl + 1;
				dtl.setSequence(lastSequenceOfDtl);
				dtl.setPicNameTemp(dtl.getPic() !=null ? dtl.getPic().getName():null);
				dtl.setPukNameTemp(dtl.getPuk() !=null ? dtl.getPuk().getName():null);
			}
		}
		
		tableModelProposerUnit = new RegulationProposerUnitTableModel<RegulationProposerUnit>(regulation.getRegulationProposerUnits());
		
		
		PrimeFaces.current().executeScript("reInitSelect2();");

	}

	public void onAddNew() {
		List<RegulationTrackRecord> listRt = new ArrayList<RegulationTrackRecord>();
		RegulationTrackRecord rt = new RegulationTrackRecord();
		lastSequenceOfDtl = lastSequenceOfDtl + 1;
		rt.setSequence(lastSequenceOfDtl);
		if (regulation.getRegulationTrackRecords() == null) {
			listRt.add(rt);
			regulation.setRegulationTrackRecords(listRt);
		} else {
			regulation.getRegulationTrackRecords().add(rt);
		}

		tableModel.setWrappedData(regulation.getRegulationTrackRecords());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onDeleteRow() {
		if(selectedData !=null) {
			for (int i = 0; i < selectedData.length; i++) {
				regulation.getRegulationTrackRecords().remove(selectedData[i]);
			}
		}
		
		if (regulation.getRegulationTrackRecords() == null
				|| regulation.getRegulationTrackRecords().size() == 0) {
			lastSequenceOfDtl = 0;
		}
		
		tableModel.setWrappedData(regulation.getRegulationTrackRecords());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void handleFileUploadPeraturanId(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesPeraturanId = uploadedFilesPeraturanId == null ? new ArrayList<UploadedFileWO>()
				: uploadedFilesPeraturanId;
		uploadedFilesPeraturanId.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_INTERNAL,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void handleFileUploadFAQ(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesFAQ = uploadedFilesFAQ == null ? new ArrayList<UploadedFileWO>() : uploadedFilesFAQ;
		uploadedFilesFAQ.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_INTERNAL,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void handleFileUploadLampiran(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesLampiran = uploadedFilesLampiran == null ? new ArrayList<UploadedFileWO>() : uploadedFilesLampiran;
		uploadedFilesLampiran.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_INTERNAL,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void handleFileUploadOther(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesOther = uploadedFilesOther == null ? new ArrayList<UploadedFileWO>() : uploadedFilesOther;
		uploadedFilesOther.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_INTERNAL,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void handleFileUploadFormRingkasan(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesFormRingkasan = uploadedFilesFormRingkasan == null ? new ArrayList<UploadedFileWO>()
				: uploadedFilesFormRingkasan;
		uploadedFilesFormRingkasan.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_INTERNAL,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	/*public Boolean validate(String publishedDate, String expDate, String effDate) {*/
	public Boolean validate() {
		Boolean flag = false;
		try {		
			
			/*Calendar calendarEffectiveDate = Calendar.getInstance();
			calendarEffectiveDate.setTime(regulation.getEffectiveDate());
			Calendar calendarExpiredDate = Calendar.getInstance();
			calendarExpiredDate.setTime(regulation.getExpiredDate());
			
			int yearEffDate = calendarEffectiveDate.get(Calendar.YEAR);
			int yearExpDate = calendarExpiredDate.get(Calendar.YEAR);
			int monthEffDate = calendarEffectiveDate.get(Calendar.MONTH);
			int monthExpDate = calendarExpiredDate.get(Calendar.MONTH);
			int dayEffDate = calendarEffectiveDate.get(Calendar.DAY_OF_MONTH);
			int dayExpDate = calendarExpiredDate.get(Calendar.DAY_OF_MONTH);
			
			int yearCalculate = yearExpDate - yearEffDate;
			
			if (yearCalculate < 3) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationReviewDate") + " "
						+ facesUtil.retrieveMessage("validateDateByYearMustBigger") + " "
						+ facesUtil.retrieveMessage("formInternalRegulationThreeYearValidate") + " " 
						+ facesUtil.retrieveMessage("formInternalRegulationEffDate"));
				flag = true;
			} else {
				if (monthExpDate < monthEffDate) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationReviewDate") + " "
							+ facesUtil.retrieveMessage("validateDateByMonthMustBigger") + " "
							+ facesUtil.retrieveMessage("formInternalRegulationEffDate"));
					flag = true;
				} else {
					if (monthExpDate == monthEffDate) {
						if (dayExpDate < dayEffDate) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationReviewDate") + " "
									+ facesUtil.retrieveMessage("validateDateByDaysMustBigger") + " "
									+ facesUtil.retrieveMessage("formInternalRegulationEffDate"));
							flag = true;
						}
					}
				}
			}*/
			
			if (counterTypeId == null || counterTypeId == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationEmailReminder") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadedFilesPeraturanId.size() == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationAttachmentPeraturan") + " "
						+ facesUtil.retrieveMessage("validateUploadMinOneData"));
				flag = true;
			}

			if (regulation.getRegulationTrackRecords() == null || regulation.getRegulationTrackRecords().size() == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationRekamJejak") + " "
						+ facesUtil.retrieveMessage("validateDetailMinOneData"));
				flag = true;
			}

			if (regulation.getRegulationTrackRecords() != null && regulation.getRegulationTrackRecords().size() > 0) {
				for (int i = 0; i < regulation.getRegulationTrackRecords().size(); i++) {
					RegulationTrackRecord trackRecord = (RegulationTrackRecord) regulation.getRegulationTrackRecords()
							.get(i);
					if (trackRecord.getTrackCode() == null || trackRecord.getTrackCode().equals("")) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationRekamJejak") + " "
								+ facesUtil.retrieveMessage("validateRequired"));

						flag = true;

					} else {
						if (trackRecord.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_CHANGE)) {
							if (trackRecord.getRegulationLinkName() == null
									|| trackRecord.getRegulationLinkName().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationRegNo") + " "
										+ facesUtil.retrieveMessage("validateRequired"));

								flag = true;

							} else if (trackRecord.getTrackNote() == null || trackRecord.getTrackNote().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationKetMencabut") + " "
										+ facesUtil.retrieveMessage("validateRequired"));

								flag = true;
							}
						} else if (trackRecord.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REVOKE)) {
							if (trackRecord.getRegulationLinkName() == null
									|| trackRecord.getRegulationLinkName().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationRegNo") + " "
										+ facesUtil.retrieveMessage("validateRequired"));

								flag = true;

							}
						} /*else if (trackRecord.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REGULATION)) {
							if (trackRecord.getRegulationLinkName() == null
									|| trackRecord.getRegulationLinkName().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationRegNo") + " "
										+ facesUtil.retrieveMessage("validateRequired"));

								flag = true;

							}
						} */
//						else {
//							if (trackRecord.getRegulationLinkName() != null
//									&& !trackRecord.getRegulationLinkName().equals("")) {
//								facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationLinkJudul") + " "
//										+ facesUtil.retrieveMessage("validateCanNotBefill"));
//
//								flag = true;
//							}
//						}
					}
				}
			} else {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationRekamJejak") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			// unit pengusul
			if (regulation.getRegulationProposerUnits() != null && regulation.getRegulationProposerUnits().size() > 0) {
				for (int i = 0; i < regulation.getRegulationProposerUnits().size(); i++) {
					RegulationProposerUnit dataUnit = (RegulationProposerUnit) regulation.getRegulationProposerUnits().get(i);
					if (dataUnit.getDirectorate() == null || dataUnit.getDirectorate().equals("")) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationDirectorate") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}
					
					if (dataUnit.getPublisherUnit() == null || dataUnit.getPublisherUnit().equals("")) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationProposerUnit") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}
				}
			}else {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationProposerUnit") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			if (!flag) {
				Regulation regulValid = regulationService.getCheckDataRegulation(regulation.getRegulationId(),
						InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL, regulation.getDocumentNo(),
						regulation.getNameIn());
				if (actionMode.equals(Constants.ACTION_EDIT)) {
					if (regulValid != null && regulValid.getRegulationId() != null
							&& regulValid.getRegulationId() > 0) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationDocNo") + " : "
								+ regulation.getDocumentNo() + " "
								+ facesUtil.retrieveMessage("formInternalRegulationRegTitle") 
								+ regulation.getNameIn() + " " 
								+ facesUtil.retrieveMessage("validateAlreadyExist"));
						flag = true;
					}
				} else {
					if (regulValid != null && regulValid.getRegulationId() != null
							&& regulValid.getRegulationId() > 0) {
						if (regulValid.getEnabledFlag() != null && regulValid.getEnabledFlag().equals("Y")) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationDocNo") + " : "
									+ regulation.getDocumentNo() + " "
									+ facesUtil.retrieveMessage("formInternalRegulationRegTitle") 
									+ regulation.getNameIn() + " " 
									+ facesUtil.retrieveMessage("validateAlreadyExist"));
							flag = true;
						} else {
							regulationValided = regulationService.findById(regulValid.getRegulationId());
							checkValidate = "DATA_UPDATE";
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage("validate error Internal Regulation = " + e.getMessage());
		}

		return flag;
	}

	public void onChangeDocType() {
		if (actionMode.equals(Constants.ACTION_ADD)) {
			regulation.setPublishedDate(null);
			regulation.setEffectiveDate(null);
			regulation.setExpiredDate(null);
			typeReviewDateParamCode = null;
		}
		isShowPanelTipeTanggalUlasan = false;
		if(docTypeId != null) {
			DocumentType docType = documentTypeService.findById(docTypeId);
			
			if(docType.getDocumentType().trim().equalsIgnoreCase("Peraturan Sementara")) {
				setTanggalUlasanPeraturanSementaraFlag(true);
				isShowPanelTipeTanggalUlasan = true;
			}else {
				setTanggalUlasanPeraturanSementaraFlag(false);
			}
		}else {
			setTanggalUlasanPeraturanSementaraFlag(false);
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onChangeReviewDateType() {
		//add
		regulation.setExpiredDate(null);
		if(docTypes != null) {
			DocumentType docType = documentTypeService.findById(docTypeId);
			
			if(docType.getDocumentType().trim().equalsIgnoreCase("Peraturan Sementara")) {
				if (regulation.getPublishedDate() != null && typeReviewDateParamCode != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(regulation.getPublishedDate());
					calendar.add(Calendar.MONTH, Integer.parseInt(reviewDateMonth.get(typeReviewDateParamCode)));
					regulation.setExpiredDate(calendar.getTime());
				}
			} else {
				regulation.setExpiredDate(null);
			}
			PrimeFaces.current().executeScript("reInitSelect2();");
		}
	}
	
	//add 14-11-2023
	private String replace(String kalimat,String users, String noDocument, String judul, String rekamJejak, String noPeraturan, String judulLama) {
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		if (StringUtils.isNotBlank(noDocument)) {
			kalimat = kalimat.replace("{nomor_document}", noDocument);
		}
		if (StringUtils.isNotBlank(judul)) {
			kalimat = kalimat.replace("{judul_peraturan}", judul);
		}
		if (StringUtils.isNotBlank(rekamJejak)) {
			kalimat = kalimat.replace("{rekam_jejak}", rekamJejak);
		}
		if (StringUtils.isNotBlank(noPeraturan)) {
			kalimat = kalimat.replace("{nomor_peraturan}",noPeraturan);
		}
		if (StringUtils.isNotBlank(judulLama)) {
			kalimat = kalimat.replace("{judul_peraturan_old}",judulLama);
		}
		return kalimat;
		 
	}
	
	private String replaceEdit(String kalimat, String noDocument, String judulPeraturan, String users, String rekamJejakAwal, String rekamJejakBaru,String nomerPeraturan, String judulPeraturanOld) {
		if (StringUtils.isNotBlank(noDocument)) {
			kalimat = kalimat.replace("{nomor_document}", noDocument);
		}
		if (StringUtils.isNotBlank(judulPeraturan)) {
			kalimat = kalimat.replace("{judul_peraturan}", judulPeraturan);
		}
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		if (StringUtils.isNotBlank(rekamJejakAwal)) {
			kalimat = kalimat.replace("{rekam_jejak_old}", rekamJejakAwal);
		}
		if (StringUtils.isNotBlank(rekamJejakBaru)) {
			kalimat = kalimat.replace("{rekam_jejak_new}",rekamJejakBaru);
		}
		if (StringUtils.isNotBlank(nomerPeraturan)) {
			kalimat = kalimat.replace("{nomor_peraturan}",nomerPeraturan);
		}
		if (StringUtils.isNotBlank(judulPeraturanOld)) {
			kalimat = kalimat.replace("{judul_peraturan_old}",judulPeraturanOld);
		}
		return kalimat;
	}
	
	@SuppressWarnings("deprecation")
	public void save() {
		try {

			/*String publishedDate = facesUtil.retrieveRequestParam("PUBLISHED_DATE");
			String expiredDate = facesUtil.retrieveRequestParam("EXPIRED_DATE");
			String effectiveDate = facesUtil.retrieveRequestParam("EFFECTIVE_DATE");*/

			/*if (!validate(publishedDate, expiredDate, effectiveDate)) {*/
			if (!validate()) {

				List<RegulationAttachment> listAttachment = new ArrayList<RegulationAttachment>();
				for (int i = 0; i < uploadedFilesPeraturanId.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesPeraturanId.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode("ATTACHMENT_REGULATIONS_IN");
					ra.setAttachmentFile(uf.getFileName());
					ra.setCreatedBy(facesUtil.retrieveUserLogin());
					ra.setCreationDate(new Timestamp(new Date().getTime()));
					ra.setDelId(new Long(0));
					ra.setEnabledFlag(Constants.CONSTANT_YES);
					
					ra.setFileId(uf.getFileId());
					ra.setFileSize(uf.getFileSize());
					
					listAttachment.add(ra);
				}
			
				for (int i = 0; i < uploadedFilesFAQ.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesFAQ.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode("ATTACHMENT_FAQ");
					ra.setAttachmentFile(uf.getFileName());
					ra.setCreatedBy(facesUtil.retrieveUserLogin());
					ra.setCreationDate(new Timestamp(new Date().getTime()));
					ra.setDelId(new Long(0));
					ra.setEnabledFlag(Constants.CONSTANT_YES);
					
					ra.setFileId(uf.getFileId());
					ra.setFileSize(uf.getFileSize());
					
					listAttachment.add(ra);

				}

				for (int i = 0; i < uploadedFilesLampiran.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesLampiran.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode("ATTACHMENT_LAMPIRAN");
					ra.setAttachmentFile(uf.getFileName());
					ra.setCreatedBy(facesUtil.retrieveUserLogin());
					ra.setCreationDate(new Timestamp(new Date().getTime()));
					ra.setDelId(new Long(0));
					ra.setEnabledFlag(Constants.CONSTANT_YES);
					
					ra.setFileId(uf.getFileId());
					ra.setFileSize(uf.getFileSize());
					
					listAttachment.add(ra);

				}
				
				for (int i = 0; i < uploadedFilesOther.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesOther.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_SUMMARY);
					ra.setAttachmentFile(uf.getFileName());
					ra.setCreatedBy(facesUtil.retrieveUserLogin());
					ra.setCreationDate(new Timestamp(new Date().getTime()));
					ra.setDelId(new Long(0));
					ra.setEnabledFlag(Constants.CONSTANT_YES);
					
					ra.setFileId(uf.getFileId());
					ra.setFileSize(uf.getFileSize());
					
					listAttachment.add(ra);

				}

				for (int i = 0; i < uploadedFilesOther.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesOther.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode("ATTACHMENT_OTHER");
					ra.setAttachmentFile(uf.getFileName());
					ra.setCreatedBy(facesUtil.retrieveUserLogin());
					ra.setCreationDate(new Timestamp(new Date().getTime()));
					ra.setDelId(new Long(0));
					ra.setEnabledFlag(Constants.CONSTANT_YES);
					
					ra.setFileId(uf.getFileId());
					ra.setFileSize(uf.getFileSize());
					
					listAttachment.add(ra);

				}

				if (listAttachment.size() > 0) {
					if (regulation.getRegulationAttachments() == null
							|| regulation.getRegulationAttachments().size() == 0) {
						regulation.setRegulationAttachments(listAttachment);
					} else {
						regulation.getRegulationAttachments().clear();
						regulation.getRegulationAttachments().addAll(listAttachment);
					}
				}

				for (int i = 0; i < regulation.getRegulationTrackRecords().size(); i++) {
					RegulationTrackRecord rt = regulation.getRegulationTrackRecords().get(i);
					rt.setRegulation(regulation);
					rt.setCreatedBy(facesUtil.retrieveUserLogin());
					rt.setCreationDate(new Timestamp(new Date().getTime()));
					rt.setDelId(new Long(0));
					rt.setEnabledFlag(Constants.CONSTANT_YES);

				}
				
				for (int i = 0; i < regulation.getRegulationProposerUnits().size(); i++) {
					RegulationProposerUnit rt = regulation.getRegulationProposerUnits().get(i);
					rt.setRegulation(regulation);
					rt.setCreatedBy(facesUtil.retrieveUserLogin());
					rt.setCreationDate(new Timestamp(new Date().getTime()));
					rt.setDelId(new Long(0));
					rt.setEnabledFlag(Constants.CONSTANT_YES);

				}
				
				if(StringUtils.isEmpty(regulation.getJenisKetentuan().getParameterDtlCode())) {
					ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(regulation.getJenisKetentuan().getParameterDtlCode());
					regulation.setJenisKetentuan(pd);
				}
				
				/*if (StringUtils.isNotEmpty(regulation.getDirectorate().getParameterDtlCode()) && 
						!regulation.getDirectorate().getParameterDtlCode().equals("")) {
					ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(regulation.getDirectorate().getParameterDtlCode());
					regulation.setDirectorate(pd);
				}
				
				if (StringUtils.isNotEmpty(regulation.getPublisherUnit().getParameterDtlCode())
						&& !regulation.getPublisherUnit().getParameterDtlCode().equals("")) {
					ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(regulation.getPublisherUnit().getParameterDtlCode());
					regulation.setPublisherUnit(pd);
				}*/
				
				if(docTypeId != null) {
					DocumentType dt = documentTypeService.findById(docTypeId);
					regulation.setDocumentType(dt);
				}
				
				if(docCategoryId != null) {
					DocumentCategory dc = documentCategoryService.findById(docCategoryId);
					regulation.setDocumentCategory(dc);
				}
				
				if(docTopicId != null) {
					DocumentTopic dt = documentTopicService.findById(docTopicId);
					regulation.setDocumentTopic(dt);
				}
				
				if (counterTypeId != null) {
					CounterType ct = counterTypeService.findById(counterTypeId);
					regulation.setCounterType(ct);
				}
				
				//Add by Jovan 6/11/2023
				if (typeReviewDateParamCode != null) {
					ParameterDetail typeReviewDate = parameterDetailService.getParameterDetailByParamDtlCode(typeReviewDateParamCode);
					regulation.setTypeReviewDate(typeReviewDate);
				}
				
				
				
				/*if(!StringUtils.isEmpty(publishedDate)) {
				regulation.setPublishedDate(sdf.parse(publishedDate));
				}
				if(!StringUtils.isEmpty(expiredDate)) {
				regulation.setExpiredDate(sdf.parse(expiredDate));
				}
				if(!StringUtils.isEmpty(effectiveDate)) {
				regulation.setEffectiveDate(sdf.parse(effectiveDate));
				}*/
				regulation.setStatus(ParameterDetail.PARAM_DET_CODE_DATA_NEW);

				if(checkValidate !=null && checkValidate.equals("DATA_UPDATE")) {		
					regulationValided.setPublisherUnit(regulation.getPublisherUnit());
					
					regulationService.updateDataAlreadyExist(regulation, regulationValided, facesUtil.retrieveUserLogin());
					
				}else {
					if (regulation.getRegulationId() != null) {
						regulationValided = regulationService.findById(regulation.getRegulationId());
						regulationValided.setPublisherUnit(regulation.getPublisherUnit());
						
						regulationService.updateDataAlreadyExist(regulation, regulationValided, facesUtil.retrieveUserLogin());
					} else {
						regulation.setCreatedBy(facesUtil.retrieveUserLogin());
						regulation.setCreationDate(new Timestamp(new Date().getTime()));
						regulation.setDelId(new Long(0));
						regulation.setEnabledFlag(Constants.CONSTANT_YES);
						regulationService.save(regulation);
					}
				}
				
				//add 13-11-2023
				if (regulation.getDocumentNo() != null && regulation.getNameIn() != null
						&& actionMode == Constants.ACTION_ADD && (regulation.getRegulationTrackRecords().size() > 0
								|| regulation.getRegulationTrackRecords() != null)) {

					for (RegulationTrackRecord rt : regulation.getRegulationTrackRecords()) {
						LogActivity la = new LogActivity();
						List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TRACK_RECORD");
						ParameterDetail paramDtl = pd.stream()
								.filter(x -> StringUtils.equals(rt.getTrackCode(), x.getParameterDtlCode())).findFirst()
								.orElse(null);
						

						Regulation r = new Regulation();
						
						if(rt.getRegulationLinkId() != null) {
							r = regulationService.findById(rt.getRegulationLinkId());
						}else{
							//SET EMPTY STRING FOR LOG PURPOSES
							r.setDocumentNo("");
						}

						List<ParameterDetail> activityType = parameterDetailService.getParameterDetailByParamCode("ACTIVITY_TYPE");
						for (ParameterDetail data : activityType) {
							if (data.getParameterDtlCode().equals("ACTIVITY_TYPE_PERATURAN_INTERNAL")) {
								la.setActivityType(data.getParameterDtlCode());
							}
						}
						la.setUser(getUserLogin());
						la.setActivityDate(new Timestamp(new Date().getTime()));
						if (paramDtl != null && (paramDtl.getParameterDtlCode().equals("RECORD_NEW_REGULATION"))) {
							ParameterDetail activityDate = parameterDetailService.getParameterDetailByParamDtlCode("LOG_ACT_PER_INTERNAL_ADD");
							String str = activityDate.getNameIn();
							String hasil = replace(str, facesUtil.getUserLogin().getName(), regulation.getDocumentNo(),regulation.getNameIn(), null, null, null);
							la.setActivityNote(hasil);
						} else if (paramDtl != null && (paramDtl.getParameterDtlCode().equals("RECORD_CHANGE"))) {
							ParameterDetail pdActivityNote = parameterDetailService.getParameterDetailByParamDtlCode("LOG_ACT_PER_INTERNAL_ADD_RJ");
							String str = pdActivityNote.getNameIn();
							String hasil = replace(str, facesUtil.getUserLogin().getName(), regulation.getDocumentNo(),regulation.getNameIn(), paramDtl.getNameIn(), r.getDocumentNo(), r.getNameIn());
							la.setActivityNote(hasil);
						} else if (paramDtl != null && (paramDtl.getParameterDtlCode().equals("RECORD_REVOKE"))) {
							ParameterDetail pdActivityNote = parameterDetailService.getParameterDetailByParamDtlCode("LOG_ACT_PER_INTERNAL_ADD_RJ");
							String str = pdActivityNote.getNameIn();
							String hasil = replace(str, facesUtil.getUserLogin().getName(), regulation.getDocumentNo(),regulation.getNameIn(), paramDtl.getNameIn(), r.getDocumentNo(), r.getNameIn());
							la.setActivityNote(hasil);
						} else {

						}
						if (StringUtils.isBlank(la.getCreatedBy())) {
							EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
						} else {
							EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
						}
						logActivityService.save(la);
					}

				}

				if (regulation.getDocumentNo() != null && regulation.getNameIn() != null
						&& actionMode == Constants.ACTION_EDIT && (regulation.getRegulationTrackRecords().size() > 0
								|| regulation.getRegulationTrackRecords() != null)) {

					for (RegulationTrackRecord rt : regulation.getRegulationTrackRecords()) {
						LogActivity la = new LogActivity();
						List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TRACK_RECORD");
						ParameterDetail paramDtl = pd.stream()
								.filter(x -> StringUtils.equals(rt.getTrackCode(), x.getParameterDtlCode())).findFirst()
								.orElse(null);
//						Regulation r = regulationService.findById(rt.getRegulationLinkId());
						Regulation r = rt.getRegulationLinkId() != null ? regulationService.findById(rt.getRegulationLinkId()) : new Regulation();
						if (StringUtils.isBlank(r.getDocumentNo())) r.setDocumentNo("");
						List<ParameterDetail> activityType = parameterDetailService.getParameterDetailByParamCode("ACTIVITY_TYPE");
						for (ParameterDetail data : activityType) {
							if (data.getParameterDtlCode().equals("ACTIVITY_TYPE_PERATURAN_INTERNAL")) {
								la.setActivityType(data.getParameterDtlCode());
							}
						}
						la.setUser(getUserLogin());
						la.setActivityDate(new Timestamp(new Date().getTime()));
						if (paramDtl != null) {
							la.setActivityDate(new Timestamp(new Date().getTime()));
							ParameterDetail activityDate = parameterDetailService.getParameterDetailByParamDtlCode("LOG_ACT_PER_INTERNAL_EDIT_RJ");
							String str = activityDate.getNameIn();
							String hasil = replaceEdit(str, regulation.getDocumentNo(), regulation.getNameIn(),
									facesUtil.getUserLogin().getName(), recordMap.get(rt.getTrackNameTemp()), recordMap.get(rt.getTrackCode()) , r.getDocumentNo(), regulation.getNameInOld());
							la.setActivityNote(hasil);
						} else {

						}
						if (StringUtils.isBlank(la.getCreatedBy())) {
							EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
						} else {
							EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
						}
						logActivityService.save(la);
					}
				}
				
				
				
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
				
				if(deletedFiles!=null) {
					for(int i=0;i<deletedFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}

				/*Regulation regulationApp = null;
				
				regulationApp = regulationService2.findById(regulation.getRegulationId());
				
				internalRegulationApprovalService.processApprove(regulationApp, "Approve By System", facesUtil.retrieveUserLogin(),ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE,ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED);
		       	   */
				facesUtil.redirect("/pages/internalRegulation/internalRegulation.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() throws Exception {
		
		for (int i = 0; i < uploadedFilesPeraturanId.size(); i++) {
			UploadedFileWO uf = (UploadedFileWO) uploadedFilesPeraturanId.get(i);
			if(uf.getIsNew() == null) {
			CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
			}
		}

		for (int i = 0; i < uploadedFilesFAQ.size(); i++) {
			UploadedFileWO uf = (UploadedFileWO) uploadedFilesFAQ.get(i);
			if(uf.getIsNew() == null) {
				CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
			}
		}

		for (int i = 0; i < uploadedFilesLampiran.size(); i++) {
			UploadedFileWO uf = (UploadedFileWO) uploadedFilesLampiran.get(i);
			if(uf.getIsNew() == null) {
				CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
			}
		}
		
		for (int i = 0; i < uploadedFilesOther.size(); i++) {
			UploadedFileWO uf = (UploadedFileWO) uploadedFilesOther.get(i);
			if(uf.getIsNew() == null) {
				CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
			}
		}
		
		for (int i = 0; i < uploadedFilesFormRingkasan.size(); i++) {
			UploadedFileWO uf = (UploadedFileWO) uploadedFilesFormRingkasan.get(i);
			if(uf.getIsNew() == null) {
				CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
			}
			

		}
			
		facesUtil.redirect("/pages/internalRegulation/internalRegulation.faces");
		
	}
	
	public void deleteAttachment(String fileId,int index, String uploadType) throws Exception {
		
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if(uploadType!=null && uploadType.equals(InternalRegulationConstants.UPLOAD_TYPE_PERATURAN_ID)) {
			uploadedFilesPeraturanId.remove(uploadedFilesPeraturanId.get(index));
		}
		else if(uploadType!=null && uploadType.equals(InternalRegulationConstants.UPLOAD_TYPE_FAQ)) {
			uploadedFilesFAQ.remove(uploadedFilesFAQ.get(index));
		}
		else if(uploadType!=null && uploadType.equals(InternalRegulationConstants.UPLOAD_TYPE_LAMPIRAN)) {
			uploadedFilesLampiran.remove(uploadedFilesLampiran.get(index));
		}
		else if(uploadType!=null && uploadType.equals(InternalRegulationConstants.UPLOAD_TYPE_OTHERS)) {
			uploadedFilesOther.remove(uploadedFilesOther.get(index));
		}
		else if(uploadType!=null && uploadType.equals(InternalRegulationConstants.UPLOAD_TYPE_FROM_RINGKASAN)) {
			uploadedFilesFormRingkasan.remove(uploadedFilesFormRingkasan.get(index));
		}
		
		
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void linkRegulationListener(AjaxBehaviorEvent vce) {
		   String[] splitClientId = vce.getComponent().getClientId().split(":");
		   Integer indexLink = Integer.parseInt(splitClientId[2]);
		   indexDtl = indexLink;
		   RegulationTrackRecord regulationTrackRecTemp = regulation.getRegulationTrackRecords().get(indexLink);
		   if(regulationTrackRecTemp !=null && regulationTrackRecTemp.getTrackCode() !=null && 
				   (regulationTrackRecTemp.getTrackCode().equals("RECORD_NEW_REGULATION") || regulationTrackRecTemp.getTrackCode().equals(""))) {
			   regulationTrackRecTemp.setRegulationLinkId(null);
			   regulationTrackRecTemp.setRegulationLinkName("");
		   } else if (regulationTrackRecTemp !=null && regulationTrackRecTemp.getTrackCode() !=null && 
				   regulationTrackRecTemp.getTrackCode().equals("RECORD_CHANGE")) {
			   //regulationTrackRecTemp.setInActiveFlag("Y");
		   }
		   
		   PrimeFaces.current().executeScript("reInitSelect2();");
	   }

	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject()+" - Internal Regulation";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String documentNumberTemp = regulation.getDocumentNo() != null ? regulation.getDocumentNo() : "";
//			String documentTitleTemp = regulation.getName();
			
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Sosialisasi");
					emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, "Internal Regulation" + " - "
							+ documentNumberTemp);
					
					ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
					String token = Constants.encryptString(regulation.getRegulationId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_APPROVAL_REQUEST_INTERNAL_REGULATION);
					String urlLink = pdHostName.getNameIn().concat("pages/dashboard/dashboard.faces?token="+token+"&menuId="+menuId);
					emailContent = emailContent.replaceAll("url_link", urlLink);
						
					ParameterDetail paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER);
					emailTo = paramEmail.getNameIn();
						
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_INTERNAL_REGULATION", "true", parameterDetailService);
						
//				        emailExecutor.execute(new Runnable() {
//				            @Override
//				            public void run() {
//								
//				            	try {
//									CallApiManager.sendEmail(subject, content, to,cc, parameterDetailService);
//									 
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//				            }
//				        });
				        
//					}
					
				
			
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void clearPic() {
		regulation.setPic(null);
		regulation.setPicNameTemp(null);
	}
	
	public void clearPuk() {
		regulation.setPuk(null);
		regulation.setPukNameTemp(null);
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public InternalRegulationService getInternalRegulationService() {
		return internalRegulationService;
	}

	public void setInternalRegulationService(InternalRegulationService internalRegulationService) {
		this.internalRegulationService = internalRegulationService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public Regulation getRegulation() {
		return regulation;
	}

	public void setRegulation(Regulation regulation) {
		this.regulation = regulation;
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
		InternalRegulationEditBean.logger = logger;
	}
	
	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/*
	 * public ParameterDetailService getParameterDetailService() { return
	 * parameterDetailService; }
	 * 
	 * public void setParameterDetailService(ParameterDetailService
	 * parameterDetailService) { this.parameterDetailService =
	 * parameterDetailService; }
	 */

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

	public List<UploadedFileWO> getUploadedFilesPeraturanId() {
		return uploadedFilesPeraturanId;
	}

	public void setUploadedFilesPeraturanId(List<UploadedFileWO> uploadedFilesPeraturanId) {
		this.uploadedFilesPeraturanId = uploadedFilesPeraturanId;
	}

	public List<UploadedFileWO> getUploadedFilesPeraturanEn() {
		return uploadedFilesPeraturanEn;
	}

	public void setUploadedFilesPeraturanEn(List<UploadedFileWO> uploadedFilesPeraturanEn) {
		this.uploadedFilesPeraturanEn = uploadedFilesPeraturanEn;
	}

	public List<UploadedFileWO> getUploadedFilesFAQ() {
		return uploadedFilesFAQ;
	}

	public void setUploadedFilesFAQ(List<UploadedFileWO> uploadedFilesFAQ) {
		this.uploadedFilesFAQ = uploadedFilesFAQ;
	}

	public List<UploadedFileWO> getUploadedFilesLampiran() {
		return uploadedFilesLampiran;
	}

	public void setUploadedFilesLampiran(List<UploadedFileWO> uploadedFilesLampiran) {
		this.uploadedFilesLampiran = uploadedFilesLampiran;
	}

	public List<UploadedFileWO> getUploadedFilesOther() {
		return uploadedFilesOther;
	}

	public void setUploadedFilesOther(List<UploadedFileWO> uploadedFilesOther) {
		this.uploadedFilesOther = uploadedFilesOther;
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

	public RegulationTrackRecordTableModel<RegulationTrackRecord> getTableModel() {
		return tableModel;
	}

	public RegulationTrackRecord getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecord selectedRow) {
		this.selectedRow = selectedRow;
	}

	public void setTableModel(RegulationTrackRecordTableModel<RegulationTrackRecord> tableModel) {
		this.tableModel = tableModel;
	}

	public RegulationTrackRecord[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(RegulationTrackRecord[] selectedData) {
		this.selectedData = selectedData;
	}
	
	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}
	

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("jdlPeraturanDialog", widgetVar)) {
			Regulation reg = (Regulation) selectedItem;
			
			/*String name = null;
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			if (locale != null && locale.equals(locale.ENGLISH)) {
				name = (String) objects[2];
			} else {
				name = (String) objects[1];
			}*/
			
			if(indexDtl == null) {
				indexDtl = 0;
			}
			
			//regulation.getRegulationTrackRecords().get(indexDtl).setRegulationLinkId(((java.math.BigInteger) objects[0]).longValue());
			regulation.getRegulationTrackRecords().get(indexDtl).setRegulationLinkId(reg.getRegulationId());
			//regulation.getRegulationTrackRecords().get(indexDtl).setRegulationLinkName(reg.getName());
			regulation.getRegulationTrackRecords().get(indexDtl).setRegulationLinkName(reg.getDocumentNo());
			
			tableModel.setWrappedData(regulation.getRegulationTrackRecords());
		}
		
		if (StringUtils.equals("picDialog", widgetVar)) {
			Object[] obj = (Object[]) selectedItem;
			User userPic = userService.findById(MathUtil.returnIdObjectToLong(obj[0]));
			
			if(indexDtlPuPic == null) {
				indexDtlPuPic = 0;
			}			
			
			regulation.getRegulationProposerUnits().get(indexDtlPuPic).setPic(userPic);
			regulation.getRegulationProposerUnits().get(indexDtlPuPic).setPicNameTemp(userPic.getName());
		}
		
		if (StringUtils.equals("pukDialog", widgetVar)) {
			Object[] obj = (Object[]) selectedItem;
			User userPuk = userService.findById(MathUtil.returnIdObjectToLong(obj[0]));
			regulation.getRegulationProposerUnits().get(indexDtlPuPic).setPuk(userPuk);
			regulation.getRegulationProposerUnits().get(indexDtlPuPic).setPukNameTemp(userPuk.getName());
		}
		
		if (StringUtils.equals("proposerUnitDialog", widgetVar)) {
			Object[] obj = (Object[]) selectedItem;
			regulation.getRegulationProposerUnits().get(indexDtlPuPic).setPublisherUnit(obj[0]+"");
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPicPu() {		
		if (regulation.getRegulationProposerUnits() == null
				|| regulation.getRegulationProposerUnits().size() == 0) {
			regulation.setRegulationProposerUnits(new ArrayList<RegulationProposerUnit>());
			lastSequenceOfPuDtl = 0;
		}  else {
			if(regulation.getRegulationProposerUnits().size() == 0) {
				lastSequenceOfPuDtl = 0;
			}			
		} 

		RegulationProposerUnit rt = new RegulationProposerUnit();
		lastSequenceOfPuDtl = lastSequenceOfPuDtl + 1;
		rt.setSequence(lastSequenceOfPuDtl);
		regulation.getRegulationProposerUnits().add(rt);
		
		tableModelProposerUnit.setWrappedData(regulation.getRegulationProposerUnits());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onDeleteRowPicPu() {
		if(selectedDataProposerUnit !=null) {
			for (int i = 0; i < selectedDataProposerUnit.length; i++) {
				regulation.getRegulationProposerUnits().remove(selectedDataProposerUnit[i]);
			}
		}
		
		if (regulation.getRegulationProposerUnits() == null
				|| regulation.getRegulationProposerUnits().size() == 0) {
			lastSequenceOfPuDtl = 0;
		}
		
		tableModelProposerUnit.setWrappedData(regulation.getRegulationProposerUnits());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onChangeDirectoratePu(int i) {		
		RegulationProposerUnit data = regulation.getRegulationProposerUnits().get(i);
		
		if (data.getDirectorate() != null) {
			/*List<String> getDivision = userService.getDivisionByDirectorate(data.getDirectorate());
			if(unitPenerbitList.size()>0){
				unitPenerbitList.clear();
			}
			
			for (String pd : getDivision) {
				SelectItem si = new SelectItem();
				si.setLabel(pd);
				si.setValue(pd);
				
				unitPenerbitList.add(si);
			}*/
			PrimeFaces.current().executeScript("reInitSelect2();");
		}
		
		
		data.setPublisherUnit(null);
		data.setPic(null);
		data.setPicNameTemp(null);
		data.setPuk(null);
		data.setPukNameTemp(null);
				
		PrimeFaces.current().executeScript("initSelect2();");		
	}
	
	public void clearPicPuDetail(int i) {	
		regulation.getRegulationProposerUnits().get(i).setPic(null);
		regulation.getRegulationProposerUnits().get(i).setPicNameTemp(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPukPuDetail(int i) {	
		regulation.getRegulationProposerUnits().get(i).setPuk(null);
		regulation.getRegulationProposerUnits().get(i).setPukNameTemp(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearProposerUnitPuDetail(int i) {	
		regulation.getRegulationProposerUnits().get(i).setPublisherUnit(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}

	public Regulation getRegulationValided() {
		return regulationValided;
	}

	public void setRegulationValided(Regulation regulationValided) {
		this.regulationValided = regulationValided;
	}

	public String getCheckValidate() {
		return checkValidate;
	}

	public void setCheckValidate(String checkValidate) {
		this.checkValidate = checkValidate;
	}

	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public List<SelectItem> getDirectorateLists() {
		return directorateLists;
	}

	public void setDirectorateLists(List<SelectItem> directorateLists) {
		this.directorateLists = directorateLists;
	}

	public List<SelectItem> getUnitPenerbitList() {
		return unitPenerbitList;
	}

	public void setUnitPenerbitList(List<SelectItem> unitPenerbitList) {
		this.unitPenerbitList = unitPenerbitList;
	}

	public List<UploadedFileWO> getUploadedFilesFormRingkasan() {
		return uploadedFilesFormRingkasan;
	}

	public void setUploadedFilesFormRingkasan(List<UploadedFileWO> uploadedFilesFormRingkasan) {
		this.uploadedFilesFormRingkasan = uploadedFilesFormRingkasan;
	}

	public SelectorInfo getSelectorPic() {
		return selectorPic;
	}

	public void setSelectorPic(SelectorInfo selectorPic) {
		this.selectorPic = selectorPic;
	}

	public SelectorInfo getSelectorPuk() {
		return selectorPuk;
	}

	public void setSelectorPuk(SelectorInfo selectorPuk) {
		this.selectorPuk = selectorPuk;
	}

	public List<SelectItem> getEmailReminderList() {
		return emailReminderList;
	}

	public void setEmailReminderList(List<SelectItem> emailReminderList) {
		this.emailReminderList = emailReminderList;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public InternalRegulationApprovalService getInternalRegulationApprovalService() {
		return internalRegulationApprovalService;
	}

	public void setInternalRegulationApprovalService(InternalRegulationApprovalService internalRegulationApprovalService) {
		this.internalRegulationApprovalService = internalRegulationApprovalService;
	}

	public RegulationService getRegulationService2() {
		return regulationService2;
	}

	public void setRegulationService2(RegulationService regulationService2) {
		this.regulationService2 = regulationService2;
	}

	public Integer getEffectiveYear() {
		return effectiveYear;
	}

	public void setEffectiveYear(Integer effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public boolean isTanggalUlasanPeraturanSementaraFlag() {
		return tanggalUlasanPeraturanSementaraFlag;
	}

	public void setTanggalUlasanPeraturanSementaraFlag(boolean tanggalUlasanPeraturanSementaraFlag) {
		this.tanggalUlasanPeraturanSementaraFlag = tanggalUlasanPeraturanSementaraFlag;
	}

	public List<SelectItem> getTypeReviewDateList() {
		return typeReviewDateList;
	}

	public void setTypeReviewDateList(List<SelectItem> typeReviewDateList) {
		this.typeReviewDateList = typeReviewDateList;
	}

	public String getTypeReviewDateParamCode() {
		return typeReviewDateParamCode;
	}

	public void setTypeReviewDateParamCode(String typeReviewDateParamCode) {
		this.typeReviewDateParamCode = typeReviewDateParamCode;
	}
	//add 13-11-2023
	public LogActivity getLogActivity() {
		return logActivity;
	}

	public void setLogActivity(LogActivity logActivity) {
		this.logActivity = logActivity;
	}

	public LogActivityService getLogActivityService() {
		return logActivityService;
	}

	public void setLogActivityService(LogActivityService logActivityService) {
		this.logActivityService = logActivityService;
	}

	public HashMap<String, String> getRecordMap() {
		return recordMap;
	}

	public void setRecordMap(HashMap<String, String> recordMap) {
		this.recordMap = recordMap;
	}

	public HashMap<String, String> getReviewDateMonth() {
		return reviewDateMonth;
	}

	public void setReviewDateMonth(HashMap<String, String> reviewDateMonth) {
		this.reviewDateMonth = reviewDateMonth;
	}

	public Boolean getIsShowPanelTipeTanggalUlasan() {
		return isShowPanelTipeTanggalUlasan;
	}

	public void setIsShowPanelTipeTanggalUlasan(Boolean isShowPanelTipeTanggalUlasan) {
		this.isShowPanelTipeTanggalUlasan = isShowPanelTipeTanggalUlasan;
	}

	public Integer getIndexDtlPuPic() {
		return indexDtlPuPic;
	}

	public void setIndexDtlPuPic(Integer indexDtlPuPic) {
		this.indexDtlPuPic = indexDtlPuPic;
	}

	public Integer getLastSequenceOfPuDtl() {
		return lastSequenceOfPuDtl;
	}

	public void setLastSequenceOfPuDtl(Integer lastSequenceOfPuDtl) {
		this.lastSequenceOfPuDtl = lastSequenceOfPuDtl;
	}

	public SelectorInfo getSelectorProposerUnit() {
		return selectorProposerUnit;
	}

	public void setSelectorProposerUnit(SelectorInfo selectorProposerUnit) {
		this.selectorProposerUnit = selectorProposerUnit;
	}

	public RegulationProposerUnit[] getSelectedDataProposerUnit() {
		return selectedDataProposerUnit;
	}

	public void setSelectedDataProposerUnit(RegulationProposerUnit[] selectedDataProposerUnit) {
		this.selectedDataProposerUnit = selectedDataProposerUnit;
	}

	public RegulationProposerUnitTableModel<RegulationProposerUnit> getTableModelProposerUnit() {
		return tableModelProposerUnit;
	}

	public void setTableModelProposerUnit(RegulationProposerUnitTableModel<RegulationProposerUnit> tableModelProposerUnit) {
		this.tableModelProposerUnit = tableModelProposerUnit;
	}
	
	
	
}