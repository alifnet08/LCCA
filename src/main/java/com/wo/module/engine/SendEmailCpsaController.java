package com.wo.module.engine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.wo.module.common.vo.SendEmailVo;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicEmailVo;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicVo;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.email.service.EmailService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.internalRegulationPenerbitan.constant.InternalRegulationPenerbitanConstants;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanPicTpkVo;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;

public class SendEmailCpsaController implements Job {

	@SuppressWarnings("unused")
	private static final String OSCAR_IN_ = "oscar_";
	private static Logger log = Logger.getLogger(SendEmailController.class);
	private static SendEmailCpsaController sendEmailCpsaController;
	
	private final String functionId = "Email";
	private final String userId = "SYSTEM";
	
	private ApplicationContext appContext;
	private LogService logService;
	private SendEmailService sendEmailService;
	private EmailService emailService;

	public SendEmailCpsaController() {}

	public static synchronized SendEmailCpsaController getInstance() {
		if (sendEmailCpsaController == null) {
			sendEmailCpsaController = new SendEmailCpsaController();
		}
		return sendEmailCpsaController;
	}

	private class Wrapper {
		public Object ref;

		public Wrapper(Object ref) {
			this.ref = ref;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Start Session Email CPSA");
		SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
		try {
			initInjection(context);
			
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId,
					ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
			logH.setLogDetailList(new ArrayList<LogDetail>());
			String location = "Create Log Header Send Email CPSA";
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
			
			location = "Send Email CPSA to PIC";
			List<CompliancePlanSelfAssessmentPicEmailVo> listData = null;
			try {
				
				listData = sendEmailService.getListDataCpsa();
				
				ParameterDetail pdHostName = sendEmailService.getDataParameter(Constants.HOST_NAME_APPLICATION);
							
				for(int i=0;i<listData.size();i++) {
					CompliancePlanSelfAssessmentPicEmailVo vo = listData.get(i);					
					SendEmailVo sendEmail = new SendEmailVo();
					String emailContent = sendEmailService.getEmailContent(CompliancePlanSelfAssessmentConstant.EMAIL_CPSA); 
					String emailSubject = sendEmailService.getEmailSubject(CompliancePlanSelfAssessmentConstant.EMAIL_CPSA);
					
					String emailTo = "";
					String emailCc = "";
					String emailCc1 = "";
					String emailCc2 = "";
					
					String token = Constants.encryptString(vo.getCpsaId().toString());
					String menuId = Constants.encryptString(Constants.MENU_ID_CPAS_FE);
					String urlLink = "";
					
					if(pdHostName !=null && pdHostName.getNameIn() !=null && 
							!pdHostName.getNameIn().equals("")) {
						urlLink = pdHostName.getNameIn().concat("pages/cpsaFE/compliancePlanSelfAssessmentFEEdit.faces?token="+token+"&menuId="+menuId);
					}                                                         
					
					emailSubject = emailSubject.replaceAll("perihal_surat", vo.getLetterAbout());
				
					emailContent = emailContent.replaceAll("nama_pic", vo.getUser1Name());
					emailContent = emailContent.replaceAll("target_date", vo.getTargetDateStr());
					emailContent = emailContent.replaceAll("url_link", urlLink);
																					
					emailTo = vo.getEmailPic1()!= null ? vo.getEmailPic1() : null;
					
					if(vo.getEmailPic2() != null && !StringUtils.isEmpty(vo.getEmailPic2())) {
						if (StringUtils.isEmpty(emailCc)) {
							emailCc = vo.getEmailPic2();	
						} else {
							emailCc = emailCc.concat(",").concat(vo.getEmailPic2());
						}
					}
					
					if(vo.getEmailPic3() != null && !StringUtils.isEmpty(vo.getEmailPic3())) {
						if (StringUtils.isEmpty(emailCc)) {
							emailCc = vo.getEmailPic3();	
						} else {
							emailCc = emailCc.concat(",").concat(vo.getEmailPic3());
						}
					}
					
					if(vo.getCpsaAdminEmail() != null && !StringUtils.isEmpty(vo.getCpsaAdminEmail())) {
						if (StringUtils.isEmpty(emailCc)) {
							emailCc = vo.getCpsaAdminEmail();	
						} else {
							emailCc = emailCc.concat(",").concat(vo.getCpsaAdminEmail());
						}
					}
																
					String result = "";
					try {
						result = CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
								emailContent, CompliancePlanSelfAssessmentConstant.EMAIL_CPSA, "true", sendEmailService);						
					} catch (Exception ex) {
						result = EmailConstant.EMAIL_STATUS_ERROR;
						msg = location + " fail email CPSA because " + ex.getMessage();
						addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
						error = true;
					}							
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " email fail CPSA because " + ex.getMessage();
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
			log.info("Finish Session Email CPSA");
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
