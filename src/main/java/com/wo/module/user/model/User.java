package com.wo.module.user.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class User extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;
	private Long userId;
	// private Employee employee;

	private String password;
	private String nik;
	private String name;
	private String email;
	private Long jobId;
	private String jobName;
	private Long positionId;
	private String positionName;
	private String pukNik;
	private String branchCode;
	
	private Long divisionId;
	private String divisionName;
	private Long responsibilityId;
	private String status;
	
	private String subBranch;
	private String subArea;
	private String branch;
	private String region;
	private String directorate;
	
	private String nikAtasan;
	
	
	
	///
	
	private String roleName;
	
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getResponsibilityId() {
		return responsibilityId;
	}

	public void setResponsibilityId(Long responsibilityId) {
		this.responsibilityId = responsibilityId;
	}

	public String getPukNik() {
		return pukNik;
	}

	public void setPukNik(String pukNik) {
		this.pukNik = pukNik;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getSubBranch() {
		return subBranch;
	}

	public void setSubBranch(String subBranch) {
		this.subBranch = subBranch;
	}

	public String getSubArea() {
		return subArea;
	}

	public void setSubArea(String subArea) {
		this.subArea = subArea;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}


	public String getDirectorate() {
		return directorate;
	}

	public void setDirectorate(String directorate) {
		this.directorate = directorate;
	}

	public String getNikAtasan() {
		return nikAtasan;
	}

	public void setNikAtasan(String nikAtasan) {
		this.nikAtasan = nikAtasan;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}