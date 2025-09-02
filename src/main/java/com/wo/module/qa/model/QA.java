package com.wo.module.qa.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class QA extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6395152164269025885L;
	private String ticketNo;
	private Long qnaId;
	private User qUser;
	private Date qDate;
	private String question;
	private User adminUser;
	private User aUser;
	private Timestamp aDate;
	private String answer;
	private String qStatus;
	private String slaType;
	private String categoryType;
	private String threadStatus;
	private String readFlag;
	private Integer faqFlag;
	
	private String qUserName;
	private String adminUserName;
	private String aUserName;
	private String qDateStr;
	private String aDateStr;
	
	private String qEmail;
	private String adminEmail;
	private String aEmail;
	private String pukEmail;
	
	private Long qUserId;
	private Long aUserId;
	private String qStatusStr;
	private String slaTypeStr;
	private String categoryTypeStr;
	
	private QA fromQnaId;
	private String qTitle;
	
	private User aUser2;
	private Timestamp aDate2;
	private String answer2;
	private User aUser3;
	private Timestamp aDate3;
	private String answer3;
	private String divisionName;
	private String title;
	
	private String keyword;
	private String publishQna;
	
	private List<QAKeyword> qaKeywords;
	private List<QAAttachment> qaAttachmentList;
	
	private String h;
	private String qName;
	private String statusNameIn;
	private String statusNameEn;
	private String status;
	private Boolean isPublish;
	private boolean isPublishTemp;
	private boolean isDisabledQna;
	private boolean isAnswer2Filled;
	private boolean isAnswer3Filled;

	public Long getQnaId() {
		return qnaId;
	}

	public void setQnaId(Long qnaId) {
		this.qnaId = qnaId;
	}

	public User getqUser() {
		return qUser;
	}

	public void setqUser(User qUser) {
		this.qUser = qUser;
	}

	public Date getqDate() {
		return qDate;
	}

	public void setqDate(Date qDate) {
		this.qDate = qDate;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public User getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(User adminUser) {
		this.adminUser = adminUser;
	}

	public User getaUser() {
		return aUser;
	}

	public void setaUser(User aUser) {
		this.aUser = aUser;
	}

	public Timestamp getaDate() {
		return aDate;
	}

	public void setaDate(Timestamp aDate) {
		this.aDate = aDate;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getqStatus() {
		return qStatus;
	}

	public void setqStatus(String qStatus) {
		this.qStatus = qStatus;
	}

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String slaType) {
		this.slaType = slaType;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getThreadStatus() {
		return threadStatus;
	}

	public void setThreadStatus(String threadStatus) {
		this.threadStatus = threadStatus;
	}

	public String getqUserName() {
		return qUserName;
	}

	public void setqUserName(String qUserName) {
		this.qUserName = qUserName;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	

	public String getaUserName() {
		return aUserName;
	}

	public void setaUserName(String aUserName) {
		this.aUserName = aUserName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFaqFlag() {
		return faqFlag;
	}

	public void setFaqFlag(Integer faqFlag) {
		this.faqFlag = faqFlag;
	}

	public String getqDateStr() {
		return qDateStr;
	}

	public void setqDateStr(String qDateStr) {
		this.qDateStr = qDateStr;
	}

	public String getaDateStr() {
		return aDateStr;
	}

	public void setaDateStr(String aDateStr) {
		this.aDateStr = aDateStr;
	}

	public List<QAKeyword> getQaKeywords() {
		return qaKeywords;
	}

	public void setQaKeywords(List<QAKeyword> qaKeywords) {
		this.qaKeywords = qaKeywords;
	}

	public String getqEmail() {
		return qEmail;
	}

	public void setqEmail(String qEmail) {
		this.qEmail = qEmail;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getaEmail() {
		return aEmail;
	}

	public void setaEmail(String aEmail) {
		this.aEmail = aEmail;
	}

	public String getPukEmail() {
		return pukEmail;
	}

	public void setPukEmail(String pukEmail) {
		this.pukEmail = pukEmail;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public Long getqUserId() {
		return qUserId;
	}

	public void setqUserId(Long qUserId) {
		this.qUserId = qUserId;
	}

	public String getqStatusStr() {
		return qStatusStr;
	}

	public void setqStatusStr(String qStatusStr) {
		this.qStatusStr = qStatusStr;
	}

	public Long getaUserId() {
		return aUserId;
	}

	public void setaUserId(Long aUserId) {
		this.aUserId = aUserId;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getSlaTypeStr() {
		return slaTypeStr;
	}

	public void setSlaTypeStr(String slaTypeStr) {
		this.slaTypeStr = slaTypeStr;
	}

	public String getCategoryTypeStr() {
		return categoryTypeStr;
	}

	public void setCategoryTypeStr(String categoryTypeStr) {
		this.categoryTypeStr = categoryTypeStr;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	

	public QA getFromQnaId() {
		return fromQnaId;
	}

	public void setFromQnaId(QA fromQnaId) {
		this.fromQnaId = fromQnaId;
	}

	public String getqTitle() {
		return qTitle;
	}

	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public User getaUser2() {
		return aUser2;
	}

	public void setaUser2(User aUser2) {
		this.aUser2 = aUser2;
	}

	public Timestamp getaDate2() {
		return aDate2;
	}

	public void setaDate2(Timestamp aDate2) {
		this.aDate2 = aDate2;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public User getaUser3() {
		return aUser3;
	}

	public void setaUser3(User aUser3) {
		this.aUser3 = aUser3;
	}

	public Timestamp getaDate3() {
		return aDate3;
	}

	public void setaDate3(Timestamp aDate3) {
		this.aDate3 = aDate3;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getqName() {
		return qName;
	}

	public void setqName(String qName) {
		this.qName = qName;
	}

	public String getStatusNameIn() {
		return statusNameIn;
	}

	public void setStatusNameIn(String statusNameIn) {
		this.statusNameIn = statusNameIn;
	}

	public String getStatusNameEn() {
		return statusNameEn;
	}

	public void setStatusNameEn(String statusNameEn) {
		this.statusNameEn = statusNameEn;
	}

	@SuppressWarnings("static-access")
	public String getStatus() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			status = statusNameEn;
		} else {
			status = statusNameIn;
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPublishQna() {
		return publishQna;
	}

	public void setPublishQna(String publishQna) {
		this.publishQna = publishQna;
	}

	public List<QAAttachment> getQaAttachmentList() {
		return qaAttachmentList;
	}

	public void setQaAttachmentList(List<QAAttachment> qaAttachmentList) {
		this.qaAttachmentList = qaAttachmentList;
	}

	public Boolean getIsPublish() {
		if (StringUtils.isEmpty(publishQna)) {
			isPublish = false;
		} else {
			if (publishQna.equals(Constants.CONSTANT_NO)) {
				isPublish = false;
			} else {
				isPublish =  true;
			}
		}
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	public Boolean getIsPublishTemp() {
		return isPublishTemp;
	}

	public void setIsPublishTemp(Boolean isPublishTemp) {
		this.isPublishTemp = isPublishTemp;
	}

	public boolean isDisabledQna() {
		return isDisabledQna;
	}

	public void setDisabledQna(boolean isDisabledQna) {
		this.isDisabledQna = isDisabledQna;
	}

	public boolean isAnswer2Filled() {
		return isAnswer2Filled;
	}

	public void setAnswer2Filled(boolean isAnswer2Filled) {
		this.isAnswer2Filled = isAnswer2Filled;
	}

	public boolean isAnswer3Filled() {
		return isAnswer3Filled;
	}

	public void setAnswer3Filled(boolean isAnswer3Filled) {
		this.isAnswer3Filled = isAnswer3Filled;
	}
	
	
	
}
