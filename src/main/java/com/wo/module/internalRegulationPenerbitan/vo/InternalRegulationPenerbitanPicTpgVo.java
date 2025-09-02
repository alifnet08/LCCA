package com.wo.module.internalRegulationPenerbitan.vo;

import java.io.Serializable;
import java.util.Date;

public class InternalRegulationPenerbitanPicTpgVo implements Serializable {

	private static final long serialVersionUID = -5653814163344934252L;
	
	private Long irgId;
	private Long irgPicId;
	private Long divisionId;
	private Long user1Id;
	private Long user2Id;
	private Long user3Id;
	private Long delId;

	private Date targetDate;
	private Date oldTargetDate;

	private String user1Name;
	private String user2Name;
	private String user3Name;
	private String note;
	private String divisionName;
	
	private String user1Email;
	private String user2Email;
	private String user3Email;

	private Integer sequence;

	private Boolean isEditableTemp;

	public Long getIrgId() {
		return irgId;
	}

	public void setIrgId(Long irgId) {
		this.irgId = irgId;
	}

	public Long getIrgPicId() {
		return irgPicId;
	}

	public void setIrgPicId(Long irgPicId) {
		this.irgPicId = irgPicId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(Long user1Id) {
		this.user1Id = user1Id;
	}

	public Long getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(Long user2Id) {
		this.user2Id = user2Id;
	}

	public Long getUser3Id() {
		return user3Id;
	}

	public void setUser3Id(Long user3Id) {
		this.user3Id = user3Id;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}

	public String getUser1Name() {
		return user1Name;
	}

	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}

	public String getUser2Name() {
		return user2Name;
	}

	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}

	public String getUser3Name() {
		return user3Name;
	}

	public void setUser3Name(String user3Name) {
		this.user3Name = user3Name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}

	public String getUser1Email() {
		return user1Email;
	}

	public void setUser1Email(String user1Email) {
		this.user1Email = user1Email;
	}

	public String getUser2Email() {
		return user2Email;
	}

	public void setUser2Email(String user2Email) {
		this.user2Email = user2Email;
	}

	public String getUser3Email() {
		return user3Email;
	}

	public void setUser3Email(String user3Email) {
		this.user3Email = user3Email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}