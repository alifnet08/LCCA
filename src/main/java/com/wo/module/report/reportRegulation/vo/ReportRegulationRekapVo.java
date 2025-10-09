package com.wo.module.report.reportRegulation.vo;

import java.io.Serializable;

public class ReportRegulationRekapVo implements Serializable{

	private static final long serialVersionUID = -7660193624954300235L;
	
	private String documentTypeIn;
	private Long totalRegulation;
	
	public String getDocumentTypeIn() {
		return documentTypeIn;
	}
	public void setDocumentTypeIn(String documentTypeIn) {
		this.documentTypeIn = documentTypeIn;
	}
	public Long getTotalRegulation() {
		return totalRegulation;
	}
	public void setTotalRegulation(Long totalRegulation) {
		this.totalRegulation = totalRegulation;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
