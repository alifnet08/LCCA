package com.wo.module.engine;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.wo.module.common.config.ApplicationContextProvider;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.utility.EmailUtil;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupEmail;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.email.service.EmailService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.engine.vo.SendEmailDetailVO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpEmailTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupEmailTrc;
import com.wo.module.tmpAuditApproval.constant.TmpAuditApprovalConstant;
import com.wo.module.tmpComplianceReviewApproval.constant.TmpComplianceReviewApprovalConstants;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupEmail;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupEmail;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupEmail;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupEmail;

public class SendEmailController implements Job {

	@SuppressWarnings("unused")
	private static final String OSCAR_IN_ = "oscar_";
	private static Logger log = Logger.getLogger(SendEmailController.class);
	private static SendEmailController sendEmailController;
//	private LogService logService = LogServiceJobImpl.getInstance();
//	private OscarJobService oscarJobService = OscarJobServiceImpl.getInstance();
	private final String functionId = "Email";
	private final String userId = "SYSTEM";
	
	private ApplicationContext appContext;
	private LogService logService;
	private SendEmailService sendEmailService;
	private EmailService emailService;

	public SendEmailController() {
	}

	public static synchronized SendEmailController getInstance() {
		if (sendEmailController == null) {
			sendEmailController = new SendEmailController();
		}
		return sendEmailController;
	}

	private class Wrapper {
		public Object ref;

		public Wrapper(Object ref) {
			this.ref = ref;
		}
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Start Session Email");
		SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
		try {
			initInjection(context);
			
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId,
					ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
			logH.setLogDetailList(new ArrayList<LogDetail>());
			String location = "Create Log Header";
			String msg = "";
			Wrapper seq = new Wrapper(new Long(0));
			boolean error = false;
			try {
				getLogService().save(logH);
			} catch (Exception ex) {
				msg = "Saving Log fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Update Email Date";
			try {
				sendEmailService.procedureUpdateEmailDate();
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email Socialization";
			List<SendEmailVO> listData = null;
			try {
				
				listData = sendEmailService.getListDataSocialization();
				
				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc1 = "";
					String emailCc2 = "";
					String emailCc = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_SOSIALISASI"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_SOSIALISASI"); 
					
					String token = Constants.encryptString(vo.getId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_SOCIALIZATION);
					String urlLink = pdHostName.concat("pages/socializationFE/socializationFEEdit.faces?token="+token+"&menuId="+menuId);
					
						emailSubject = emailSubject.replaceAll("counter_type",vo.getCounterType());
						emailSubject = emailSubject.replaceAll("regulation_title_in",vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						emailSubject = emailSubject.replaceAll("regulation_title_en",vo.getRegulationTitleEn()!=null?vo.getRegulationTitleEn():"");
						
						emailContent = emailContent.replaceAll("target_date", vo.getTargetDate()!=null?sdf.format(vo.getTargetDate()):"");
						emailContent = emailContent.replaceAll("document_number", vo.getDocumentNo()!=null?vo.getDocumentNo():"");
						emailContent = emailContent.replaceAll("regulation_title_in", vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						emailContent = emailContent.replaceAll("regulation_title_en", vo.getRegulationTitleEn()!=null?vo.getRegulationTitleEn():"");
						emailContent = emailContent.replaceAll("published_date", vo.getPublishedDate()!=null?sdf.format(vo.getPublishedDate()):"");
						emailContent = emailContent.replaceAll("effective_date", vo.getEffctiveDate()!=null?sdf.format(vo.getEffctiveDate()):"");
						emailContent = emailContent.replaceAll("url_link", urlLink);
						
						emailTo = vo.getEmailTo();
						emailCc1 = vo.getEmailCc1();
						emailCc2 = vo.getEmailCc2();
						
						if(emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = vo.getPic1();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = vo.getPic2();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = vo.getPic3();
						}
						
						if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = vo.getPic1();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = vo.getPic2();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = vo.getPic3();
						}
						
						if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = vo.getPic1();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = vo.getPic2();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = vo.getPic3();
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						
						if (StringUtils.isNotEmpty(vo.getEmailCcCompliance())) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(vo.getEmailCcCompliance()):emailCc.concat(vo.getEmailCcCompliance());
						}
						
						//CallApiManager.sendEmailScheduller(emailSubject, emailContent, emailTo,emailCc, sendEmailService);
						String result = "";
						try {
							result = CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, "EMAIL_SOSIALISASI", "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
							
						SocializationPICFollowupEmailTrc emailTrc = sendEmailService.findEmailSocializationById(vo.getEmailId());
						emailTrc.setEmailStatus(result);
						emailTrc.setEmailSubject(emailSubject);
						emailTrc.setEmailContent(emailContent);
						emailTrc.setLastUpdateBy("SOSIALISASI");
						emailTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
						sendEmailService.updateEmailSocialization(emailTrc);
						
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email Correspondence";
			try {
				
				listData = sendEmailService.getListDataCorrespondence();
				
				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc1 = "";
					String emailCc2 = "";
					String emailCc = "";
					String emailCcSupporting = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_KORESPONDENSI"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_KORESPONDENSI"); 
					
					String token = Constants.encryptString(vo.getId().toString());
					String menuId = "";
					String urlLink = "";
					if (vo.getSenderCode() != null && vo.getSenderCode().equals(ParameterHeader.PARAM_HEAD_CODE_SENDER)) {
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE);
						urlLink = pdHostName.concat("pages/correspondenceFE/correspondenceFEEdit.faces?token="+token+"&menuId="+menuId);
					} else if (vo.getSenderCode() != null && vo.getSenderCode().equals(ParameterHeader.PARAM_HEAD_CODE_SENDER_AML)) {
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE_AML);
						urlLink = pdHostName.concat("pages/trcCorrespondenceAml/trcCorrespondenceAmlEdit.faces?token="+token+"&menuId="+menuId);
					}
					
						emailSubject = emailSubject.replaceAll("counter_type",vo.getCounterType());
						emailSubject = emailSubject.replaceAll("perihal_in",vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						emailSubject = emailSubject.replaceAll("perihal_en",vo.getRegulationTitleEn()!=null?vo.getRegulationTitleEn():"");
						emailSubject = emailSubject.replaceAll("letter_no",vo.getLetterNo()!=null?vo.getLetterNo():"");
						
						emailContent = emailContent.replaceAll("perihal_in", vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						emailContent = emailContent.replaceAll("perihal_en", vo.getRegulationTitleEn()!=null?vo.getRegulationTitleEn():"");
						emailContent = emailContent.replaceAll("sender_in", vo.getSenderIn()!=null?vo.getSenderIn():"");
						emailContent = emailContent.replaceAll("sender_en", vo.getSenderEn()!=null?vo.getSenderEn():"");
						emailContent = emailContent.replaceAll("division_name", StringUtils.isNotBlank(vo.getDivisionName()) ? vo.getDivisionName() : "NA");
						emailContent = emailContent.replaceAll("pic_1_name", vo.getPic1Name());
						emailContent = emailContent.replaceAll("pic_2_name", StringUtils.isNotBlank(vo.getPic2Name()) ? vo.getPic2Name() : "NA");
						emailContent = emailContent.replaceAll("pic_3_name", StringUtils.isNotBlank(vo.getPic3Name()) ? vo.getPic3Name() : "NA");
						
						emailContent = emailContent.replaceAll("target_date", vo.getTargetDate()!=null?sdf.format(vo.getTargetDate()):"");
						emailContent = emailContent.replaceAll("letter_no", vo.getLetterNo()!=null?vo.getLetterNo():"");
						emailContent = emailContent.replaceAll("receive_letter_date", vo.getLetterReceiveDate()!=null?sdf.format(vo.getLetterReceiveDate()):"");
						emailContent = emailContent.replaceAll("letter_date", vo.getLetterDate()!=null?sdf.format(vo.getLetterDate()):"");
						emailContent = emailContent.replaceAll("url_link", urlLink);
						
						// add by dwi
						emailContent = emailContent.replaceAll("summary_in", vo.getLetterSummary());
						
						if (vo.getListDetail() != null && vo.getListDetail().size() > 0) {
							if (vo.getListDetail().size() == 1) {
								emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(0).getDivisionName()) ? vo.getListDetail().get(0).getDivisionName() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic1Name()) ? vo.getListDetail().get(0).getPic1Name() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic2Name()) ? vo.getListDetail().get(0).getPic2Name() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic3Name()) ? vo.getListDetail().get(0).getPic3Name() : "NA");
							} else {
								if (emailContent.contains("<tbody>") && emailContent.contains("</tbody>") && emailContent.contains("supporting_")) {
									for (int z = -1; (z = emailContent.indexOf("<tbody>", z + 1)) != -1; z++) {
										String partial = emailContent.substring(z+7, emailContent.indexOf("</tbody>", z+7));
										if (partial.contains("supporting_")) {
											emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(0).getDivisionName()) ? vo.getListDetail().get(0).getDivisionName() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic1Name()) ? vo.getListDetail().get(0).getPic1Name() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic2Name()) ? vo.getListDetail().get(0).getPic2Name() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic3Name()) ? vo.getListDetail().get(0).getPic3Name() : "NA");
											String temp = "";
											for (int y = 1; y < vo.getListDetail().size(); y++) {
												temp += partial;
												temp = temp.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(y).getDivisionName()) ? vo.getListDetail().get(y).getDivisionName() : "NA");
												temp = temp.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic1Name()) ? vo.getListDetail().get(y).getPic1Name() : "NA");
												temp = temp.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic2Name()) ? vo.getListDetail().get(y).getPic2Name() : "NA");
												temp = temp.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic3Name()) ? vo.getListDetail().get(y).getPic3Name() : "NA");
											}
											emailContent = EmailUtil.insertString(emailContent, temp, emailContent.indexOf("</tbody>", z+7) - 1);
											break;
										}
									}
								}
							}
						} else {
							emailContent = emailContent.replaceAll("supporting_name_division", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_1", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_2", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_3", "NA");
						}
						
						if (vo.getListDetail() != null && vo.getListDetail().size() > 0) {
							for (SendEmailDetailVO su : vo.getListDetail()) {
								if(StringUtils.isNotEmpty(su.getPic1Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic1Email())):emailCcSupporting.concat(su.getPic1Email());
								}
								if(StringUtils.isNotEmpty(su.getPic2Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic2Email())):emailCcSupporting.concat(su.getPic2Email());
								}
								if(StringUtils.isNotEmpty(su.getPic3Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic3Email())):emailCcSupporting.concat(su.getPic3Email());
								}
							}
						}
						
						emailTo = vo.getEmailTo();
						emailCc1 = vo.getEmailCc1();
						emailCc2 = vo.getEmailCc2();
						
						if(emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = vo.getPic1();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = vo.getPic2();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = vo.getPic3();
						}
						
						if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = vo.getPic1();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = vo.getPic2();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = vo.getPic3();
						}
						
						if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = vo.getPic1();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = vo.getPic2();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = vo.getPic3();
						}
						
						if(StringUtils.isNotBlank(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotBlank(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						
						if (StringUtils.isNotEmpty(emailCcSupporting)) {
							if(StringUtils.isNotEmpty(emailCc)) {
								emailCc= emailCc.concat(",").concat(emailCcSupporting);
							}else {
								emailCc = emailCcSupporting;
							}
						}
						
						//CallApiManager.sendEmailScheduller(emailSubject, emailContent, emailTo,emailCc, sendEmailService);
						
						String result = "";
						try {
							result = CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, "EMAIL_KORESPONDENSI", "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
						
						TrcCorrespondencePicFollowupEmail emailTrc = sendEmailService.findEmailCorrespondenceById(vo.getEmailId());
						emailTrc.setEmailStatus(result);
						emailTrc.setEmailSubject(emailSubject);
						emailTrc.setEmailContent(emailContent);
						emailTrc.setLastUpdateBy("KORESPONDENSI");
						emailTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
						sendEmailService.updateEmailCorrespondence(emailTrc);
					
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email RMD";
			try {
				
				listData = sendEmailService.getListDataRMD();
				
				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc1 = "";
					String emailCc2 = "";
					String emailCc = "";
					String emailCcSupporting = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_RMD"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_RMD");
					
					String token = Constants.encryptString(vo.getId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_RMD);
					String urlLink = pdHostName.concat("pages/regulatoryReportingFE/regulatoryReportingFEEdit.faces?token="+token+"&menuId="+menuId);
					
						emailSubject = emailSubject.replaceAll("counter_type",vo.getCounterType());
						emailSubject = emailSubject.replaceAll("report_name_in",vo.getReportNameIn()!=null?vo.getReportNameIn():"");
						emailSubject = emailSubject.replaceAll("report_name_en",vo.getReportNameEn()!=null?vo.getReportNameEn():"");
						
						emailContent = emailContent.replaceAll("report_name_in",vo.getReportNameIn()!=null?vo.getReportNameIn():"");
						emailContent = emailContent.replaceAll("report_name_en",vo.getReportNameEn()!=null?vo.getReportNameEn():"");
						emailContent = emailContent.replaceAll("target_date", vo.getTargetDate()!=null?sdf.format(vo.getTargetDate()):"");
						emailContent = emailContent.replaceAll("due_date", vo.getDueDate()!=null?sdf.format(vo.getDueDate()):"");
						emailContent = emailContent.replaceAll("supporting_unit", vo.getSupportingUnit()!=null?vo.getSupportingUnit():"");
						emailContent = emailContent.replaceAll("report_type_in",vo.getReportTypeIn()!=null?vo.getReportTypeIn():"");
						emailContent = emailContent.replaceAll("report_type_en",vo.getReportTypeEn()!=null?vo.getReportTypeEn():"");
						emailContent = emailContent.replaceAll("regulation_title_in",vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						emailContent = emailContent.replaceAll("regulation_title_en",vo.getRegulationTitleEn()!=null?vo.getRegulationTitleEn():"");
						emailContent = emailContent.replaceAll("document_number",vo.getDocumentNo()!=null?vo.getDocumentNo():"");
						emailContent = emailContent.replaceAll("letter_no",vo.getLetterNo()!=null?vo.getLetterNo():"");
						emailContent = emailContent.replaceAll("dedicated_to",vo.getDedicatedTo()!=null?vo.getDedicatedTo():"");
						emailContent = emailContent.replaceAll("sanction",vo.getSanction()!=null?vo.getSanction():"");
						emailContent = emailContent.replaceAll("publisher_unit",vo.getPublisherUnit()!=null?vo.getPublisherUnit():"");
						emailContent = emailContent.replaceAll("url_link", urlLink);
						emailContent = emailContent.replaceAll("division_name", StringUtils.isNotBlank(vo.getDivisionName()) ? vo.getDivisionName() : "NA");
						emailContent = emailContent.replaceAll("pic_1_name", vo.getPic1Name());
						emailContent = emailContent.replaceAll("pic_2_name", StringUtils.isNotBlank(vo.getPic2Name()) ? vo.getPic2Name() : "NA");
						emailContent = emailContent.replaceAll("pic_3_name", StringUtils.isNotBlank(vo.getPic3Name()) ? vo.getPic3Name() : "NA");
						emailContent = emailContent.replaceAll("document_category_in", StringUtils.isNotBlank(vo.getDocumentCategoryIn()) ? vo.getDocumentCategoryIn(): "");
						emailContent = emailContent.replaceAll("document_category_en", StringUtils.isNotBlank(vo.getDocumentCategoryEn()) ? vo.getDocumentCategoryEn(): "");
						
						
						
						if (vo.getListDetail() != null && vo.getListDetail().size() > 0) {
							if (vo.getListDetail().size() == 1) {
								emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(0).getDivisionName()) ? vo.getListDetail().get(0).getDivisionName() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic1Name()) ? vo.getListDetail().get(0).getPic1Name() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic2Name()) ? vo.getListDetail().get(0).getPic2Name() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic3Name()) ? vo.getListDetail().get(0).getPic3Name() : "NA");
							} else {
								if (emailContent.contains("<tbody>") && emailContent.contains("</tbody>") && emailContent.contains("supporting_")) {
									for (int z = -1; (z = emailContent.indexOf("<tbody>", z + 1)) != -1; z++) {
										String partial = emailContent.substring(z+7, emailContent.indexOf("</tbody>", z+7));
										if (partial.contains("supporting_")) {
											emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(0).getDivisionName()) ? vo.getListDetail().get(0).getDivisionName() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic1Name()) ? vo.getListDetail().get(0).getPic1Name() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic2Name()) ? vo.getListDetail().get(0).getPic2Name() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic3Name()) ? vo.getListDetail().get(0).getPic3Name() : "NA");
											String temp = "";
											for (int y = 1; y < vo.getListDetail().size(); y++) {
												temp += partial;
												temp = temp.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(y).getDivisionName()) ? vo.getListDetail().get(y).getDivisionName() : "NA");
												temp = temp.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic1Name()) ? vo.getListDetail().get(y).getPic1Name() : "NA");
												temp = temp.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic2Name()) ? vo.getListDetail().get(y).getPic2Name() : "NA");
												temp = temp.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic3Name()) ? vo.getListDetail().get(y).getPic3Name() : "NA");
											}
											emailContent = EmailUtil.insertString(emailContent, temp, emailContent.indexOf("</tbody>", z+7) - 1);
											break;
										}
									}
								}
							}
						} else {
							emailContent = emailContent.replaceAll("supporting_name_division", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_1", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_2", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_3", "NA");
						}
						
						if (vo.getListDetail() != null && vo.getListDetail().size() > 0) {
							for (SendEmailDetailVO su : vo.getListDetail()) {
								if(StringUtils.isNotEmpty(su.getPic1Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic1Email())):emailCcSupporting.concat(su.getPic1Email());
								}
								if(StringUtils.isNotEmpty(su.getPic2Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic2Email())):emailCcSupporting.concat(su.getPic2Email());
								}
								if(StringUtils.isNotEmpty(su.getPic3Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic3Email())):emailCcSupporting.concat(su.getPic3Email());
								}
							}
						}
						
						emailTo = vo.getEmailTo();
						emailCc1 = vo.getEmailCc1();
						emailCc2 = vo.getEmailCc2();
						
						if(emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = vo.getPic1();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = vo.getPic2();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = vo.getPic3();
						}
						
						if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = vo.getPic1();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = vo.getPic2();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = vo.getPic3();
						}
						
						if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = vo.getPic1();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = vo.getPic2();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = vo.getPic3();
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
						
						//CallApiManager.sendEmailScheduller(emailSubject, emailContent, emailTo,emailCc, sendEmailService);
						String result = "";
						try {
							result =  CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, "EMAIL_RMD", "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
						
						TrcRmdPicFollowupEmail emailTrc = sendEmailService.findEmailRmdById(vo.getEmailId());
						emailTrc.setEmailStatus(result);
						emailTrc.setEmailSubject(emailSubject);
						emailTrc.setEmailContent(emailContent);
						emailTrc.setLastUpdateBy("EMAIL_RMD");
						emailTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
						if(emailTrc.getTrcRmd()!=null && emailTrc.getTrcRmd().getPicFollowupDetails()!=null && emailTrc.getTrcRmd().getPicFollowupDetails().size()>0) {
							TrcRmdPicFollowup trcRmdPicFollowup = emailTrc.getTrcRmd().getPicFollowupDetails().stream().filter(e->e.getTargetDate().equals(emailTrc.getTargetDate())).findFirst().orElse(null);
							if(trcRmdPicFollowup!=null) {
								trcRmdPicFollowup.setEnabledFlag("Y");
								trcRmdPicFollowup.setLastUpdateBy("EMAIL_RMD");
								trcRmdPicFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
								sendEmailService.updateFollowupRmd(trcRmdPicFollowup);
							}
						}
						sendEmailService.updateEmailRmd(emailTrc);
					//System.out.println("result=="+result);
					
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email Compliance Testing";
			try {
				
				listData = sendEmailService.getListDataComplianceReview();
				
				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc1 = "";
					String emailCc2 = "";
					String emailCc = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_TEMPLATE_COMPLIANCE_TESTING"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_TEMPLATE_COMPLIANCE_TESTING"); 
					
					String token = Constants.encryptString(vo.getId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_COMPLIANCE_REVIEW);
					String urlLink = pdHostName.concat("pages/complianceTestingFE/complianceTestingFEEdit.faces?token="+token+"&menuId="+menuId);
					
						emailSubject = emailSubject.replaceAll("counter_type",vo.getCounterType()!=null?vo.getCounterType():"");
						emailSubject = emailSubject.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_NO,vo.getInspectionNo()!=null?vo.getInspectionNo():"");
						
						//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_TARGET_DATE, vo.getTargetDate()!=null?sdf.format(vo.getTargetDate()):"");
						//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_DATE, vo.getDocumentDateStr()!=null?vo.getDocumentDateStr():"");
						//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_PERIHAL_IN, vo.getInspectionTitle()!=null?vo.getInspectionTitle():"");
						//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_PERIHAL_EN, vo.getPerihalEn()!=null?vo.getPerihalEn():"");
						emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_URL_LINK, urlLink);
						
						//set detail value
						/*if(vo.getDetail1() != null && !vo.getDetail1().isEmpty()) {
							String areaReview = "<ul>";
							for(String d1 : vo.getDetail1())
								areaReview += "<li>" + d1 + "</li>";
							areaReview += "</ul>";
							emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_AREA_REVIEW_POIN, areaReview);
						} else {
							emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_AREA_REVIEW_POIN, "NA");
						}
						
						if(vo.getDetail2() != null && !vo.getDetail2().isEmpty()) {
							String temuan = "<ul>";
							for(String d2 : vo.getDetail2())
								temuan += "<li>" + d2 + "</li>";
							temuan += "</ul>";
							emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_TEMUAN_POIN, temuan);
						} else {
							emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_TEMUAN_POIN, "NA");
						}
						
						if(vo.getDetail3() != null && !vo.getDetail3().isEmpty()) {
							String followup = "<ul>";
							for(String d3 : vo.getDetail3())
								followup += "<li>" + d3 + "</li>";
							followup += "</ul>";
							emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_CATATAN_POIN, followup);
						} else {
							emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_CATATAN_POIN, "NA");
						}*/
						
						emailTo = vo.getEmailTo();
						emailCc1 = vo.getEmailCc1();
						emailCc2 = vo.getEmailCc2();
						
						if(emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = vo.getPic1();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = vo.getPic2();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = vo.getPic3();
						}
						
						if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = vo.getPic1();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = vo.getPic2();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = vo.getPic3();
						}
						
						if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = vo.getPic1();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = vo.getPic2();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = vo.getPic3();
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						if (StringUtils.isNotEmpty(vo.getEmailCcCompliance())) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(vo.getEmailCcCompliance()):emailCc.concat(vo.getEmailCcCompliance());
						}
						
						//CallApiManager.sendEmailScheduller(emailSubject, emailContent, emailTo,emailCc, sendEmailService);
						String result = "";
						try {
							result =  CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, "EMAIL_TEMPLATE_COMPLIANCE_TESTING", "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
						
						ComplianceTestingPICFollowupEmail emailTrc = sendEmailService.findEmailComplianceReviewById(vo.getEmailId());
						emailTrc.setEmailStatus(result);
						emailTrc.setEmailSubject(emailSubject);
						emailTrc.setEmailContent(emailContent);
						emailTrc.setLastUpdateBy("COMPLIANCE_REVIEW");
						emailTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
						sendEmailService.updateEmailComplianceReview(emailTrc);
					
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email Audit";
			try {
				
				listData = sendEmailService.getListDataAudit();
				
				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc1 = "";
					String emailCc2 = "";
					String emailCc = "";
					String emailCcSupporting = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_TEMPLATE_AUDIT"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_TEMPLATE_AUDIT"); 
					
					String token = Constants.encryptString(vo.getId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_AUDIT);
					String urlLink = pdHostName.concat("pages/auditFE/auditFEEdit.faces?token="+token+"&menuId="+menuId);
					
						emailSubject = emailSubject.replaceAll("counter_type",vo.getCounterType()!=null?vo.getCounterType():"");
//						emailSubject = emailSubject.replaceAll("perihal_in",vo.getRegulationTitleIn());
//						emailSubject = emailSubject.replaceAll("perihal_en",vo.getRegulationTitleEn());
//						emailSubject = emailSubject.replaceAll("letter_no",vo.getLetterNo());
						
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_TARGET_DATE,vo.getTargetDate()!=null?sdf.format(vo.getTargetDate()):"");
//						emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_OBJECT, vo.getAuditObject()!=null?vo.getAuditObject():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_OBJECT_IN, vo.getAuditObjectIn()!=null?vo.getAuditObjectIn():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_OBJECT_EN, vo.getAuditObjectEn()!=null?vo.getAuditObjectEn():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TOPIC_IN, vo.getAuditTopicIn()!=null?vo.getAuditTopicIn():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TOPIC_EN, vo.getAuditTopicEn()!=null?vo.getAuditTopicEn():"");
//						emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_CATEGORY, vo.getAuditCategory()!=null?vo.getAuditCategory():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_CATEGORY_IN, vo.getAuditCategoryIn()!=null?vo.getAuditCategoryIn():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_CATEGORY_EN, vo.getAuditCategoryEn()!=null?vo.getAuditCategoryEn():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TYPE_IN, vo.getAuditTypeIn()!=null?vo.getAuditTypeIn():"");
						//emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_TYPE_EN, vo.getAuditTypeEn()!=null?vo.getAuditTypeEn():"");
						
						/*StringBuffer sb = new StringBuffer();
						sb.append(vo.getAuditDateFrom() != null ? vo.getAuditDateFrom() : "");
						if(vo.getAuditDateTo() != null) {
							sb.append(" - ");
							sb.append(vo.getAuditDateTo() != null ? vo.getAuditDateTo() : "");
						}
						
						emailContent = emailContent.replaceAll("audit_date", sb.toString());
						emailContent = emailContent.replaceAll("audit_scope", vo.getScope()!=null?vo.getScope():"");*/
						emailContent = emailContent.replaceAll("url_link", urlLink);
						
						//set detail value
						/*if(vo.getDetail1() != null && !vo.getDetail1().isEmpty()) {
							String finding = "<ul>";
							for(String d1 : vo.getDetail1())
								finding += "<li>" + d1 + "</li>";
							finding += "</ul>";
							emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_FINDINGS, finding);
						} else {
							emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_AUDIT_FINDINGS, "NA");
						}
						
						if(vo.getDetail2() != null && !vo.getDetail2().isEmpty()) {
							String response = "<ul>";
							for(String d2 : vo.getDetail2())
								response += "<li>" + d2 + "</li>";
							response += "</ul>";
							emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_BANK_RESPONSE, response);
						} else {
							emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_BANK_RESPONSE, "NA");
						}
						
						if(vo.getDetail3() != null && !vo.getDetail3().isEmpty()) {
							String commitment = "<ul>";
							for(String d3 : vo.getDetail3())
								commitment += "<li>" + d3 + "</li>";
							commitment += "</ul>";
							emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_BANK_COMMITMENT, commitment);
						} else {
							emailContent = emailContent.replaceAll(TmpAuditApprovalConstant.EMAIL_TEMPLATE_EMAIL_BANK_COMMITMENT, "NA");
						}
						
						if (vo.getListDetail() != null && vo.getListDetail().size() > 0) {
							if (vo.getListDetail().size() == 1) {
								emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(0).getDivisionName()) ? vo.getListDetail().get(0).getDivisionName() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic1Name()) ? vo.getListDetail().get(0).getPic1Name() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic2Name()) ? vo.getListDetail().get(0).getPic2Name() : "NA");
								emailContent = emailContent.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic3Name()) ? vo.getListDetail().get(0).getPic3Name() : "NA");
							} else {
								if (emailContent.contains("<tbody>") && emailContent.contains("</tbody>") && emailContent.contains("supporting_")) {
									for (int z = -1; (z = emailContent.indexOf("<tbody>", z + 1)) != -1; z++) {
										String partial = emailContent.substring(z+7, emailContent.indexOf("</tbody>", z+7));
										if (partial.contains("supporting_")) {
											emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(0).getDivisionName()) ? vo.getListDetail().get(0).getDivisionName() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic1Name()) ? vo.getListDetail().get(0).getPic1Name() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic2Name()) ? vo.getListDetail().get(0).getPic2Name() : "NA");
											emailContent = emailContent.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(0).getPic3Name()) ? vo.getListDetail().get(0).getPic3Name() : "NA");
											String temp = "";
											for (int y = 1; y < vo.getListDetail().size(); y++) {
												temp += partial;
												temp = temp.replaceAll("supporting_name_division", StringUtils.isNotBlank(vo.getListDetail().get(y).getDivisionName()) ? vo.getListDetail().get(y).getDivisionName() : "NA");
												temp = temp.replaceAll("supporting_pic_name_1", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic1Name()) ? vo.getListDetail().get(y).getPic1Name() : "NA");
												temp = temp.replaceAll("supporting_pic_name_2", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic2Name()) ? vo.getListDetail().get(y).getPic2Name() : "NA");
												temp = temp.replaceAll("supporting_pic_name_3", StringUtils.isNotBlank(vo.getListDetail().get(y).getPic3Name()) ? vo.getListDetail().get(y).getPic3Name() : "NA");
											}
											emailContent = EmailUtil.insertString(emailContent, temp, emailContent.indexOf("</tbody>", z+7) - 1);
											break;
										}
									}
								}
							}
						} else {
							emailContent = emailContent.replaceAll("supporting_name_division", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_1", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_2", "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_3", "NA");
						}
						
						if (vo.getListDetail() != null && vo.getListDetail().size() > 0) {
							for (SendEmailDetailVO su : vo.getListDetail()) {
								if(StringUtils.isNotEmpty(su.getPic1Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic1Email())):emailCcSupporting.concat(su.getPic1Email());
								}
								if(StringUtils.isNotEmpty(su.getPic2Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic2Email())):emailCcSupporting.concat(su.getPic2Email());
								}
								if(StringUtils.isNotEmpty(su.getPic3Email())) {
									emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getPic3Email())):emailCcSupporting.concat(su.getPic3Email());
								}
							}
						}*/
						
						emailTo = vo.getEmailTo();
						emailCc1 = vo.getEmailCc1();
						emailCc2 = vo.getEmailCc2();
						
						if(emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = vo.getPic1();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = vo.getPic2();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = vo.getPic3();
						}
						
						if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = vo.getPic1();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = vo.getPic2();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = vo.getPic3();
						}
						
						if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = vo.getPic1();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = vo.getPic2();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = vo.getPic3();
						}
						
						if(StringUtils.isNotBlank(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotBlank(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						
						if (StringUtils.isNotEmpty(emailCcSupporting)) {
							if(StringUtils.isNotEmpty(emailCc)) {
								emailCc= emailCc.concat(",").concat(emailCcSupporting);
							}else {
								emailCc = emailCcSupporting;
							}
						}
						
						//CallApiManager.sendEmailScheduller(emailSubject, emailContent, emailTo,emailCc, sendEmailService);
						String result = "";
						try {
							result =  CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, "EMAIL_TEMPLATE_AUDIT", "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
						
						TrcAuditPicFollowupEmail emailTrc = sendEmailService.findEmailAuditById(vo.getEmailId());
						emailTrc.setEmailStatus(result);
						emailTrc.setEmailSubject(emailSubject);
						emailTrc.setEmailContent(emailContent);
						emailTrc.setLastUpdateBy("AUDIT");
						emailTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
						
						sendEmailService.updateEmailAudit(emailTrc);
					
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email Regulation Monitoring";
			
			try {
				
				listData = sendEmailService.getListDataRegulationMonitoring();
				
				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc1 = "";
					String emailCc2 = "";
					String emailCc = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_REGULATION_MONITORING"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_REGULATION_MONITORING"); 
					
					String token = Constants.encryptString(vo.getId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_REG_MONITORING);
					String urlLink = pdHostName.concat("pages/regulationMonitoringFE/regulationMonitoringFEEdit.faces?token="+token+"&menuId="+menuId);
					
						emailSubject = emailSubject.replaceAll("counter_type",vo.getCounterType());
						emailSubject = emailSubject.replaceAll("regulation_title_in",vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						
						
						emailContent = emailContent.replaceAll("target_date", vo.getTargetDate()!=null?sdf.format(vo.getTargetDate()):"");
						emailContent = emailContent.replaceAll("document_number", vo.getDocumentNo()!=null?vo.getDocumentNo():"");
						emailContent = emailContent.replaceAll("regulation_title_in", vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						emailContent = emailContent.replaceAll("published_date", vo.getPublishedDate()!=null?sdf.format(vo.getPublishedDate()):"");
						emailContent = emailContent.replaceAll("effective_date", vo.getEffctiveDate()!=null?sdf.format(vo.getEffctiveDate()):"");
						emailContent = emailContent.replaceAll("url_link", urlLink);
						
						emailTo = vo.getEmailTo();
						emailCc1 = vo.getEmailCc1();
						emailCc2 = vo.getEmailCc2();
						
						if(emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = vo.getPic1();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = vo.getPic2();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = vo.getPic3();
						}
						
						if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = vo.getPic1();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = vo.getPic2();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = vo.getPic3();
						}
						
						if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = vo.getPic1();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = vo.getPic2();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = vo.getPic3();
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						
						//CallApiManager.sendEmailScheduller(emailSubject, emailContent, emailTo,emailCc, sendEmailService);
						String result = "";
						try {
							result = CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, "EMAIL_REGULATION_MONITORING", "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
							
						RegMonitoringPICFollowUpEmailTrc emailTrc = sendEmailService.findEmailRegMonitoringById(vo.getEmailId());
						emailTrc.setEmailStatus(result);
						emailTrc.setEmailSubject(emailSubject);
						emailTrc.setEmailContent(emailContent);
						emailTrc.setLastUpdateBy("REGULATION_MONITORING");
						emailTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
						sendEmailService.updateEmailRegMonitoring(emailTrc);
						
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email Denda";
			
			try {
				
				listData = sendEmailService.getListDataDenda();
				
				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc1 = "";
					String emailCc2 = "";
					String emailCc = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_DENDA"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_DENDA"); 
					
					String token = Constants.encryptString(vo.getId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_FINE);
					String urlLink = pdHostName.concat("pages/fineFE/fineFEEdit.faces?token="+token+"&menuId="+menuId);
					
						emailSubject = emailSubject.replaceAll("counter_type", (vo.getCounterType() != null ? vo.getCounterType() : ""));
						emailSubject = emailSubject.replaceAll("letter_no",vo.getLetterNo()!=null?vo.getLetterNo():"");
						
						emailContent = emailContent.replaceAll("perihal_in", vo.getPerihalIn()!=null?vo.getPerihalIn():"");
						emailContent = emailContent.replaceAll("sender_in", vo.getSenderIn()!=null?vo.getSenderIn():"");
						emailContent = emailContent.replaceAll("letter_no",vo.getLetterNo()!=null?vo.getLetterNo():"");
						emailContent = emailContent.replaceAll("letter_date",vo.getLetterDate()!=null?sdf.format(vo.getLetterDate()):"");
						emailContent = emailContent.replaceAll("region_code",vo.getRegionCode()!=null?vo.getRegionCode():"");
						emailContent = emailContent.replaceAll("debited",vo.getDebitted()!=null?sdf.format(vo.getDebitted()):"");
						emailContent = emailContent.replaceAll("nominal", vo.getAmount()!=null?vo.getAmount().toString():"");
						emailContent = emailContent.replaceAll("breaches", vo.getBreaches()!=null?vo.getBreaches():"");
						emailContent = emailContent.replaceAll("root_cause", vo.getRootCause()!=null?vo.getRootCause():"");
						emailContent = emailContent.replaceAll("category", vo.getCategory()!=null?vo.getCategory():"");
						emailContent = emailContent.replaceAll("division_name", vo.getDivisionName()!=null?vo.getDivisionName():"");
						emailContent = emailContent.replaceAll("pic_1_name", vo.getPic1Name()!=null?vo.getPic1Name():"");
						emailContent = emailContent.replaceAll("pic_2_name", vo.getPic2Name()!=null?vo.getPic2Name():"");
						emailContent = emailContent.replaceAll("pic_3_name", vo.getPic3Name()!=null?vo.getPic3Name():"");
						emailContent = emailContent.replaceAll("report_name", vo.getReportNameIn()!=null?vo.getReportNameIn():"");
						
						emailContent = emailContent.replaceAll("url_link", urlLink);
						
						emailTo = vo.getEmailTo();
						emailCc1 = vo.getEmailCc1();
						emailCc2 = vo.getEmailCc2();
						
						if(emailTo.equals(Constants.REMINDER_PIC1)) {
							emailTo = vo.getPic1();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC2)) {
							emailTo = vo.getPic2();
						}
						else if(emailTo.equals(Constants.REMINDER_PIC3)) {
							emailTo = vo.getPic3();
						}
						
						if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
							emailCc1 = vo.getPic1();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
							emailCc1 = vo.getPic2();
						}
						else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
							emailCc1 = vo.getPic3();
						}
						
						if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
							emailCc2 = vo.getPic1();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
							emailCc2 = vo.getPic2();
						}
						else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
							emailCc2 = vo.getPic3();
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						
						//CallApiManager.sendEmailScheduller(emailSubject, emailContent, emailTo,emailCc, sendEmailService);
						String result = "";
						try {
							result = CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, "EMAIL_DENDA", "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
							
						TrcFinePicFollowupEmail emailTrc = sendEmailService.findEmailDendaById(vo.getEmailId());
						emailTrc.setEmailStatus(result);
						emailTrc.setEmailSubject(emailSubject);
						emailTrc.setEmailContent(emailContent);
						emailTrc.setLastUpdateBy("DENDA");
						emailTrc.setLastUpdateDate(new Timestamp(new Date().getTime()));
						sendEmailService.updateEmailDenda(emailTrc);
						
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email Expired Regulation";
			
			try {
				
				listData = sendEmailService.getListDataRegulationMonitoring();
				
//				String pdHostName = sendEmailService.getSystemProperty("HOST_NAME_APPLICATION");
				
				for(int i=0;i<listData.size();i++) {
					String emailTo  = "";
					String emailCc = "";
					
					SendEmailVO vo = listData.get(i);
					
					String emailContent = sendEmailService.getEmailContent("EMAIL_PERATURAN_EXPIRED"); 
					String emailSubject = sendEmailService.getEmailSubject("EMAIL_PERATURAN_EXPIRED"); 
					
//					String token = Constants.encryptString(vo.getId().toString());
					
						emailContent = emailContent.replaceAll("month_expired",vo.getSla());
						emailContent = emailContent.replaceAll("expired_date",vo.getTargetDate()!=null?sdf.format(vo.getTargetDate()):"");
						emailContent = emailContent.replaceAll("document_number",vo.getDocumentNo()!=null?vo.getDocumentNo():"");
						emailContent = emailContent.replaceAll("regulation_title_in", vo.getRegulationTitleIn()!=null?vo.getRegulationTitleIn():"");
						emailContent = emailContent.replaceAll("regulation_title_en", vo.getRegulationTitleEn()!=null?vo.getRegulationTitleEn():"");
						
						if (vo.getEmailTo() != null)
							emailTo = vo.getEmailTo();
						else if (vo.getEmailCc1() != null)
							emailTo = vo.getEmailCc1();
						
						if (vo.getEmailTo() != null && vo.getEmailCc1() != null)
							emailCc = vo.getEmailCc1();
						
						if (emailTo != null) {
							try {
								CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
										emailContent, "EMAIL_PERATURAN_EXPIRED", "true", sendEmailService);
							} catch (Exception ex) {
								msg = location + " fail because " + ex.getMessage();
								addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
								error = true;
							}
						}
						
				}

				//
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			logH.setEndDate(DateUtil.currentSqlTimestamp());
			if (error)
				logH.setProcessSts(ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_E);
			else
				logH.setProcessSts(ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_S);
			
			getLogService().update(logH);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("Finish Session Email");
		}
	}
	
	private void initInjection(JobExecutionContext context) {
		ApplicationContext appContext = (ApplicationContext) context.getMergedJobDataMap().get("applicationContextKey");
		logService = appContext.getBean("logService", LogService.class);
		sendEmailService = appContext.getBean("sendEmailService", SendEmailService.class);
		
//		logService = ApplicationContextProvider.getApplicationContext().getBean("logService", LogService.class);
//		sendEmailService = ApplicationContextProvider.getApplicationContext().getBean("sendEmailService", SendEmailService.class);
	}
	
	@SuppressWarnings("unused")
	private void convertCsvToJson(String fileName, String localFolder) {
		File input = new File(localFolder + fileName);
        File output = new File(localFolder + fileName +".json");
        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build(); 
        CsvMapper csvMapper = new CsvMapper();
        
        try {
			// Read data from CSV file
			List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();
 
			ObjectMapper mapper = new ObjectMapper();
 
			// Write JSON formated data to output.json file
			mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private boolean checkRetrievedFiles(String fileName, String localFolder) {
		boolean exist = false;
		File check = null;
		check = new File(localFolder + fileName);
		if (check.exists()) {
			exist = true; 
		}else {
			exist = false;
			log.error("File " + localFolder + fileName + " Not exist");
		}
		
		return exist;
	}

	private void addLogDetail(LogHeader logH, Wrapper seq, String type, String location, String msg) {
		if (seq == null) {
			seq = new Wrapper(new Long(1));
		} else {
			seq.ref = ((Long) seq.ref).longValue() + 1;
		}

		LogDetail logD = new LogDetail(logH, ((Long) seq.ref).longValue(), type, location, msg);
		logH.getLogDetailList().add(logD);
	}

	@SuppressWarnings("unused")
	private void checkExistingFiles(Map<String, String> retrievedFiles, String fileName,
			ParameterDetail inboundFolder) {
		File check = null;
		String csvFileName = null;

		csvFileName = inboundFolder.getNameIn() + ((inboundFolder.getNameIn().endsWith(CommonConstants.FILE_SEPARATOR))
				? fileName + DateUtil.dateToStringDDMMYYYY(new Date(), true)
				: CommonConstants.FILE_SEPARATOR + fileName + DateUtil.dateToStringDDMMYYYY(new Date(), true));
		csvFileName = csvFileName + CommonConstants.CSV_EXT;
		check = new File(csvFileName);
		if (check.exists()) {
			retrievedFiles.put(csvFileName, csvFileName);
		}
	}

	

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public SendEmailService getSendEmailService() {
		return sendEmailService;
	}

	public void setSendEmailService(SendEmailService sendEmailService) {
		this.sendEmailService = sendEmailService;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	
}
