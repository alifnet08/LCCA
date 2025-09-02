package com.wo.module.cpsa.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class CompliancePlanSelfAssessmentPic extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 196099225654842515L;

	private Long cpsaPicId;
	private Long divisionId;
	private Long delId;

	private Integer sequence;
	private Integer totalQuestion;
	private Integer totalAnswer;

	private String divisionName;
	private String note;
	private String cpsaNote;
	private String cpsaStatusTemp;
	private String sendEmailFlag;
	private String branchCode;
	private String branchName;

	private Date targetDate;
	private Date oldTargetDate;
	private Date approvalDate;
	private Date reviewDate;	

	private Boolean isEditableTemp;
	private Boolean isAddtableTemp;

	private User user1;
	private User user2;
	private User user3;
	private User userApproval;
		
	private CompliancePlanSelfAssessment cpsa;
	private ParameterDetail statusPic;
	private ParameterDetail cpsaStatus;
	private String approvalDateStr;
	private String lockFlag;
	private String userNikLock;
	private Boolean isCanLock;
	
	private boolean isDisabledStatusCompliance;
	private boolean isCanEdit;
	
	private List<CompliancePlanSelfAssessmentAnswer> cpsaAnswers;
	private List<CompliancePlanSelfAssessmentPicEmail> cpsaPicEmailList;

	public Long getCpsaPicId() {
		return cpsaPicId;
	}

	public void setCpsaPicId(Long cpsaPicId) {
		this.cpsaPicId = cpsaPicId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getDelId() {
		return delId;
	}

	public void setDelId(Long delId) {
		this.delId = delId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}	

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getOldTargetDate() {
		return oldTargetDate;
	}

	public void setOldTargetDate(Date oldTargetDate) {
		this.oldTargetDate = oldTargetDate;
	}

	public Boolean getIsEditableTemp() {
		return isEditableTemp;
	}

	public void setIsEditableTemp(Boolean isEditableTemp) {
		this.isEditableTemp = isEditableTemp;
	}

	public Boolean getIsAddtableTemp() {
		return isAddtableTemp;
	}

	public void setIsAddtableTemp(Boolean isAddtableTemp) {
		this.isAddtableTemp = isAddtableTemp;
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

	public CompliancePlanSelfAssessment getCpsa() {
		return cpsa;
	}

	public void setCpsa(CompliancePlanSelfAssessment cpsa) {
		this.cpsa = cpsa;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<CompliancePlanSelfAssessmentAnswer> getCpsaAnswers() {
		return cpsaAnswers;
	}

	public void setCpsaAnswers(List<CompliancePlanSelfAssessmentAnswer> cpsaAnswers) {
		this.cpsaAnswers = cpsaAnswers;
	}

	public ParameterDetail getCpsaStatus() {
		return cpsaStatus;
	}

	public void setCpsaStatus(ParameterDetail cpsaStatus) {
		this.cpsaStatus = cpsaStatus;
	}

	public Integer getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(Integer totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	public Integer getTotalAnswer() {
		return totalAnswer;
	}

	public void setTotalAnswer(Integer totalAnswer) {
		this.totalAnswer = totalAnswer;
	}

	public User getUserApproval() {
		return userApproval;
	}

	public void setUserApproval(User userApproval) {
		this.userApproval = userApproval;
	}

	public ParameterDetail getStatusPic() {
		return statusPic;
	}

	public void setStatusPic(ParameterDetail statusPic) {
		this.statusPic = statusPic;
	}

	public String getCpsaNote() {
		return cpsaNote;
	}

	public void setCpsaNote(String cpsaNote) {
		this.cpsaNote = cpsaNote;
	}

	public String getCpsaStatusTemp() {
		return cpsaStatusTemp;
	}

	public void setCpsaStatusTemp(String cpsaStatusTemp) {
		this.cpsaStatusTemp = cpsaStatusTemp;
	}

	public String getSendEmailFlag() {
		return sendEmailFlag;
	}

	public void setSendEmailFlag(String sendEmailFlag) {
		this.sendEmailFlag = sendEmailFlag;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getApprovalDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		
		if (approvalDate != null) {
			approvalDateStr = sdf.format(approvalDate);
		}
		
		return approvalDateStr;
	}

	public void setApprovalDateStr(String approvalDateStr) {
		this.approvalDateStr = approvalDateStr;
	}

	public boolean isDisabledStatusCompliance() {
		boolean flag = true;
		
		if (statusPic != null) {
			if (statusPic.getParameterDtlCode().equals("CPSA_APPROVED")) {
				flag = false;
			}
		}
		
		if (cpsaStatus != null) {
			if (cpsaStatus.getParameterDtlCode().equals("COMPLIANCE_OPEN")) {
				flag = false;
			}
		}
		
		return flag;
	}

	public void setDisabledStatusCompliance(boolean isDisabledStatusCompliance) {
		this.isDisabledStatusCompliance = isDisabledStatusCompliance;
	}

	public boolean isCanEdit() {
		boolean flag = false;
		
		if (cpsaStatus != null) {
			if (cpsaStatus.getParameterDtlCode().equals("COMPLIANCE_CLOSE")) {
				flag = true;
			}
		}
		return flag;
	}

	public void setCanEdit(boolean isCanEdit) {
		this.isCanEdit = isCanEdit;
	}

	public List<CompliancePlanSelfAssessmentPicEmail> getCpsaPicEmailList() {
		return cpsaPicEmailList;
	}

	public void setCpsaPicEmailList(List<CompliancePlanSelfAssessmentPicEmail> cpsaPicEmailList) {
		this.cpsaPicEmailList = cpsaPicEmailList;
	}

	public String getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public String getUserNikLock() {
		return userNikLock;
	}

	public void setUserNikLock(String userNikLock) {
		this.userNikLock = userNikLock;
	}

	public Boolean getIsCanLock() {
		return isCanLock;
	}

	public void setIsCanLock(Boolean isCanLock) {
		this.isCanLock = isCanLock;
	}
	
	

}