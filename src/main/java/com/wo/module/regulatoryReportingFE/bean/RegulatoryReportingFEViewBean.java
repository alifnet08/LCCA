package com.wo.module.regulatoryReportingFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.regulatoryReportingFE.constant.RegulatoryReportingFEConstant;
import com.wo.module.regulatoryReportingFE.service.RegulatoryReportingFEService;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.service.TrcRmdPicFollowupService;
import com.wo.module.user.service.UserService;

public class RegulatoryReportingFEViewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = -310952600422764519L;
	private static final Logger logger = Logger.getLogger(RegulatoryReportingFEViewBean.class);
	private static final String NAVIGATE_BACK = RegulatoryReportingFEConstant.NAVIGATE_REGULATORY_REPORTING_FE;

	private UserService userService;
	private RegulatoryReportingFEService regulatoryReportingFEService;
	private TrcRmdPicFollowupService trcRmdPicFollowupService;
	private TrcRmdPicFollowup trcRmdPicFollowup;
	private EmailTemplateService emailTemplateService;

	private TrcRmd trcRmd;

	private String viewId;
	private String actionMode;

	private FacesUtil facesUtil;
	private FileUtil fileUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));

	private Integer testFirst;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first")!=null?facesUtil.retrieveRequestParam("first"):"0");
		facesUtil.setSessionAttribute("FIRST_REG_REPORT_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}

	private void checkNewOrEdit() {
		this.viewId = facesUtil.retrieveRequestParam("viewId");
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			viewId = Constants.decryptString(token);
		}
		if (StringUtils.isBlank(viewId)) {
			// do nothing
		} else {
			handleView(viewId);
		}
	}

	private void handleView(String viewId) {
		Long viewIdLong = Long.parseLong(viewId);
		// trcRmd = regulatoryReportingFEService.findById(viewIdLong);
		trcRmdPicFollowup = trcRmdPicFollowupService.findById(viewIdLong);
		trcRmd = regulatoryReportingFEService.findById(trcRmdPicFollowup.getTrcRmd().getRmdId());
		System.out.println();
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/regulatoryReportingFE/regulatoryReportingFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public RegulatoryReportingFEService getRegulatoryReportingFEService() {
		return regulatoryReportingFEService;
	}

	public void setRegulatoryReportingFEService(RegulatoryReportingFEService regulatoryReportingFEService) {
		this.regulatoryReportingFEService = regulatoryReportingFEService;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public TrcRmdPicFollowup getTrcRmdPicFollowup() {
		return trcRmdPicFollowup;
	}

	public void setTrcRmdPicFollowup(TrcRmdPicFollowup trcRmdPicFollowup) {
		this.trcRmdPicFollowup = trcRmdPicFollowup;
	}

	public TrcRmdPicFollowupService getTrcRmdPicFollowupService() {
		return trcRmdPicFollowupService;
	}

	public void setTrcRmdPicFollowupService(TrcRmdPicFollowupService trcRmdPicFollowupService) {
		this.trcRmdPicFollowupService = trcRmdPicFollowupService;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

}
