package com.wo.module.complianceTestingMockup.vo;

import java.io.Serializable;

public class AttachmentVO implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;

	
	private String id;
	private String name;
	private String size;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	
}