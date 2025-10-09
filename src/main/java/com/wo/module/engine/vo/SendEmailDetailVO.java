package com.wo.module.engine.vo;

import java.io.Serializable;

public class SendEmailDetailVO implements Serializable {
	private static final long serialVersionUID = 687264384218735952L;

	private String divisionName;

	private String pic1Name;

	private String pic2Name;

	private String pic3Name;
	
	private String pic1Email;
	
	private String pic2Email;
	
	private String pic3Email;

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getPic1Name() {
		return pic1Name;
	}

	public void setPic1Name(String pic1Name) {
		this.pic1Name = pic1Name;
	}

	public String getPic2Name() {
		return pic2Name;
	}

	public void setPic2Name(String pic2Name) {
		this.pic2Name = pic2Name;
	}

	public String getPic3Name() {
		return pic3Name;
	}

	public void setPic3Name(String pic3Name) {
		this.pic3Name = pic3Name;
	}

	public String getPic1Email() {
		return pic1Email;
	}

	public void setPic1Email(String pic1Email) {
		this.pic1Email = pic1Email;
	}

	public String getPic2Email() {
		return pic2Email;
	}

	public void setPic2Email(String pic2Email) {
		this.pic2Email = pic2Email;
	}

	public String getPic3Email() {
		return pic3Email;
	}

	public void setPic3Email(String pic3Email) {
		this.pic3Email = pic3Email;
	}
}