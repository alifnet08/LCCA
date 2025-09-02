package com.wo.module.qa.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.service.QABackEndService;

public class QABackEndEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -5667900130007486090L;
	private static final Logger logger = Logger.getLogger(QABackEndEditBean.class);
	private static final String NAVIGATE_BACK = QAConstants.NAVIGATE_SEARCH_BACK_END;
	
	private QABackEndService qaBackEndService;
	
	private QA qa;
	
	private String actionMode;
	private String editId;
	
	private FacesUtil facesUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		editId = facesUtil.retrieveRequestParam("editId");
		
		handleEdit(editId);
	}
	
	private void handleEdit(String editId) {
		Long idLong = Long.parseLong(editId);
		
		qa = qaBackEndService.findById(idLong);
	}

	public void save() {
		try {
			if (qa.getQnaId() != null) {
				qaBackEndService.update(qa);
				
				facesUtil.redirect("/pages/qa/qaBackEnd.faces");
			}
		} catch (Exception ex) {
			facesUtil.addFacesMsg(
                    FacesMessage.SEVERITY_ERROR, 
                    null, 
                    "Operation Failed : " + ex.getMessage(), "");
		}
	}
	
    public void cancel() {
    	try {
			facesUtil.redirect("/pages/qa/qaBackEnd.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public QABackEndService getQaBackEndService() {
		return qaBackEndService;
	}

	public void setQaBackEndService(QABackEndService qaBackEndService) {
		this.qaBackEndService = qaBackEndService;
	}

	public QA getQa() {
		return qa;
	}

	public void setQa(QA qa) {
		this.qa = qa;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}
	
}
