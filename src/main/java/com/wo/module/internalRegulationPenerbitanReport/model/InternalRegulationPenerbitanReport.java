package com.wo.module.internalRegulationPenerbitanReport.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class InternalRegulationPenerbitanReport extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6991465320848660763L;

	private Long irgId;

	private String referenceNo;
	private String irgTitle;
	private String regulationNo;
	private String directorateTpg;
	private String regulationTypeCode;
	private String regulationTypeName;
	private String picTpg;
	private String workUnitTpgCode;
	private String workUnitTpgName;
	private String regulationStatusCode;
	private String regulationStatusName;
	private String picIrg;

	public Long getIrgId() {
		return irgId;
	}

	public void setIrgId(Long irgId) {
		this.irgId = irgId;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getIrgTitle() {
		return irgTitle;
	}

	public void setIrgTitle(String irgTitle) {
		this.irgTitle = irgTitle;
	}

	public String getRegulationNo() {
		return regulationNo;
	}

	public void setRegulationNo(String regulationNo) {
		this.regulationNo = regulationNo;
	}

	public String getDirectorateTpg() {
		return directorateTpg;
	}

	public void setDirectorateTpg(String directorateTpg) {
		this.directorateTpg = directorateTpg;
	}

	public String getRegulationTypeCode() {
		return regulationTypeCode;
	}

	public void setRegulationTypeCode(String regulationTypeCode) {
		this.regulationTypeCode = regulationTypeCode;
	}

	public String getRegulationTypeName() {
		return regulationTypeName;
	}

	public void setRegulationTypeName(String regulationTypeName) {
		this.regulationTypeName = regulationTypeName;
	}

	public String getPicTpg() {
		return picTpg;
	}

	public void setPicTpg(String picTpg) {
		this.picTpg = picTpg;
	}

	public String getWorkUnitTpgCode() {
		return workUnitTpgCode;
	}

	public void setWorkUnitTpgCode(String workUnitTpgCode) {
		this.workUnitTpgCode = workUnitTpgCode;
	}

	public String getWorkUnitTpgName() {
		return workUnitTpgName;
	}

	public void setWorkUnitTpgName(String workUnitTpgName) {
		this.workUnitTpgName = workUnitTpgName;
	}

	public String getRegulationStatusCode() {
		return regulationStatusCode;
	}

	public void setRegulationStatusCode(String regulationStatusCode) {
		this.regulationStatusCode = regulationStatusCode;
	}

	public String getRegulationStatusName() {
		return regulationStatusName;
	}

	public void setRegulationStatusName(String regulationStatusName) {
		this.regulationStatusName = regulationStatusName;
	}

	public String getPicIrg() {
		return picIrg;
	}

	public void setPicIrg(String picIrg) {
		this.picIrg = picIrg;
	}

}