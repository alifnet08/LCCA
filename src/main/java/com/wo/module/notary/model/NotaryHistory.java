package com.wo.module.notary.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class NotaryHistory extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long notaryHistoryId;
	private Notary notary;
	private String status;
	private String catatanRevisi;
	private String perubahan;
	private String jenisPengajuan;
	private String notaryNo;
	private String actorLabel;

	public Long getNotaryHistoryId() {
		return notaryHistoryId;
	}

	public void setNotaryHistoryId(Long notaryHistoryId) {
		this.notaryHistoryId = notaryHistoryId;
	}

	public Notary getNotary() {
		return notary;
	}

	public void setNotary(Notary notary) {
		this.notary = notary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCatatanRevisi() {
		return catatanRevisi;
	}

	public void setCatatanRevisi(String catatanRevisi) {
		this.catatanRevisi = catatanRevisi;
	}

	public String getPerubahan() {
		return perubahan;
	}

	public void setPerubahan(String perubahan) {
		this.perubahan = perubahan;
	}

	public String getJenisPengajuan() {
		return jenisPengajuan;
	}

	public void setJenisPengajuan(String jenisPengajuan) {
		this.jenisPengajuan = jenisPengajuan;
	}

	public String getNotaryNo() {
		return notaryNo;
	}

	public void setNotaryNo(String notaryNo) {
		this.notaryNo = notaryNo;
	}

	public String getActorLabel() {
		return actorLabel;
	}

	public void setActorLabel(String actorLabel) {
		this.actorLabel = actorLabel;
	}

}
