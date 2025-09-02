package com.wo.module.tmpRmd.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;

public class TmpRmdCorrespondence extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3019374791490640764L;
	
	private Long rmdCorrespondenceId;
	private TmpRmd tmpRmd;

	//private Long regulationId;
	private TrcCorrespondence trcCorrespondence;
	
	private int sequence;

	

	public TmpRmd getTmpRmd() {
		return tmpRmd;
	}

	public void setTmpRmd(TmpRmd tmpRmd) {
		this.tmpRmd = tmpRmd;
	}

	/*public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}*/

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Long getRmdCorrespondenceId() {
		return rmdCorrespondenceId;
	}

	public void setRmdCorrespondenceId(Long rmdCorrespondenceId) {
		this.rmdCorrespondenceId = rmdCorrespondenceId;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
	}

	

	
	
}