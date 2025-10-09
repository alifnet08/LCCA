package com.wo.module.externalRegulationApproval.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.model.RegulationTrackRecordTableModel;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.externalRegulationApproval.service.ExternalRegulationApprovalService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.service.UserService;

public class ExternalRegulationApprovalEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ExternalRegulationApprovalEditBean.class);

	private Regulation regulation;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long docTypeId;

	private Long docCategoryId;

	private Long docTopicId;
	
	private String approvalStat;

	private List<SelectItem> provTypes;

	private List<SelectItem> trackCodes;

	private List<SelectItem> docTypes;

	private List<SelectItem> docCategories;

	private List<SelectItem> docTopics;
	
	private List<SelectItem> approvalStatus;

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

	private ExternalRegulationApprovalService externalRegulationApprovalService;

	private RegulationService regulationService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;
	
	private UserService userService;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;

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
		selectProvType();
		selectTrackCode();
		selectDocType();
		selectDocCategory();
		selectDocTopic();
		selectApprovalStatus();
		
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	public void selectApprovalStatus() {
		approvalStatus = new ArrayList<SelectItem>();
		try {
			approvalStatus = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_APPROVAL_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN);
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
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_TRACK_RECORD);
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
					Arrays.asList(new DefaultSearchObject("JENIS_KETENTUAN_EKSTERNAL", null)), 0, Integer.MAX_VALUE,
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
					Arrays.asList(new DefaultSearchObject("JENIS_KETENTUAN_EKSTERNAL", null)), 0, Integer.MAX_VALUE,
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

			RegulationTrackRecord rt = new RegulationTrackRecord();
			trList.add(rt);
			rt.setSequence(1);
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
		
		if(regulation.getPublishedDate()!=null) {
			regulation.setPublishedDateStr(sdf.format(regulation.getPublishedDate()));
		}
		
		if(regulation.getEffectiveDate()!=null) {
			regulation.setEffectiveDateStr(sdf.format(regulation.getEffectiveDate()));
		}
		
		if(regulation.getExpiredDate()!=null) {
			regulation.setExpiredDateStr(sdf.format(regulation.getExpiredDate()));
		}
		
		if(regulation.getDocumentType()!=null && regulation.getDocumentType().getDocumentTypeId()!=null) {
			docTypeId = regulation.getDocumentType().getDocumentTypeId();
		}
		if(regulation.getDocumentCategory()!=null && regulation.getDocumentCategory().getDocumentCategoryId()!=null) {
			docCategoryId = regulation.getDocumentCategory().getDocumentCategoryId();
		}
		if(regulation.getDocumentTopic()!=null && regulation.getDocumentTopic().getDocumentTopicId()!=null) {
			docTopicId = regulation.getDocumentTopic().getDocumentTopicId();
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
		
		for(int i=0; i<regulation.getRegulationAttachments().size();i++) {
			RegulationAttachment ra= regulation.getRegulationAttachments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileId(ra.getFileId());
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileSize(ra.getFileSize());
			
			if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_REGULATIONS_IN)) {
				uploadedFilesPeraturanId.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_SUMMARY)) {
				uploadedFilesSummary.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_EXPLAINATION)) {
				uploadedFilesPenjelasan.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FAQ)) {
				uploadedFilesFAQ.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_LAMPIRAN)) {
				uploadedFilesLampiran.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_MATERIAL)) {
				uploadedFilesMateri.add(uf);
			}
		}
		
		for(int i=0; i<regulation.getRegulationTrackRecords().size();i++) {
			RegulationTrackRecord rtr = (RegulationTrackRecord)regulation.getRegulationTrackRecords().get(i);
			if(rtr.getRegulationLinkId()!=null) {
//				Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
//				String name= "";
				
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

	}
	
	public void save() {
		if (StringUtils.isBlank(approvalStat)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("textApprovalStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
		} else if (ParameterDetail.PARAM_DET_CODE_STATUS_REVISE.equals(approvalStat)) {
			revise();
		} else if (ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED.equals(approvalStat)) {
			approve();
		}
	}

	

	public void approve() {
		try {
			String note = facesUtil.retrieveRequestParam("NOTE");
			if(regulation.getNameIn().contains("(deleted)")){
				externalRegulationApprovalService.processApprove(regulation, note, facesUtil.retrieveUserLogin(),"DATA_DELETED","STATUS_APPROVED");
				externalRegulationApprovalService.procedureUpdateTmpTrackRecord(regulation.getRegulationId());
			}else{
			externalRegulationApprovalService.processApprove(regulation, note, facesUtil.retrieveUserLogin(),ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE,ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED);
       	   // externalRegulationApprovalService.procedureUpdateTmpTrackRecord(regulation.getRegulationId());
			
			externalRegulationApprovalService.procedureUpdateTmpTrackRecord(regulation.getRegulationId());
			}
			
			// this should be a singleton
	        ExecutorService emailExecutor = Executors.newCachedThreadPool();

	        // from you sendEmail() method
	        emailExecutor.execute(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                	sendEmailApprove();
	                } catch (Exception e) {
	                    logger.error("send email failed", e);
	                }
	            }
	        });
		 	
			facesUtil.redirect("/pages/externalRegulationApproval/externalRegulationApproval.faces");
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void revise() {
		try {
			String note = facesUtil.retrieveRequestParam("NOTE");
			if(StringUtils.isEmpty(note)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationApprovalNote") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
			}else {
			externalRegulationApprovalService.processApprove(regulation, note, facesUtil.retrieveUserLogin(),"DATA_REVISE","STATUS_REVISE");
			// this should be a singleton
	        ExecutorService emailExecutor = Executors.newCachedThreadPool();

	        // from you sendEmail() method
	        emailExecutor.execute(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                	sendEmailReject();
	                } catch (Exception e) {
	                    logger.error("send email failed", e);
	                }
	            }
	        });
			facesUtil.redirect("/pages/externalRegulationApproval/externalRegulationApproval.faces");
			}
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}
	
	public void sendEmailApprove() {
		try {
			
			String emailSubject = "LCCA Approval Notification - External Regulation";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String docNo = regulation.getDocumentNo() != null ? regulation.getDocumentNo() : "";

			emailContent = "("+docNo+" - "+regulation.getNameIn()+") telah disetujui <br/><br/>";
			if(regulation.getRegulationApprovals()!=null && regulation.getRegulationApprovals().size()>0){
			emailContent = emailContent+"Catatan: <br/>";
			emailContent = emailContent+""+regulation.getRegulationApprovals().get(regulation.getRegulationApprovals().size()-1).getApprovalNote()+ "";
			}
			emailTo = userService.getUserByNik(regulation.getCreatedBy()).getEmail();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_EXTERNAL_REGULATION", "true", parameterDetailService);
						
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void sendEmailReject() {
		try {
			
			String emailSubject = "LCCA Approval Notification - External Regulation";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String docNo = regulation.getDocumentNo() != null ? regulation.getDocumentNo() : "";

			emailContent = "("+docNo+" - "+regulation.getNameIn()+") telah ditolak <br/><br/>";
			if(regulation.getRegulationApprovals()!=null && regulation.getRegulationApprovals().size()>0){
			emailContent = emailContent+"Catatan: <br/>";
			emailContent = emailContent+""+regulation.getRegulationApprovals().get(regulation.getRegulationApprovals().size()-1).getApprovalNote()+ "";
			}
			emailTo = userService.getUserByNik(regulation.getCreatedBy()).getEmail();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_EXTERNAL_REGULATION", "true", parameterDetailService);
						

			
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/externalRegulationApproval/externalRegulationApproval.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public ExternalRegulationApprovalService getExternalRegulationApprovalService() {
		return externalRegulationApprovalService;
	}

	public void setExternalRegulationApprovalService(ExternalRegulationApprovalService externalRegulationApprovalService) {
		this.externalRegulationApprovalService = externalRegulationApprovalService;
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
		ExternalRegulationApprovalEditBean.logger = logger;
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

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
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

	public List<SelectItem> getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(List<SelectItem> approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getApprovalStat() {
		return approvalStat;
	}

	public void setApprovalStat(String approvalStat) {
		this.approvalStat = approvalStat;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	

}