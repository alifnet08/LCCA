package com.wo.module.internalRegulationObsolete.model;

import java.io.Serializable;

/**
 * WARNING: 
 * 
 * THIS MODEL IS NO LONGER USED, GO AHEAD AND DELETE THIS FILE
 * 
 * THANK YOU VERY MUCH.
 * 
 * - Jovan Kaladina
 */

public class InternalRegulationTest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1081105160351910933L;
	
	private Long internalRegulationId;
	private String internalRegulationTitle;
	
	public Long getInternalRegulationId() {
		return internalRegulationId;
	}
	public void setInternalRegulationId(Long internalRegulationId) {
		this.internalRegulationId = internalRegulationId;
	}
	public String getInternalRegulationTitle() {
		return internalRegulationTitle;
	}
	public void setInternalRegulationTitle(String internalRegulationTitle) {
		this.internalRegulationTitle = internalRegulationTitle;
	}
}
