package com.wo.module.tmpAuditApproval.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.ColumnModel;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.mstAudit.vo.MstAuditVO;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.tmpAudit.constant.AuditConstant;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.model.TmpAuditCheckPoint;
import com.wo.module.tmpAudit.model.TmpAuditCheckPointTableModel;
import com.wo.module.tmpAudit.model.TmpAuditDocument;
import com.wo.module.tmpAudit.model.TmpAuditPICComplianceTableModel;
import com.wo.module.tmpAudit.model.TmpAuditPICFollowupTableModel;
import com.wo.module.tmpAudit.model.TmpAuditPicCompliance;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowup;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupAuditFindings;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupBankCommitment;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupBankResponse;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupSupportingUnit;
import com.wo.module.tmpAudit.service.TmpAuditService;
import com.wo.module.tmpAuditApproval.constant.TmpAuditApprovalConstant;
import com.wo.module.tmpAuditApproval.service.TmpAuditApprovalService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpAuditApprovalEditBean extends CommonBean implements Serializable, AuditConstant {

	private static final long serialVersionUID = 4084425730216263847L;

	static Logger logger = Logger.getLogger(TmpAuditApprovalBean.class);

	private TmpAudit tmpAudit;

	private Long oldCounterTypeId;
	private Long newCounterTypeId;

	private Boolean disabledFollowUpStatus;
	private Integer indexDtlCompliance;
	private Long counterTypeId;

	private List<SelectItem> selectAuditFollowUp;
	private List<SelectItem> selectAuditObject;
	private List<SelectItem> selectAuditCategory;
	private List<SelectItem> counterTypes;
	private List<SelectItem> followUps;

	private List<UploadedFileWO> uploadedFilesAuditFindings;
	private List<UploadedFileWO> uploadedFilesAuditBankResponse;
	private List<UploadedFileWO> uploadedFilesAuditBankCommitment;
	private List<UploadedFileWO> uploadedFilesAuditBankAttachment;
	
	private FileUtil fileUtil;

	// services
	private TmpAuditService tmpAuditService;
	private CounterTypeService counterTypeService;
	private UserService userService;
	private EmailTemplateService emailTemplateService;
	private TmpAuditApprovalService tmpAuditApprovalService;
	private MstAuditService mstAuditService;
	
	private List<SelectItem> divisions;

	private List<SelectItem> reminderStatusList;
	
	private List<SelectItem> templateAuditList;

	private TmpAuditPicCompliance[] selectedDataCompliance;

	private TmpAuditPicFollowup[] selectedDataFollowup;

	private TmpAuditPICComplianceTableModel<TmpAuditPicCompliance> tableModelCompliance;

	private TmpAuditCheckPointTableModel<TmpAuditCheckPoint> tableModelFollowup;

	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private List<SocializationApprovalVO> tableApproval;

	public FacesUtil facesUtil;

	private String navigateSearch = TMP_AUDIT_SEARCH;
	
	private String note;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	private List<SelectItem> yesNo;
	
	private String approvalStat;
	
	private List<SelectItem> approvalStatus;
	
	@PostConstruct
	public void init() {
		super.init();
		fileUtil = FileUtil.getInstance();

		constructSelectComponent();

		tableApproval = new ArrayList<SocializationApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
	}

	private void constructSelectComponent() {
		setupAuditFollowUp();
		setupAuditObject();
		setupAuditCategory();
		setupAuditCounterType();
		setupAuditPicFollowupDivision();
		setupReminderStatus();
		setupFollowup();
		setupYesNo();
		selectApprovalStatus();
		setupTemplateAuditList();
	}
	
	private void setupTemplateAuditList() {
		templateAuditList = new ArrayList<SelectItem>();
		try {
			List<MstAudit> allDataMstAudit = mstAuditService.getAllMstAuditData();
			for (int i = 0; i < allDataMstAudit.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel((allDataMstAudit.get(i)).getAuditTemplate());
				si.setValue((allDataMstAudit.get(i)).getMstAuditId());
				templateAuditList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private void setupAuditFollowUp() {
		setSelectAuditFollowUp(new ArrayList<SelectItem>());
		try {
			setSelectAuditFollowUp(parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDITOR,
					false, true, facesUtil.retrieveDefaultLocale()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupYesNo() {
		yesNo = new ArrayList<SelectItem>();
		yesNo.add(new SelectItem(Constants.CONSTANT_YES));
		yesNo.add(new SelectItem(Constants.CONSTANT_NO));
	}

	private void setupAuditObject() {
		selectAuditObject = new ArrayList<SelectItem>();
		try {
			selectAuditObject = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDIT_OBJECT,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setupAuditCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupAuditCategory() {
		selectAuditCategory = new ArrayList<SelectItem>();
		try {
			selectAuditCategory = parameterDetailService.getListLabelValue(
					ParameterHeader.PARAM_HEAD_CODE_AUDIT_CATEGORY, false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setupAuditPicFollowupDivision() {
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

	public void setupFollowup() {
		followUps = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue(Y);
		followUps.add(si);

		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue(N);
		followUps.add(si2);
	}

	public void setupReminderStatus() {
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

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");

		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	private void handleNew() {
		try {
			disabledFollowUpStatus = false;

			tmpAudit = new TmpAudit();
			tmpAudit.setReminderStatus(ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE);

			lastSequenceOfCompliance = 0;
			lastSequenceOfFollowup = 0;

			tableModelCompliance = new TmpAuditPICComplianceTableModel<TmpAuditPicCompliance>(
					tmpAudit.getTmpAuditPicCompliances());

			tableModelFollowup = new TmpAuditCheckPointTableModel<TmpAuditCheckPoint>(
					tmpAudit.getTmpAuditCheckPoints());

			// onAddNewCompliance();
			if (tmpAudit.getTmpAuditPicCompliances() == null || tmpAudit.getTmpAuditPicCompliances().size() == 0) {
				tmpAudit.setTmpAuditPicCompliances(new ArrayList<TmpAuditPicCompliance>());
				lastSequenceOfCompliance = 0;
			}

			TmpAuditPicCompliance rt = new TmpAuditPicCompliance();
			lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
			rt.setSequence(lastSequenceOfCompliance);

			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			rt.setUser(user);
			tmpAudit.getTmpAuditPicCompliances().add(rt);
			tableModelCompliance.setWrappedData(tmpAudit.getTmpAuditPicCompliances());
			facesUtil.setSessionAttribute("token", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		disabledFollowUpStatus = false;
		Long idLong = Long.parseLong(editId);
		tmpAudit = tmpAuditService.findById(idLong);
		
		for(TmpAuditCheckPoint tapf : tmpAudit.getTmpAuditCheckPoints() ) {
			
			if(tapf.getColumnModel() == null) {
				int idx = tmpAudit.getTmpAuditCheckPoints().indexOf(tapf);
				ColumnModel cm = new ColumnModel();
				cm.setRow(idx);
				cm.setColumnModels(new ArrayList<ColumnModel>());
				
				int i=0;
				while(i < tapf.getColumn()) {
					cm.getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
					i++;
				}
				
				tapf.setColumnModel(cm);
			}
			
			tapf.getTmpAuditPicFollowupBankCommitments().forEach( bCommit -> {
			
			if (bCommit.getTmpAuditPicFollowups() != null) {
				lastSequenceOfFollowup = bCommit.getTmpAuditPicFollowups().size();
				for (int i = 0; i < bCommit.getTmpAuditPicFollowups().size(); i++) {
					TmpAuditPicFollowup dtl = bCommit.getTmpAuditPicFollowups().get(i);
					lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
					dtl.setSequence(lastSequenceOfFollowup);
					dtl.setOldTargetDate(dtl.getTargetDate());

				}
			}
			
			bCommit.getTmpAuditPicFollowups().forEach(picFollowup -> {
			
			if(picFollowup.getTmpAuditPicFollowupSupportingUnits() != null
					&& !picFollowup.getTmpAuditPicFollowupSupportingUnits().isEmpty())
				picFollowup.getTmpAuditPicFollowupSupportingUnits().forEach(supp -> {
					if (supp.getEmailCc1() != null)
						supp.setEmailCcTemp1(supp.getEmailCc1().getNik() + "-" + supp.getEmailCc1().getName());
					if (supp.getEmailCc2() != null)
						supp.setEmailCcTemp2(supp.getEmailCc2().getNik() + "-" + supp.getEmailCc2().getName());
					if (supp.getEmailCc3() != null)
						supp.setEmailCcTemp3(supp.getEmailCc3().getNik() + "-" + supp.getEmailCc3().getName());
				});
			});
		});
		}

		lastSequenceOfCompliance = 0;
		lastSequenceOfFollowup = 0;

		if (tmpAudit.getCounterType() != null) {
			counterTypeId = tmpAudit.getCounterType().getCounterTypeId();
			oldCounterTypeId = new Long(counterTypeId);
		}

		if (tmpAudit.getTmpAuditPicCompliances() != null) {
			lastSequenceOfCompliance = tmpAudit.getTmpAuditPicCompliances().size();
			for (int i = 0; i < tmpAudit.getTmpAuditPicCompliances().size(); i++) {
				TmpAuditPicCompliance dtl = tmpAudit.getTmpAuditPicCompliances().get(i);
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);
			}
		}

		

		
		uploadedFilesAuditBankAttachment = new ArrayList<UploadedFileWO>();
		for (TmpAuditDocument tad : tmpAudit.getTmpAuditDocuments()) {
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(tad.getAttachmentFile());
			uf.setFileId(tad.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(tad.getFileSize());
			uploadedFilesAuditBankAttachment.add(uf);
		}

		tableModelCompliance = new TmpAuditPICComplianceTableModel<TmpAuditPicCompliance>(
				tmpAudit.getTmpAuditPicCompliances());
		tableModelFollowup = new TmpAuditCheckPointTableModel<TmpAuditCheckPoint>(tmpAudit.getTmpAuditCheckPoints());

	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
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
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/tmpAuditApproval/tmpAuditApproval.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void approve() {
		try {
			tmpAuditApprovalService.processApprove(tmpAudit, note, facesUtil.retrieveUserLogin(),
					DATA_ACTIVE, STATUS_APPROVED);
			
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
	        
	     // this should be a singleton
	        ExecutorService emailExecutor2 = Executors.newCachedThreadPool();

	        // from you sendEmail() method
	        emailExecutor2.execute(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                	sendEmailApprove();
	                } catch (Exception e) {
	                    logger.error("send email failed", e);
	                }
	            }
	        });
	        
			facesUtil.redirect("/pages/tmpAuditApproval/tmpAuditApproval.faces");
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void revise() {
		try {

			if (StringUtils.isEmpty(note)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formAuditApprovalNote") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
			} else {
				tmpAuditApprovalService.processApprove(tmpAudit, note,
						facesUtil.retrieveUserLogin(), DATA_REVISE, STATUS_REVISE);
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
				facesUtil.redirect("/pages/tmpAuditApproval/tmpAuditApproval.faces");
			}
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}
	
	public void sendEmailApprove() {
		try {
			
			String emailSubject = "LCCA Approval Notification - Audit";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String auditorTemp = "";
			ParameterDetail paramAuditorTemp = parameterDetailService.getParameterDetailByParamDtlCode(tmpAudit.getAuditor());
			
			auditorTemp = paramAuditorTemp.getNameIn() != null ? paramAuditorTemp.getNameIn() : "";

			emailContent = "("+auditorTemp+" - "+tmpAudit.getFindingNameIn()+") telah disetujui <br/><br/>";
			if(tmpAudit.getTmpAuditApprovals()!=null && tmpAudit.getTmpAuditApprovals().size()>0){
			emailContent = emailContent+"Catatan: <br/>";
			emailContent = emailContent+""+tmpAudit.getTmpAuditApprovals().get(tmpAudit.getTmpAuditApprovals().size()-1).getApprovalNote()+ "";
			}
			
			emailTo = userService.getUserByNik(tmpAudit.getCreatedBy()).getEmail();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_AUDIT", "true", parameterDetailService);
						
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void sendEmailReject() {
		try {
			
			String emailSubject = "LCCA Approval Notification - Audit";
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		   
			String auditorTemp = "";
			ParameterDetail paramAuditorTemp = parameterDetailService.getParameterDetailByParamDtlCode(tmpAudit.getAuditor());
			
			auditorTemp = paramAuditorTemp.getNameIn() != null ? paramAuditorTemp.getNameIn() : "";

			emailContent = "("+auditorTemp+" - "+tmpAudit.getFindingNameIn()+") telah ditolak <br/><br/>";
			if(tmpAudit.getTmpAuditApprovals()!=null && tmpAudit.getTmpAuditApprovals().size()>0){
			emailContent = emailContent+"Catatan: <br/>";
			emailContent = emailContent+""+tmpAudit.getTmpAuditApprovals().get(tmpAudit.getTmpAuditApprovals().size()-1).getApprovalNote()+ "";
			}
			
			emailTo = userService.getUserByNik(tmpAudit.getCreatedBy()).getEmail();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_AUDIT", "true", parameterDetailService);
					
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	

	
	private void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_TEMPLATE_AUDIT");
			String emailSubject = replaceAll(emailTemplate.getEmailSubject(),TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_COUNTER_TYPE,"NOTIFICATION");

			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_HOST_NAME_APPLICATION);
			
		    if (tmpAudit.getReminderStatus()!=null && tmpAudit.getReminderStatus().equals(REMINDER_ACTIVE) 
		    		&& tmpAudit.getFollowUp() != null && tmpAudit.getFollowUp().equals(Constants.CONSTANT_YES)
		    		&& tmpAudit.getTmpAuditCheckPoints() != null && tmpAudit.getTmpAuditCheckPoints().size() > 0
		    		) {
				for (int i = 0; i < tmpAudit.getTmpAuditCheckPoints().size(); i++) {
					TmpAuditCheckPoint tmpAuditCheckPoint = tmpAudit.getTmpAuditCheckPoints().get(i);
					for(int x=0;x<tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments().size();x++) {
						TmpAuditPicFollowupBankCommitment tmpAuditPicFollowupBankCommitment = tmpAuditCheckPoint.getTmpAuditPicFollowupBankCommitments().get(x);
						for(int y=0;y<tmpAuditPicFollowupBankCommitment.getTmpAuditPicFollowups().size();y++) {
							TmpAuditPicFollowup tmpAuditPicFollowup = tmpAuditPicFollowupBankCommitment.getTmpAuditPicFollowups().get(y);
							buildAndSendEmail(emailTemplate, emailSubject, pdHostName, tmpAuditPicFollowup);
						}
					}
					
					
				}
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	private void buildAndSendEmail(EmailTemplate emailTemplate, String emailSubject, ParameterDetail pdHostName, TmpAuditPicFollowup tmp)
			throws Exception {
		String emailContent;
		String emailTo;
		String emailCc1;
		String emailCc2;
		String emailCc;
		String emailCcSupporting;
		
		emailContent = emailTemplate.getEmailContent();
		/*emailContent = emailTemplate.getEmailContent().replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_TARGET_DATE, tmp.getTargetDate()!=null?sdf.format(tmp.getTargetDate()):"");
		ParameterDetail audit = null;
		if (tmpAudit.getAuditObject() != null && !tmpAudit.getAuditObject().isEmpty()) {
			audit = parameterDetailService.getParameterDetailByParamDtlCode(tmpAudit.getAuditObject());
		}
		if (audit != null) {
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_OBJECT_IN, audit.getNameIn()!=null?audit.getNameIn():"");
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_OBJECT_EN, audit.getNameEn()!=null?audit.getNameEn():"");
		} else {
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_OBJECT_IN, "");
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_OBJECT_EN, "");
		}
		emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TOPIC_IN, tmpAudit.getAuditTopicIn()!=null?tmpAudit.getAuditTopicIn():"");
		emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TOPIC_EN, tmpAudit.getAuditTopicEn()!=null?tmpAudit.getAuditTopicEn():"");
		audit = null;
		if (tmpAudit.getAuditCategory() != null && !tmpAudit.getAuditCategory().isEmpty()) {
			audit = parameterDetailService.getParameterDetailByParamDtlCode(tmpAudit.getAuditCategory());
		}
		if (audit != null) {
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_CATEGORY_IN, audit.getNameIn() !=null?audit.getNameIn():"");
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_CATEGORY_IN, audit.getNameEn() !=null?audit.getNameEn():"");
		} else {
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_CATEGORY_IN, "");
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_CATEGORY_IN, "");
		}
		audit = null;
		if (tmpAudit.getAuditor() != null && !tmpAudit.getAuditor().isEmpty()) {
			audit = parameterDetailService.getParameterDetailByParamDtlCode(tmpAudit.getAuditor());
		}
		if (audit != null) {
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TYPE_IN, audit.getNameIn() !=null?audit.getNameIn():"");
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TYPE_EN, audit.getNameEn() !=null?audit.getNameEn():"");
		} else {
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TYPE_IN, "");
			emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TYPE_EN, "");
		}
		StringBuffer sb = new StringBuffer();
		sb.append(tmpAudit.getAuditDateFrom() != null ? DateUtil.dateToString(tmpAudit.getAuditDateFrom())  : "");
		if(tmpAudit.getAuditDateTo() != null) {
			sb.append(" - ");
			sb.append(tmpAudit.getAuditDateTo() != null ? DateUtil.dateToString(tmpAudit.getAuditDateTo()) : "");
		}
		
		emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_DATE, sb.toString());
		emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_SCOPE, tmpAudit.getScope());*/
		String token = "";
		String urlLink = "";
		String menuId = "";
		//if (tmpAudit.getFollowUp() != null && tmpAudit.getFollowUp().equals(CommonConstants.Y)) {
			token = Constants.encryptString(tmp.getAuditPicFollowupId().toString());
			menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_AUDIT);
			urlLink = pdHostName.getNameIn().concat("pages/auditFE/auditFEEdit.faces?token="+token+"&menuId="+menuId);
		//}
		
		emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_URL_LINK, urlLink);
		
		
		
		emailCcSupporting = "";
		if(tmp.getTmpAuditPicFollowupSupportingUnits() != null && !tmp.getTmpAuditPicFollowupSupportingUnits().isEmpty()) {
			for(TmpAuditPicFollowupSupportingUnit su : tmp.getTmpAuditPicFollowupSupportingUnits()) {
				if(su.getEmailCc1()!=null && su.getEmailCc1().getEmail()!=null) {
					emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getEmailCc1().getEmail())):emailCcSupporting.concat(su.getEmailCc1().getEmail());
				}
				if(su.getEmailCc2()!=null && su.getEmailCc2().getEmail()!=null) {
					emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getEmailCc2().getEmail())):emailCcSupporting.concat(su.getEmailCc2().getEmail());
				}
				if(su.getEmailCc3()!=null && su.getEmailCc3().getEmail()!=null) {
					emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getEmailCc3().getEmail())):emailCcSupporting.concat(su.getEmailCc3().getEmail());
				}
			}
		}
		
		emailCc = "";
		

		if (tmpAudit != null) {
			if(tmpAudit.getCounterType() != null && tmpAudit.getCounterType().getDetails() != null && tmpAudit.getCounterType().getDetails().size() > 0) {
				CounterTypeDtl cd = tmpAudit.getCounterType().getDetails().get(0);
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
			if (StringUtils.isNotEmpty(emailCcSupporting)) {
				if(StringUtils.isNotEmpty(emailCc)) {
					emailCc= emailCc.concat(",").concat(emailCcSupporting);
				}else {
					emailCc = emailCcSupporting;
				}
			}
			
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			//final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;
			
			CallApiManager.sendEmailAPI(to,cc, subject,
					content, "EMAIL_TEMPLATE_AUDIT", "true", parameterDetailService);
			
		}
	}
	
	private String replaceAll(String INPUT,String REGEX,String REPLACE) {
		Pattern p = Pattern.compile(REGEX);
	      //get a matcher object
	      Matcher m = p.matcher(INPUT);
	      INPUT = m.replaceAll(REPLACE);
		return INPUT;
	}

	public void onChangeAuditMaster() {
		MstAuditVO getSingleData = mstAuditService.getSingleDataMstAudit(tmpAudit.getMstAudit().getMstAuditId());
		
		try {
			tmpAudit.setAuditor(getSingleData.getAuditorCode());
			tmpAudit.setAuditDateFrom(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateFrom()));
			tmpAudit.setAuditDateTo(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateTo()));
			tmpAudit.setScope(getSingleData.getScope());
		} catch (ParseException e) {
			e.printStackTrace();
		}
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

	public static Logger getLogger() {
		return logger;
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

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public List<SelectItem> getSelectAuditFollowUp() {
		return selectAuditFollowUp;
	}

	public void setSelectAuditFollowUp(List<SelectItem> selectAuditFollowUp) {
		this.selectAuditFollowUp = selectAuditFollowUp;
	}

	public List<SelectItem> getSelectAuditObject() {
		return selectAuditObject;
	}

	public void setSelectAuditObject(List<SelectItem> selectAuditObject) {
		this.selectAuditObject = selectAuditObject;
	}

	public TmpAudit getTmpAudit() {
		return tmpAudit;
	}

	public void setTmpAudit(TmpAudit tmpAudit) {
		this.tmpAudit = tmpAudit;
	}

	public List<SelectItem> getSelectAuditCategory() {
		return selectAuditCategory;
	}

	public void setSelectAuditCategory(List<SelectItem> selectAuditCategory) {
		this.selectAuditCategory = selectAuditCategory;
	}

	public List<UploadedFileWO> getUploadedFilesAuditFindings() {
		return uploadedFilesAuditFindings;
	}

	public void setUploadedFilesAuditFindings(List<UploadedFileWO> uploadedFilesAuditFindings) {
		this.uploadedFilesAuditFindings = uploadedFilesAuditFindings;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankResponse() {
		return uploadedFilesAuditBankResponse;
	}

	public void setUploadedFilesAuditBankResponse(List<UploadedFileWO> uploadedFilesAuditBankResponse) {
		this.uploadedFilesAuditBankResponse = uploadedFilesAuditBankResponse;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankCommitment() {
		return uploadedFilesAuditBankCommitment;
	}

	public void setUploadedFilesAuditBankCommitment(List<UploadedFileWO> uploadedFilesAuditBankCommitment) {
		this.uploadedFilesAuditBankCommitment = uploadedFilesAuditBankCommitment;
	}

	public TmpAuditService getTmpAuditService() {
		return tmpAuditService;
	}

	public void setTmpAuditService(TmpAuditService tmpAuditService) {
		this.tmpAuditService = tmpAuditService;
	}

	public TmpAuditPICComplianceTableModel<TmpAuditPicCompliance> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(TmpAuditPICComplianceTableModel<TmpAuditPicCompliance> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public TmpAuditPicCompliance[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(TmpAuditPicCompliance[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	

	public TmpAuditCheckPointTableModel<TmpAuditCheckPoint> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(TmpAuditCheckPointTableModel<TmpAuditCheckPoint> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public TmpAuditPicFollowup[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(TmpAuditPicFollowup[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public TmpAuditApprovalService getTmpAuditApprovalService() {
		return tmpAuditApprovalService;
	}

	public void setTmpAuditApprovalService(TmpAuditApprovalService tmpAuditApprovalService) {
		this.tmpAuditApprovalService = tmpAuditApprovalService;
	}

	public List<SelectItem> getYesNo() {
		return yesNo;
	}

	public void setYesNo(List<SelectItem> yesNo) {
		this.yesNo = yesNo;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankAttachment() {
		return uploadedFilesAuditBankAttachment;
	}

	public void setUploadedFilesAuditBankAttachment(List<UploadedFileWO> uploadedFilesAuditBankAttachment) {
		this.uploadedFilesAuditBankAttachment = uploadedFilesAuditBankAttachment;
	}

	public String getApprovalStat() {
		return approvalStat;
	}

	public void setApprovalStat(String approvalStat) {
		this.approvalStat = approvalStat;
	}

	public List<SelectItem> getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(List<SelectItem> approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public List<SelectItem> getTemplateAuditList() {
		return templateAuditList;
	}

	public void setTemplateAuditList(List<SelectItem> templateAuditList) {
		this.templateAuditList = templateAuditList;
	}

}