package com.wo.module.common.paging;

/*
 * to accomodate feature changes on searchVO. Currently searchVO only handles String value.
 * Since we are using jsf, value set to bean is no longer only string. it can be any object, such as date.
 * therefore we need generic interface to handle object, not only String.
 * new codes should use object instead of searchVO.
 */
		
public interface SearchObject <T>{
    String ALL_COLUMNS = "*";
    
	String getSearchColumn();
	
	T getSearchValue();
	
	boolean isEmpty();
	
	String getSearchValueAsString();
}
