package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TmpReffDocument implements Serializable {
		
	private static final long serialVersionUID = 1760200818705612652L;
	
	private Long correspondenceAttachmentId;
	
	private String attachmentFile;
	private String fileId;
	
	
	public TmpReffDocument() {
		super();
	}

	public Long getCorrespondenceAttachmentId() {
		return correspondenceAttachmentId;
	}

	public void setCorrespondenceAttachmentId(Long correspondenceAttachmentId) {
		this.correspondenceAttachmentId = correspondenceAttachmentId;
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
		
}
