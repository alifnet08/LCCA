package com.wo.module.log.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wo.module.common.util.DateUtil;

public class LogDetail implements Serializable {
	
	private static final long serialVersionUID = 3518447374856993939L;
	private LogHeader logHeader;
	private Long seqNo;
	private String msgType;
	private String msgLocation;
	private String msgMessage;
	private Timestamp msgDate;

	public static final String MSG_TYPE_WARNING = "WRN";
	public static final String MSG_TYPE_ERROR = "ERR";
	public static final String MSG_TYPE_INFO = "INF";

	public LogDetail() {
		
	}
	
	public LogDetail(LogHeader logHeader, Long seqNo, String msgType, String msgLocation, String msgMessage) {
		this.logHeader = logHeader;
		this.seqNo = seqNo;
		this.msgType = msgType;
		this.msgLocation = msgLocation;
		this.msgMessage = msgMessage;
		this.msgDate = DateUtil.currentSqlTimestamp();
	}

	public LogHeader getLogHeader() {
		return logHeader;
	}

	public void setLogHeader(LogHeader logHeader) {
		this.logHeader = logHeader;
	}

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgLocation() {
		return msgLocation;
	}

	public void setMsgLocation(String msgLocation) {
		this.msgLocation = msgLocation;
	}

	public String getMsgMessage() {
		return msgMessage;
	}

	public void setMsgMessage(String msgMessage) {
		this.msgMessage = msgMessage;
	}

	public Timestamp getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(Timestamp msgDate) {
		this.msgDate = msgDate;
	}
}