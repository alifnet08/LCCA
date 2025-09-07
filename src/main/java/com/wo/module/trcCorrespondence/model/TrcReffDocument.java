package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;

public class TrcReffDocument implements Serializable {
		
	private static final long serialVersionUID = 1760200818705612652L;
	
	private Long correspondenceAttachmentId;
	
	private String attachmentFile;
	private String fileId;
	
	
	public TrcReffDocument() {
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
