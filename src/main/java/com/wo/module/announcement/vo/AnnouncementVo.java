package com.wo.module.announcement.vo;

import java.io.Serializable;
import java.util.Date;

public class AnnouncementVo implements Serializable{

	private static final long serialVersionUID = -3620234747803743239L;
	
	private Long announcementId;
	private String announcementTitle;
	private String period;
	private Date periodStart;
	private String periodStartStr;
	private Date periodEnd;
	private String periodEndStr;
	private String announcementContent;
	
	public Long getAnnouncementId() {
		return announcementId;
	}
	public void setAnnouncementId(Long announcementId) {
		this.announcementId = announcementId;
	}
	public String getAnnouncementTitle() {
		return announcementTitle;
	}
	public void setAnnouncementTitle(String announcementTitle) {
		this.announcementTitle = announcementTitle;
	}
	public Date getPeriodStart() {
		return periodStart;
	}
	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}
	public Date getPeriodEnd() {
		return periodEnd;
	}
	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}
	public String getAnnouncementContent() {
		return announcementContent;
	}
	public void setAnnouncementContent(String announcementContent) {
		this.announcementContent = announcementContent;
	}
	public String getPeriod() {
		if (periodStartStr != null && !periodStartStr.equals("")) {
			period = periodStartStr;
			if (periodEndStr != null && !periodEndStr.equals("")) {
				period = periodStartStr.concat(" - ").concat(periodEndStr);
			}
		} else {
			if (periodEndStr != null && !periodEndStr.equals("")) {
				period = periodEndStr;
			}			
		}
		
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPeriodStartStr() {
		return periodStartStr;
	}
	public void setPeriodStartStr(String periodStartStr) {
		this.periodStartStr = periodStartStr;
	}
	public String getPeriodEndStr() {
		return periodEndStr;
	}
	public void setPeriodEndStr(String periodEndStr) {
		this.periodEndStr = periodEndStr;
	}
	
}
