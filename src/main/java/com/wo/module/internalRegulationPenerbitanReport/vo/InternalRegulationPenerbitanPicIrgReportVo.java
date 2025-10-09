package com.wo.module.internalRegulationPenerbitanReport.vo;

import java.io.Serializable;

public class InternalRegulationPenerbitanPicIrgReportVo implements Serializable {

	private static final long serialVersionUID = 5261769965313553224L;

	private Long irgId;
	private Long irgPicId;

	private String picNik1;
	private String picName1;
	private String picNik2;
	private String picName2;

	public Long getIrgId() {
		return irgId;
	}

	public void setIrgId(Long irgId) {
		this.irgId = irgId;
	}

	public Long getIrgPicId() {
		return irgPicId;
	}

	public void setIrgPicId(Long irgPicId) {
		this.irgPicId = irgPicId;
	}

	public String getPicNik1() {
		return picNik1;
	}

	public void setPicNik1(String picNik1) {
		this.picNik1 = picNik1;
	}

	public String getPicName1() {
		return picName1;
	}

	public void setPicName1(String picName1) {
		this.picName1 = picName1;
	}

	public String getPicNik2() {
		return picNik2;
	}

	public void setPicNik2(String picNik2) {
		this.picNik2 = picNik2;
	}

	public String getPicName2() {
		return picName2;
	}

	public void setPicName2(String picName2) {
		this.picName2 = picName2;
	}

}