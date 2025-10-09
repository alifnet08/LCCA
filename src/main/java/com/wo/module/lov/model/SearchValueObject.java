package com.wo.module.lov.model;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.paging.SearchObject;

public class SearchValueObject <T> implements SearchObject<T>{
	private String searchColumn;
	private T searchValueObject;
	
	public SearchValueObject(String searchColumn, T searchValueObject) {
		this.searchColumn = searchColumn;
		this.searchValueObject = searchValueObject;
	}
	
	@Override
	public String getSearchColumn() {		
		return searchColumn;
	}
	
	@Override
	public T getSearchValue() {
		return searchValueObject;
	}
	
	@Override
	public boolean isEmpty() {
		return StringUtils.isBlank(searchColumn);
	}

	@Override
	public String getSearchValueAsString() {
		
		return searchValueObject.toString();
	}

}
