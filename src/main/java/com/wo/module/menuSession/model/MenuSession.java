/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menuSession.model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

/**
 *
 * @author hendra
 */
public class MenuSession implements Serializable {

	private static final long serialVersionUID = -7311808953320426632L;
	private Long menuId;
	private String menuNameIn;
	private String menuNameEn;
	private String menuName;
	private String menuAction;
	private Long parentId;
	private String fontawesome;
	private String description;
	private Integer menuLevel;
	private Integer menuOrder;

	private List<MenuSession> detail;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuAction() {
		return menuAction;
	}

	public void setMenuAction(String menuAction) {
		this.menuAction = menuAction;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getMenuNameIn() {
		return menuNameIn;
	}

	public void setMenuNameIn(String menuNameIn) {
		this.menuNameIn = menuNameIn;
	}

	public String getMenuNameEn() {
		return menuNameEn;
	}

	public void setMenuNameEn(String menuNameEn) {
		this.menuNameEn = menuNameEn;
	}

	public String getFontawesome() {
		return fontawesome;
	}

	public void setFontawesome(String fontawesome) {
		this.fontawesome = fontawesome;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public Integer getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MenuSession> getDetail() {
		return detail;
	}

	public void setDetail(List<MenuSession> detail) {
		this.detail = detail;
	}

	@SuppressWarnings("static-access")
	public String getMenuName() {

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			menuName = menuNameEn;
		} else {
			menuName = menuNameIn;
		}

		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	

}
