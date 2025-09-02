package com.wo.module.common.vo;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.wo.module.common.constant.CommonSQLMapConstants;
import com.wo.module.common.paging.SearchObject;


public class SearchVO implements SearchObject<String>, Serializable {
	private static final long serialVersionUID = 1242722432573488471L;
	protected String searchColumn;
	protected String searchValue;
//	protected Object searchValueObject;
	
	public SearchVO() {
		this.searchColumn = "";
		this.searchValue = CommonSQLMapConstants.SQL_WILDCHAR;
	}
	
	
	public SearchVO(String searchColumn, String searchValue) {
		this.searchColumn = searchColumn;
		this.searchValue = searchValue;
		if (searchColumn != null && searchColumn.length() > 0) {
			if (searchValue == null || searchValue.trim().length() <= 0)
				this.searchValue = CommonSQLMapConstants.SQL_WILDCHAR;
			else
				this.searchValue = searchValue;
		}
		
	}
	
	
	public String toString() {
		StringBuffer strBuff = new StringBuffer();
        strBuff.append("[SOUT SEARCHCOLUMNVALUE]>>>>> ");
		try {
			Field[] fields = SearchVO.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				strBuff.append(fields[i].getName()).append(" = ").append(fields[i].get(this)).append(", ");
			}
		} catch (IllegalAccessException iae) {}
		return strBuff.toString();
	}

	public String getSearchColumn() {
		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public boolean isEmpty() {
		boolean emptyFlag;
		if (this.searchColumn != null && this.searchColumn.trim().equals("") && this.searchValue != null)
			emptyFlag = true;
		else 
			emptyFlag = false;
		
		return emptyFlag;
	}


	@Override
	public String getSearchValueAsString() {
		
		return searchValue;
	}
	
}