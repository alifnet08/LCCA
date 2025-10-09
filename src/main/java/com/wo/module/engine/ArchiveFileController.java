package com.wo.module.engine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.wo.module.common.config.ApplicationContextProvider;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.engine.ReadXlsController.Wrapper;
import com.wo.module.engine.service.ArchiveFileService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;

public class ArchiveFileController implements Job {

	@SuppressWarnings("unused")
	private static final String OSCAR_IN_ = "oscar_";
	private static Logger log = Logger.getLogger(ArchiveFileController.class);
	private static ArchiveFileController archiveFileController;

	private final String functionId = "Email";
	private final String userId = "SYSTEM";
	
	private ApplicationContext appContext;
	private LogService logService;
	private ArchiveFileService archiveFileService;
	
	
	public static synchronized ArchiveFileController getInstance() {
		if(archiveFileController == null) {
			archiveFileController = new ArchiveFileController();
		}
		return archiveFileController;
	}
	
	public ArchiveFileController() {

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
//		SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.INPUT_DATE_FORMAT);
		try {
			initInjection(context);
			
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId,
					ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			logH.setLogDetailList(new ArrayList<LogDetail>());
			String location = "Create Log Header Archive File";
			String msg = "";
			String attachmentPath = archiveFileService.getSystemProperty(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FILE_PATH);
			
			Wrapper seq = new Wrapper(new Long(0));
			boolean error = false;
			try {
				getLogService().save(logH);
			} catch (Exception ex) {
				msg = "Saving Log fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			String archivePath = null;
			try {
				archivePath = archiveFileService.getSystemProperty(ParameterDetail.PARAM_DET_ARCHIVE_FILE_PATH);
			} catch (Exception ex) {
				msg = "Saving Log fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			String daysArchive = null;
			try {
				daysArchive = archiveFileService.getSystemProperty(ParameterDetail.PARAM_DET_DAYS_FILE_ARCHIVE_TIME);
			} catch (Exception ex) {
				msg = "Saving Log fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			if(!error) {
				try {
					FileUtil fileUtil = FileUtil.getInstance();
					
					List<String> listFile = archiveFileService.getFileExpiredFromDate(Integer.parseInt(daysArchive));
//					listFile.add("812efd87-cd5e-4153-afc3-491e2e212e13.docx");
					for(int x=0; x < listFile.size(); x++) {
						String pathStr = listFile.get(x);
						if(Files.exists(Paths.get(archivePath, pathStr))) {
							log.info("============== already exist ================");
							msg = "Saving Log fail because "+Paths.get(archivePath, pathStr).toString()+" already exist";
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							continue;
						} else if(!Files.exists(Paths.get(attachmentPath, pathStr))) {
							log.info("============== not exist ================");
							msg = "Saving Log fail because "+Paths.get(archivePath, pathStr).toString()+" not exist";
							addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
							continue;
						} else {
							log.info("============== process ================");
							String newPath = fileUtil.copyFile(Paths.get(attachmentPath, pathStr).toString(), 
									archivePath, pathStr, true);
							log.info("Success move file from: "+Paths.get(attachmentPath, pathStr).toString()
									+"\n to: "+newPath);
						}

					}
				
				} catch (Exception ex) {
					msg = "Saving Log fail because " + ex.getMessage();
					addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
					error = true;
				}
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
			log.info("Finish Session Arsip File");
		}
		
	}
	
	private void initInjection(JobExecutionContext context) {
		ApplicationContext appContext = (ApplicationContext) context.getMergedJobDataMap().get("applicationContextKey");
		logService = appContext.getBean("logService", LogService.class);
		archiveFileService = appContext.getBean("archiveFileService", ArchiveFileService.class);
		
//		logService = ApplicationContextProvider.getApplicationContext().getBean("logService", LogService.class);
//		archiveFileService = ApplicationContextProvider.getApplicationContext().getBean("archiveFileService", ArchiveFileService.class);
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

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		ArchiveFileController.log = log;
	}

	public static ArchiveFileController getArchiveFileController() {
		return archiveFileController;
	}

	public static void setArchiveFileController(ArchiveFileController archiveFileController) {
		ArchiveFileController.archiveFileController = archiveFileController;
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

	public ArchiveFileService getArchiveFileService() {
		return archiveFileService;
	}

	public void setArchiveFileService(ArchiveFileService archiveFileService) {
		this.archiveFileService = archiveFileService;
	}

	public static String getOscarIn() {
		return OSCAR_IN_;
	}

	public String getFunctionId() {
		return functionId;
	}

	public String getUserId() {
		return userId;
	}
	
	

}
