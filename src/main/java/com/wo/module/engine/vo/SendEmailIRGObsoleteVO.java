package com.wo.module.engine.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class SendEmailIRGObsoleteVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921240632660925605L;
	
	private String userEmail1;
	private String userName1;

	private String userEmail2;
	private String userName2;
	
	private String userEmail3;
	private String userName3;
	
	private String picIrgEmail1;
	private String picIrgName1;
	
	private String picIrgEmail2;
	private String picIrgName2;
	
	private String obsoleteTitle;
	private String regObsoleteTypeStr;
	private String obsoleteInfo;
	
	private String emailDateStr;
	private Timestamp emailDate;
	
	private String slaType;
	private Long sla;
	
	//GETTER SETTER

	public String getUserEmail1() {
		return userEmail1;
	}

	public void setUserEmail1(String userEmail1) {
		this.userEmail1 = userEmail1;
	}

	public String getUserEmail2() {
		return userEmail2;
	}

	public void setUserEmail2(String userEmail2) {
		this.userEmail2 = userEmail2;
	}

	public String getUserEmail3() {
		return userEmail3;
	}

	public void setUserEmail3(String userEmail3) {
		this.userEmail3 = userEmail3;
	}

	public String getObsoleteTitle() {
		return obsoleteTitle;
	}

	public void setObsoleteTitle(String obsoleteTitle) {
		this.obsoleteTitle = obsoleteTitle;
	}

	public String getRegObsoleteTypeStr() {
		return regObsoleteTypeStr;
	}

	public void setRegObsoleteTypeStr(String regObsoleteTypeStr) {
		this.regObsoleteTypeStr = regObsoleteTypeStr;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public Long getSla() {
		return sla;
	}

	public void setSla(Long sla) {
		this.sla = sla;
	}

	public String getPicIrgEmail1() {
		return picIrgEmail1;
	}

	public void setPicIrgEmail1(String picIrgEmail1) {
		this.picIrgEmail1 = picIrgEmail1;
	}

	public String getPicIrgEmail2() {
		return picIrgEmail2;
	}

	public void setPicIrgEmail2(String picIrgEmail2) {
		this.picIrgEmail2 = picIrgEmail2;
	}

	public String getUserName1() {
		return userName1;
	}

	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}

	public String getUserName2() {
		return userName2;
	}

	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}

	public String getUserName3() {
		return userName3;
	}

	public void setUserName3(String userName3) {
		this.userName3 = userName3;
	}

	public String getPicIrgName1() {
		return picIrgName1;
	}

	public void setPicIrgName1(String picIrgName1) {
		this.picIrgName1 = picIrgName1;
	}

	public String getPicIrgName2() {
		return picIrgName2;
	}

	public void setPicIrgName2(String picIrgName2) {
		this.picIrgName2 = picIrgName2;
	}

	public String getObsoleteInfo() {
		return obsoleteInfo;
	}

	public void setObsoleteInfo(String obsoleteInfo) {
		this.obsoleteInfo = obsoleteInfo;
	}

	public String getEmailDateStr() {
		return emailDateStr;
	}

	public void setEmailDateStr(String emailDateStr) {
		this.emailDateStr = emailDateStr;
	}

	public Timestamp getEmailDate() {
		return emailDate;
	}

	public void setEmailDate(Timestamp emailDate) {
		this.emailDate = emailDate;
	}

	
	
}
