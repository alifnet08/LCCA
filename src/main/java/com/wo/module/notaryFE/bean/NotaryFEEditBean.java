package com.wo.module.notaryFE.bean;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.FileUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.service.NotaryService;

public class NotaryFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4439556059549152604L;

	private Notary notary;

	private NotaryService notaryService;

	public FacesUtil facesUtil;

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
			facesUtil.setSessionAttribute("FIRST_NOTARY_FE", testFirst);
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
		Long idLong = Long.parseLong(editId);
		notary = notaryService.findById(idLong);
	}
	
	public void cancel() {
		try {
			if (facesUtil.retrieveRequestParam("first") != null) {
				facesUtil.setSessionAttribute("BACK_SESSION", true);
			}
			facesUtil.redirect("/pages/notaryFE/notaryFE.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Notary getNotary() {
		return notary;
	}

	public void setNotary(Notary notary) {
		this.notary = notary;
	}

	public NotaryService getNotaryService() {
		return notaryService;
	}

	public void setNotaryService(NotaryService notaryService) {
		this.notaryService = notaryService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
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
