package com.wo.module.common.bean;

import org.apache.log4j.Logger;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.RunnableFacesUtil;

public class CommonReportRunnableBean extends CommonBean {
	static Logger logger = Logger.getLogger(CommonReportRunnableBean.class);
	private static final long serialVersionUID = 7086153935034130524L;

	public FacesUtil facesUtil;
	public RunnableFacesUtil runnableFacesUtil;
	
	public void init() {
		super.init();
		runnableFacesUtil = new RunnableFacesUtil(facesUtil.retrieveDefaultLocale());
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public RunnableFacesUtil getRunnableFacesUtil() {
		return runnableFacesUtil;
	}

	public void setRunnableFacesUtil(RunnableFacesUtil runnableFacesUtil) {
		this.runnableFacesUtil = runnableFacesUtil;
	}
}
