package com.wo.module.log.dao;

import java.util.List;

import org.primefaces.model.SortOrder;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.log.model.LogHeader;

public interface LogDAO extends GenericDAO<LogHeader, Long> {
	@SuppressWarnings("rawtypes")
	Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception;

	@SuppressWarnings("rawtypes")
	List<LogHeader> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception;

}