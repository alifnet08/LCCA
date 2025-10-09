package com.wo.module.trcFineApproval.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.trcFineApproval.constant.TrcFineApprovalConstants;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFineAttachment;
import com.wo.module.trcFineApproval.model.TrcFineDocument;
import com.wo.module.trcFineApproval.model.TrcFinePicCompliance;
import com.wo.module.trcFineApproval.model.TrcFinePicComplianceTableModel;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupAttachment;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupEmail;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupHistory;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupTableModel;
import com.wo.module.trcFineApproval.service.TrcFineApprovalService;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TrcFineApprovalEditBean extends CommonBean implements  SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TrcFineApprovalEditBean.class);

	private TrcFine trcFine;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;

	private List<UploadedFileWO> uploadedFilesEvidence;

	private TrcFinePicCompliance[] selectedPicComplianceData;
	

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;
	private SelectorInfo selectorPicAttendance;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesDocument;
	private List<UploadedFileWO> uploadedFilesProofOfPayment;

	private TrcFinePicComplianceTableModel<TrcFinePicCompliance> tablePicComplianceModel;
	private TrcFinePicFollowupTableModel<TrcFinePicFollowup> tablePicFollowupModel;
	

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;
	
	private Integer indexDtlPicAttendance;
	private Integer lastSequenceOfPicAttendance;

	// private List<TrcRmd> trcRmdList;
	private List<TrcFine> trcFineList;
	private List<TrcFinePicFollowup> trcFineFollowups;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TrcFineApprovalService trcFineApprovalService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;
	private RCService rcService;
	private HolidayService holidayService;

	public FacesUtil facesUtil;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> approvalStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> attendanceList;
	
	private List<SelectItem> reminderStatusList;
	private List<SelectItem> fineTypeCodeList;
	private List<SelectItem> rcList;
	private List<SelectItem> categoryList;
	private List<SelectItem> reportNameList;
	
	private FileUtil fileUtil;
	
	private String viewOnly;
	
	private Boolean isOtherReportNameChosen = false;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;
	private String textWarningUpload;

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
		initList();
		selectorPicAttendance = TrcFineApprovalConstants.buildSelectorUserAttendace();
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
		facesUtil.removeSessionAttribute(Constants.SESSION_NEED_REDIRECT);
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText != null ? getText.getName() : "";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initList() {
		try {
			
			trcFineFollowups = new ArrayList<TrcFinePicFollowup>();
			
			approvalStatusList = new ArrayList<SelectItem>();
			
			List<ParameterDetail> listApprovalDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FINE_TARGET_DATE_STATUS);
			
			for (ParameterDetail vo : listApprovalDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				approvalStatusList.add(si);
			}
			
			complianceStatusList = new ArrayList<SelectItem>();

			
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
			
			
			rcList = new ArrayList<SelectItem>();
			List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
			List<RC> listRC = rcService.searchData(searchCriteria, 0, Integer.MAX_VALUE, null, null);
			
			for (RC vo : listRC) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getRegionCode()+"-"+vo.getWorkingUnit()+"-"+vo.getRegion());
				si.setValue(vo.getRcId());
				rcList.add(si);
			}
			
			categoryList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FINE_CATEGORY);

			for (ParameterDetail vo : listCategory) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				categoryList.add(si);
			}
			
			senderCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_SENDER);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				senderCodeList.add(si);
			}

			reminderStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamReminderDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamReminderDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}

			counterTypeList = counterTypeService.getAllCounterTypeLabelValue();

			divisionList = new ArrayList<SelectItem>();
			List<Division> listDiv = userService.getAllDivision();
			for (Division vo : listDiv) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisionList.add(si);
			}

			yesNoList = new ArrayList<SelectItem>();
			yesNoList.add(new SelectItem(Constants.CONSTANT_YES, "Yes"));
			yesNoList.add(new SelectItem(Constants.CONSTANT_NO, "No"));
			
			attendanceList = new ArrayList<SelectItem>();
			List<ParameterDetail> listAttendance = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_ATTENDANCE);
			for (ParameterDetail vo : listAttendance) {
				SelectItem si =  new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				attendanceList.add(si);
			}
			
			fineTypeCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listFineTypeDtl = parameterDetailService
					.getParameterDetailByParamCodeOrdered(ParameterHeader.PARAM_HEAD_CODE_CORRESPONDENCE_TYPE);

			for (ParameterDetail vo : listFineTypeDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				fineTypeCodeList.add(si);
			}
			
			reportNameList = new ArrayList<SelectItem>();
			List<ParameterDetail> reportNameDtl = parameterDetailService
					.getParameterDetailByParamCodeOrdered(ParameterHeader.PARAM_HEAD_FINE_REPORT_NAME);
			
			for (ParameterDetail vo : reportNameDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reportNameList.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleFileUploadEvidence(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesEvidence = uploadedFilesEvidence == null ? new ArrayList<UploadedFileWO>() : uploadedFilesEvidence;
		uploadedFilesEvidence.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
				Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC, parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
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
			//this.handleNew();
		} else {
			this.handleEdit(editId);
			onOtherReportName();
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
		trcFine = trcFineApprovalService.findById(idLong);

		trcFine.setNotes(trcFine.getNotesDecrypted());
				
		
//		TrcFine trcFine = trcFineApprovalService.findById(idLong);
		
		trcFine.setAttendance(new ParameterDetail());
		
		if (trcFine != null) {
			trcFineList = new ArrayList<TrcFine>();
			trcFineList.add(trcFine);
		}
		

		if (trcFine.getTrcFinePicCompliances() != null) {
			for (int i = 0; i < trcFine.getTrcFinePicCompliances().size(); i++) {
				TrcFinePicCompliance dtl = (TrcFinePicCompliance) trcFine
						.getTrcFinePicCompliances().get(i);
				if (dtl.getUser() != null) {
					dtl.setNikTemp(dtl.getUser().getNik());
					dtl.setNameTemp(dtl.getUser().getName());
					dtl.setEmailTemp(dtl.getUser().getEmail());
				}
			}
		}

		

		uploadedFilesDocument = new ArrayList<UploadedFileWO>();

		for (int i = 0; i < trcFine.getTrcFineDocuments().size(); i++) {
			TrcFineDocument ra = trcFine.getTrcFineDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileId(ra.getFileId());
			uf.setFileName(ra.getAttachmentFile());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesDocument.add(uf);

		}
		
		for(int i = 0; i < trcFine.getTrcFinePicFollowups().size(); i++){
			TrcFinePicFollowup trcFinePicFollowup = trcFine.getTrcFinePicFollowups().get(i);
			if(trcFinePicFollowup.getComplianceStatus() == null) {
				trcFinePicFollowup.setComplianceStatus(new ParameterDetail());
				trcFinePicFollowup.getComplianceStatus().setParameterDtlCode("");
			}
			
			if(trcFinePicFollowup.getTargetDateStatus() == null) {
				trcFinePicFollowup.setTargetDateStatus(new ParameterDetail());
				trcFinePicFollowup.getTargetDateStatus().setParameterDtlCode("");
			}
			
			if(trcFinePicFollowup.getFollowupStatus() != null 
					&& trcFinePicFollowup.getFollowupStatus().getParameterDtlCode() != null
					&& (trcFinePicFollowup.getFollowupStatus().getParameterDtlCode()
					.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE) || trcFinePicFollowup.getFollowupStatus().getParameterDtlCode()
					.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION))) {
				trcFineFollowups.add(trcFinePicFollowup);
			}
			
		}

		uploadedFilesEvidence = new ArrayList<UploadedFileWO>();
		
		uploadedFilesProofOfPayment = new ArrayList<UploadedFileWO>();
		
		tablePicComplianceModel = new TrcFinePicComplianceTableModel<TrcFinePicCompliance>(
				trcFine.getTrcFinePicCompliances());
		
		List<TrcFinePicFollowup> pfList = trcFine.getTrcFinePicFollowups();
		
		for (int i = 0; i < pfList.size(); i++) {
			TrcFinePicFollowup pf = pfList.get(i);
			List<TrcFinePicFollowupAttachment> trcFinePicExtendeds = new ArrayList<>();
			List<TrcFinePicFollowupAttachment> trcFinePicFollowups = new ArrayList<>();
			pf.setTrcFinePicExtendeds(trcFinePicExtendeds);
			pf.setTrcFinePicFollowups(trcFinePicFollowups);
			
			if(pf.getTrcFinePicFollowupAttachments() != null) {
				for (int j = 0; j < pf.getTrcFinePicFollowupAttachments().size() ; j++) {
					TrcFinePicFollowupAttachment pfa = pf.getTrcFinePicFollowupAttachments().get(j);
					if(pfa.getAttachmentFrom() != null && pfa.getAttachmentFrom().equals(ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED)) {
						trcFinePicExtendeds.add(pfa);
					}else {
						trcFinePicFollowups.add(pfa);
					}
				}
			}
		}
		
		tablePicFollowupModel = new TrcFinePicFollowupTableModel<TrcFinePicFollowup>(
				trcFine.getTrcFinePicFollowups());

		/*for (int i = 0; i < trcFine.getTrcFinePicFollowupAttachments().size(); i++) {
			TrcFinePicFollowupAttachment ra = trcFine.getTrcFinePicFollowupAttachments()
					.get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileId(ra.getFileId());
			uf.setFileName(ra.getAttachmentFile());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesEvidence.add(uf);
		}
		
		tableSupportingUnitModel = new TrcFineSupportingUnitTableModel<TrcFineSupportingUnit>(
				trcFine.getTrcFineSupportingUnits());
		tablePicComplianceModel = new TrcFinePicComplianceTableModel<TrcFinePicCompliance>(
				trcFine.getTrcFinePicCompliances());
		
		tableAttedanceModel = new TrcFinePicFollowupAttendanceTableModel<TrcFinePicFollowupAttendance>(
				trcFine.getTrcFinePicFollowupAttendance());*/
		
		if (trcFine.getFollowupStatus() != null && trcFine.getFollowupStatus().getParameterDtlCode() != null
				&& trcFine.getFollowupStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
			viewOnly = "Y";
			
			SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
			
			facesUtil.addWarnMessage(
					facesUtil.retrieveMessage("formTmpFineNotifConfirmationDone", 
					sdf.format(trcFine.getTargetDate()),
					trcFine.getFollowupBy().getName()));
		} else {
			viewOnly = "N";
		}
	}

	public Boolean validate() {
		Boolean flag = false;
		
		if (trcFine.getFineId() != null) {
			for(int i=0; i<trcFine.getTrcFinePicFollowups().size();i++){
				TrcFinePicFollowup trcFinePicFollowup = (TrcFinePicFollowup)trcFine.getTrcFinePicFollowups().get(i);
				String complianceStatusCd = trcFinePicFollowup.getComplianceStatus() != null ? trcFinePicFollowup.getComplianceStatus().getParameterDtlCode() : "";
				String targetStatusCd = trcFinePicFollowup.getTargetDateStatus() != null ? trcFinePicFollowup.getTargetDateStatus().getParameterDtlCode() : "";
				
				if(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN.equals(complianceStatusCd) 
						&& StringUtils.isEmpty(trcFinePicFollowup.getComplianceNote())) {

					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineComplianceNote") + " PIC "
							+ trcFinePicFollowup.getFollowupBy().getNik()+"-"+trcFinePicFollowup.getFollowupBy().getName() + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
					
				}else if(ParameterDetail.PARAM_DET_FINE_TARGET_DATE_NOT_APPROPRIATE.equals(targetStatusCd)
							&& StringUtils.isEmpty(trcFinePicFollowup.getComplianceNote())){
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineComplianceNote") + " PIC "
							+ trcFinePicFollowup.getFollowupBy().getNik()+"-"+trcFinePicFollowup.getFollowupBy().getName() + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
					
				}
				
			}
			
		}
		
		

		return flag;
	}
	
	public void onOtherReportName() {
		if (trcFine.getReportName().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_FINE_LHP_OTHER)) {
			isOtherReportNameChosen = true;
		} else {
			isOtherReportNameChosen = false;
		}
	}
	
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
			if(emailTemplate == null) {
				logger.info("Template Email not found..");
				return;
			}
			
			String emailSubject = emailTemplate.getEmailSubject();
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Korespondensi");
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Korespondensi" + " - " + trcFine.getLetterNo());
			emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Korespondensi" + " - " + trcFine.getLetterNo());
					
					for(int x=0;x<trcFine.getTrcFinePicCompliances().size();x++) {
						TrcFinePicCompliance cd = trcFine.getTrcFinePicCompliances().get(x);
						emailTo = cd.getUser().getEmail();
						
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
  
					}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void sendEmailOpen(TrcFinePicFollowup trcFinePicFollowup) {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_FINE_VERIFICATION_OPEN");
			
			if(emailTemplate == null) {
				logger.info("Template Email not found..");
				return;
			}
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			
			String emailSubject = emailTemplate.getEmailSubject();
			emailSubject = emailSubject.replace("letter_no","Denda" + " - " + trcFine.getLetterNo())
					.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo())
					.replaceAll("counter_type","");
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
			String token = "";
			String urlLink = "";
			String menuId = "";
			
			token = Constants.encryptString(trcFinePicFollowup.getFinePicFollowupId().toString());
			menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_FINE);
			urlLink = pdHostName.getNameIn().concat("pages/fineFE/fineFEEdit.faces?token="+token+"&menuId="+menuId+"&first=0");
		    
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Korespondensi");
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo());
			
			emailContent = emailContent.replaceAll("url_link", urlLink);
					for(int x=0;x<trcFine.getTrcFinePicCompliances().size();x++) {
						TrcFinePicCompliance cd = trcFine.getTrcFinePicCompliances().get(x);
						emailTo = cd.getUser().getEmail();
						
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
  
					}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void sendEmailClosed(TrcFinePicFollowup trcFinePicFollowup) {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_FINE_VERIFICATION_CLOSE");
			if(emailTemplate == null) {
				logger.info("Template Email not found..");
				return;
			}
			
			String emailSubject = emailTemplate.getEmailSubject();
			emailSubject = emailSubject.replace("letter_no","Denda" + " - " + trcFine.getLetterNo())
					.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo())
					.replaceAll("counter_type","");
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Korespondensi");
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo());
			for(int x=0;x<trcFine.getTrcFinePicCompliances().size();x++) {
				TrcFinePicCompliance cd = trcFine.getTrcFinePicCompliances().get(x);
				emailTo = cd.getUser().getEmail();
				
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				//final String to = "h3ndr407@gmail.com";
				final String cc = emailCc;
				
				CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
  
					}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void sendEmailSesuai(TrcFinePicFollowup trcFinePicFollowup) {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_FINE_VERIFICATION_OK");
			if(emailTemplate == null) {
				logger.info("Template Email not found..");
				return;
			}
			
			String emailSubject = emailTemplate.getEmailSubject();
			emailSubject = emailSubject.replace("letter_no","Denda" + " - " + trcFine.getLetterNo())
					.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo())
					.replaceAll("counter_type","");
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Korespondensi");
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo());
			for(int x=0;x<trcFine.getTrcFinePicCompliances().size();x++) {
				TrcFinePicCompliance cd = trcFine.getTrcFinePicCompliances().get(x);
				emailTo = cd.getUser().getEmail();
				

				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				//final String to = "h3ndr407@gmail.com";
				final String cc = emailCc;
				
				CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
  
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void sendEmailRevise(TrcFinePicFollowup trcFinePicFollowup) {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_FINE_VERIFY_EXTEND_NOT_OK");
			if(emailTemplate == null) {
				logger.info("Template Email not found..");
				return;
			}
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			
			String emailSubject = emailTemplate.getEmailSubject();
			emailSubject = emailSubject.replace("letter_no","Denda" + " - " + trcFine.getLetterNo())
					.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo())
					.replaceAll("counter_type","");
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
			String token = "";
			String urlLink = "";
			String menuId = "";
			
			token = Constants.encryptString(trcFinePicFollowup.getFinePicFollowupId().toString());
			menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_FINE);
			urlLink = pdHostName.getNameIn().concat("pages/fineFE/fineFEEdit.faces?token="+token+"&menuId="+menuId+"&first=0");
		    
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Korespondensi");
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo());
			emailContent = emailContent.replaceAll("url_link", urlLink);
			for(int x=0;x<trcFine.getTrcFinePicCompliances().size();x++) {
				TrcFinePicCompliance cd = trcFine.getTrcFinePicCompliances().get(x);
				emailTo = cd.getUser().getEmail();
				
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				//final String to = "h3ndr407@gmail.com";
				final String cc = emailCc;
				
				CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
  
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void sendEmailApproved(TrcFinePicFollowup trcFinePicFollowup) {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_FINE_VERIFY_EXTEND_OK");
			if(emailTemplate == null) {
				logger.info("Template Email not found..");
				return;
			}
			
			String emailSubject = emailTemplate.getEmailSubject();
			emailSubject = emailSubject.replace("letter_no","Denda" + " - " + trcFine.getLetterNo())
					.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo())
					.replaceAll("counter_type","");
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Korespondensi");
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo());
					for(int x=0;x<trcFine.getTrcFinePicCompliances().size();x++) {
						TrcFinePicCompliance cd = trcFine.getTrcFinePicCompliances().get(x);
						emailTo = cd.getUser().getEmail();
						
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
  
					}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public void handleFileUploadProofOfPayment(FileUploadEvent event) throws Exception {
		try {
			System.out.println(event.getFile().getFileName());
			uploadedFilesProofOfPayment = uploadedFilesProofOfPayment == null ? new ArrayList<UploadedFileWO>()
					: uploadedFilesProofOfPayment;
			uploadedFilesProofOfPayment.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
					Constants.COMPLIANCE_DOC_TYPE_BUKTI_PEMBAYARAN_DENDA, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
			System.out.println("size=="+uploadedFilesProofOfPayment.size());
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void save() {
		try {

			if (!validate()) {
				
				if (trcFine.getFineId() != null) {
					for(int i=0; i<trcFine.getTrcFinePicFollowups().size();i++){
						TrcFinePicFollowup trcFinePicFollowup = (TrcFinePicFollowup)trcFine.getTrcFinePicFollowups().get(i);
						
						ParameterDetail complianceStatus = parameterDetailService.getParameterDetailByParamDtlCode(trcFinePicFollowup.getComplianceStatus().getParameterDtlCode());
						ParameterDetail targeDatetStatus = parameterDetailService.getParameterDetailByParamDtlCode(trcFinePicFollowup.getTargetDateStatus().getParameterDtlCode());
						
						trcFinePicFollowup.setComplianceBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));
						trcFinePicFollowup.setComplianceStatus(complianceStatus);
						trcFinePicFollowup.setTargetDateStatus(targeDatetStatus);
						trcFinePicFollowup.setComplianceDate(new Timestamp(new Date().getTime()));
						
						if(trcFinePicFollowup.getConfirmationDate() == null) trcFinePicFollowup.setConfirmationDate(new Timestamp(new Date().getTime()));
						
						trcFinePicFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
						trcFinePicFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
						
						Boolean isExtended = (trcFinePicFollowup.getFollowupStatus() != null 
								&& trcFinePicFollowup.getFollowupStatus().getParameterDtlCode()
									.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION));
						
						if (complianceStatus != null && ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN
								.equals(complianceStatus.getParameterDtlCode())) {
							ParameterDetail followupStatus = parameterDetailService
									.getParameterDetailByParamDtlCode(
											ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
							trcFinePicFollowup.setFollowupStatus(followupStatus);
						}
						
						if (targeDatetStatus != null && trcFinePicFollowup.getFollowupDate() == null) {
							ParameterDetail followupStatus = parameterDetailService
									.getParameterDetailByParamDtlCode(
											ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
							trcFinePicFollowup.setFollowupStatus(followupStatus);
						}
						
						List<TrcFinePicFollowupHistory> history = null;
						
						if((complianceStatus != null 
								&& complianceStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN))) {
							
							if(trcFinePicFollowup.getTrcFinePicFollowupHis() != null) {
								history = trcFinePicFollowup.getTrcFinePicFollowupHis();
							}else {
								history = new ArrayList<TrcFinePicFollowupHistory>();
								trcFinePicFollowup.setTrcFinePicFollowupHis(history);
							}
							
							TrcFinePicFollowupHistory fpfh = addNewHistory(trcFinePicFollowup);
							
							history.add(fpfh);
							
							trcFineApprovalService.saveHis(fpfh);
						}else if((complianceStatus == null || StringUtils.isEmpty(complianceStatus.getParameterDtlCode())) 
								&& (targeDatetStatus != null 
								&& targeDatetStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_FINE_TARGET_DATE_NOT_APPROPRIATE))) {
							
							if(trcFinePicFollowup.getTrcFinePicFollowupHis() != null) {
								history = trcFinePicFollowup.getTrcFinePicFollowupHis();
							}else {
								history = new ArrayList<TrcFinePicFollowupHistory>();
								trcFinePicFollowup.setTrcFinePicFollowupHis(history);
							}							
							
							TrcFinePicFollowupHistory fpfh = addNewHistory(trcFinePicFollowup);
							history.add(fpfh);
							trcFineApprovalService.saveHis(fpfh);
							
							if(!isExtended) {
//								trcFinePicFollowup.setTargetDate(null);
							}else {
								//DISABLE FLAG FOR EMAIL REMINDER RESPONSE
								if(trcFinePicFollowup.getTrcFinePicFollowupEmails() != null) {
									for(TrcFinePicFollowupEmail emailRespon : trcFinePicFollowup.getTrcFinePicFollowupEmails()) {
										emailRespon.setEnabledFlag(Constants.ENABLED_FLAG_FALSE);
									}
								}
							}
							
						}else if((complianceStatus == null || StringUtils.isEmpty(complianceStatus.getParameterDtlCode())) 
								&& (targeDatetStatus != null 
								&& targeDatetStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_FINE_TARGET_DATE_APPROPRIATE))) {
							List<TrcFinePicFollowupEmail> trcFolupEmails = trcFinePicFollowup.getTrcFinePicFollowupEmails();
							if(trcFolupEmails == null) {
								trcFolupEmails = new ArrayList<>();
								trcFinePicFollowup.setTrcFinePicFollowupEmails(trcFolupEmails);
							}
							
							//DISABLE FLAG FOR EMAIL REMINDER RESPONSE
							if(trcFinePicFollowup.getTrcFinePicFollowupEmails() != null) {
								for(TrcFinePicFollowupEmail emailRespon : trcFinePicFollowup.getTrcFinePicFollowupEmails()) {
									emailRespon.setEnabledFlag(Constants.ENABLED_FLAG_FALSE);
								}
							}
							
							//ADD NEW
							trcFolupEmails.addAll(getEmailQueueFollowup(trcFine.getCounterType(), trcFinePicFollowup)); 

						} else if((targeDatetStatus != null 
								&& targeDatetStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE))) {
							List<TrcFinePicFollowupEmail> trcFolupEmails = trcFinePicFollowup.getTrcFinePicFollowupEmails();
							if(trcFolupEmails == null) {
								trcFolupEmails = new ArrayList<>();
								trcFinePicFollowup.setTrcFinePicFollowupEmails(trcFolupEmails);
							}
							
							//DISABLE FLAG FOR EMAIL REMINDER RESPONSE
							if(trcFinePicFollowup.getTrcFinePicFollowupEmails() != null) {
								for(TrcFinePicFollowupEmail emailRespon : trcFinePicFollowup.getTrcFinePicFollowupEmails()) {
									emailRespon.setEnabledFlag(Constants.ENABLED_FLAG_FALSE);
								}
							}

						}
					}

					if (uploadedFilesProofOfPayment != null) {
						for (int i = 0; i < uploadedFilesProofOfPayment.size(); i++) {
							TrcFineAttachment doc = new TrcFineAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadedFilesProofOfPayment.get(i);
							doc.setTrcFine(trcFine);

							doc.setAttachmentFile(uf.getFileName());
							doc.setCreatedBy(facesUtil.retrieveUserLogin());
							doc.setCreationDate(new Timestamp(new Date().getTime()));
							doc.setDelId(new Long(0));
							doc.setEnabledFlag(Constants.CONSTANT_YES);

							doc.setFileId(uf.getFileId());
							doc.setFileSize(uf.getFileSize());
							trcFine.getTrcFineAttachments().add(doc);
						}
					}
					
					ParameterDetail pdReportName = parameterDetailService
							.getParameterDetailByParamDtlCode(trcFine.getReportName().getParameterDtlCode());
					trcFine.setReportName(pdReportName);
					
					trcFine.setNotes(trcFine.getNotesEncrypted());
					trcFine.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcFine.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcFine.setDelId(new Long(0));
					trcFine.setEnabledFlag(Constants.CONSTANT_YES);
					trcFineApprovalService.update(trcFine);
				}
				
				for(int i=0; i<trcFineFollowups.size();i++){
					TrcFinePicFollowup trcFinePicFollowup = (TrcFinePicFollowup)trcFine.getTrcFinePicFollowups().get(i);
					ParameterDetail complianceStatus = trcFinePicFollowup.getComplianceStatus() != null ? parameterDetailService.getParameterDetailByParamDtlCode(trcFinePicFollowup.getComplianceStatus().getParameterDtlCode()) :null;
					ParameterDetail targeDatetStatus = trcFinePicFollowup.getTargetDateStatus() != null ? parameterDetailService.getParameterDetailByParamDtlCode(trcFinePicFollowup.getTargetDateStatus().getParameterDtlCode()) :null;
					
					Boolean isExtended = (trcFinePicFollowup.getFollowupStatus() != null 
							&& trcFinePicFollowup.getFollowupStatus().getParameterDtlCode()
								.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION));
					
					// this should be a singleton
			        ExecutorService emailExecutor = Executors.newCachedThreadPool();
			        
			        if(isExtended) {
			        	if(targeDatetStatus != null && targeDatetStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_FINE_TARGET_DATE_NOT_APPROPRIATE)) {
							//REVISE
					        emailExecutor.execute(new Runnable() {
					            @Override
					            public void run() {
					                try {
					                	sendEmailRevise(trcFinePicFollowup);
					                } catch (Exception e) {
					                    logger.error("send email failed", e);
					                }
					            }
					        });
						}else if(targeDatetStatus != null && targeDatetStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_FINE_TARGET_DATE_APPROPRIATE)) {
							//APPROVED
							// from you sendEmail() method
					        emailExecutor.execute(new Runnable() {
					            @Override
					            public void run() {
					                try {
					                	sendEmailApproved(trcFinePicFollowup);
					                } catch (Exception e) {
					                    logger.error("send email failed", e);
					                }
					            }
					        });
						}
			        }else {
			        	if((complianceStatus != null && complianceStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN)) 
			        			|| (targeDatetStatus != null && targeDatetStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_FINE_TARGET_DATE_NOT_APPROPRIATE))) {
							//OPEN
							// from you sendEmail() method
					        emailExecutor.execute(new Runnable() {
					            @Override
					            public void run() {
					                try {
					                	sendEmailOpen(trcFinePicFollowup);
					                } catch (Exception e) {
					                    logger.error("send email failed", e);
					                }
					            }
					        });
					        
						} else if(complianceStatus != null && complianceStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE)) {
							//CLOSED
							// from you sendEmail() method
					        emailExecutor.execute(new Runnable() {
					            @Override
					            public void run() {
					                try {
					                	sendEmailClosed(trcFinePicFollowup);
					                } catch (Exception e) {
					                    logger.error("send email failed", e);
					                }
					            }
					        });
					        
						} else if(targeDatetStatus != null && targeDatetStatus.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_FINE_TARGET_DATE_APPROPRIATE)) {
							//OPEN
							// from you sendEmail() method
					        emailExecutor.execute(new Runnable() {
					            @Override
					            public void run() {
					                try {
					                	sendEmailSesuai(trcFinePicFollowup);
					                } catch (Exception e) {
					                    logger.error("send email failed", e);
					                }
					            }
					        });
						}
			        }

			        
				}

				facesUtil.redirect("/pages/trcFineApproval/trcFineApproval.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	private List<TrcFinePicFollowupEmail> getEmailQueueFollowup(CounterType counterType,
			TrcFinePicFollowup folup) {
		List<TrcFinePicFollowupEmail> emailList = new ArrayList<>();
		
		CounterType ctResponse = counterType;
		
		if(ctResponse != null) {
			ctResponse = counterTypeService.findById(counterType.getCounterTypeId());
			if(ctResponse != null && ctResponse.getDetails() != null) {
				Calendar calendar = Calendar.getInstance();
				
				for (CounterTypeDtl dataCounterTypeDtl : ctResponse.getDetails()) {
					int counterDate = 0;
					Date targetDateTmp = folup.getTargetDate();
					calendar.setTime(targetDateTmp);
					if (dataCounterTypeDtl.getSlaType().equals("+")) {
						while (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
							int day = calendar.get(Calendar.DAY_OF_WEEK);
							
							if (day == 1 || day == 7) {
								// do nothing
							} else {
								if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
									counterDate++;
								}
							}
							
							if (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
//								calendar.setTime(targetDateTmp);
								calendar.add(Calendar.DAY_OF_MONTH, 1);
								targetDateTmp = calendar.getTime();
							}
						}
					} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
						while (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
							int day = calendar.get(Calendar.DAY_OF_WEEK);
							
							if (day == 1 || day == 7) {
								// do nothing
							} else {
								if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
									counterDate++;
								}
							}
							
							if (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
//								calendar.setTime(targetDateTmp);
								calendar.add(Calendar.DAY_OF_MONTH, -1);
								targetDateTmp = calendar.getTime();
							}
						}
					}
					Date emailDate = calendar.getTime();
					
					TrcFinePicFollowupEmail vo = new TrcFinePicFollowupEmail();
					vo.setTrcFinePicFollowup(folup);
					vo.setEmailDate(emailDate);
					vo.setSlaType(dataCounterTypeDtl.getSlaType());
					vo.setSla(dataCounterTypeDtl.getSla());
					vo.setEmailType(ParameterDetail.PARAM_DET_FINE_EMAIL_FINISHING);
					
					vo.setCreatedBy(facesUtil.retrieveUserLogin());
					vo.setCreationDate(new Timestamp(new Date().getTime()));
					vo.setDelId(new Long(0));
					vo.setEnabledFlag(Constants.CONSTANT_YES);
					
					emailList.add(vo);
				}
			}
		}
		
		return emailList;
	}

	private TrcFinePicFollowupHistory addNewHistory(TrcFinePicFollowup trcFinePicFollowup) {
		TrcFinePicFollowupHistory fpfh = new TrcFinePicFollowupHistory();
		fpfh.setTrcFinePicFollowup(trcFinePicFollowup);
		fpfh.setRc(trcFinePicFollowup.getRc());
		fpfh.setFineDebitted(trcFinePicFollowup.getFineDebitted());
		fpfh.setFineAmount(trcFinePicFollowup.getFineAmount());
		fpfh.setBreaches(trcFinePicFollowup.getBreaches());
		fpfh.setRootCause(trcFinePicFollowup.getRootCause());
		fpfh.setCategory(trcFinePicFollowup.getCategory());
		fpfh.setDivisionId(trcFinePicFollowup.getDivisionId());
		fpfh.setTargetDate(trcFinePicFollowup.getTargetDate());
		fpfh.setTargetResponseDate(trcFinePicFollowup.getTargetResponseDate());
		fpfh.setRescheduleReason(trcFinePicFollowup.getRescheduleReason());
		fpfh.setNotes(trcFinePicFollowup.getNotes());
		fpfh.setFollowupStatus(trcFinePicFollowup.getFollowupStatus());
		fpfh.setFollowupBy(trcFinePicFollowup.getFollowupBy());
		fpfh.setConfirmationDate(trcFinePicFollowup.getComplianceDate());
		fpfh.setFollowupDate(trcFinePicFollowup.getFollowupDate());
		fpfh.setFollowupNote(trcFinePicFollowup.getFollowupNote());
		fpfh.setComplianceStatus(trcFinePicFollowup.getComplianceStatus());
		fpfh.setTargetDateStatus(trcFinePicFollowup.getTargetDateStatus());
		fpfh.setComplianceNote(trcFinePicFollowup.getComplianceNote());
		fpfh.setComplianceBy(trcFinePicFollowup.getComplianceBy());
		fpfh.setComplianceDate(trcFinePicFollowup.getComplianceDate());
		
		fpfh.setCreatedBy(facesUtil.retrieveUserLogin());
		fpfh.setCreationDate(new Timestamp(new Date().getTime()));
		fpfh.setDelId(new Long(0));
		fpfh.setEnabledFlag(Constants.CONSTANT_YES);
		return fpfh;
	}

	public void cancel() {
		try {
			if(uploadedFilesDocument != null) {
				for (int i = 0; i < uploadedFilesDocument.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			facesUtil.redirect("/pages/trcFineApproval/trcFineApproval.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAttachment(String fileId,int index,String uploadType) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if(uploadType!=null && uploadType.equals(TrcFineApprovalConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadedFilesDocument.remove(uploadedFilesDocument.get(index));
		}else if(uploadType!=null && uploadType.equals(TrcFineApprovalConstants.UPLOAD_TYPE_EVIDENCE)) {
			uploadedFilesEvidence.remove(uploadedFilesEvidence.get(index));
		}else if(uploadType!=null && uploadType.equals(TrcFineApprovalConstants.UPLOAD_TYPE_PROOF_OF_PAYMENT)) {
			uploadedFilesProofOfPayment.remove(uploadedFilesProofOfPayment.get(index));
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
	
	public TrcFine getTrcFine() {
		return trcFine;
	}

	public void setTrcFine(TrcFine trcFine) {
		this.trcFine = trcFine;
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

	/*
	 * public ParameterDetailService getParameterDetailService() { return
	 * parameterDetailService; }
	 * 
	 * public void setParameterDetailService(ParameterDetailService
	 * parameterDetailService) { this.parameterDetailService =
	 * parameterDetailService; }
	 */

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public TrcFinePicComplianceTableModel<TrcFinePicCompliance> getTablePicComplianceModel() {
		return tablePicComplianceModel;
	}

	public void setTablePicComplianceModel(
			TrcFinePicComplianceTableModel<TrcFinePicCompliance> tablePicComplianceModel) {
		this.tablePicComplianceModel = tablePicComplianceModel;
	}

	

	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<SelectItem> divisionList) {
		this.divisionList = divisionList;
	}
	
	public String getREMINDER_ACTIVE() {
		return REMINDER_ACTIVE;
	}

	public void setREMINDER_ACTIVE(String rEMINDER_ACTIVE) {
		REMINDER_ACTIVE = rEMINDER_ACTIVE;
	}

	public String getREMINDER_INACTIVE() {
		return REMINDER_INACTIVE;
	}

	public void setREMINDER_INACTIVE(String rEMINDER_INACTIVE) {
		REMINDER_INACTIVE = rEMINDER_INACTIVE;
	}

	public SelectorInfo getSelectorUser1() {
		return selectorUser1;
	}

	public void setSelectorUser1(SelectorInfo selectorUser1) {
		this.selectorUser1 = selectorUser1;
	}

	public SelectorInfo getSelectorUser2() {
		return selectorUser2;
	}

	public void setSelectorUser2(SelectorInfo selectorUser2) {
		this.selectorUser2 = selectorUser2;
	}

	public SelectorInfo getSelectorUser3() {
		return selectorUser3;
	}

	public void setSelectorUser3(SelectorInfo selectorUser3) {
		this.selectorUser3 = selectorUser3;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public SelectorInfo getSelectorUserCc1() {
		return selectorUserCc1;
	}

	public void setSelectorUserCc1(SelectorInfo selectorUserCc1) {
		this.selectorUserCc1 = selectorUserCc1;
	}

	public SelectorInfo getSelectorUserCc2() {
		return selectorUserCc2;
	}

	public void setSelectorUserCc2(SelectorInfo selectorUserCc2) {
		this.selectorUserCc2 = selectorUserCc2;
	}

	public SelectorInfo getSelectorUserCc3() {
		return selectorUserCc3;
	}

	public void setSelectorUserCc3(SelectorInfo selectorUserCc3) {
		this.selectorUserCc3 = selectorUserCc3;
	}

	public TrcFinePicCompliance[] getSelectedPicComplianceData() {
		return selectedPicComplianceData;
	}

	public void setSelectedPicComplianceData(TrcFinePicCompliance[] selectedPicComplianceData) {
		this.selectedPicComplianceData = selectedPicComplianceData;
	}

	

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public List<SelectItem> getSenderCodeList() {
		return senderCodeList;
	}

	public void setSenderCodeList(List<SelectItem> senderCodeList) {
		this.senderCodeList = senderCodeList;
	}

	public List<SelectItem> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(List<SelectItem> yesNoList) {
		this.yesNoList = yesNoList;
	}

	

	public List<SelectItem> getCounterTypeList() {
		return counterTypeList;
	}

	public void setCounterTypeList(List<SelectItem> counterTypeList) {
		this.counterTypeList = counterTypeList;
	}

	public Integer getIndexDtlPicCompliance() {
		return indexDtlPicCompliance;
	}

	public void setIndexDtlPicCompliance(Integer indexDtlPicCompliance) {
		this.indexDtlPicCompliance = indexDtlPicCompliance;
	}

	public SelectorInfo getSelectorPicCompliance() {
		return selectorPicCompliance;
	}

	public void setSelectorPicCompliance(SelectorInfo selectorPicCompliance) {
		this.selectorPicCompliance = selectorPicCompliance;
	}

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public TrcFineApprovalService getTrcFineApprovalService() {
		return trcFineApprovalService;
	}

	public void setTrcFineApprovalService(TrcFineApprovalService trcFineApprovalService) {
		this.trcFineApprovalService = trcFineApprovalService;
	}

	public List<TrcFine> getTrcFineList() {
		return trcFineList;
	}

	public void setTrcFineList(List<TrcFine> trcFineList) {
		this.trcFineList = trcFineList;
	}

	

	public List<UploadedFileWO> getUploadedFilesEvidence() {
		return uploadedFilesEvidence;
	}

	public void setUploadedFilesEvidence(List<UploadedFileWO> uploadedFilesEvidence) {
		this.uploadedFilesEvidence = uploadedFilesEvidence;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
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

	public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
	}

	public List<SelectItem> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<SelectItem> attendanceList) {
		this.attendanceList = attendanceList;
	}

	

	public SelectorInfo getSelectorPicAttendance() {
		return selectorPicAttendance;
	}

	public void setSelectorPicAttendance(SelectorInfo selectorPicAttendance) {
		this.selectorPicAttendance = selectorPicAttendance;
	}

	public Integer getIndexDtlPicAttendance() {
		return indexDtlPicAttendance;
	}

	public void setIndexDtlPicAttendance(Integer indexDtlPicAttendance) {
		this.indexDtlPicAttendance = indexDtlPicAttendance;
	}

	

	public Integer getLastSequenceOfPicAttendance() {
		return lastSequenceOfPicAttendance;
	}

	public void setLastSequenceOfPicAttendance(Integer lastSequenceOfPicAttendance) {
		this.lastSequenceOfPicAttendance = lastSequenceOfPicAttendance;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		/*if (StringUtils.equals("picAttendanceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User userPicCompliance = userService.findById(((BigInteger) objects[0]).longValue());
			User userPicAttendee = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (userPicAttendee != null) {
				trcFine.getTrcFinePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserId(userPicAttendee);
				trcFine.getTrcFinePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserNIK(userPicAttendee.getNik());
				trcFine.getTrcFinePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserName(userPicAttendee.getName());
				trcFine.getTrcFinePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserEmail(userPicAttendee.getEmail());

				tableAttedanceModel.setWrappedData(trcFine.getTrcFinePicFollowupAttendance());
			}
		}*/
	}

	public List<SelectItem> getFineTypeCodeList() {
		return fineTypeCodeList;
	}

	public void setFineTypeCodeList(List<SelectItem> fineTypeCodeList) {
		this.fineTypeCodeList = fineTypeCodeList;
	}

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public List<SelectItem> getRcList() {
		return rcList;
	}

	public void setRcList(List<SelectItem> rcList) {
		this.rcList = rcList;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public TrcFinePicFollowupTableModel<TrcFinePicFollowup> getTablePicFollowupModel() {
		return tablePicFollowupModel;
	}

	public void setTablePicFollowupModel(TrcFinePicFollowupTableModel<TrcFinePicFollowup> tablePicFollowupModel) {
		this.tablePicFollowupModel = tablePicFollowupModel;
	}

	public List<SelectItem> getReportNameList() {
		return reportNameList;
	}

	public void setReportNameList(List<SelectItem> reportNameList) {
		this.reportNameList = reportNameList;
	}

	public List<UploadedFileWO> getUploadedFilesProofOfPayment() {
		return uploadedFilesProofOfPayment;
	}

	public void setUploadedFilesProofOfPayment(List<UploadedFileWO> uploadedFilesProofOfPayment) {
		this.uploadedFilesProofOfPayment = uploadedFilesProofOfPayment;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrcFineApprovalEditBean.logger = logger;
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

	public Boolean getIsOtherReportNameChosen() {
		return isOtherReportNameChosen;
	}

	public void setIsOtherReportNameChosen(Boolean isOtherReportNameChosen) {
		this.isOtherReportNameChosen = isOtherReportNameChosen;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public List<SelectItem> getApprovalStatusList() {
		return approvalStatusList;
	}

	public void setApprovalStatusList(List<SelectItem> approvalStatusList) {
		this.approvalStatusList = approvalStatusList;
	}

	public List<TrcFinePicFollowup> getTrcFineFollowups() {
		return trcFineFollowups;
	}

	public void setTrcFineFollowups(List<TrcFinePicFollowup> trcFineFollowups) {
		this.trcFineFollowups = trcFineFollowups;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	
}