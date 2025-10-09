package com.wo.module.header.vo;

import java.io.Serializable;

public class NotificationDtlVo implements Serializable {
	
	private static final long serialVersionUID = -9115766225143688066L;
	private String link;
	private Long count;
	private String text;
	private String maxDate;
	
	public NotificationDtlVo(Long count, String maxDate, String link, String text) {
		this.count = count;
		this.maxDate = maxDate;
		this.link = link;
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

}