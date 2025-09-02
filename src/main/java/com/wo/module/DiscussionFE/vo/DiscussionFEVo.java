package com.wo.module.DiscussionFE.vo;

import java.io.Serializable;
import java.util.Date;

public class DiscussionFEVo implements Serializable{

	private static final long serialVersionUID = 8998979178090011180L;

	private Long discussionId;
	private String thread;
	private Long replies;
	private Long views;
	private String lastPostBy;
	private Date lastPostDate;
	private String lastPostDateStr;
	private String startby;
	private Date startDate;
	private String startDateStr;
	private Long rating;
	
	public Long getDiscussionId() {
		return discussionId;
	}
	public void setDiscussionId(Long discussionId) {
		this.discussionId = discussionId;
	}
	public String getThread() {
		return thread;
	}
	public void setThread(String thread) {
		this.thread = thread;
	}
	public Long getReplies() {
		return replies;
	}
	public void setReplies(Long replies) {
		this.replies = replies;
	}
	public Long getViews() {
		return views;
	}
	public void setViews(Long views) {
		this.views = views;
	}
	public String getLastPostBy() {
		return lastPostBy;
	}
	public void setLastPostBy(String lastPostBy) {
		this.lastPostBy = lastPostBy;
	}
	public Date getLastPostDate() {
		return lastPostDate;
	}
	public void setLastPostDate(Date lastPostDate) {
		this.lastPostDate = lastPostDate;
	}
	public String getLastPostDateStr() {
		return lastPostDateStr;
	}
	public void setLastPostDateStr(String lastPostDateStr) {
		this.lastPostDateStr = lastPostDateStr;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getStartby() {
		return startby;
	}
	public void setStartby(String startby) {
		this.startby = startby;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public Long getRating() {
		return rating;
	}
	public void setRating(Long rating) {
		this.rating = rating;
	}
}
