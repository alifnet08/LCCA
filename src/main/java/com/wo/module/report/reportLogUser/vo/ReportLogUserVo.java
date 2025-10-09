package com.wo.module.report.reportLogUser.vo;

import java.io.Serializable;
import java.util.Date;

public class ReportLogUserVo implements Serializable{

	private static final long serialVersionUID = -2992255594834697418L;
	
	private String title;
	private String data;
	private String createdBy;
	private String createdName;
	private Date createdDate;
	private String createdPosition;
	private String createdBranch;
	private String lastStatus;
	private String lastUpdateBy;
	private String lastUpdateName;
	private Date lastUpdateDate;
	private String lastUpdatePosition;
	private String lastUpdateBranch;
	
	// helper
	private String createdDateStr;
	private String lastUpdateDateStr;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedName() {
		return createdName;
	}
	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedPosition() {
		return createdPosition;
	}
	public void setCreatedPosition(String createdPosition) {
		this.createdPosition = createdPosition;
	}
	public String getCreatedBranch() {
		return createdBranch;
	}
	public void setCreatedBranch(String createdBranch) {
		this.createdBranch = createdBranch;
	}
	public String getLastStatus() {
		return lastStatus;
	}
	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public String getLastUpdateName() {
		return lastUpdateName;
	}
	public void setLastUpdateName(String lastUpdateName) {
		this.lastUpdateName = lastUpdateName;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatePosition() {
		return lastUpdatePosition;
	}
	public void setLastUpdatePosition(String lastUpdatePosition) {
		this.lastUpdatePosition = lastUpdatePosition;
	}
	public String getLastUpdateBranch() {
		return lastUpdateBranch;
	}
	public void setLastUpdateBranch(String lastUpdateBranch) {
		this.lastUpdateBranch = lastUpdateBranch;
	}
	public String getCreatedDateStr() {
		return createdDateStr;
	}
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}
	public String getLastUpdateDateStr() {
		return lastUpdateDateStr;
	}
	public void setLastUpdateDateStr(String lastUpdateDateStr) {
		this.lastUpdateDateStr = lastUpdateDateStr;
	}

}
