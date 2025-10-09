package com.wo.module.trcFine.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.faq.model.FaqDocument;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttachment;
import com.wo.module.trcFine.constant.TrcFineConstants;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFineDocument;
import com.wo.module.trcFineApproval.model.TrcFinePicCompliance;
import com.wo.module.trcFineApproval.model.TrcFinePicComplianceTableModel;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupAttachment;

import com.wo.module.trcFine.service.TrcFineService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcFineEditBean extends CommonBean implements  SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TrcFineEditBean.class);

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

	private TrcFinePicComplianceTableModel<TrcFinePicCompliance> tablePicComplianceModel;

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;
	
	private Integer indexDtlPicAttendance;
	private Integer lastSequenceOfPicAttendance;

	// private List<TrcRmd> trcRmdList;
	private List<TrcFine> trcFineList;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TrcFineService trcFineService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;
	private RCService rcService;

	public FacesUtil facesUtil;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> attendanceList;
	
	private List<SelectItem> reminderStatusList;
	private List<SelectItem> fineTypeCodeList;
	private List<SelectItem> rcList;
	private List<SelectItem> categoryList;
	
	private FileUtil fileUtil;
	
	private String viewOnly;

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
		selectorPicAttendance = TrcFineConstants.buildSelectorUserAttendace();
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

	public void initList() {
		try {
			
			/*rcList = new ArrayList<SelectItem>();
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
			}*/
			
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
		trcFine = trcFineService.findById(idLong);
		
		trcFine.setAttendance(new ParameterDetail());
		
		if (trcFine != null) {
			trcFineList = new ArrayList<TrcFine>();
			trcFineList.add(trcFine);
		}

		if (trcFine.getUserId1() != null) {
			trcFine.setUserNameTemp1(trcFine.getUserId1().getName());
		}

		if (trcFine.getUserId2() != null) {
			trcFine.setUserNameTemp2(trcFine.getUserId2().getName());
		}

		if (trcFine.getUserId3() != null) {
			trcFine.setUserNameTemp3(trcFine.getUserId3().getName());
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

		uploadedFilesEvidence = new ArrayList<UploadedFileWO>();

		
		tablePicComplianceModel = new TrcFinePicComplianceTableModel<TrcFinePicCompliance>(
				trcFine.getTrcFinePicCompliances());
		
		
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
		
		if(trcFine.getFineCode().getParameterDtlCode().equals(TrcFineConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
			if(trcFine.getAttendance().getParameterDtlCode() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineAttedance") 
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if(trcFine.getAttendance().getParameterDtlCode().equals(TrcFineConstants.PARAM_DETAIL_ATTENDEE_NOT_ATTEND)) {
				if(trcFine.getFollowupNote() == null || trcFine.getFollowupNote().equals("")) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdInformation") + " " 
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
			}
		}else if(trcFine.getFineCode().getParameterDtlCode().equals(TrcFineConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_FINE)) {
			if (trcFine.getFollowupDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineTimeline") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadedFilesEvidence == null || uploadedFilesEvidence.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdPicConfirmationEvidence") + " File "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}else{
			if (trcFine.getFollowupDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineFollowupDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadedFilesEvidence == null || uploadedFilesEvidence.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdPicConfirmationEvidence") + " File "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}

		return flag;
	}
	
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
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
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public void save() {
		try {

			if (!validate()) {
				if (trcFine.getFineId() != null) {
					
					
					
					ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
					Date currentDate = new Date();
					
					for(int i=0;i<trcFine.getTrcFinePicFollowups().size();i++){
						TrcFinePicFollowup trcFinePicFollowup = trcFine.getTrcFinePicFollowups().get(i);
						trcFinePicFollowup.setFollowupStatus(followupStatus);
						trcFinePicFollowup.setFollowupBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));
						trcFinePicFollowup.setFollowupDate(currentDate);
						trcFinePicFollowup.setConfirmationDate(new Date());
						
						if(trcFinePicFollowup.getTrcFinePicFollowupAttachments() == null || trcFinePicFollowup.getTrcFinePicFollowupAttachments().size() <= 0){
							trcFinePicFollowup.setTrcFinePicFollowupAttachments(new ArrayList<TrcFinePicFollowupAttachment>());
						}
						
						trcFinePicFollowup.getTrcFinePicFollowupAttachments().clear();
						
						
						if(uploadedFilesEvidence != null) {
							
							for (int x = 0; x < uploadedFilesEvidence.size(); x++) {
								TrcFinePicFollowupAttachment evidence = new TrcFinePicFollowupAttachment();
								UploadedFileWO uf = (UploadedFileWO) uploadedFilesEvidence.get(x);
								evidence.setTrcFinePicFollowup(trcFinePicFollowup);
								evidence.setAttachmentFile(uf.getFileName());
								evidence.setCreatedBy(facesUtil.retrieveUserLogin());
								evidence.setCreationDate(new Timestamp(new Date().getTime()));
								evidence.setDelId(new Long(0));
								evidence.setEnabledFlag(Constants.CONSTANT_YES);
								evidence.setFileId(uf.getFileId());
								evidence.setFileSize(uf.getFileSize());
													
								trcFinePicFollowup.getTrcFinePicFollowupAttachments().add(evidence);
								
							}
						}
					}

					trcFine.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcFine.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcFine.setDelId(new Long(0));
					trcFine.setEnabledFlag(Constants.CONSTANT_YES);
					trcFineService.update(trcFine);
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

				facesUtil.redirect("/pages/trcFine/trcFine.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

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
			facesUtil.redirect("/pages/trcFine/trcFine.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAttachment(String fileId,int index,String uploadType) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if(uploadType!=null && uploadType.equals(TrcFineConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadedFilesDocument.remove(uploadedFilesDocument.get(index));
		}else if(uploadType!=null && uploadType.equals(TrcFineConstants.UPLOAD_TYPE_EVIDENCE)) {
			uploadedFilesEvidence.remove(uploadedFilesEvidence.get(index));
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

	public TrcFineService getTrcFineService() {
		return trcFineService;
	}

	public void setTrcFineService(TrcFineService trcFineService) {
		this.trcFineService = trcFineService;
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
		if (StringUtils.equals("picAttendanceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User userPicCompliance = userService.findById(((BigInteger) objects[0]).longValue());
			User userPicAttendee = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (userPicAttendee != null) {
				
			}
		}
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

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}
	
	
}