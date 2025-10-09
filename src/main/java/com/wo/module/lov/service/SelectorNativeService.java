/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.service;

import java.util.List;

import org.primefaces.model.SortOrder;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.externalRegulation.model.Regulation;

/**
 *
 * @author hendra
 */
@SuppressWarnings("rawtypes")
public interface SelectorNativeService extends RetrieverDataPage {
    
	List<Regulation> searchDataRegulation(List<? extends SearchObject> searchCriteria, int first, int pageSize,
            String sortField, SortOrder sortOrder) throws Exception ;

	Long searchCountDataRegulation(List<? extends SearchObject> searchCriteria) throws Exception;
}
