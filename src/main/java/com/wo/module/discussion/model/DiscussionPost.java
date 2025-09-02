package com.wo.module.discussion.model;

import java.io.Serializable;
import java.util.Date;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

public class DiscussionPost extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long discussionPostId;
	private Discussion discussion;
	private User postedBy;
	private Date postedDate;
	private String postedComment;
	private Double postedRating;
	
	public Long getDiscussionPostId() {
		return discussionPostId;
	}
	public void setDiscussionPostId(Long discussionPostId) {
		this.discussionPostId = discussionPostId;
	}
	public Discussion getDiscussion() {
		return discussion;
	}
	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}
	public User getPostedBy() {
		return postedBy;
	}
	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	public String getPostedComment() {
		return postedComment;
	}
	public void setPostedComment(String postedComment) {
		this.postedComment = postedComment;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Double getPostedRating() {
		return postedRating;
	}
	public void setPostedRating(Double postedRating) {
		this.postedRating = postedRating;
	}
	
	
	
	
	
	
	
	
}
