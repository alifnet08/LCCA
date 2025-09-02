package com.wo.module.report.reportRegulationInternalHistory.vo;

import java.io.Serializable;

public class ReportRegulationInternalHistoryDiagramVo implements Serializable{

	private static final long serialVersionUID = -7660193624954300235L;
	
	private String pageName;
	private Long pageCount;
	
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public Long getPageCount() {
		return pageCount;
	}
	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
