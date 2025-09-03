package com.wo.module.trcCorrespondence.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class TrcCrpdcReffLetter extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8774013826085818901L;

	private Long crpdcReffLetterId;

	private TrcCorrespondence trcCorrespondence;
	private TrcCorrespondence reffLetterCorrespondence;

	private int sequence;

	private String perihal;
	private String letterNo;
	private String lampiran;

	public Long getCrpdcReffLetterId() {
		return crpdcReffLetterId;
	}

	public void setCrpdcReffLetterId(Long crpdcReffLetterId) {
		this.crpdcReffLetterId = crpdcReffLetterId;
	}
	
	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
	}

	public TrcCorrespondence getReffLetterCorrespondence() {
		return reffLetterCorrespondence;
	}

	public void setReffLetterCorrespondence(TrcCorrespondence reffLetterCorrespondence) {
		this.reffLetterCorrespondence = reffLetterCorrespondence;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getPerihal() {
		return perihal;
	}

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getLampiran() {
		return lampiran;
	}

	public void setLampiran(String lampiran) {
		this.lampiran = lampiran;
	}

}