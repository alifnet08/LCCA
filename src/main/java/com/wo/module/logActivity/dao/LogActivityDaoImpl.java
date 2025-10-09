package com.wo.module.logActivity.dao;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.logActivity.model.LogActivity;

@Repository("logActivityDao")
public class LogActivityDaoImpl extends GenericDAOHibernate<LogActivity, Long> implements LogActivityDao {

	@Override
	public List<LogActivity> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
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
