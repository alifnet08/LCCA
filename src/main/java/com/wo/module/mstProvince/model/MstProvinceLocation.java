package com.wo.module.mstProvince.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class MstProvinceLocation extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4013809617357199865L;
	
	private Long provinceLocationId;
	private String province;
	private Integer pinTop;
	private Integer pinLeft;
	
	
	public Long getProvinceLocationId() {
		return provinceLocationId;
	}
	public void setProvinceLocationId(Long provinceLocationId) {
		this.provinceLocationId = provinceLocationId;
	}
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Integer getPinTop() {
		return pinTop;
	}
	public void setPinTop(Integer pinTop) {
		this.pinTop = pinTop;
	}
	public Integer getPinLeft() {
		return pinLeft;
	}
	public void setPinLeft(Integer pinLeft) {
		this.pinLeft = pinLeft;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}