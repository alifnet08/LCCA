package com.wo.module.internalRegulationFE.vo;

import java.io.Serializable;

public class InternalRegulationFEVO  implements Serializable {
		
	private static final long serialVersionUID = 1031961290076567131L;

	private Long regulationId;
	
	private String docNo;
	private String nameIn;
	private String expiredDate;
	private String effectiveDate;
	private String status;
	
	private Integer isAccess;
	
	public Long getRegulationId() {
		return regulationId;
	}
	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}
	public String getDocNo() {
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	public String getNameIn() {
		return nameIn;
	}
	public void setNameIn(String nameIn) {
		this.nameIn = nameIn;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getIsAccess() {
		return isAccess;
	}
	public void setIsAccess(Integer isAccess) {
		this.isAccess = isAccess;
	}
	
	
}
