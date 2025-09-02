package com.wo.module.mstProvince.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class MstProvince extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4013809617357199865L;
	
	private Long provinceId;
	private String province;
	private String branchCode;
	
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}