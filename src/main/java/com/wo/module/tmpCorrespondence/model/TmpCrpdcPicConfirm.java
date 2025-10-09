package com.wo.module.tmpCorrespondence.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class TmpCrpdcPicConfirm extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -2868792882459045331L;
	
	private TmpCorrespondence tmpCorrespondence;
	private ParameterDetail statusPic;
	private ParameterDetail complianceStatus;
	
	private Long crpdcPicConfirmId;
	private Long divisionId;

	private String divisionName;
	
	private User user1;
	private User user2;
	private User user3;
	
	private Date targetDate;	
	
	private Integer sequence;
	
	private String userName1;
	private String userName2;
	private String userName3;
	
	private Long divisionIdTemp;
	
	private Boolean isEditableTemp;
	

	public Long getCrpdcPicConfirmId() {
		return crpdcPicConfirmId;
	}

	public void setCrpdcPicConfirmId(Long crpdcPicConfirmId) {
		this.crpdcPicConfirmId = crpdcPicConfirmId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getUserName1() {
		return userName1;
	}

	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}

	public String getUserName2() {
		return userName2;
	}

	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}

	public String getUserName3() {
		return userName3;
	}

	public void setUserName3(String userName3) {
		this.userName3 = userName3;
	}

	public Long getDivisionIdTemp() {
		return divisionIdTemp;
	}

	public void setDivisionIdTemp(Long divisionIdTemp) {
		this.divisionIdTemp = divisionIdTemp;
	}

	public ParameterDetail getStatusPic() {
		return statusPic;
	}

	public void setStatusPic(ParameterDetail statusPic) {
		this.statusPic = statusPic;
	}

	public ParameterDetail getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(ParameterDetail complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}
	
}