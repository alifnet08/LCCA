package com.wo.module.internalRegulationObsolete.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.division.model.Division;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class InternalRegulationObsolete extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -2146664940442686691L;
	
	private Long internalRegulasiObsoleteId;
	private String judulObsolete;
	private ParameterDetail tipeRegulasiObsolete;
	private String nomorObsolete;
	private String infoObsolete;
	private Division publisherDivision;
	private String publisherDirectorateName;
	private Date tanggalObsolete;
	private ParameterDetail openCloseRegulationObsolete;
	
	private User picIrg1;
	private User picIrg2;
	
	private List<InternalRegulationObsoletePic> internalRegulationObsoletePicKonversis;
	
	
	//helper
	private Boolean isEditable;

	public Long getInternalRegulasiObsoleteId() {
		return internalRegulasiObsoleteId;
	}

	public void setInternalRegulasiObsoleteId(Long internalRegulasiObsoleteId) {
		this.internalRegulasiObsoleteId = internalRegulasiObsoleteId;
	}

	public String getJudulObsolete() {
		return judulObsolete;
	}

	public void setJudulObsolete(String judulObsolete) {
		this.judulObsolete = judulObsolete;
	}

	public ParameterDetail getTipeRegulasiObsolete() {
		return tipeRegulasiObsolete;
	}

	public void setTipeRegulasiObsolete(ParameterDetail tipeRegulasiObsolete) {
		this.tipeRegulasiObsolete = tipeRegulasiObsolete;
	}

	public String getNomorObsolete() {
		return nomorObsolete;
	}

	public void setNomorObsolete(String nomorObsolete) {
		this.nomorObsolete = nomorObsolete;
	}

	public String getInfoObsolete() {
		return infoObsolete;
	}

	public void setInfoObsolete(String infoObsolete) {
		this.infoObsolete = infoObsolete;
	}

	public Division getPublisherDivision() {
		return publisherDivision;
	}

	public void setPublisherDivision(Division publisherDivision) {
		this.publisherDivision = publisherDivision;
	}

	public String getPublisherDirectorateName() {
		return publisherDirectorateName;
	}

	public void setPublisherDirectorateName(String publisherDirectorateName) {
		this.publisherDirectorateName = publisherDirectorateName;
	}

	public Date getTanggalObsolete() {
		return tanggalObsolete;
	}

	public void setTanggalObsolete(Date tanggalObsolete) {
		this.tanggalObsolete = tanggalObsolete;
	}

	public List<InternalRegulationObsoletePic> getInternalRegulationObsoletePicKonversis() {
		return internalRegulationObsoletePicKonversis;
	}

	public void setInternalRegulationObsoletePicKonversis(
			List<InternalRegulationObsoletePic> internalRegulationObsoletePicKonversis) {
		this.internalRegulationObsoletePicKonversis = internalRegulationObsoletePicKonversis;
	}

	public User getPicIrg1() {
		return picIrg1;
	}

	public void setPicIrg1(User picIrg1) {
		this.picIrg1 = picIrg1;
	}

	public User getPicIrg2() {
		return picIrg2;
	}

	public void setPicIrg2(User picIrg2) {
		this.picIrg2 = picIrg2;
	}

	public ParameterDetail getOpenCloseRegulationObsolete() {
		return openCloseRegulationObsolete;
	}

	public void setOpenCloseRegulationObsolete(ParameterDetail openCloseRegulationObsolete) {
		this.openCloseRegulationObsolete = openCloseRegulationObsolete;
	}
	
	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	
}