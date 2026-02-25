package com.wo.module.formulirRegulationFE.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Logger;

public class FormulirRegulationFEMainBean implements Serializable{
	
	private static final long serialVersionUID = 5575279337804073467L;

	static Logger logger = Logger.getLogger(FormulirRegulationFEMainBean.class);
	
	private String showPage;
	
	@ManagedProperty(value = "#{formulirRegulationFEBean}")
	private FormulirRegulationFEBean formulirRegulationFEBean;
	
	@ManagedProperty(value = "#{formulirRegulationFEViewBean}")
	private FormulirRegulationFEViewBean formulirRegulationFEViewBean;
	
	@PostConstruct
	public void init() {
		showPage = "SEARCH";
	}
	
	public void modeSearch() {
		showPage = "SEARCH";
	}
	
	public void modeView() {
		showPage = "VIEW";
	}

	public String getShowPage() {
		return showPage;
	}

	public void setShowPage(String showPage) {
		this.showPage = showPage;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		FormulirRegulationFEMainBean.logger = logger;
	}

	public FormulirRegulationFEBean getFormulirRegulationFEBean() {
		return formulirRegulationFEBean;
	}

	public void setFormulirRegulationFEBean(FormulirRegulationFEBean formulirRegulationFEBean) {
		this.formulirRegulationFEBean = formulirRegulationFEBean;
	}

	public FormulirRegulationFEViewBean getFormulirRegulationFEViewBean() {
		return formulirRegulationFEViewBean;
	}

	public void setFormulirRegulationFEViewBean(FormulirRegulationFEViewBean formulirRegulationFEViewBean) {
		this.formulirRegulationFEViewBean = formulirRegulationFEViewBean;
	}
	
}
