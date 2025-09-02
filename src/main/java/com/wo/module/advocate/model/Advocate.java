package com.wo.module.advocate.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class Advocate extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long advocateId;
	private String region;
	private String branch;
	private String no;
	private String advocateName;
	private String partners;
	private String website;
	private String clasification;
	private String note;
	
	private List<AdvocateInfo> advocateInfos;
	private List<AdvocatePartners> advocatePartners;
	
	
	public Long getAdvocateId() {
		return advocateId;
	}
	public void setAdvocateId(Long advocateId) {
		this.advocateId = advocateId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	
	public String getAdvocateName() {
		return advocateName;
	}
	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}
	public String getPartners() {
		return partners;
	}
	public void setPartners(String partners) {
		this.partners = partners;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getClasification() {
		return clasification;
	}
	public void setClasification(String clasification) {
		this.clasification = clasification;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<AdvocateInfo> getAdvocateInfos() {
		return advocateInfos;
	}
	public void setAdvocateInfos(List<AdvocateInfo> advocateInfos) {
		this.advocateInfos = advocateInfos;
	}
	public List<AdvocatePartners> getAdvocatePartners() {
		return advocatePartners;
	}
	public void setAdvocatePartners(List<AdvocatePartners> advocatePartners) {
		this.advocatePartners = advocatePartners;
	}
	
	
	
	
	
	
}
