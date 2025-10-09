/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.dao;


import java.util.List;
import org.primefaces.model.SortOrder;

import com.wo.module.common.paging.SearchObject;



/**
 *
 * @author hendra
 */
public interface SelectorDao {
    
    @SuppressWarnings("rawtypes")
	public List searchData(
            List <? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder);
    public Long searchCountData(@SuppressWarnings("rawtypes") List <? extends SearchObject> searchCriteria);

    @SuppressWarnings("rawtypes")
	public List searchDataUsingNative(
            List <? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder);
    public Long searchCountDataUsingNative(@SuppressWarnings("rawtypes") List <? extends SearchObject> searchCriteria);
}
