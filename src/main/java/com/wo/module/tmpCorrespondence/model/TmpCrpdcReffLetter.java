package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class TmpCrpdcReffLetter extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 4754048736171815381L;

	private Long crpdcReffLetterId;
	
	private TmpCorrespondence tmpCorrespondence;
	private TmpCorrespondence reffLetterCorrespondence;
	
	private int sequence;
	
	private String perihal;
	private String letterNo;
	private String lampiran;
	
	private List<TmpReffDocument> reffDocumentList;

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Long getCrpdcReffLetterId() {
		return crpdcReffLetterId;
	}

	public void setCrpdcReffLetterId(Long crpdcReffLetterId) {
		this.crpdcReffLetterId = crpdcReffLetterId;
	}

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
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

	public TmpCorrespondence getReffLetterCorrespondence() {
		return reffLetterCorrespondence;
	}

	public void setReffLetterCorrespondence(TmpCorrespondence reffLetterCorrespondence) {
		this.reffLetterCorrespondence = reffLetterCorrespondence;
	}

	public List<TmpReffDocument> getReffDocumentList() {
		return reffDocumentList;
	}

	public void setReffDocumentList(List<TmpReffDocument> reffDocumentList) {
		this.reffDocumentList = reffDocumentList;
	}

	
}