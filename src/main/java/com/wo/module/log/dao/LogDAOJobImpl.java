package com.wo.module.log.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortOrder;

import com.wo.module.common.dao.GenericDAOHibernateOther;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.log.model.LogHeader;

public class LogDAOJobImpl extends GenericDAOHibernateOther<LogHeader, Long> implements LogDAO {
	
	private static LogDAO thisCodeDAO;

	public static synchronized LogDAO getInstance() {
		if (thisCodeDAO == null) {
			thisCodeDAO = new LogDAOJobImpl();
		}
		return thisCodeDAO;
	}

	private LogDAOJobImpl() {
	}
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {

		Criteria criteria = getSession().createCriteria(LogHeader.class);

		decorateCriteria(criteria, searchCriteria);

		Integer results = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (results == null) {
			results = 0;
		}
		return results.longValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@Override
	public List<LogHeader> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		Criteria criteria = getSession().createCriteria(LogHeader.class);

		decorateCriteria(criteria, searchCriteria);
		if (StringUtils.isNotBlank(sortField)) {
			if (SortOrder.ASCENDING.equals(sortOrder)) {
				criteria.addOrder(Order.asc(sortField));
			} else {
				criteria.addOrder(Order.desc(sortField));
			}
		} else {
			criteria.addOrder(Order.asc("jobName"));
		}
		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}

	@SuppressWarnings("rawtypes")
	private void decorateCriteria(Criteria criteria, List<? extends SearchObject> selectedOptions) throws Exception {
		if (selectedOptions != null) {
			for (SearchObject selectedOption : selectedOptions) {
				if ((selectedOption != null) && !selectedOption.isEmpty()) {
					criteria.add(Restrictions.ilike(selectedOption.getSearchColumn(),
							selectedOption.getSearchValueAsString(), MatchMode.ANYWHERE));
				}
			}

		}
	}
}
