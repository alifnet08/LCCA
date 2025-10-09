package com.wo.module.log.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.log.model.LogHeader;

@Repository("logDAO")
public class LogDAOImpl extends GenericDAOHibernate<LogHeader, Long> implements LogDAO {
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {

		Criteria criteria = getSession().createCriteria(LogHeader.class);

		decorateCriteria(criteria, searchCriteria);

		Long results = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (results == null) {
			results = 0L;
		}
		return results;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@Override
	public List<LogHeader> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		Criteria criteria = getSession().createCriteria(LogHeader.class);

		decorateCriteria(criteria, searchCriteria);
//		if (StringUtils.isNotBlank(sortField)) {
//			if (SortOrder.ASCENDING.equals(sortOrder)) {
//				criteria.addOrder(Order.asc(sortField));
//			} else {
//				criteria.addOrder(Order.desc(sortField));
//			}
//		} else {
//			//criteria.addOrder(Order.asc("jobName"));
//		}
		criteria.addOrder(Order.desc("processId"));
		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}

	@SuppressWarnings("rawtypes")
	private void decorateCriteria(Criteria criteria, List<? extends SearchObject> selectedOptions) throws Exception {
		if (selectedOptions != null) {
			for (SearchObject selectedOption : selectedOptions) {
				if ((selectedOption != null) && !selectedOption.isEmpty()) {
					if(selectedOption.getSearchColumn().equals("searchProcessDateFrom"))					{
						criteria.add(Restrictions.ge("processDate",
								selectedOption.getSearchValue()));
					} else if(selectedOption.getSearchColumn().equals("searchProcessDateTo"))					{
						criteria.add(Restrictions.le("processDate",
								selectedOption.getSearchValue()));
					} else {
						criteria.add(Restrictions.ilike(selectedOption.getSearchColumn(),
							selectedOption.getSearchValueAsString(), MatchMode.ANYWHERE));
					}
				}
			}

		}
	}
}
