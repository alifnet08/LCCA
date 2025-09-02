package com.wo.module.searchAllFE.vo;

import java.io.Serializable;

public class SearchAllFEVO  implements Serializable {
		
	private static final long serialVersionUID = 1031961290076567131L;

	private Long internalId;
	private String no;
	private String name;
	private String type;
	private String typeCode;
	private String status;
	
	public Long getInternalId() {
		return internalId;
	}
	public void setInternalId(Long internalId) {
		this.internalId = internalId;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
