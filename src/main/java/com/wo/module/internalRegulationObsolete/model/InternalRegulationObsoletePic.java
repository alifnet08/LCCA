package com.wo.module.internalRegulationObsolete.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.division.model.Division;
import com.wo.module.user.model.User;

public class InternalRegulationObsoletePic extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -119288709394024232L;

	private Long internalRegObsoletePicKonvId;
	private InternalRegulationObsolete internalRegulasiObsolete;
	private Long divisionId;
	private Division division;
	private User user1;
	private User user2;
	private User user3;
	private Date targetDate;
	private String notes;
	private String sendEmailFlag;

	private Integer sequence;
	private Boolean isEditable;
	
	private List<InternalRegulationObsoleteEmailTmp> regulationObsoleteEmailTmps;
	
	public Long getInternalRegObsoletePicKonvId() {
		return internalRegObsoletePicKonvId;
	}

	public void setInternalRegObsoletePicKonvId(Long internalRegObsoletePicKonvId) {
		this.internalRegObsoletePicKonvId = internalRegObsoletePicKonvId;
	}

	public InternalRegulationObsolete getInternalRegulasiObsolete() {
		return internalRegulasiObsolete;
	}

	public void setInternalRegulasiObsolete(InternalRegulationObsolete internalRegulasiObsolete) {
		this.internalRegulasiObsolete = internalRegulasiObsolete;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSendEmailFlag() {
		return sendEmailFlag;
	}

	public void setSendEmailFlag(String sendEmailFlag) {
		this.sendEmailFlag = sendEmailFlag;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public List<InternalRegulationObsoleteEmailTmp> getRegulationObsoleteEmailTmps() {
		return regulationObsoleteEmailTmps;
	}

	public void setRegulationObsoleteEmailTmps(List<InternalRegulationObsoleteEmailTmp> regulationObsoleteEmailTmps) {
		this.regulationObsoleteEmailTmps = regulationObsoleteEmailTmps;
	}

}