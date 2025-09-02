/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menu.model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

/**
 *
 * @author hendra
 */
public class Menu extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7311808953320426632L;
	private Long menuId;
	private String nameIn;
	private String nameEn;
	private String menuName;
	private String action;
	private Long parentId;	
	//private Menu parentId;	
	private String fontawesome;
	private String description;
	private Long menuLevel;
	private Long menuOrder;

	private List<Menu> detail;
	
	private String parentMenuNameIn;
	private String parentMenuNameEn;
	private String parentMenuName;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/*public Menu getParentId() {
		return parentId;
	}

	public void setParentId(Menu parentId) {
		this.parentId = parentId;
	}*/
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getParentId() {
		return parentId;
	}

	public String getNameIn() {
		return nameIn;
	}

	public void setNameIn(String nameIn) {
		this.nameIn = nameIn;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getFontawesome() {
		return fontawesome;
	}

	public void setFontawesome(String fontawesome) {
		this.fontawesome = fontawesome;
	}

	public Long getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Long menuLevel) {
		this.menuLevel = menuLevel;
	}

	public Long getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Long menuOrder) {
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

	public List<Menu> getDetail() {
		return detail;
	}

	public void setDetail(List<Menu> detail) {
		this.detail = detail;
	}

	@SuppressWarnings("static-access")
	public String getMenuName() {

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			menuName = nameEn;
		} else {
			menuName = nameIn;
		}

		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentMenuNameIn() {
		return parentMenuNameIn;
	}

	public void setParentMenuNameIn(String parentMenuNameIn) {
		this.parentMenuNameIn = parentMenuNameIn;
	}

	public String getParentMenuNameEn() {
		return parentMenuNameEn;
	}

	public void setParentMenuNameEn(String parentMenuNameEn) {
		this.parentMenuNameEn = parentMenuNameEn;
	}

	@SuppressWarnings("static-access")
	public String getParentMenuName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			parentMenuName = parentMenuNameEn;
		} else {
			parentMenuName = parentMenuNameIn;
		}
		
		return parentMenuName;
	}

	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}
	
	
	
	

}
