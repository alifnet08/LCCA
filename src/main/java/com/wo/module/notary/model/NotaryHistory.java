package com.wo.module.notary.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;

public class NotaryHistory extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long notaryHistoryId;
	private Notary notary;
	private String status;
	private String catatanRevisi;

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

}
