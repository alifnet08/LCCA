package com.wo.module.common.paging;

import java.util.List;

import org.primefaces.model.SortOrder;

public interface RetrieverDataPage <E>{

    @SuppressWarnings("rawtypes")
	List<E> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
                    String sortField, SortOrder sortOrder) throws Exception ;

    @SuppressWarnings("rawtypes")
	Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception;
 
}
