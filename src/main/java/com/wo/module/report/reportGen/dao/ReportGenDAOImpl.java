package com.wo.module.report.reportGen.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportGenDAO")
public class ReportGenDAOImpl extends GenericDAOHibernate<ReportGen, Long> implements
		ReportGenDAO {
	
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@Override
	public List<ReportGen> searchData(
			List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		Criteria criteria = getSession().createCriteria(ReportGen.class);
		
		decorateCriteria(criteria, searchCriteria);
		/*
		if (StringUtils.isNotBlank(sortField)) {
			if (sortOrder) {
				criteria.addOrder(Order.asc(sortField));
			} else {
				criteria.addOrder(Order.desc(sortField));
			}
		}
		*/
		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize);
		
		criteria.addOrder(Order.desc("creationDate"));		
			
		return criteria.list();
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria)
			throws Exception {
		Criteria criteria = getSession().createCriteria(ReportGen.class);

		decorateCriteria(criteria, searchCriteria);

		Long results = (Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		return results.longValue();
	}

	@SuppressWarnings("rawtypes")
	private void decorateCriteria(Criteria criteria,
			List<? extends SearchObject> selectedOptions) throws Exception {

		criteria.add(Restrictions.eq("enabledFlag",
				CommonConstants.ENABLED_FLAG_TRUE));
		
		if (selectedOptions != null) {
			for (SearchObject selectedOption : selectedOptions) {
				if ((selectedOption != null) && !selectedOption.isEmpty()) {
					if (StringUtils.equals(
							selectedOption.getSearchColumn(), 
							CommonConstants.SEARCH_FILTER_BY_REPORT_CODE)) {
						criteria.add(Restrictions.eq(
								"reportGenReportName", 
								selectedOption.getSearchValueAsString()));
					} else if (StringUtils.equals(
							selectedOption.getSearchColumn(), 
							CommonConstants.SEARCH_FILTER_BY_NIK)) {
						criteria.add(Restrictions.eq(
								"reportGenNik", 
								selectedOption.getSearchValueAsString()));
					}
				}
			}
		}		
	}
	
	
}
