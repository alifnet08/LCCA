package com.wo.module.responsibility.vo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class ResponsibilityDtlVO implements Serializable {

	private static final long serialVersionUID = -7165038648716788188L;
	private Long responsibility_dtl_id;
	private Long responsibility_id;
	private Long menu_id;

	private String check2;
	private Boolean check;
	private String menu_name;
	private String responsibility_name;
	private String menu_action;
	private Long parent_id;
	private List<ResponsibilityDtlVO> childResponsibilityMenuList;
	private int childResponsibilityMenuListSize;
	private String menu_type;

	private String menu_name_in;
	private String menu_name_en;

	private int parentIndex;
	private int currentIndex;

	public ResponsibilityDtlVO() {
		// table_name = "BTPN_MST_RESPONSIBILITY_DTL";
		responsibility_dtl_id = new Long(0);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ResponsibilityDtlVO)) {
			return false;
		}
		ResponsibilityDtlVO other = (ResponsibilityDtlVO) obj;
		if ((this.menu_id == null && other.getMenu_id() != null)
				|| (this.menu_id != null && other.getMenu_id() == null)) {
			return false;
		}
		if ((this.responsibility_id == null && other.getResponsibility_id() != null)
				|| (this.responsibility_id != null && other.getResponsibility_id() == null)) {
			return false;
		}
		if (this.menu_id == null && other.getMenu_id() == null && this.responsibility_id == null
				&& other.getResponsibility_id() == null) {
			return true;
		}
		if (!this.menu_id.equals(other.getMenu_id())) {
			return false;
		}
		if (!this.responsibility_id.equals(other.getResponsibility_id())) {
			return false;
		}
		return true;
	}

	public String toString() {
		StringBuffer strBuff = new StringBuffer();
		// strBuff.append("["+ getTable_name() +"]>>>>> ");
		try {
			Field[] fields = ResponsibilityDtlVO.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++)
				strBuff.append(fields[i].getName()).append(" = ").append(fields[i].get(this)).append(", ");
		} catch (IllegalAccessException iae) {
		}
		return strBuff.toString();
	}

	public Long getResponsibility_id() {
		return responsibility_id;
	}

	public void setResponsibility_id(Long responsibilityId) {
		responsibility_id = responsibilityId;
	}

	public Long getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Long menuId) {
		menu_id = menuId;
	}

	@SuppressWarnings("static-access")
	public String getMenu_name() {

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			menu_name = menu_name_en;
		} else {
			menu_name = menu_name_in;
		}

		return menu_name;
	}

	public void setMenu_name(String menuName) {
		menu_name = menuName;
	}

	public String getResponsibility_name() {
		return responsibility_name;
	}

	public void setResponsibility_name(String responsibilityName) {
		responsibility_name = responsibilityName;
	}

	public String getMenu_type() {
		return menu_type;
	}

	public void setMenu_type(String menuType) {
		menu_type = menuType;
		if (menuType != null) {
			/*
			 * if (menuType.equals(MenuFieldsConstants.MENU_TYPE_FOLDER))
			 * setMenu_type2(false); else if
			 * (menuType.equals(MenuFieldsConstants.MENU_TYPE_TRANSACTION))
			 * setMenu_type2(true);
			 */
		}
	}

	public String getCheck2() {
		return check2;
	}

	public void setCheck2(String check2) {
		this.check2 = check2;
		setCheck(new Boolean(check2));
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public int getParentIndex() {
		return parentIndex;
	}

	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public Long getResponsibility_dtl_id() {
		return responsibility_dtl_id;
	}

	public void setResponsibility_dtl_id(Long responsibilityDtlId) {
		responsibility_dtl_id = responsibilityDtlId;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parentId) {
		parent_id = parentId;
	}

	public List<ResponsibilityDtlVO> getChildResponsibilityMenuList() {
		return childResponsibilityMenuList;
	}

	public void setChildResponsibilityMenuList(List<ResponsibilityDtlVO> childResponsibilityMenuList) {
		this.childResponsibilityMenuList = childResponsibilityMenuList;
		if (childResponsibilityMenuList != null) {
			setChildResponsibilityMenuListSize(childResponsibilityMenuList.size());
		}
	}

	public int getChildResponsibilityMenuListSize() {
		return childResponsibilityMenuListSize;
	}

	public void setChildResponsibilityMenuListSize(int childResponsibilityMenuListSize) {
		this.childResponsibilityMenuListSize = childResponsibilityMenuListSize;
	}

	public String getMenu_action() {
		return menu_action;
	}

	public void setMenu_action(String menuAction) {
		menu_action = menuAction;
	}

	public String getMenu_name_in() {
		return menu_name_in;
	}

	public void setMenu_name_in(String menu_name_in) {
		this.menu_name_in = menu_name_in;
	}

	public String getMenu_name_en() {
		return menu_name_en;
	}

	public void setMenu_name_en(String menu_name_en) {
		this.menu_name_en = menu_name_en;
	}

}