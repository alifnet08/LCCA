package com.wo.module.cpsaView.vo;

import java.io.Serializable;
public class CpsaPicViewVo implements Serializable {

	private static final long serialVersionUID = 8734141169534362778L;
	
	private Long cpsaPicId;
	private Long cpsaId;
	private CpsaViewVO cpsa;
	private Long divisionId;

	private String branchName;
	private String divisionName;		
	private String cpsaStatus;
	private String cpsaStatusName;	
	private String userName1;
	private String userName2;
	private String userName3;
	private String picAdmin;
	
	public CpsaPicViewVo() {}


	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}


	public Long getCpsaPicId() {
		return cpsaPicId;
	}
	public void setCpsaPicId(Long cpsaPicId) {
		this.cpsaPicId = cpsaPicId;
	}


	public Long getCpsaId() {
		return cpsaId;
	}


	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}


	public CpsaViewVO getCpsa() {
		return cpsa;
	}


	public void setCpsa(CpsaViewVO cpsa) {
		this.cpsa = cpsa;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public String getDivisionName() {
		return divisionName;
	}


	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}


	public String getCpsaStatus() {
		return cpsaStatus;
	}


	public void setCpsaStatus(String cpsaStatus) {
		this.cpsaStatus = cpsaStatus;
	}


	public String getCpsaStatusName() {
		return cpsaStatusName;
	}


	public void setCpsaStatusName(String cpsaStatusName) {
		this.cpsaStatusName = cpsaStatusName;
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


	public String getPicAdmin() {
		return picAdmin;
	}


	public void setPicAdmin(String picAdmin) {
		this.picAdmin = picAdmin;
	}

	
	
}