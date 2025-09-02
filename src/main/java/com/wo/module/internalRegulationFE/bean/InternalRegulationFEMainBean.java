package com.wo.module.internalRegulationFE.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Logger;

import com.wo.module.internalRegulationFE.constant.InternalRegulationFEConstants;

public class InternalRegulationFEMainBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7374956616696759959L;
	static Logger logger = Logger.getLogger(InternalRegulationFEMainBean.class);
	
	private String showPage;
	
	@ManagedProperty(value = "#{internalFESearchBean}")
	private InternalRegulationFEBean internalFESearchBean;
	
	@ManagedProperty(value = "#{internalFEViewBean}")
	private InternalRegulationFEViewBean internalFEViewBean;
	
	@PostConstruct
	public void init() {
		showPage = "SEARCH";
	}
	
	public void modeSearch() {
		showPage = "SEARCH";
	}
	
	public void modeView() {
		showPage = "VIEW";
		System.out.println("modeView changed to \"VIEW\"");
//		return InternalRegulationFEConstants.NAVIGATE_EDIT;
	}

	public String getShowPage() {
		return showPage;
	}

	public void setShowPage(String showPage) {
		this.showPage = showPage;
	}

	public InternalRegulationFEBean getInternalFESearchBean() {
		return internalFESearchBean;
	}

	public void setInternalFESearchBean(InternalRegulationFEBean internalFESearchBean) {
		this.internalFESearchBean = internalFESearchBean;
	}

	public InternalRegulationFEViewBean getInternalFEViewBean() {
		return internalFEViewBean;
	}

	public void setInternalFEViewBean(InternalRegulationFEViewBean internalFEViewBean) {
		this.internalFEViewBean = internalFEViewBean;
	}
}
