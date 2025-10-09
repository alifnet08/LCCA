/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.advocate.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.advocate.constant.AdvocateConstants;
import com.wo.module.advocate.model.Advocate;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

/**
 *
 * @author hendra
 */
@Repository("advocateDao")
public class AdvocateDaoImpl extends GenericDAOHibernate<Advocate, Long> implements AdvocateDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AdvocateDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(AdvocateConstants.SEARCH_BY_REGION, col)) {
							sb.append(" and UPPER(ct.region) LIKE UPPER(:region) ");
						
					}
					if (StringUtils.equals(AdvocateConstants.SEARCH_BY_CABANG, col)) {
						sb.append(" and UPPER(ct.branch) LIKE UPPER(:branch) ");
					
					}
					if (StringUtils.equals(AdvocateConstants.SEARCH_BY_NAMA_KANTOR, col)) {
						sb.append(" and UPPER(ct.advocate_name) LIKE UPPER(:advocatName) ");
					
					}
					
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(AdvocateConstants.SEARCH_BY_REGION, col)) {
						query.setParameter("region", "%" + val + "%");
					}
					if (StringUtils.equals(AdvocateConstants.SEARCH_BY_CABANG, col)) {
						query.setParameter("branch", "%" + val + "%");
					}
					if (StringUtils.equals(AdvocateConstants.SEARCH_BY_NAMA_KANTOR, col)) {
						query.setParameter("advocatName", "%" + val + "%");
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {

		Number results = searchCountDataCriteria(searchCriteria);
		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_mst_advocate ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Advocate> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Advocate> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Advocate> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.ADVOCATE_ID, ct.REGION ");
		sb.append(" from wo_mst_advocate ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ct.ADVOCATE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Advocate> vo = new ArrayList<Advocate>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Advocate data = new Advocate();

				Long advocatId = MathUtil.returnIdObjectToLong(obj[0]);
				data = findById(advocatId);
			
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
