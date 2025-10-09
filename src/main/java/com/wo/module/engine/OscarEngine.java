package com.wo.module.engine;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.wo.module.common.config.ApplicationContextProvider;
import com.wo.module.engine.service.OscarJobService;
import com.wo.module.parameter.model.ParameterDetail;

public class OscarEngine extends Thread implements Serializable {

	private static final long serialVersionUID = 7653855659172864062L;
	static Logger logger = Logger.getLogger(OscarEngine.class);

//	private OscarJobService oscarJobService = OscarJobServiceImpl.getInstance();

	private OscarJobService oscarJobService;
	
	public OscarEngine() {
	}

	@SuppressWarnings("unused")
	public void run() {
		try {
			oscarJobService = ApplicationContextProvider.getApplicationContext().getBean("oscarJobService", OscarJobService.class);
			String waktuEksekusi = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_INBOUND_EXEC_TIME);

			if (StringUtils.isBlank(waktuEksekusi)) {
				throw new Exception(
						ParameterDetail.PARAM_DET_CODE_LOG_INBOUND_EXEC_TIME + " is not found in Parameter Detail");
			} else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(waktuEksekusi);
				} catch (ParseException ex) {
					throw new Exception(
							ParameterDetail.PARAM_DET_CODE_LOG_INBOUND_EXEC_TIME + " is not in HH:mm format");
				}
			}

			String jamEksekusi = waktuEksekusi.split(":")[0];
			String minutesEksekusi = waktuEksekusi.split(":")[1];

			JobDataMap myJobDataMap = new JobDataMap();
			myJobDataMap.put("applicationContextKey", ApplicationContextProvider.getApplicationContext());
			
			
			JobDetail jobIn = JobBuilder.newJob(ReadXlsController.class).withIdentity("importViewJob", "groupView")
					.setJobData(myJobDataMap)
					.build();

			Trigger triggerIn = TriggerBuilder.newTrigger().withIdentity("importViewTrigger", "groupView").withSchedule(
					CronScheduleBuilder.cronSchedule("0 " + minutesEksekusi + " " + jamEksekusi + " ? * SUN-FRI"))
					.build();
			
			String waktuEksekusiSendMail = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_EXEC_TIME);

			if (StringUtils.isBlank(waktuEksekusiSendMail)) {
				throw new Exception(
						ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_EXEC_TIME + " is not found in Parameter Detail");
			} else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(waktuEksekusiSendMail);
				} catch (ParseException ex) {
					throw new Exception(
							ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_EXEC_TIME + " is not in HH:mm format");
				}
			}

			String jamEksekusiSendMail = waktuEksekusiSendMail.split(":")[0];
			String minutesEksekusiSendMail = waktuEksekusiSendMail.split(":")[1];
			
			JobDetail jobSendEmail = JobBuilder.newJob(SendEmailController.class).withIdentity("importViewJob2", "groupView2")
					.setJobData(myJobDataMap)
					.build();

			Trigger triggerSendEmail = TriggerBuilder.newTrigger().withIdentity("importViewTrigger2", "groupView2").withSchedule(
					CronScheduleBuilder.cronSchedule("0 " + minutesEksekusiSendMail + " " + jamEksekusiSendMail + " ? * *"))
					.build();
			
		
			
			String waktuEksekusiSendMailQA = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_QA_EXEC_TIME);

			if (StringUtils.isBlank(waktuEksekusiSendMailQA)) {
				throw new Exception(
						ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_QA_EXEC_TIME + " is not found in Parameter Detail");
			} else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(waktuEksekusiSendMail);
				} catch (ParseException ex) {
					throw new Exception(
							ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_QA_EXEC_TIME + " is not in HH:mm format");
				}
			}

			String jamEksekusiSendMailQA = waktuEksekusiSendMailQA.split(":")[0];
			String minutesEksekusiSendMailQA = waktuEksekusiSendMailQA.split(":")[1];
			
			JobDetail jobSendEmailQA = JobBuilder.newJob(SendEmailQAController.class).withIdentity("importViewJob3", "groupView3")
					.setJobData(myJobDataMap)
					.build();

			Trigger triggerSendEmailQA = TriggerBuilder.newTrigger().withIdentity("importViewTrigger3", "groupView3").withSchedule(
					CronScheduleBuilder.cronSchedule("0 " + minutesEksekusiSendMailQA + " " + jamEksekusiSendMailQA + " ? * *"))
					.build();
			
			String waktuEksekusiSendMailQAEveryHour = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_QA_EVERY_HOUR_EXEC_TIME);
			
			JobDetail jobSendEmailQAEveryHour = JobBuilder.newJob(SendEmailQAEveryHourController.class).withIdentity("importViewJob4", "groupView4")
					.setJobData(myJobDataMap)
					.build();
			Trigger triggerSendEmailQAEveryHour = TriggerBuilder.newTrigger().withIdentity("importViewTrigger4", "groupView4").withSchedule(
					CronScheduleBuilder.cronSchedule("0 0 */"+waktuEksekusiSendMailQAEveryHour+" ? * *"))
					.build();// dibuat parameter
			
			String waktuEksekusiSendMailQAForCloseEveryHour = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_QA_EVERY_HOUR_EXEC_TIME);
			
			JobDetail jobSendEmailQAForCloseEveryHour = JobBuilder.newJob(SendEmailQAForCloseEveryHourController.class).withIdentity("importViewJob9", "groupView9")
					.setJobData(myJobDataMap)
					.build();
			Trigger triggerSendEmailQAForCloseEveryHour = TriggerBuilder.newTrigger().withIdentity("importViewTrigger9", "groupView9").withSchedule(
					CronScheduleBuilder.cronSchedule("0 0 */"+waktuEksekusiSendMailQAForCloseEveryHour+" ? * SUN-FRI"))
					.build();// dibuat parameter
					
			//CronScheduleBuilder.cronSchedule("0 0 * ? * *"))//tiap 1 jam
			//ARSIP FILE
			String waktuEksekusiArsipFile = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_LOG_ARCHIVE_FILE_EXEC_TIME);
			if (StringUtils.isBlank(waktuEksekusiArsipFile)) {
				throw new Exception(
						ParameterDetail.PARAM_DET_LOG_ARCHIVE_FILE_EXEC_TIME + " is not found in Parameter Detail");
			} else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(waktuEksekusiArsipFile);
				} catch (ParseException ex) {
					throw new Exception(
							ParameterDetail.PARAM_DET_LOG_ARCHIVE_FILE_EXEC_TIME + " is not in HH:mm format");
				}
			}
			
			String jamEksekusiArsipFile = waktuEksekusiArsipFile.split(":")[0];
			String minutesEksekusiArsipFile = waktuEksekusiArsipFile.split(":")[1];
//			String jamEksekusiArsipFile = "15";
//			String minutesEksekusiArsipFile = "30";
			
			JobDetail jobArsipFile = JobBuilder.newJob(ArchiveFileController.class).withIdentity("importViewJob8", "groupView8")
					.setJobData(myJobDataMap)
					.build();

			Trigger triggerArsipFile = TriggerBuilder.newTrigger().withIdentity("importViewTrigger8", "groupView8").withSchedule(
					CronScheduleBuilder.cronSchedule("0 " + minutesEksekusiArsipFile + " " + jamEksekusiArsipFile + " ? * *"))
					.build();		
			
			
			//IRG Obsolete Send Email Job and Trigger
			String executionTimeIRGObsoleteSendEmail = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_OBS_EXEC_TIME);
			
			if(StringUtils.isBlank(executionTimeIRGObsoleteSendEmail)) {
				throw new Exception(
						ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_OBS_EXEC_TIME + " is not found in Parameter Detail");
			}else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(executionTimeIRGObsoleteSendEmail);
				}catch(ParseException ex) {
					throw new Exception(
						ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_OBS_EXEC_TIME + " is not in HH:mm format");
				}
			}
			
			String executionHourIRGObsoleteSendEmail = executionTimeIRGObsoleteSendEmail.split(":")[0];
			String executionMinuteIRGObsoleteSendEmail = executionTimeIRGObsoleteSendEmail.split(":")[1];
			
			JobDetail jobSendEmailIRGObsolete = JobBuilder.newJob(SendEmailIRGObsoleteController.class)
					.withIdentity("importViewJob5","groupView5")
					.setJobData(myJobDataMap)
					.build();
			
			Trigger triggerSendEmailIRGObsolete = TriggerBuilder.newTrigger().withIdentity("importViewTrigger5","groupView5")
					.withSchedule(CronScheduleBuilder
							.cronSchedule("0 "+executionMinuteIRGObsoleteSendEmail+" "+executionHourIRGObsoleteSendEmail+" ? * *")).build();

			String waktuEksekusiSendMailIrgPenerbitan = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_PENERBITAN_EXEC_TIME);
			
			if (StringUtils.isBlank(waktuEksekusiSendMailIrgPenerbitan)) {
				throw new Exception(
						ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_PENERBITAN_EXEC_TIME + " is not found in Parameter Detail");
			} else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(waktuEksekusi);
				} catch (ParseException ex) {
					throw new Exception(
							ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_PENERBITAN_EXEC_TIME + " is not in HH:mm format");
				}
			}

			String jamEksekusiIrgPenerbitan = waktuEksekusiSendMailIrgPenerbitan.split(":")[0];
			String minutesEksekusiPenerbitan = waktuEksekusiSendMailIrgPenerbitan.split(":")[1];
			
			JobDetail jobSendEmailIrgPenerbitan = JobBuilder.newJob(SendEmailIrgPenerbitanController.class)
					.withIdentity("importViewJob6", "groupView6").setJobData(myJobDataMap).build();
			Trigger triggerSendEmailIrgPenerbitan = TriggerBuilder.newTrigger().withIdentity("importViewTrigger6", "groupView6")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 " + minutesEksekusiPenerbitan + " "+ jamEksekusiIrgPenerbitan + " ? * *")).build();
			
			String waktuEksekusiSendMailCpsa = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_CPSA_EXEC_TIME);
			
			if (StringUtils.isBlank(waktuEksekusiSendMailCpsa)) {
				throw new Exception(
						ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_CPSA_EXEC_TIME + " is not found in Parameter Detail");
			} else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(waktuEksekusiSendMailCpsa);
				} catch (ParseException ex) {
					throw new Exception(
							ParameterDetail.PARAM_DET_CODE_LOG_SEND_EMAIL_CPSA_EXEC_TIME + " is not in HH:mm format");
				}
			}

			String jamEksekusiCpsa = waktuEksekusiSendMailCpsa.split(":")[0];
			String minutesEksekusiCpsa = waktuEksekusiSendMailCpsa.split(":")[1];
			
			JobDetail jobSendEmailCpsa = JobBuilder.newJob(SendEmailCpsaController.class)
					.withIdentity("importViewJob7", "groupView7").setJobData(myJobDataMap).build();
			Trigger triggerSendEmailCpsa = TriggerBuilder.newTrigger().withIdentity("importViewTrigger7", "groupView7")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 " + minutesEksekusiCpsa + " "+ jamEksekusiCpsa + " ? * *")).build();
			
			// AUTO ABSOLUTE INTERNAL
			String waktuEksekusiAutoAbsoluteInternal = oscarJobService.getSystemProperty(ParameterDetail.PARAM_DET_CODE_LOG_AUTO_ABSOLUTE_INTERNAL);
			
			if (StringUtils.isBlank(waktuEksekusiAutoAbsoluteInternal)) {
				throw new Exception(ParameterDetail.PARAM_DET_CODE_LOG_AUTO_ABSOLUTE_INTERNAL + " is not found in Parameter Detail");
			} else {
				DateFormat formatter = new SimpleDateFormat("HH:mm");
				try {
					Date time = formatter.parse(waktuEksekusiAutoAbsoluteInternal);
				} catch (ParseException ex) {
					throw new Exception(ParameterDetail.PARAM_DET_CODE_LOG_AUTO_ABSOLUTE_INTERNAL + " is not in HH:mm format");
				}
			}
			
			String jamEksekusiAutoAbsoluteInternal = waktuEksekusiAutoAbsoluteInternal.split(":")[0];
			String minutesEksekusiAutoAbsoluteInternal = waktuEksekusiAutoAbsoluteInternal.split(":")[1];
			
			JobDetail jobAutoAbsoluteInternal = JobBuilder.newJob(AutoAbsoluteInternalRegulationController.class).
					withIdentity("importViewJob10", "groupView10").setJobData(myJobDataMap).build();
			Trigger triggerAutoAbsoluteInternal = TriggerBuilder.newTrigger().withIdentity("importViewTrigger10", "groupView10")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 " + minutesEksekusiAutoAbsoluteInternal + " "+ jamEksekusiAutoAbsoluteInternal + " ? * *"))
					.build();
			// AUTO ABSOLUTE INTERNAL
			
			// Tell quartz to schedule the job using our trigger
			Scheduler sched = new StdSchedulerFactory().getScheduler();
						
			sched.start();
			sched.scheduleJob(jobIn, triggerIn);
			sched.scheduleJob(jobAutoAbsoluteInternal, triggerAutoAbsoluteInternal);
			sched.scheduleJob(jobSendEmail, triggerSendEmail); 
			sched.scheduleJob(jobArsipFile, triggerArsipFile); 
			sched.scheduleJob(jobSendEmailQA, triggerSendEmailQA);
			sched.scheduleJob(jobSendEmailQAEveryHour, triggerSendEmailQAEveryHour);
			sched.scheduleJob(jobSendEmailQAForCloseEveryHour, triggerSendEmailQAForCloseEveryHour);
			sched.scheduleJob(jobSendEmailIRGObsolete, triggerSendEmailIRGObsolete);
			sched.scheduleJob(jobSendEmailIrgPenerbitan, triggerSendEmailIrgPenerbitan);			
			sched.scheduleJob(jobSendEmailCpsa, triggerSendEmailCpsa);

		} catch (Exception e) {
			// System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	public OscarJobService getOscarJobService() {
		return oscarJobService;
	}

	public void setOscarJobService(OscarJobService oscarJobService) {
		this.oscarJobService = oscarJobService;
	}
}
