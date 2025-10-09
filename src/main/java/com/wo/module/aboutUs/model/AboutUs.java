package com.wo.module.aboutUs.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class AboutUs extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long aboutUsId;
	private String ourName;
	private String ourJob;
	private String email;
	private String photoFile;
	private String fileId;
	private Long fileSize;
	private AboutUs puk;
	private String ext;
	
	
	public Long getAboutUsId() {
		return aboutUsId;
	}
	public void setAboutUsId(Long aboutUsId) {
		this.aboutUsId = aboutUsId;
	}
	public String getOurName() {
		return ourName;
	}
	public void setOurName(String ourName) {
		this.ourName = ourName;
	}
	public String getOurJob() {
		return ourJob;
	}
	public void setOurJob(String ourJob) {
		this.ourJob = ourJob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhotoFile() {
		return photoFile;
	}
	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public AboutUs getPuk() {
		return puk;
	}
	public void setPuk(AboutUs puk) {
		this.puk = puk;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	
	
	
}
