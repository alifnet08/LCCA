package com.wo.module.dashboard.vo;

import java.io.Serializable;

public class MapVO  implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private String province;
	
	private Integer pinLeft;
	
	private Integer pinTop;

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getProvince() {
		return province;
	}



	public void setProvince(String province) {
		this.province = province;
	}



	public Integer getPinLeft() {
		return pinLeft;
	}



	public void setPinLeft(Integer pinLeft) {
		this.pinLeft = pinLeft;
	}



	public Integer getPinTop() {
		return pinTop;
	}



	public void setPinTop(Integer pinTop) {
		this.pinTop = pinTop;
	}
	
	
	
}
