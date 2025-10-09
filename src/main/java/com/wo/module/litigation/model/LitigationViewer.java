package com.wo.module.litigation.model;

import java.io.Serializable;
import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class LitigationViewer extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long litigationViewerId;
	private Litigation litigation;
	
	private User user;
	private String status;
	
	private int sequence;
	
	public Long getLitigationViewerId() {
		return litigationViewerId;
	}
	public void setLitigationViewerId(Long litigationViewerId) {
		this.litigationViewerId = litigationViewerId;
	}
	public Litigation getLitigation() {
		return litigation;
	}
	public void setLitigation(Litigation litigation) {
		this.litigation = litigation;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	
	
	
	
	
}
