package com.wo.module.log.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.lov.bean.FacesUtil;

public class LogDetailBean extends CommonBean implements Serializable {
	
	private static final long serialVersionUID = 4414064818397606678L;

	static Logger logger = Logger.getLogger(LogDetailBean.class);

	private LogService logService;
	private String home = "logHeader.jsf"; 
	private FacesUtil facesUtil;
	private LogHeader log;

	@PostConstruct
	public void init() {
		super.init();
		try {
			if (StringUtils.isNotBlank(facesUtil.retrieveRequestParam("id"))) {
				String id = facesUtil.retrieveRequestParam("id");

				log = logService.findById(Long.parseLong(id));

				/*
				 * if (log.getDetails() != null && log.getDetails().size() > 0) { for (int i =
				 * 0; i < log.getDetails().size(); i++) { LogDetail det = (LogDetail)
				 * log.getDetails().get(i); SfSystem sys = sfSystemService.findSfSystem(
				 * SfSystemConstants.MSG_TYPE, det.getMessageType());
				 * det.setMessageTypeName(sys.getSystemValue()); log.getDetails().set(i, det); }
				 * }
				 */

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void back() {
		try {
			getFacesUtil().redirect("/pages/log/log.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public LogHeader getLog() {
		return log;
	}

	public void setLog(LogHeader log) {
		this.log = log;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}
}