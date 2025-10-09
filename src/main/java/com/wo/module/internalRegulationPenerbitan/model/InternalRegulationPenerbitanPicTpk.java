package com.wo.module.internalRegulationPenerbitan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.user.model.User;

public class InternalRegulationPenerbitanPicTpk extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7588624441221153853L;
	
	private Long irgPicId;
	private Long divisionId;
	private Long fileSize;
	
	private User user1;
	private User user2;
	private User user3;

	private Date targetDate;
	private Date oldTargetDate;
	
	private String reviewApprovalFlag;
	private String attachmentFile;
	private String fileId;
	private String note;
	private String divisionName;
	
	private Integer sequence;
	
	private Boolean isEditableTemp;
	private boolean checkFlag;
	
	private InternalRegulationPenerbitan irg;
	
	private List<UploadedFileWO> uploadedFilesDocument;
	private List<UploadedFileWO> deletedFiles;
	
	private List<InternalRegulationPenerbitanPicTpkEmail> internalRegulationPenerbitanPicTpkEmailList = new ArrayList<>();

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

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
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

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}

	public String getReviewApprovalFlag() {
		return reviewApprovalFlag;
	}

	public void setReviewApprovalFlag(String reviewApprovalFlag) {
		this.reviewApprovalFlag = reviewApprovalFlag;
	}

	public String getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public InternalRegulationPenerbitan getIrg() {
		return irg;
	}

	public void setIrg(InternalRegulationPenerbitan irg) {
		this.irg = irg;
	}

	public boolean getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<InternalRegulationPenerbitanPicTpkEmail> getInternalRegulationPenerbitanPicTpkEmailList() {
		return internalRegulationPenerbitanPicTpkEmailList;
	}

	public void setInternalRegulationPenerbitanPicTpkEmailList(List<InternalRegulationPenerbitanPicTpkEmail> internalRegulationPenerbitanPicTpkEmailList) {
		this.internalRegulationPenerbitanPicTpkEmailList = internalRegulationPenerbitanPicTpkEmailList;
	}

}