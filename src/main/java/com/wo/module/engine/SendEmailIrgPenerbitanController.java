package com.wo.module.engine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
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
import com.wo.module.common.vo.SendEmailVo;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.email.service.EmailService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.internalRegulationPenerbitan.constant.InternalRegulationPenerbitanConstants;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanAttachmentVo;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanPicTpgVo;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanPicTpkVo;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;

public class SendEmailIrgPenerbitanController implements Job {

	@SuppressWarnings("unused")
	private static final String OSCAR_IN_ = "oscar_";
	private static Logger log = Logger.getLogger(SendEmailController.class);
	private static SendEmailIrgPenerbitanController sendEmailIrgPenerbitanController;
//	private LogService logService = LogServiceJobImpl.getInstance();
//	private OscarJobService oscarJobService = OscarJobServiceImpl.getInstance();
	private final String functionId = "Email";
	private final String userId = "SYSTEM";
	
	private ApplicationContext appContext;
	private LogService logService;
	private SendEmailService sendEmailService;
	private EmailService emailService;

	public SendEmailIrgPenerbitanController() {}

	public static synchronized SendEmailIrgPenerbitanController getInstance() {
		if (sendEmailIrgPenerbitanController == null) {
			sendEmailIrgPenerbitanController = new SendEmailIrgPenerbitanController();
		}
		return sendEmailIrgPenerbitanController;
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
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
		Locale localeIndo = new Locale("in","ID");
		SimpleDateFormat sdfFull = new SimpleDateFormat("dd MMMMM yyyy",localeIndo);
		try {
			initInjection(context);
			
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId,
					ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
			logH.setLogDetailList(new ArrayList<LogDetail>());
			String location = "Create Log Header Send Email IRG Penerbitan";
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
			
			location = "Send Email IRG Penerbitan to PIC";
			List<InternalRegulationPenerbitanPicTpkVo> listData = null;
			
			try {
				listData = sendEmailService.getListDataIrgPenerbitanPIC();
				List<File> attachmentFileTemp = new ArrayList<>();		
				
				String emailCcPicIrg = "";
				String namePicIrg1 = "";
				String namePicIrg2 = "";
				
				List<SendEmailVo> sendEmailList = new ArrayList<>();
				Map<String, SendEmailVo> sendEmailVoByTargetDateMap = new HashMap<>();
				Calendar calendar = Calendar.getInstance();
				ParameterDetail paramDtl = sendEmailService.getDataParameter("IRG_PENERBITAN_REMAIN");
				String targetDateSendEmail = "";
				for(int i=0;i<listData.size();i++) {
					InternalRegulationPenerbitanPicTpkVo vo = listData.get(i);
					String dataIrgKey = vo.getTargetDateStr().concat("-").concat(String.valueOf(vo.getIrgId()));
					//vo.getSla()
					Integer counterTypeDtlCount = sendEmailService.getCountDataCounterType(vo.getCounterTypeId());
							
					if (sendEmailVoByTargetDateMap.containsKey(dataIrgKey)) { 
						SendEmailVo dataSendEmail = sendEmailVoByTargetDateMap.get(dataIrgKey);
						String emailTo = "";
						String emailCc = "";
						
						if(dataSendEmail.getEmailTo() != null && StringUtils.isNotBlank(dataSendEmail.getEmailTo())) {
							emailTo = dataSendEmail.getEmailTo();
						}
						
						if(dataSendEmail.getEmailCc() != null && StringUtils.isNotBlank(dataSendEmail.getEmailCc())) {
							emailCc = dataSendEmail.getEmailCc();
						}
						
						if (vo.getEmailPic2() != null &&  StringUtils.isNotBlank(vo.getEmailPic2())) {
							if (StringUtils.isBlank(emailTo)) {
								emailTo = vo.getEmailPic2();
							} else {
								emailTo = emailTo.concat(",").concat(vo.getEmailPic2());
							}
						}
						
						if(vo.getEmailPic1() != null && StringUtils.isNotBlank(vo.getEmailPic1())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = vo.getEmailPic1();
							} else {
								emailCc = emailCc.concat(",").concat(vo.getEmailPic1());
							}
						}
						
						if(vo.getEmailPic3() != null && !StringUtils.isNotBlank(vo.getEmailPic3())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = vo.getEmailPic3();	
							} else {
								emailCc = emailCc.concat(",").concat(vo.getEmailPic3());
							}
						}
						
						dataSendEmail.setEmailTo(emailTo);
						dataSendEmail.setEmailCc(emailCc);
						
						sendEmailVoByTargetDateMap.replace(dataIrgKey, dataSendEmail);
					} else {
						SendEmailVo sendEmail = new SendEmailVo();
						List<File> attachmentFileList = new ArrayList<>();
						
						if(i == 0) {													
							int counterDate = 0;
							Date sysdateTimeStamp = new Date();
							String sysdateTimeStampStr = sdf.format(sysdateTimeStamp);
							Date targetDateTmp = sdf.parse(sysdateTimeStampStr);
							
							/*Apparently we need this code snippet to make sure when this data is created on Weekend, 
								the due date is place correctly. Else it will be increase by 1 day */
							int checkDay = calendar.get(Calendar.DAY_OF_WEEK);
							if (checkDay == 1 || checkDay == 7){
								counterDate +=1;
							}
							
							while (counterDate < Integer.parseInt(paramDtl.getNameIn())) {
								int day = calendar.get(Calendar.DAY_OF_WEEK);	
								if (day == 1 || day == 7) {
									// do nothing
								} else {
									if (Boolean.TRUE.equals(sendEmailService.isAvailableDate(targetDateTmp))) {
										counterDate++;
									}
								}
								
								if (counterDate < Integer.parseInt(paramDtl.getNameIn())) {
									calendar.setTime(targetDateTmp);
									calendar.add(Calendar.DAY_OF_MONTH, 1);
									targetDateTmp = calendar.getTime();
									targetDateSendEmail = sdfFull.format(targetDateTmp);
								}
							}
						}
						String emailContent = StringUtils.EMPTY;
						
						if(counterTypeDtlCount !=null && counterTypeDtlCount == 1)
						{
							if(vo.getSla() == 3)
							{
								emailContent = sendEmailService.getEmailContent("EMAIL_IRG_REMINDER_H_PLUS_3"); 
							}
							else if(vo.getSla() == 5)
							{
								emailContent = sendEmailService.getEmailContent("EMAIL_IRG_REMINDER_H_PLUS_5"); 
							}
							else
							{
								emailContent = sendEmailService.getEmailContent(InternalRegulationPenerbitanConstants.EMAIL_IRG_REMINDER); 
							}
						}
						else
						{
							emailContent = sendEmailService.getEmailContent(InternalRegulationPenerbitanConstants.EMAIL_IRG_REMINDER); 
						}
						
						String emailSubject = sendEmailService.getEmailSubject(InternalRegulationPenerbitanConstants.EMAIL_IRG_REMINDER);
						String emailTo  = "";
						String emailCc = "";
						String emailCcPicTpg = "";
						String emailCcEmailGroupTpk = "";
						
						emailSubject = emailSubject.replaceAll("judul_regulasi", vo.getIrgTitle());
						emailSubject = emailSubject.replaceAll("tipe_peraturan", vo.getRegulationTypeName());
						
						emailContent = emailContent.replaceAll("nama_pic_irg1", replaceUserToLowerCaseAndFirstLetterUpper(vo.getIrgPicName1()));
						emailContent = emailContent.replaceAll("Tanggal_Due_Date", targetDateSendEmail);
						
						// PIC TPG
						if (vo.getIrgPicTpgVoList() != null && !vo.getIrgPicTpgVoList().isEmpty()) {
							for (int j = 0; j < vo.getIrgPicTpgVoList().size(); j++) {
								InternalRegulationPenerbitanPicTpgVo dataPicTpg = vo.getIrgPicTpgVoList().get(j);
								
								if (j == 0) {
									emailContent = emailContent.replaceAll("nama_pic_tpg1", StringUtils.isNotBlank(dataPicTpg.getUser1Name()) ? replaceUserToLowerCaseAndFirstLetterUpper(dataPicTpg.getUser1Name()) : "N/A");
								}
								
								if (StringUtils.isNotBlank(dataPicTpg.getUser1Email())) {
									if (StringUtils.isBlank(emailCcPicTpg)) {
										emailCcPicTpg = dataPicTpg.getUser1Email();
									} else {
										emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataPicTpg.getUser1Email());
									}
								}
								
								if (StringUtils.isNotBlank(dataPicTpg.getUser2Email())) {
									if (StringUtils.isBlank(emailCcPicTpg)) {
										emailCcPicTpg = dataPicTpg.getUser2Email();
									} else {
										emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataPicTpg.getUser2Email());
									}
								} 
								
								if (StringUtils.isNotBlank(dataPicTpg.getUser3Email())) {
									if (StringUtils.isBlank(emailCcPicTpg)) {
										emailCcPicTpg = dataPicTpg.getUser3Email();
									} else {
										emailCcPicTpg = emailCcPicTpg.concat(",").concat(dataPicTpg.getUser3Email());
									}
								}
							}
						}
						// PIC TPG
						
						// EMAIL GROUP TPK
						/*if (StringUtils.isNotBlank(vo.getEmailGroupTpk())) {
							if (vo.getEmailGroupTpk().contains(";")) {
								String[] split = vo.getEmailGroupTpk().split(";");
								for (String string : split) {
									if (StringUtils.isBlank(emailCcEmailGroupTpk)) {
										emailCcEmailGroupTpk = string;
									} else {
										emailCcEmailGroupTpk = emailCcEmailGroupTpk.concat(",").concat(string);
									}
								}
							} else {
								emailCcEmailGroupTpk = vo.getEmailGroupTpk();
							}
						}*/
						emailCcEmailGroupTpk = vo.getEmailGroupTpk();
						// EMAIL GROUP TPK
						
						// ATTACHMENT
						if (vo.getIrgAttachmentVoList() != null && !vo.getIrgAttachmentVoList().isEmpty()) {
							for (InternalRegulationPenerbitanAttachmentVo dataIrgAttachment : vo.getIrgAttachmentVoList()) {
								if (StringUtils.isNotBlank(dataIrgAttachment.getFileId())) {
									File file = new File(dataIrgAttachment.getFileId());
									File file1 = new File(dataIrgAttachment.getAttachmentFile());
									FileUtils.copyFile(file, file1);
									
									attachmentFileList.add(file1);
								}
							}
							sendEmail.setAttachmentFileList(attachmentFileList);
							attachmentFileTemp.addAll(attachmentFileList);
						}
						// ATTACHMENT
						
						emailTo = vo.getEmailPic2()!= null ? vo.getEmailPic2() : null;
						
						if(vo.getEmailPic1() != null && StringUtils.isNotBlank(vo.getEmailPic1())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = vo.getEmailPic1();
							} else {
								emailCc = emailCc.concat(",").concat(vo.getEmailPic1());
							}
						}
						
						if(vo.getEmailPic3() != null && StringUtils.isNotBlank(vo.getEmailPic3())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = vo.getEmailPic3();	
							} else {
								emailCc = emailCc.concat(",").concat(vo.getEmailPic3());
							}
						}
						
						if(vo.getEmailIrgPic1() != null && StringUtils.isNotBlank(vo.getEmailIrgPic1())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = vo.getEmailIrgPic1();	
							} else {
								emailCc = emailCc.concat(",").concat(vo.getEmailIrgPic1());
							}
						}
						
						if(vo.getEmailIrgPic2() != null && StringUtils.isNotBlank(vo.getEmailIrgPic2())) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = vo.getEmailIrgPic2();	
							} else {
								emailCc = emailCc.concat(",").concat(vo.getEmailIrgPic2());
							}
						}
						
						if(vo.getEmailIrgPic3() != null && StringUtils.isNotBlank(vo.getEmailIrgPic3())) {
							if(StringUtils.isBlank(emailCc)) {
								emailCc = vo.getEmailIrgPic3();
							}else{
								emailCc = emailCc.concat(",").concat(vo.getEmailIrgPic3());
							}
						}
						
						if(StringUtils.isNotBlank(emailCcPicTpg)) {
							if (StringUtils.isBlank(emailCc)) {
								emailCc = emailCcPicTpg;
							} else {
								emailCc = emailCc.concat(",").concat(emailCcPicTpg);
							}
						}
						
						if(StringUtils.isNotBlank(emailCcEmailGroupTpk)) {
							if (StringUtils.isBlank(emailCc)) {
								emailTo = emailCcEmailGroupTpk;
							} else {
								emailTo = emailTo.concat(",").concat(emailCcEmailGroupTpk);
							}
						}
						
						sendEmail.setEmailTo(emailTo);
						sendEmail.setEmailCc(emailCc);
						sendEmail.setSubject(emailSubject);
						sendEmail.setContent(emailContent);
						sendEmail.setAttachmentFileList(attachmentFileList);
						
						sendEmailVoByTargetDateMap.put(dataIrgKey, sendEmail);
					}	
				}
				
				if (sendEmailVoByTargetDateMap != null && !sendEmailVoByTargetDateMap.isEmpty()) {
					String result = "";
					
					for (SendEmailVo sendEmailVo : sendEmailVoByTargetDateMap.values()) {
						try {
							result = CallApiManager.sendEmailAPISchedullerAttachment(sendEmailVo.getEmailTo(), sendEmailVo.getEmailCc(), sendEmailVo.getSubject(),
									sendEmailVo.getContent(), InternalRegulationPenerbitanConstants.EMAIL_IRG_REMINDER, "true", sendEmailService, !sendEmailVo.getAttachmentFileList().isEmpty() ? sendEmailVo.getAttachmentFileList() : null);
						} catch (Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
					}
					
					if (attachmentFileTemp != null && !attachmentFileTemp.isEmpty()) {
						for (File file : attachmentFileTemp) {
							if (file.exists()) {
								file.delete();
							}
						}
					}
				}
				
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
			log.info("Finish Session Email IRG Penerbitan");
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
	
	public String replaceUserToLowerCaseAndFirstLetterUpper(String name) {
		String nameTemp = "";
		
		if (name.contains(" ")) {
			String[] split = name.split(" ");
			for (String string : split) {
				char firstChar = string.charAt(0);
				String afterChar = string.substring(1);
				
				if (org.apache.commons.lang3.StringUtils.isBlank(nameTemp)) {
					nameTemp = String.valueOf(firstChar).concat(afterChar.toLowerCase());
				} else {
					nameTemp = nameTemp.concat(" ").concat(String.valueOf(firstChar).concat(afterChar.toLowerCase()));
				}
			}
		} else {
			char firstChar = name.charAt(0);
			String afterChar = name.substring(1);
			
			nameTemp = String.valueOf(firstChar).concat(afterChar.toLowerCase());
		}
		
		return nameTemp;
	}
	
}
