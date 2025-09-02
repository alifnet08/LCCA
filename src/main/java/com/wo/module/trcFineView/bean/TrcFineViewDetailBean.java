package com.wo.module.trcFineView.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpFine.model.TmpFineApproval;
import com.wo.module.tmpFine.service.TmpFineService;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFineDocument;
import com.wo.module.trcFineApproval.model.TrcFinePicCompliance;
import com.wo.module.trcFineApproval.model.TrcFinePicComplianceTableModel;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupTableModel;
import com.wo.module.trcFineApproval.service.TrcFineApprovalService;
import com.wo.module.trcFineView.constants.TrcFineViewConstants;
import com.wo.module.trcFineView.service.TrcFineViewService;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class TrcFineViewDetailBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TrcFineViewDetailBean.class);

	private TrcFine trcFine;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;

	private Boolean disabledFollowUpStatus;

	private TrcFinePicCompliance[] selectedPicComplianceData;
	
	private TrcFinePicFollowup[] selectedPicFollowupData;
	
	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesDocument;

	private Integer lastSequenceOfPicCompliance;
	private Integer lastSequenceOfSupportingUnitModel;
	private Integer lastSequenceOfPicFollowup;

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;
	private Integer indexDtlPicFollowup;

	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TrcFineViewService trcFineViewService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	// private ParameterDetailService parameterDetailService;
	private UserService userService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;
	private RCService rcService;
	private TrcFineApprovalService trcFineApprovalService;
	private TmpFineService tmpFineService;
	
	public FacesUtil facesUtil;

	private FileUtil fileUtil;
	
	private TrcFinePicComplianceTableModel<TrcFinePicCompliance> tablePicComplianceModel;
	private TrcFinePicFollowupTableModel<TrcFinePicFollowup> tablePicFollowupModel;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;
	private List<SelectItem> fineTypeCodeList;
	private List<SelectItem> rcList;
	private List<SelectItem> categoryList;
	
	private List<TrcFine> trcFineList;
	private List<TmpFineApproval> tableApproval;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;

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
		selectorUser1 = TrcFineViewConstants.buildSelectorUser();
		selectorUser2 = TrcFineViewConstants.buildSelectorUser();
		selectorUser3 = TrcFineViewConstants.buildSelectorUser();
		selectorPicCompliance = TrcFineViewConstants.buildSelectorUserCompliance();
		selectorUserCc1 = TrcFineViewConstants.buildSelectorUser();
		selectorUserCc2 = TrcFineViewConstants.buildSelectorUser();
		selectorUserCc3 = TrcFineViewConstants.buildSelectorUser();

		checkNewOrEdit();

		fileUtil = FileUtil.getInstance();
	}

	@SuppressWarnings("rawtypes")
	public void initList() {
		try {
			
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

			complianceStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listComplianceDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);

			for (ParameterDetail vo : listComplianceDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				complianceStatusList.add(si);
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

			fineTypeCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listFineTypeDtl = parameterDetailService
					.getParameterDetailByParamCodeOrdered(ParameterHeader.PARAM_HEAD_CODE_CORRESPONDENCE_TYPE);

			for (ParameterDetail vo : listFineTypeDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				if(vo.getParameterDtlCode().equals("FINE")){
				fineTypeCodeList.add(si);
				}
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
			if (viewId.trim().equalsIgnoreCase("true")) {
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
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		trcFine = trcFineViewService.findById(idLong);
		trcFine.setNotes(trcFine.getNotesDecrypted());
		lastSequenceOfPicCompliance = 0;
		lastSequenceOfSupportingUnitModel = 0;

		TrcFine trcFine = trcFineApprovalService.findById(idLong);
		if (trcFine != null) {

			if (trcFine.getFollowupStatus() != null) {
				disabledFollowUpStatus = true;
			} else {
				disabledFollowUpStatus = false;
			}

			trcFineList = new ArrayList<TrcFine>();
			trcFineList.add(trcFine);

			if (trcFineList.get(0).getComplianceStatus() == null) {
				trcFineList.get(0).setComplianceStatus(new ParameterDetail());
			}
		} else {
			disabledFollowUpStatus = false;
		}


		if (trcFine.getTrcFinePicCompliances() != null) {
			lastSequenceOfPicCompliance = trcFine.getTrcFinePicCompliances().size();
			for (int i = 0; i < trcFine.getTrcFinePicCompliances().size(); i++) {
				TrcFinePicCompliance dtl = (TrcFinePicCompliance) trcFine
						.getTrcFinePicCompliances().get(i);

				lastSequenceOfPicCompliance = lastSequenceOfPicCompliance + 1;
				dtl.setSequence(lastSequenceOfPicCompliance);

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
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesDocument.add(uf);

		}

		tablePicComplianceModel = new TrcFinePicComplianceTableModel<TrcFinePicCompliance>(
				trcFine.getTrcFinePicCompliances());
		
		tablePicFollowupModel = new TrcFinePicFollowupTableModel<TrcFinePicFollowup>(
				trcFine.getTrcFinePicFollowups());
		
		tableApproval = new ArrayList<TmpFineApproval>();
		tableApproval = tmpFineService.getDataApprovalByFineId(idLong);
		
	}

	public void back() {
		try {
			facesUtil.redirect("/pages/trcFineView/trcFineView.faces");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	
	
	@SuppressWarnings("unused")
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_KORESPONDENSI");
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("counter_type","NOTIFICATION");
				   emailSubject = emailSubject.replaceAll("perihal_in",trcFine.getPerihalIn());
				   emailSubject = emailSubject.replaceAll("perihal_en",trcFine.getPerihalEn());
				   emailSubject = emailSubject.replaceAll("letter_no",trcFine.getLetterNo());
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc3 = "";
			String emailCc = "";
			String emailCcSupporting = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			
			if (trcFine.getReminderStatus()!=null && trcFine.getReminderStatus().getParameterDtlCode().equals("REMINDER_ACTIVE") 
					//&& trcFine.getFollowUp() != null && trcFine.getFollowUp().equals("Y")
		    		//trcFine.getUserId1() != null
		    		) {
			    	String token = Constants.encryptString(trcFine.getFineId().toString());
			    	String menuId = "";
			    	String urlLink = "";
			    	if (trcFine.getFollowUp() != null && trcFine.getFollowUp().equals("Y")) {
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE);
						urlLink = pdHostName.getNameIn().concat("pages/trcFineView/trcFineViewDetail.faces?token="+token+"&menuId="+menuId);
			    	} else if (trcFine.getFollowUp() != null && trcFine.getFollowUp().equals("N")) {
			    		menuId = Constants.encryptString(Constants.MENU_ID_CORRESPONDENCE_VIEW);
						urlLink = pdHostName.getNameIn().concat("pages/trcFineView/trcFineViewDetail.faces?token="+token+"&menuId="+menuId);
			    	}
		    		
					//emailContent = emailTemplate.getEmailContent().replaceAll("target_date", trcFine.getTargetDate()!=null?sdf.format(trcFine.getTargetDate()):"");
					emailContent = emailContent.replaceAll("perihal_in", trcFine.getPerihalIn());
					emailContent = emailContent.replaceAll("perihal_en", trcFine.getPerihalEn());
					if (trcFine.getSenderCode() != null && trcFine.getSenderCode().getParameterDtlCode() != null) {
						ParameterDetail psSenderCode = parameterDetailService.getParameterDetailByParamDtlCode(trcFine.getSenderCode().getParameterDtlCode());
						emailContent = emailContent.replaceAll("sender_in", psSenderCode.getNameIn()!=null?psSenderCode.getNameIn():"NA");
						emailContent = emailContent.replaceAll("sender_en", psSenderCode.getNameEn()!=null?psSenderCode.getNameEn():"NA");
					} else {
						emailContent = emailContent.replaceAll("sender_in", "NA");
						emailContent = emailContent.replaceAll("sender_en", "NA");
					}
					
					/*emailContent = emailContent.replaceAll("division_name", (trcFine.getDivisionId()!=null?userService.getDivisionNameByDivisionId(trcFine.getDivisionId()):"NA"));
					emailContent = emailContent.replaceAll("pic_1_name", (trcFine.getUserId1() != null?trcFine.getUserId1().getName():"NA"));
					emailContent = emailContent.replaceAll("pic_2_name", (trcFine.getUserId2()!=null?trcFine.getUserId2().getName():"NA"));
					emailContent = emailContent.replaceAll("pic_3_name", (trcFine.getUserId3()!=null?trcFine.getUserId3().getName():"NA"));*/
					
					emailContent = emailContent.replaceAll("letter_no", trcFine.getLetterNo());
					emailContent = emailContent.replaceAll("receive_letter_date", trcFine.getLetterReceivedDate()!=null?sdf.format(trcFine.getLetterReceivedDate()):"");
					emailContent = emailContent.replaceAll("letter_date", trcFine.getLetterDate()!=null?sdf.format(trcFine.getLetterDate()):"");
					emailContent = emailContent.replaceAll("url_link", urlLink);
					
					// add by dwi
					emailContent = emailContent.replaceAll("summary_in", trcFine.getLetterSummary());
					
					
					
					
//					for(int x=0;x<trcFine.getCounterType().getDetails().size();x++) {
					if (trcFine != null) {
						
						
						for(int i=0;i<trcFine.getTrcFinePicFollowups().size();i++){
							TrcFinePicFollowup tmpFinePicFollowup =(TrcFinePicFollowup)trcFine.getTrcFinePicFollowups().get(i);
							if(emailTo.isEmpty()){
								emailTo =  tmpFinePicFollowup.getUserId1() != null ? tmpFinePicFollowup.getUserId1().getEmail() : "";
							}else{
								emailCc3 = tmpFinePicFollowup.getUserId1() != null ? tmpFinePicFollowup.getUserId1().getEmail() : "";
							}
							
							emailCc1 = tmpFinePicFollowup.getUserId2() != null ? tmpFinePicFollowup.getUserId2().getEmail() : "";
							emailCc2 = tmpFinePicFollowup.getUserId3() != null ? tmpFinePicFollowup.getUserId3().getEmail() : "";
							
							if(StringUtils.isNotEmpty(emailCc3)) {
								emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc3):emailCc.concat(emailCc3);
							}
							
							if(StringUtils.isNotEmpty(emailCc1)) {
								emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc1):emailCc.concat(emailCc1);
							}
							if(StringUtils.isNotEmpty(emailCc2)) {
								emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
							}
						}
						
		
						ExecutorService emailExecutor = Executors.newCachedThreadPool();
						
						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						final String cc = emailCc;
						
						System.out.println("subject=="+subject);
						System.out.println("content=="+content);
						System.out.println("to=="+to);
						System.out.println("cc=="+cc);
						
						if(emailTo!=null && !emailTo.isEmpty()){
						CallApiManager.sendEmailAPI(to,cc, subject,
								content, "EMAIL_CORESPONDENCE", "true", parameterDetailService);
						}
					}
					
					
				
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TrcFineViewDetailBean.logger = logger;
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

	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public TrcFinePicCompliance[] getSelectedPicComplianceData() {
		return selectedPicComplianceData;
	}

	public void setSelectedPicComplianceData(TrcFinePicCompliance[] selectedPicComplianceData) {
		this.selectedPicComplianceData = selectedPicComplianceData;
	}

	public TrcFinePicFollowup[] getSelectedPicFollowupData() {
		return selectedPicFollowupData;
	}

	public void setSelectedPicFollowupData(TrcFinePicFollowup[] selectedPicFollowupData) {
		this.selectedPicFollowupData = selectedPicFollowupData;
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

	public SelectorInfo getSelectorPicCompliance() {
		return selectorPicCompliance;
	}

	public void setSelectorPicCompliance(SelectorInfo selectorPicCompliance) {
		this.selectorPicCompliance = selectorPicCompliance;
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

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}

	public Integer getLastSequenceOfPicCompliance() {
		return lastSequenceOfPicCompliance;
	}

	public void setLastSequenceOfPicCompliance(Integer lastSequenceOfPicCompliance) {
		this.lastSequenceOfPicCompliance = lastSequenceOfPicCompliance;
	}

	public Integer getLastSequenceOfSupportingUnitModel() {
		return lastSequenceOfSupportingUnitModel;
	}

	public void setLastSequenceOfSupportingUnitModel(Integer lastSequenceOfSupportingUnitModel) {
		this.lastSequenceOfSupportingUnitModel = lastSequenceOfSupportingUnitModel;
	}

	public Integer getLastSequenceOfPicFollowup() {
		return lastSequenceOfPicFollowup;
	}

	public void setLastSequenceOfPicFollowup(Integer lastSequenceOfPicFollowup) {
		this.lastSequenceOfPicFollowup = lastSequenceOfPicFollowup;
	}

	public Integer getIndexDtlPicCompliance() {
		return indexDtlPicCompliance;
	}

	public void setIndexDtlPicCompliance(Integer indexDtlPicCompliance) {
		this.indexDtlPicCompliance = indexDtlPicCompliance;
	}

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public Integer getIndexDtlPicFollowup() {
		return indexDtlPicFollowup;
	}

	public void setIndexDtlPicFollowup(Integer indexDtlPicFollowup) {
		this.indexDtlPicFollowup = indexDtlPicFollowup;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public TrcFineViewService getTrcFineViewService() {
		return trcFineViewService;
	}

	public void setTrcFineViewService(TrcFineViewService trcFineViewService) {
		this.trcFineViewService = trcFineViewService;
	}

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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public TrcFineApprovalService getTrcFineApprovalService() {
		return trcFineApprovalService;
	}

	public void setTrcFineApprovalService(TrcFineApprovalService trcFineApprovalService) {
		this.trcFineApprovalService = trcFineApprovalService;
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

	public TrcFinePicComplianceTableModel<TrcFinePicCompliance> getTablePicComplianceModel() {
		return tablePicComplianceModel;
	}

	public void setTablePicComplianceModel(TrcFinePicComplianceTableModel<TrcFinePicCompliance> tablePicComplianceModel) {
		this.tablePicComplianceModel = tablePicComplianceModel;
	}

	public TrcFinePicFollowupTableModel<TrcFinePicFollowup> getTablePicFollowupModel() {
		return tablePicFollowupModel;
	}

	public void setTablePicFollowupModel(TrcFinePicFollowupTableModel<TrcFinePicFollowup> tablePicFollowupModel) {
		this.tablePicFollowupModel = tablePicFollowupModel;
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

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public List<SelectItem> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<SelectItem> divisionList) {
		this.divisionList = divisionList;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public List<SelectItem> getFineTypeCodeList() {
		return fineTypeCodeList;
	}

	public void setFineTypeCodeList(List<SelectItem> fineTypeCodeList) {
		this.fineTypeCodeList = fineTypeCodeList;
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

	public List<TrcFine> getTrcFineList() {
		return trcFineList;
	}

	public void setTrcFineList(List<TrcFine> trcFineList) {
		this.trcFineList = trcFineList;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<TmpFineApproval> getTableApproval() {
		return tableApproval;
	}

	public void setTableApproval(List<TmpFineApproval> tableApproval) {
		this.tableApproval = tableApproval;
	}

	public TmpFineService getTmpFineService() {
		return tmpFineService;
	}

	public void setTmpFineService(TmpFineService tmpFineService) {
		this.tmpFineService = tmpFineService;
	}
}