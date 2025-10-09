package com.wo.module.report.reportLogUser.vo;

import java.io.Serializable;

public class ReportLogUserDiagramVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
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
