package com.wo.module.qaCategory.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class QACategory extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6395152164269025885L;
	
	private Long qnaCategoryMapId;
	private ParameterDetail qnaCategoryCode;
	private String categoryCodeStr;
	private User user;
	private String userName;
	
	private Integer sequence;
	
	public Long getQnaCategoryMapId() {
		return qnaCategoryMapId;
	}
	public void setQnaCategoryMapId(Long qnaCategoryMapId) {
		this.qnaCategoryMapId = qnaCategoryMapId;
	}
	public ParameterDetail getQnaCategoryCode() {
		return qnaCategoryCode;
	}
	public void setQnaCategoryCode(ParameterDetail qnaCategoryCode) {
		this.qnaCategoryCode = qnaCategoryCode;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCategoryCodeStr() {
		return categoryCodeStr;
	}
	public void setCategoryCodeStr(String categoryCodeStr) {
		this.categoryCodeStr = categoryCodeStr;
	}
}
