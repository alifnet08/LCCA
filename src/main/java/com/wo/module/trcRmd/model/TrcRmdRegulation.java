package com.wo.module.trcRmd.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.externalRegulation.model.RegulationMst;

public class TrcRmdRegulation extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;
	
	private Long rmdRegulationId;
	private TrcRmd trcRmd;

	//private Long regulationId;
	private RegulationMst regulationMst;
	
	private int sequence;

	public Long getRmdRegulationId() {
		return rmdRegulationId;
	}

	public void setRmdRegulationId(Long rmdRegulationId) {
		this.rmdRegulationId = rmdRegulationId;
	}

	/*public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}*/

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public RegulationMst getRegulationMst() {
		return regulationMst;
	}

	public void setRegulationMst(RegulationMst regulationMst) {
		this.regulationMst = regulationMst;
	}
	
	
}