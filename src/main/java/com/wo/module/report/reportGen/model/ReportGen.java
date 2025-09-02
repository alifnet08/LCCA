package com.wo.module.report.reportGen.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.model.BaseEntity;

public class ReportGen extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4626668178261861203L;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ReportGen.class);

	private Long reportGenId;
	private String reportGenNik;
	private String reportGenEmployeeName;
	private String reportGenReportName;
	private String reportGenReportFileName;
	private String reportGenStatus;
	private String reportGenStatusMsg;
	private String reportGenFileId;
	private Long reportGenFileSize;
	private String reportGenFile;

	public String getReportGenNik() {
		return reportGenNik;
	}

	public void setReportGenNik(String reportGenNik) {
		this.reportGenNik = reportGenNik;
	}

	public String getReportGenEmployeeName() {
		return reportGenEmployeeName;
	}

	public void setReportGenEmployeeName(String reportGenEmployeeName) {
		this.reportGenEmployeeName = reportGenEmployeeName;
	}

	public String getReportGenReportName() {
		return reportGenReportName;
	}

	public void setReportGenReportName(String reportGenReportName) {
		this.reportGenReportName = reportGenReportName;
	}

	public String getReportGenStatus() {
		return reportGenStatus;
	}

	public void setReportGenStatus(String reportGenStatus) {
		this.reportGenStatus = reportGenStatus;
	}

	public String getReportGenStatusMsg() {
		return reportGenStatusMsg;
	}

	public void setReportGenStatusMsg(String reportGenStatusMsg) {
		this.reportGenStatusMsg = reportGenStatusMsg;
	}

	public boolean getStatusIsCompleted() {
		return StringUtils.equals(CommonConstants.REPORT_GEN_STATUS_COMPLETE_SUCCESS, reportGenStatus);
	}

	public String getReportGenFileId() {
		return reportGenFileId;
	}

	public void setReportGenFileId(String reportGenFileId) {
		this.reportGenFileId = reportGenFileId;
	}

	public Long getReportGenFileSize() {
		return reportGenFileSize;
	}

	public void setReportGenFileSize(Long reportGenFileSize) {
		this.reportGenFileSize = reportGenFileSize;
	}

	public String getReportGenFile() {
		return reportGenFile;
	}

	public void setReportGenFile(String reportGenFile) {
		this.reportGenFile = reportGenFile;
	}

	public Long getReportGenId() {
		return reportGenId;
	}

	public void setReportGenId(Long reportGenId) {
		this.reportGenId = reportGenId;
	}

	public String getReportGenReportFileName() {
		return reportGenReportFileName;
	}

	public void setReportGenReportFileName(String reportGenReportFileName) {
		this.reportGenReportFileName = reportGenReportFileName;
	}

}
