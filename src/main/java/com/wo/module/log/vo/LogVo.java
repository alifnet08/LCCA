package com.wo.module.log.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.parameter.model.ParameterDetail;

public class LogVo implements Serializable {
	
	private static final long serialVersionUID = 2591170413535445920L;
	private Long processId;
	private Timestamp processDate;
	private String functionId;
	private ParameterDetail functionParam;
	private String processSts;
	private ParameterDetail processStsParam;
	private String userId;
	private Timestamp endDate;
	private String remarks;
	//private List<LogDetail> logDetailList;

	public LogVo() {
		
	}
	
	public LogVo(Timestamp processDate, String functionId, String processSts, String userId) {
		this.processDate = processDate;
		this.functionId = functionId;
		this.processSts = processSts;
		this.userId = userId;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Timestamp getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Timestamp processDate) {
		this.processDate = processDate;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getProcessSts() {
		return processSts;
	}

	public void setProcessSts(String processSts) {
		this.processSts = processSts;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

//	public List<LogDetail> getLogDetailList() {
//		return logDetailList;
//	}
//
//	public void setLogDetailList(List<LogDetail> logDetailList) {
//		this.logDetailList = logDetailList;
//	}

	public ParameterDetail getFunctionParam() {
		return functionParam;
	}

	public void setFunctionParam(ParameterDetail functionParam) {
		this.functionParam = functionParam;
	}

	public ParameterDetail getProcessStsParam() {
		return processStsParam;
	}

	public void setProcessStsParam(ParameterDetail processStsParam) {
		this.processStsParam = processStsParam;
	}
}