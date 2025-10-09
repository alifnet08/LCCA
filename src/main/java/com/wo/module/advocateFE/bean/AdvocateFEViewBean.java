package com.wo.module.advocateFE.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.advocate.constant.AdvocateConstants;
import com.wo.module.advocate.model.Advocate;
import com.wo.module.advocate.model.AdvocateInfo;
import com.wo.module.advocate.model.AdvocateInfoTableModel;
import com.wo.module.advocate.service.AdvocateService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpFine.model.TmpFinePicCompliance;
import com.wo.module.tmpFine.model.TmpFinePicComplianceTableModel;

public class AdvocateFEViewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(AdvocateFEViewBean.class);

	private Advocate advocate;
	
	private AdvocateService advocateService;

	public FacesUtil facesUtil;
	
	private AdvocateInfoTableModel<AdvocateInfo> advocateInfoModel;
	
	private FileUtil fileUtil;

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
		if (facesUtil.retrieveRequestParam("first") != null) {
			testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first"));
			facesUtil.setSessionAttribute("FIRST_ADVOCATE_FE", testFirst);
			facesUtil.setSessionAttribute("BACK_SESSION", false);
		}
		handleEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	
	
	private void handleEdit() {
		String editId = facesUtil.retrieveRequestParam("CHOSEN_ID");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		/*if(StringUtils.isNotEmpty(editId)) {
			editId = Constants.decryptString(editId);
		}*/
		
		Long idLong = Long.parseLong(editId);
		advocate = advocateService.findById(idLong);
		
		advocateInfoModel = new AdvocateInfoTableModel<AdvocateInfo>(
				advocate.getAdvocateInfos());
		
	}

	public void cancel() {
		try {
			if (facesUtil.retrieveRequestParam("first") != null) {
				facesUtil.setSessionAttribute("BACK_SESSION", true);
			}
			facesUtil.redirect("/pages/advocateFE/advocateFE.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AdvocateFEViewBean.logger = logger;
	}

	public Advocate getAdvocate() {
		return advocate;
	}

	public void setAdvocate(Advocate advocate) {
		this.advocate = advocate;
	}

	public AdvocateService getAdvocateService() {
		return advocateService;
	}

	public void setAdvocateService(AdvocateService advocateService) {
		this.advocateService = advocateService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public AdvocateInfoTableModel<AdvocateInfo> getAdvocateInfoModel() {
		return advocateInfoModel;
	}

	public void setAdvocateInfoModel(AdvocateInfoTableModel<AdvocateInfo> advocateInfoModel) {
		this.advocateInfoModel = advocateInfoModel;
	}


	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}
}