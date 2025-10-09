package com.wo.module.engine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.wo.module.common.config.ApplicationContextProvider;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.DateUtil;
import com.wo.module.engine.service.AutoAbsoluteInternalRegulationService;
import com.wo.module.engine.vo.AutoAbsoluteInternalRegulationVO;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.model.RegulationTrackRecordMst;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.externalRegulation.service.RegulationTrackRecordMstService;
import com.wo.module.externalRegulation.service.RegulationTrackRecordService;
import com.wo.module.externalRegulationApproval.model.RegulationApproval;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class AutoAbsoluteInternalRegulationController implements Job {

	private static Logger log = Logger.getLogger(AutoAbsoluteInternalRegulationController.class);
	private static AutoAbsoluteInternalRegulationController autoAbsoluteInternalRegulationController;
	
	private final String functionId = "autoAbsolute";
	private final String userId = "SYSTEM";
	
	private LogService logService;
	private AutoAbsoluteInternalRegulationService autoAbsoluteInternalRegulationService;
	private RegulationService regulationService;
	private RegulationMstService regulationMstService;
	private UserService userService;
	private ParameterDetailService parameterDetailService;
	private RegulationTrackRecordService regulationTrackRecordService;
	private RegulationTrackRecordMstService regulationTrackRecordMstService;
	
	public AutoAbsoluteInternalRegulationController() {}
	
	public static synchronized AutoAbsoluteInternalRegulationController getInstance() {
		if (autoAbsoluteInternalRegulationController == null) autoAbsoluteInternalRegulationController = new AutoAbsoluteInternalRegulationController();
		
		return autoAbsoluteInternalRegulationController;
	}

	private class Wrapper {
		public Object ref;

		public Wrapper(Object ref) {
			this.ref = ref;
		}
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Start Session Auto Absolete - Internal Regulation");
		
		try {
			initInjection(context);
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId, ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
			logH.setLogDetailList(new ArrayList<>());
			
			String location = "Create Log Header";
			String msg = "";
			Wrapper seq = new Wrapper(Long.valueOf(0));
			boolean error = false;
			
			try {
				logService.save(logH);
			} catch (Exception e) {
				msg = "Saving Log fail because " + e.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Update Auto Absolete Internal Regulation - Peraturan Sementara";
			try {
				List<AutoAbsoluteInternalRegulationVO> result = autoAbsoluteInternalRegulationService.getDataAutoAbsoluteInternalRegulation();
				
 				for (AutoAbsoluteInternalRegulationVO data : result) {
					Regulation dataTmpRegulation = regulationService.findById(data.getRegulationId());
					RegulationMst dataMstRegulation = regulationMstService.findById(data.getRegulationId());
					RegulationTrackRecord newTrackRecord = new RegulationTrackRecord();
					User user = userService.findById(data.getUserId());
					
					if (dataTmpRegulation != null) {
						dataTmpRegulation.setStatus(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE);
						
						RegulationApproval newRegulationApproval = new RegulationApproval();
						newRegulationApproval.setRegulation(dataTmpRegulation);
						newRegulationApproval.setUser(user);
						newRegulationApproval.setApprovalDate(new Date());
						newRegulationApproval.setApprovalStatus(parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED));
						newRegulationApproval.setCreatedBy(user.getNik());
						newRegulationApproval.setCreationDate(new Timestamp(new Date().getTime()));
						newRegulationApproval.setDelId(0l);
						newRegulationApproval.setEnabledFlag(Constants.CONSTANT_YES);
						
						if (dataTmpRegulation.getRegulationApprovals() == null) dataTmpRegulation.setRegulationApprovals(new ArrayList<>());
						dataTmpRegulation.getRegulationApprovals().add(newRegulationApproval);
						
						try {
							regulationService.update(dataTmpRegulation);							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						newTrackRecord.setRegulation(dataTmpRegulation);
						newTrackRecord.setTrackCode(ParameterDetail.PARAM_DET_CODE_RECORD_REVOKE);
						newTrackRecord.setRegulationLinkId(dataTmpRegulation.getRegulationId());
						newTrackRecord.setRegulationLinkName(dataTmpRegulation.getDocumentNo());
						newTrackRecord.setInActive(true);
						newTrackRecord.setInActiveFlag(Constants.CONSTANT_YES);
						newTrackRecord.setCreatedBy(user.getNik());
						newTrackRecord.setCreationDate(new Timestamp(new Date().getTime()));
						newTrackRecord.setDelId(0l);
						newTrackRecord.setEnabledFlag(Constants.CONSTANT_YES);
						
						try {
							regulationTrackRecordService.save(newTrackRecord);							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if (dataMstRegulation != null) {
						dataMstRegulation.setStatus(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE);
						try {
							regulationMstService.update(dataMstRegulation);							
						} catch (Exception e) {
						}
						
						RegulationTrackRecordMst newTrackRecordMst = new RegulationTrackRecordMst();
						newTrackRecordMst.setRegulationTrackRecordId(newTrackRecord.getRegulationTrackRecordId());
						newTrackRecordMst.setRegulationMst(dataMstRegulation);
						newTrackRecordMst.setTrackCode(ParameterDetail.PARAM_DET_CODE_RECORD_REVOKE);
						newTrackRecordMst.setRegulationLinkId(dataMstRegulation.getRegulationId());
						newTrackRecordMst.setRegulationLinkName(dataMstRegulation.getDocumentNo());
						newTrackRecordMst.setInActive(true);
						newTrackRecordMst.setInActiveFlag(Constants.CONSTANT_YES);
						newTrackRecordMst.setCreatedBy(user.getNik());
						newTrackRecordMst.setCreationDate(new Timestamp(new Date().getTime()));
						newTrackRecordMst.setDelId(0l);
						newTrackRecordMst.setEnabledFlag(Constants.CONSTANT_YES);
						
						try {
							regulationTrackRecordMstService.save(newTrackRecordMst);							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = location + " fail because " + e.getMessage();
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
			log.info("Finish Session Auto Absolete - Internal Regulation");
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
		autoAbsoluteInternalRegulationService = appContext.getBean("autoAbsoluteInternalRegulationService", AutoAbsoluteInternalRegulationService.class);
		regulationService = appContext.getBean("regulationService", RegulationService.class);
		regulationMstService = appContext.getBean("regulationMstService", RegulationMstService.class);
		userService = appContext.getBean("userService", UserService.class);
		parameterDetailService = appContext.getBean("parameterDetailService", ParameterDetailService.class);
		regulationTrackRecordService = appContext.getBean("regulationTrackRecordService", RegulationTrackRecordService.class);
		regulationTrackRecordMstService = appContext.getBean("regulationTrackRecordMstService", RegulationTrackRecordMstService.class);
		
//		logService = ApplicationContextProvider.getApplicationContext().getBean("logService", LogService.class);
//		autoAbsoluteInternalRegulationService = ApplicationContextProvider.getApplicationContext().getBean("autoAbsoluteInternalRegulationService", AutoAbsoluteInternalRegulationService.class);
//		regulationService = ApplicationContextProvider.getApplicationContext().getBean("regulationService", RegulationService.class);
//		regulationMstService = ApplicationContextProvider.getApplicationContext().getBean("regulationMstService", RegulationMstService.class);
//		userService = ApplicationContextProvider.getApplicationContext().getBean("userService", UserService.class);
//		parameterDetailService = ApplicationContextProvider.getApplicationContext().getBean("parameterDetailService", ParameterDetailService.class);
//		regulationTrackRecordService = ApplicationContextProvider.getApplicationContext().getBean("regulationTrackRecordService", RegulationTrackRecordService.class);
//		regulationTrackRecordMstService = ApplicationContextProvider.getApplicationContext().getBean("regulationTrackRecordMstService", RegulationTrackRecordMstService.class);
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		AutoAbsoluteInternalRegulationController.log = log;
	}

	public static AutoAbsoluteInternalRegulationController getAutoAbsoluteInternalRegulationController() {
		return autoAbsoluteInternalRegulationController;
	}

	public static void setAutoAbsoluteInternalRegulationController(
			AutoAbsoluteInternalRegulationController autoAbsoluteInternalRegulationController) {
		AutoAbsoluteInternalRegulationController.autoAbsoluteInternalRegulationController = autoAbsoluteInternalRegulationController;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public AutoAbsoluteInternalRegulationService getAutoAbsoluteInternalRegulationService() {
		return autoAbsoluteInternalRegulationService;
	}

	public void setAutoAbsoluteInternalRegulationService(
			AutoAbsoluteInternalRegulationService autoAbsoluteInternalRegulationService) {
		this.autoAbsoluteInternalRegulationService = autoAbsoluteInternalRegulationService;
	}

	public String getFunctionId() {
		return functionId;
	}

	public String getUserId() {
		return userId;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public RegulationTrackRecordService getRegulationTrackRecordService() {
		return regulationTrackRecordService;
	}

	public void setRegulationTrackRecordService(RegulationTrackRecordService regulationTrackRecordService) {
		this.regulationTrackRecordService = regulationTrackRecordService;
	}

}
