package com.wo.module.trcRmd.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;

public class TrcRmdCorrespondence extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2549572530536488690L;
	
	private Long rmdCorrespondenceId;
	private TrcRmd trcRmd;
	
	private TrcCorrespondence trcCorrespondence;
	
	private int sequence;

	public Long getRmdCorrespondenceId() {
		return rmdCorrespondenceId;
	}

	public void setRmdCorrespondenceId(Long rmdCorrespondenceId) {
		this.rmdCorrespondenceId = rmdCorrespondenceId;
	}

	public TrcRmd getTrcRmd() {
		return trcRmd;
	}

	public void setTrcRmd(TrcRmd trcRmd) {
		this.trcRmd = trcRmd;
	}

	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}