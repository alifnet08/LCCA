package com.wo.module.engine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.email.service.EmailService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;

public class SendEmailQAForCloseEveryHourController implements Job {

	@SuppressWarnings("unused")
	private static final String OSCAR_IN_ = "oscar_";
	private static Logger log = Logger.getLogger(SendEmailQAForCloseEveryHourController.class);
	private static SendEmailQAForCloseEveryHourController sendEmailController;
//	private LogService logService = LogServiceJobImpl.getInstance();
//	private OscarJobService oscarJobService = OscarJobServiceImpl.getInstance();
	private final String functionId = "Email";
	private final String userId = "SYSTEM";
	
	private ApplicationContext appContext;
	private LogService logService;
	private SendEmailService sendEmailService;
	private EmailService emailService;

	public SendEmailQAForCloseEveryHourController() {
	}

	public static synchronized SendEmailQAForCloseEveryHourController getInstance() {
		if (sendEmailController == null) {
			sendEmailController = new SendEmailQAForCloseEveryHourController();
		}
		return sendEmailController;
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
			
			
			location = "Send Email QA To PIC for Closing";
			List<QA> listDataClose = null;
			try {
				
				listDataClose = sendEmailService.getListDataQAClose();
				
				for(int i=0;i<listDataClose.size();i++) {
					String emailTo  = "";
					String emailCc = "";
					
					QA vo = listDataClose.get(i);
					
					String emailContent = sendEmailService.getEmailContent(QAConstants.EMAIL_QA_CLOSE); 
					String emailSubject = sendEmailService.getEmailSubject(QAConstants.EMAIL_QA_CLOSE);
					if (vo.getH() != null) {
						
						emailSubject = emailSubject.replaceAll("counter_type", "H" + vo.getH());
						
					} else {
						emailSubject = emailSubject.replaceAll("counter_type", "");
					}
					
					emailSubject = emailSubject.replaceAll("ticket_no", vo.getTicketNo());
					
					emailContent = emailContent.replaceAll("division_name",vo.getDivisionName());
					emailContent = emailContent.replaceAll("q_date",vo.getqDateStr());
					emailContent = emailContent.replaceAll("q_name",vo.getqUserName());
					emailContent = emailContent.replaceAll("ticket_no",vo.getTicketNo());
					emailContent = emailContent.replaceAll("q_title",vo.getTitle());
					emailContent = emailContent.replaceAll("q_question",vo.getQuestion());
						
						
				    //emailTo = vo.getaEmail();
					//emailCc = vo.getPukEmail()!=null?vo.getPukEmail():"";
					
					List<SendEmailVO> list =  sendEmailService.getListEmailAdminByQnaCategory(vo.getCategoryType());
					for(int x=0;x<list.size();x++){
						SendEmailVO vo2 = list.get(x);
						if(emailTo.equals("")){
								emailTo = vo2.getEmailTo();
						}else{
							if(emailCc.equals("")){
								emailCc = vo2.getEmailTo();
							}else{
								emailCc = emailCc.concat(",").concat(vo2.getEmailTo());
							}
						}
					}
						
						String result = "";
						try {
							result = CallApiManager.sendEmailAPIScheduller(emailTo,emailCc, emailSubject,
									emailContent, QAConstants.EMAIL_QA_CLOSE, "true", sendEmailService);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
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
