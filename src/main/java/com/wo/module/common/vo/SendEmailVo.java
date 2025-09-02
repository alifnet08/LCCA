package com.wo.module.common.vo;

import java.io.File;
import java.util.List;

public class SendEmailVo {

	private String emailTo;
	private String emailCc;
	private String subject;
	private String content;
	
	private List<File> attachmentFileList;
	
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<File> getAttachmentFileList() {
		return attachmentFileList;
	}
	public void setAttachmentFileList(List<File> attachmentFileList) {
		this.attachmentFileList = attachmentFileList;
	}
	
}
