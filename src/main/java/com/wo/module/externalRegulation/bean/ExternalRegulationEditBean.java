package com.wo.module.externalRegulation.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
//import com.wo.module.counterType.model.CounterTypeDtl;
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
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.model.RegulationTrackRecordTableModel;
import com.wo.module.externalRegulation.service.ExternalRegulationService;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.externalRegulationApproval.model.RegulationApproval;
import com.wo.module.externalRegulationApproval.service.ExternalRegulationApprovalService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class ExternalRegulationEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ExternalRegulationBean.class);

	private Regulation regulation;

	private List<RegulationApproval> regApprovals;
	
	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long docTypeId;

	private Long docCategoryId;

	private Long docTopicId;

	private List<SelectItem> provTypes;

	private List<SelectItem> trackCodes;

	private List<SelectItem> docTypes;

	private List<SelectItem> docCategories;

	private List<SelectItem> docTopics;
	
	private List<UploadedFileWO> deletedFiles;

	private List<UploadedFileWO> uploadedFilesPeraturanId;

	private List<UploadedFileWO> uploadedFilesPeraturanEn;

	private List<UploadedFileWO> uploadedFilesSummary;

	private List<UploadedFileWO> uploadedFilesPenjelasan;

	private List<UploadedFileWO> uploadedFilesFAQ;

	private List<UploadedFileWO> uploadedFilesLampiran;

	private List<UploadedFileWO> uploadedFilesBuletin;

	private List<UploadedFileWO> uploadedFilesMateri;

	private SelectorInfo selectorJdlPeraturan;

	private RegulationTrackRecord[] selectedData;

	private RegulationTrackRecordTableModel<RegulationTrackRecord> tableModel;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;

	private ExternalRegulationService externalRegulationService;
	
	private ExternalRegulationApprovalService externalRegulationApprovalService;

	private RegulationService regulationService;
	
	private RegulationService regulationService2;
	
	//private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;
	
	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;

	private String navigateSearch = ExternalRegulationConstants.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private Regulation regulationValided;

	private String localLanguange;

	private String checkValidate;
	
	private String textWarningUpload;
	
	private FileUtil fileUtil;
	
	private Integer lastSequenceOfDtl;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
		super.init();
		selectProvType();
		selectTrackCode();
		selectDocType();
		selectDocCategory();
		selectDocTopic();

		selectorJdlPeraturan = ExternalRegulationConstants.buildSelectorJdlPeraturan(facesUtil);

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}

		checkNewOrEdit();
		regulationValided = new Regulation();
		regApprovals = new ArrayList<RegulationApproval>();
		
		checkValidate = "";
		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN);
			for (int i = 0; i < pd.size(); i++) {
				// ParameterDetail paramDtl = (ParameterDetail) pd.get(i);
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
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_TRACK_RECORD);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				ParameterDetail paramDtl = (ParameterDetail) pd.get(i);
				if (paramDtl.getParameterDtlCode() != null && (paramDtl.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_CHANGE)
						|| paramDtl.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REGULATION)
						|| paramDtl.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REVOKE))) {
					si.setLabel(paramDtl.getName());
					si.setValue(paramDtl.getParameterDtlCode());
					trackCodes.add(si);
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
							ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)),
					0, Integer.MAX_VALUE, null, null);
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
							ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)),
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
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, ParameterDetail.PARAM_DET_CODE_KETENTUAN_EXTERNAL)), 0,
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
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_KETENTUAN_EXTERNAL);
			regulation.setJenisKetentuan(pd);

			uploadedFilesPeraturanId = new ArrayList<UploadedFileWO>();
			uploadedFilesPeraturanEn = new ArrayList<UploadedFileWO>();
			uploadedFilesSummary = new ArrayList<UploadedFileWO>();
			uploadedFilesPenjelasan = new ArrayList<UploadedFileWO>();
			uploadedFilesFAQ = new ArrayList<UploadedFileWO>();
			uploadedFilesLampiran = new ArrayList<UploadedFileWO>();
			uploadedFilesBuletin = new ArrayList<UploadedFileWO>();
			uploadedFilesMateri = new ArrayList<UploadedFileWO>();

			RegulationTrackRecord rt = new RegulationTrackRecord();
			lastSequenceOfDtl = 1;
			rt.setSequence(lastSequenceOfDtl);
			trList.add(rt);
			regulation.setRegulationTrackRecords(trList);

			actionMode = Constants.ACTION_ADD;
			tableModel = new RegulationTrackRecordTableModel<RegulationTrackRecord>(
					regulation.getRegulationTrackRecords());
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
		regulation = regulationService.findById(idLong);

		if (regulation.getPublishedDate() != null) {
			regulation.setPublishedDateStr(sdf.format(regulation.getPublishedDate()));
		}

		if (regulation.getEffectiveDate() != null) {
			regulation.setEffectiveDateStr(sdf.format(regulation.getEffectiveDate()));
		}

		if (regulation.getExpiredDate() != null) {
			regulation.setExpiredDateStr(sdf.format(regulation.getExpiredDate()));
		}

		if (regulation.getDocumentType() != null && regulation.getDocumentType().getDocumentTypeId() != null) {
			docTypeId = regulation.getDocumentType().getDocumentTypeId();
		}
		if (regulation.getDocumentCategory() != null
				&& regulation.getDocumentCategory().getDocumentCategoryId() != null) {
			docCategoryId = regulation.getDocumentCategory().getDocumentCategoryId();
		}
		if (regulation.getDocumentTopic() != null && regulation.getDocumentTopic().getDocumentTopicId() != null) {
			docTopicId = regulation.getDocumentTopic().getDocumentTopicId();
		}
		
	    lastSequenceOfDtl = 0;
			
		if (regulation.getRegulationTrackRecords() != null) {
			lastSequenceOfDtl = regulation.getRegulationTrackRecords().size();
			for (int i = 0; i < regulation.getRegulationTrackRecords().size(); i++) {
				RegulationTrackRecord dtl = regulation.getRegulationTrackRecords().get(i);
				lastSequenceOfDtl = lastSequenceOfDtl + 1;
				dtl.setSequence(lastSequenceOfDtl);
			}
		}

		tableModel = new RegulationTrackRecordTableModel<RegulationTrackRecord>(regulation.getRegulationTrackRecords());

		uploadedFilesPeraturanId = new ArrayList<UploadedFileWO>();
		uploadedFilesPeraturanEn = new ArrayList<UploadedFileWO>();
		uploadedFilesSummary = new ArrayList<UploadedFileWO>();
		uploadedFilesPenjelasan = new ArrayList<UploadedFileWO>();
		uploadedFilesFAQ = new ArrayList<UploadedFileWO>();
		uploadedFilesLampiran = new ArrayList<UploadedFileWO>();
		uploadedFilesBuletin = new ArrayList<UploadedFileWO>();
		uploadedFilesMateri = new ArrayList<UploadedFileWO>();

		for (int i = 0; i < regulation.getRegulationAttachments().size(); i++) {
			RegulationAttachment ra = regulation.getRegulationAttachments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
		
			if (ra.getAttachmentCode() != null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_REGULATIONS_IN)) {
				uploadedFilesPeraturanId.add(uf);
			} else if (ra.getAttachmentCode() != null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_SUMMARY)) {
				uploadedFilesSummary.add(uf);
			} else if (ra.getAttachmentCode() != null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_EXPLAINATION)) {
				uploadedFilesPenjelasan.add(uf);
			} else if (ra.getAttachmentCode() != null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FAQ)) {
				uploadedFilesFAQ.add(uf);
			} else if (ra.getAttachmentCode() != null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_LAMPIRAN)) {
				uploadedFilesLampiran.add(uf);
			} else if (ra.getAttachmentCode() != null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_MATERIAL)) {
				uploadedFilesMateri.add(uf);
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
		for (int i = 0; i < selectedData.length; i++) {
			regulation.getRegulationTrackRecords().remove(selectedData[i]);
		}
		
		if (regulation.getRegulationTrackRecords() == null
				|| regulation.getRegulationTrackRecords().size() == 0) {
			lastSequenceOfDtl = 0;
		}

		tableModel.setWrappedData(regulation.getRegulationTrackRecords());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void handleFileUploadPeraturanId(FileUploadEvent event)  {
		
		try {
			uploadedFilesPeraturanId = uploadedFilesPeraturanId == null ? new ArrayList<UploadedFileWO>()
					: uploadedFilesPeraturanId;
			uploadedFilesPeraturanId.add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_EKSTERNAL,
							parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}

	}

	public void handleFileUploadSummary(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesSummary = uploadedFilesSummary == null ? new ArrayList<UploadedFileWO>() : uploadedFilesSummary;
			uploadedFilesSummary.add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_EKSTERNAL,
							parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void handleFileUploadPenjelasan(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesPenjelasan = uploadedFilesPenjelasan == null ? new ArrayList<UploadedFileWO>()
					: uploadedFilesPenjelasan;
			uploadedFilesPenjelasan.add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_EKSTERNAL,
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
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_EKSTERNAL,
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
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_EKSTERNAL,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void handleFileUploadMateri(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesMateri = uploadedFilesMateri == null ? new ArrayList<UploadedFileWO>() : uploadedFilesMateri;
		uploadedFilesMateri.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_KETENTUAN_EKSTERNAL,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void deleteAttachment(String fileId,int index, String uploadType) throws Exception {
		//CallApiManager.deleteFile(fileId, parameterDetailService, fileUtil);
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if(uploadType!=null && uploadType.equals(ExternalRegulationConstants.UPLOAD_TYPE_PERATURAN_ID)) {
			uploadedFilesPeraturanId.remove(uploadedFilesPeraturanId.get(index));
		}
		else if(uploadType!=null && uploadType.equals(ExternalRegulationConstants.UPLOAD_TYPE_SUMMARY)) {
			uploadedFilesSummary.remove(uploadedFilesSummary.get(index));
		}
		else if(uploadType!=null && uploadType.equals(ExternalRegulationConstants.UPLOAD_TYPE_PENJELASAN)) {
			uploadedFilesPenjelasan.remove(uploadedFilesPenjelasan.get(index));
		}
		else if(uploadType!=null && uploadType.equals(ExternalRegulationConstants.UPLOAD_TYPE_FAQ)) {
			uploadedFilesFAQ.remove(uploadedFilesFAQ.get(index));
		}
		else if(uploadType!=null && uploadType.equals(ExternalRegulationConstants.UPLOAD_TYPE_LAMPIRAN)) {
			uploadedFilesLampiran.remove(uploadedFilesLampiran.get(index));
		}
		else if(uploadType!=null && uploadType.equals(ExternalRegulationConstants.UPLOAD_TYPE_MATERI)) {
			uploadedFilesMateri.remove(uploadedFilesMateri.get(index));
		}
		
	}
	
	// public Boolean validate(String publishedDate, String expDate, String effDate)
	// {
	public Boolean validate() {
		Boolean flag = false;
		checkValidate = "";
		try {
			
		   if (uploadedFilesPeraturanId.size() == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationAttachmentPeraturan") + " "
						+ facesUtil.retrieveMessage("validateUploadMinOneData"));
				flag = true;
			} else if (regulation.getRegulationTrackRecords() == null
					|| regulation.getRegulationTrackRecords().size() == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationRekamJejak") + " "
						+ facesUtil.retrieveMessage("validateDetailMinOneData"));
				flag = true;
			}
		   
		   if(regulation.getRegulationTrackRecords() !=null && regulation.getRegulationTrackRecords().size() > 0) {
				for(int i=0; i<regulation.getRegulationTrackRecords().size(); i++) {
					 RegulationTrackRecord trackRecord = (RegulationTrackRecord)regulation.getRegulationTrackRecords().get(i);
					if(trackRecord.getTrackCode() == null || trackRecord.getTrackCode().equals("")) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationRekamJejak") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						
						flag = true;
						
					}else {
						if(trackRecord.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_CHANGE)) {
							if(trackRecord.getRegulationLinkName() == null || trackRecord.getRegulationLinkName().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationRegNo") + " "
										+ facesUtil.retrieveMessage("validateRequired"));
								
								flag = true;
								
							}else if(trackRecord.getTrackNote() == null || trackRecord.getTrackNote().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationKetMencabut") + " "
										+ facesUtil.retrieveMessage("validateRequired"));
								
								flag = true;
							}
						}else if(trackRecord.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REVOKE)) {
							if(trackRecord.getRegulationLinkName() == null || trackRecord.getRegulationLinkName().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationRegNo") + " "
										+ facesUtil.retrieveMessage("validateRequired"));
								
								flag = true;
								
							}
						}/*else if(trackRecord.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REGULATION)) {
							if(trackRecord.getRegulationLinkName() == null || trackRecord.getRegulationLinkName().equals("")) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationRegNo") + " "
										+ facesUtil.retrieveMessage("validateRequired"));
								
								flag = true;
								
							}
						}*/
//						else {
//							if(trackRecord.getRegulationLinkName() != null && !trackRecord.getRegulationLinkName().equals("")) {
//								facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationLinkJudul") + " "
//										+ facesUtil.retrieveMessage("validateCanNotBefill"));
//								
//								flag = true;							
//							}
//						}
					}				
				}
			}else {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationRekamJejak") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		   
		   if(!flag) {
			   Regulation regulValid = regulationService.getCheckDataRegulation(regulation.getRegulationId(), ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL, 
						regulation.getDocumentNo(), regulation.getNameIn());
				if (actionMode.equals(Constants.ACTION_EDIT)) {
					if (regulValid != null && regulValid.getRegulationId() != null && regulValid.getRegulationId() > 0) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationDocNo") + " : "
								+ regulation.getDocumentNo() + " "
								+ facesUtil.retrieveMessage("formExternalRegulationRegTitle") + " ID : "
								+ regulation.getNameIn() + " "
								+ facesUtil.retrieveMessage("validateAlreadyExist"));
						flag = true;
					}
				} else {
					if (regulValid != null && regulValid.getRegulationId() != null && regulValid.getRegulationId() > 0) {
						if (regulValid.getEnabledFlag() != null && regulValid.getEnabledFlag().equals("Y")) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationDocNo") + " : "
									+ regulation.getDocumentNo() + " "
									+ facesUtil.retrieveMessage("formExternalRegulationRegTitle") + " ID : "
									+ regulation.getNameIn() + " " + facesUtil.retrieveMessage("validateAlreadyExist"));
							flag = true;
						}else {
							regulationValided = regulationService.findById(regulValid.getRegulationId());
							checkValidate = "DATA_UPDATE";
						}
						
					}
				}
		   }
		   
		
		}catch (Exception e) {
			flag = true;
			facesUtil.addErrMessage("" + e.getMessage());
		}
		
		return flag;
	}

	public void save() {
		try {

			/*
			 * String publishedDate = facesUtil.retrieveRequestParam("PUBLISHED_DATE");
			 * String expiredDate = facesUtil.retrieveRequestParam("EXPIRED_DATE"); String
			 * effectiveDate = facesUtil.retrieveRequestParam("EFFECTIVE_DATE");
			 * 
			 * if (!validate(publishedDate, expiredDate, effectiveDate)) {
			 */
			if (!validate()) {

				
				List<RegulationAttachment> listAttachment = new ArrayList<RegulationAttachment>();
				for (int i = 0; i < uploadedFilesPeraturanId.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesPeraturanId.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_REGULATIONS_IN);
					ra.setAttachmentFile(uf.getFileName());
					ra.setCreatedBy(facesUtil.retrieveUserLogin());
					ra.setCreationDate(new Timestamp(new Date().getTime()));
					ra.setDelId(new Long(0));
					ra.setEnabledFlag(Constants.CONSTANT_YES);
					ra.setFileId(uf.getFileId());
					ra.setFileSize(uf.getFileSize());
					listAttachment.add(ra);

				}

				for (int i = 0; i < uploadedFilesSummary.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesSummary.get(i);
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

				for (int i = 0; i < uploadedFilesPenjelasan.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesPenjelasan.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_EXPLAINATION);
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
					ra.setAttachmentCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FAQ);
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
					ra.setAttachmentCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_LAMPIRAN);
					ra.setAttachmentFile(uf.getFileName());
					ra.setCreatedBy(facesUtil.retrieveUserLogin());
					ra.setCreationDate(new Timestamp(new Date().getTime()));
					ra.setDelId(new Long(0));
					ra.setEnabledFlag(Constants.CONSTANT_YES);
					ra.setFileId(uf.getFileId());
					ra.setFileSize(uf.getFileSize());
					listAttachment.add(ra);

				}

				for (int i = 0; i < uploadedFilesMateri.size(); i++) {
					RegulationAttachment ra = new RegulationAttachment();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesMateri.get(i);
					ra.setRegulation(regulation);
					ra.setAttachmentCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_MATERIAL);
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
					if(rt.getRegulation() == null) {
						rt.setRegulation(regulation);
					}
					
					if(rt.getCreatedBy() == null) {
						rt.setCreatedBy(facesUtil.retrieveUserLogin());
						rt.setCreationDate(new Timestamp(new Date().getTime()));
					}else {
						rt.setLastUpdateBy(facesUtil.retrieveUserLogin());
						rt.setLastUpdateDate(new Timestamp(new Date().getTime()));
					}
					
					rt.setDelId(new Long(0));
					rt.setEnabledFlag(Constants.CONSTANT_YES);
				}

		
				
				
				if (StringUtils.isEmpty(regulation.getJenisKetentuan().getParameterDtlCode())) {
					ParameterDetail pd = parameterDetailService
							.getParameterDetailByParamDtlCode(regulation.getJenisKetentuan().getParameterDtlCode());
					regulation.setJenisKetentuan(pd);
				}

				if (docTypeId != null) {
					DocumentType dt = documentTypeService.findById(docTypeId);
					regulation.setDocumentType(dt);
				}

				if (docCategoryId != null) {
					DocumentCategory dc = documentCategoryService.findById(docCategoryId);
					regulation.setDocumentCategory(dc);
				}

				if (docTopicId != null) {
					DocumentTopic dt = documentTopicService.findById(docTopicId);
					regulation.setDocumentTopic(dt);
				}

				/*
				 * if(!StringUtils.isEmpty(publishedDate)) {
				 * regulation.setPublishedDate(sdf.parse(publishedDate)); }
				 * if(!StringUtils.isEmpty(expiredDate)) {
				 * regulation.setExpiredDate(sdf.parse(expiredDate)); }
				 * if(!StringUtils.isEmpty(effectiveDate)) {
				 * regulation.setEffectiveDate(sdf.parse(effectiveDate)); }
				 */
				regulation.setStatus(ParameterDetail.PARAM_DET_CODE_DATA_NEW);
				
				if(checkValidate !=null && checkValidate.equals("DATA_UPDATE")) {				
					regulationValided.setDocumentTopic(regulation.getDocumentTopic());
					regulationService.updateDataAlreadyExist(regulation, regulationValided, facesUtil.retrieveUserLogin());
					
				}else {
					if (regulation.getRegulationId() != null) {
						regulationValided = regulationService.findById(regulation.getRegulationId());
						regulationValided.setDocumentTopic(regulation.getDocumentTopic());
						
						regulationService.updateDataAlreadyExist(regulation, regulationValided, facesUtil.retrieveUserLogin());
					} else {
						regulation.setCreatedBy(facesUtil.retrieveUserLogin());
						regulation.setCreationDate(new Timestamp(new Date().getTime()));
						regulation.setDelId(new Long(0));
						regulation.setEnabledFlag(Constants.CONSTANT_YES);
						
						regulationService.save(regulation);
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
				externalRegulationApprovalService.processApprove(regulationApp, "Approve By System", facesUtil.retrieveUserLogin(),ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE,ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED);
		       	*/   
				facesUtil.redirect("/pages/externalRegulation/externalRegulation.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() throws Exception {
		
			for (int i = 0; i < uploadedFilesPeraturanId.size(); i++) {
				UploadedFileWO uf = (UploadedFileWO) uploadedFilesPeraturanId.get(i);
				if(uf.getIsNew() == null) {
				CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
				}
			}
			
			for (int i = 0; i < uploadedFilesSummary.size(); i++) {
				UploadedFileWO uf = (UploadedFileWO) uploadedFilesSummary.get(i);
				if(uf.getIsNew() == null) {
					CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
				}
				

			}

			for (int i = 0; i < uploadedFilesPenjelasan.size(); i++) {
				UploadedFileWO uf = (UploadedFileWO) uploadedFilesPenjelasan.get(i);
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

			for (int i = 0; i < uploadedFilesMateri.size(); i++) {
				UploadedFileWO uf = (UploadedFileWO) uploadedFilesMateri.get(i);
				if(uf.getIsNew() == null) {
					CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
				}
				

			}
			facesUtil.redirect("/pages/externalRegulation/externalRegulation.faces");
		
	}
	
   public void linkRegulationListener(AjaxBehaviorEvent vce) {
	   String[] splitClientId = vce.getComponent().getClientId().split(":");
	   Integer indexLink = Integer.parseInt(splitClientId[2]);
	   indexDtl = indexLink;
	   RegulationTrackRecord regulationTrackRecTemp = regulation.getRegulationTrackRecords().get(indexLink);
	   if(regulationTrackRecTemp !=null && regulationTrackRecTemp.getTrackCode() !=null && 
			   (regulationTrackRecTemp.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REGULATION) || 
					   regulationTrackRecTemp.getTrackCode().equals(""))) {
		   regulationTrackRecTemp.setRegulationLinkId(null);
		   regulationTrackRecTemp.setRegulationLinkName("");
	   } else if (regulationTrackRecTemp !=null && regulationTrackRecTemp.getTrackCode() !=null && 
			   regulationTrackRecTemp.getTrackCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_CHANGE)) {
		   //regulationTrackRecTemp.setInActiveFlag("Y");
	   }
	   
	   PrimeFaces.current().executeScript("reInitSelect2();");
//	   RequestContext.getCurrentInstance().execute("reInitSelect2();");
   }
   
   public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject()+" - External Regulation";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String documentNumberTemp = regulation.getDocumentNo() != null ? regulation.getDocumentNo() : "";
//			String documentTitleTemp = regulation.getName();
			
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Sosialisasi");
					emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM, "External Regulation" + " - "
							+ documentNumberTemp);
					
					ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
					String token = Constants.encryptString(regulation.getRegulationId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_APPROVAL_REQUEST_EXTERNAL_REGULATION);
					String urlLink = pdHostName.getNameIn().concat("pages/dashboard/dashboard.faces?token="+token+"&menuId="+menuId);
					emailContent = emailContent.replaceAll("url_link", urlLink);
					
//					for(int x=0;x<socializationTmp.getSocializationPICComplianceTmps().size();x++) {
//						SocializationPICComplianceTmp cd = socializationTmp.getSocializationPICComplianceTmps().get(x);
//						emailTo = cd.getUser().getEmail();
					
						
					ParameterDetail paramEmail = parameterDetailService.getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER);
					emailTo = paramEmail.getNameIn();
						
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_EXTERNAL_REGULATION", "true", parameterDetailService);
						
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
   
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public ExternalRegulationService getExternalRegulationService() {
		return externalRegulationService;
	}

	public void setExternalRegulationService(ExternalRegulationService externalRegulationService) {
		this.externalRegulationService = externalRegulationService;
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
		ExternalRegulationEditBean.logger = logger;
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

	public List<UploadedFileWO> getUploadedFilesSummary() {
		return uploadedFilesSummary;
	}

	public void setUploadedFilesSummary(List<UploadedFileWO> uploadedFilesSummary) {
		this.uploadedFilesSummary = uploadedFilesSummary;
	}

	public List<UploadedFileWO> getUploadedFilesPenjelasan() {
		return uploadedFilesPenjelasan;
	}

	public void setUploadedFilesPenjelasan(List<UploadedFileWO> uploadedFilesPenjelasan) {
		this.uploadedFilesPenjelasan = uploadedFilesPenjelasan;
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

	public List<UploadedFileWO> getUploadedFilesBuletin() {
		return uploadedFilesBuletin;
	}

	public void setUploadedFilesBuletin(List<UploadedFileWO> uploadedFilesBuletin) {
		this.uploadedFilesBuletin = uploadedFilesBuletin;
	}

	public List<UploadedFileWO> getUploadedFilesMateri() {
		return uploadedFilesMateri;
	}

	public void setUploadedFilesMateri(List<UploadedFileWO> uploadedFilesMateri) {
		this.uploadedFilesMateri = uploadedFilesMateri;
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
			
			regulation.getRegulationTrackRecords().get(indexDtl)
					//.setRegulationLinkId(((java.math.BigInteger) objects[0]).longValue());
					  .setRegulationLinkId(reg.getRegulationId());
			//regulation.getRegulationTrackRecords().get(indexDtl).setRegulationLinkName(reg.getName());
			regulation.getRegulationTrackRecords().get(indexDtl).setRegulationLinkName(reg.getDocumentNo());
			
			tableModel.setWrappedData(regulation.getRegulationTrackRecords());
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
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

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public ExternalRegulationApprovalService getExternalRegulationApprovalService() {
		return externalRegulationApprovalService;
	}

	public void setExternalRegulationApprovalService(ExternalRegulationApprovalService externalRegulationApprovalService) {
		this.externalRegulationApprovalService = externalRegulationApprovalService;
	}

	public List<RegulationApproval> getRegApprovals() {
		return regApprovals;
	}

	public void setRegApprovals(List<RegulationApproval> regApprovals) {
		this.regApprovals = regApprovals;
	}

	public RegulationService getRegulationService2() {
		return regulationService2;
	}

	public void setRegulationService2(RegulationService regulationService2) {
		this.regulationService2 = regulationService2;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	
	

}