package com.wo.module.discussion.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;

public class Discussion extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long discussionId;
	private User threadInitiator;
	private User threadUser;
	private Date threadInitiatorDate;
	private String threadInitiatorComment;
	private String threadNameIn;
	private String threadNameEn;
	private String threadRating;
	private Date threadStartDate;
	private ParameterDetail threadType;
	private String threadContentIn;
	private String threadContentEn;
	private String threadCloseStatus;
	
	private String threadTypeStr;
	private Integer rating;
	private Long replies;
	private String lastPostBy;
	
	private List<DiscussionPost> discussionPosts;
	
	public Long getDiscussionId() {
		return discussionId;
	}
	public void setDiscussionId(Long discussionId) {
		this.discussionId = discussionId;
	}
	public User getThreadInitiator() {
		return threadInitiator;
	}
	public void setThreadInitiator(User threadInitiator) {
		this.threadInitiator = threadInitiator;
	}
	public User getThreadUser() {
		return threadUser;
	}
	public void setThreadUser(User threadUser) {
		this.threadUser = threadUser;
	}
	public Date getThreadInitiatorDate() {
		return threadInitiatorDate;
	}
	public void setThreadInitiatorDate(Date threadInitiatorDate) {
		this.threadInitiatorDate = threadInitiatorDate;
	}
	public String getThreadInitiatorComment() {
		return threadInitiatorComment;
	}
	public void setThreadInitiatorComment(String threadInitiatorComment) {
		this.threadInitiatorComment = threadInitiatorComment;
	}
	public String getThreadNameIn() {
		return threadNameIn;
	}
	public void setThreadNameIn(String threadNameIn) {
		this.threadNameIn = threadNameIn;
	}
	public String getThreadNameEn() {
		return threadNameEn;
	}
	public void setThreadNameEn(String threadNameEn) {
		this.threadNameEn = threadNameEn;
	}
	public String getThreadRating() {
		return threadRating;
	}
	public void setThreadRating(String threadRating) {
		this.threadRating = threadRating;
	}
	public Date getThreadStartDate() {
		return threadStartDate;
	}
	public void setThreadStartDate(Date threadStartDate) {
		this.threadStartDate = threadStartDate;
	}
	
	public String getThreadContentIn() {
		return threadContentIn;
	}
	public void setThreadContentIn(String threadContentIn) {
		this.threadContentIn = threadContentIn;
	}
	public String getThreadContentEn() {
		return threadContentEn;
	}
	public void setThreadContentEn(String threadContentEn) {
		this.threadContentEn = threadContentEn;
	}
	public String getThreadCloseStatus() {
		return threadCloseStatus;
	}
	public void setThreadCloseStatus(String threadCloseStatus) {
		this.threadCloseStatus = threadCloseStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ParameterDetail getThreadType() {
		return threadType;
	}
	public void setThreadType(ParameterDetail threadType) {
		this.threadType = threadType;
	}
	public String getThreadTypeStr() {
		return threadTypeStr;
	}
	public void setThreadTypeStr(String threadTypeStr) {
		this.threadTypeStr = threadTypeStr;
	}
	
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Long getReplies() {
		return replies;
	}
	public void setReplies(Long replies) {
		this.replies = replies;
	}
	public String getLastPostBy() {
		return lastPostBy;
	}
	public void setLastPostBy(String lastPostBy) {
		this.lastPostBy = lastPostBy;
	}
	public List<DiscussionPost> getDiscussionPosts() {
		return discussionPosts;
	}
	public void setDiscussionPosts(List<DiscussionPost> discussionPosts) {
		this.discussionPosts = discussionPosts;
	}
	
	
	
	
	
	
}
