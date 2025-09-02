package com.wo.module.rc.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class RC extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long rcId;
	private String regionCode;
	private String workingUnit;
	private String region;
	
	public Long getRcId() {
		return rcId;
	}
	public void setRcId(Long rcId) {
		this.rcId = rcId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getWorkingUnit() {
		return workingUnit;
	}
	public void setWorkingUnit(String workingUnit) {
		this.workingUnit = workingUnit;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	
}
