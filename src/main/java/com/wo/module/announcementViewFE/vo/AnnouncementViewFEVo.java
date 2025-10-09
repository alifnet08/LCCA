package com.wo.module.announcementViewFE.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnnouncementViewFEVo implements Serializable{

	private static final long serialVersionUID = 6930973366658093657L;
	
	private Long announcementId;
	private String announcementTitle;
	private Date periodStart;
	private Date periodEnd;
	private String announcementContent;
	
	// helper
	private String periodStartStr;
	private String periodEndStr;
	private String periode;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPeriode() {
		if (periodStart != null) {
			periode = sdf.format(periodStart);
			if (periodEnd != null) {
				periode = sdf.format(periodStart).concat(" - ").concat(sdf.format(periodEnd));
			}
		} else {
			if (periodEnd != null) {
				periode = sdf.format(periodEnd);
			}
		}
		return periode;
	}
	public void setPeriode(String periode) {
		this.periode = periode;
	}
}
