package com.wo.module.report.reportComplianceDocumentRekap.model;

import java.util.Locale;

import javax.faces.context.FacesContext;

public class ReportComplianceDocumentRekap {

	private String tipeDokumenIn;
	private String tipeDokumenEn;
	private Integer total;

	// helper
	private String namaTipeDokumen;

	public String getTipeDokumenIn() {
		return tipeDokumenIn;
	}

	public void setTipeDokumenIn(String tipeDokumenIn) {
		this.tipeDokumenIn = tipeDokumenIn;
	}

	public String getTipeDokumenEn() {
		return tipeDokumenEn;
	}

	public void setTipeDokumenEn(String tipeDokumenEn) {
		this.tipeDokumenEn = tipeDokumenEn;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@SuppressWarnings("static-access")
	public String getNamaTipeDokumen() {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (locale != null && locale.equals(locale.ENGLISH)) {
			namaTipeDokumen = tipeDokumenEn;
		} else {
			namaTipeDokumen = tipeDokumenIn;
		}
		
		return namaTipeDokumen;
	}

	public void setNamaTipeDokumen(String namaTipeDokumen) {
		this.namaTipeDokumen = namaTipeDokumen;
	}
}
