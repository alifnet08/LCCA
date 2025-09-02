package com.wo.module.internalRegulationPenerbitan.vo;

import java.io.Serializable;
import java.util.Date;

public class InternalRegulationPenerbitanPicIrgVo implements Serializable {

	private static final long serialVersionUID = 5523623590485932916L;
	
	private Long irgId;
	private Long irgPicId;
	private Long divisionId;	
	private Long user1Id;
	private Long user2Id;
	private Long user3Id;
	private Long delId;
	
	private Date targetDate;
	private Date oldTargetDate;

	private String userId1Name;
	private String userId2Name;
	private String userId3Name;
	private String note;
	
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

	public String getUserId1Name() {
		return userId1Name;
	}

	public void setUserId1Name(String userId1Name) {
		this.userId1Name = userId1Name;
	}

	public String getUserId2Name() {
		return userId2Name;
	}

	public void setUserId2Name(String userId2Name) {
		this.userId2Name = userId2Name;
	}

	public String getUserId3Name() {
		return userId3Name;
	}

	public void setUserId3Name(String userId3Name) {
		this.userId3Name = userId3Name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
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

}