package com.wo.module.picFollowupConfirmation.bean;

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
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.FileUtil;
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
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.picFollowupConfirmation.service.PICFollowupConfirmationService;
import com.wo.module.regulationSocialization.constant.RegulationSocializationConstants;
import com.wo.module.regulationSocialization.model.SocializationDocumentTmp;
import com.wo.module.regulationSocialization.model.SocializationDocumentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTrc;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTrcTableModel;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupAttachmentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrcTableModel;
import com.wo.module.regulationSocialization.model.SocializationRegulationTrc;
import com.wo.module.regulationSocialization.model.SocializationRegulationTrcTableModel;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocialization.service.RegulationSocializationService;
import com.wo.module.regulationSocialization.service.SocializationPICFollowupTrcService;
import com.wo.module.regulationSocialization.service.SocializationTrcService;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class PICFollowupConfirmationEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(PICFollowupConfirmationEditBean.class);

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

	private List<UploadedFileWO> uploadedFiles;
	
	private List<UploadedFileWO> deletedFiles;

	private SocializationRegulationTrc[] selectedData;

	private SocializationPICComplianceTrc[] selectedDataCompliance;

	private SocializationPICFollowupTrc[] selectedDataFollowup;

	private SocializationRegulationTrcTableModel<SocializationRegulationTrc> tableModel;

	private SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc> tableModelCompliance;

	private SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc> tableModelFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private List<SocializationApprovalVO> tableApproval;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;

	private Integer indexDtlCompliance;

	private Integer indexDtlFollowup;

	private PICFollowupConfirmationService picFollowupConfirmationService;

	private RegulationSocializationService regulationSocializationService;

	private SocializationTrcService socializationTrcService;
	
	private SocializationPICFollowupTrcService socializationPICFollowupTrcService;

	//private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private RegulationService regulationService;

	private UserService userService;

	private CounterTypeService counterTypeService;
	
	private EmailTemplateService emailTemplateService;
	
	private List<SelectItem> complianceStatusList;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;
	
	private Long socializationPICFollowupTrcId;

	private String navigateSearch = ExternalRegulationConstants.NAVIGATE_SEARCH;
	
	private List<SocializationPICFollowupTrc> complianceTableList;
	
	private String viewOnly;
	
	private SocializationPICFollowupTrc socializationPICFollowupTrc;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private String textWarningUpload;
	
	private List<UploadedFileWO> uploadFiles;

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
		selectComplianceStatus();

		selectorJdlPeraturan = RegulationSocializationConstants.buildSelectorJdlPeraturan(facesUtil);
		selectorCompliance = RegulationSocializationConstants.buildSelectorPICCompliance(facesUtil);
		selectorFollowup = RegulationSocializationConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);

		tableApproval = new ArrayList<SocializationApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
		facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
//		redirectCurrentPage();
		String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
		
		String viewId = facesUtil.retrieveRequestParam("viewId");
		/*String token = facesUtil.retrieveRequestParam("token");
		
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}*/
		
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

	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
		uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;
		uploadedFiles.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_PIC_FOLLOWUP,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void deleteAttachment(String fileId,int index) throws Exception {
		//CallApiManager.deleteFile(fileId, parameterDetailService, fileUtil);
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		
		uploadedFiles.remove(uploadedFiles.get(index));
		
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		socializationPICFollowupTrcId = idLong;
		socializationPICFollowupTrc = socializationPICFollowupTrcService.findById(idLong);
		complianceTableList = new ArrayList<SocializationPICFollowupTrc>();
		complianceTableList.add(socializationPICFollowupTrc);
		socializationTrc = socializationTrcService.findById(socializationPICFollowupTrc.getSocializationTrc().getSocializationId());
		
		if(socializationTrc.getCounterType() != null) {
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

		tableModel = new SocializationRegulationTrcTableModel<SocializationRegulationTrc>(
				socializationTrc.getSocializationRegulationTrcs());
		tableModelCompliance = new SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc>(
				socializationTrc.getSocializationPICComplianceTrcs());
		tableModelFollowup = new SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc>(
				socializationTrc.getSocializationPICFollowupTrcs());

		tableApproval = regulationSocializationService.getDataApprovalBySocializationId(idLong);
		tableStatus = regulationSocializationService.getDataConfirmStatusBySocializationId(idLong);
		
		if (socializationPICFollowupTrc != null && socializationPICFollowupTrc.getSocializationPICFollowupAttachmentTrcs() != null
				&& socializationPICFollowupTrc.getSocializationPICFollowupAttachmentTrcs().size() > 0) {
			uploadedFiles = new ArrayList<UploadedFileWO>();
			for (SocializationPICFollowupAttachmentTrc dtl : socializationPICFollowupTrc.getSocializationPICFollowupAttachmentTrcs()) {
				UploadedFileWO file = new UploadedFileWO();
				file.setFileId(dtl.getFileId());
				file.setFileName(dtl.getAttachmentFile());
				file.setIsNew(false);
				file.setFileSize(dtl.getFileSize());
				uploadedFiles.add(file);
			}
		}
			
		
		if (socializationPICFollowupTrc.getFollowupStatus() != null && socializationPICFollowupTrc.getFollowupStatus().getParameterDtlCode() != null
				&& socializationPICFollowupTrc.getFollowupStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";			

			SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
			
			facesUtil.addWarnMessage(
					facesUtil.retrieveMessage("formRegulationSocializationNotifConfirmationDone", 
					sdf.format(socializationPICFollowupTrc.getTargetDate()),
					socializationPICFollowupTrc.getFollowupBy().getName()));
			
		} else {
			viewOnly = "N";
		}

	}

	public Boolean isError() {
		Boolean flag = false;

		if (socializationPICFollowupTrc.getFollowupDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationFollowupDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		
		if (uploadedFiles == null || uploadedFiles.size() <= 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formPICFollowupConfirmationPICConfirmationEvidence") + " File "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		return flag;
	}
	
	public void sendEmail() throws Exception {
		
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
			String emailSubject = emailTemplate.getEmailSubject();
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			
			String documentNumberTemp = "";
			String documentTitleTemp = "";
			for (int i = 0; i < socializationTrc.getSocializationRegulationTrcs().size(); i++) {
				SocializationRegulationTrc temp = socializationTrc.getSocializationRegulationTrcs().get(0);
				documentNumberTemp = temp.getRegulation().getDocumentNo();
				documentTitleTemp = temp.getRegulation().getName();
			}
			
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Sosialisasi");
					emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Sosialisasi" + " - "
							+ documentNumberTemp + "_" + documentTitleTemp);
					
					emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Sosialisasi" + " - "
							+ documentNumberTemp + "_" + documentTitleTemp);
					
					for(int x=0;x<socializationTrc.getSocializationPICComplianceTrcs().size();x++) {
						SocializationPICComplianceTrc cd = socializationTrc.getSocializationPICComplianceTrcs().get(x);
						emailTo = cd.getUser().getEmail();
						
						
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
						
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
				        
					}
					
				
		
	}

	public void save() {
		try {
			if (!isError()) {
				User user = (User) facesUtil.getUserLogin();

				picFollowupConfirmationService.processConfirm(socializationTrc, socializationPICFollowupTrcId, 
						socializationPICFollowupTrc.getFollowupNote(), socializationPICFollowupTrc.getFollowupDate(), uploadedFiles,user);
				
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
				
				

				facesUtil.redirect("/pages/picFollowupConfirmation/picFollowupConfirmation.faces");
			}
			
			

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void cancel() {
		try {
			if(uploadedFiles != null) {
				for (int i = 0; i < uploadedFiles.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			
			facesUtil.redirect("/pages/picFollowupConfirmation/picFollowupConfirmation.faces");
		} catch (Exception e) {
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
		PICFollowupConfirmationEditBean.logger = logger;
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

	public SocializationRegulationTrc[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(SocializationRegulationTrc[] selectedData) {
		this.selectedData = selectedData;
	}

	public SocializationTrc getSocializationTrc() {
		return socializationTrc;
	}

	public void setSocializationTrc(SocializationTrc socializationTrc) {
		this.socializationTrc = socializationTrc;
	}

	public SocializationRegulationTrcTableModel<SocializationRegulationTrc> getTableModel() {
		return tableModel;
	}

	public void setTableModel(SocializationRegulationTrcTableModel<SocializationRegulationTrc> tableModel) {
		this.tableModel = tableModel;
	}

	public SocializationTrcService getSocializationTrcService() {
		return socializationTrcService;
	}

	public void setSocializationTrcService(SocializationTrcService socializationTrcService) {
		this.socializationTrcService = socializationTrcService;
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

	public SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			SocializationPICComplianceTrcTableModel<SocializationPICComplianceTrc> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public SocializationPICComplianceTrc[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(SocializationPICComplianceTrc[] selectedDataCompliance) {
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

	public SocializationPICFollowupTrc[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(SocializationPICFollowupTrc[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(
			SocializationPICFollowupTrcTableModel<SocializationPICFollowupTrc> tableModelFollowup) {
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

	public PICFollowupConfirmationService getPicFollowupConfirmationService() {
		return picFollowupConfirmationService;
	}

	public void setPicFollowupConfirmationService(PICFollowupConfirmationService picFollowupConfirmationService) {
		this.picFollowupConfirmationService = picFollowupConfirmationService;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
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

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public Long getSocializationPICFollowupTrcId() {
		return socializationPICFollowupTrcId;
	}

	public void setSocializationPICFollowupTrcId(Long socializationPICFollowupTrcId) {
		this.socializationPICFollowupTrcId = socializationPICFollowupTrcId;
	}

	public SocializationPICFollowupTrcService getSocializationPICFollowupTrcService() {
		return socializationPICFollowupTrcService;
	}

	public void setSocializationPICFollowupTrcService(SocializationPICFollowupTrcService socializationPICFollowupTrcService) {
		this.socializationPICFollowupTrcService = socializationPICFollowupTrcService;
	}

	public List<SocializationPICFollowupTrc> getComplianceTableList() {
		return complianceTableList;
	}

	public void setComplianceTableList(List<SocializationPICFollowupTrc> complianceTableList) {
		this.complianceTableList = complianceTableList;
	}

	public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
	}

	public SocializationPICFollowupTrc getSocializationPICFollowupTrc() {
		return socializationPICFollowupTrc;
	}

	public void setSocializationPICFollowupTrc(SocializationPICFollowupTrc socializationPICFollowupTrc) {
		this.socializationPICFollowupTrc = socializationPICFollowupTrc;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
	

}