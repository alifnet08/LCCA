package com.wo.module.qaFE.vo;

import java.io.Serializable;
import java.util.Date;

public class QAFEVo implements Serializable{

	private static final long serialVersionUID = 3309555879859618280L;
	
	private Long qaId;
	private String qTitle;
	private String sender;
	private Date qDate;
	private String qDateStr;
	private String question;
	private String categoryType;
	private String categoryTypeCode;
	private String categoryTypeIn;
	private String categoryTypeEn;
	private String status;
	private String statusCode;
	private String statusIn;
	private String statusEn;
	private Integer isAdmin;
	private String ticketNo;
	private String divisionName;
	
	private String answer;
	private String dataType;
	private String lastUpdateBy;
	private Date lastUpdateDate;
	
	public Long getQaId() {
		return qaId;
	}
	public void setQaId(Long qaId) {
		this.qaId = qaId;
	}
	public String getqTitle() {
		return qTitle;
	}
	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getqDate() {
		return qDate;
	}
	public void setqDate(Date qDate) {
		this.qDate = qDate;
	}
	public String getqDateStr() {
		return qDateStr;
	}
	public void setqDateStr(String qDateStr) {
		this.qDateStr = qDateStr;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getCategoryTypeCode() {
		return categoryTypeCode;
	}
	public void setCategoryTypeCode(String categoryTypeCode) {
		this.categoryTypeCode = categoryTypeCode;
	}
	public String getCategoryTypeIn() {
		return categoryTypeIn;
	}
	public void setCategoryTypeIn(String categoryTypeIn) {
		this.categoryTypeIn = categoryTypeIn;
	}
	public String getCategoryTypeEn() {
		return categoryTypeEn;
	}
	public void setCategoryTypeEn(String categoryTypeEn) {
		this.categoryTypeEn = categoryTypeEn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusIn() {
		return statusIn;
	}
	public void setStatusIn(String statusIn) {
		this.statusIn = statusIn;
	}
	public String getStatusEn() {
		return statusEn;
	}
	public void setStatusEn(String statusEn) {
		this.statusEn = statusEn;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
}
