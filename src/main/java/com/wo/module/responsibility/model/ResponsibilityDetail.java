
package com.wo.module.responsibility.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.menu.model.Menu;

public class ResponsibilityDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6904164992369379399L;

	private Long responsibilityDtlId;
	private Responsibility responsibility;
	
	private Menu menu;

	
	public Long getResponsibilityDtlId() {
		return responsibilityDtlId;
	}

	public void setResponsibilityDtlId(Long responsibilityDtlId) {
		this.responsibilityDtlId = responsibilityDtlId;
	}

	public Responsibility getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(Responsibility responsibility) {
		this.responsibility = responsibility;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	

}
