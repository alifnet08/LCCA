package com.wo.module.engine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedProperty;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.wo.module.common.config.ApplicationContextProvider;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.engine.vo.SendEmailIRGObsoleteVO;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.internalRegulationObsolete.constant.InternalRegulationObsoleteConstants;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;

public class SendEmailIRGObsoleteController implements Job {

	private static Logger log = Logger.getLogger(SendEmailController.class);
	private static SendEmailIRGObsoleteController sendEmailIRGObsoleteController;
	private final String functionId = "email";
	private final String userId = "SYSTEM";
	
	private LogService logService;
	private SendEmailService sendEmailService;
	
	Locale localeIndo = new Locale("in","ID");
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd MMMMM yyyy",localeIndo);
	
	@ManagedProperty("#{holidayService}")
	private HolidayService holidayService;
	
	public SendEmailIRGObsoleteController() {
		
	}
	
	public static synchronized SendEmailIRGObsoleteController getInstance() {
		if(sendEmailIRGObsoleteController == null) {
			sendEmailIRGObsoleteController = new SendEmailIRGObsoleteController();
		}
		return sendEmailIRGObsoleteController;
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
		log.info("Start Session Email - IRG Obsolete");
		
		try {
			initInjection(context);
			
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId, 
					ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
			logH.setLogDetailList(new ArrayList<LogDetail>());
			String location = "Create Log Header";
			String msg = "";
			Wrapper seq = new Wrapper(Long.valueOf(0));
			boolean error = false;
			
			try {
				logService.save(logH);
			}catch(Exception ex) {
				msg = "Saving Log fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Update Email Date";
			try {
				sendEmailService.procedureUpdateEmailDate();
			}catch(Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Send Email IRG to PIC";
			List<SendEmailIRGObsoleteVO> listData = null;
			try {
				
				listData = sendEmailService.getListDataIRGObsolete();
				
				for(SendEmailIRGObsoleteVO vo : listData) {
					
					String emailTo = "", 
						emailCc = "", emailCc1 = "", 
						emailCc2 = "", emailCc3 = "",
						emailSubject = "", emailContent="";
					String emailDateFull = "N/A";
					
					if(vo.getEmailDate() != null) {
						emailDateFull = sdfFull.format(vo.getEmailDate());
					}
					
					if(vo.getSlaType().equals(InternalRegulationObsoleteConstants.REMINDER_H_PLUS_0)) {
						emailSubject = sendEmailService.getEmailSubject("EMAIL_IRG_OBSOLETE_H_PLUS_0");
						emailContent = sendEmailService.getEmailContent("EMAIL_IRG_OBSOLETE_H_PLUS_0");
					}else if(vo.getSlaType().equals(InternalRegulationObsoleteConstants.REMINDER_H_MINUS_30)) {
						emailSubject = sendEmailService.getEmailSubject("EMAIL_IRG_OBSOLETE_H_MINUS_30");
						emailContent = sendEmailService.getEmailContent("EMAIL_IRG_OBSOLETE_H_MINUS_30");
					}
					
					emailSubject = emailSubject.replaceAll("judul_obsolete", vo.getObsoleteTitle());
					emailSubject = emailSubject.replaceAll("nama_pic_irg_1", replaceUserToLowerCaseAndFirstLetterUpper(vo.getPicIrgName1()));
					emailSubject = emailSubject.replaceAll("nama_pic_irg_2", StringUtils.isNotBlank(vo.getPicIrgName2()) ? replaceUserToLowerCaseAndFirstLetterUpper(vo.getPicIrgName2()) : "N/A");
					emailSubject = emailSubject.replaceAll("tipe_obsolete", vo.getRegObsoleteTypeStr());
					emailSubject = emailSubject.replaceAll("info_obsolete", vo.getObsoleteTitle());
					
					emailSubject = emailSubject.replaceAll("nama_pic_konv_1", replaceUserToLowerCaseAndFirstLetterUpper(vo.getUserName1()));
					emailSubject = emailSubject.replaceAll("nama_pic_konv_2", StringUtils.isNotBlank(vo.getUserName2()) ? replaceUserToLowerCaseAndFirstLetterUpper(vo.getUserName2()) : "N/A");
					emailSubject = emailSubject.replaceAll("nama_pic_konv_3", StringUtils.isNotBlank(vo.getUserName3()) ? replaceUserToLowerCaseAndFirstLetterUpper(vo.getUserName3()) : "N/A");
					emailSubject = emailSubject.replaceAll("tanggal_konversi",emailDateFull);
					
					emailContent = emailContent.replaceAll("judul_obsolete", vo.getObsoleteTitle());
					emailContent = emailContent.replaceAll("nama_pic_irg_1", replaceUserToLowerCaseAndFirstLetterUpper(vo.getPicIrgName1()));
					emailContent = emailContent.replaceAll("nama_pic_irg_2", StringUtils.isNotBlank(vo.getPicIrgName2()) ? replaceUserToLowerCaseAndFirstLetterUpper(vo.getPicIrgName2()) : "N/A");
					emailContent = emailContent.replaceAll("tipe_obsolete", vo.getRegObsoleteTypeStr());
					emailContent = emailContent.replaceAll("info_obsolete", vo.getObsoleteTitle());
					
					emailContent = emailContent.replaceAll("nama_pic_konv_1", replaceUserToLowerCaseAndFirstLetterUpper(vo.getUserName1()));
					emailContent = emailContent.replaceAll("nama_pic_konv_2", StringUtils.isNotBlank(vo.getUserName2()) ? replaceUserToLowerCaseAndFirstLetterUpper(vo.getUserName2()) : "N/A");
					emailContent = emailContent.replaceAll("nama_pic_konv_3", StringUtils.isNotBlank(vo.getUserName3()) ? replaceUserToLowerCaseAndFirstLetterUpper(vo.getUserName3()) : "N/A");
					emailContent = emailContent.replaceAll("tanggal_konversi",emailDateFull);
					
					
					
//					emailContent = emailContent.replaceAll("email_pic_2", vo.getUserEmail2());
//					emailContent = emailContent.replaceAll("email_pic_1", vo.getUserEmail1());
//					emailContent = emailContent.replaceAll("email_pic_3", vo.getUserEmail3());
//					emailContent = emailContent.replaceAll("email_irg_1", vo.getPicIrgEmail1());
//					emailContent = emailContent.replaceAll("email_irg_2", vo.getPicIrgEmail2());
					
					emailTo = vo.getUserEmail2() != null ? vo.getUserEmail2() : null;
					emailCc = vo.getUserEmail1() != null ? vo.getUserEmail1() : null;
					emailCc1 = vo.getUserEmail3() != null ? vo.getUserEmail3() : null;
					emailCc2 = vo.getPicIrgEmail1() != null ? vo.getPicIrgEmail1() : null;
					emailCc3 = vo.getPicIrgEmail2() != null ? vo.getPicIrgEmail2() : null;
					
					
					if(StringUtils.isNotEmpty(emailCc1)) {
						emailCc = emailCc.concat(emailCc1);
					}
					if(StringUtils.isNotEmpty(emailCc2)) {
						emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc2) : emailCc.concat(emailCc2);
					}
					if(StringUtils.isNotEmpty(emailCc3)) {
						emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc3) : emailCc.concat(emailCc3);
					}	
					
					//unused because only used for log purposes
					String result = "";
					if(StringUtils.isNotBlank(emailTo)) {
						try {
							// change my email to emailTo and empty string to emailCc
							
							if(vo.getSlaType().equals(InternalRegulationObsoleteConstants.REMINDER_H_PLUS_0)) {
								result = CallApiManager.sendEmailAPIScheduller(emailTo, emailCc, emailSubject, emailContent, 
										"EMAIL_IRG_OBSOLETE_H_PLUS_0", "true", sendEmailService);
							}else if(vo.getSlaType().equals(InternalRegulationObsoleteConstants.REMINDER_H_MINUS_30)) {
								result = CallApiManager.sendEmailAPIScheduller(emailTo, emailCc, emailSubject, emailContent, 
										"EMAIL_IRG_OBSOLETE_H_MINUS_30", "true", sendEmailService);
							}
							
						}catch(Exception ex) {
							result = EmailConstant.EMAIL_STATUS_ERROR;
							msg = location + " fail because " + ex.getMessage();
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							error = true;
						}
					}
						
				}
				
			}catch(Exception ex) {
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
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			log.info("Finish Session Email - IRG Obsolete");
		}
	}

	private void addLogDetail(LogHeader logH, Wrapper seq, String type, String location, String msg) {
		if (seq == null) {
			seq = new Wrapper(Long.valueOf(1));
		} else {
			seq.ref = ((Long) seq.ref).longValue() + 1;
		}

		LogDetail logD = new LogDetail(logH, ((Long) seq.ref).longValue(), type, location, msg);
		logH.getLogDetailList().add(logD);
	}

	private void initInjection(JobExecutionContext context) {
		ApplicationContext appContext = (ApplicationContext) context.getMergedJobDataMap().get("applicationContextKey");
		logService = appContext.getBean("logService",LogService.class);
		sendEmailService = appContext.getBean("sendEmailService", SendEmailService.class);
		
//		logService = ApplicationContextProvider.getApplicationContext().getBean("logService", LogService.class);
//		sendEmailService = ApplicationContextProvider.getApplicationContext().getBean("sendEmailService", SendEmailService.class);
	}
	
	public String replaceUserToLowerCaseAndFirstLetterUpper(String name) {
		String nameTemp = "";
		
		if (name.contains(" ")) {
			String[] split = name.split(" ");
			for (String string : split) {
				char firstChar = string.charAt(0);
				String afterChar = string.substring(1);
				
				if (StringUtils.isBlank(nameTemp)) {
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
	
	//GETTER SETTER
	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	
}
