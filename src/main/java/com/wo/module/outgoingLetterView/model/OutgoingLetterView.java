package com.wo.module.outgoingLetterView.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

public class OutgoingLetterView extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4584173738817534458L;
	
	private Long outgoingLetterId;
	private String letterPurposeIn;
	private String letterPurposeEn;
	private String letterNo;
	private Date letterDate;
	private String perihalIn;
	private String perihalEn;
	private String deliveredTo;
	private String tembusan;
	
	private List<OutgoingLetterAttachmentView> outgoingLetterAttachmentView;
	
	// helper
	private String letterPursposeName;
	private String perihalName;
	private String letterDateStr;
	public Long getOutgoingLetterId() {
		return outgoingLetterId;
	}
	public void setOutgoingLetterId(Long outgoingLetterId) {
		this.outgoingLetterId = outgoingLetterId;
	}
	public String getLetterPurposeIn() {
		return letterPurposeIn;
	}
	public void setLetterPurposeIn(String letterPurposeIn) {
		this.letterPurposeIn = letterPurposeIn;
	}
	public String getLetterPurposeEn() {
		return letterPurposeEn;
	}
	public void setLetterPurposeEn(String letterPurposeEn) {
		this.letterPurposeEn = letterPurposeEn;
	}
	public String getLetterNo() {
		return letterNo;
	}
	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}
	public Date getLetterDate() {
		return letterDate;
	}
	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}
	public String getPerihalIn() {
		return perihalIn;
	}
	public void setPerihalIn(String perihalIn) {
		this.perihalIn = perihalIn;
	}
	public String getPerihalEn() {
		return perihalEn;
	}
	public void setPerihalEn(String perihalEn) {
		this.perihalEn = perihalEn;
	}
	public String getDeliveredTo() {
		return deliveredTo;
	}
	public void setDeliveredTo(String deliveredTo) {
		this.deliveredTo = deliveredTo;
	}
	public String getTembusan() {
		return tembusan;
	}
	public void setTembusan(String tembusan) {
		this.tembusan = tembusan;
	}
	public List<OutgoingLetterAttachmentView> getOutgoingLetterAttachmentView() {
		return outgoingLetterAttachmentView;
	}
	public void setOutgoingLetterAttachmentView(List<OutgoingLetterAttachmentView> outgoingLetterAttachmentView) {
		this.outgoingLetterAttachmentView = outgoingLetterAttachmentView;
	}
	@SuppressWarnings("static-access")
	public String getLetterPursposeName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale != null && locale.equals(locale.ENGLISH)) {
			letterPursposeName = letterPurposeEn;
		} else {
			letterPursposeName = letterPurposeIn;
		}
		return letterPursposeName;
	}
	public void setLetterPursposeName(String letterPursposeName) {
		this.letterPursposeName = letterPursposeName;
	}
	@SuppressWarnings("static-access")
	public String getPerihalName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale != null && locale.equals(locale.ENGLISH)) {
			perihalName = perihalEn;
		} else {
			perihalName = perihalIn;
		}
		return perihalName;
	}
	public void setPerihalName(String perihalName) {
		this.perihalName = perihalName;
	}
	public String getLetterDateStr() {
		return letterDateStr;
	}
	public void setLetterDateStr(String letterDateStr) {
		this.letterDateStr = letterDateStr;
	}
}