package com.wo.module.counterType.model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

public class CounterType extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long counterTypeId;
	private String counterTypeIn;
	private String counterTypeEn;
	
	private String counterTypeName;

	private List<CounterTypeDtl> details;

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public String getCounterTypeIn() {
		return counterTypeIn;
	}

	public void setCounterTypeIn(String counterTypeIn) {
		this.counterTypeIn = counterTypeIn;
	}

	public String getCounterTypeEn() {
		return counterTypeEn;
	}

	public void setCounterTypeEn(String counterTypeEn) {
		this.counterTypeEn = counterTypeEn;
	}

	@SuppressWarnings("static-access")
	public String getCounterTypeName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			counterTypeName = counterTypeEn;
		} else {
			counterTypeName = counterTypeIn;
		}

		return counterTypeName;
	}

	public void setCounterTypeName(String counterTypeName) {
		this.counterTypeName = counterTypeName;
	}

	public List<CounterTypeDtl> getDetails() {
		return details;
	}

	public void setDetails(List<CounterTypeDtl> details) {
		this.details = details;
	}

}
