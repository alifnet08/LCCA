package com.wo.module.cpsaPicLockHistory.dao;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsaPicLockHistory.model.CpsaPicLockHistory;

@Repository("cpsaPicLockHistoryDao")
public class CpsaPicLockHistoryDaoImpl extends GenericDAOHibernate<CpsaPicLockHistory, Long> implements CpsaPicLockHistoryDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2666242125117725244L;

	@Override
	public List<CpsaPicLockHistory> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
