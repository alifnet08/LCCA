package com.wo.module.user.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class Branch extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	private String branchCode;
	private String branchName;
	private String subBranchName;
	
	
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSubBranchName() {
		return subBranchName;
	}
	public void setSubBranchName(String subBranchName) {
		this.subBranchName = subBranchName;
	}
	
	

}