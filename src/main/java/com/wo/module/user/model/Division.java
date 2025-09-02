package com.wo.module.user.model;

import java.io.Serializable;
import java.util.Objects;

import com.wo.module.common.model.BaseEntity;

public class Division extends BaseEntity implements Serializable, Comparable<Division> {
	private static final long serialVersionUID = 3019374791490640764L;

	private Long divisionId;
	private String divisionName;

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(divisionId, divisionName);
	}
	
	@Override
	public String toString() {
		return divisionName;
	}
	
	@Override
	public int compareTo(Division o) {
		return divisionName.compareTo(o.divisionName);
	}

}