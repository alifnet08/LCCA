package com.wo.module.responsibility.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class Responsibility extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6395152164269025885L;
	private Long responsibilityId;
	private String name;

	private String description;
	
	private List<ResponsibilityDetail> details = new ArrayList<ResponsibilityDetail>();

	public Long getResponsibilityId() {
		return responsibilityId;
	}

	public void setResponsibilityId(Long responsibilityId) {
		this.responsibilityId = responsibilityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ResponsibilityDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ResponsibilityDetail> details) {
		this.details = details;
	}

}
