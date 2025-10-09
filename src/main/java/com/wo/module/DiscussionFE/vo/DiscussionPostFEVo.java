package com.wo.module.DiscussionFE.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class DiscussionPostFEVo implements Serializable{

	private static final long serialVersionUID = 3338753395791120390L;
	
	private Long discussionPostId;
	private Long discussionId;
	private String postedByName;
	private Date postedDate;
	private String postedDateStr;
	private String postedComment;
	private Long postedRating;
	private Long postedById;
	
	private Date creationDate;
	
	// helper
	private String time;
	
	public Long getDiscussionPostId() {
		return discussionPostId;
	}
	public void setDiscussionPostId(Long discussionPostId) {
		this.discussionPostId = discussionPostId;
	}
	public Long getDiscussionId() {
		return discussionId;
	}
	public void setDiscussionId(Long discussionId) {
		this.discussionId = discussionId;
	}
	public String getPostedByName() {
		return postedByName;
	}
	public void setPostedByName(String postedByName) {
		this.postedByName = postedByName;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	public String getPostedDateStr() {
		return postedDateStr;
	}
	public void setPostedDateStr(String postedDateStr) {
		this.postedDateStr = postedDateStr;
	}
	public String getPostedComment() {
		return postedComment;
	}
	public void setPostedComment(String postedComment) {
		this.postedComment = postedComment;
	}
	public Long getPostedRating() {
		return postedRating;
	}
	public void setPostedRating(Long postedRating) {
		this.postedRating = postedRating;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getPostedById() {
		return postedById;
	}
	public void setPostedById(Long postedById) {
		this.postedById = postedById;
	}
	public String getTime() {
		try {
			this.time = "";
			if (postedDate != null) {
				Date current = new Timestamp(new Date().getTime());
				
				Long diff = current.getTime() - creationDate.getTime();
				
				Long diffSeconds = diff / 1000 % 60;
				Long diffMinutes = diff / (60 * 1000) % 60;
				Long diffHours = diff / (60 * 60 * 1000) % 24;
				Long diffDays = diff / (24 * 60 * 60 * 1000);
				
				if (diffDays != null && diffDays > 0) {
					time = diffDays + " hari";
					if (diffHours != null && diffHours > 0) {
						time = diffDays + " hari " + diffHours + " jam"; 
						if (diffMinutes != null && diffMinutes > 0) {
							time = diffDays + " hari " + diffHours + " jam " + diffMinutes + " menit";
							if (diffSeconds != null && diffSeconds > 0) {
								time = diffDays + " hari " + diffHours + " jam " + diffMinutes + " menit " + diffSeconds + " detik";
							}
						}
					}
				} else {
					if (diffHours != null && diffHours > 0) {
						time = diffHours + " jam"; 
						if (diffMinutes != null && diffMinutes > 0) {
							time = diffHours + " jam " + diffMinutes + " menit";
							if (diffSeconds != null && diffSeconds > 0) {
								time = diffHours + " jam " + diffMinutes + " menit " + diffSeconds + " detik";
							}
						}
					} else {
						if (diffMinutes != null && diffMinutes > 0) {
							time = diffMinutes + " menit";
							if (diffSeconds != null && diffSeconds > 0) {
								time = diffMinutes + " menit " + diffSeconds + " detik";
							}
						} else {
							if (diffSeconds != null && diffSeconds > 0) {
								time = diffSeconds + " detik";
							}
						}
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			time = "";
		}
		
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
