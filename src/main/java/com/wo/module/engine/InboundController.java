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
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.util.DateUtil;
import com.wo.module.engine.service.OscarJobOracleService;
import com.wo.module.engine.service.OscarJobService;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class InboundController implements Job {

	@SuppressWarnings("unused")
	private static final String OSCAR_IN_ = "oscar_";
	private static Logger log = Logger.getLogger(InboundController.class);
	private static InboundController inboundController;
//	private LogService logService = LogServiceJobImpl.getInstance();
//	private OscarJobService oscarJobService = OscarJobServiceImpl.getInstance();
	private final String functionId = "Inbound";
	private final String userId = "SYSTEM";
	
	private ApplicationContext appContext;
	private LogService logService;
	private OscarJobService oscarJobService;
	private OscarJobOracleService oscarJobOracleService;

	public InboundController() {
	}

	public static synchronized InboundController getInstance() {
		if (inboundController == null) {
			inboundController = new InboundController();
		}
		return inboundController;
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
		log.info("Start Session Inbound");

		try {
			initInjection(context);
			
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId,
					ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateStr = sdf.format(date);
			
			logH.setLogDetailList(new ArrayList<LogDetail>());
			String location = "Create Log Header";
			String msg = "";
			Wrapper seq = new Wrapper(new Long(0));
			boolean cont = true;
			boolean error = false;
			try {
				getLogService().save(logH);
			} catch (Exception ex) {
				msg = "Saving Log fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Retrieve Table Query from mysql";
			String tableQuery = null;
			try {
				tableQuery = oscarJobService.getSystemProperty(ParameterDetail.PARAM_DET_CODE_INBOUND_USER_VIEW);
				System.out.println(location + " success");
			} catch (Exception ex) {
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
			}
			
			location = "Retrieve User from oracle";
			List<User> users = null;
			try {
				users = oscarJobOracleService.getUserFromOracle(tableQuery);
//				Gson gson = new Gson();
//				gson.toJson(users, new FileWriter("D://users.json"));
				//String jsonUser = gson.toJson(users);
				
				//oscarJobService.execInboundCommon("wo_sp_update_user", jsonUser);
				System.out.println(location + " success");
			} catch (Exception ex) {
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
			}
			
			location = "Update user data";
			try {
				oscarJobService.updateUser(users);
				System.out.println(location + " success");
			} catch (Exception ex) {
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
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
			log.info("Finish Session Inbound");
		}
	}
	
	private void initInjection(JobExecutionContext context) {
		ApplicationContext appContext = (ApplicationContext) context.getMergedJobDataMap().get("applicationContextKey");
		logService = appContext.getBean("logService", LogService.class);
		oscarJobService = appContext.getBean("oscarJobService", OscarJobService.class);
		oscarJobOracleService = appContext.getBean("oscarJobOracleService", OscarJobOracleService.class);
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

	public OscarJobService getOscarJobService() {
		return oscarJobService;
	}

	public void setOscarJobService(OscarJobService oscarJobService) {
		this.oscarJobService = oscarJobService;
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

	public OscarJobOracleService getOscarJobOracleService() {
		return oscarJobOracleService;
	}

	public void setOscarJobOracleService(OscarJobOracleService oscarJobOracleService) {
		this.oscarJobOracleService = oscarJobOracleService;
	}
}
