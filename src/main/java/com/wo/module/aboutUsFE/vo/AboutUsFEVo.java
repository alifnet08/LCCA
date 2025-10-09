package com.wo.module.aboutUsFE.vo;

import java.io.Serializable;
import java.util.List;

public class AboutUsFEVo implements Serializable{

	private static final long serialVersionUID = -5729153097142480282L;

	private Long aboutUsId;
	private String ourName;
	private String ourJob;
	private String ext;
	private String photoFile;
	private Long pukId;
	private String email;
	private String fileId;
	private Long fileSize;
	
	// list child
	private List<AboutUsFEVo> child;
	
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
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getPhotoFile() {
		return photoFile;
	}
	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}
	public Long getPukId() {
		return pukId;
	}
	public void setPukId(Long pukId) {
		this.pukId = pukId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<AboutUsFEVo> getChild() {
		return child;
	}
	public void setChild(List<AboutUsFEVo> child) {
		this.child = child;
	}
	
}
